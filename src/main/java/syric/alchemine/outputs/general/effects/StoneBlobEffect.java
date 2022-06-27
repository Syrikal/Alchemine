package syric.alchemine.outputs.general.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import syric.alchemine.setup.AlchemineBlocks;

public class StoneBlobEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        effectsUtil.placeSwellInstantaneous(level, pos, 60, Blocks.STONE, effectsUtil.DEFAULT);
    }

}
