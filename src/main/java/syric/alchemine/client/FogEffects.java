package syric.alchemine.client;

import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import syric.alchemine.setup.alchemineBlocks;
import syric.alchemine.setup.alchemineOverlays;

public class FogEffects {

    public static void renderFog(final EntityViewRenderEvent.RenderFogEvent initialEvent) {
        IIngameOverlay vita_slime_overlay = alchemineOverlays.VITA_ELEMENT;
        IIngameOverlay berserkers_resin_overlay = alchemineOverlays.BERSERKERS_ELEMENT;

        //Vita-slime fog and overlay
        if (initialEvent.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.VITA_SLIME.get()) && initialEvent.isCancelable()) {
            initialEvent.setCanceled(true);
            EntityViewRenderEvent.RenderFogEvent replacement = new EntityViewRenderEvent.RenderFogEvent(FogType.POWDER_SNOW, initialEvent.getCamera(), (float) initialEvent.getPartialTick(), 0.5F, 4.0F, initialEvent.getFogShape());
            OverlayRegistry.enableOverlay(vita_slime_overlay, true);
//            System.out.println("Rendered fog in VitaSlime. Enabled Overlay: " + OverlayRegistry.getEntry(vita_slime_overlay).isEnabled());
        } else {
            OverlayRegistry.enableOverlay(vita_slime_overlay, false);
        }

        //Berserker's Resin fog and overlay
        if (initialEvent.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.BERSERKERS_RESIN.get()) && initialEvent.isCancelable()) {
            initialEvent.setCanceled(true);
            EntityViewRenderEvent.RenderFogEvent replacement = new EntityViewRenderEvent.RenderFogEvent(FogType.POWDER_SNOW, initialEvent.getCamera(), (float) initialEvent.getPartialTick(), 0.0F, 2.0F, initialEvent.getFogShape());
            OverlayRegistry.enableOverlay(berserkers_resin_overlay, true);
        }
        else {
            OverlayRegistry.enableOverlay(berserkers_resin_overlay, false);
//            System.out.println("Rendered fog not in VitaSlime. Enabled Overlay: " + OverlayRegistry.getEntry(vita_slime_overlay).isEnabled());
        }
    }
    public static void fogColor(final EntityViewRenderEvent.FogColors event) {
        //Vita-slime fog color
        if (event.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.VITA_SLIME.get())) {
            event.setBlue(0.095F);
            event.setGreen(0.484F);
            event.setRed(0.137F);
        }
        //Berserker's Resin fog color
        else if (event.getCamera().getBlockAtCamera().getBlock().equals(alchemineBlocks.BERSERKERS_RESIN.get())) {
            event.setBlue(0.243F);
            event.setGreen(0.522F);
            event.setRed(0.882F);
        }
    }

}
