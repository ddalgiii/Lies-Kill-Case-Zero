package characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuspectManager {
    private List<Suspect> suspects;
    private List<Suspect> previousMurders; // track previous murderers so that they don't repeat
    private Suspect lastMurderer; // stores last murderer
    private Random random;

    public SuspectManager(){
        random = new Random();
        suspects = new ArrayList<>();
        previousMurders = new ArrayList<>();

        // initiate suspects with their roles and REAL motives
        suspects.add(new Suspect("Lady Margaret Primelle de Valimont", "Daughter", "Forced marriage",
                    "Margaret is a refined and intelligent noblewoman who speaks with measured politeness, though her words sometimes carry sharp wit and defiance against her father's control."));

        suspects.add(new Suspect("Daryus Denham", "Chamberlain", "His wife lost her position",
                    "Daryus is a wise and composed chamberlain who speaks in a formal and calculated manner, always careful with his words but not afraid to give sharp observations."));

        suspects.add(new Suspect("Loenna Kammerzell", "Maid", "Lost head maid position",
        "Loenna is a strong-willed and fiercely protective maid who speaks bluntly, often with sarcasm, but has a warm and motherly side to those she trusts."));

        suspects.add(new Suspect("Anna Joe", "Lover", "Wanted to escape",
        "Anna Joe is a soft-spoken yet determined woman who speaks with a mix of cautious optimism and deep sorrow, always choosing her words carefully to hide her pain."));

        suspects.add(new Suspect("Scottie Joe", "Stable Boy", "Revenge for his sister",
        "Scottie Joe is a cheerful and loyal stable boy who speaks in an upbeat and friendly manner, often cracking jokes to lighten the mood but turning fierce when protecting his loved ones."));

        lastMurderer = null; // no murderer at the start
    }

    public Suspect assignMurderer(){
        // reset list if all suspects have been murderers
        if(previousMurders.size() == suspects.size()){
            previousMurders.clear();
        }

        // get a list of suspects who haven't been the murderer yet
        List<Suspect> availableSuspects = new ArrayList<>(suspects);
        availableSuspects.removeAll(previousMurders);

        // remove the last murderer if they are still in the available suspects list to avoid repetition
        if(lastMurderer != null && availableSuspects.contains(lastMurderer)){
            availableSuspects.remove(lastMurderer);
        }

        // pick a new murderer from remaining suspects
        Suspect murderer = availableSuspects.get(random.nextInt(availableSuspects.size()));

        // mark as guilty
        murderer.setGuilty(true);

        // store this murderer in the history
        previousMurders.add(murderer);
        lastMurderer = murderer;
        return murderer;
    }

    public List<Suspect> getSuspects(){
        return new ArrayList<>(suspects); // return a copy to prevent external modification
    }
}

