package syric.alchemine.client;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import syric.alchemine.setup.AlchemineBlocks;
import syric.alchemine.setup.AlchemineOverlays;

public class FogEffects {

    public static void renderFog(final EntityViewRenderEvent.RenderFogEvent event) {
        IIngameOverlay vita_slime_overlay = AlchemineOverlays.VITA_ELEMENT;
        IIngameOverlay berserkers_resin_overlay = AlchemineOverlays.BERSERKERS_ELEMENT;

        //Vita-slime fog and overlay
        if (event.getCamera().getBlockAtCamera().getBlock().equals(AlchemineBlocks.VITA_SLIME.get()) && event.isCancelable()) {
//            event.setCanceled(true);
//            event.setNearPlaneDistance(1.5F);
//            event.setFarPlaneDistance(6.0F);
//            event.setCanceled(false);
            OverlayRegistry.enableOverlay(vita_slime_overlay, true);
        } else {
            if (OverlayRegistry.getEntry(vita_slime_overlay).isEnabled()) {
                OverlayRegistry.enableOverlay(vita_slime_overlay, false);
            }
        }

        //Berserker's Resin fog and overlay
        if (event.getCamera().getBlockAtCamera().getBlock().equals(AlchemineBlocks.BERSERKERS_RESIN.get()) && event.isCancelable()) {
            event.setNearPlaneDistance(0.0F);
            event.setFarPlaneDistance(4.0F);
            event.setCanceled(true);
//            event.setCanceled(false);
            OverlayRegistry.enableOverlay(berserkers_resin_overlay, true);
        }
        else {
            if (OverlayRegistry.getEntry(berserkers_resin_overlay).isEnabled()) {
                OverlayRegistry.enableOverlay(berserkers_resin_overlay, false);
            }
        }
    }


    public static void fogColor(final EntityViewRenderEvent.FogColors event) {
        //Vita-slime fog color
        if (event.getCamera().getBlockAtCamera().getBlock().equals(AlchemineBlocks.VITA_SLIME.get())) {
//            event.setBlue(0.095F);
//            event.setGreen(0.484F);
//            event.setRed(0.137F);
        }
        //Berserker's Resin fog color
        else if (event.getCamera().getBlockAtCamera().getBlock().equals(AlchemineBlocks.BERSERKERS_RESIN.get())) {
            event.setBlue(0.243F);
            event.setGreen(0.522F);
            event.setRed(0.882F);
        }
    }

}
