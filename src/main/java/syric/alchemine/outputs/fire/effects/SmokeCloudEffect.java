package syric.alchemine.outputs.fire.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.FlattenedSpreadPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;
import syric.alchemine.setup.AlchemineBlocks;

import java.util.Map;

public class SmokeCloudEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();

        //Play a sound?
        playSound(level, pos);

        PlacementPattern pattern = new FlattenedSpreadPattern(level, pos, 350, PlacementSet.AIR_ONLY, 0.4);
        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.AIR_ONLY).placeImmediate(AlchemineBlocks.SMOKE_CLOUD, true);

        Player player = context.getPlayer();
        //Turn the player invisible if they're in that space
        if (player != null) {
            AABB playerBox = player.getBoundingBox();
            for (BlockPos pos1 : pattern.blockMap().keySet()) {
                AABB blockBox = AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos1));
                if (playerBox.intersects(blockBox)) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, true, true));
                    break;
                }
            }
        }


    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource randomsource = level.getRandom();
        level.playSound((Player)null, pos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1.0F, (randomsource.nextFloat() - randomsource.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public String toString() { return "Smoke Cloud"; }


}
