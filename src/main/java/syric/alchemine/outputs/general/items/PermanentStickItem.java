package syric.alchemine.outputs.general.items;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffects;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;
import syric.alchemine.setup.AlchemineItems;

public class PermanentStickItem extends Item {

    public PermanentStickItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block block = level.getBlockState(pos).getBlock();

        if (block instanceof PossiblyPermanentBlock) {
            LogUtils.getLogger().info("Detected possibly permanent block");
            ((PossiblyPermanentBlock) block).togglePermanent(level, pos, level.getBlockState(pos));
        }

        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

}
