package domain.services;

import domain.model.Ranking;
import domain.model.RankingUserScore;
import domain.ports.incoming.RankingService;
import domain.ports.outcoming.RankingRepository;

import java.util.Arrays;
import java.util.concurrent.locks.StampedLock;

import static domain.model.RankingUserScore.EMPTY_SCORE;

public class RankingServiceImpl implements RankingService {
    private static final int DEFAULT_MAX_TOP = 15;
    private final RankingRepository rankingRepository;
    private final StampedLock lock;

    public RankingServiceImpl(RankingRepository rankingRepository) {
        lock = new StampedLock();
        this.rankingRepository = rankingRepository;
    }

    @Override
    public Ranking getOrCreate(int level) {
        var ranking = rankingRepository.get(level);
        if (ranking != null)
            return ranking;
        ranking = new Ranking(level, DEFAULT_MAX_TOP);
        rankingRepository.merge(ranking);
        return ranking;
    }

    @Override
    public void addRankingUserScore(RankingUserScore incomingRankingUserScore) {
        var stamp = lock.tryOptimisticRead();
        try {
            var ranking = getOrCreate(incomingRankingUserScore.getLevel());
            var rankingUserScores = ranking.getRankingUserScores();

            while ((!ranking.isFull() ||
                    ranking.isTopScore(incomingRankingUserScore.getScore())) &&
                    incomingRankingUserScore.getScore() > EMPTY_SCORE) {

                var existingRankingUserScore = findRankingUserScore(ranking, incomingRankingUserScore);
                stamp = lock.tryConvertToWriteLock(stamp);
                if (stamp != 0) {
                    if (existingRankingUserScore != null && incomingRankingUserScore.isGreaterThan(existingRankingUserScore)) {
                        rankingUserScores.remove(existingRankingUserScore);
                    } else {
                        if (rankingUserScores.size() >= ranking.getMaxTop()) {
                            rankingUserScores.remove(rankingUserScores.last());
                        }
                    }
                    rankingUserScores.add(incomingRankingUserScore);
                    var lowerRankingUserScore = findRankingUserScore(ranking, rankingUserScores.last());
                    if (lowerRankingUserScore != null) ranking.setLowerScore(lowerRankingUserScore.getScore());
                    break;
                }
                stamp = lock.tryOptimisticRead();
                ranking = getOrCreate(incomingRankingUserScore.getLevel());
                rankingUserScores = ranking.getRankingUserScores();
            }
        } finally {
            lock.tryUnlockRead();
            lock.tryUnlockWrite();
        }
    }

//    @Override
//    public void addRankingUserScore(RankingUserScore incomingRankingUserScore) {
//        synchronized (this) {
//            var ranking = getOrCreate(incomingRankingUserScore.getLevel());
//            var rankingUserScores = ranking.getRankingUserScores();
//
//            if (rankingUserScores.size() >= ranking.getMaxTop() && !ranking.isTopScore(incomingRankingUserScore.getScore()))
//                return;
//
//            var existingRankingUserScore = findRankingUserScore(ranking, incomingRankingUserScore);
//            if (existingRankingUserScore != null && incomingRankingUserScore.isGreaterThan(existingRankingUserScore)) {
//                rankingUserScores.remove(existingRankingUserScore);
//            } else {
//                if (rankingUserScores.size() >= ranking.getMaxTop()) {
//                    rankingUserScores.remove(rankingUserScores.last());
//                }
//            }
//            rankingUserScores.add(incomingRankingUserScore);
//            var lowerRankingUserScore = findRankingUserScore(ranking, rankingUserScores.last());
//            if (lowerRankingUserScore != null) ranking.setLowerScore(lowerRankingUserScore.getScore());
//        }
//    }

    private static RankingUserScore findRankingUserScore(Ranking ranking, RankingUserScore rankingUserScore) {
        var rankingUserScores = ranking.getRankingUserScores();
        var rankingUserScoresArray = rankingUserScores.toArray();
        var index = Arrays.binarySearch(rankingUserScoresArray, rankingUserScore);
        return index < 0 ? null : (RankingUserScore) rankingUserScoresArray[index];
    }
}
