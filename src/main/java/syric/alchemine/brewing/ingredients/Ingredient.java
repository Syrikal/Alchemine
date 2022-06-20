package syric.alchemine.brewing.ingredients;

import net.minecraft.world.item.Item;
import syric.alchemine.brewing.util.Aspect;
import syric.alchemine.brewing.util.AspectSet;

import java.util.List;

public class Ingredient {
    private Item item;
    private AspectSet aspects;
    private final double energy;
    private final double linger;
    private int volatility = 0;
    private int stability = 0;
    private int crash = 0;
    private boolean contradictory = false;
    private boolean metapotion = false;
    private double lingerMultiplier = 1.0D;

    //Constructor and builders
    public Ingredient(Item item, double energy, double linger) {
        this.item = item;
        this.energy = energy;
        this.linger = linger;
    }

    public Ingredient addAspect(Aspect aspect, int value) {
        aspects.add(aspect, value);
        this.contradictory = aspects.checkContradictions();
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
    public Ingredient setContradictory() {
        this.contradictory = true;
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
    public Item getItem() { return item; }
    public AspectSet getAspects() {
        return aspects;
    }
    public List<Aspect> getMaxAspects() { return aspects.getMaxAspects(); }
    public double getEnergynergy() {
        return energy;
    }
    public double getLinger() {
        return linger;
    }
    public int getVolatility() { return volatility; }
    public int getStability() { return stability; }
    public int getCrash() { return crash; }
    public boolean isContradictory() {
        return contradictory;
    }
    public boolean getMetapotion() { return metapotion; }
    public double getLingerMultiplier() { return lingerMultiplier; }

}
