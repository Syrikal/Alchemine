package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.AlchemineBlocks;

public class TidyFireBlock extends AbstractAlchemicalFireBlock {

    public TidyFireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    public int getFireTickDelay(RandomSource randomSource) {
        return 100 + randomSource.nextInt(20);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity)) {
            return;
        }
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setSecondsOnFire(8);
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    public Block getSelf() {
        return AlchemineBlocks.TIDY_FIRE.get();
    }

    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return 0;
    }


}
