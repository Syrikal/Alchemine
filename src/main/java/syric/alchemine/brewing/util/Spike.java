package syric.alchemine.brewing.util;

import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.logging.Logger;

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
        MutablePair<Double, Double> derivatives = findDerivatives(ticks+0.5);
        ticks++;
        return derivatives;
    }

    public MutablePair<Double, Double> findDerivatives(double t) {
        double v = (1-rate) * peak / 8;
        double energyOutput;
        double lingerOutput;
        if (t < 100-20*rate) {
            energyOutput = -(peak/160)*t + peak/2; //check this
        } else if (t < 400) {
            energyOutput = ((v/(300+20*rate)) * t) - ((5.0D*v*peak)/((4.0D*peak)-(2.0D*v)));
        } else {
            energyOutput = 0;
        }

        if (0 <= t && t < 80) {
            lingerOutput = (rate * peak) / 80D;
        } else {
            lingerOutput = 0;
        }
        return new MutablePair<>(energyOutput * 0.05, lingerOutput);
    }

    public boolean isDone() {
        return ticks > 400;
    }

    public int getTicks() {
        return ticks;
    }

    public String toString() {
        return "[" + ticks + ", " + peak + ", " + rate + "]";
    }

}
