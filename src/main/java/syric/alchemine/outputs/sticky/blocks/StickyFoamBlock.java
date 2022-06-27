package syric.alchemine.outputs.sticky.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class StickyFoamBlock extends Block implements net.minecraftforge.common.IForgeShearable {
    private final double stickiness;

        public StickyFoamBlock(BlockBehaviour.Properties properties, int stick) {
            super(properties);
            this.stickiness = (double) stick;
        }

        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            Vec3 stuckVector = new Vec3(0.3 / stickiness, 0.1/ stickiness, 0.3 / stickiness);
//            chatPrint("Reduced speed by " + (0.4/stickiness), entity);
            entity.makeStuckInBlock(state, stuckVector);
        }

}
