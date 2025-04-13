package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import characters.Suspect;
import characters.SuspectManager;

public class AIMurderLogic {
    private static String murderMethod;
    private static String bodyDiscoveredBy;
    private static Suspect murderer;

    private static final String[] DISCOVERERS = {
        "Lady Margaret de Valimont",
        "Mr. Daryus Denham",
        "Miss Anna Joe",
        "Scottie Joe",
        "Loenna Kammerzell"
    };

    private static final Map<String, String[]> MURDER_METHODS = new HashMap<>();

    static {
        MURDER_METHODS.put("Lady Margaret de Valimont", new String[]{
            "poisoned his wine at dinner",
            "stabbed him in the heart with a letter opener"
        });
        MURDER_METHODS.put("Mr. Daryus Denham", new String[]{
            "strangled him with a silk cord",
            "bludgeoned him with a fireplace poker"
        });
        MURDER_METHODS.put("Miss Anna Joe", new String[]{
            "stabbed him in the back with sewing shears",
            "poisoned his tea with nightshade"
        });
        MURDER_METHODS.put("Scottie Joe", new String[]{
            "beat him to death with a riding crop",
            "slashed his throat with a stable knife"
        });
        MURDER_METHODS.put("Loenna Kammerzell", new String[]{
            "strangled him with a scarf",
            "pushed him down the grand staircase"
        });
    }

    public static void determineMurderDetails(SuspectManager suspectManager) {
        Random rand = new Random();
        suspectManager.assignMurderer();
        
        for (Suspect suspect : suspectManager.getSuspects()) {
            if (suspect.isMurderer()) {
                murderer = suspect;
                break;
            }
        }

        murderMethod = MURDER_METHODS.get(murderer.getName())[rand.nextInt(MURDER_METHODS.get(murderer.getName()).length)];
        bodyDiscoveredBy = DISCOVERERS[rand.nextInt(DISCOVERERS.length)];
    }

    public static Suspect getMurderer() {
        return murderer;
    }

    public static String getMurderMethod() {
        return murderMethod;
    }

    public static String getBodyDiscoveredBy() {
        return bodyDiscoveredBy;
    }
}
