package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.Objects;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class FlexibleSludgeBlock extends SludgeBlock {
    protected static final VoxelShape SHAPE = Block.box(0.5D, 0.0D, 0.5D, 15.5D, 16.0D, 15.5D);

    public FlexibleSludgeBlock(Properties properties) {
        super(properties);
    }

//    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallHeight) {
//        if (entity.isSuppressingBounce() || !state.getValue(WEAK_VERSION)) {
//            super.fallOn(level, state, pos, entity, fallHeight);
//        } else {
//            entity.causeFallDamage(fallHeight, 0.5F, DamageSource.FALL);
//        }
//    }

    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        launch(entity.getBlockStateOn(), entity.getOnPos(), entity, true);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        launch(state, pos, entity, true);
        super.stepOn(level, pos, state, entity);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        launch(state, pos, entity, true);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }


    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        launch(state, pos, player, false);
    }

    private void launch (BlockState state, BlockPos pos, Entity entity, boolean direct) {
        RandomSource rand =RandomSource.create();
        double rn = rand.nextDouble();
        double baseChance = 1;
        double magnitude = 2.2;
        if (entity.isSteppingCarefully()) {
            baseChance *= 0.5;
            magnitude *= 0.7;
        }
        if (state.getValue(WEAK_VERSION)) {
            baseChance *= 0.7;
            magnitude *= 0.7;
        }
        if (!direct) {
            magnitude *= 0.7;
            baseChance *= 0.7;
        }


        if (direct || rn < baseChance) {
            Vec3 vectorToEntityCenter = entity.getBoundingBox().getCenter();
            double entityX = vectorToEntityCenter.x;
            double entityY = vectorToEntityCenter.y;
            double entityZ = vectorToEntityCenter.z;

            double pos1 = pos.getX() + 0.5;
            double pos2 = pos.getY() + 0.5;
            double pos3 = pos.getZ() + 0.5;

            Vec3 displacement = new Vec3(entityX - pos1, entityY - pos2, entityZ - pos3);

            displacement = displacement.normalize().scale(magnitude);
            entity.setDeltaMovement(displacement);
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
            }
        }
    }


}
