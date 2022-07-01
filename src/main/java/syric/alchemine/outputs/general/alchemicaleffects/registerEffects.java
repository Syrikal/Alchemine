package syric.alchemine.outputs.general.alchemicaleffects;

import syric.alchemine.outputs.bouncy.effects.CrashPadEffect;
import syric.alchemine.outputs.bouncy.effects.InstantShelterEffect;
import syric.alchemine.outputs.slippery.effects.OilSlickEffect;
import syric.alchemine.outputs.slippery.effects.WallSlideEffect;
import syric.alchemine.outputs.sticky.effects.FoamsnareEffect;
import syric.alchemine.outputs.sticky.effects.GlueStickEffect;
import syric.alchemine.outputs.sticky.effects.TarStickEffect;
import syric.alchemine.outputs.sticky.effects.WebsnareEffect;

public class registerEffects {

    public static final AlchemicalEffect CRASH_PAD_EFFECT = new CrashPadEffect();
    public static final AlchemicalEffect INSTANT_SHELTER_EFFECT = new InstantShelterEffect();
    public static final AlchemicalEffect FOAMSNARE_EFFECT = new FoamsnareEffect();
    public static final AlchemicalEffect WEBSNARE_EFFECT = new WebsnareEffect();
    public static final AlchemicalEffect TAR_STICK_EFFECT = new TarStickEffect();
    public static final AlchemicalEffect GLUE_STICK_EFFECT = new GlueStickEffect();
    public static final AlchemicalEffect STONE_BLOB_EFFECT = new StoneBlobEffect();
    public static final AlchemicalEffect OIL_SLICK_EFFECT = new OilSlickEffect();
    public static final AlchemicalEffect WALL_SLIDE_EFFECT = new WallSlideEffect();

}
