package domain.model;

import java.util.*;

public class Ranking {
    private int level;
    private int maxTop;
    private int lowerScore;

    private final NavigableSet<RankingUserScore> rankingUserScores;

    public Ranking(int level, int maxTop) {
        this.level = level;
        this.maxTop = maxTop;
        this.rankingUserScores = new TreeSet<>();

        this.lowerScore = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxTop() {
        return maxTop;
    }

    public NavigableSet<RankingUserScore> getRankingUserScores() {
        return rankingUserScores;
    }

    public void setLowerScore(int lowerScore) {
        this.lowerScore = lowerScore;
    }

    /**
     * Checks if a User Score could be inside the Ranking.
     *
     * @param userScore User Score.
     * @return true if user score can be inside the Ranking, false otherwise.
     */
    public boolean isTopScore(int userScore) {
        return userScore > lowerScore;
    }

    public boolean isFull() {
        return rankingUserScores.size() >= maxTop;
    }
}
