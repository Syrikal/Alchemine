package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.AlchemineBlocks;

public class CozyFireBlock extends AbstractAlchemicalFireBlock {

    public CozyFireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    }

    //Doesn't extinguish or spread.
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
    }

    public Block getSelf() {
        return AlchemineBlocks.TIDY_FIRE.get();
    }

}
