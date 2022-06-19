package syric.alchemine.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class BlockRendering {

    public static void registerBlockRenderTypes() {

        translucent(alchemineBlocks.BOUNCE_SLIME.get());
        translucent(alchemineBlocks.CUSHIONING_SLIME.get());
        translucent(alchemineBlocks.CRASH_PAD.get());
        translucent(alchemineBlocks.LAUNCH_PAD.get());
        translucent(alchemineBlocks.VITA_SLIME.get());
        translucent(alchemineBlocks.BERSERKERS_RESIN.get());
        translucent(alchemineBlocks.RED_SLIME.get());
        translucent(alchemineBlocks.ALCHEMICAL_CAULDRON.get());
    }

    private static void translucent(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
    }

}
