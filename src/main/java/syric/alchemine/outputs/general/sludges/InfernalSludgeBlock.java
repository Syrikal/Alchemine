package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;

public class InfernalSludgeBlock extends SludgeBlock implements IForgeBlock {

    public InfernalSludgeBlock(Properties properties) {
        super(properties);
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (level.isClientSide()) {
            return;
        }
        RandomSource rdm = RandomSource.create();
        if (rdm.nextDouble() < 0.05) {
            level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 1);
        }
        if (rdm.nextDouble() < 0.7) {
            randomTick(state, (ServerLevel) level, pos, rdm);
        }
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            float damage1 = state.getValue(WEAK_VERSION) ? 1F : 2F;
            float damage2 = entity.isSteppingCarefully() ? 1F : 0F;
            float damage = damage1 - damage2;
            if (damage > 0) {
                entity.hurt(DamageSource.HOT_FLOOR, damage);
            }
            if (!state.getValue(WEAK_VERSION) && !entity.isSteppingCarefully()) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 20);
            }

        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!player.fireImmune() && !state.getValue(WEAK_VERSION)) {
            player.setRemainingFireTicks(player.getRemainingFireTicks() + 20);
        }
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
        if (!player.fireImmune() && !state.getValue(WEAK_VERSION)) {
            player.setRemainingFireTicks(player.getRemainingFireTicks() + 20);
        }
        return super.getDestroyProgress(state, player, getter, pos);
    }


    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (levelAccessor.isClientSide()) {
            return;
        }
        RandomSource rdm = RandomSource.create();
        if (rdm.nextDouble() < 0.05 && levelAccessor instanceof ServerLevel) {
            levelAccessor.setBlock(pos, Blocks.LAVA.defaultBlockState(), 1);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!player.fireImmune() && !level.isClientSide()) {
            player.setRemainingFireTicks(player.getRemainingFireTicks() + (state.getValue(WEAK_VERSION) ? 100 : 200));
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
            int variation = 1;
            BlockPos pos1 = new BlockPos(pos.getX()-variation, pos.getY()-variation,pos.getZ()-variation);
            BlockPos pos2 = new BlockPos(pos.getX()+variation, pos.getY()+variation, pos.getZ()+variation);
            BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(pos, c) <= 1.0F).filter(c -> effectsUtil.BLOCK_REPLACEABLE.check(level.getBlockState(c)))
                    .forEach(c -> {
                    if (source.nextFloat() < 0.3) {
                        level.destroyBlock(c, true);
                        level.setBlockAndUpdate(c, Blocks.FIRE.defaultBlockState());
                    };
            });


        PlacementPattern pattern = new SpherePattern(pos, 1);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.AIR_ONLY).cull(PlacementSet.randomFilter(0.5F)).placeImmediate(Blocks.FIRE, false);

    }

    private float distance(BlockPos origin, BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

}
