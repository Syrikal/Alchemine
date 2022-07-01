package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.outputs.general.alchemicaleffects.effectsUtil;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.ReplaceablesFilter;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class VigorousSludgeBlock extends SludgeBlock {
    public static final IntegerProperty SPREAD = IntegerProperty.create("spread", 0, 10);

    public VigorousSludgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SPREAD, 5));
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        double rand = source.nextDouble();

        int spread = state.getValue(SPREAD);
        if (spread <= 0) {
            return;
        }
        double spreadChance = ((double) spread / 10D) + 0.4;
//        chatPrint("Vigorous Sludge has randomly ticked. Grow chance is " + spreadChance, level);

        if ((!state.getValue(WEAK_VERSION) && rand < spreadChance) || rand < spreadChance * 0.7D) {
//            chatPrint("Vigorous Sludge is attempting to grow", level);
            int numberReplacements = 0;
            for (Direction direction : Direction.allShuffled(source)) {
                if (numberReplacements >= 2 || (state.getValue(WEAK_VERSION) && numberReplacements >= 1)) {
//                    chatPrint("Enough replacements, breaking", level);
                    break;
                }
                int targetSpread = spread - 1;
                BlockState placeState = state.setValue(SPREAD, targetSpread);
                BlockPos candidate = pos.relative(direction);
//                chatPrint("Candidate direction: " + direction, level);
                BlockState candidateState = level.getBlockState(candidate);
//                chatPrint("Candidate block: " + candidateState.getBlock(), level);
                ReplaceablesFilter filter = effectsUtil.BLOCK_REPLACEABLE;
                ReplaceablesFilter air = (c) -> (c.getMaterial() == Material.AIR);
                if (air.check(candidateState)) {
//                    chatPrint("Passed filter, placing", level);
                    level.destroyBlock(candidate, true);
                    level.setBlockAndUpdate(candidate, placeState);
                    numberReplacements++;
                } else if (filter.check(candidateState) && source.nextDouble() < 0.2) {
                    level.destroyBlock(candidate, true);
                    level.setBlockAndUpdate(candidate, placeState);
                    numberReplacements++;
                }
            }
            int newSpread = state.getValue(WEAK_VERSION) ? Math.max(spread - 3, 0) : Math.max(spread - 2, 0);
            level.setBlockAndUpdate(pos, state.setValue(SPREAD, newSpread));
        }

    }

    public void destroy(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        RandomSource source = RandomSource.create();
        if (!state.getValue(WEAK_VERSION) && source.nextDouble() < 0.01 * (8-state.getValue(SPREAD))) {
            if (levelAccessor instanceof ServerLevel level) {
                Slime entity = EntityType.SLIME.create(level);
                BlockPos position = pos.offset(0.5, 0.5, 0.5);
                assert entity != null;
                entity.absMoveTo(position.getX(), position.getY(), position.getZ());
                entity.setSize(2, true);
                entity.setDeltaMovement(Vec3.ZERO);
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPREAD, WEAK_VERSION);
    }

}
