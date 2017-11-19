package younkyulee.android.com.nosports;

/**
 * Created by Younkyu on 2017-11-04.
 */

public class Match {

    public String firebaseKey;
    private String homeTeam;
    private String awayTeam;
    private String awayTeamCode;
    private String homeTeamCode;
    private String matchUrl;
    private String matchTime;
    private String matchLabel;
    private String matchRound;
    private String hit;

    public String getAwayTeamCode() {
        return awayTeamCode;
    }

    public void setAwayTeamCode(String awayTeamCode) {
        this.awayTeamCode = awayTeamCode;
    }

    public String getHomeTeamCode() {
        return homeTeamCode;
    }

    public void setHomeTeamCode(String homeTeamCode) {
        this.homeTeamCode = homeTeamCode;
    }

    public String getMatchRound() {
        return matchRound;
    }

    public void setMatchRound(String matchRound) {
        this.matchRound = matchRound;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchLabel() {
        return matchLabel;
    }

    public void setMatchLabel(String matchLabel) {
        this.matchLabel = matchLabel;
    }

    public String getMatchUrl() {
        return matchUrl;
    }

    public void setMatchUrl(String matchUrl) {
        this.matchUrl = matchUrl;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
}
