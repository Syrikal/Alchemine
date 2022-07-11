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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
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
            if (itemEntity.getItem().getItem() == Items.NETHERITE_SCRAP) {

                Vec3 itemCenter = itemEntity.getBoundingBox().getCenter();
                boolean xContained = (itemCenter.x <= pos.getX()+1 && itemCenter.x > pos.getX());
                boolean yContained = (itemCenter.y <= pos.getY()+1 && itemCenter.y > pos.getY());
                boolean zContained = (itemCenter.z <= pos.getZ()+1 && itemCenter.z > pos.getZ());

                boolean contained = xContained && yContained && zContained;

                if (contained && level.getRandom().nextDouble() < 0.2) {
                    int quantity = itemEntity.getItem().getCount();
                    ItemStack popStack = new ItemStack(AlchemineItems.FORGED_NETHERITE_SCRAP.get(), quantity);
                    itemEntity.discard();
                    popResource(level, pos, popStack);
                }

            }
        }


        float multiplier = entity.fireImmune() ? 1.5F : 1F;
        float damage = this.getFireDamage() / multiplier;

        if (entity instanceof ItemEntity) {
            entity.hurt(DamageSource.IN_FIRE, damage);
        } else {
            entity.hurt(DamageSource.MAGIC, damage / 2F);
            entity.hurt(DamageSource.IN_FIRE, damage / 2F);
        }



    }

    //Doesn't extinguish or spread.
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
    }

    public Block getSelf() {
        return AlchemineBlocks.ABYSSAL_FORGEFLAME.get();
    }

}
