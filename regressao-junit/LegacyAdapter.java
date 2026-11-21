import game.Developer;
import game.exceptions.InsufficientXPException;
import java.lang.reflect.Field;
import java.util.Scanner;

class LegacyAdapter implements VersionAdapter {
    public double xp(Developer developer) throws Exception {
        Field field = Developer.class.getDeclaredField("xp");
        field.setAccessible(true);
        return field.getDouble(developer);
    }
    public void promote(Developer developer) throws InsufficientXPException {
        developer.getPromotion();
    }
    public void girlfriendChallenge(Developer developer, Scanner scanner) {
        throw new UnsupportedOperationException("GirlfriendChallenge nao existe nesta versao");
    }
    public void activateXpBoost(Developer developer, double multiplier, int duration) {
        throw new UnsupportedOperationException("Boost temporario nao existe nesta versao");
    }
    public void useBoost(Developer developer) {
        throw new UnsupportedOperationException("Boost temporario nao existe nesta versao");
    }
    public double xpMultiplier(Developer developer) {
        throw new UnsupportedOperationException("Boost temporario nao existe nesta versao");
    }
    public int boostRemainingProjects(Developer developer) {
        throw new UnsupportedOperationException("Boost temporario nao existe nesta versao");
    }
}
