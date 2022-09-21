package infrastructure.server;

import com.sun.net.httpserver.HttpServer;
import domain.ports.incoming.RankingService;
import infrastructure.server.handlers.RoutingHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyHttpServer {
    private static final int DEFAULT_PORT = 8000;
    public MyHttpServer(RankingService rankingService, Integer port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port != null ? port : DEFAULT_PORT), 0);
        server.createContext("/", new RoutingHandler(rankingService));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
