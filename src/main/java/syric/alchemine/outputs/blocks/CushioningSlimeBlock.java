package syric.alchemine.outputs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import syric.alchemine.setup.AlchemineBlocks;

public class CushioningSlimeBlock extends HalfTransparentBlock {

    public static final BooleanProperty STABLE = BooleanProperty.create("stable");

    public CushioningSlimeBlock(BlockBehaviour.Properties properties, boolean stable) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STABLE, stable));
    }

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity faller, float num) {
        if (faller.isSuppressingBounce()) {
            super.fallOn(level, state, pos, faller, num);
        } else {
            faller.causeFallDamage(num, 0.0F, DamageSource.FALL);
        }
        if (!level.isClientSide && num > 3 && !state.getValue(STABLE)) {
            level.destroyBlock(pos, true);
            BlockPos pos1 = new BlockPos(pos.getX()-4, pos.getY()-1,pos.getZ()-4);
            BlockPos pos2 = new BlockPos(pos.getX()+4, pos.getY(), pos.getZ()+4);
            BlockPos.betweenClosedStream(pos1, pos2)
                    .filter(c -> distance(pos, c) < 21)
                    .filter(c -> level.getBlockState(c).getBlock().equals(AlchemineBlocks.CRASH_PAD.get()))
                    .forEach(c -> level.destroyBlock(c, false));
        }
    }

    private double distance(BlockPos pos, BlockPos other) {
        double xdist = Math.pow(pos.getX() - other.getX(), 2);
        double ydist = Math.pow(3*(pos.getY() - other.getY()), 2);
        double zdist = Math.pow(pos.getZ() - other.getZ(), 2);
        return xdist + ydist + zdist;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STABLE);
    }

}
