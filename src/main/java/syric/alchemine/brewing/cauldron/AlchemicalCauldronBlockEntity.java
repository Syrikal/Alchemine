package syric.alchemine.brewing.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.alchemineBlockEntityTypes;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalCauldronBlockEntity extends BlockEntity implements Clearable {


    public AlchemicalCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(alchemineBlockEntityTypes.ALCHEMICAL_CAULDRON.get(), pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    public void tick() {}

    public void speak(Player player) {
        chatPrint("block entity exists", player);
    }

    @Override
    public void clearContent() {}
}
