package syric.alchemine.outputs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BerserkersResinBlock extends AbstractImmersionBlock  {

    //Variables
    public static final BooleanProperty WAITING = BooleanProperty.create("waiting");
    public static final BooleanProperty CHARGED = BooleanProperty.create("charged");
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");

    private static MobEffectInstance getWaitWeak() {return new MobEffectInstance(MobEffects.WEAKNESS, 5, 2, true, true);}

    private static MobEffectInstance getSealedSlow() {return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2, true, true); }
    private static MobEffectInstance getSealedWeak() {return new MobEffectInstance(MobEffects.WEAKNESS, 30, 1, true, true);}
    private static MobEffectInstance getSealedFatigue() {return new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30, 0, true, true);}

    private static MobEffectInstance getSpeed() {return new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 3, true, true);}
    private static MobEffectInstance getSlow() {return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 7800, 1, true, true);}
    private static MobEffectInstance getStrength() {return new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 3, true, true);}
    private static MobEffectInstance getWeakness() {return new MobEffectInstance(MobEffects.WEAKNESS, 7800, 0, true, true);}

    //Basic Setup
    public BerserkersResinBlock(BlockBehaviour.Properties properties) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGED, Boolean.FALSE).setValue(SEALED, Boolean.FALSE).setValue(WAITING, Boolean.FALSE));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGED, SEALED, WAITING);
    }

    //Behavior
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        //Stickiness
        if (state.getValue(SEALED)) {
            entity.makeStuckInBlock(state, new Vec3(0.001D, 0.001D, 0.001D));
        } else {
            entity.makeStuckInBlock(state, new Vec3(0.6D, 0.2D, 0.6D));
        }

        //Noises
        if (!(entity instanceof LivingEntity) || entity.getFeetBlockState().is(this)) {
            if (level.isClientSide) {
                RandomSource randomsource = level.getRandom();
                boolean flag = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
//                if (flag && randomsource.nextDouble() < 0.1) {
//                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getX(), (double)(pos.getY() + 1), entity.getZ(), (double)(Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F), (double)0.05F, (double)(Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F));
//                }
                if (flag && randomsource.nextDouble() <= 0.01 || randomsource.nextDouble() <= 0.001) {
                    SoundEvent soundEvent = SoundEvents.SLIME_ATTACK;
                    entity.playSound(soundEvent, 0.7F, 0.6F);
                }
            }
        }


        //All effect stuff
        if (entity instanceof LivingEntity live && isTop(level, state, pos)) {

            //If charged, attempt to seal or break.
            if (state.getValue(CHARGED)) {
                //Attempt seal
                if (state.getValue(WAITING) && !state.getValue(SEALED)) {
                    if (canSeal(live) && !level.isClientSide()) {
                        seal(level, state, pos, live);
                    } else if (!level.isClientSide()) {
                        level.setBlockAndUpdate(pos, state.setValue(CHARGED, false).setValue(WAITING, false));
                    }
                //Attempt break
                } else if (state.getValue(SEALED)) {
                    if (!level.isClientSide()) {
                        shatter(level, pos, live);
                    } else {
                        poof(level, live);
                    }
                } else {
                    //something failed
                }
            }

            //If not charged, start the wait, continue the wait, or continue the seal
            else if (!level.isClientSide()) {
                //Start the wait
                if (!state.getValue(WAITING) && !state.getValue(SEALED)) {
                    level.setBlockAndUpdate(pos, state.setValue(WAITING, true));
                    level.scheduleTick(pos, this, 100);
                }
                //Give waiting effects
                else if (state.getValue(WAITING)) {
                    giveWaitEffects(live);
                }
                //Give sealed effects
                else if (state.getValue(SEALED)) {
                    giveSealedEffects(live);
                }
            }
        }



        //Extinguishes fire
        if (!level.isClientSide) {
            entity.setSharedFlagOnFire(false);
            entity.clearFire();
        }

    }

    //Effects
    private void giveWaitEffects(LivingEntity entity) {
        entity.addEffect(getWaitWeak());
    }

    private boolean canSeal(LivingEntity entity) {
        if (entity.hasEffect(MobEffects.WEAKNESS)) {
            if (entity.getEffect(MobEffects.WEAKNESS).getDuration() <= 5 && entity.getEffect(MobEffects.WEAKNESS).getAmplifier() == 2) {
                return true;
            }
        }
        return false;
    }

    private void seal(Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        giveSealedEffects(entity);
        Vec3i blockposition = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
        Vec3 position = Vec3.upFromBottomCenterOf(blockposition, -1);
        entity.moveTo(position);
        level.setBlockAndUpdate(pos, state.setValue(SEALED, true).setValue(WAITING, false).setValue(CHARGED, false));
        level.setBlockAndUpdate(pos.below(), state.setValue(SEALED, true).setValue(WAITING, false).setValue(CHARGED, false));
        level.scheduleTick(pos, this, 3600);
        level.scheduleTick(pos, this, 3800);
    }

    private void giveSealedEffects(LivingEntity entity) {
        entity.addEffect(getSealedSlow());
        entity.addEffect(getSealedWeak());
        entity.addEffect(getSealedFatigue());
    }

    private void shatter(Level level, BlockPos pos, LivingEntity entity) {
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        entity.removeEffect(MobEffects.DIG_SLOWDOWN);
        entity.removeEffect(MobEffects.WEAKNESS);

        entity.addEffect(getWeakness());
        entity.addEffect(getStrength());
        entity.addEffect(getSpeed());
        entity.addEffect(getSlow());

        poof(level, entity);
        level.destroyBlock(pos, false);
        level.destroyBlock(pos.below(), false);
    }

    private void breakFail(Level level, BlockPos pos) {
        level.destroyBlock(pos, false);
        level.destroyBlock(pos.below(), false);
    }

    private boolean isTop(Level level, BlockState state, BlockPos pos) {
        return (level.getBlockState(pos.below()).getBlock() == this && level.getBlockState(pos.above()).getBlock() != this);
    }



    //Audiovisual
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (level.isClientSide) {
            if (source.nextDouble() < 0.3) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();

                double rand1 = Mth.randomBetween(source, -0.1F, 1.1F);
                double rand2 = Mth.randomBetween(source, -0.1F, 1.1F);
                double rand3 = Mth.randomBetween(source, -0.1F, 1.1F);

                double d0 = (double)i + rand1;
                double d1 = (double)j + rand2;
                double d2 = (double)k + rand3;

                level.addParticle(ParticleTypes.FLAME, d0, d1, d2, (rand1 - 0.5)*.01,.01,(rand3 - 0.5)*.01);
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

                level.addParticle(ParticleTypes.FLAME, i, j, k, d0, d1, d2);
//                System.out.println("Produced poof particle at " + i + ", " + j + ", " + k + ". Velocity was " + d0 + ", " + d1 + ", " + d2);
            }
            SoundEvent soundEvent = SoundEvents.ZOMBIE_VILLAGER_CURE;
            entity.playSound(soundEvent, 1.0F, 1.2F);
        }
    }



    //CHARGE AND TICKING
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (state.getValue(CHARGED) && state.getValue(SEALED)) {
            breakFail(level, pos);
        } else {
            level.setBlockAndUpdate(pos, state.setValue(CHARGED, true));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (state.getValue(SEALED)) {
            return Shapes.block();
        }
        else return super.getCollisionShape(state, getter, pos, context);
    }

//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        if (state.getValue(SEALED)) {
//            return Shapes.block();
//        }
//        else return super.getShape(state, getter, pos, context);
//    }


//    private void unCharge(BlockState state, Level level, BlockPos pos, int directness) {
//        boolean wasCharged = state.getValue(CHARGED);
//        if (wasCharged) {
//            level.scheduleTick(pos, this, 400);
////            level.scheduleTick(pos, this, 3600);
//            level.setBlockAndUpdate(pos, state.setValue(CHARGED, false));
//        }
//        if (directness <= 1) {
//            unChargeNeighbors(state, level, pos, directness + 1);
//        }
//    }
//
//    private void unChargeNeighbors(BlockState state, Level level, BlockPos pos, int directness) {
//        BlockPos pos1 = new BlockPos(pos.getX()-1, pos.getY()-1,pos.getZ()-1);
//        BlockPos pos2 = new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1);
//        BlockPos.betweenClosedStream(pos1, pos2)
//                .filter(c -> level.getBlockState(c).getBlock().equals(alchemineBlocks.VITA_SLIME.get()))
//                .forEach(c -> unCharge(level.getBlockState(c), level, c, directness));
//    }

}
