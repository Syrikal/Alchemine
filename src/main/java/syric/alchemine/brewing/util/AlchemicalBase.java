package syric.alchemine.brewing.util;

public class AlchemicalBase {
    public BaseTypes type;
    public Aspect aspect;

    public AlchemicalBase(Aspect aspect) {
        this.type = BaseTypes.ASPECT;
        this.aspect = aspect;
    }
    public AlchemicalBase(BaseTypes cat) {
        this.type = cat;
        this.aspect = null;
    }

    public AlchemicalBase setAspect(Aspect aspect) {
        this.type = BaseTypes.ASPECT;
        this.aspect = aspect;
        return this;
    }

}