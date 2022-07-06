package syric.alchemine.outputs.general.alchemicaleffects.testeffects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import syric.alchemine.outputs.general.alchemicaleffects.AlchemicalEffect;
import syric.alchemine.outputs.general.alchemicaleffects.PlacementSet;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.PlacementPattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpherePattern;
import syric.alchemine.outputs.general.alchemicaleffects.placementpatterns.SpreadPattern;

import static syric.alchemine.util.ChatPrint.chatPrint;

public class StoneSphereEffect implements AlchemicalEffect {

    @Override
    public void detonate(UseOnContext context) {
        BlockPos pos = AlchemicalEffect.getOrigin(context);
        Level level = context.getLevel();


        PlacementPattern pattern = new SpherePattern(pos, 2);
        PlacementSet set = new PlacementSet(level);
//        StringBuilder sb = new StringBuilder();
//        sb.append("Placement set created. There are ").append(set.blockCount()).append(" blocks in it.\n");
        set = set.addPattern(pattern);
//        sb.append(" After adding pattern, there are ").append(set.blockCount()).append(" blocks in it.\n");
        set = set.cull(PlacementSet.BREAK_ON_PUSH);
//        sb.append("After culling incorrect blocks, there are ").append(set.blockCount()).append(" blocks in it.\n");
//        sb.append("Placing.\n");
//        chatPrint(sb.toString(), level);
        set.placeImmediate(Blocks.STONE, true);
//        new PlacementSet(level).addPattern(pattern).cull(PlacementSet.BREAK_ON_PUSH).placeImmediate(Blocks.STONE.defaultBlockState(), true);

    }

    @Override
    public String toString() { return "Stone Sphere"; }

}
