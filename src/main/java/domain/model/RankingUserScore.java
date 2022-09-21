package domain.model;

import java.util.Objects;

public class RankingUserScore implements Comparable<RankingUserScore> {
    public static final int EMPTY_SCORE = 0;
    private final int idUser;
    private final int score;
    private final int level;

    public RankingUserScore(int idUser, int score, int level) {
        this.idUser = idUser;
        this.score = Math.max(score, EMPTY_SCORE);
        this.level = level;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, level);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        var comparedObject = (RankingUserScore) obj;
        return idUser == comparedObject.getIdUser() &&
                level == comparedObject.getLevel();
    }

    /**
     * Return values are reversed to sort in descending order.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is greater than, equal to, or less than the specified object.
     */
    @Override
    public int compareTo(RankingUserScore o) {
        if (this.equals(o)) return 0;

        if (this.isGreaterThan(o))
            return -1;
        else if (this.isLowerThan(o))
            return 1;

        return -1;
    }

    public boolean isGreaterThan(RankingUserScore rankingUserScore) {
        return this.score > rankingUserScore.getScore();
    }

    public boolean isLowerThan(RankingUserScore rankingUserScore) {
        return this.score < rankingUserScore.getScore();
    }

    @Override
    public String toString() {
        return "RankingUserScore{" +
                "idUser=" + idUser +
                ", score=" + score +
                ", level=" + level +
                '}';
    }

}
