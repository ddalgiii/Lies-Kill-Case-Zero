package game;

import characters.Suspect;
import characters.SuspectManager;
import java.util.List;

public class GameLauncher {
    public static void main(String[]args){
        System.out.println("\nStarting Lies Kill: Case Zero...\n");

        //intiate suspect manager
        SuspectManager manager = new SuspectManager();

        //run one round
        System.out.println("New Round: ");

        //assign a murderer for this round
        Suspect murderer = manager.assignMurderer();
        List<Suspect> allSuspects = manager.getSuspects();

        //print all suspects and their status
        for(Suspect suspect : allSuspects){
            if(suspect.equals(murderer)){
                System.out.println(suspect.getName()+" Murderer!!");
            } else {
                System.out.println(suspect.getName()+" Innocent");
            }
        }
        System.out.println(); //space for readability

    System.out.println("Murder selection test completed.");
    }
}
