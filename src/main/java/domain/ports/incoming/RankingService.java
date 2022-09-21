package domain.ports.incoming;

import domain.model.Ranking;
import domain.model.RankingUserScore;

public interface RankingService {
    Ranking getOrCreate(int level);
    void addRankingUserScore(RankingUserScore rankingUserScore);
}
