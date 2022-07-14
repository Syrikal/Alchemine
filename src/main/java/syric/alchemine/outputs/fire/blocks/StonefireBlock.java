package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import syric.alchemine.setup.AlchemineBlocks;

public class StonefireBlock extends AbstractAlchemicalFireBlock {

    public StonefireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    //BURNING ENTITIES
    //Damages entities and sets them on fire
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setSecondsOnFire(4);
            }
        }

        entity.hurt(DamageSource.IN_FIRE, this.getFireDamage());
        super.entityInside(state, level, pos, entity);
    }

    public static void bootStrap() {
        StonefireBlock stonefireBlock = (StonefireBlock) AlchemineBlocks.STONE_FIRE.get();
        stonefireBlock.setFlammable(Blocks.STONE, 40, 100);
        stonefireBlock.setFlammable(Blocks.ANDESITE, 40, 100);
        stonefireBlock.setFlammable(Blocks.GRANITE, 40, 100);
        stonefireBlock.setFlammable(Blocks.DIORITE, 40, 100);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE, 30, 80);
        stonefireBlock.setFlammable(Blocks.TUFF, 30, 80);
        stonefireBlock.setFlammable(Blocks.CALCITE, 10, 40);
        stonefireBlock.setFlammable(Blocks.DRIPSTONE_BLOCK, 30, 80);
        stonefireBlock.setFlammable(Blocks.COBBLESTONE, 10, 30);
        stonefireBlock.setFlammable(Blocks.MOSSY_COBBLESTONE, 10, 30);
        stonefireBlock.setFlammable(Blocks.COBBLED_DEEPSLATE, 10, 20);
        stonefireBlock.setFlammable(Blocks.NETHERRACK, 50, 100);
        stonefireBlock.setFlammable(Blocks.BLACKSTONE, 10, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE, 5, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_BRICKS, 5, 10);
        stonefireBlock.setFlammable(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 10, 20);
        stonefireBlock.setFlammable(Blocks.BASALT, 30, 80);
        stonefireBlock.setFlammable(Blocks.SMOOTH_BASALT, 5, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_ANDESITE, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_GRANITE, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DIORITE, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DEEPSLATE, 10, 20);
        stonefireBlock.setFlammable(Blocks.CHISELED_DEEPSLATE, 10, 20);
        stonefireBlock.setFlammable(Blocks.SMOOTH_STONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.STONE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.CHISELED_STONE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.MOSSY_STONE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.CRACKED_STONE_BRICKS, 10, 40);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.CRACKED_DEEPSLATE_BRICKS, 10, 40);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_TILES, 5, 20);
        stonefireBlock.setFlammable(Blocks.CRACKED_DEEPSLATE_TILES, 10, 40);
        stonefireBlock.setFlammable(Blocks.OBSIDIAN, 5, 0);
        stonefireBlock.setFlammable(Blocks.CRYING_OBSIDIAN, 5, 0);
        stonefireBlock.setFlammable(Blocks.SANDSTONE, 40, 100);
        stonefireBlock.setFlammable(Blocks.CUT_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.SMOOTH_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.CHISELED_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.RED_SANDSTONE, 40, 100);
        stonefireBlock.setFlammable(Blocks.CUT_RED_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.SMOOTH_RED_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.CHISELED_RED_SANDSTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.END_STONE, 20, 60);
        stonefireBlock.setFlammable(Blocks.END_STONE_BRICKS, 5, 20);

        //Stairs
        stonefireBlock.setFlammable(Blocks.STONE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.ANDESITE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.GRANITE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.DIORITE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.COBBLESTONE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.MOSSY_COBBLESTONE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.COBBLED_DEEPSLATE_STAIRS, 10, 20);
        stonefireBlock.setFlammable(Blocks.BLACKSTONE_STAIRS, 10, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_STAIRS, 5, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, 5, 10);
        stonefireBlock.setFlammable(Blocks.POLISHED_ANDESITE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_GRANITE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DIORITE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DEEPSLATE_STAIRS, 10, 20);
        stonefireBlock.setFlammable(Blocks.STONE_BRICK_STAIRS, 5, 20);
        stonefireBlock.setFlammable(Blocks.MOSSY_STONE_BRICK_STAIRS, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_BRICK_STAIRS, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_TILE_STAIRS, 5, 20);
        stonefireBlock.setFlammable(Blocks.SANDSTONE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.SMOOTH_SANDSTONE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.RED_SANDSTONE_STAIRS, 40, 100);
        stonefireBlock.setFlammable(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, 15, 30);
        stonefireBlock.setFlammable(Blocks.END_STONE_BRICK_STAIRS, 5, 20);

        //Slabs
        stonefireBlock.setFlammable(Blocks.STONE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.ANDESITE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.GRANITE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.DIORITE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.COBBLESTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.MOSSY_COBBLESTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.COBBLED_DEEPSLATE_SLAB, 10, 20);
        stonefireBlock.setFlammable(Blocks.BLACKSTONE_SLAB, 10, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_SLAB, 5, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, 5, 10);
        stonefireBlock.setFlammable(Blocks.POLISHED_ANDESITE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_GRANITE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DIORITE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.POLISHED_DEEPSLATE_SLAB, 10, 20);
        stonefireBlock.setFlammable(Blocks.SMOOTH_STONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.STONE_BRICK_SLAB, 5, 20);
        stonefireBlock.setFlammable(Blocks.MOSSY_STONE_BRICK_SLAB, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_BRICK_SLAB, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_TILE_SLAB, 5, 20);
        stonefireBlock.setFlammable(Blocks.SANDSTONE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.CUT_SANDSTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.SMOOTH_SANDSTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.RED_SANDSTONE_SLAB, 40, 100);
        stonefireBlock.setFlammable(Blocks.CUT_RED_SANDSTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.SMOOTH_RED_SANDSTONE_SLAB, 15, 30);
        stonefireBlock.setFlammable(Blocks.END_STONE_BRICK_SLAB, 5, 20);

        //Walls
        stonefireBlock.setFlammable(Blocks.ANDESITE_WALL, 40, 100);
        stonefireBlock.setFlammable(Blocks.GRANITE_WALL, 40, 100);
        stonefireBlock.setFlammable(Blocks.DIORITE_WALL, 40, 100);
        stonefireBlock.setFlammable(Blocks.COBBLESTONE_WALL, 15, 30);
        stonefireBlock.setFlammable(Blocks.MOSSY_COBBLESTONE_WALL, 15, 30);
        stonefireBlock.setFlammable(Blocks.COBBLED_DEEPSLATE_WALL, 10, 20);
        stonefireBlock.setFlammable(Blocks.BLACKSTONE_WALL, 10, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_WALL, 5, 20);
        stonefireBlock.setFlammable(Blocks.POLISHED_BLACKSTONE_BRICK_WALL, 5, 10);
        stonefireBlock.setFlammable(Blocks.POLISHED_DEEPSLATE_WALL, 10, 20);
        stonefireBlock.setFlammable(Blocks.STONE_BRICK_WALL, 5, 20);
        stonefireBlock.setFlammable(Blocks.MOSSY_STONE_BRICK_WALL, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_BRICK_WALL, 5, 20);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_TILE_WALL, 5, 20);
        stonefireBlock.setFlammable(Blocks.SANDSTONE_WALL, 40, 100);
        stonefireBlock.setFlammable(Blocks.RED_SANDSTONE_WALL, 40, 100);
        stonefireBlock.setFlammable(Blocks.END_STONE_BRICK_WALL, 5, 20);

        //Ores
        stonefireBlock.setFlammable(Blocks.COAL_ORE, 20, 40);
        stonefireBlock.setFlammable(Blocks.COPPER_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.IRON_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.GOLD_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.LAPIS_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.REDSTONE_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.EMERALD_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DIAMOND_ORE, 5, 10);

        stonefireBlock.setFlammable(Blocks.DEEPSLATE_COAL_ORE, 15, 30);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_COPPER_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_IRON_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_GOLD_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_LAPIS_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_REDSTONE_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_EMERALD_ORE, 5, 10);
        stonefireBlock.setFlammable(Blocks.DEEPSLATE_DIAMOND_ORE, 5, 10);

        stonefireBlock.setFlammable(Blocks.NETHER_GOLD_ORE, 10, 20);
        stonefireBlock.setFlammable(Blocks.NETHER_QUARTZ_ORE, 10, 20);



        //Infested
        stonefireBlock.setFlammable(Blocks.INFESTED_STONE, 40, 100);
        stonefireBlock.setFlammable(Blocks.INFESTED_COBBLESTONE, 15, 30);
        stonefireBlock.setFlammable(Blocks.INFESTED_DEEPSLATE, 30, 80);
        stonefireBlock.setFlammable(Blocks.INFESTED_STONE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.INFESTED_CHISELED_STONE_BRICKS, 5, 20);
        stonefireBlock.setFlammable(Blocks.INFESTED_CRACKED_STONE_BRICKS, 10, 40);
        stonefireBlock.setFlammable(Blocks.INFESTED_MOSSY_STONE_BRICKS, 5, 20);

    }

    public Block getSelf() {
        return AlchemineBlocks.STONE_FIRE.get();
    }

    @Override
    public boolean canBlockIgnite(BlockGetter level, BlockPos pos, Direction face) {
        return this.canIgniteStored(level.getBlockState(pos));
    }

    @Override
    public int getBlockIgniteChance(BlockGetter level, BlockPos pos, Direction face) {
        BlockState state = level.getBlockState(pos);
        return this.getIgniteChanceStored(state);
    }

    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return this.getBurnChanceStored(level.getBlockState(pos));
    }

}
