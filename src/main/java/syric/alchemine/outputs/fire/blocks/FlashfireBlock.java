package syric.alchemine.outputs.fire.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.function.Function;
import java.util.stream.Collectors;

public class FlashfireBlock extends AbstractAlchemicalFireBlock {

    public FlashfireBlock(BlockBehaviour.Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public int getFireTickDelay(RandomSource source) {
        return 5 + source.nextInt(5);
    }

//    public static void bootStrap() {
//        defaultFireBootStrap((AbstractAlchemicalFireBlock) AlchemineBlocks.FLASH_FIRE.get());
//    }

    public Block getSelf() {
        return AlchemineBlocks.FLASH_FIRE.get();
    }

}
