package syric.alchemine.brewing.util;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import syric.alchemine.Alchemine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class AlchemicalRecipes {

    private static boolean isInitialized = false;

    public static final DeferredRegister<Recipe> RECIPES = DeferredRegister.create(new ResourceLocation(Alchemine.MODID, "recipes"), Alchemine.MODID);

    public static final List<Recipe> RECIPE_LIST = new ArrayList<>();
    public static final HashMap<Aspect, List<Recipe>> RECIPES_BY_BASE = new HashMap<>();


    public static void register(final IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        }
        RECIPES.makeRegistry(RegistryBuilder::new);
        RECIPES.register(modEventBus);
        isInitialized = true;
        LogUtils.getLogger().info("Ingredients initialized");
    }

    private static <T extends Recipe> RegistryObject<T> register(String name, Supplier<T> recipe) {
        RegistryObject<T> ret = RECIPES.register(name, recipe);
        RECIPE_LIST.add(recipe.get());
        for (Aspect a : recipe.get().getBaseAspects()) {
            List<Recipe> aspectList = RECIPES_BY_BASE.get(a);
            if (aspectList == null) {
                RECIPES_BY_BASE.put(a, new ArrayList<>());
                aspectList = RECIPES_BY_BASE.get(a);
            }
            aspectList.add(recipe.get());
        }
        return ret;
    }

}
