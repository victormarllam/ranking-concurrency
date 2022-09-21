package infrastructure.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.model.RankingUserScore;
import domain.ports.incoming.RankingService;
import infrastructure.server.SessionKey;
import infrastructure.server.SessionKeyProvider;
import infrastructure.server.errors.HttpError;
import infrastructure.server.errors.HttpErrorTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static infrastructure.server.HttpUtils.*;

public class PostRankingUserScoreHandler implements HttpHandler {
    private static final String HTTP_METHOD_TYPE = "POST";
    public static final String PATH = "/score";
    private static final int ID_LEVEL_PATH_POSITION = 0;

    private final RankingService rankingService;

    public PostRankingUserScoreHandler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var queryParameters = getQueryParametersMap(exchange.getRequestURI().getQuery());
        var rawSessionKey = queryParameters.get("sessionKey");
        var sessionKey = SessionKeyProvider.getIfValid(rawSessionKey);
        var httpError = isValid(sessionKey, exchange);
        if (httpError != null)
            sendResponse(exchange, httpError.toString().getBytes());

        try {
            var path = exchange.getRequestURI().getPath();
            var pathVariables = getAndCleanPathVariables(path);
            var score = getScore(exchange);
            var idUser = sessionKey.getIdUser();
            var idLevel = Integer.parseInt(pathVariables.get(ID_LEVEL_PATH_POSITION));

            rankingService.addRankingUserScore(new RankingUserScore(idUser, score, idLevel));
            exchange.sendResponseHeaders(200, idLevel);
            OutputStream os = exchange.getResponseBody();
            os.close();
        }
        catch (NumberFormatException exception) {
            httpError = new HttpError(exception.getMessage(), HttpErrorTypes.BAD_REQUEST);
            exchange.sendResponseHeaders(HttpErrorTypes.BAD_REQUEST.getCode(), httpError.toString().length());
            sendResponse(exchange, httpError.toString().getBytes());
        }
    }

    private int getScore(HttpExchange exchange) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        var score = Integer.parseInt(bufferedReader.readLine());
        bufferedReader.close();
        inputStreamReader.close();

        return score;
    }

    private HttpError isValid(SessionKey sessionKey, HttpExchange exchange) throws IOException {
        if (sessionKey == null) {
            var httpError = new HttpError("Invalid SessionKey", HttpErrorTypes.UNAUTHORIZED);
            exchange.sendResponseHeaders(HttpErrorTypes.UNAUTHORIZED.getCode(), httpError.toString().length());
            return httpError;
        }

        if (!HTTP_METHOD_TYPE.equals(exchange.getRequestMethod())) {
            var httpError = new HttpError("", HttpErrorTypes.METHOD_NOT_ALLOWED);
            exchange.sendResponseHeaders(HttpErrorTypes.METHOD_NOT_ALLOWED.getCode(), httpError.toString().length());
            return httpError;
        }

        return null;
    }
}
