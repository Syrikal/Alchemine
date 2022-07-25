package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import syric.alchemine.setup.AlchemineBlocks;

public class WardingFireBlock extends AbstractAlchemicalFireBlock {
    protected static final VoxelShape SHAPE1 = Block.box(7.9, 0.0, 0.0, 8.1, 24.0, 16.0);
    protected static final VoxelShape SHAPE2 = Block.box(0.0, 0.0, 7.9, 16.0, 24.0, 8.1);


    public WardingFireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    //Ticks slower
    public int getFireTickDelay(RandomSource randomSource) {
        return 100 + randomSource.nextInt(20);
    }

    //Doesn't burn items
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setSecondsOnFire(8);
            }
        }

        //Make things flee?

        super.entityInside(state, level, pos, entity);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.or(SHAPE1, SHAPE2);
    }


    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }


    //Doesn't spread
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        return 0;
    }

    public Block getSelf() {
        return AlchemineBlocks.WARDING_FIRE.get();
    }


    //Can't burn blocks
    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return 0;
    }


}
