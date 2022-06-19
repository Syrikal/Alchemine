package syric.alchemine.brewing.cauldron;

public enum Aspect {
    BOUNCY,
    STICKY,
    SLIPPERY,

    FIRE,
    ICE,
    LIGHT,

    CAUSTIC,
    METALLIC,
    VITAL,

    SOLID,
    EXPLOSIVE,
    ZEPHYROUS;


    public boolean reacts(Aspect aspect) {
        return switch (this) {
            case BOUNCY, VITAL, LIGHT -> false;
            case STICKY -> aspect.equals(SLIPPERY);
            case SLIPPERY -> aspect.equals(STICKY);
            case FIRE -> aspect.equals(ICE);
            case ICE -> aspect.equals(FIRE);
            case CAUSTIC -> aspect.equals(METALLIC);
            case METALLIC -> aspect.equals(CAUSTIC);
            case SOLID -> aspect.equals(EXPLOSIVE) || aspect.equals(ZEPHYROUS);
            case EXPLOSIVE, ZEPHYROUS -> aspect.equals(SOLID);
        };
    }
    
    public boolean stabilizes(Aspect aspect) {
        return switch (this) {
            case BOUNCY -> aspect.equals(METALLIC) || aspect.equals(VITAL);
            case STICKY -> aspect.equals(CAUSTIC) || aspect.equals(VITAL);
            case SLIPPERY -> aspect.equals(ICE) || aspect.equals(ZEPHYROUS);
            case FIRE -> aspect.equals(EXPLOSIVE) || aspect.equals(LIGHT);
            case ICE -> aspect.equals(SOLID) || aspect.equals(SLIPPERY);
            case LIGHT -> aspect.equals(FIRE) || aspect.equals(ZEPHYROUS);
            case CAUSTIC -> aspect.equals(EXPLOSIVE) || aspect.equals(STICKY);
            case METALLIC -> aspect.equals(SOLID) || aspect.equals(BOUNCY);
            case VITAL -> aspect.equals(BOUNCY) || aspect.equals(STICKY);
            case SOLID -> aspect.equals(METALLIC) || aspect.equals(ICE);
            case EXPLOSIVE -> aspect.equals(FIRE) || aspect.equals(CAUSTIC);
            case ZEPHYROUS -> aspect.equals(SLIPPERY) || aspect.equals(LIGHT);
        };
    }

    public boolean isThermophilic() {
        return this.equals(FIRE) || this.equals(LIGHT) || this.equals(METALLIC) || this.equals(EXPLOSIVE);
    }

    public boolean isCryophilic() {
        return this.equals(BOUNCY) || this.equals(ICE) || this.equals(SOLID) || this.equals(ZEPHYROUS);
    }
    
}
