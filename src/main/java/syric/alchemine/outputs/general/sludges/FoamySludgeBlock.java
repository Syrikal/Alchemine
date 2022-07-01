package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FoamySludgeBlock extends SludgeBlock {

    public FoamySludgeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getValue(WEAK_VERSION)) {
            entity.makeStuckInBlock(state, new Vec3(0.3D, 0.15D, 0.3D));
        } else {
            entity.makeStuckInBlock(state, new Vec3(0.2D, 0.05D, 0.2D));
        }
    }

}
