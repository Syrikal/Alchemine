package syric.alchemine.outputs.fire.items;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import syric.alchemine.outputs.fire.blocks.AbstractAlchemicalFireBlock;
import syric.alchemine.setup.AlchemineItems;

public class FirestarterItem extends Item implements IForgeItem {
    private final AbstractAlchemicalFireBlock firetype;

    public FirestarterItem(Properties properties, RegistryObject<Block> fireReg) {
        super(properties);
        if (fireReg.get() instanceof AbstractAlchemicalFireBlock fireBlock) {
            firetype = fireBlock;
        } else {
            throw new IllegalArgumentException("FirestarterItem passed a block that isn't fire");
        }
    }

    public InteractionResult useOn(UseOnContext context) {
        LogUtils.getLogger().info("FirestarterItem triggered");
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos().relative(context.getClickedFace());
        boolean consume = false;
        if (firetype.canBePlacedAt(level, blockpos, context.getHorizontalDirection())) {
            LogUtils.getLogger().info("Can place fire, placing!");
            this.playSound(level, blockpos);
            level.setBlockAndUpdate(blockpos, firetype.getStateForPlacement(level, blockpos));
            level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockpos);
            consume = true;
        } else {LogUtils.getLogger().info("Can't place fire");}
        if (consume) {
            LogUtils.getLogger().info("Consuming.");
            context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource randomsource = level.getRandom();
        level.playSound((Player)null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (randomsource.nextFloat() - randomsource.nextFloat()) * 0.2F + 1.0F);
    }


}
