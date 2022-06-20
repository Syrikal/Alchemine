package syric.alchemine.brewing.laboratory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import syric.alchemine.setup.AlchemineBlockEntityTypes;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalGrinderBlock extends Block implements EntityBlock {

    //Constructor
    public AlchemicalGrinderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AlchemineBlockEntityTypes.GRINDER.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        return EntityBlock.super.getTicker(level, state, type);
        return level.isClientSide ? null : (level0, pos, state2, blockEntity) -> ((AlchemicalGrinderBlockEntity) blockEntity).tick();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        final var blockentity = (AlchemicalGrinderBlockEntity) level.getBlockEntity(pos);
        final var success = blockentity != null;
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            chatPrint("block detected use", player);
            if (success) {
                blockentity.speak(player);
            }
        }

        return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;

    }
}
