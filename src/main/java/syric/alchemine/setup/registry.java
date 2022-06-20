package syric.alchemine.setup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class registry {


    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        AlchemineBlocks.register(modEventBus);
        AlchemineItems.register(modEventBus);
        AlchemineBlockEntityTypes.register(modEventBus);
    }

}