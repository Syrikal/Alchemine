package syric.alchemine.brewing.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
        if (interactedItemStack.is(AlchemineTags.Items.INGREDIENTS)) {
//            return addIngredient(interactedItemStack);
        }

        //AddLiquid? Base? something

        //Stir
            if (interactedItemStack.is(AlchemineTags.Items.CAULDRON_STIRRERS)) {
                return InteractionResult.SUCCESS;
            }

        //Examine

        //Extract
        if (interactedItemStack.is(AlchemineTags.Items.CAULDRON_EXTRACTORS) || interactedItemStack.is(AlchemineTags.Items.CATALYSTS) || interactedItemStack.isEmpty()) {
//            return extract(state, level, pos, player, hand, result, false);
        }

        return InteractionResult.PASS;
    }









    public void speak(Player player) {
        chatPrint("block entity exists", player);
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
    public void clearContent() {}
}
