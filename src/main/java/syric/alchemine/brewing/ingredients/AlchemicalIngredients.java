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
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.LogManager;

public class AlchemicalIngredients {

    private static boolean isInitialized = false;

    public static final DeferredRegister<Ingredient> INGREDIENTS = DeferredRegister.create(new ResourceLocation(Alchemine.MODID, "ingredients"), Alchemine.MODID);

    public static final HashMap<Item, Ingredient> INGREDIENTS_MAP = new HashMap<>();


    //METAINGREDIENTS
    public static final RegistryObject<Ingredient> CHARCOAL = registerIngredient("charcoal",
            () -> new Ingredient(Items.CHARCOAL, 1, 0.2D)
                    .setCrash(4));

    public static final RegistryObject<Ingredient> SALT = registerIngredient("salt",
            () -> new Ingredient(AlchemineItems.SALT.get(), 2, 0.2D)
                    .setStability(2));

    public static final RegistryObject<Ingredient> ASH = registerIngredient("ash",
            () -> new Ingredient(AlchemineItems.ASH.get(), 1, 0.2D)
                    .setVolatile(2));

    public static final RegistryObject<Ingredient> CLAY_BALL = registerIngredient("clay_ball",
            () -> new Ingredient(Items.CLAY_BALL, 1, 0.2D)
                    .setVolatile(-2));

    public static final RegistryObject<Ingredient> POTASH = registerIngredient("potash",
            () -> new Ingredient(AlchemineItems.POTASH.get(), 1.5, 0.2D)
                    .setVolatile(1).setStability(1));

    public static final RegistryObject<Ingredient> CHALK = registerIngredient("chalk",
            () -> new Ingredient(AlchemineItems.CHALK.get(), 1.5, 0.2D)
                    .setVolatile(-1).setStability(1));

    public static final RegistryObject<Ingredient> PURE_SALT = registerIngredient("pure_salt",
            () -> new Ingredient(AlchemineItems.PURE_SALT.get(), 1.5, 0.2D)
                    .setStability(-2));

    public static final RegistryObject<Ingredient> PURIFIED_CHARCOAL = registerIngredient("purified_charcoal",
            () -> new Ingredient(AlchemineItems.PURIFIED_CHARCOAL.get(), 1, 0.3)
                    .setCrash(8));

    public static final RegistryObject<Ingredient> SAL_ALCHIMIAE = registerIngredient("sal_alchimiae",
            () -> new Ingredient(AlchemineItems.SAL_ALCHIMIAE.get(), 2, 0.3)
                    .setStability(5));

    public static final RegistryObject<Ingredient> NIGREDO = registerIngredient("nigredo",
            () -> new Ingredient(AlchemineItems.NIGREDO.get(), 1.5, 0.3)
                    .setVolatile(4));

    public static final RegistryObject<Ingredient> ALBEDO = registerIngredient("albedo",
            () -> new Ingredient(AlchemineItems.ALBEDO.get(), 1.5, 0.3)
                    .setVolatile(-4));

    public static final RegistryObject<Ingredient> RUBEDO = registerIngredient("rubedo",
            () -> new Ingredient(AlchemineItems.RUBEDO.get(), 2, 0.3)
                    .setVolatile(3).setStability(3));

    public static final RegistryObject<Ingredient> CITRINITAS = registerIngredient("citrinitas",
            () -> new Ingredient(AlchemineItems.CITRINITAS.get(), 2, 0.3)
                    .setVolatile(-3).setStability(3));

    public static final RegistryObject<Ingredient> ALKAHEST = registerIngredient("alkahest",
            () -> new Ingredient(AlchemineItems.ALKAHEST.get(), 2, 0.4)
                    .setStability(-4).setLingerMult(0.6));


    //BASIC INGREDIENTS
    public static final RegistryObject<Ingredient> SLIME_BALL = registerIngredient("slime_ball",
            () -> new Ingredient(Items.SLIME_BALL, 2, 0.5D)
                    .addAspect(Aspect.BOUNCY, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> ROTTEN_FLESH = registerIngredient("rotten_flesh",
            () -> new Ingredient(Items.ROTTEN_FLESH, 2.2, 0.5D)
                    .addAspect(Aspect.BOUNCY, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> LILY_PAD = registerIngredient("lily_pad",
            () -> new Ingredient(Items.LILY_PAD, 2.3, 0.3D)
                    .addAspect(Aspect.BOUNCY, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> FERMENTED_SPIDER_EYE = registerIngredient("fermented_spider_eye",
            () -> new Ingredient(Items.FERMENTED_SPIDER_EYE, 2.8, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.STICKY, 1)
                    .setMetapotion());

    public static final RegistryObject<Ingredient> SOUL_SAND = registerIngredient("soul_sand",
            () -> new Ingredient(Items.SOUL_SAND, 2.6, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> PUFFER_FISH = registerIngredient("puffer_fish",
            () -> new Ingredient(Items.PUFFERFISH, 2.7, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 1).addAspect(Aspect.EXPLOSIVE, 1).addAspect(Aspect.ZEPHYROUS, 1)
                    .setVolatile(1).setStability(1));

    public static final RegistryObject<Ingredient> SPIDER_EYE = registerIngredient("spider_eye",
            () -> new Ingredient(Items.SPIDER_EYE, 2.7, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 1).addAspect(Aspect.SLIPPERY, 1));

    public static final RegistryObject<Ingredient> MAGMA_CREAM = registerIngredient("magma_cream",
            () -> new Ingredient(Items.MAGMA_CREAM, 2.7, 0.6D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.BOUNCY, 2)
                    .setStability(2));

    public static final RegistryObject<Ingredient> BLAZE_POWDER = registerIngredient("blaze_powder",
            () -> new Ingredient(Items.BLAZE_POWDER, 3, 0.3D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.EXPLOSIVE, 1)
                    .setVolatile(2).setStability(-1));

    public static final RegistryObject<Ingredient> COAL = registerIngredient("coal",
            () -> new Ingredient(Items.COAL, 2.7, 0.4D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> CRIMSON_FUNGUS = registerIngredient("crimson_fungus",
            () -> new Ingredient(Items.CRIMSON_FUNGUS, 2.5, 0.4D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> FLINT = registerIngredient("flint",
            () -> new Ingredient(Items.FLINT, 2.4, 0.3D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> ICE = registerIngredient("ice",
            () -> new Ingredient(Items.ICE, 2.2, 0.4D)
                    .addAspect(Aspect.ICE, 2).addAspect(Aspect.SLIPPERY, 1));

    public static final RegistryObject<Ingredient> PACKED_ICE = registerIngredient("packed_ice",
            () -> new Ingredient(Items.PACKED_ICE, 2.3, 0.4D)
                    .addAspect(Aspect.ICE, 2).addAspect(Aspect.SOLID, 2)
                    .setVolatile(-2));

    public static final RegistryObject<Ingredient> SNOW = registerIngredient("snow",
            () -> new Ingredient(Items.SNOW, 2, 0.4D)
                    .addAspect(Aspect.ICE, 1).addAspect(Aspect.ZEPHYROUS, 1));

    public static final RegistryObject<Ingredient> PHOSPHORUS = registerIngredient("phosphorus",
            () -> new Ingredient(AlchemineItems.PHOSPHORUS.get(), 2.9, 0.3D)
                    .addAspect(Aspect.LIGHT, 2).addAspect(Aspect.STICKY, 1));

    public static final RegistryObject<Ingredient> QUARTZ = registerIngredient("quartz",
            () -> new Ingredient(Items.QUARTZ, 2.6, 0.3D)
                    .addAspect(Aspect.LIGHT, 1).addAspect(Aspect.ICE, 1).addAspect(Aspect.SOLID, 1)
                    .setStability(-1));

    public static final RegistryObject<Ingredient> GLOW_BERRIES = registerIngredient("glow_berries",
            () -> new Ingredient(Items.GLOW_BERRIES, 2.4, 0.3D)
                    .addAspect(Aspect.LIGHT, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> IRON_NUGGET = registerIngredient("iron_nugget",
            () -> new Ingredient(Items.IRON_NUGGET, 2.5, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> GOLD_NUGGET = registerIngredient("gold_nugget",
            () -> new Ingredient(Items.GOLD_NUGGET, 2.6, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> RAW_COPPER = registerIngredient("raw_copper",
            () -> new Ingredient(Items.RAW_COPPER, 2.6, 0.4D)
                    .addAspect(Aspect.METALLIC, 1).addAspect(Aspect.FIRE, 1)
                    .setVolatile(1).setStability(1));

    public static final RegistryObject<Ingredient> INK = registerIngredient("ink",
            () -> new Ingredient(Items.INK_SAC, 2.3, 0.4D)
                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.STICKY, 2)
                    .setVolatile(-1).setStability(1).setContradictory());

    public static final RegistryObject<Ingredient> FISH_OIL = registerIngredient("fish_oil",
            () -> new Ingredient(AlchemineItems.FISH_OIL.get(), 2.5, 0.4D)
                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> STONE_DUST = registerIngredient("stone_dust",
            () -> new Ingredient(AlchemineItems.STONE_DUST.get(), 2.3, 0.4D)
                    .addAspect(Aspect.SOLID, 2).addAspect(Aspect.FIRE, 1));

    public static final RegistryObject<Ingredient> CACTUS = registerIngredient("cactus",
            () -> new Ingredient(Items.CACTUS, 2.7, 0.3D)
                    .addAspect(Aspect.SOLID, 1).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> WARPED_FUNGUS = registerIngredient("warped_fungus",
            () -> new Ingredient(Items.WARPED_FUNGUS, 2.8, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.CAUSTIC, 2)
                    .setStability(-2));

    public static final RegistryObject<Ingredient> HONEY = registerIngredient("honey",
            () -> new Ingredient(Items.HONEY_BOTTLE, 2.4, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.VITAL, 1)
                    .setReturnedItem(Items.GLASS_BOTTLE));

    public static final RegistryObject<Ingredient> COBWEBS = registerIngredient("cobwebs",
            () -> new Ingredient(Items.COBWEB, 2.4, 0.4D)
                    .addAspect(Aspect.STICKY, 2).addAspect(Aspect.ZEPHYROUS, 2)
                    .setStability(3));

    public static final RegistryObject<Ingredient> HANGING_ROOTS = registerIngredient("hanging_roots",
            () -> new Ingredient(Items.HANGING_ROOTS, 2.3, 0.3D)
                    .addAspect(Aspect.STICKY, 1).addAspect(Aspect.ZEPHYROUS, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> GOLDEN_CARROT = registerIngredient("golden_carrot",
            () -> new Ingredient(Items.GOLDEN_CARROT, 2.6, 0.4D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> GLISTERING_MELON_SLICE = registerIngredient("glistering_melon_slice",
            () -> new Ingredient(Items.GLISTERING_MELON_SLICE, 2.8, 0.5D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.METALLIC, 1)
                    .setVolatile(1).setStability(2));

    public static final RegistryObject<Ingredient> BONEMEAL = registerIngredient("bonemeal",
            () -> new Ingredient(Items.BONE_MEAL, 2.6, 0.4D)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.SOLID, 1));

    public static final RegistryObject<Ingredient> MILK = registerIngredient("milk",
            () -> new Ingredient(Items.MILK_BUCKET, 2.3, 0.4D)
                    .addAspect(Aspect.VITAL, 1).addAspect(Aspect.SLIPPERY, 1)
                    .setVolatile(-1).setReturnedItem(Items.BUCKET));

    public static final RegistryObject<Ingredient> SUGAR = registerIngredient("sugar",
            () -> new Ingredient(Items.SUGAR, 2.7, 0.3D)
                    .addAspect(Aspect.VITAL, 1).addAspect(Aspect.ZEPHYROUS, 1));

    public static final RegistryObject<Ingredient> BAMBOO = registerIngredient("bamboo",
            () -> new Ingredient(Items.BAMBOO, 2.6, 0.4D)
                    .addAspect(Aspect.ZEPHYROUS, 2).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> MOSS_CARPET = registerIngredient("moss_carpet",
            () -> new Ingredient(Items.MOSS_CARPET, 2.3, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> FERN = registerIngredient("fern",
            () -> new Ingredient(Items.FERN, 2.5, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> KELP = registerIngredient("kelp",
            () -> new Ingredient(Items.KELP, 2.6, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 1).addAspect(Aspect.SLIPPERY, 1)
                    .setVolatile(-1));


    //ADVANCED INGREDIENTS
    public static final RegistryObject<Ingredient> GUM_ARABIC = registerIngredient("gum_arabic",
            () -> new Ingredient(AlchemineItems.GUM_ARABIC.get(), 2.55, 0.4D)
                    .addAspect(Aspect.BOUNCY, 2).addAspect(Aspect.STICKY, 2));

    public static final RegistryObject<Ingredient> SPIRIT_OF_SALT = registerIngredient("spirit_of_salt",
            () -> new Ingredient(AlchemineItems.SPIRIT_OF_SALT.get(), 3.45, 0.3)
                    .addAspect(Aspect.CAUSTIC, 3).addAspect(Aspect.SLIPPERY, 2)
                    .setVolatile(2));

    public static final RegistryObject<Ingredient> CINNABAR = registerIngredient("cinnabar",
            () -> new Ingredient(AlchemineItems.CINNABAR.get(), 3.15, 0.3)
                    .addAspect(Aspect.CAUSTIC, 3).addAspect(Aspect.SOLID, 1)
                    .setVolatile(2).setStability(-1));

    public static final RegistryObject<Ingredient> REALGAR = registerIngredient("realgar",
            () -> new Ingredient(AlchemineItems.REALGAR.get(), 3, 0.3)
                    .addAspect(Aspect.FIRE, 3).addAspect(Aspect.CAUSTIC, 1).addAspect(Aspect.SOLID, 1)
                    .setVolatile(3).setStability(1));

    public static final RegistryObject<Ingredient> SOUL_ASH = registerIngredient("soul_ash",
            () -> new Ingredient(AlchemineItems.SOUL_ASH.get(), 3.15, 0.5)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.CAUSTIC, 2)
                    .setStability(1));

    public static final RegistryObject<Ingredient> SULFUR = registerIngredient("sulfur",
            () -> new Ingredient(AlchemineItems.SULFUR.get(), 3.6, 0.3)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.EXPLOSIVE, 2).addAspect(Aspect.LIGHT, 1)
                    .setVolatile(4).setStability(-1));

    public static final RegistryObject<Ingredient> ULTRAMARINE = registerIngredient("ultramarine",
            () -> new Ingredient(AlchemineItems.ULTRAMARINE.get(), 2.7, 0.4D)
                    .addAspect(Aspect.ICE, 2).addAspect(Aspect.LIGHT, 2)
                    .setStability(2));

    public static final RegistryObject<Ingredient> SHROOMLIGHT = registerIngredient("shroomlight",
            () -> new Ingredient(Items.SHROOMLIGHT, 2.55, 0.4D)
                    .addAspect(Aspect.LIGHT, 2).addAspect(Aspect.BOUNCY, 1).addAspect(Aspect.CAUSTIC, 1));

    public static final RegistryObject<Ingredient> LUMINOUS_SPORES = registerIngredient("luminous_spores",
            () -> new Ingredient(AlchemineItems.LUMINOUS_SPORES.get(), 2.25, 0.4D)
                    .addAspect(Aspect.LIGHT, 3).addAspect(Aspect.VITAL, 1));

    public static final RegistryObject<Ingredient> BRASS_DUST = registerIngredient("brass_dust",
            () -> new Ingredient(AlchemineItems.BRASS_DUST.get(), 3, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.FIRE, 2)
                    .setVolatile(2).setStability(1));

    public static final RegistryObject<Ingredient> SILVER_DUST = registerIngredient("silver_dust",
            () -> new Ingredient(AlchemineItems.SILVER_DUST.get(), 2.55, 0.5)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.ICE, 2)
                    .setVolatile(-2));

    public static final RegistryObject<Ingredient> ZINC_DUST = registerIngredient("zinc_dust",
            () -> new Ingredient(AlchemineItems.ZINC_DUST.get(), 2.85, 0.4D)
                    .addAspect(Aspect.METALLIC, 2).addAspect(Aspect.SLIPPERY, 2)
                    .setStability(2));

    public static final RegistryObject<Ingredient> SEA_PICKLE = registerIngredient("sea_pickle",
            () -> new Ingredient(Items.SEA_PICKLE, 2.4, 0.5)
                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.LIGHT, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> AMBER = registerIngredient("amber",
            () -> new Ingredient(AlchemineItems.AMBER.get(), 2.7, 0.5)
                    .addAspect(Aspect.SOLID, 3).addAspect(Aspect.BOUNCY, 1)
                    .setStability(2));

    public static final RegistryObject<Ingredient> SCUTE = registerIngredient("scute",
            () -> new Ingredient(Items.SCUTE, 2.85, 0.4D)
                    .addAspect(Aspect.SOLID, 2).addAspect(Aspect.BOUNCY, 2));

    public static final RegistryObject<Ingredient> AMETHYST_SHARD = registerIngredient("amethyst_shard",
            () -> new Ingredient(Items.AMETHYST_SHARD, 3.3, 0.4D)
                    .addAspect(Aspect.SOLID, 2).addAspect(Aspect.LIGHT, 2)
                    .setVolatile(-2).setStability(1));

    public static final RegistryObject<Ingredient> ANTIMONY = registerIngredient("antimony",
            () -> new Ingredient(AlchemineItems.ANTIMONY.get(), 2.55, 0.4D)
                    .addAspect(Aspect.SOLID, 2).addAspect(Aspect.ZEPHYROUS, 2)
                    .setContradictory());

    public static final RegistryObject<Ingredient> NITRE = registerIngredient("nitre",
            () -> new Ingredient(AlchemineItems.NITRE.get(), 3.15, 0.5)
                    .addAspect(Aspect.VITAL, 3).addAspect(Aspect.EXPLOSIVE, 2)
                    .setVolatile(1).setStability(2));

    public static final RegistryObject<Ingredient> AZALEA_PETALS = registerIngredient("azalea_petals",
            () -> new Ingredient(AlchemineItems.AZALEA_PETALS.get(), 2.55, 0.5)
                    .addAspect(Aspect.VITAL, 2).addAspect(Aspect.ZEPHYROUS, 2)
                    .setVolatile(-3).setStability(1));

    public static final RegistryObject<Ingredient> RABBITS_FOOT = registerIngredient("rabbits_foot",
            () -> new Ingredient(Items.RABBIT_FOOT, 2.85, 0.3)
                    .addAspect(Aspect.ZEPHYROUS, 2).addAspect(Aspect.SLIPPERY, 1));


    //PURE INGREDIENTS
    public static final RegistryObject<Ingredient> SLIME_RESIN = registerIngredient("slime_resin",
            () -> new Ingredient(AlchemineItems.SLIME_RESIN.get(), 2.5, 0.4D)
                    .addAspect(Aspect.BOUNCY, 3));

    public static final RegistryObject<Ingredient> VITRIOL = registerIngredient("vitriol",
            () -> new Ingredient(AlchemineItems.VITRIOL.get(), 3, 0.4D)
                    .addAspect(Aspect.CAUSTIC, 3));

    public static final RegistryObject<Ingredient> GUNPOWDER = registerIngredient("gunpowder",
            () -> new Ingredient(Items.GUNPOWDER, 3, 0.3D)
                    .addAspect(Aspect.EXPLOSIVE, 3).setMetapotion());

    public static final RegistryObject<Ingredient> INFERNO_POWDER = registerIngredient("inferno_powder",
            () -> new Ingredient(AlchemineItems.INFERNO_POWDER.get(), 3, 0.4D)
                    .addAspect(Aspect.FIRE, 3));

    public static final RegistryObject<Ingredient> SPIRIT_OF_WINTER = registerIngredient("spirit_of_winter",
            () -> new Ingredient(AlchemineItems.SPIRIT_OF_WINTER.get(), 2.6, 0.4D)
                    .addAspect(Aspect.ICE, 3));

    public static final RegistryObject<Ingredient> GLOWSTONE_DUST = registerIngredient("glowstone_dust",
            () -> new Ingredient(Items.GLOWSTONE_DUST, 2.8, 0.3D)
                    .addAspect(Aspect.LIGHT, 3).setMetapotion());

    public static final RegistryObject<Ingredient> PURIFIED_GOLD = registerIngredient("purified_gold",
            () -> new Ingredient(AlchemineItems.PURIFIED_GOLD.get(), 2.6, 0.4D)
                    .addAspect(Aspect.METALLIC, 3));

    public static final RegistryObject<Ingredient> REFINED_OIL = registerIngredient("refined_oil",
            () -> new Ingredient(AlchemineItems.REFINED_OIL.get(), 2.4, 0.4D)
                    .addAspect(Aspect.SLIPPERY, 3));

    public static final RegistryObject<Ingredient> OBSIDIAN_DUST = registerIngredient("obsidian_dust",
            () -> new Ingredient(AlchemineItems.OBSIDIAN_DUST.get(), 2.5, 0.4D)
                    .addAspect(Aspect.SOLID, 3));

    public static final RegistryObject<Ingredient> BITUMEN = registerIngredient("bitumen",
            () -> new Ingredient(AlchemineItems.BITUMEN.get(), 2.3, 0.4D)
                    .addAspect(Aspect.STICKY, 3));

    public static final RegistryObject<Ingredient> OAK_HEARTWOOD = registerIngredient("oak_heartwood",
            () -> new Ingredient(AlchemineItems.OAK_HEARTWOOD.get(), 2.2, 0.4D)
                    .addAspect(Aspect.VITAL, 3));

    public static final RegistryObject<Ingredient> PHANTOM_MEMBRANE = registerIngredient("phantom_membrane",
            () -> new Ingredient(Items.PHANTOM_MEMBRANE, 2.6, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 3));


    //POTENT INGREDIENTS
    public static final RegistryObject<Ingredient> SLIME_OIL = registerIngredient("slime_oil",
            () -> new Ingredient(AlchemineItems.SLIME_OIL.get(), 3.3, 0.4)
                    .addAspect(Aspect.BOUNCY, 4).addAspect(Aspect.SLIPPERY, 3)
                    .setVolatile(-1).setStability(2));

    public static final RegistryObject<Ingredient> AQUA_FORTIS = registerIngredient("aqua_fortis",
            () -> new Ingredient(AlchemineItems.AQUA_FORTIS.get(), 4, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 4).addAspect(Aspect.FIRE, 2)
                    .setVolatile(3).setStability(-1));

    public static final RegistryObject<Ingredient> WITHERBONE = registerIngredient("witherbone",
            () -> new Ingredient(AlchemineItems.WITHERBONE.get(), 4, 0.2)
                    .addAspect(Aspect.CAUSTIC, 4).addAspect(Aspect.FIRE, 3)
                    .setVolatile(2).setStability(-1));

    public static final RegistryObject<Ingredient> ENDER_SALT = registerIngredient("ender_salt",
            () -> new Ingredient(AlchemineItems.ENDER_SALT.get(), 3.8, 0.4)
                    .addAspect(Aspect.CAUSTIC, 3).addAspect(Aspect.ZEPHYROUS, 2).addAspect(Aspect.ICE, 1));

    public static final RegistryObject<Ingredient> QUICKSILVER = registerIngredient("quicksilver",
            () -> new Ingredient(AlchemineItems.QUICKSILVER.get(), 3.6, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 3).addAspect(Aspect.METALLIC, 2).addAspect(Aspect.ZEPHYROUS, 2)
                    .setVolatile(1).setStability(-1).setContradictory());

    public static final RegistryObject<Ingredient> WITHER_ROSE = registerIngredient("wither_rose",
            () -> new Ingredient(Items.WITHER_ROSE, 3.8, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 3).addAspect(Aspect.VITAL, 3).addAspect(Aspect.EXPLOSIVE, 2));

    public static final RegistryObject<Ingredient> GHAST_TEAR = registerIngredient("ghast_tear",
            () -> new Ingredient(Items.GHAST_TEAR, 3.9, 0.3D)
                    .addAspect(Aspect.EXPLOSIVE, 4).addAspect(Aspect.FIRE, 3).addAspect(Aspect.ZEPHYROUS, 1));

    public static final RegistryObject<Ingredient> FULMINATING_GOLD = registerIngredient("fulminating_gold",
            () -> new Ingredient(AlchemineItems.FULMINATING_GOLD.get(), 3.9, 0.4)
                    .addAspect(Aspect.EXPLOSIVE, 3).addAspect(Aspect.METALLIC, 3)
                    .setVolatile(1));

    public static final RegistryObject<Ingredient> FULMINATING_SILVER = registerIngredient("fulminating_silver",
            () -> new Ingredient(AlchemineItems.FULMINATING_SILVER.get(), 3.8, 0.4)
                    .addAspect(Aspect.EXPLOSIVE, 3).addAspect(Aspect.METALLIC, 2).addAspect(Aspect.ICE, 2)
                    .setVolatile(-1));

    public static final RegistryObject<Ingredient> QUICKLIME = registerIngredient("quicklime",
            () -> new Ingredient(AlchemineItems.QUICKLIME.get(), 3.7, 0.4)
                    .addAspect(Aspect.EXPLOSIVE, 3).addAspect(Aspect.SOLID, 3).addAspect(Aspect.FIRE, 2)
                    .setVolatile(1).setStability(2).setContradictory());

    public static final RegistryObject<Ingredient> DRAGON_BREATH = registerIngredient("dragon_breath",
            () -> new Ingredient(Items.DRAGON_BREATH, 4, 0.3D)
                    .addAspect(Aspect.EXPLOSIVE, 3).addAspect(Aspect.ZEPHYROUS, 3).addAspect(Aspect.VITAL, 2)
                    .setStability(1).setMetapotion());

    public static final RegistryObject<Ingredient> PHLOGISTON = registerIngredient("phlogiston",
            () -> new Ingredient(AlchemineItems.PHLOGISTON.get(), 3.7, 0.3D)
                    .addAspect(Aspect.FIRE, 4).addAspect(Aspect.STICKY, 2)
                    .setVolatile(3));

    public static final RegistryObject<Ingredient> SALAMANDER_OIL = registerIngredient("salamander_oil",
            () -> new Ingredient(AlchemineItems.SALAMANDER_OIL.get(), 3.6, 0.4)
                    .addAspect(Aspect.FIRE, 3).addAspect(Aspect.SLIPPERY, 3)
                    .setVolatile(-2).setStability(-1));

    public static final RegistryObject<Ingredient> BLUE_ICE = registerIngredient("blue_ice",
            () -> new Ingredient(Items.BLUE_ICE, 3.6, 0.3D)
                    .addAspect(Aspect.ICE, 3).addAspect(Aspect.SLIPPERY, 3).addAspect(Aspect.SOLID, 2)
                    .setVolatile(-4).setStability(-1));

    public static final RegistryObject<Ingredient> FROSTFLAME_DUST = registerIngredient("frostflame_dust",
            () -> new Ingredient(AlchemineItems.FROSTFLAME_DUST.get(), 3.6, 0.3D)
                    .addAspect(Aspect.ICE, 3).addAspect(Aspect.FIRE, 3)
                    .setContradictory());

    public static final RegistryObject<Ingredient> FROGLIGHT_GEL = registerIngredient("froglight_gel",
            () -> new Ingredient(AlchemineItems.FROGLIGHT_GEL.get(), 3.3, 0.3D)
                    .addAspect(Aspect.LIGHT, 4).addAspect(Aspect.BOUNCY, 3)
                    .setStability(-1));

    public static final RegistryObject<Ingredient> NETHERITE_SCRAP = registerIngredient("netherite_scrap",
            () -> new Ingredient(Items.NETHERITE_SCRAP, 3.5, 0.3D)
                    .addAspect(Aspect.METALLIC, 4).addAspect(Aspect.SOLID, 4)
                    .setStability(4));

    public static final RegistryObject<Ingredient> REDSTONE_DUST = registerIngredient("redstone_dust",
            () -> new Ingredient(Items.REDSTONE, 3.4, 0.3D)
                    .addAspect(Aspect.METALLIC, 3).addAspect(Aspect.LIGHT, 3).setMetapotion());

    public static final RegistryObject<Ingredient> DIAMOND = registerIngredient("diamond",
            () -> new Ingredient(Items.DIAMOND, 3.4, 0.5)
                    .addAspect(Aspect.SOLID, 4).addAspect(Aspect.LIGHT, 3)
                    .setStability(2));

    public static final RegistryObject<Ingredient> LEAD = registerIngredient("lead",
            () -> new Ingredient(AlchemineItems.LEAD.get(), 3.5, 0.5)
                    .addAspect(Aspect.SOLID, 3).addAspect(Aspect.METALLIC, 2).addAspect(Aspect.CAUSTIC, 2)
                    .setVolatile(-2).setStability(2).setContradictory());

    public static final RegistryObject<Ingredient> TAR = registerIngredient("tar",
            () -> new Ingredient(AlchemineItems.TAR.get(), 3.3, 0.4)
                    .addAspect(Aspect.STICKY, 3).addAspect(Aspect.FIRE, 2).addAspect(Aspect.SOLID, 2));

    public static final RegistryObject<Ingredient> GOLDEN_APPLE = registerIngredient("golden_apple",
            () -> new Ingredient(Items.GOLDEN_APPLE, 3.7, 0.5)
                    .addAspect(Aspect.VITAL, 4).addAspect(Aspect.METALLIC, 3)
                    .setVolatile(-2).setStability(2));

    public static final RegistryObject<Ingredient> SCULK = registerIngredient("sculk",
            () -> new Ingredient(Items.SCULK, 3.5, 0.4)
                    .addAspect(Aspect.VITAL, 3).addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> END_BLOSSOM = registerIngredient("end_blossom",
            () -> new Ingredient(AlchemineItems.END_BLOSSOM.get(), 3.6, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 4).addAspect(Aspect.LIGHT, 4)
                    .setVolatile(-3).setStability(2));

    public static final RegistryObject<Ingredient> POWDER_SNOW = registerIngredient("powder_snow",
            () -> new Ingredient(Items.POWDER_SNOW_BUCKET, 3.7, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 3).addAspect(Aspect.ICE, 3)
                    .setVolatile(-4).setStability(-1).setReturnedItem(Items.BUCKET));


    //GENTLE INGREDIENTS
    public static final RegistryObject<Ingredient> WARPED_ROOTS = registerIngredient("warped_roots",
            () -> new Ingredient(Items.WARPED_ROOTS, 1.7, 0.3D)
                    .addAspect(Aspect.CAUSTIC, 2).addAspect(Aspect.ZEPHYROUS, 1)
                    .setStability(-1));

    public static final RegistryObject<Ingredient> CRIMSON_ROOTS = registerIngredient("crimson_roots",
            () -> new Ingredient(Items.CRIMSON_ROOTS, 1.7, 0.3D)
                    .addAspect(Aspect.FIRE, 2).addAspect(Aspect.LIGHT, 1)
                    .setStability(-1));

    public static final RegistryObject<Ingredient> NETHER_SPROUTS = registerIngredient("nether_sprouts",
            () -> new Ingredient(Items.NETHER_SPROUTS, 1.9, 0.3D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.BOUNCY, 1));

    public static final RegistryObject<Ingredient> NETHER_WART = registerIngredient("nether_wart",
            () -> new Ingredient(Items.NETHER_WART, 1.8, 0.3D)
                    .addAspect(Aspect.FIRE, 1).addAspect(Aspect.STICKY, 1)
                    .setStability(1).setMetapotion());

    public static final RegistryObject<Ingredient> SWEET_BERRIES = registerIngredient("sweet_berries",
            () -> new Ingredient(Items.SWEET_BERRIES, 1.8, 0.3D)
                    .addAspect(Aspect.ICE, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(1));

    public static final RegistryObject<Ingredient> GLOW_LICHEN = registerIngredient("glow_lichen",
            () -> new Ingredient(Items.GLOW_LICHEN, 1.7, 0.3D)
                    .addAspect(Aspect.LIGHT, 1).addAspect(Aspect.VITAL, 1)
                    .setStability(2));

    public static final RegistryObject<Ingredient> GLOW_INK = registerIngredient("glow_ink",
            () -> new Ingredient(Items.GLOW_INK_SAC, 2, 0.3D)
                    .addAspect(Aspect.SLIPPERY, 2).addAspect(Aspect.LIGHT, 1));

    public static final RegistryObject<Ingredient> BROWN_MUSHROOM = registerIngredient("brown_mushroom",
            () -> new Ingredient(Items.BROWN_MUSHROOM, 1.8, 0.3D)
                    .addAspect(Aspect.SOLID, 1).addAspect(Aspect.METALLIC, 1)
                    .setVolatile(-1));

    public static final RegistryObject<Ingredient> RED_MUSHROOM = registerIngredient("red_mushroom",
            () -> new Ingredient(Items.RED_MUSHROOM, 1.8, 0.3D)
                    .addAspect(Aspect.VITAL, 1).addAspect(Aspect.CAUSTIC, 1)
                    .setVolatile(1));

    public static final RegistryObject<Ingredient> CHORUS_FRUIT = registerIngredient("chorus_fruit",
            () -> new Ingredient(Items.CHORUS_FRUIT, 2, 0.3D)
                    .addAspect(Aspect.ZEPHYROUS, 2).addAspect(Aspect.BOUNCY, 1)
                    .setStability(1));




//    public static final RegistryObject<Ingredient> BLANK = registerIngredient("blank",
//            () -> new Ingredient(Items.GRASS, 2, 0.5D)
//                    .addAspect(Aspect.BOUNCY, 2).addAspect(Aspect.STICKY, 1));



    //METHODS
    public static void register(final IEventBus modEventBus) {
        INGREDIENTS.makeRegistry(RegistryBuilder::new);
        INGREDIENTS.register(modEventBus);
        LogUtils.getLogger().info("Ingredients register created");
    }

    private static <T extends Ingredient> RegistryObject<T> registerIngredient(String name, Supplier<T> ingredient) {
        RegistryObject<T> ret = INGREDIENTS.register(name, ingredient);
        return ret;
    }

    public static void fillMap() {
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        }
        for (RegistryObject<Ingredient> entry : INGREDIENTS.getEntries() ) {
            INGREDIENTS_MAP.put(entry.get().getItem(), entry.get());
//            LogUtils.getLogger().info("Adding " + entry.get().getItem().toString() + " to ingredients map");
        }
        isInitialized = true;
    }

}
