package characters;

import java.util.Random;

public class CaseFile {
    private static final String VICTIM = "Duke Morvander de Valimont";
    private static final String STATUS = "Deceased";
    private static final String ROLE = "Patriarch of the de Valimont family";
    private static final String BACKSTORY = """
            Duke Morvander de Valimont was a powerful noble who ruled his household with an iron fist.
            He was feared by his servants, he was overprotective of his daughter, and had many secrets.
            Last evening, he was found murdered in his manor, sending shokewaves through the household.
            """;

    // private static String murderMethod; // Removed unused variable
    private static String bodyDiscoveredBy;

    private static final String[] DISCOVERERS = {
        "Lady Margaret de Valimont, his daughter",
        "Mr. Daryus Denham, his loyal butler",
        "Miss Anna Joe, his personal maid",
        "Scottie Joe, the stable boy",
        "Loenna Kammerzell, the maid"
    };

    public static void generateMurderDetails() {
        Random rand = new Random();
        bodyDiscoveredBy = DISCOVERERS[rand.nextInt(DISCOVERERS.length)];
    }

    public static String displayCaseIntro() {
        StringBuilder caseIntro = new StringBuilder();
        
        caseIntro.append("=== MURDER CASE FILE ===\n")
                 .append("Victim: ").append(VICTIM).append("\n")
                 .append("Status: ").append(STATUS).append("\n")
                 .append("Role: ").append(ROLE).append("\n")
                 .append(BACKSTORY).append("\n")
                 .append("The Duke was found dead last night,\n")
                 .append("His body was discovered by ").append(bodyDiscoveredBy).append(".\n\n")
                 .append("=== SUSPECTS PRESENT IN THE MANOR ===\n");
    
        // List only the known identities
        String[] suspects = {
            "Lady Margaret de Valimont, the Duke’s daughter",
            "Mr. Daryus Denham, the Duke’s head butler and chamberlain",
            "Miss Anna Joe, the Duke’s personal maid",
            "Scottie Joe, the stable boy",
            "Loenna Kammerzell, the maid"
        };
    
        for (String suspect : suspects) {
            caseIntro.append("- ").append(suspect).append("\n");
        }
    
        return caseIntro.toString();
    }
    
}
