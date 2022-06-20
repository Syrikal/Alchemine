package syric.alchemine.brewing.ingredients;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.Alchemine;
import syric.alchemine.setup.AlchemineItems;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.LogManager;

public class AlchemicalIngredients {

    private static boolean isInitialized = false;

    public static final DeferredRegister<Ingredient> INGREDIENTS = DeferredRegister.create(new ResourceLocation(Alchemine.MODID, "Ingredients"), Alchemine.MODID);

    public static final HashMap<Item, Ingredient> INGREDIENTS_MAP = new HashMap<>();

    public static void register(final IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        }
        INGREDIENTS.makeRegistry(RegistryBuilder::new);
        INGREDIENTS.register(modEventBus);
        isInitialized = true;
        LogManager.getLogManager().getLogger(Alchemine.MODID).info("Registered ingredients");
    }

    private static <T extends Ingredient> RegistryObject<T> register(String name, Supplier<T> ingredient, Item item) {
        RegistryObject<T> ret = INGREDIENTS.register(name, ingredient);
        INGREDIENTS_MAP.put(item, ingredient.get());
        return ret;
    }
}
