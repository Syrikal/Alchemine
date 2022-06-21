package syric.alchemine.brewing.util;

import java.util.*;

public class AspectSet {
    private final Map<Aspect,Integer> aspectMap = new HashMap<Aspect, Integer>();

    //Constructor
    public AspectSet() {}


    //Getters
    public Map<Aspect, Integer> getMap() {
        return this.aspectMap;
    }

    public int get(Aspect aspect) {
        return aspectMap.getOrDefault(aspect, 0);
    }

    public int totalPoints() {
        int acc = 0;
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            acc += entry.getValue();
        }
        return acc;
    }

    public List<Aspect> getMaxAspects() {
        int max = 0;
        List<Aspect> output = new ArrayList<>();
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            if (entry.getValue() == max) {
                output.add(entry.getKey());
            }
        }
        return output;
    }

    public Aspect randomMaxAspect() {
        Random rand = new Random();
        List<Aspect> maxes = getMaxAspects();
        int index = rand.nextInt(maxes.size());
        return maxes.get(index);
    }

    //weight exactly proportional to values
    public Aspect randomAspect() {
        int total = this.totalPoints();
        Random rand = new Random();
        int index = rand.nextInt(total);

        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            if (index <= entry.getValue() - 1) {
                return entry.getKey();
            } else {
                index -= entry.getValue();
            }
        }

        System.out.println("Defaulted on random aspect selection");
        return getMaxAspects().get(0);
    }

    //Counts reactions to a new AspectSet being added. Doesn't add the set.
    public int countReactions(AspectSet input) {
        int count = 0;
        for (Map.Entry<Aspect, Integer> inputEntry : input.getMap().entrySet()) {
            for (Map.Entry<Aspect, Integer> existingEntry : aspectMap.entrySet()) {
                if (existingEntry.getKey().reacts(inputEntry.getKey())) {
                    count += existingEntry.getValue() * inputEntry.getValue();
                }
            }
        }
        return count;
    }

    //Counts stabilizations with a new AspectSet being added. Doesn't add the set.
    public int countStabilizations(AspectSet input) {
        int count = 0;
        for (Map.Entry<Aspect, Integer> inputEntry : input.getMap().entrySet()) {
            for (Map.Entry<Aspect, Integer> existingEntry : aspectMap.entrySet()) {
                if (existingEntry.getKey().stabilizes(inputEntry.getKey())) {
                    count += existingEntry.getValue() * inputEntry.getValue();
                }
            }
        }
        return count;
    }

    public int thermophilicPoints() {
        int count = 0;
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            if (entry.getKey().isThermophilic()) {
                count += entry.getValue();
            }
        }
        return count;
    }

    public int cryophilicPoints() {
        int count = 0;
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            if (entry.getKey().isCryophilic()) {
                count += entry.getValue();
            }
        }
        return count;
    }

    public boolean checkContradictions() {
        for (Aspect i : aspectMap.keySet()) {
            for (Aspect j : aspectMap.keySet()) {
                if (i.reacts(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Aspect, Integer> entry : aspectMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


    //Setters
    public void add(Aspect aspect, int val) {
        if (aspectMap.containsKey(aspect)) {
            setAspect(aspect, aspectMap.get(aspect) + val);
        } else {
            setAspect(aspect, val);
        }
    }

    public void add(AspectSet inputSet) {
        for (Map.Entry<Aspect, Integer> entry : inputSet.aspectMap.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public void setAspect(Aspect aspect, int val) {
        if (val < 0) {
            throw new ArithmeticException("Tried to aspectMap an aspect value to a negative number");
        } else {
            aspectMap.put(aspect, val);
        }
    }

    public void clear() {
        aspectMap.clear();
    }


    //Builder class
    public static class ASBuilder {
        private final Map<Aspect, Integer> buildSet = new HashMap<Aspect, Integer>();

        public ASBuilder() {}

        public ASBuilder(Aspect aspect, int val) {
            this.buildSet.put(aspect, val);
        }

        public ASBuilder add(Aspect aspect, int val) {
            if (buildSet.containsKey(aspect)) {
                buildSet.put(aspect, buildSet.get(aspect) + val);
            } else {
                buildSet.put(aspect, val);
            }
            return this;
        }

        public AspectSet build() {
            AspectSet output = new AspectSet();
            for (Map.Entry<Aspect, Integer> entry : buildSet.entrySet()) {
                output.add(entry.getKey(), entry.getValue());
            }
            return output;
        }

    }


}
