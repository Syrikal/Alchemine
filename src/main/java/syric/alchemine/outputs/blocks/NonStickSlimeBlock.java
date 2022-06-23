package syric.alchemine.outputs.blocks;

import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

public class NonStickSlimeBlock extends SlimeBlock implements IForgeBlock {
    public NonStickSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSlimeBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean isStickyBlock(BlockState state) {
        return false;
    }

    @Override
    public boolean canStickTo(BlockState state, BlockState other) {
        return other.isStickyBlock();
    }
}
