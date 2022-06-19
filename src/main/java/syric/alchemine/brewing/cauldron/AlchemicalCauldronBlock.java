package syric.alchemine.brewing.cauldron;



import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.brewing.laboratory.AlchemicalAlembicBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class AlchemicalCauldronBlock extends Block implements EntityBlock {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

    //Constructor
    public AlchemicalCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LEVEL, 2));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemicalCauldronBlockEntity(pos, state);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        return EntityBlock.super.getTicker(level, state, type);
        return level.isClientSide ? null : (level0, pos, state2, blockEntity) -> ((AlchemicalAlembicBlockEntity) blockEntity).tick();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        boolean success = false;
        if (blockentity instanceof AlchemicalCauldronBlockEntity alchemicalCauldronBlockEntity) {
            if (!level.isClientSide && !player.isShiftKeyDown()) {
                success = true;
                chatPrint("block detected use", player);
                alchemicalCauldronBlockEntity.speak(player);
                BlockState blockstate;
                if (state.getValue(LEVEL) == 3) {
                    blockstate = state.setValue(LEVEL, 0);
                    level.setBlockAndUpdate(pos, blockstate);
                } else {
                    blockstate = state.setValue(LEVEL, state.getValue(LEVEL) + 1);
                    level.setBlockAndUpdate(pos, blockstate);
                }
            }
        }

        return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;

    }


//
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
//        BlockEntity blockentity = level.getBlockEntity(pos);
//        if (blockentity instanceof AlchemicalCauldronBlockEntity alchemicalCauldronBlockEntity) {
//            alchemicalCauldronBlockEntity.speak(player);
//        }
//
//        if (state.getValue(LEVEL) == 3) {
//            state.setValue(LEVEL, 0);
//        } else {
//            state.setValue(LEVEL, state.getValue(LEVEL)+1);
////            BlockState blockstate = state.setValue(LEVEL, state.getValue(LEVEL) + 1);
////            level.setBlockAndUpdate(pos, blockstate);
//        }
//
//        return InteractionResult.PASS;
//    }



    //Some necessary stuff to make shape and blockstates work
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LEVEL); }
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) { return SHAPE; }
    public VoxelShape getInteractionShape(BlockState state, BlockGetter getter, BlockPos pos) { return INSIDE; }

}
