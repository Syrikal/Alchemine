package syric.alchemine.setup;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

public class AlchemineBlockMaterials {
    public static final Material SHELL_SLIME_MAT = (new Builder(MaterialColor.GRASS)).noCollider().notSolidBlocking().nonSolid().notPushable().build();
    public static final Material TAR_DEC_MAT = (new Builder(MaterialColor.COLOR_BLACK)).noCollider().notSolidBlocking().nonSolid().flammable().replaceable().destroyOnPush().build();
    public static final Material TAR_MAT = (new Builder(MaterialColor.COLOR_BLACK)).flammable().build();
    public static final Material GLUE_MAT = (new Builder(MaterialColor.TERRACOTTA_WHITE)).noCollider().notSolidBlocking().nonSolid().replaceable().notPushable().build();


    public static class Builder {
        private PushReaction pushReaction = PushReaction.NORMAL;
        private boolean blocksMotion = true;
        private boolean flammable;
        private boolean liquid;
        private boolean replaceable;
        private boolean solid = true;
        private final MaterialColor color;
        private boolean solidBlocking = true;

        public Builder(MaterialColor color) {
            this.color = color;
        }

        public AlchemineBlockMaterials.Builder liquid() {
            this.liquid = true;
            return this;
        }

        public AlchemineBlockMaterials.Builder nonSolid() {
            this.solid = false;
            return this;
        }

        public AlchemineBlockMaterials.Builder noCollider() {
            this.blocksMotion = false;
            return this;
        }

        public AlchemineBlockMaterials.Builder notSolidBlocking() {
            this.solidBlocking = false;
            return this;
        }

        public AlchemineBlockMaterials.Builder flammable() {
            this.flammable = true;
            return this;
        }

        public AlchemineBlockMaterials.Builder replaceable() {
            this.replaceable = true;
            return this;
        }

        public AlchemineBlockMaterials.Builder destroyOnPush() {
            this.pushReaction = PushReaction.DESTROY;
            return this;
        }

        public AlchemineBlockMaterials.Builder notPushable() {
            this.pushReaction = PushReaction.BLOCK;
            return this;
        }

        public Material build() {
            return new Material(this.color, this.liquid, this.solid, this.blocksMotion, this.solidBlocking, this.flammable, this.replaceable, this.pushReaction);
        }
    }

}
