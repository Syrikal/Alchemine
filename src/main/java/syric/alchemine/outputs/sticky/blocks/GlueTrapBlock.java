package syric.alchemine.outputs.sticky.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class GlueTrapBlock extends StickyFlatBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty PRIMED = BooleanProperty.create("primed");

    public GlueTrapBlock(Properties properties, int stick, int dur) {
        super(properties, stick, dur);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false).setValue(PRIMED, false));
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, PRIMED);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 entityDistance = new Vec3(entity.getX() - pos.getX() - 0.5, 0, entity.getZ() - pos.getZ() - 0.5);
        if (entity.getY() > pos.getY() + 0.7) {
            return;
        } else if (state.getValue(ACTIVE)) {
            Vec3 stuckVector = new Vec3(0.05, 1, 0.05);
            entity.makeStuckInBlock(state, stuckVector);
//            chatPrint("Entity stuck", entity);
        } else if ((entityDistance.length() < 0.25) && !state.getValue(ACTIVE)) {
            if (level.isClientSide) { return; }
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
//            chatPrint("Entity close to center, activating glue", entity);
        }  else {
            if (level.isClientSide) { return; }
            level.setBlockAndUpdate(pos, state.setValue(PRIMED, true));
            level.scheduleTick(pos, this, 100);
//            chatPrint("Starting five-second countdown to activation", entity);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (!level.isClientSide() && state.getValue(PRIMED)) {
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
            chatPrint("Time up, activating glue", level);
        }
    }


}