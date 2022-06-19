package syric.alchemine.outputs.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import syric.alchemine.setup.alchemineBlocks;

public class CrashPadEffect implements AlchemicalEffect {

    public CrashPadEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();
        BlockPos pos1 = new BlockPos(pos.getX()-2, pos.getY(),pos.getZ()-2);
        BlockPos pos2 = new BlockPos(pos.getX()+2, pos.getY(), pos.getZ()+2);
        BlockPos.betweenClosedStream(pos1, pos2)
                .filter(c -> distance(pos, c) < 6)
                .filter(c -> replaceable(level.getBlockState(c)))
                .forEach(c -> level.setBlockAndUpdate(c, alchemineBlocks.CRASH_PAD.get().defaultBlockState()));
    }
    private static double distance(BlockPos pos, BlockPos other) {
        double xdist = Math.pow(pos.getX() - other.getX(), 2);
        double zdist = Math.pow(pos.getZ() - other.getZ(), 2);
        return xdist + zdist;
    }

    private static boolean replaceable(BlockState state) {
        return state.getMaterial().getPushReaction() == PushReaction.DESTROY || state.getMaterial().equals(Material.AIR);
    }


}
