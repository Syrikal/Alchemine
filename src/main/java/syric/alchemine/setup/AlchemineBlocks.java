package syric.alchemine.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
import syric.alchemine.outputs.blocks.*;
import syric.alchemine.brewing.cauldron.AlchemicalCauldronBlock;

import java.util.function.Supplier;

public class AlchemineBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Alchemine.MODID);

    //Fancy
    public static final RegistryObject<Block> ALCHEMICAL_CAULDRON = register("alchemical_cauldron",
            () -> new AlchemicalCauldronBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.STONE)
                    .requiresCorrectToolForDrops().strength(2.0F).noOcclusion()),
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
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> BERSERKERS_RESIN = register("berserkers_resin",
            () -> new BerserkersResinBlock(BlockBehaviour.Properties.of(Material.POWDER_SNOW, MaterialColor.COLOR_ORANGE)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape().strength(1.0F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    public static final RegistryObject<Block> RED_SLIME = register("red_slime",
            () -> new RedSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.TERRACOTTA_RED)
                    .friction(.8F).sound(SoundType.SLIME_BLOCK).noOcclusion()),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

//    public static final RegistryObject<Block> SHELL_SLIME = register("shell_slime",
//            () -> new ShellSlimeBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.GRASS)
//                    .friction(.8F).sound(SoundType.SLIME_BLOCK).strength(4.0F)),
//            alchemineCreativeTabs.ALCHEMICAL_CREATIONS);



    //Morb
    public static final RegistryObject<Block> MORB_BLOCK = register("morb_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE)
                    .sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().strength(1.5F)),
            AlchemineCreativeTabs.ALCHEMICAL_CREATIONS);

    //Methods
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