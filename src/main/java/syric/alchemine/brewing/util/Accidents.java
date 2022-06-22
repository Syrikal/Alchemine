package syric.alchemine.brewing.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import syric.alchemine.brewing.cauldron.AlchemicalCauldronBlockEntity;


public class Accidents {

    //Explosions
    public static boolean checkExplosion(AlchemicalCauldronBlockEntity cauldron) {
        return false;
    }

    public static void explode(AlchemicalCauldronBlockEntity cauldron) {

        //CHECK IF OBSTRUCTED!

        int magnitude = calculateExplosionMagnitude(cauldron);
        int numberProjectiles = 4 + magnitude / 2;

        for (int i = 0; i < numberProjectiles; i++) {
            //create a projectile

            //Choose a type
            ProjectileType projectileType = ProjectileType.STANDARD;
            double typeRandom = Math.random();
            if (typeRandom < .005 * magnitude) {
                projectileType = ProjectileType.CATASTROPHE;
            } else if (typeRandom > .9-.02* magnitude) {
                projectileType = ProjectileType.RARE;
            }
            if (cauldron.base.type == BaseTypes.POTION) {
                projectileType = ProjectileType.POTION;
            }

            //Choose an aspect
            Aspect projectileAspect = null;
            Aspect primaryAspect = cauldron.aspects.randomMaxAspect();
            double aspectRandom = Math.random();
            if (aspectRandom < 0.6 || i == 0) {
                projectileAspect = primaryAspect;
            } else {
                projectileAspect = cauldron.aspects.randomAspect();
            }

            //Choose a size
            double projectileSize = 0;
            RandomSource source = RandomSource.create();
            double maxSize = (double)(magnitude - 1) / 18 + .5D;
            double minSize = (double)(magnitude - 1) * 2 / 9 + 1.5;
            if (i == 0) {
                projectileSize = maxSize * 1.5D;
            } else {
                projectileSize = Mth.randomBetween(source, (float) minSize, (float) maxSize);
            }

            double distance = 0;
            double distanceRandom = Math.random();
            if (i == 0) {
                distance = 0;
            } else if (projectileType == ProjectileType.CATASTROPHE) {
                if (distanceRandom < 0.3) {
                    distance = Mth.randomBetween(source, 0F, (float) magnitude * 6);
                } else if (distanceRandom < 0.6) {
                    distance = Mth.randomBetween(source, (float) magnitude * 6, (float) magnitude * 10);
                } else {
                    distance = Mth.randomBetween(source, (float) magnitude * 10, (float) magnitude * 20);
                }
            } else {
                if (distanceRandom < 0.15) {
                    distance = Mth.randomBetween(source, 0F, (float) magnitude * 2);
                } else if (distanceRandom < .65) {
                    distance = Mth.randomBetween(source,(float) magnitude * 2, (float) magnitude * 6);
                } else if (distanceRandom < .95) {
                    distance = Mth.randomBetween(source, (float) magnitude * 6, (float) magnitude * 10);
                } else {
                    distance = Mth.randomBetween(source, (float) magnitude * 6, (float) magnitude * 20);
                }
            }

            double angle = Mth.randomBetween(source, (float) 10, (float) 80);

            Vec3 launchVector = getLaunchVector(angle, distance);

        }

    }

    private static int calculateExplosionMagnitude(AlchemicalCauldronBlockEntity cauldron) {
        double energy = cauldron.energy;
        int volatility = cauldron.volatility;
        int stability = cauldron.stability;
        int count = 1;

        //Energy contribution
        if (energy >= 6) {
            count++;
        }
        if (energy >= 9) {
            count++;
        }
        if (energy >= 12) {
            count++;
        }
        if (energy >= 15) {
            count++;
        }

        //Volatility contribution
        if (volatility < 0) {
            count--;
        }
        if (volatility >= 3) {
            count++;
        }
        if (volatility >= 6) {
            count++;
        }
        if (volatility >= 9) {
            count++;
        }


        //Stability contribution
        if (stability < 8) {
            count++;
        }
        if (stability < 3) {
            count++;
        }

        //Bounding
        count = Math.max(1, Math.min(count, 10));

        return count;
    }

    private static Vec3 getLaunchVector(double angle, double distance) {
        return Vec3.ZERO;
    }

    //Sludges
    public static boolean checkSludge(AlchemicalCauldronBlockEntity cauldron) {
        return false;
    }

    public static void sludge(AlchemicalCauldronBlockEntity cauldron) {}

}

enum ProjectileType {
    STANDARD, RARE, CATASTROPHE, POTION;
}


