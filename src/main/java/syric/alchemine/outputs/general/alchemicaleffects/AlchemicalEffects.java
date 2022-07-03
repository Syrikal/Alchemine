package syric.alchemine.outputs.general.alchemicaleffects;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.openal.ALC;
import syric.alchemine.Alchemine;
import syric.alchemine.brewing.ingredients.Ingredient;
import syric.alchemine.brewing.util.Aspect;
import syric.alchemine.brewing.util.Recipe;
import syric.alchemine.outputs.bouncy.effects.CrashPadEffect;
import syric.alchemine.outputs.bouncy.effects.InstantShelterEffect;
import syric.alchemine.outputs.slippery.blocks.OilSlickBlock;
import syric.alchemine.outputs.slippery.blocks.WallSlideBlock;
import syric.alchemine.outputs.slippery.effects.OilSlickEffect;
import syric.alchemine.outputs.slippery.effects.WallSlideEffect;
import syric.alchemine.outputs.sticky.effects.FoamsnareEffect;
import syric.alchemine.outputs.sticky.effects.GlueStickEffect;
import syric.alchemine.outputs.sticky.effects.TarStickEffect;
import syric.alchemine.outputs.sticky.effects.WebsnareEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AlchemicalEffects {


    private static boolean isInitialized = false;

    public static final DeferredRegister<AlchemicalEffect> ALCHEMICAL_EFFECTS = DeferredRegister.create(new ResourceLocation(Alchemine.MODID, "effects"), Alchemine.MODID);

    public static final List<AlchemicalEffect> EFFECT_LIST = new ArrayList<>();
//    public static final HashMap<Aspect, List<Recipe>> RECIPES_BY_BASE = new HashMap<>();


    //Bouncy
    public static final RegistryObject<AlchemicalEffect> CRASH_PAD = register("crash_pad",
            CrashPadEffect::new);
    public static final RegistryObject<AlchemicalEffect> INSTANT_SHELTER = register("instant_shelter",
            InstantShelterEffect::new);

    //Sticky
    public static final RegistryObject<AlchemicalEffect> FOAMSNARE = register("foamsnare",
            FoamsnareEffect::new);
    public static final RegistryObject<AlchemicalEffect> WEBSNARE = register("websnare",
            WebsnareEffect::new);
    public static final RegistryObject<AlchemicalEffect> TAR_STICK = register("tar_stick",
            TarStickEffect::new);
    public static final RegistryObject<AlchemicalEffect> GLUE_STICK = register("glue_stick",
            GlueStickEffect::new);

    //Slippery
    public static final RegistryObject<AlchemicalEffect> OIL_SLICK = register("oil_slick",
            OilSlickEffect::new);
    public static final RegistryObject<AlchemicalEffect> WALL_SLIDE = register("wall_slide",
            WallSlideEffect::new);


    //Misc
    public static final RegistryObject<AlchemicalEffect> STONE_BLOB = register("stone_blob",
            StoneBlobEffect::new);



    public static void register(final IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Effects already initialized");
        }
        ALCHEMICAL_EFFECTS.makeRegistry(RegistryBuilder::new);
        ALCHEMICAL_EFFECTS.register(modEventBus);
        isInitialized = true;
        LogUtils.getLogger().info("Effects initialized");
    }

    private static <T extends AlchemicalEffect> RegistryObject<T> register(String name, Supplier<T> effect) {
        RegistryObject<T> ret = ALCHEMICAL_EFFECTS.register(name, effect);
        EFFECT_LIST.add(effect.get());
        return ret;
    }


}
