package domain.ports.outcoming;

import domain.model.Ranking;

public interface RankingRepository {
    Ranking get(int level);
    void merge(Ranking ranking);
}
