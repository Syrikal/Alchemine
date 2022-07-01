package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.Nullable;
import syric.alchemine.brewing.laboratory.AlchemicalAlembicBlockEntity;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.ReplaceablesFilter;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;
import syric.alchemine.setup.AlchemineBlockEntityTypes;

public class StygianSludgeBlock extends SludgeBlock implements IForgeBlock, EntityBlock {
    private static MobEffectInstance getBriefSlow() { return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, true, true);}
    private static MobEffectInstance getLongSlow() { return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1, true, true);}


    public StygianSludgeBlock(Properties properties) {
        super(properties);
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (level.isClientSide()) {
            return;
        }
        RandomSource rdm = RandomSource.create();
        if (rdm.nextDouble() < 0.7) {
            randomTick(state, (ServerLevel) level, pos, rdm);
        }
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            float damage1 = state.getValue(WEAK_VERSION) ? 1F : 2F;
            float damage2 = entity.isSteppingCarefully() ? 1F : 0F;
            float damage = damage1 - damage2;
            if (damage > 0) {
                entity.hurt(DamageSource.FREEZE, damage);
            }
            if (!state.getValue(WEAK_VERSION) && !entity.isSteppingCarefully()) {
                entity.setTicksFrozen(entity.getTicksFrozen() + 3);
            }

            if (!state.getValue(WEAK_VERSION)) {
                ((LivingEntity) entity).addEffect(getBriefSlow());
            }

        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (player.canFreeze() && !state.getValue(WEAK_VERSION)) {
            player.setTicksFrozen(player.getTicksFrozen()+10);
            player.addEffect(getBriefSlow());
        }
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
        if (player.canFreeze()) {
            player.setTicksFrozen(player.getTicksFrozen() + 1);
        }
        return super.getDestroyProgress(state, player, getter, pos);
    }


    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        if (levelAccessor.isClientSide()) {
            return;
        }
        RandomSource rdm = RandomSource.create();
        if (levelAccessor instanceof ServerLevel) {
            randomTick(state, (ServerLevel) levelAccessor, pos, rdm);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player.canFreeze() && !level.isClientSide()) {
            player.setTicksFrozen(state.getValue(WEAK_VERSION) ? 100 : 200);
            player.addEffect(getLongSlow());
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
            int variation = state.getValue(WEAK_VERSION) ? 2 : 5;
            BlockPos pos1 = new BlockPos(pos.getX()-variation, pos.getY()-variation,pos.getZ()-variation);
            BlockPos pos2 = new BlockPos(pos.getX()+variation, pos.getY()+variation, pos.getZ()+variation);

            //Freeze water
            BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(pos, c) <= (float) variation).filter(c -> level.getBlockState(c).getMaterial() == Material.WATER && level.getBlockState(c.above()).getMaterial() == Material.AIR)
                    .forEach(c -> {
                    if (source.nextFloat() < 0.2) {
                        level.setBlockAndUpdate(c, Blocks.ICE.defaultBlockState());
                    };
            });
            //Break plants
            BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(pos, c) <= (float) variation).filter(c -> level.getBlockState(c).getMaterial() == Material.REPLACEABLE_PLANT || level.getBlockState(c).getMaterial() == Material.PLANT)
                .forEach(c -> {
                    if (source.nextFloat() < 0.3) {
                        level.destroyBlock(c, true);
                    };
            });
            //Place snow
            BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(pos, c) <= (float) variation).filter(c -> level.getBlockState(c).getMaterial() == Material.AIR && level.getBlockState(c.below()).getMaterial().isSolid())
                .forEach(c -> {
                    if (source.nextFloat() < 0.2) {
                        level.setBlockAndUpdate(c, Blocks.SNOW.defaultBlockState());
                    };
            });
            BlockPos.betweenClosedStream(pos1, pos2).filter(c -> distance(pos, c) <= (float) variation).filter(c -> level.getBlockState(c).getBlock() == Blocks.SNOW)
                .forEach(c -> {
                    if (source.nextFloat() < 0.2) {
                        BlockState currentState = level.getBlockState(c);
                        BlockState updatedState = currentState.setValue(BlockStateProperties.LAYERS, Math.min(currentState.getValue(BlockStateProperties.LAYERS) + 1, 8));
                        level.setBlockAndUpdate(c, updatedState);
                    };
                });
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AlchemineBlockEntityTypes.STYGIAN.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        return EntityBlock.super.getTicker(level, state, type);
        return level.isClientSide ? null : (level0, pos, state2, blockEntity) -> ((StygianSludgeBlockEntity) blockEntity).tick();
    }



    private float distance(BlockPos origin, BlockPos pos) {
        double xdist = Math.pow(origin.getX() - pos.getX(), 2);
        double zdist = Math.pow(origin.getZ() - pos.getZ(), 2);
        double ydist = Math.pow(origin.getY() - pos.getY(), 2);
        return (float) Math.sqrt(xdist + zdist + ydist);
    }

}
