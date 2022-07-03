package syric.alchemine.setup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import syric.alchemine.brewing.ingredients.AlchemicalIngredients;
import syric.alchemine.brewing.util.AlchemicalRecipes;

public class registry {


    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AlchemineBlocks.register(modEventBus);
        AlchemineItems.register(modEventBus);
        AlchemineBlockEntityTypes.register(modEventBus);
        AlchemineEffects.register(modEventBus);
        AlchemineSoundEvents.register(modEventBus);
        AlchemicalIngredients.register(modEventBus);
        AlchemicalRecipes.register(modEventBus);
        AlchemineEffects.register(modEventBus);
    }

}