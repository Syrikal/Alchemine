package syric.alchemine.brewing.cauldron;

import net.minecraft.core.BlockPos;
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
    public void clearContent() {

    }

    public void speak(Player player) {
        chatPrint("tile entity exists", player);
    }

}
