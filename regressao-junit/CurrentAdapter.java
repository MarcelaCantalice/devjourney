import game.Developer;
import game.exceptions.InsufficientXPException;
import game.service.PromotionService;
import game.challenges.GirlfriendChallenge;
import java.util.Scanner;

class CurrentAdapter implements VersionAdapter {
    public double xp(Developer developer) { return developer.getXp(); }
    public void promote(Developer developer) throws InsufficientXPException {
        PromotionService.attemptPromotion(developer);
    }
    public void girlfriendChallenge(Developer developer, Scanner scanner) {
        new GirlfriendChallenge().execute(developer, scanner);
    }
    public void activateXpBoost(Developer developer, double multiplier, int duration) {
        developer.activateXpBoost(multiplier, duration);
    }
    public void useBoost(Developer developer) { developer.useBoost(); }
    public double xpMultiplier(Developer developer) { return developer.getXpMultiplier(); }
    public int boostRemainingProjects(Developer developer) { return developer.getBoostRemainingProjects(); }
}
