package syric.alchemine.outputs.bouncy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class BounceSlimeBlock extends HalfTransparentBlock {

    public static final BooleanProperty STABLE = BooleanProperty.create("stable");
    protected static final VoxelShape TOP = Block.box(3.0D, 8.0D, 3.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape BOTTOM = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);


    public BounceSlimeBlock(BlockBehaviour.Properties properties, boolean stable) {
        super (properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STABLE, stable));
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (!entity.isSteppingCarefully()) {
            double lat = Math.sqrt(Math.pow(vec3.x, 2) + Math.pow(vec3.z, 2));

            double angleDegrees = entity.isSprinting() ? 40 : 90;

            double angle = angleDegrees * Math.PI / 180;
            double vert = 2.2 * Math.sin(angle);
            double latAdd = 2.2 * Math.cos(angle);
            double mult = (lat + latAdd) / lat;

            entity.setDeltaMovement(vec3.x * mult, vert, vec3.z*mult);
//            if (entity instanceof Player) {
//                ChatPrint.chatPrint("lateral speed: " + lat + ", launch angle: " + angleDegrees, (Player) entity);
//            }
        }
        super.stepOn(level, pos, state, entity);
        if (!level.isClientSide && !state.getValue(STABLE)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STABLE);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.or(TOP, BOTTOM);
    }
}


