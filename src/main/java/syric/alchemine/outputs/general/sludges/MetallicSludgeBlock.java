package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineEffects;

public class MetallicSludgeBlock extends SludgeBlock implements IForgeBlock {

    public MetallicSludgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide()) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.isDamageableItem()) {
                itemStack.hurtAndBreak(9, player, (c) -> {
                    c.broadcastBreakEvent(player.swingingArm);
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemStack, player.swingingArm);
                });
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

}
