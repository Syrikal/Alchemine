package syric.alchemine.brewing.cauldron;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Pair;
import syric.alchemine.brewing.ingredients.AlchemicalIngredients;
import syric.alchemine.brewing.ingredients.Ingredient;
import syric.alchemine.brewing.util.*;
import syric.alchemine.setup.AlchemineBlockEntityTypes;
import syric.alchemine.setup.AlchemineItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalCauldronBlockEntity extends BlockEntity implements Clearable {
    public AspectSet aspects = new AspectSet();
    public List<Ingredient> ingredients = new ArrayList<>();
    public AlchemicalBase base = new AlchemicalBase(BaseTypes.NONE);
    public List<Reaction> reactionList = new ArrayList<>();
    public List<Spike> spikeList = new ArrayList<>();

    public double energy;
    public double lingeringEnergy;
    public double lingerMult = 1;

    public double volatility = 0;
    public double stability = 0;

    private int drainCounter = 0;
    public double explosionCounter = 0;
    public double sludgeCounter = 0;

    public AlchemicalCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemineBlockEntityTypes.ALCHEMICAL_CAULDRON.get(), pos, state);
    }



    public void tick() {

        //Handle spikes
        //Tell it to not decrease below lingering!
        List<Spike> toRemove = new ArrayList<>();
        for (Spike spike : spikeList) {
            Pair<Double, Double> output = spike.tick();
            if (spike.isDone()) {
                toRemove.add(spike);
            } else {
                energy += output.getLeft();
                lingeringEnergy += output.getRight();
            }
        }
        //Remove old spikes
        spikeList.removeAll(toRemove);


        //Increase lingering if ingredient added early!


        //Handle reactions
        for (Reaction reaction: reactionList) {
            energy += reaction.tick();
        }
        //Including decay
        energy -= drainCounter * 0.00025;


        //Check for decay
        if (spikeList.isEmpty() && energy >= 1) {
            if (Math.random() * 60 < 1) {
                drainCounter ++;
            }

        }

        //Check for available recipes
        if (RecipeChecking.recipesExist(this)) {
            //do something
        }

        //Add to explosion and sludge counters
        if (energy >= 4) {
            double addition = Math.pow(energy, 2) - 8*energy + 16;
            explosionCounter += addition;
        } else if (energy <= 1) {
            double addition = Math.pow(energy, 2) - 2*energy + 1;
            sludgeCounter += addition;
        }

        //Check for accident
        if (energy >= 3) {
            if (Accidents.checkExplosion(this)) {
                Accidents.explode(this);
            }
        } else if (energy <= 2) {
            if (Accidents.checkSludge(this)) {
                Accidents.sludge(this);
            }
        }


    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack interactedItemStack = player.getItemInHand(hand);

        //Add a base or liquid



        //Add an ingredient
        if (AlchemicalIngredients.INGREDIENTS_MAP.containsKey(interactedItemStack.getItem())) {
            chatPrint("ATTEMPTING TO ADD INGREDIENT", player);
            announceIngredient(interactedItemStack.getItem(), player);
            Ingredient ingredient = AlchemicalIngredients.INGREDIENTS_MAP.get(interactedItemStack.getItem());
            addIngredient(ingredient, player);
            return InteractionResult.SUCCESS;
        }


        //Stir



        //Extract



        //Debug
        if (interactedItemStack.getItem() == AlchemineItems.CAULDRON_DEBUG_STICK.get()) {
            chatPrint(toString(), player);
            return InteractionResult.SUCCESS;
        }

        //Empty
        if (interactedItemStack.getItem() == AlchemineItems.CAULDRON_EMPTY_STICK.get()) {
            clearContent();
            chatPrint("Content cleared", player);
            return InteractionResult.SUCCESS;
        }


        chatPrint("FAILED TO DO ANYTHING, ANNOUNCING ITEM", player);
        announceIngredient(interactedItemStack.getItem(), player);
        return InteractionResult.SUCCESS;
    }



    public void announceIngredient(Item item, Entity entity) {
        String output = null;
        if (AlchemicalIngredients.INGREDIENTS_MAP.containsKey(item)) {
//            chatPrint("announceIngredient detects that this is an ingredient", entity);
            output = AlchemicalIngredients.INGREDIENTS_MAP.get(item).toString();
//            LogUtils.getLogger().info("Ingredient toString from announceIngredient: " + output);
        } else {
            output = "This item isn't an ingredient. This is a(n) " + item.toString() + "\nThere are " + AlchemicalIngredients.INGREDIENTS_MAP.size() + " ingredients registered in the map.";
        }
        String[] outputSplit = output.split("\n");
        for (String i : outputSplit) {
            chatPrint(i, entity);
        }
    }


    public void addIngredient(Ingredient ingredient, Player player){
        //Check reactions
        int numReacts = ingredient.getAspects().countReactions(aspects);
        if (!ingredient.isContradictory() && numReacts > 0) {
            if (volatility < -2) {
                reactionList.add(new Reaction(numReacts).negative());
            } else {
                reactionList.add(new Reaction(numReacts));
            }
        }

        //Check stabilizations
        int numStabilizations = ingredient.getAspects().countStabilizations(aspects);
        stability += numStabilizations * 0.3;

        //Add to ingredients
        ingredients.add(ingredient);

        //Add to aspects
        aspects.add(ingredient.getAspects());

        //Add spike
        double energy = ingredient.getEnergy();
        if (ingredient.isMetapotion() && base.type == BaseTypes.POTION) {
            energy /= 2;
        }
        double linger = ingredient.getLinger() * lingerMult;
        spikeList.add(new Spike(energy, linger));

        //Modify stability and volatility
        //Change this so they can't be moved out of bounds!
        volatility += ingredient.getVolatility();
        stability += ingredient.getStability();

        //Modify lingerMult
        //Change so it's not multiplicative?
        lingerMult *= ingredient.getLingerMultiplier();

        //Add crashes, if any
        reactionList.add(new Reaction(ingredient.getCrash()).negative());


        //Remove item from player (give one back if it has a returned item)

        chatPrint("Added ingredient", player);


    }







    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Energy: ").append(energy).append(", Lingering: ").append(lingeringEnergy).append("\n");
        sb.append("Aspects: ").append(aspects.toString()).append("\n");
        sb.append("Volatility: ").append(volatility).append(", Stability: ").append(stability).append("\n");
        sb.append("Reactions: ").append(reactionList.size()).append(", Drain: ").append(drainCounter).append("\n");
        sb.append("Ingredients: ");
        for (Ingredient i : ingredients) {
            sb.append(i.getItem().toString()).append(", ");
        }
        sb.append("\n");
        sb.append("Spikes: ");
        for (Spike spike : spikeList) {
            sb.append(spike.toString()).append(", ");
        }
        sb.append("\n");
        if (lingerMult != 1) {
            sb.append("Linger Multiplier: ").append(lingerMult).append("\n");
        }

        return sb.toString();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    @Override
    public void clearContent() {
        aspects.clear();
        ingredients.clear();
        base = new AlchemicalBase(BaseTypes.NONE);
        reactionList.clear();
        spikeList.clear();
        energy = 0;
        lingeringEnergy = 0;
        volatility = 0;
        stability = 0;
        drainCounter = 0;
        explosionCounter = 0;
        sludgeCounter = 0;
        lingerMult = 1;
    }
}
