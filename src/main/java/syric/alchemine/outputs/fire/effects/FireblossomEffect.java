package syric.alchemine.outputs.fire.effects;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;

import java.util.List;

public class FireblossomEffect implements AlchemicalEffect{


    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        //Fire and/or explosion sound
        playSound(level, pos);

        //Particle burst
        particleBurst(level, context);

        PlacementPattern pattern = new SpherePattern(pos, 4F);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.AIR_ONLY).cull(PlacementSet.randomFilter(0.4F)).placeImmediate(Blocks.FIRE, false);

        //Damage and ignite entities
        Vec3 posVector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 firstVector = posVector.add(new Vec3(-4, -4, -4));
        Vec3 secondVector = posVector.add(new Vec3(4, 4, 4));
        List<Entity> entityList = level.getEntities(null, new AABB(firstVector, secondVector));
        for (Entity entity : entityList) {
            if (entity instanceof LivingEntity && !entity.fireImmune() && entity.getBoundingBox().getCenter().distanceTo(posVector) <= 4) {
                entity.setSecondsOnFire(8);
                entity.hurt(DamageSource.IN_FIRE, 8);
            }
        }

    }


    private void playSound(Level level, BlockPos pos) {
        RandomSource randomsource = level.getRandom();
        level.playSound((Player)null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 2.0F, (randomsource.nextFloat() - randomsource.nextFloat()) * 0.2F + 1.0F);
        level.playSound((Player)null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.8F, 1.3F);
    }

    private void particleBurst(Level level, UseOnContext context) {
        RandomSource source = level.getRandom();
        for (int i = 0; i < 20; i++) {
            double d0 = context.getClickLocation().x;
            double d1 = context.getClickLocation().y;
            double d2 = context.getClickLocation().z;

            double magnitude = source.nextDouble() * 0.2;
            Vec3 direction = new Vec3(source.nextDouble() - 0.5, source.nextDouble() - 0.5, source.nextDouble() - 0.5).normalize().scale(magnitude);

            LogUtils.getLogger().info("Creating particle at location (" + d0 + "," + d1 + ","  + d2 + "), side is " + (level.isClientSide() ? "client" : "server"));
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, direction.x, direction.y, direction.z);
        }
    }


    @Override
    public String toString() { return "Fireblossom"; }

}
