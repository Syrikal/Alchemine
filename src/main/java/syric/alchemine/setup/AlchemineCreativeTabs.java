package syric.alchemine.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AlchemineCreativeTabs {

    public static final CreativeModeTab ALCHEMY = new CreativeModeTab("alchemy") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AlchemineBlocks.ALCHEMICAL_CAULDRON.get());
        }
    };

    public static final CreativeModeTab ALCHEMICAL_CREATIONS = new CreativeModeTab("alchemycreations") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AlchemineBlocks.BOUNCE_SLIME.get());
        }
    };

}
