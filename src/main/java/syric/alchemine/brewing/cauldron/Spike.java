package syric.alchemine.brewing.cauldron;

public class Spike {
    private int ticks;
    private double peak;
    private double rate;

    public Spike(int ticks, double peak, double rate) {}

    public double tick() {
        double v = (1-rate) * peak / 8;
        double output = 0;
        if (ticks <= 0) {
            output = 0;
        } else if (ticks <= 100-20*rate) {
            output = -(peak/160)*ticks + peak/2;
        } else if (ticks <= 400) {
            output = (v/(300+20*rate))*ticks - (5*v*rate)/(4*peak-2*v);
        } else {
            output = 0;
        }
        ticks++;
        return output;
    }

    public boolean isDone() {
        return ticks >= 400;
    }

}
