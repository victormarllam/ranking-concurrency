package domain.services;

import domain.model.Ranking;
import domain.model.RankingUserScore;
import domain.ports.outcoming.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.TreeSet;

import static domain.RankingTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceImplTest {
    private Ranking ranking;
    @InjectMocks
    RankingServiceImpl tested;
    @Mock
    RankingRepository rankingRepository;

    @BeforeEach
    public void initializeEach() {
        ranking = new Ranking(LEVEL, ANY_MAX_TOP);
    }

    @Test
    void Given_ExistingRankingLevel_When_getOrCreate_Then_ExistingRankingIsReturned() {
        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        var ranking = tested.getOrCreate(LEVEL);

        assertThat(ranking).isNotNull();
        verify(rankingRepository, never()).merge(any());
    }

    @Test
    void Given_NonExistingRankingLevel_When_getOrCreate_Then_RankingIsCreatedAndReturned() {
        int expectedNumberOfInvocations = 1;
        when(rankingRepository.get(LEVEL)).thenReturn(null);

        var ranking = tested.getOrCreate(LEVEL);

        assertThat(ranking).isNotNull();
        verify(rankingRepository, times(expectedNumberOfInvocations)).merge(any());
    }

    @Test
    void Given_UserWithZeroScore_When_addRankingUserScore_Then_UserNotAdded() {
        var zeroScore = 0;
        var clonedRankingUserScores = new TreeSet<>(ranking.getRankingUserScores()).toArray();

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(new RankingUserScore(ID_USER, zeroScore, LEVEL));

        assertThat(ranking.getRankingUserScores().size()).isEqualTo(clonedRankingUserScores.length);
    }

    @Test
    void Given_UserWithoutTopScoreOutsideFullRanking_When_addRankingUserScore_Then_RankingIsUnmodified() {
        var lowerScore = 50;
        var userScore = 5;
        ranking.setLowerScore(lowerScore);
        fillRankingWithUsers(ranking);
        var clonedRankingUserScores = new TreeSet<>(ranking.getRankingUserScores()).toArray();

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(new RankingUserScore(ID_USER, userScore, LEVEL));

        assertThat(ranking.getRankingUserScores().size()).isEqualTo(clonedRankingUserScores.length);

        for (int index = 0; index < ranking.getRankingUserScores().size(); index++) {
            var clonedRankingUserScore = (RankingUserScore) clonedRankingUserScores[index];
            assertThat(ranking.getRankingUserScores()).contains(clonedRankingUserScore);
        }
    }

    @Test
    void Given_UserWithoutTopScoreOutsideNotFullRanking_When_addRankingUserScore_Then_UserIsAdded() {
        var lowerScore = 50;
        var userScore = 5;
        var userToAdd = new RankingUserScore(ID_USER, userScore, LEVEL);
        ranking.setLowerScore(lowerScore);

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(new RankingUserScore(ID_USER, userScore, LEVEL));

        assertThat(ranking.getRankingUserScores()).contains(userToAdd);
    }

    @Test
    void Given_UserWithTopScoreOutsideNotFullRanking_When_addRankingUserScore_Then_UserIsAdded() {
        var lowerScore = 50;
        var userScore = 500;
        ranking.setLowerScore(lowerScore);
        var userToAdd = new RankingUserScore(ID_USER, userScore, LEVEL);
        var rankingUserScores = ranking.getRankingUserScores();
        var oldSize = rankingUserScores.size();

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(userToAdd);

        assertThat(rankingUserScores.size()).isGreaterThan(oldSize);
        assertThat(rankingUserScores).contains(userToAdd);
    }

    @Test
    void Given_UserWithTopScoreOutsideFullRanking_When_addRankingUserScore_Then_UserIsAddedAndLowerUserIsDeleted() {
        var outsideRankingIdUser = 99;
        var userScore = 500;
        var rankingUserScoreToAdd = new RankingUserScore(outsideRankingIdUser, userScore, LEVEL);

        fillRankingWithUsers(ranking);
        var oldRankingUserScores = ranking.getRankingUserScores();
        var oldSize = oldRankingUserScores.size();
        var oldLowerUser = (RankingUserScore) oldRankingUserScores.toArray()
                [ranking.getRankingUserScores().size() - 1];
        ranking.setLowerScore(oldLowerUser.getScore());

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(rankingUserScoreToAdd);

        assertThat(ranking.getRankingUserScores().size()).isEqualTo(oldSize);
        assertThat(ranking.getRankingUserScores()).doesNotContain(oldLowerUser);
        assertThat(ranking.getRankingUserScores()).contains(rankingUserScoreToAdd);
    }

    @Test
    void Given_UserInsideRankingBeatingOwnScore_When_addRankingUserScore_Then_UserIsUpdated() {
        var oldScore = 10;
        var newScore = 100;
        ranking.setLowerScore(oldScore);
        var rankingUserScores = ranking.getRankingUserScores();
        var userInsideRanking = new RankingUserScore(ID_USER, oldScore, LEVEL);
        rankingUserScores.add(userInsideRanking);

        when(rankingRepository.get(LEVEL)).thenReturn(ranking);

        tested.addRankingUserScore(new RankingUserScore(ID_USER, newScore, LEVEL));

        var rankingUserScoresArray = rankingUserScores.toArray();
        var updatedUser = (RankingUserScore) rankingUserScoresArray[Arrays.binarySearch(rankingUserScoresArray,
                userInsideRanking)];

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getScore()).isGreaterThan(oldScore);
    }
}