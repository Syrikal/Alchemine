package syric.alchemine.outputs.fire.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import syric.alchemine.setup.AlchemineItems;

public class EternalEmberItem extends Item implements IForgeItem {

    public EternalEmberItem(Properties properties) {
        super(properties);
    }

    //Smelts 16 stacks of items
//    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
//        return 204800;
//    }
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 200;
    }


    //Returns itself so it can keep going
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if (!hasContainerItem(itemStack))
        {
            return ItemStack.EMPTY;
        }
        return new ItemStack(AlchemineItems.ETERNAL_EMBER.get());
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

}
