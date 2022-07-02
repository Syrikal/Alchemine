package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineBlocks;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class GreasySludgeBlock extends SludgeBlock implements IForgeBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16D, 15D, 16D);

    public GreasySludgeBlock(Properties properties) {
        super(properties);
    }


//    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
//        boolean slip = true;
//        if (entity instanceof LivingEntity livingEntity) {
//            if (livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) || (state.getValue(WEAK_VERSION) && entity.isSteppingCarefully())) {
//                slip = false;
//            }
//        }
//        if (slip) {
//            slip(level, entity);
//        }
//        super.stepOn(level, pos, state, entity);
//    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()) {
            slip(level, state, entity);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
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

    private void slip(Level level, BlockState state, Entity entity) {
        boolean slip = true;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) || (state.getValue(WEAK_VERSION) && entity.isSteppingCarefully())) {
                slip = false;
            }
        }

        if (slip) {
            MobEffectInstance slow = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 9, true, true);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(slow);
            }
            RandomSource rand = RandomSource.create();
            Vec3 direction = new Vec3(rand.nextDouble() - 0.5, 0, rand.nextDouble() - 0.5).normalize().scale(1).add(0, 0.1, 0);
//            chatPrint("Slipping with magnitude " + direction.length() + " on the " + (level.isClientSide() ? "client" : "server") + " side", entity);
            entity.setDeltaMovement(direction);
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
            }
        }

    }

}
