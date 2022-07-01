package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
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

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallHeight) {
        if (entity.isSuppressingBounce() || !state.getValue(WEAK_VERSION)) {
            super.fallOn(level, state, pos, entity, fallHeight);
        } else {
            entity.causeFallDamage(fallHeight, 0.5F, DamageSource.FALL);
        }

    }

    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(getter, entity);
        } else {
            this.bounceUp(entity);
        }

    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        boolean weak = false;
        BlockState state = entity.getBlockStateOn().getBlock() == AlchemineBlocks.FLEXIBLE_SLUDGE.get() ? entity.getBlockStateOn() : null;
        if (state != null) {
            weak = state.getValue(WEAK_VERSION);
        }
        if (vec3.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
            if (weak) {
                entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
            } else {
                Vec3 newDelta = new Vec3(vec3.x, 2.2 * d0, vec3.z);
                entity.setDeltaMovement(newDelta);
                chatPrint("Delta magnitude: " + newDelta.length(), entity);
            }
        }

    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if ((d0 < 0.1D && !entity.isSteppingCarefully()) || !state.getValue(WEAK_VERSION)) {
            double d1 = 0.4D + d0 * 0.2D;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0D, d1));
        }

        super.stepOn(level, pos, state, entity);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vectorToEntityCenter = entity.getBoundingBox().getCenter();
        double entityX = vectorToEntityCenter.x;
        double entityY = vectorToEntityCenter.y;
        double entityZ = vectorToEntityCenter.z;
//        chatPrint("Entity X: " + entityX + ", Y: " + entityY + ", Z: " + entityZ, level);

        double pos1 = pos.getX() + 0.5;
        double pos2 = pos.getY() + 0.5;
        double pos3 = pos.getZ() + 0.5;
//        chatPrint("Block center calculated: " + blockCenter.toShortString(), level);

        Vec3 displacement = new Vec3(entityX - pos1, entityY - pos2, entityZ - pos3);
        chatPrint("Displacement Vector: " + displacement.toString() + ", length: " + displacement.length() + ", multiplying...", level);


        double factor = (2.5D / displacement.length());

        displacement = displacement.multiply(factor, factor, factor);
        chatPrint("Displacement Vector: " + displacement.toString() + ", length: " + displacement.length(), level);
        entity.setDeltaMovement(displacement);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }


    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        RandomSource rand =RandomSource.create();
        if (rand.nextDouble() < 0.7) {
            Vec3 vectorToEntityCenter = player.getBoundingBox().getCenter();
            double entityX = vectorToEntityCenter.x;
            double entityY = vectorToEntityCenter.y;
            double entityZ = vectorToEntityCenter.z;
//        chatPrint("Entity X: " + entityX + ", Y: " + entityY + ", Z: " + entityZ, level);

            double pos1 = pos.getX() + 0.5;
            double pos2 = pos.getY() + 0.5;
            double pos3 = pos.getZ() + 0.5;
//        chatPrint("Block center calculated: " + blockCenter.toShortString(), level);

            Vec3 displacement = new Vec3(entityX - pos1, entityY - pos2, entityZ - pos3);
//            chatPrint("Displacement Vector: " + displacement.toString() + ", length: " + displacement.length() + ", multiplying...", level);


            double factor = (1.5D / displacement.length());

            displacement = displacement.multiply(factor, factor, factor);
//            chatPrint("Displacement Vector: " + displacement.toString() + ", length: " + displacement.length(), level);
            player.setDeltaMovement(displacement);
        }
    }


}
