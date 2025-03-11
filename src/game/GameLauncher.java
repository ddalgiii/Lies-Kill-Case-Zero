package game;

import java.util.List;

import ai.AIDialogueGenerator;
import characters.Suspect;
import characters.SuspectManager;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.GameUI; // Ensure GameUI is properly imported

// Main JavaFX class
public class GameLauncher extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        GameUI gameUI = new GameUI(); // Initialize UI
        gameUI.start(primaryStage);

        // Generate salutations for each hardcoded suspect
        SuspectManager suspectManager = new SuspectManager(); // Ensure an instance of SuspectManager is created
        List<Suspect> suspects = suspectManager.getSuspects(); // Retrieve the list properly
        
        for (Suspect suspect : suspects) {
            String salutation = AIDialogueGenerator.generateText(suspect.getName() + " is " + suspect.getPersonality() + " and plays the role of " + suspect.getRole() + ". Their motivation is " + suspect.getMotive() + ". Generate a fitting greeting based on this.");
            System.out.println(salutation); // Use the salutation variable
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
