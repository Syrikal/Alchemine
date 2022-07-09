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

    //Ticks slower
    public int getFireTickDelay(RandomSource randomSource) {
        return 100 + randomSource.nextInt(20);
    }

    //Only burns items
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

    //Spreads slower
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 20;
        baseIgniteChance /= (age * 3 + 30);
        if (level.isHumidAt(pos)) {baseIgniteChance /= 3;}
        if (age >= 7) {
            baseIgniteChance /= 3;
        }
        if (age == 15) {
            return 0;
        }
        baseIgniteChance /= 2;
        return baseIgniteChance;
    }

    public Block getSelf() {
        return AlchemineBlocks.TIDY_FIRE.get();
    }

    //Doesn't burn things
    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return 0;
    }


}
