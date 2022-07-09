package syric.alchemine.outputs.fire.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import syric.alchemine.setup.AlchemineBlocks;

public class SunfireBlock extends AbstractAlchemicalFireBlock {

    public SunfireBlock(Properties properties, float damage) {
        super(properties, damage);
    }

    //Ticks faster
    @Override
    public int getFireTickDelay(RandomSource source) {
        return 15 + source.nextInt(10);
    }


//    @Override
//    public void tick(BlockState fireState, ServerLevel level, BlockPos firePos, RandomSource randSource) {
//        int age = fireState.getValue(AGE);
//        BlockState newFireState = fireState;
//        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
//            int newage = Math.min(15, age + randSource.nextInt(5) / 2);
//            if (age != newage) {
//                newFireState = fireState.setValue(AGE, newage);
//                LogUtils.getLogger().info("Changing a sunfire block's age from " + age + " to " + newage);
//                level.setBlock(firePos, newFireState, 3);
//            }
//        }
//        super.tick(newFireState, level, firePos, randSource);
//        if (level.getBlockState(firePos).getBlock() instanceof SunfireBlock) {
//            age = level.getBlockState(firePos).getValue(AGE);
//            LogUtils.getLogger().info("Age after tick is " + age);
//        }
//    }

    //Only burns undead
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity live && live.getMobType() == MobType.UNDEAD) {
            if (!entity.fireImmune()) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                if (entity.getRemainingFireTicks() == 0) {
                    entity.setSecondsOnFire(16);
                }
            }
            entity.hurt(DamageSource.IN_FIRE, this.getFireDamage() * 4);
            super.entityInside(state, level, pos, entity);
        } else {
            super.entityInside(state, level, pos, entity);
        }
    }

    //Spreads slower
    public int modifiedIgniteChance(int baseIgniteChance, int age, Level level, BlockPos pos) {
        baseIgniteChance += 20;
        baseIgniteChance /= (age * 3 + 30);
        if (level.isHumidAt(pos)) {baseIgniteChance /= 3;}
        if (age >= 7) {
            baseIgniteChance /= 2;
        }
        baseIgniteChance /= 3;
        return baseIgniteChance;
    }

    public Block getSelf() {
        return AlchemineBlocks.LIFEBANE_FIRE.get();
    }

    //Half as likely to burn things
    @Override
    public int getBlockBurnChance(Level level, BlockPos pos, Direction face) {
        return super.getBlockBurnChance(level, pos, face) / 2;
    }

}