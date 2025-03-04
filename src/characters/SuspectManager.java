package characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuspectManager {
    private List<Suspect> suspects;
    private List<Suspect> previousMurders; //track previous murderers so that they dont repeat
    private Suspect lastMurderer; //stores last murderer
    private Random random;

    public SuspectManager(){
        random = new Random();
        suspects = new ArrayList<>();
        previousMurders = new ArrayList<>();

        //initate suspects with their roles and REAL motives
        suspects.add(new Suspect("Petunia Fields", "Daughter","Forced marrgiage"));
        suspects.add(new Suspect("Mr.Duke Denham","Butler","His wife lost her position"));
        suspects.add(new Suspect("Mrs.Lois Hemming", "Maid", "Lost head maid position"));
        suspects.add(new Suspect("Miss Oliva Joe","Lover","Wanted to escape"));
        suspects.add(new Suspect("Scottie Joe","Stable Boy","Revenge for his sister"));

        lastMurderer = null; //no murderer at the start
    }

    public Suspect assignMurderer(){
        //reset list if all suspectshave been murderers
        if(previousMurders.size() == suspects.size()){
            previousMurders.clear();
        }

        //get a list of suspects who havent been the murderer yet
        List<Suspect> avaliableSuspects = new ArrayList<>(suspects);
        avaliableSuspects.removeAll(previousMurders);

        //remove the last murderer if they are still in the available suspects list and to ensure that last murderer is not repeated
        if(lastMurderer !=null&& avaliableSuspects.contains(lastMurderer)){
            avaliableSuspects.remove(lastMurderer);
        }

        //pick a new murderer from remaining suspects
        Suspect murderer = avaliableSuspects.get(random.nextInt(avaliableSuspects.size()));

        //mark as guilty
        murderer.setGuilty(true);

        //store this murderer in the history
        previousMurders.add(murderer);
        lastMurderer = murderer;
        return murderer;
    }
    public List<Suspect> getSuspects(){
        return new ArrayList<>(suspects); // return a copy to prevent external modification
    }
}
