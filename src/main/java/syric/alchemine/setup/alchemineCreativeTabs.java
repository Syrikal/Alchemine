package syric.alchemine.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class alchemineCreativeTabs {

    public static final CreativeModeTab ALCHEMY = new CreativeModeTab("alchemy") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(alchemineBlocks.ALCHEMICAL_CAULDRON.get());
        }
    };

    public static final CreativeModeTab ALCHEMICAL_CREATIONS = new CreativeModeTab("alchemycreations") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(alchemineBlocks.BOUNCE_SLIME.get());
        }
    };

}
