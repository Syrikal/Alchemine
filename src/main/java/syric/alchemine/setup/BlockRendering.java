package syric.alchemine.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class BlockRendering {

    public static void registerBlockRenderTypes() {

        translucent(AlchemineBlocks.ALCHEMICAL_CAULDRON.get());

        translucent(AlchemineBlocks.BOUNCE_SLIME.get());
        translucent(AlchemineBlocks.CUSHIONING_SLIME.get());
        translucent(AlchemineBlocks.CRASH_PAD.get());
        translucent(AlchemineBlocks.LAUNCH_PAD.get());
        translucent(AlchemineBlocks.VITA_SLIME.get());
        translucent(AlchemineBlocks.BERSERKERS_RESIN.get());
        translucent(AlchemineBlocks.RED_SLIME.get());
    }

    private static void translucent(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
    }

}
