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
import syric.alchemine.brewing.ingredients.AlchemicalIngredients;
import syric.alchemine.brewing.util.AlchemicalBase;
import syric.alchemine.brewing.util.AspectSet;
import syric.alchemine.brewing.util.Reaction;
import syric.alchemine.brewing.util.Spike;
import syric.alchemine.setup.AlchemineBlockEntityTypes;
import syric.alchemine.util.AlchemineTags;
import net.minecraft.world.level.block.CauldronBlock;

import java.util.List;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalCauldronBlockEntity extends BlockEntity implements Clearable {
    private AspectSet aspects;
    private AlchemicalBase base;
    private List<Reaction> reactionList;
    private List<Spike> spikeList;

    private double energy;
    private double lingeringEnergy;

    private int volatility;
    private int stability;

    public AlchemicalCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemineBlockEntityTypes.ALCHEMICAL_CAULDRON.get(), pos, state);
        aspects = new AspectSet();

    }



    public void tick() {}

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack interactedItemStack = player.getUseItem();

        //AddIngredient
//        if (interactedItemStack.is(AlchemineTags.Items.INGREDIENTS)) {
////            return addIngredient(interactedItemStack);
//        }

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

        //AddLiquid? Base? something

        //Stir
//            if (interactedItemStack.is(AlchemineTags.Items.CAULDRON_STIRRERS)) {
//                return InteractionResult.SUCCESS;
//            }

        //Examine

        //Extract
//        if (interactedItemStack.is(AlchemineTags.Items.CAULDRON_EXTRACTORS) || interactedItemStack.is(AlchemineTags.Items.CATALYSTS) || interactedItemStack.isEmpty()) {
////            return extract(state, level, pos, player, hand, result, false);
//        }

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





//    public void speak(Player player) {
//        chatPrint("block entity exists", player);
//    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    @Override
    public void clearContent() {}
}
