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
import syric.alchemine.util.AlchemineTags;
import net.minecraft.world.level.block.CauldronBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalCauldronBlockEntity extends BlockEntity implements Clearable {
    public AspectSet aspects = new AspectSet();
    public List<Ingredient> ingredients = new ArrayList<>();
    public AlchemicalBase base = new AlchemicalBase(BaseTypes.NONE);
    public List<Reaction> reactionList = new ArrayList<>();
    public List<Spike> spikeList = new ArrayList<>();

    public double energy;
    public double lingeringEnergy;

    public int volatility = 0;
    public int stability = 0;

    private int drainCounter = 0;
    public double explosionCounter = 0;
    public double sludgeCounter = 0;

    public AlchemicalCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemineBlockEntityTypes.ALCHEMICAL_CAULDRON.get(), pos, state);
        aspects = new AspectSet();

    }



    public void tick() {

        //Handle spikes
        for (Spike spike : spikeList) {
            Pair<Double, Double> output = spike.tick();
            if (output == null) {
                spikeList.remove(spike);
            } else {
                energy += output.getLeft();
                lingeringEnergy += output.getRight();
            }
        }

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
        if (energy >= 4) {
            if (Accidents.checkExplosion(this)) {
                Accidents.explode(this);
            }
        } else if (energy <= 1) {
            if (Accidents.checkSludge(this)) {
                Accidents.sludge(this);
            }
        }


    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack interactedItemStack = player.getUseItem();

        //Add a base or liquid



        //Add an ingredient
        if (AlchemicalIngredients.INGREDIENTS_MAP.containsKey(interactedItemStack.getItem())) {
//            addIngredient(AlchemicalIngredients.INGREDIENTS_MAP.get(interactedItemStack.getItem()))
        }


        //Stir



        //Extract



        //Debug




        if (AlchemicalIngredients.INGREDIENTS_MAP.containsKey(interactedItemStack.getItem())) {
            chatPrint("Ingredient detected", player);
            announceIngredient(interactedItemStack.getItem(), player);
            return InteractionResult.SUCCESS;
        } else {
            chatPrint("Not an ingredient", player);

            int size = AlchemicalIngredients.INGREDIENTS_MAP.size();
            chatPrint("Ingredients map has " + size + " entries", player);
            return InteractionResult.FAIL;
        }

    }



    public void announceIngredient(Item item, Entity entity) {
        String output = null;
        if (AlchemicalIngredients.INGREDIENTS_MAP.containsKey(item)) {
//            chatPrint("announceIngredient detects that this is an ingredient", entity);
            output = AlchemicalIngredients.INGREDIENTS_MAP.get(item).toString();
            LogUtils.getLogger().info("Ingredient toString from announceIngredient: " + output);
        } else {
            output = "This item isn't an ingredient. This is a(n) " + item.toString() + "\nThere are " + AlchemicalIngredients.INGREDIENTS_MAP.size() + " ingredients registered in the map.";
        }
        String[] outputSplit = output.split("\n");
        for (String i : outputSplit) {
            chatPrint(i, entity);
        }
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

    }
}
