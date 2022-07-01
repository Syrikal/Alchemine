package syric.alchemine.outputs.general.sludges;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.setup.AlchemineBlockEntityTypes;

import static syric.alchemine.outputs.general.sludges.SludgeBlock.WEAK_VERSION;
import static syric.alchemine.util.ChatPrint.chatPrint;

public class StygianSludgeBlockEntity extends BlockEntity {


    public StygianSludgeBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemineBlockEntityTypes.STYGIAN.get(), pos, state);
    }

    public void tick() {
        BlockState state = this.getBlockState();
        if (state.getValue(WEAK_VERSION)) {
            return;
        }
        BlockPos pos = this.getBlockPos();
        int variation = 3;
        BlockPos pos1 = new BlockPos(pos.getX()-variation, pos.getY()-variation,pos.getZ()-variation);
        BlockPos pos2 = new BlockPos(pos.getX()+variation, pos.getY()+variation, pos.getZ()+variation);

        assert level != null;
        for (Entity entity : level.getEntities(null, new AABB(pos1, pos2))) {
            if (entity.position().vectorTo(new Vec3(pos.getX(), pos.getY(), pos.getZ())).length() <= ((double) variation / 2D)) {
                entity.setTicksFrozen(entity.getTicksFrozen() + 1);
            }
        }

    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    public void speak(Player player) {
        chatPrint("block entity exists", player);
    }
}
