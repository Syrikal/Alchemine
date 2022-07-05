package syric.alchemine.setup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import syric.alchemine.brewing.ingredients.AlchemicalIngredients;
import syric.alchemine.brewing.util.AlchemicalRecipes;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffects;

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
        AlchemicalEffects.register(modEventBus);
    }

}