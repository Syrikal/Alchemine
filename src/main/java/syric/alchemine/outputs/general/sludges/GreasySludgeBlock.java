package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineBlocks;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class GreasySludgeBlock extends SludgeBlock implements IForgeBlock {

    public GreasySludgeBlock(Properties properties) {
        super(properties);
    }


    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
//        if (!level.isClientSide()) {
            boolean slip = true;
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) || (state.getValue(WEAK_VERSION) && entity.isSteppingCarefully())) {
                    slip = false;
                }
            }
            if (slip) {
                slip(entity);
            }
//        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide() && RandomSource.create().nextDouble() < 0.8) {
            evade(level, state, pos, player);
        }
    }


    private void evade(Level level, BlockState state, BlockPos pos, Player player) {
        Vec3 lookAngle = player.getLookAngle();
        Direction.Axis axis = null;
        double x = Math.abs(lookAngle.x);
        double y = Math.abs(lookAngle.y);
        double z = Math.abs(lookAngle.z);
        if (x >= y && x >= z) {
            axis = Direction.Axis.X;
        } else if (y >= x && y >= z) {
            axis = Direction.Axis.Y;
        } else {
            axis = Direction.Axis.Z;
        }

        for (Direction dir : Direction.allShuffled(RandomSource.create())) {
            if (dir.getAxis() != axis) {
                BlockState candidateState = level.getBlockState(pos.relative(dir));
                BlockState underCandidateState = level.getBlockState(pos.relative(dir).below());
                if (candidateState.getMaterial().isReplaceable() && underCandidateState.getMaterial().isSolid() && !underCandidateState.is(AlchemineBlocks.GREASY_SLUDGE.get())) {
                    level.destroyBlock(pos, false);
                    level.setBlockAndUpdate(pos.relative(dir), state);
                    return;
                }
            }
        }



    }

    private void slip(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, true, true);
            RandomSource rand = RandomSource.create();
            Vec3 direction = new Vec3(rand.nextDouble(), 0, rand.nextDouble()).normalize().multiply(2, 0, 2);
            chatPrint("Launching with magnitude " + direction.length(), entity);
            livingEntity.setDeltaMovement(direction);
            livingEntity.addEffect(slow);
        }
    }

}
