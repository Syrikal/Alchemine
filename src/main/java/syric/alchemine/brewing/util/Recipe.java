package syric.alchemine.brewing.util;

import java.util.ArrayList;
import java.util.List;

public class Recipe {


    public List<Aspect> getBaseAspects() {
        List<Aspect> output = new ArrayList<Aspect>();
        output.add(Aspect.BOUNCY);
        return output;
    }

}
