package syric.alchemine.outputs.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class LumaSlimeBlock extends AbstractVariablyBouncySlimeBlock {
    public static final BooleanProperty LIGHT = BooleanProperty.create("light");

    public LumaSlimeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIGHT, true));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        } else if (state.getValue(LIGHT)) {
            level.setBlockAndUpdate(pos, state.setValue(LIGHT, false));
            return InteractionResult.SUCCESS;
        } else {
            level.setBlockAndUpdate(pos, state.setValue(LIGHT, true));
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public int power(Level level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel) {
            if (state.getValue(LIGHT)) {
                int value = serverLevel.getMaxLocalRawBrightness(pos, 0);
                LogUtils.getLogger().info("Detected light level of " + value);
                return value;
            } else {
                int value = 15 - serverLevel.getMaxLocalRawBrightness(pos, 0);
                LogUtils.getLogger().info("Detected light level of " + value);
                return value;
            }
        }
        else {
            int value = level.getMaxLocalRawBrightness(pos, 0);
            LogUtils.getLogger().info("Level not instance of serverlevel. Detected light level of " + value);
            return value;
        }
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT);
    }

}
