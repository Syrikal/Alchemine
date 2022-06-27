package syric.alchemine.outputs.bouncy.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

public class ChromaSlimeBlock extends SlimeBlock implements IForgeBlock {
    public ChromaSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSlimeBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    @Override
    public boolean canStickTo(BlockState state, BlockState other) {
        if (other.getBlock() == Blocks.HONEY_BLOCK) { return false; }
        else { return state.isStickyBlock() || other.isStickyBlock(); }
    }
}
