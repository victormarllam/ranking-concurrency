package domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static domain.RankingTestUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RankingTest {
    private static final int MAX_TOP = 15;
    private static final int LOWER_SCORE = 5;
    private Ranking ranking;

    @BeforeEach
    public void initializeEach() {
        ranking = new Ranking(LEVEL, MAX_TOP);
    }

    @Test
    void Given_FullRanking_When_isFull_Then_TrueIsReturned() {
        fillRankingWithUsers(ranking);
        assertThat(ranking.isFull()).isTrue();
    }

    @Test
    void Given_RankingUserScoresLowerThanMaxTop_When_isFull_Then_TrueIsReturned() {
        assertThat(ranking.isFull()).isFalse();
    }


    @ParameterizedTest
    @ValueSource(ints = {1, LOWER_SCORE})
    void Given_LowerOrEqualScore_When_isTopScore_Then_FalseIsReturned(int score) {
        ranking.setLowerScore(LOWER_SCORE);

        assertThat(ranking.isTopScore(score)).isFalse();
    }

    @Test
    void Given_GreaterScore_When_isTopScore_Then_TrueIsReturned() {
        var greaterScore = 100;
        assertThat(ranking.isTopScore(greaterScore)).isTrue();
    }

}