package syric.alchemine.brewing.ingredients;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.Alchemine;
import syric.alchemine.brewing.util.Aspect;
import syric.alchemine.outputs.blocks.VitaSlimeBlock;
import syric.alchemine.setup.AlchemineCreativeTabs;
import syric.alchemine.setup.AlchemineItems;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.LogManager;

public class AlchemicalIngredients {

    private static boolean isInitialized = false;

    public static final DeferredRegister<Ingredient> INGREDIENTS = DeferredRegister.create(new ResourceLocation(Alchemine.MODID, "ingredients"), Alchemine.MODID);

    public static final HashMap<Item, Ingredient> INGREDIENTS_MAP = new HashMap<>();



    public static final RegistryObject<Ingredient> SLIME_BALL = register("slime_ball",
            () -> new Ingredient(Items.SLIME_BALL, 2, 0.5D)
                    .addAspect(Aspect.BOUNCY, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> ROTTEN_FLESH = register("rotten_flesh",
            () -> new Ingredient(Items.ROTTEN_FLESH, 2.2, 0.5D)
                    .addAspect(Aspect.BOUNCY, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> LILY_PAD = register("lily_pad",
            () -> new Ingredient(Items.LILY_PAD, 2.3, 0.3D)
                    .addAspect(Aspect.BOUNCY, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> FERMENTED_SPIDER_EYE = register("fermented_spider_eye",
            () -> new Ingredient(Items.FERMENTED_SPIDER_EYE, 2.8, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.STICKY, 1)
                    .setMetapotion());

    public static final RegistryObject<Ingredient> SOUL_SAND = register("soul_sand",
            () -> new Ingredient(Items.SOUL_SAND, 2.6, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> PUFFER_FISH = register("puffer_fish",
            () -> new Ingredient(Items.PUFFERFISH, 2.7, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 1).addAspect(Aspect.EXPLOSIVE, 1).addAspect(Aspect.ZEPHYROUS, 1)
                    .setVolatile(1).setStability(1));

    public static final RegistryObject<Ingredient> SPIDER_EYE = register("spider_eye",
            () -> new Ingredient(Items.SPIDER_EYE, 2.7, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 1).addAspect(Aspect.SLIPPERY, 1));

    public static final RegistryObject<Ingredient> MAGMA_CREAM = register("magma_cream",
            () -> new Ingredient(Items.MAGMA_CREAM, 2.7, 0.6D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.BOUNCY, 2)
                    .setStability(2));

    public static final RegistryObject<Ingredient> BLAZE_POWDER = register("blaze_powder",
            () -> new Ingredient(Items.BLAZE_POWDER, 3, 0.3D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.EXPLOSIVE, 1)
                    .setVolatile(2).setStability(-1));

    public static final RegistryObject<Ingredient> COAL = register("coal",
            () -> new Ingredient(Items.COAL, 2.7, 0.4D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> CRIMSON_FUNGUS = register("crimson_fungus",
            () -> new Ingredient(Items.CRIMSON_FUNGUS, 2.5, 0.4D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> FLINT = register("flint",
            () -> new Ingredient(Items.FLINT, 2.4, 0.3D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> ICE = register("ice",
            () -> new Ingredient(Items.ICE, 2.2, 0.4D)
                    .addAspect(Aspect.ICE, 2).addAspect(Aspect.SLIPPERY, 1));

    public static final RegistryObject<Ingredient> PACKED_ICE = register("packed_ice",
            () -> new Ingredient(Items.PACKED_ICE, 2.3, 0.4D)
                    .addAspect(Aspect.ICE, 2).addAspect(Aspect.SOLID, 2)
                    .setVolatile(-2));

    public static final RegistryObject<Ingredient> SNOW = register("snow",
            () -> new Ingredient(Items.SNOW, 2, 0.4D)
                    .addAspect(Aspect.ICE, 1).addAspect(Aspect.ZEPHYROUS, 1));

//    public static final RegistryObject<Ingredient> PHOSPHORUS = register("phosphorus",
//            () -> new Ingredient(AlchemineItems.PHOSPHORUS.get(), 2.9, 0.3D)
//                    .addAspect(Aspect.LIGHT, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> QUARTZ = register("quartz",
            () -> new Ingredient(Items.QUARTZ, 2.6, 0.3D)
                    .addAspect(Aspect.LIGHT, 1).addAspect(Aspect.ICE, 1).addAspect(Aspect.SOLID, 1)
                    .setStability(-1));

    public static final RegistryObject<Ingredient> GLOW_BERRIES = register("glow_berries",
            () -> new Ingredient(Items.GLOW_BERRIES, 2.4, 0.3D)
                    .addAspect(Aspect.LIGHT, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> IRON_NUGGET = register("iron_nugget",
            () -> new Ingredient(Items.IRON_NUGGET, 2.5, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> GOLD_NUGGET = register("gold_nugget",
            () -> new Ingredient(Items.GOLD_NUGGET, 2.6, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> RAW_COPPER = register("raw_copper",
            () -> new Ingredient(Items.RAW_COPPER, 2.6, 0.4D)
                    .addAspect(Aspect.METALLIC, 1).addAspect(Aspect.FIRE, 1)
                    .setVolatile(1).setStability(1));

    public static final RegistryObject<Ingredient> INK = register("ink",
            () -> new Ingredient(Items.INK_SAC, 2.3, 0.4D)
                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.STICKY, 2)
                    .setVolatile(-1).setStability(1));

//    public static final RegistryObject<Ingredient> FISH_OIL = register("fish_oil",
//            () -> new Ingredient(AlchemineItems.FISH_OIL.get(), 2.5, 0.4D)
//                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.VITAL, 1));
//
//    public static final RegistryObject<Ingredient> STONE_DUST = register("stone_dust",
//            () -> new Ingredient(AlchemineItems.STONE_DUST.get(), 2.3, 0.4D)
//                    .addAspect(Aspect.SOLID, 2).addAspect(Aspect.FIRE, 1));

    public static final RegistryObject<Ingredient> CACTUS = register("cactus",
            () -> new Ingredient(Items.CACTUS, 2.7, 0.3D)
                    .addAspect(Aspect.SOLID, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> WARPED_FUNGUS = register("warped_fungus",
            () -> new Ingredient(Items.WARPED_FUNGUS, 2.8, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.CAUSTIC, 2)
                    .setStability(-2));

    public static final RegistryObject<Ingredient> HONEY = register("honey",
            () -> new Ingredient(Items.HONEY_BOTTLE, 2.4, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.VITAL, 1)
                    .setReturnedItem(Items.GLASS_BOTTLE));

    public static final RegistryObject<Ingredient> COBWEBS = register("cobwebs",
            () -> new Ingredient(Items.COBWEB, 2.4, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.ZEPHYROUS, 2)
                    .setStability(3));

    public static final RegistryObject<Ingredient> HANGING_ROOTS = register("hanging_roots",
            () -> new Ingredient(Items.HANGING_ROOTS, 2.3, 0.3D)
                    .addAspect(Aspect.STICKY, 1).addAspect(Aspect.ZEPHYROUS, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> GOLDEN_CARROT = register("golden_carrot",
            () -> new Ingredient(Items.GOLDEN_CARROT, 2.6, 0.4D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> GLISTERING_MELON_SLICE = register("glistering_melon_slice",
            () -> new Ingredient(Items.GLISTERING_MELON_SLICE, 2.8, 0.5D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.METALLIC, 1)
                    .setVolatile(1).setStability(2));

    public static final RegistryObject<Ingredient> BONEMEAL = register("bonemeal",
            () -> new Ingredient(Items.BONE_MEAL, 2.6, 0.4D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> MILK = register("milk",
            () -> new Ingredient(Items.MILK_BUCKET, 2.3, 0.4D)
                    .addAspect(Aspect.VITAL, 1).addAspect(Aspect.SLIPPERY, 1)
                    .setVolatile(-1).setReturnedItem(Items.BUCKET));

    public static final RegistryObject<Ingredient> SUGAR = register("sugar",
            () -> new Ingredient(Items.SUGAR, 2.7, 0.3D)
                    .addAspect(Aspect.VITAL, 1).addAspect(Aspect.ZEPHYROUS, 1));

    public static final RegistryObject<Ingredient> BAMBOO = register("bamboo",
            () -> new Ingredient(Items.BAMBOO, 2.6, 0.4D)
                    .addAspect(Aspect.ZEPHYROUS, 2).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> MOSS_CARPET = register("moss_carpet",
            () -> new Ingredient(Items.MOSS_CARPET, 2.3, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> FERN = register("fern",
            () -> new Ingredient(Items.FERN, 2.5, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> KELP = register("kelp",
            () -> new Ingredient(Items.KELP, 2.6, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.SLIPPERY, 1)
                    .setVolatile(-1));



    public static final RegistryObject<Ingredient> BLANK = register("blank",
            () -> new Ingredient(Items.GRASS, 2, 0.5D)
                    .addAspect(Aspect.BOUNCY, 2).addAspect(Aspect.STICKY, 1));





    public static void register(final IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        }
        INGREDIENTS.makeRegistry(RegistryBuilder::new);
        INGREDIENTS.register(modEventBus);
        isInitialized = true;
        LogUtils.getLogger().info("Ingredients initialized");
    }

    private static <T extends Ingredient> RegistryObject<T> register(String name, Supplier<T> ingredient) {
        RegistryObject<T> ret = INGREDIENTS.register(name, ingredient);
        INGREDIENTS_MAP.put(ingredient.get().getItem(), ingredient.get());
        return ret;
    }
}
