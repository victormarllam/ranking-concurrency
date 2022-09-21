package infrastructure.server.handlers.model;

import java.util.List;

public class HighScoreListDto {
    private static final String DELIMITER = ",";
    private List<RankingUserScoreDto> rankingUserScoresDto;

    public HighScoreListDto(List<RankingUserScoreDto> rankingUserScoresDto) {
        this.rankingUserScoresDto = rankingUserScoresDto;
    }

    public String parse() {
        StringBuilder stringBuilder = new StringBuilder();
        if (rankingUserScoresDto.isEmpty()) {
            return "";
        }
        rankingUserScoresDto.forEach(rankingUserScoreDto -> stringBuilder.append(rankingUserScoreDto.parse())
                .append(DELIMITER));
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(DELIMITER));
        return stringBuilder.toString();
    }
}
