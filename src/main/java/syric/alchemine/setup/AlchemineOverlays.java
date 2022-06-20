package syric.alchemine.setup;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import syric.alchemine.client.BerserkersResinOverlay;
import syric.alchemine.client.VitaSlimeOverlay;

public class AlchemineOverlays {

    public static final IIngameOverlay VITA_ELEMENT = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FROSTBITE_ELEMENT, "vita_slime_overlay", new VitaSlimeOverlay());
    public static final IIngameOverlay BERSERKERS_ELEMENT = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FROSTBITE_ELEMENT, "berserkers_resin_overlay", new BerserkersResinOverlay());


    public static void register() {}

}
