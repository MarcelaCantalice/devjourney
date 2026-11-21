package game.service;

import game.Developer;
import game.service.Promotion.Level;

class DeveloperFake extends Developer {
    private Level cargo;
    private double xp;
    int chamadasResetXp = 0;
    int chamadasSetCurrentPosition = 0;

    DeveloperFake(Level cargo, double xp) {
        super("Dev Fake");
        this.cargo = cargo;
        this.xp = xp;
    }

    @Override
    public Level getPosition() { return cargo; }

    @Override
    public double getXp() { return xp; }

    @Override
    public void setCurrentPosition(Level novoCargo) {
        chamadasSetCurrentPosition++;
        this.cargo = novoCargo;
    }

    @Override
    public void resetXp() {
        chamadasResetXp++;
        this.xp = 0;
    }
}
