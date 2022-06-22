package syric.alchemine.brewing.util;

import org.apache.commons.lang3.tuple.MutablePair;

public class Spike {
    private int ticks = 0;
    private final double peak;
    private final double rate;

    public Spike(double peak, double rate){
        this.peak = peak;
        this.rate = rate;
    }

    //First of the pair is added to the energy, second is added to the lingering.
    public MutablePair<Double, Double> tick() {
        double v = (1-rate) * peak / 8;
        double energyOutput;
        double lingerOutput = 0;
        if (ticks <= 0) {
            energyOutput = 0;
        } else if (ticks <= 100-20*rate) {
            energyOutput = -(peak/160)*ticks + peak/2; //check this
            lingerOutput = (rate * peak) / (100-20 * rate);
        } else if (ticks <= 400) {
            energyOutput = (v/(300+20*rate))*ticks - (5*v*rate)/(4*peak-2*v);
        } else {
            energyOutput = 0;
        }
        ticks++;
        return new MutablePair<>(energyOutput, lingerOutput);
    }

    public boolean isDone() {
        return ticks >= 400;
    }

    public String toString() {
        return "[" + ticks + ", " + peak + ", " + rate + "]";
    }

}
