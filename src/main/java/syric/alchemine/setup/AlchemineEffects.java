package syric.alchemine.setup;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.Alchemine;
import syric.alchemine.effect.LightBlindnessEffect;
import syric.alchemine.util.ColorsUtil;

public class AlchemineEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Alchemine.MODID);

    public static final RegistryObject<MobEffect> LIGHT_BLINDNESS = EFFECTS.register("light_blindness", () -> new LightBlindnessEffect(MobEffectCategory.HARMFUL, ColorsUtil.rawColorFromRGB(98, 99, 85)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}
