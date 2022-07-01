package syric.alchemine.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import syric.alchemine.setup.AlchemineBlocks;
import syric.alchemine.setup.AlchemineEffects;
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
//            LogUtils.getLogger().info("Resin fog shape: " + event.getFogShape()+ ", near: " + event.getNearPlaneDistance() + ", far: " + event.getFarPlaneDistance());
            event.setCanceled(true);
//            event.setCanceled(false);
            OverlayRegistry.enableOverlay(berserkers_resin_overlay, true);
        }
        else {
            if (OverlayRegistry.getEntry(berserkers_resin_overlay).isEnabled()) {
                OverlayRegistry.enableOverlay(berserkers_resin_overlay, false);
            }
        }


        //Light Blindness
        if (event.getCamera().getEntity() instanceof LivingEntity live && event.isCancelable()) {
            if (live.hasEffect(AlchemineEffects.LIGHT_BLINDNESS.get())) {
                event.setFogShape(FogShape.CYLINDER);
//                event.setNearPlaneDistance(2.0F);
//                event.setFarPlaneDistance(8.0F);
//                LogUtils.getLogger().info("Blindness fog shape: " + event.getFogShape()+ ", near: " + event.getNearPlaneDistance() + ", far: " + event.getFarPlaneDistance());

                MobEffectInstance effect = live.getEffect(AlchemineEffects.LIGHT_BLINDNESS.get());

                assert effect != null;
                float render = event.getRenderer().getRenderDistance();
                float f = Mth.lerp(Math.min(1.0F, (float)effect.getDuration() / 20.0F), render, 5.0F);
                if (event.getFogShape() == FogShape.CYLINDER) {
                    event.setNearPlaneDistance(0.0F);
                    event.setFarPlaneDistance(f * 0.8F);
                } else {
                    event.setNearPlaneDistance(f * 0.25F);
                    event.setFarPlaneDistance(f);
                }

                event.setCanceled(true);

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
        if (event.getCamera().getBlockAtCamera().getBlock().equals(AlchemineBlocks.BERSERKERS_RESIN.get())) {
            event.setBlue(0.243F);
            event.setGreen(0.522F);
            event.setRed(0.882F);
        }

        //Light Blindness
        if (event.getCamera().getEntity() instanceof LivingEntity live) {
            if (live.hasEffect(AlchemineEffects.LIGHT_BLINDNESS.get())) {
                event.setRed(98);
                event.setBlue(99);
                event.setGreen(85);
//                event.setGreen(10);
            }
        }

    }

}


