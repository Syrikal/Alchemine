package syric.alchemine.setup;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class BlockRendering {

    public static void registerOverlayTypes() {

        translucent(alchemineBlocks.BOUNCE_SLIME.get());
        translucent(alchemineBlocks.CUSHIONING_SLIME.get());
        translucent(alchemineBlocks.CRASH_PAD.get());
        translucent(alchemineBlocks.LAUNCH_PAD.get());
        translucent(alchemineBlocks.VITA_SLIME.get());
        translucent(alchemineBlocks.BERSERKERS_RESIN.get());
        translucent(alchemineBlocks.RED_SLIME.get());
        translucent(alchemineBlocks.ALCHEMICAL_CAULDRON.get());


//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.BOUNCE_SLIME.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.CRASH_PAD.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.CUSHIONING_SLIME.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.LAUNCH_PAD.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.ALCHEMICAL_CAULDRON.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.VITA_SLIME.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(alchemineBlocks.RED_SLIME.get(), RenderType.translucent());
    }

    private static void translucent(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
    }

}
