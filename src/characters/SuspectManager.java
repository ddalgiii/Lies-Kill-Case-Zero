package characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuspectManager {
    private final List<Suspect> suspects;
    private Suspect prevMurderer;
    
    public SuspectManager() {
        suspects = new ArrayList<>();
        initializeSuspects();
    }
    
    private void initializeSuspects() {

        suspects.add(new Suspect("Lady Margaret de Valimont", 20, "Anglo-French Noble", "Duke’s Daughter",
                "Kind, headstrong, but sheltered; resents her father’s control",
                "Raised strictly, only allowed to befriend Scottie and Anna, secretly dated Scottie",
                "Torn between love for Scottie and her father’s control, blind to Anna’s suffering",
                false));

        suspects.add(new Suspect("Daryus Denham", 56, "Half-Persian, half-local commoner", "Chamberlain",
                "Wise, reserved, loyal but morally conflicted",
                "Illegitimate child, worked his way up, secretly married Loenna, raised Margaret as a father figure",
                "Deeply respects Margaret, protective of Omar & Parvana, resents the Duke",
                false));

        suspects.add(new Suspect("Anna Joe", 19, "Tajik-Italian", "Duke’s Personal Maid",
                "Intelligent, compassionate, strong-willed but feels trapped, fiercely protective of Scottie",
                "Orphaned at 4, forced into an abusive relationship with the Duke at 16",
                "Hides her suffering from Scottie, trapped by the Duke’s control, conflicted with Margaret",
                false));

        suspects.add(new Suspect("Loenna Kammerzell", 59, "German descent", "Former Head Maid, Demoted to Common Maid",
                "Stern, fiercely loyal, deeply protective",
                "Descendant of nobility, lost status over generations, secretly married Daryus, demoted by the Duke",
                "Loves her husband, cares for the Tavellas, despises the Duke",
                false));

        suspects.add(new Suspect("Scottie Joe", 24, "Tajik-Italian", "Stable Boy (Formerly Footman)",
                "Loyal, protective, resourceful, cheerful but fierce when defending loved ones",
                "Fled with his sister to a new country, worked in Duke’s manor since 11, secretly dated Margaret",
                "Deeply protective of Anna Joe, in love with Margaret, enemy of the Duke",
                false));
    }
    
    public void assignMurderer() {
        Random rand = new Random();
        Suspect murderer;
        do {
            murderer = suspects.get(rand.nextInt(suspects.size()));
        } while (murderer.equals(prevMurderer));
        
        for (Suspect suspect : suspects) {
            suspect.setMurderer(false);
        }
        
        murderer.setMurderer(true);
        prevMurderer = murderer;
    }
    
    public List<Suspect> getSuspects() {
        return suspects;
    }
    
    public void printSuspects() {
        for (Suspect suspect : suspects) {
            System.out.println(suspect);
        }
    }
    public Suspect getSuspectByName(String name) {
        for (Suspect suspect : suspects) {
            if (suspect.getName().equals(name)) {
                return suspect;
            }
        }
        return null; // Returns null if no suspect with the given name is found
    }
}
