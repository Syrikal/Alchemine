package syric.alchemine.outputs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class ShellSlimeBlock<pathfindable> extends Block {
    public static final IntegerProperty HEALTH = IntegerProperty.create("state", 1, 4);

    public ShellSlimeBlock(BlockBehaviour.Properties properties, boolean stable) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HEALTH, 4));
    }


    public void takeDamage(int damage) {

    }

    public void distributeDamage(Level level, BlockPos pos, int damage) {

    }

    public void breakOneLevel(Level level, BlockPos pos, BlockState state) {
        int currentHealth = state.getValue(HEALTH);
        level.setBlockAndUpdate(pos, state.setValue(HEALTH, currentHealth - 1));

    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return true;
    }


    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEALTH);
    }


}