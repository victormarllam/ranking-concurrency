package domain;

import domain.model.Ranking;
import domain.model.RankingUserScore;

public class RankingTestUtils {
    public static final int ID_USER = 0;
    public static final int LEVEL = 1;
    public static final int ANY_MAX_TOP = 15;

    public static void fillRankingWithUsers(Ranking ranking) {
        var rankingUserScores = ranking.getRankingUserScores();
        for (var i = rankingUserScores.size(); i < ranking.getMaxTop(); i++) {
            rankingUserScores.add(new RankingUserScore(i, i, LEVEL));
        }
    }
}
