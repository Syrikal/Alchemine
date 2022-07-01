package syric.alchemine.outputs.general.sludges;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.extensions.IForgeBlock;

import java.util.Map;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class VolatileSludgeBlock extends SludgeBlock implements IForgeBlock {

    public VolatileSludgeBlock(Properties properties) {
        super(properties);
    }


    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide() && !entity.isSteppingCarefully()) {
            explode(level, pos, state,0.01F);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            float chance = player.isSteppingCarefully() ? 0.1F : 0.2F;
            explode(level, pos, state,chance);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide() && !player.isCreative()) {
            float toolMultiplier = (player.getMainHandItem().getItem().canPerformAction(player.getMainHandItem(), ToolActions.PICKAXE_DIG)) ? 0.8F : 1.0F;
            if (toolMultiplier == 0.8F) {
                LogUtils.getLogger().info("Detected pickaxe");
            }
            float silkMultiplier = 1.0F;
            float sneakMultiplier = player.isSteppingCarefully() ? 0.8F : 1.0F;
            Enchantment silktouch = Enchantments.SILK_TOUCH;
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(player.getMainHandItem());
            if (enchantments.containsKey(silktouch)) {
                silkMultiplier = 0.4F;
                LogUtils.getLogger().info("Decreasing explosion chance, silk touch detected");
            }
            explode(level, pos, state, 1F * silkMultiplier * sneakMultiplier * toolMultiplier);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private void explode(Level level, BlockPos pos, BlockState state, float chance) {
        RandomSource rdm = RandomSource.create();
//        chatPrint("Checking explosion with chance " + chance, level);
        if (rdm.nextFloat() < chance) {
            level.destroyBlock(pos, false);
//            chatPrint("Exploding!", level);
            if (state.getValue(WEAK_VERSION)) {
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5F, Explosion.BlockInteraction.BREAK);
            } else {
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.5F, Explosion.BlockInteraction.BREAK);
            }

        }

    }


}
