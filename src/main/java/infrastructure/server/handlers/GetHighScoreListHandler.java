package infrastructure.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.ports.incoming.RankingService;
import infrastructure.server.errors.HttpError;
import infrastructure.server.errors.HttpErrorTypes;
import infrastructure.server.handlers.mapper.RankingUserScoreMapper;
import infrastructure.server.handlers.model.HighScoreListDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static infrastructure.server.HttpUtils.getAndCleanPathVariables;
import static infrastructure.server.HttpUtils.sendResponse;

public class GetHighScoreListHandler implements HttpHandler {
    private static final String HTTP_METHOD_TYPE = "GET";

    public static final String PATH = "/highscorelist";
    private static final int ID_LEVEL_PATH_POSITION = 0;

    private final RankingService rankingService;

    public GetHighScoreListHandler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var httpError = isValid(exchange);
        if (httpError != null)
            sendResponse(exchange, httpError.toString().getBytes());

        var path = exchange.getRequestURI().getPath();
        var pathVariables = getAndCleanPathVariables(path);
        var idLevel = Integer.parseInt(pathVariables.get(ID_LEVEL_PATH_POSITION));

        var ranking = rankingService.getOrCreate(idLevel);
        var rankingUserScoresDto = RankingUserScoreMapper.map(new ArrayList<>(ranking.getRankingUserScores()));

        String response = new HighScoreListDto(rankingUserScoresDto).parse();
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseHeaders().put("Content-Type",  Collections.singletonList("text/csv"));
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private HttpError isValid(HttpExchange exchange) throws IOException {
        if (!HTTP_METHOD_TYPE.equals(exchange.getRequestMethod())) {
            var httpError = new HttpError("", HttpErrorTypes.METHOD_NOT_ALLOWED);
            exchange.sendResponseHeaders(HttpErrorTypes.METHOD_NOT_ALLOWED.getCode(), httpError.toString().length());
            return httpError;
        }
        return null;
    }
}
