package syric.alchemine.outputs.bouncy.items;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SlimeseedItem extends Item {
    public static final String DATA_ENTITY = "SlimeseedData";

    public SlimeseedItem(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            Level level = context.getLevel();
            Slime entity = EntityType.SLIME.create(level);
//            BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
            ItemStack stack = context.getItemInHand();
            Player player = context.getPlayer();

            if (!level.isClientSide && stack.hasTag()) // read data first!: setting position before reading will reset that position!
            {
//            CompoundNBT tag = stack.getTag();
//            if (tag.contains(DATA_ENTITY)) entity.deserializeNBT(tag.getCompound(DATA_ENTITY));
                if (stack.hasCustomHoverName())
                    entity.setCustomName(stack.getHoverName()); // set entity name from stack name
            }

            entity.absMoveTo(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z);
            if (!level.noCollision(entity)) {
                return InteractionResult.FAIL;
            }

            if (!player.isCreative() || stack.getOrCreateTag().contains(DATA_ENTITY)) {
                player.setItemInHand(context.getHand(), ItemStack.EMPTY);
            }
            entity.setSize(2, true);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.yBodyRot = entity.yHeadRot = player.yHeadRot + 180;
            level.addFreshEntity(entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}