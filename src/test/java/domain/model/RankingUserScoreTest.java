package domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static domain.RankingTestUtils.ID_USER;
import static domain.RankingTestUtils.LEVEL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RankingUserScoreTest {
    private static final int OTHER_USER = 2;
    private static final int LOWER_SCORE = 3;
    private static final int EQUAL_SCORE = 5;
    private static final int GREATER_SCORE = 10;

    @Test
    void Given_LowerScore_When_isGreaterThan_Then_TrueIsReturned() {
        var rankingUserScore = new RankingUserScore(ID_USER, EQUAL_SCORE, LEVEL);

        assertThat(rankingUserScore.isGreaterThan(new RankingUserScore(OTHER_USER, LOWER_SCORE, LEVEL))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {GREATER_SCORE, EQUAL_SCORE})
    void Given_GreaterOrEqualScore_When_isGreaterThan_Then_FalseIsReturned() {
        var rankingUserScore = new RankingUserScore(ID_USER, EQUAL_SCORE, LEVEL);

        assertThat(rankingUserScore.isGreaterThan(new RankingUserScore(OTHER_USER, GREATER_SCORE, LEVEL))).isFalse();
    }

    @Test
    void Given_GreaterScore_When_isLowerThan_Then_TrueIsReturned() {
        var rankingUserScore = new RankingUserScore(ID_USER, EQUAL_SCORE, LEVEL);

        assertThat(rankingUserScore.isLowerThan(new RankingUserScore(OTHER_USER, GREATER_SCORE, LEVEL))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {LOWER_SCORE, EQUAL_SCORE})
    void Given_LowerOrEqualScore_When_isLowerThan_Then_FalseIsReturned(int score) {
        var rankingUserScore = new RankingUserScore(ID_USER, EQUAL_SCORE, LEVEL);

        assertThat(rankingUserScore.isLowerThan(new RankingUserScore(OTHER_USER, score, LEVEL))).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {LOWER_SCORE, EQUAL_SCORE, GREATER_SCORE})
    void Given_LowerAndEqualAndGreaterScore_When_compareTo_Then_MinusOne_Then_MinusOne_Then_OneIsReturned(int score) {
        var rankingUserScore = new RankingUserScore(ID_USER, EQUAL_SCORE, LEVEL);

        switch(score) {
            case LOWER_SCORE:
            case EQUAL_SCORE:
                assertThat(rankingUserScore.
                        compareTo(new RankingUserScore(OTHER_USER, score, LEVEL))).isEqualTo(-1);
                break;
            case GREATER_SCORE:
                assertThat(rankingUserScore.
                        compareTo(new RankingUserScore(OTHER_USER, score, LEVEL))).isOne();
                break;
        }
    }
}