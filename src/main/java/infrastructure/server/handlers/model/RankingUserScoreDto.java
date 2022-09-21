package infrastructure.server.handlers.model;

public class RankingUserScoreDto {
    private int idUser;
    private int score;

    public RankingUserScoreDto(int idUser, int score) {
        this.idUser = idUser;
        this.score = score;
    }

    public String parse() {
        return String.join("=", Integer.toString(idUser), Integer.toString(score));
    }
}
