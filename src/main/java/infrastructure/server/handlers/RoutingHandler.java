package infrastructure.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.ports.incoming.RankingService;

import java.io.IOException;

public class RoutingHandler implements HttpHandler {
    private final RankingService rankingService;

    public RoutingHandler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI().getPath();

        if (path.contains(LoginHandler.PATH)){
            new LoginHandler().handle(exchange);
        } else if (path.contains(PostRankingUserScoreHandler.PATH)) {
            new PostRankingUserScoreHandler(rankingService).handle(exchange);
        } else if (path.contains(GetHighScoreListHandler.PATH)) {
            new GetHighScoreListHandler(rankingService).handle(exchange);
        }
    }
}
