package configurationAdapters;

import domain.ports.incoming.RankingService;
import domain.ports.outcoming.RankingRepository;
import domain.services.RankingServiceImpl;
import infrastructure.InMemoryRankingDao;
import infrastructure.server.MyHttpServer;

import java.io.IOException;

public class MiniGameRanking {
    private static final RankingRepository rankingRepository = new InMemoryRankingDao();
    private static final RankingService rankingService = new RankingServiceImpl(rankingRepository);

    public static void main(String[] args) throws IOException {
        new MyHttpServer(rankingService, null);
    }
}
