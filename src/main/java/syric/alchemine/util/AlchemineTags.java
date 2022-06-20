package syric.alchemine.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import syric.alchemine.Alchemine;

public class AlchemineTags {

    public static class Blocks {
        public static final TagKey<Block> BOUNCY_PRODUCTS = tag("bouncy_products");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Alchemine.MODID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }

    public static class Items {
        public static final TagKey<Item> CAULDRON_EXTRACTORS = tag("cauldron_extractors");

        public static final TagKey<Item> CAULDRON_STIRRERS = tag("cauldron_stirrers");

        public static final TagKey<Item> CATALYSTS = tag("catalysts");

        public static final TagKey<Item> INGREDIENTS = tag("ingredients");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Alchemine.MODID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

}
