package syric.alchemine.outputs.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.setup.AlchemineBlocks;

public class InstantShelterEffect implements AlchemicalEffect {

    public InstantShelterEffect(){};

    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();
        BlockPos pos1 = new BlockPos(pos.getX()-4, pos.getY()-4,pos.getZ()-4);
        BlockPos pos2 = new BlockPos(pos.getX()+4, pos.getY()+4, pos.getZ()+4);
        BlockPos.betweenClosedStream(pos1, pos2)
                .filter(c -> distance(pos, c) < 8)
                .filter(c -> distance(pos, c) > 3 )
                .filter(c -> replaceable(level.getBlockState(c)))
                .forEach(c -> {
                    level.destroyBlock(c, true);
                    level.setBlockAndUpdate(c, AlchemineBlocks.SHELL_SLIME.get().defaultBlockState());
                });
    }

    private static double distance(BlockPos pos, BlockPos other) {
        double xdist = Math.pow(pos.getX() - other.getX(), 2);
        double zdist = Math.pow(pos.getZ() - other.getZ(), 2);
        double ydist = Math.pow(pos.getY() - other.getY(), 2);
        return xdist + zdist + ydist;
    }

    private static boolean replaceable(BlockState state) {
        return state.getMaterial().getPushReaction() == PushReaction.DESTROY || state.getMaterial().equals(Material.AIR);
    }


}
