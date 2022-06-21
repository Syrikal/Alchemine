package syric.alchemine.brewing.ingredients;

import net.minecraft.world.item.Item;
import org.checkerframework.checker.units.qual.A;
import syric.alchemine.brewing.util.Aspect;
import syric.alchemine.brewing.util.AspectSet;

import java.util.List;

public class Ingredient {
    private final Item item;
    private final AspectSet aspects = new AspectSet();
    private final double energy;
    private final double linger;
    private int volatility = 0;
    private int stability = 0;
    private int crash = 0;
    private boolean contradictory = false;
    private boolean metapotion = false;
    private double lingerMultiplier = 1.0D;
    private Item returnedItem = null;

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
    public Ingredient setReturnedItem(Item item) {
        this.returnedItem = item;
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
    public Item getReturnedItem() { return returnedItem; }

    //toString
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("This item is an Ingredient.\n");
        builder.append("Item: ").append(item.toString()).append("\n");
        if (!aspects.getMap().isEmpty()) {
            builder.append("Aspects: ").append(aspects.toString()).append("\n");
        }
        builder.append("Energy: ").append(energy).append(", Linger Rate: ").append(linger).append("\n");
        builder.append("Volatility: ").append(volatility).append(", Stability: ").append(stability).append("\n");
        if (crash != 0) {
            builder.append("Crashes: ").append(crash).append("\n");
        }
        if (contradictory) {
            builder.append("It is a contradictory ingredient.\n");
        }
        if (metapotion) {
            builder.append("It is a metapotion ingredient.\n");
        }
        if (returnedItem != null) {
            builder.append("It returns the item ").append(returnedItem.toString()).append("\n");
        }

        return builder.toString();
    }

}
