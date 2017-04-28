package edu.virginia.engine.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jaz on 4/27/17.
 */
public class RNJesus {

    private List<String> normal = Arrays.asList("3x1platform.png","spikerow.png","LaserGun.png","1x1platform.png", "box.png", "sawblade.png","slidingplatform.png","Dynamite.png");
    private List<String> noLasers = Arrays.asList("3x1platform.png","spikerow.png","1x1platform.png", "box.png", "sawblade.png","slidingplatform.png","Dynamite.png");
    private List<String> moreDynamite = Arrays.asList("3x1platform.png","spikerow.png","LaserGun.png","1x1platform.png", "box.png", "sawblade.png","slidingplatform.png","Dynamite.png","Dynamite.png","Dynamite.png");


    public RNJesus() {
    }

    public String intelligentlyRandomize(int roundsSinceLevelCompleted, int numberOfLaserGuns) {
        if(roundsSinceLevelCompleted > 3) {
            int random = ThreadLocalRandom.current().nextInt(0,moreDynamite.size());
            System.out.println("Adding dynamite");
            return moreDynamite.get(random);
        } else if(numberOfLaserGuns > 5) {
            int random = ThreadLocalRandom.current().nextInt(0,noLasers.size());
            System.out.println("Less lasers");
            return noLasers.get(random);
        } else {
            int random = ThreadLocalRandom.current().nextInt(0,normal.size());
            return normal.get(random);
        }
    }


}
