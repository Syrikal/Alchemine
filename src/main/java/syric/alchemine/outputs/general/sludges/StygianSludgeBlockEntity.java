package syric.alchemine.outputs.general.sludges;

import com.mojang.logging.LogUtils;
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
        int range = 5;
        BlockPos pos1 = new BlockPos(pos.getX()-range, pos.getY()-range,pos.getZ()-range);
        BlockPos pos2 = new BlockPos(pos.getX()+range, pos.getY()+range, pos.getZ()+range);

        assert level != null;
        StringBuilder sb = new StringBuilder();
        for (Entity entity : level.getEntities(null, new AABB(pos1, pos2))) {
            if (entity.position().vectorTo(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)).length() <= ((double) range)) {
                StygianSludgeBlock.freeze(level, entity, 1, true);
            }
        }
//        LogUtils.getLogger().info("Stygian Sludge block entity attempting to freeze nearby entities");
        if (sb.length() > 0) {
            LogUtils.getLogger().info(sb.toString());
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
