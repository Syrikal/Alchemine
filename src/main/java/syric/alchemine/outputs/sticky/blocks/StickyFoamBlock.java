package syric.alchemine.outputs.sticky.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.outputs.general.blocks.PossiblyPermanentBlock;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class StickyFoamBlock extends Block implements net.minecraftforge.common.IForgeShearable, PossiblyPermanentBlock {
    private final double stickiness;
    private final int duration;

        public StickyFoamBlock(BlockBehaviour.Properties properties, int stick, int duration) {
            super(properties);
            this.stickiness = (double) stick;
            this.duration = duration;
            this.registerDefaultState(this.defaultBlockState().setValue(PossiblyPermanentBlock.PERMANENT, false));
        }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PossiblyPermanentBlock.PERMANENT);
    }

        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            Vec3 stuckVector = new Vec3(0.3 / stickiness, 0.1/ stickiness, 0.3 / stickiness);
//            chatPrint("Reduced speed by " + (0.4/stickiness), entity);
            entity.makeStuckInBlock(state, stuckVector);
        }

    //Stuff relating to automatic destruction
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, duration);
        }
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        if (!state.getValue(PERMANENT)) {
            level.destroyBlock(pos, false);
        }
    }

}
