package syric.alchemine.brewing.ingredients;

import syric.alchemine.brewing.cauldron.Aspect;
import syric.alchemine.brewing.cauldron.AspectSet;

import java.util.List;

public class Ingredient {
    private AspectSet aspects;
    private final double energy;
    private final double linger;
    private int volatility = 0;
    private int stability = 0;
    private int crash = 0;
    private boolean metapotion = false;
    private double lingerMultiplier = 1.0D;

    //Constructor and builders
    public Ingredient(double energy, double linger) {
        this.energy = energy;
        this.linger = linger;
    }

    public Ingredient addAspect(Aspect aspect, int value) {
        aspects.add(aspect, value);
        return this;
    }
    public Ingredient setVolatile(int volatility) {
        this.volatility = volatility;
        return this;
    }
    public Ingredient setStability(int stability) {
        this.stability = stability;
        return this;
    }
    public Ingredient setCrash(int crashes) {
        this.crash = crashes;
        return this;
    }
    public Ingredient setMetapotion() {
        this.metapotion = true;
        return this;
    }
    public Ingredient setLingerMult(double mult) {
        this.lingerMultiplier = mult;
        return this;
    }

    //Getters
    public AspectSet aspects() {
        return aspects;
    }
    public List<Aspect> maxAspects() { return aspects.getMaxAspects(); }
    public double energy() {
        return energy;
    }
    public double linger() {
        return linger;
    }
    public int volatility() { return volatility; }
    public int stability() { return stability; }
    public int crash() { return crash; }
    public boolean metapotion() { return metapotion; }
    public double lingerMultiplier() { return lingerMultiplier; }

}
