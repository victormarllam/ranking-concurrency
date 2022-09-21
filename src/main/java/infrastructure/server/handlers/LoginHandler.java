package infrastructure.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import infrastructure.server.SessionKeyProvider;
import infrastructure.server.errors.HttpError;
import infrastructure.server.errors.HttpErrorTypes;

import java.io.IOException;
import java.util.List;

import static infrastructure.server.HttpUtils.getAndCleanPathVariables;
import static infrastructure.server.HttpUtils.sendResponse;

public class LoginHandler implements HttpHandler {
    private static final String HTTP_METHOD_TYPE = "GET";
    private static final int ID_USER_PATH_POSITION = 0;

    public static final String PATH = "/login";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath();
        List<String> pathVariables = getAndCleanPathVariables(path);
        var httpError = isValid(pathVariables, exchange);
        if (httpError != null)
            sendResponse(exchange, httpError.toString().getBytes());

        var idUser = Integer.parseInt(pathVariables.get(ID_USER_PATH_POSITION));
        var sessionKey = SessionKeyProvider.createAndGetSessionKey(idUser);

        exchange.sendResponseHeaders(200, sessionKey.length());
        sendResponse(exchange, sessionKey.getBytes());
    }

    private HttpError isValid(List<String> pathVariables, HttpExchange exchange) throws IOException {
        try {
            if (!HTTP_METHOD_TYPE.equals(exchange.getRequestMethod())) {
                var httpError = new HttpError("", HttpErrorTypes.METHOD_NOT_ALLOWED);
                exchange.sendResponseHeaders(HttpErrorTypes.METHOD_NOT_ALLOWED.getCode(), httpError.toString().length());
                return httpError;
            }
            var idUser = Integer.parseInt(pathVariables.get(ID_USER_PATH_POSITION));
            if (idUser < 0)  {
                var httpError = new HttpError("No negative idUser allowed", HttpErrorTypes.BAD_REQUEST);
                exchange.sendResponseHeaders(HttpErrorTypes.BAD_REQUEST.getCode(), httpError.toString().length());
                return httpError;
            }
        } catch (NumberFormatException exception) {
            var httpError = new HttpError(exception.getMessage(), HttpErrorTypes.BAD_REQUEST);
            exchange.sendResponseHeaders(HttpErrorTypes.BAD_REQUEST.getCode(), httpError.toString().length());
            return httpError;
        }

        return null;
    }
}
