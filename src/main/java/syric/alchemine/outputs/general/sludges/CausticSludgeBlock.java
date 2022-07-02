package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.util.ColorsUtil;

public class CausticSludgeBlock extends SludgeBlock implements IForgeBlock {
    private static MobEffectInstance getPoison() { return new MobEffectInstance(MobEffects.POISON, 80, 1, true, true); }
    private static MobEffectInstance getWither() { return new MobEffectInstance(MobEffects.WITHER, 100, 1, true, true); }
    private static MobEffectInstance getWeakPoison() { return new MobEffectInstance(MobEffects.POISON, 40, 0, true, true); }


    public CausticSludgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity live) {
            if (!live.hasEffect(MobEffects.POISON)) {
                live.addEffect(state.getValue(WEAK_VERSION) ? getWeakPoison() : getPoison());
            }
        }
        entity.makeStuckInBlock(state, new Vec3(0.2, 0.2, 0.2));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide()) {
            player.addEffect(state.getValue(WEAK_VERSION) ? getPoison() : getWither());
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (!levelAccessor.isClientSide()) {
            RandomSource source = RandomSource.create();
            AreaEffectCloud cloud = EntityType.AREA_EFFECT_CLOUD.create((Level) levelAccessor);
            BlockPos position = pos.offset(0.5, 0.5, 0.5);
            assert cloud != null;
            cloud.absMoveTo(position.getX(), position.getY(), position.getZ());
            cloud.setDuration(600);
            cloud.setRadius(2F);
            MobEffectInstance poison = new MobEffectInstance(MobEffects.POISON, 200, 0, false, false);
            cloud.addEffect(poison);
            cloud.setDeltaMovement(Vec3.ZERO);
            cloud.setFixedColor(ColorsUtil.rawColorFromRGB(78, 147, 49));
            levelAccessor.addFreshEntity(cloud);
        }

    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
        if (!player.level.isClientSide()) {
            if (!player.hasEffect(MobEffects.POISON)) {
                player.addEffect(state.getValue(WEAK_VERSION) ? getWeakPoison() : getPoison());
            }
        }
        return super.getDestroyProgress(state, player, getter, pos);
    }


    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        player.addEffect(state.getValue(WEAK_VERSION) ? getWeakPoison() : getPoison());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }



}
