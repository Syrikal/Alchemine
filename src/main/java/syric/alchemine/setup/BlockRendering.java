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
        translucent(AlchemineBlocks.LUMA_SLIME.get());
        translucent(AlchemineBlocks.SHELL_SLIME.get());
        translucent(AlchemineBlocks.NON_STICK_SLIME.get());
        translucent(AlchemineBlocks.HUNGRY_SLIME.get());
        translucent(AlchemineBlocks.SHELL_SLIME.get());

        translucent(AlchemineBlocks.WHITE_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.LIGHT_GRAY_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.GRAY_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.BLACK_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.RED_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.ORANGE_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.YELLOW_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.LIME_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.GREEN_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.CYAN_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.BLUE_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.LIGHT_BLUE_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.PINK_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.PURPLE_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.MAGENTA_CHROMA_SLIME.get());
        translucent(AlchemineBlocks.BROWN_CHROMA_SLIME.get());

        cutout(AlchemineBlocks.TAR_STICK.get());
        cutout(AlchemineBlocks.GLUE_STICK.get());
        translucent(AlchemineBlocks.SUPER_GLUE_STICK.get());
        cutout(AlchemineBlocks.WEB_SNARE.get());
        cutout(AlchemineBlocks.FOAM_SNARE.get());

        translucent(AlchemineBlocks.WALL_SLIDE.get());


    }

    private static void translucent(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
    }

    private static void cutout(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
    }

}
