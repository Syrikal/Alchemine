package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.util.ColorsUtil;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ChaoticSludgeBlock extends SludgeBlock implements IForgeBlock {
    private static MobEffectInstance getSpeed() { return new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1, true, true); }
    private static MobEffectInstance getSlow() { return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1800, 2, true, true); }
    private static MobEffectInstance getHaste() { return new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0, true, true); }
    private static MobEffectInstance getFatigue() { return new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1200, 1, true, true); }
    private static MobEffectInstance getStrength() { return new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1, true, true); }
    private static MobEffectInstance getWeak() { return new MobEffectInstance(MobEffects.WEAKNESS, 3600, 1, true, true); }
    private static MobEffectInstance getJump() { return new MobEffectInstance(MobEffects.JUMP, 1200, 3, true, true); }
    private static MobEffectInstance getNausea() { return new MobEffectInstance(MobEffects.CONFUSION, 800, 1, true, true); }
    private static MobEffectInstance getRegen() { return new MobEffectInstance(MobEffects.REGENERATION, 1200, 1, true, true); }
    private static MobEffectInstance getResist() { return new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 2, true, true); }
    private static MobEffectInstance getFire() { return new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0, true, true); }
    private static MobEffectInstance getWater() { return new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0, true, true); }
    private static MobEffectInstance getInvis() { return new MobEffectInstance(MobEffects.INVISIBILITY, 3600, 0, true, true); }
    private static MobEffectInstance getBlind() { return new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0, true, true); }
    private static MobEffectInstance getNight() { return new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0, true, true); }
    private static MobEffectInstance getPoison() { return new MobEffectInstance(MobEffects.POISON, 1200, 2, true, true); }
    private static MobEffectInstance getGlow() { return new MobEffectInstance(MobEffects.GLOWING, 12000, 0, true, true); }
    private static MobEffectInstance getLevit() { return new MobEffectInstance(MobEffects.LEVITATION, 800, 1, true, true); }
    private static MobEffectInstance getLuck() { return new MobEffectInstance(MobEffects.LUCK, 6000, 2, true, true); }
    private static MobEffectInstance getSlowfall() { return new MobEffectInstance(MobEffects.SLOW_FALLING, 6000, 2, true, true); }

    private static final NavigableMap<Integer, MobEffectInstance> effectMap = new TreeMap<Integer, MobEffectInstance>();
    private static int totalWeight = 0;

    public ChaoticSludgeBlock(Properties properties) {
        super(properties);
        addToMap(getSpeed(), 2);
        addToMap(getSlow(), 2);
        addToMap(getHaste(), 2);
        addToMap(getFatigue(), 1);
        addToMap(getStrength(), 1);
        addToMap(getWeak(), 1);
        addToMap(getJump(), 3);
        addToMap(getNausea(), 4);
        addToMap(getRegen(), 1);
        addToMap(getResist(), 1);
        addToMap(getFire(), 1);
        addToMap(getWater(), 1);
        addToMap(getInvis(), 2);
        addToMap(getBlind(), 2);
        addToMap(getNight(), 1);
        addToMap(getPoison(), 1);
        addToMap(getGlow(), 4);
        addToMap(getLevit(), 4);
        addToMap(getLuck(), 2);
        addToMap(getSlowfall(), 1);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide()) {
            inflictRandomEffect(player);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (!levelAccessor.isClientSide()) {
            createRandomCloud((Level) levelAccessor, pos);
        }

    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
        if (!player.level.isClientSide()) {
            inflictRandomEffect(player);
        }
        return super.getDestroyProgress(state, player, getter, pos);
    }


    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            inflictRandomEffect(player);
        }
    }


    private void createRandomCloud(Level level, BlockPos pos) {
        BlockPos destination = pos.offset(0.5, 0.5, 0.5);
        MobEffectInstance effectInstance = getRandomEffect();
        AreaEffectCloud cloud = EntityType.AREA_EFFECT_CLOUD.create(level);
        assert cloud != null;
        cloud.absMoveTo(destination.getX(), destination.getY(), destination.getZ());
        cloud.setDuration(600);
        cloud.setRadius(2F);
        cloud.addEffect(effectInstance);
        cloud.setDeltaMovement(Vec3.ZERO);
        cloud.setFixedColor(effectInstance.getEffect().getColor());
        level.addFreshEntity(cloud);
    }

    private void inflictRandomEffect(Entity entity) {
        if (entity instanceof LivingEntity live) {
            if (live.getActiveEffects().size() < 3) {
                MobEffectInstance instance = getRandomEffect();
                live.addEffect(instance);
            }
        }
    }

    private MobEffectInstance getRandomEffect() {
        double check = RandomSource.create().nextDouble() * totalWeight;
        return effectMap.higherEntry((int) check).getValue();
    }

    private void addToMap(MobEffectInstance effect, int weight) {
        totalWeight += weight;
        effectMap.put(totalWeight, effect);
    }



}
