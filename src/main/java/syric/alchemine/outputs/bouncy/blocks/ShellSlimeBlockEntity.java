package syric.alchemine.outputs.bouncy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.AlchemineBlockEntityTypes;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class ShellSlimeBlockEntity extends BlockEntity {
    protected int breakTime = 0;
    protected int lastBreakProgress = -1;
    protected int timeToBreak = 200;


    public ShellSlimeBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemineBlockEntityTypes.SHELL_SLIME.get(), pos, state);
    }

    public int getBreakTime() {
        return breakTime;
    }

    public int getLastBreakProgress() {
        return lastBreakProgress;
    }

    public int getTimeToBreak() {
        return timeToBreak;
    }

    public void setLastBreakProgress(int i) {
        lastBreakProgress = i;
    }

    public void increaseBreakTime() {
        breakTime++;
    }

    public void resetBreakTime() {
        breakTime = 0;
    }


    public void speak(Player player) {
        chatPrint("block entity exists", player);
    }
}
