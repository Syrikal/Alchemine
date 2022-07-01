package syric.alchemine.outputs.general.sludges;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import syric.alchemine.setup.AlchemineBlocks;

public class AdhesiveSludgeBlock extends SludgeBlock {
    protected static final VoxelShape SHAPE = Block.box(1D, 0.0D, 1D, 15D, 16.0D, 15D);
    private static MobEffectInstance getFatigue() { return new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200, 1, true, true); }

    public AdhesiveSludgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3(0.01, 0.01, 0.01));
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        player.addEffect(getFatigue());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }



}
