package syric.alchemine.setup;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.Alchemine;
import syric.alchemine.brewing.laboratory.AlchemicalAlembicBlock;
import syric.alchemine.brewing.laboratory.AlchemicalCrucibleBlock;
import syric.alchemine.brewing.laboratory.AlchemicalGrinderBlock;
import syric.alchemine.brewing.cauldron.AlchemicalCauldronBlock;
import syric.alchemine.outputs.bouncy.blocks.*;
import syric.alchemine.outputs.fire.blocks.*;
import syric.alchemine.outputs.general.sludges.*;
import syric.alchemine.outputs.slippery.blocks.GroovedIceBlock;
import syric.alchemine.outputs.slippery.blocks.OilSlickBlock;
import syric.alchemine.outputs.slippery.blocks.WallSlideBlock;
import syric.alchemine.outputs.sticky.blocks.*;


import java.util.function.Supplier;

public class AlchemineBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Alchemine.MODID);

    //Fancy
    public static final RegistryObject<Block> ALCHEMICAL_CAULDRON = register("alchemical_cauldron",
            () -> new AlchemicalCauldronBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.STONE)
                    .requiresCorrectToolForDrops().strength(2.0F)),
            AlchemineCreativeTabs.ALCHEMY);

    public static final RegistryObject<Block> ALCHEMICAL_GRINDER = register("alchemical_grinder",
            () -> new AlchemicalGrinderBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .requiresCorrectToolForDrops().strength(3.5F)),
            AlchemineCreativeTabs.ALCHEMY);

    public static final RegistryObject<Block> ALCHEMICAL_ALEMBIC = register("alchemical_alembic",
            () -> new AlchemicalAlembicBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops().strength(3.0F)),
            AlchemineCreativeTabs.ALCHEMY);

    public static final RegistryObject<Block> ALCHEMICAL_CRUCIBLE = register("alchemical_crucible",
            () -> new AlchemicalCrucibleBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .requiresCorrectToolForDrops().strength(3.0F)),
            AlchemineCreativeTabs.ALCHEMY);





    //Bouncy
    public static final RegistryObject<Block> CUSHIONING_SLIME = register("cushioning_slime",
            () -> new CushioningSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion(), true),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> CRASH_PAD = register("crash_pad",
            () -> new CushioningSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion(), false),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> BOUNCE_SLIME = register("bounce_slime",
            () -> new BounceSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion(), true),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> LAUNCH_PAD = register("launch_pad",
            () -> new BounceSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion(), false),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> VITA_SLIME = register("vita_slime",
            () -> new VitaSlimeBlock(BlockBehaviour.Properties.of(Material.POWDER_SNOW, MaterialColor.GRASS)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape().strength(0.5F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> BERSERKERS_RESIN = register("berserkers_resin",
            () -> new BerserkersResinBlock(BlockBehaviour.Properties.of(Material.POWDER_SNOW, MaterialColor.COLOR_ORANGE)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape().strength(1.0F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> RED_SLIME = register("red_slime",
            () -> new RedSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.TERRACOTTA_RED)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> SHELL_SLIME = register("shell_slime",
            () -> new ShellSlimeBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.SHELL_SLIME_MAT)
                    .friction(0.8F).sound(SoundType.STONE).noOcclusion().strength(2.0F).isViewBlocking(AlchemineBlocks::never).isSuffocating(AlchemineBlocks::never)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> LUMA_SLIME = register("luma_slime",
            () -> new LumaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.SAND)
                    .friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> NON_STICK_SLIME = register("non_stick_slime",
            () -> new NonStickSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> HUNGRY_SLIME = register("hungry_slime",
            () -> new HungrySlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
                    .friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().randomTicks().strength(2.0F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> WHITE_CHROMA_SLIME = register("white_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.SNOW).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> LIGHT_GRAY_CHROMA_SLIME = register("light_gray_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GRAY).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> GRAY_CHROMA_SLIME = register("gray_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_GRAY).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> BLACK_CHROMA_SLIME = register("black_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_BLACK).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> RED_CHROMA_SLIME = register("red_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> ORANGE_CHROMA_SLIME = register("orange_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> YELLOW_CHROMA_SLIME = register("yellow_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_YELLOW).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> LIME_CHROMA_SLIME = register("lime_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> GREEN_CHROMA_SLIME = register("green_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_GREEN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> CYAN_CHROMA_SLIME = register("cyan_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_CYAN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> BLUE_CHROMA_SLIME = register("blue_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_BLUE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> LIGHT_BLUE_CHROMA_SLIME = register("light_blue_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_LIGHT_BLUE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> PINK_CHROMA_SLIME = register("pink_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_PINK).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> PURPLE_CHROMA_SLIME = register("purple_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_PURPLE).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> MAGENTA_CHROMA_SLIME = register("magenta_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_MAGENTA).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> BROWN_CHROMA_SLIME = register("brown_chroma_slime", () -> new ChromaSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_BROWN).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()), AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);



    //Sticky
    public static final RegistryObject<Block> TAR_STICK = register("tar_stick",
            () -> new StickyFlatBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.TAR_DEC_MAT, MaterialColor.COLOR_BLACK)
                    .sound(SoundType.MUD).noOcclusion().strength(0.5F)
                    .jumpFactor(0.1F), 1),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> GLUE_STICK = register("glue_stick",
            () -> new StickyFlatBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.GLUE_MAT, MaterialColor.COLOR_LIGHT_GRAY)
                    .sound(SoundType.SLIME_BLOCK).noOcclusion().strength(2.0F)
                    .jumpFactor(0.01F), 2),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> SUPER_GLUE_STICK = register("super_glue_stick",
            () -> new GlueTrapBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.GLUE_MAT, MaterialColor.NONE)
                    .sound(SoundType.SLIME_BLOCK).noOcclusion().strength(6.0F)
                    .jumpFactor(0.01F), 3),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> FOAM_SNARE = register("foam_snare",
            () -> new StickyFoamBlock(BlockBehaviour.Properties.of(Material.WEB)
                    .noCollission().sound(SoundType.WOOL).strength(4.0F), 1, 1200),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> WEB_SNARE = register("web_snare",
            () -> new StickyFoamBlock(BlockBehaviour.Properties.of(Material.WEB)
                    .noCollission().sound(SoundType.WOOL).strength(6.0F), 7, 1200),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> TAR_BLOCK = register("tar_block",
            () -> new TarBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.TAR_MAT, MaterialColor.COLOR_BLACK)
                    .friction(0.8F).sound(SoundType.MUD).strength(2.0F)
                    .speedFactor(0.3F).jumpFactor(0.1F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);



    //Slippery
    public static final RegistryObject<Block> OIL_SLICK = register("oil_slick",
            () -> new OilSlickBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.TAR_DEC_MAT)
                    .speedFactor(1.6F).sound(SoundType.MUD).noOcclusion().strength(2.0F), 1200),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> PERMANENT_OIL_SLICK = register("permanent_oil_slick",
            () -> new OilSlickBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.TAR_DEC_MAT)
                    .speedFactor(1.6F).sound(SoundType.MUD).noOcclusion().strength(2.0F), 0),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> WALL_SLIDE = register("wall_slide",
            () -> new WallSlideBlock(BlockBehaviour.Properties.of(Material.TOP_SNOW, MaterialColor.ICE)
                    .sound(SoundType.MUD).noCollission().noOcclusion()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> SILVER_ICE = register("silver_ice",
            () -> new HalfTransparentBlock(BlockBehaviour.Properties.of(Material.ICE_SOLID)
                    .friction(1.01F).sound(SoundType.GLASS)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> GROOVED_ICE = register("grooved_ice",
            () -> new GroovedIceBlock(BlockBehaviour.Properties.of(Material.ICE_SOLID)
                    .sound(SoundType.GLASS)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);



    //Fire
    public static final RegistryObject<Block> FLASH_FIRE = register("flash_fire",
            () -> new FlashfireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL), 1.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> STONE_FIRE = register("stone_fire",
            () -> new StonefireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 8).sound(SoundType.WOOL), 0.5F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> LIFEBANE_FIRE = register("lifebane_fire",
            () -> new LifebaneFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 12).sound(SoundType.WOOL), 3.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> SUN_FIRE = register("sun_fire",
            () -> new SunfireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL), 1.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> TIDY_FIRE = register("tidy_fire",
            () -> new TidyFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL), 0.5F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> COZY_FIRE = register("cozy_fire",
            () -> new CozyFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL), 0.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> FELL_FIRE = register("fell_fire",
            () -> new FellfireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().sound(SoundType.WOOL), 4.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> WARDING_FIRE = register("warding_fire",
            () -> new WardingFireBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().sound(SoundType.WOOL), 3.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> ABYSSAL_FORGEFLAME = register("abyssal_forgeflame",
            () -> new AbyssalForgeflameBlock(BlockBehaviour.Properties.of(Material.FIRE)
                    .noCollission().instabreak().sound(SoundType.WOOL), 6.0F),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    //What are post-processing and emissive rendering? Should I use them?
    public static final RegistryObject<Block> SPARKS = register("sparks_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.FIRE)
                    .sound(SoundType.WOOL).noLootTable().instabreak().noCollission().lightLevel((state) -> 15)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> EMBERSTONE = register("emberstone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .lightLevel((state) -> 10)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> COBBLED_EMBERSTONE = register("cobbled_emberstone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .lightLevel((state) -> 10)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> DARK_EMBERSTONE = register("dark_emberstone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .lightLevel((state) -> 10)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> SMOKE_CLOUD = register("smoke_cloud",
            () -> new SmokeCloudBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.SMOKE_MAT)
                    .sound(SoundType.WOOL)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> ASH_CLOUD = register("ash_cloud",
            () -> new AshCloudBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.SMOKE_MAT)
                    .sound(SoundType.WOOL)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> INCENDIARY_CLOUD = register("incendiary_cloud",
            () -> new IncendiaryCloudBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.SMOKE_MAT)
                    .sound(SoundType.WOOL).lightLevel((state) -> 15)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);


    //Sludge
    public static final RegistryObject<Block> FLEXIBLE_SLUDGE = register("flexible_sludge",
            () -> new FlexibleSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.GRASS)
                    .sound(SoundType.SLIME_BLOCK).noLootTable().strength(5.0F)
                    .friction(0.8F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> ADHESIVE_SLUDGE = register("adhesive_sludge",
            () -> new AdhesiveSludgeBlock(BlockBehaviour.Properties.of(AlchemineBlockMaterials.UNPUSHABLE, MaterialColor.COLOR_ORANGE)
                    .sound(SoundType.HONEY_BLOCK).noLootTable().strength(3.0F)
                    .speedFactor(0.07F).jumpFactor(0.03F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> GREASY_SLUDGE = register("greasy_sludge",
            () -> new GreasySludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
                    .sound(SoundType.MUD).noLootTable().strength(5.0F)
                    .friction(1F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> INFERNAL_SLUDGE = register("infernal_sludge",
            () -> new InfernalSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.NETHER)
                    .sound(SoundType.STONE).noLootTable().strength(5.0F)
                    .randomTicks().lightLevel((c) -> {return 15;})),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> STYGIAN_SLUDGE = register("stygian_sludge",
            () -> new StygianSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.ICE)
                    .sound(SoundType.GLASS).noLootTable().strength(20.0F)
                    .friction(0.98F).randomTicks()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> LUMINOUS_SLUDGE = register("luminous_sludge",
            () -> new LuminousSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SAND)
                    .sound(SoundType.GLASS).noLootTable().strength(3.0F).lightLevel((c) -> {return 10;})),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> CAUSTIC_SLUDGE = register("caustic_sludge",
            () -> new CausticSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GREEN)
                    .sound(SoundType.MUD).noLootTable().strength(5.0F)
                    .randomTicks()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> METALLIC_SLUDGE = register("metallic_sludge",
            () -> new MetallicSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.METAL)
                    .sound(SoundType.METAL).strength(30.0F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> VIGOROUS_SLUDGE = register("vigorous_sludge",
            () -> new VigorousSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN)
                    .sound(SoundType.SLIME_BLOCK).noLootTable().strength(3F, 1F)
                    .randomTicks()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> HARDENED_SLUDGE = register("hardened_sludge",
            () -> new Block(BlockBehaviour.Properties.of(AlchemineBlockMaterials.UNPUSHABLE, MaterialColor.DEEPSLATE)
                    .sound(SoundType.STONE).noLootTable().strength(60.0F, 200F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> FOAMY_SLUDGE = register("foamy_sludge",
            () -> new FoamySludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE)
                    .sound(SoundType.WOOL).noLootTable().strength(3F)
                    .noCollission()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> VOLATILE_SLUDGE = register("volatile_sludge",
            () -> new VolatileSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .sound(SoundType.STONE).noLootTable().strength(3F, 200F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);
    public static final RegistryObject<Block> CHAOTIC_SLUDGE = register("chaotic_sludge",
            () -> new ChaoticSludgeBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                    .sound(SoundType.MUD).noLootTable().strength(5F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);



    //Morb
    public static final RegistryObject<Block> MORB_BLOCK = register("morb_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE)
                    .sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().strength(1.5F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    //Utility methods
    private static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }


    //Registration Methods
    static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> ret = registerNoItem(name, block);
        AlchemineItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(tab)));
        return ret;
    }

}