package infrastructure;

import domain.model.Ranking;
import domain.ports.outcoming.RankingRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRankingDao implements RankingRepository {
    private static final Map<Integer, Ranking> rankings = new HashMap<>();

    @Override
    public Ranking get(int level) {
        return rankings.get(level);
    }
    @Override
    public void merge(Ranking ranking) {
        rankings.put(ranking.getLevel(), ranking);
    }
}
