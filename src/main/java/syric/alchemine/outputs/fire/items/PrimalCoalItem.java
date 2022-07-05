package syric.alchemine.outputs.fire.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;

public class PrimalCoalItem extends Item implements IForgeItem {

    public PrimalCoalItem(Properties properties) {
        super(properties);
    }


    //Smelts one stack of items
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 12800;
    }

}
