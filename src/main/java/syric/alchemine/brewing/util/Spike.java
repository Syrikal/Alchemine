package syric.alchemine.brewing.util;

import org.apache.commons.lang3.tuple.MutablePair;

public class Spike {
    private int ticks;
    private double peak;
    private double rate;

    public Spike(int ticks, double peak, double rate) {}

    //First of the pair is added to the energy, second is added to the lingering.
    public MutablePair<Double, Double> tick() {
        double v = (1-rate) * peak / 8;
        double energyOutput = 0;
        double lingerOutput = 0;
        if (ticks <= 0) {
            energyOutput = 0;
        } else if (ticks <= 100-20*rate) {
            energyOutput = -(peak/160)*ticks + peak/2;
            lingerOutput = (rate * peak) / (100-20 * rate);
        } else if (ticks <= 400) {
            energyOutput = (v/(300+20*rate))*ticks - (5*v*rate)/(4*peak-2*v);
        } else {
            return null;
        }
        ticks++;
        return new MutablePair<Double, Double>(energyOutput, lingerOutput);
    }

    public boolean isDone() {
        return ticks >= 400;
    }

}
