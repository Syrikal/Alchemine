package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineEffects;
import syric.alchemine.setup.AlchemineSoundEvents;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class LuminousSludgeBlock extends SludgeBlock implements IForgeBlock {

    public LuminousSludgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide() && !player.isCreative()) {
            MobEffectInstance blind = new MobEffectInstance(MobEffects.BLINDNESS, 600, 0, true, true);
            MobEffectInstance lightblind = new MobEffectInstance(AlchemineEffects.LIGHT_BLINDNESS.get(), 600, 0, true, true);

            player.addEffect(blind);
            player.addEffect(lightblind);

//            level.playSound(player, pos, AlchemineSoundEvents.LUMINOUS_BREAK_AMBIENT.get(), SoundSource.BLOCKS, 0.2F, 1F);

        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

}
