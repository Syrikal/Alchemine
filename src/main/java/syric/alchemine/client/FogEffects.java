package syric.alchemine.client;

import net.minecraft.client.Camera;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import syric.alchemine.setup.alchemineBlocks;

public class FogEffects {

    public static void renderFog(final EntityViewRenderEvent.RenderFogEvent initialEvent) {
        if (initialEvent.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.VITA_SLIME.get()) && initialEvent.isCancelable()) {
            initialEvent.setCanceled(true);
            EntityViewRenderEvent.RenderFogEvent replacement = new EntityViewRenderEvent.RenderFogEvent(FogType.POWDER_SNOW, initialEvent.getCamera(), (float) initialEvent.getPartialTick(), 0.0F, 2.0F, initialEvent.getFogShape());
        }
    }
    public static void fogColor(final EntityViewRenderEvent.FogColors event) {
        if (event.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.VITA_SLIME.get())) {
            event.setBlue(0.095F);
            event.setGreen(0.484F);
            event.setRed(0.137F);
        }
    }

}
