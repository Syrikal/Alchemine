package syric.alchemine.outputs.fire.blocks;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AbstractAlchemicalFireBlock extends Block {
    //Max age is 15.
    //Ignition chances: Instant-60, easy-30, medium-15, hard-5.
    //Burn chances: Instant-100, easy-60, medium-20, hard-5.
    //By default, sets entities on fire for 8 seconds.
    //AABB Offset (idk) is 1.0F.

    //Blockstate Properties
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> entry.getKey() != Direction.DOWN).collect(Util.toMap());

    //Shapes
    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private final Map<BlockState, VoxelShape> shapesCache;

    //Burning
    private final Object2IntMap<Block> igniteOdds = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Block> burnOdds = new Object2IntOpenHashMap<>();
    private final float fireDamage;


    public AbstractAlchemicalFireBlock(BlockBehaviour.Properties properties, float damage) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(UP, Boolean.FALSE));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().filter((state) -> {
            return state.getValue(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), AbstractAlchemicalFireBlock::calculateShape)));
        this.fireDamage = damage;
    }

    //SHAPE AND STATES
    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (state.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    //Extinguishes or changes state if the block it's attached to breaks.
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        return this.canSurvive(state, level, pos1) ? this.getStateWithAge(level, pos1, state.getValue(AGE)) : Blocks.AIR.defaultBlockState();
    }
    //Returns the appropriate shape based on where the fire is.
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state.setValue(AGE, 0));
    }

    //Custom gSFP that only needs a BlockGetter and a BlockPos.
    public BlockState getStateForPlacement(BlockGetter getter, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = getter.getBlockState(belowPos);
        if (!this.canBlockIgnite(getter, pos, Direction.UP) && !belowState.isFaceSturdy(getter, belowPos, Direction.UP)) {
            BlockState modifiedState = this.defaultBlockState();

            for(Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                if (booleanproperty != null) {
                    modifiedState = modifiedState.setValue(booleanproperty, this.canBlockIgnite(getter, pos.relative(direction), direction.getOpposite()));
                }
            }

            return modifiedState;
        } else {
            return this.defaultBlockState();
        }
    }
    //The standard gSFP takes a context. We redirect it to the custom one.
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStateForPlacement(context.getLevel(), context.getClickedPos());
    }


    //Returns the blockstate appropriate for the given position, with the given age.
    private BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
        BlockState blockstate = getStateForPlacement(level, pos);
        return blockstate.is(this.getSelf()) ? blockstate.setValue(AGE, age) : blockstate;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

    //For direct placement, as with flint and steel or a fire charge.
    public boolean canBePlacedAt(Level level, BlockPos pos, Direction direction) {
        BlockState blockstate = level.getBlockState(pos);
        if (!blockstate.isAir()) {
            return false;
        } else {
            return getState(level, pos).canSurvive(level, pos) || isPortal(level, pos, direction);
        }
    }
    //Only referenced in canBePlacedAt. Should return itself. Probably does?
    public BlockState getState(BlockGetter getter, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = getter.getBlockState(blockpos);
        return getStateForPlacement(getter, pos);
//        return SoulFireBlock.canSurviveOnBlock(blockstate) ? Blocks.SOUL_FIRE.defaultBlockState() : Blocks.FIRE.defaultBlockState();
    }


    //ANIMATION
    //Creates particles and crackling sounds.
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (source.nextInt(24) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + source.nextFloat(), source.nextFloat() * 0.7F + 0.3F, false);
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        //Replace canIgniteStored(belowState) with canBlockIgnite(level, pos, face)


        if (!this.canBlockIgnite(level, belowPos, Direction.UP) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            if (this.canBlockIgnite(level, pos.west(), Direction.EAST)) {
                for(int j = 0; j < 2; ++j) {
                    double d3 = (double)pos.getX() + source.nextDouble() * (double)0.1F;
                    double d8 = (double)pos.getY() + source.nextDouble();
                    double d13 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.east(), Direction.WEST)) {
                for(int k = 0; k < 2; ++k) {
                    double d4 = (double)(pos.getX() + 1) - source.nextDouble() * (double)0.1F;
                    double d9 = (double)pos.getY() + source.nextDouble();
                    double d14 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.north(), Direction.SOUTH)) {
                for(int l = 0; l < 2; ++l) {
                    double d5 = (double)pos.getX() + source.nextDouble();
                    double d10 = (double)pos.getY() + source.nextDouble();
                    double d15 = (double)pos.getZ() + source.nextDouble() * (double)0.1F;
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.south(), Direction.NORTH)) {
                for(int i1 = 0; i1 < 2; ++i1) {
                    double d6 = (double)pos.getX() + source.nextDouble();
                    double d11 = (double)pos.getY() + source.nextDouble();
                    double d16 = (double)(pos.getZ() + 1) - source.nextDouble() * (double)0.1F;
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.canBlockIgnite(level, pos.above(), Direction.DOWN)) {
                for(int j1 = 0; j1 < 2; ++j1) {
                    double d7 = (double)pos.getX() + source.nextDouble();
                    double d12 = (double)(pos.getY() + 1) - source.nextDouble() * (double)0.1F;
                    double d17 = (double)pos.getZ() + source.nextDouble();
                    level.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for(int i = 0; i < 3; ++i) {
                double d0 = (double)pos.getX() + source.nextDouble();
                double d1 = (double)pos.getY() + source.nextDouble() * 0.5D + 0.5D;
                double d2 = (double)pos.getZ() + source.nextDouble();
                level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }


        //OLD VERSION!!!
//        if (!this.canIgniteStored(belowState) && !belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
//            if (this.canIgniteStored(level.getBlockState(pos.west()))) {
//                for(int j = 0; j < 2; ++j) {
//                    double d3 = (double)pos.getX() + source.nextDouble() * (double)0.1F;
//                    double d8 = (double)pos.getY() + source.nextDouble();
//                    double d13 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.east()))) {
//                for(int k = 0; k < 2; ++k) {
//                    double d4 = (double)(pos.getX() + 1) - source.nextDouble() * (double)0.1F;
//                    double d9 = (double)pos.getY() + source.nextDouble();
//                    double d14 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d4, d9, d14, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.north()))) {
//                for(int l = 0; l < 2; ++l) {
//                    double d5 = (double)pos.getX() + source.nextDouble();
//                    double d10 = (double)pos.getY() + source.nextDouble();
//                    double d15 = (double)pos.getZ() + source.nextDouble() * (double)0.1F;
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d5, d10, d15, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.south()))) {
//                for(int i1 = 0; i1 < 2; ++i1) {
//                    double d6 = (double)pos.getX() + source.nextDouble();
//                    double d11 = (double)pos.getY() + source.nextDouble();
//                    double d16 = (double)(pos.getZ() + 1) - source.nextDouble() * (double)0.1F;
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d6, d11, d16, 0.0D, 0.0D, 0.0D);
//                }
//            }
//
//            if (this.canIgniteStored(level.getBlockState(pos.above()))) {
//                for(int j1 = 0; j1 < 2; ++j1) {
//                    double d7 = (double)pos.getX() + source.nextDouble();
//                    double d12 = (double)(pos.getY() + 1) - source.nextDouble() * (double)0.1F;
//                    double d17 = (double)pos.getZ() + source.nextDouble();
//                    level.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0D, 0.0D, 0.0D);
//                }
//            }
//        } else {
//            for(int i = 0; i < 3; ++i) {
//                double d0 = (double)pos.getX() + source.nextDouble();
//                double d1 = (double)pos.getY() + source.nextDouble() * 0.5D + 0.5D;
//                double d2 = (double)pos.getZ() + source.nextDouble();
//                level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
//            }
//        }

    }
    //Doesn't make particles when broken
    @Override
    public void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
    }


    //BURNING ENTITIES
    //Damages entities and sets them on fire
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setSecondsOnFire(8);
            }
        }

        entity.hurt(DamageSource.IN_FIRE, this.fireDamage);
        super.entityInside(state, level, pos, entity);
    }
    public float getFireDamage() {
        return fireDamage;
    }


    //NETHER PORTALS
    //Checks for portals and whether this block can survive there
    public void onPlace(BlockState state1, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!state2.is(state1.getBlock())) {
            if (inPortalDimension(level)) {
                Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X);
                optional = net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(level, pos, optional);
                if (optional.isPresent()) {
                    optional.get().createPortalBlocks();
                    return;
                }
            }

            if (!state1.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }

        }
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
    }
    //Just checks if it's in overworld or nether
    private static boolean inPortalDimension(Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }
    //Attempts to find a portal shape
    private static boolean isPortal(Level level, BlockPos pos, Direction dir) {
        if (!inPortalDimension(level)) {
            return false;
        } else {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
            boolean flag = false;

            for(Direction direction : Direction.values()) {
                if (level.getBlockState(blockpos$mutableblockpos.set(pos).move(direction)).is(Blocks.OBSIDIAN)) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                return false;
            } else {
                Direction.Axis direction$axis = dir.getAxis().isHorizontal() ? dir.getCounterClockWise().getAxis() : Direction.Plane.HORIZONTAL.getRandomAxis(level.random);
                return PortalShape.findEmptyPortalShape(level, pos, direction$axis).isPresent();
            }
        }
    }



    //SPREAD AND LIFECYCLE
    //How long between ticks
    public int getFireTickDelay(RandomSource randomSource) {
        return 30 + randomSource.nextInt(10);
    }

    //Does lots of stuff. Includes spreading and extinguishing.
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
        //Schedules the next tick
        level.scheduleTick(firePos, this, getFireTickDelay(level.random));

        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            //Removes block if it can't survive.
            if (!fireState.canSurvive(level, firePos)) {
                level.removeBlock(firePos, false);
            }

            //Check if the below block is an infinite fire source, which will prevent extinguishing.
            BlockState belowBlockState = level.getBlockState(firePos.below());
            boolean source = belowBlockState.isFireSource(level, firePos, Direction.UP);

            int age = fireState.getValue(AGE);

            //Extinguish if it's raining and not on an infinite source.
            if (!source && level.isRaining() && this.isNearRain(level, firePos) && randSource.nextFloat() < 0.2F + (float)age * 0.03F) {
                level.removeBlock(firePos, false);
            } else {
                //Age the fire.
                int newage = Math.min(15, age + randSource.nextInt(3) / 2);
                if (age != newage) {
                    fireState = fireState.setValue(AGE, newage);
                    level.setBlock(firePos, fireState, 4);
                }

                //If not on an infinite source block:
                if (!source) {
                    //Extinguish if it's on an invalid block (e.g. stone for normal fire) and at least age 3
                    if (!this.canPositionIgnite(level, firePos)) {
                        BlockPos belowPos = firePos.below();
                        if (!level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP) || age > 3) {
                            level.removeBlock(firePos, false);
                        }
                        return;
                    }

                    //If it's age 15 and it's not on top of a valid burning block, chance to extinguish.
                    if (age == 15 && randSource.nextInt(4) == 0 && !this.canBlockIgnite(level, firePos.below(), Direction.UP)) {
                        level.removeBlock(firePos, false);
                        return;
                    }
                }

                //Try to burn (i.e. destroy) all the blocks immediately around it.
                int humidityModifier = level.isHumidAt(firePos) ? -50 : 0;
                this.attemptToBurn(level, firePos.east(), 300 + humidityModifier, randSource, age, Direction.WEST);
                this.attemptToBurn(level, firePos.west(), 300 + humidityModifier, randSource, age, Direction.EAST);
                this.attemptToBurn(level, firePos.below(), 250 + humidityModifier, randSource, age, Direction.UP);
                this.attemptToBurn(level, firePos.above(), 250 + humidityModifier, randSource, age, Direction.DOWN);
                this.attemptToBurn(level, firePos.north(), 300 + humidityModifier, randSource, age, Direction.SOUTH);
                this.attemptToBurn(level, firePos.south(), 300 + humidityModifier, randSource, age, Direction.NORTH);

                //Try to set blocks on fire.
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for(int xOffset = -1; xOffset <= 1; ++xOffset) {
                    for(int zOffset = -1; zOffset <= 1; ++zOffset) {
                        for(int yOffset = -1; yOffset <= 4; ++yOffset) {
                            if (xOffset != 0 || yOffset != 0 || zOffset != 0) {

                                //We create positionIgniteChance and rollCap. Then we take a random number from 0
                                //to rollCap and ignite if it's less than positionIgniteChance.
                                int rollCap = 100;
                                if (yOffset > 1) {
                                    rollCap += (yOffset - 1) * 100;
                                }

                                blockpos$mutableblockpos.setWithOffset(firePos, xOffset, yOffset, zOffset);

                                int positionIgniteChance = this.getPositionIgniteChance(level, blockpos$mutableblockpos);
                                if (positionIgniteChance > 0) {

                                    int newIgniteChance = modifiedIgniteChance(positionIgniteChance, age, level, firePos);

                                    if (newIgniteChance > 0 && randSource.nextInt(rollCap) <= newIgniteChance && (!level.isRaining() || !this.isNearRain(level, blockpos$mutableblockpos))) {
                                        int spreadAge = Math.min(15, age + randSource.nextInt(5) / 4);
                                        level.setBlock(blockpos$mutableblockpos, this.getStateWithAge(level, blockpos$mutableblockpos, spreadAge), 3);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    //This tries to burn something. It sometimes puts fire there if it can, otherwise just destroys whatever's there.
    private void attemptToBurn(Level level, BlockPos pos, int baseRollCap, RandomSource source, int fireAge, Direction face) {
//        chatPrint("Attempting to spread fire adjacent", level);
        int burnChance = getBlockBurnChance(level, pos, face);
        if (source.nextInt(baseRollCap) < burnChance) {
            BlockState blockstate = level.getBlockState(pos);
            if (source.nextInt(baseRollCap + 10) < 5 && !level.isRainingAt(pos)) {
//                chatPrint("Successfully spread fire adjacent, placing", level);
                //Spread fire to the position.
                int ageForPlacement = Math.min(baseRollCap + source.nextInt(5) / 4, 15);
                level.setBlock(pos, this.getStateWithAge(level, pos, ageForPlacement), 3);
            } else {
                level.removeBlock(pos, false);
            }
            blockstate.onCaughtFire(level, pos, face, null);
        }
    }

    //Utility methods for spreading and dousing
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 40;
        baseIgniteChance += level.getDifficulty().getId() * 7;
        baseIgniteChance /= (age + 30);
        if (level.isHumidAt(pos)) {baseIgniteChance /= 2;}
        return baseIgniteChance;
    }
    protected boolean isNearRain(Level level, BlockPos pos) {
        return level.isRainingAt(pos) || level.isRainingAt(pos.west()) || level.isRainingAt(pos.east()) || level.isRainingAt(pos.north()) || level.isRainingAt(pos.south());
    }


    //FLAMMABILITY AND BURN CHANCE
    //This section is difficult because Forge has added a lot of hooks to this.

    //CAN IT CATCH FIRE AT ALL?
    //Returns true if it can catch fire.
    //STANDARD USE. Includes forge. Override with canIgniteStored for nonstandard fire.
    public boolean canBlockIgnite(BlockGetter level, BlockPos pos, Direction face) {
        return level.getBlockState(pos).isFlammable(level, pos, face);
    }
    //Returns ignition possibility using stored ignition values. Use this for nonstandard fire.
    @Deprecated //Forge: Use canCatchFire with more context
    protected boolean canIgniteStored(BlockState state) {
        return this.getIgniteChanceStored(state) > 0;
    }
    //Checks if a state can catch fire.
    //Returns true if adjacent to any block that can catch fire with canBlockIgnite.
    private boolean canPositionIgnite(BlockGetter getter, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (this.canBlockIgnite(getter, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }


    //HOW LIKELY IS THIS THING TO CATCH FIRE?
    //Gets the ignite chance of a blockstate.
    //STANDARD USE. Includes Forge. Override with getIgniteChanceStored for nonstandard fire.
    public int getBlockIgniteChance(BlockGetter level, BlockPos pos, Direction face) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            return 0;
        } else {
            return state.getFireSpreadSpeed(level, pos, face);
        }
    }
    @Deprecated //Forge: Use IForgeBlockState.getFireSpreadSpeed
    //Returns ignition chance with stored values. Use this for nonstandard fire.
    public int getIgniteChanceStored(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            return 0;
        } else {
            return this.igniteOdds.getInt(state.getBlock());
        }
    }
    //Returns the maximum flammability of all adjacent blocks to a given position,
    //to determine the chance that fire will spread to that position. Uses getBlockIgniteChance.
    private int getPositionIgniteChance(LevelReader levelReader, BlockPos pos) {
        if (!levelReader.isEmptyBlock(pos)) {
            return 0;
        } else {
            int igniteChance = 0;

            for(Direction direction : Direction.values()) {
                BlockState blockstate = levelReader.getBlockState(pos.relative(direction));
//                igniteChance = Math.max(getBlockStateIgniteOdds(blockstate), igniteChance);
                int contender = getBlockIgniteChance(levelReader, pos.relative(direction), direction.getOpposite());
                igniteChance = Math.max(contender, igniteChance);
            }
            return igniteChance;
        }
    }


    //HOW LIKELY IS THIS THING TO BE DESTROYED?
    //Gets the chance of a block to be destroyed.
    //STANDARD USE. Includes Forge. Override with getBurnChanceStored for nonstandard fire.
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return level.getBlockState(pos).getFlammability(level, pos, face);
    }
    @Deprecated //Forge: Use IForgeBlockState.getFlammability, Public for default implementation only.
    //This is chance to be destroyed.
    //Returns 0 if it's waterlogged, stored burn odds otherwise. Use this for nonstandard fire.
    public int getBurnChanceStored(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            return 0;
        } else {
            return this.burnOdds.getInt(state.getBlock());
        }
    }









    //PLACEMENT AND DESTRUCTION
    //CanSurvive: either the block below must be sturdy, or the block's position must be igniteable.
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || this.canPositionIgnite(level, pos);
    }
    //Does a level event when player destroys it
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            level.levelEvent((Player)null, 1009, pos, 0);
        }

        super.playerWillDestroy(level, pos, state, player);
    }





    //CREATING PRIVATE FLAMMABILITY
    //Set which blocks are flammable, and how much.
    public void setFlammable(Block block, int ignitionChance, int burnChance) {
        if (block == Blocks.AIR) throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        this.igniteOdds.put(block, ignitionChance);
        this.burnOdds.put(block, burnChance);
    }
    public static void defaultFireBootStrap(AbstractAlchemicalFireBlock alchemicalFireBlock) {
        alchemicalFireBlock.setFlammable(Blocks.OAK_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_PLANKS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_SLAB, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_FENCE_GATE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_FENCE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_STAIRS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_LOG, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_WOOD, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_ROOTS, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.OAK_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.MANGROVE_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BOOKSHELF, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.TNT, 15, 100);
        alchemicalFireBlock.setFlammable(Blocks.GRASS, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.FERN, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.DEAD_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.SUNFLOWER, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LILAC, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ROSE_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.PEONY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.TALL_GRASS, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LARGE_FERN, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.DANDELION, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.POPPY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ALLIUM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.AZURE_BLUET, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.RED_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.PINK_TULIP, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.CORNFLOWER, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WITHER_ROSE, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.MAGENTA_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.YELLOW_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIME_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.PINK_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GRAY_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.CYAN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.PURPLE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BROWN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GREEN_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.RED_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BLACK_WOOL, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.VINE, 15, 100);
        alchemicalFireBlock.setFlammable(Blocks.COAL_BLOCK, 5, 5);
        alchemicalFireBlock.setFlammable(Blocks.HAY_BLOCK, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.TARGET, 15, 20);
        alchemicalFireBlock.setFlammable(Blocks.WHITE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.ORANGE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.MAGENTA_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.YELLOW_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIME_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.PINK_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.GRAY_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.CYAN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.PURPLE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BLUE_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BROWN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.GREEN_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.RED_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.BLACK_CARPET, 60, 20);
        alchemicalFireBlock.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BAMBOO, 60, 60);
        alchemicalFireBlock.setFlammable(Blocks.SCAFFOLDING, 60, 60);
        alchemicalFireBlock.setFlammable(Blocks.LECTERN, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.COMPOSTER, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BEEHIVE, 5, 20);
        alchemicalFireBlock.setFlammable(Blocks.BEE_NEST, 30, 20);
        alchemicalFireBlock.setFlammable(Blocks.AZALEA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.FLOWERING_AZALEA_LEAVES, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.CAVE_VINES, 15, 60);
        alchemicalFireBlock.setFlammable(Blocks.CAVE_VINES_PLANT, 15, 60);
        alchemicalFireBlock.setFlammable(Blocks.SPORE_BLOSSOM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.AZALEA, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.FLOWERING_AZALEA, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.BIG_DRIPLEAF, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.BIG_DRIPLEAF_STEM, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.SMALL_DRIPLEAF, 60, 100);
        alchemicalFireBlock.setFlammable(Blocks.HANGING_ROOTS, 30, 60);
        alchemicalFireBlock.setFlammable(Blocks.GLOW_LICHEN, 15, 100);
    }

    public Block getSelf() {
        return Blocks.FIRE;
    }


}
