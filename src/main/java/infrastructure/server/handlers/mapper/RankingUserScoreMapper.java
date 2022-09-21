package infrastructure.server.handlers.mapper;

import domain.model.RankingUserScore;
import infrastructure.server.handlers.model.RankingUserScoreDto;

import java.util.List;
import java.util.stream.Collectors;

public class RankingUserScoreMapper {
    public static List<RankingUserScoreDto> map(List<RankingUserScore> rankingUserScores) {
        return rankingUserScores.stream()
                .map(RankingUserScoreMapper::map)
                .collect(Collectors.toList());
    }

    public static RankingUserScoreDto map(RankingUserScore rankingUserScore) {
        return new RankingUserScoreDto(rankingUserScore.getIdUser(), rankingUserScore.getScore());
    }
}
