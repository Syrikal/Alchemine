package syric.alchemine.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import syric.alchemine.Alchemine;
import syric.alchemine.brewing.ingredients.Ingredient;

public class AlchemineRegistries {

    public static void registerRegistries(final NewRegistryEvent e) {
        RegistryBuilder<Ingredient> ingredientRegistryBuilder = new RegistryBuilder<>();
        ingredientRegistryBuilder.setName(new ResourceLocation(Alchemine.MODID, "ingredients"));
    }

}
