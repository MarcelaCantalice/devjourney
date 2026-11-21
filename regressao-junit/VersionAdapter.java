import game.Developer;
import game.exceptions.InsufficientXPException;
import java.util.Scanner;

interface VersionAdapter {
    double xp(Developer developer) throws Exception;
    void promote(Developer developer) throws InsufficientXPException;
    void girlfriendChallenge(Developer developer, Scanner scanner);
    void activateXpBoost(Developer developer, double multiplier, int duration);
    void useBoost(Developer developer);
    double xpMultiplier(Developer developer);
    int boostRemainingProjects(Developer developer);
}
