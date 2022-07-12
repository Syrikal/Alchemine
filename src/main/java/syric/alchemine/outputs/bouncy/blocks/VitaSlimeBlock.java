package syric.alchemine.outputs.bouncy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.Objects;

public class VitaSlimeBlock extends AbstractImmersionBlock {

    //Variables
    public static final BooleanProperty CHARGED = BooleanProperty.create("charged");

    private static MobEffectInstance getRegen() { return new MobEffectInstance(MobEffects.REGENERATION, 50, 0, true, true); }
    private static MobEffectInstance getAbs1() { return new MobEffectInstance(MobEffects.ABSORPTION, 1800, 0, true, true); }
    private static MobEffectInstance getAbs2() { return new MobEffectInstance(MobEffects.ABSORPTION, 3000, 1, true, true); }
    private static MobEffectInstance getAbs3() { return new MobEffectInstance(MobEffects.ABSORPTION, 6000, 2, true, true); }

//    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, (double)0.9F, 1.0D);

    //Basic Setup
    public VitaSlimeBlock(BlockBehaviour.Properties properties) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGED, Boolean.FALSE));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
    }

    //Behavior
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3(0.5D, 0.2D, 0.5D));


        if (!(entity instanceof LivingEntity) || entity.getFeetBlockState().is(this)) {
            if (level.isClientSide) {
                RandomSource randomsource = level.getRandom();
                boolean flag = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
                if (flag && randomsource.nextDouble() < 0.1) {
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getX(), (double)(pos.getY() + 1), entity.getZ(), (double)(Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F), (double)0.05F, (double)(Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F));
                }
                if (flag && randomsource.nextDouble() <= 0.01) {
                    SoundEvent soundEvent = SoundEvents.SLIME_ATTACK;
                    entity.playSound(soundEvent, 0.7F, 0.6F);
                }
            }
        }


        boolean isCharged = state.getValue(CHARGED);
        unCharge(state, level, pos, 0);
        if (entity instanceof LivingEntity live) {
            if (live.hasEffect(MobEffects.REGENERATION)) {
//                chatPrint("Regen detected", entity);
                if (Objects.requireNonNull(live.getEffect(MobEffects.REGENERATION)).getDuration() <= 10) {
                    live.addEffect(getRegen());
                }
                if (isCharged) {
//                    chatPrint("Charge consumed", entity);
                    if (!live.hasEffect(MobEffects.ABSORPTION)) {
//                        chatPrint("No Absorption Detected", entity);
                        if (!level.isClientSide) {
                            live.addEffect(getAbs1());
                        }
                        poof(level, entity);
//                        chatPrint("Applying Absorption I", entity);
                    } else {
                        int dur = live.getEffect(MobEffects.ABSORPTION).getDuration();
//                        chatPrint("Absorption duration detected: " + dur, entity);
                        int lvl = live.getEffect(MobEffects.ABSORPTION).getAmplifier();
//                        chatPrint("Absorption magnitude detected : " + lvl, entity);

                        if (lvl == 0 && dur <= 600) {
                            if (!level.isClientSide) {
                                live.addEffect(getAbs2());
                            }
                            poof(level, entity);
//                            chatPrint("Applying Absorption II", entity);
                        } else if (lvl == 1 && dur <= 600) {
                            if (!level.isClientSide) {
                                live.addEffect(getAbs3());
                            }
                            poof(level, entity);
//                            chatPrint("Applying Absorption III", entity);
                        } else {
//                            chatPrint("No Absorption effect applied", entity);
                        }
                    }
                }
            } else {
//                chatPrint("No Regen detected", entity);
                if (isCharged) {
//                    chatPrint("Charge consumed", entity);
                }
                live.addEffect(getRegen());
                poof(level, entity);
//                chatPrint("Regen added", entity);
            }
        }

        //Extinguishes fire
        if (!level.isClientSide) {
            entity.setSharedFlagOnFire(false);
            entity.clearFire();
        }

    }


    //Audiovisual
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (level.isClientSide) {
            boolean animate = (source.nextDouble() < 0.15 || state.getValue(CHARGED));
            if (animate) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();

                double rand1 = source.nextDouble();
                double rand2 = source.nextDouble();
                double rand3 = source.nextDouble();

                double d0 = (double)i + rand1;
                double d1 = (double)j + rand2;
                double d2 = (double)k + rand3;

                level.addParticle(ParticleTypes.HAPPY_VILLAGER, d0, d1, d2, 0,0,0);
            }
        }
    }

    public void poof(Level level, Entity entity) {
//        System.out.println("Poof triggered");
        if (level.isClientSide) {
//            System.out.println("Poof triggered clientside");
            RandomSource source = level.random;
            for (int a = 0; a<30; a++) {
                double i = entity.getX() + Mth.randomBetween(source, -1, 1)*.5;
                double j = entity.getEyeY() + Mth.randomBetween(source, -1, 1)*.5;
                double k = entity.getZ() + Mth.randomBetween(source, -1, 1)*.5;

                double d0 = Mth.randomBetween(source, -1, 1)*.5;
                double d1 = Mth.randomBetween(source, -1, 1)*.5;
                double d2 = Mth.randomBetween(source, -1, 1)*.5;

                level.addParticle(ParticleTypes.HAPPY_VILLAGER, i, j, k, d0, d1, d2);
//                System.out.println("Produced poof particle at " + i + ", " + j + ", " + k + ". Velocity was " + d0 + ", " + d1 + ", " + d2);
            }
            SoundEvent soundEvent = SoundEvents.SLIME_BLOCK_BREAK;
            entity.playSound(soundEvent, 1.0F, 1.2F);
        }
    }



    //CHARGE AND TICKING
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 600);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (!state.getValue(CHARGED)) {
            level.setBlockAndUpdate(pos, state.setValue(CHARGED, true));
        }
    }

    private void unCharge(BlockState state, Level level, BlockPos pos, int directness) {
        boolean wasCharged = state.getValue(CHARGED);
        if (wasCharged) {
            level.scheduleTick(pos, this, 600);
            level.setBlockAndUpdate(pos, state.setValue(CHARGED, false));
        }
        if (directness <= 1) {
            unChargeNeighbors(state, level, pos, directness + 1);
        }

    }

    private void unChargeNeighbors(BlockState state, Level level, BlockPos pos, int directness) {
        BlockPos pos1 = new BlockPos(pos.getX()-1, pos.getY()-1,pos.getZ()-1);
        BlockPos pos2 = new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1);
        BlockPos.betweenClosedStream(pos1, pos2)
                .filter(c -> level.getBlockState(c).getBlock().equals(AlchemineBlocks.VITA_SLIME.get()))
                .forEach(c -> unCharge(level.getBlockState(c), level, c, directness));
    }


//    //SHAPE AND RENDERING
//    @Override
//    public boolean skipRendering(BlockState state, BlockState state2, Direction dir) {
//        return state2.is(this) || super.skipRendering(state, state2, dir);
//    }
//
//    @Override
//    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        return Shapes.empty();
//    }
//
//    public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
//        return Shapes.empty();
//    }
//
//    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        if (context instanceof EntityCollisionContext entitycollisioncontext) {
//            Entity entity = entitycollisioncontext.getEntity();
//            if (entity != null) {
//                if (entity.fallDistance > 2.5F) {
//                    return FALLING_COLLISION_SHAPE;
//                }
//
//                boolean flag = entity instanceof FallingBlockEntity;
//                if (flag || entity.isShiftKeyDown() && context.isAbove(Shapes.block(), pos, false) && !context.isDescending()) {
////                if (flag && context.isAbove(Shapes.block(), pos, false) && !context.isDescending()) {
//
//                    return super.getCollisionShape(state, getter, pos, context);
//                }
//            }
//        }
//
//        return Shapes.empty();
//    }

}
