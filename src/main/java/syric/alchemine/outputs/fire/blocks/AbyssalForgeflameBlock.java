package syric.alchemine.outputs.fire.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.AlchemineBlocks;
import syric.alchemine.setup.AlchemineItems;

public class AbyssalForgeflameBlock extends AbstractAlchemicalFireBlock {

    public AbyssalForgeflameBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 2);
            if (entity.getRemainingFireTicks() <= 0) {
                entity.setSecondsOnFire(16);
            }
        }
        if (entity instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().getItem().isFireResistant()) {
                return;
            }
            if (itemEntity.getItem().getItem() == Items.NETHERITE_SCRAP) {
                int quantity = itemEntity.getItem().getCount();
                ItemStack popStack = new ItemStack(AlchemineItems.FORGED_NETHERITE_SCRAP.get(), quantity);
                popResource(level, pos, popStack);
                itemEntity.kill();
            } else if (itemEntity.getItem().getItem() == AlchemineItems.FORGED_NETHERITE_SCRAP.get()) {
                LogUtils.getLogger().info("Detected forged scrap in the forgeflame");
            }
        }

        float multiplier = entity.fireImmune() ? 1.5F : 1F;
        float damage = this.getFireDamage() / multiplier;

        entity.hurt(DamageSource.MAGIC, damage / 2F);
        entity.hurt(DamageSource.IN_FIRE, damage / 2F);

    }

    //Doesn't extinguish or spread.
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
    }

    public Block getSelf() {
        return AlchemineBlocks.ABYSSAL_FORGEFLAME.get();
    }

}
