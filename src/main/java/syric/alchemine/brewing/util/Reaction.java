package syric.alchemine.brewing.util;

public class Reaction {
    private int ticks;
    private boolean upwards = true;

    public Reaction() {
        this.ticks = 0;
    }

    public Reaction negative() {
        this.upwards = false;
        return this;
    }

    public double tick() {
        double output = 0.0005*Math.pow(ticks, 2) + 0.0015;
        if (!upwards) {
            output *= -1;
        }
        ticks++;
        return output;
    }
}
