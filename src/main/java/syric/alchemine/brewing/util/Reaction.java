package syric.alchemine.brewing.util;

public class Reaction {
    private int ticks;
    private boolean upwards = true;
    private int multiplier = 1;

    public Reaction() {
        this.ticks = 0;
    }

    public Reaction(int mult){
        this.ticks = 0;
        this.multiplier = mult;
    }

    public Reaction negative() {
        this.upwards = false;
        return this;
    }

    public double tick() {
        double output = 0.000025*Math.pow(ticks, 2) + 0.0015;
        if (!upwards) {
            output *= -1;
        }
        output *= multiplier;
        ticks++;
        return output;
    }

    public int getMultiplier() { return multiplier; }

    public boolean isNegative() { return !upwards; }

    public String toString() {
        return "[" + ticks + ", " + upwards + ", " + multiplier + "]";
    }
}
