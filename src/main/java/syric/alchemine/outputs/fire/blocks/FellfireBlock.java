package syric.alchemine.outputs.fire.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import org.apache.commons.lang3.StringUtils;
import syric.alchemine.setup.AlchemineBlocks;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class FellfireBlock extends AbstractAlchemicalFireBlock {

    public FellfireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public int getFireTickDelay(RandomSource source) {
        return 20 + source.nextInt(10);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() >= -10 && entity.getRemainingFireTicks() <= 0) {
                entity.setSecondsOnFire(16);
            }
        }
        entity.hurt(DamageSource.IN_FIRE, this.getFireDamage());
    }

    @Override
    //Age makes it slow down more than normal, but it's much faster at first.
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 60;
        baseIgniteChance += level.getDifficulty().getId() * 7;
        baseIgniteChance /= (2 * age) + 10;
        if (level.isHumidAt(pos)) {baseIgniteChance /= 2;}
        if (age == 15) {
            baseIgniteChance /= 6;
        }
        return baseIgniteChance;
    }

    @Override
    //Chance to remove it if it's old enough
    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && fireState.getValue(AGE) == 15 && randSource.nextInt(50) == 0) {
            level.removeBlock(firePos, false);
            return;
        }
        super.tick(fireState, level, firePos, randSource);
    }


    public Block getSelf() {
        return AlchemineBlocks.FELL_FIRE.get();
    }

    @Override
    public boolean canBlockIgnite(BlockGetter level, BlockPos pos, Direction face) {
        return (level.getBlockState(pos).isFaceSturdy(level, pos, face) || level.getBlockState(pos).is(BlockTags.LEAVES));
    }

    @Override
    public int getBlockIgniteChance(BlockGetter level, BlockPos pos, Direction face) {
        if (!level.getBlockState(pos).isFaceSturdy(level, pos, face) && !level.getBlockState(pos).is(BlockTags.LEAVES)) {
            return 0;
        } else {
            return 40;
        }
    }

    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return 0;
    }

}
