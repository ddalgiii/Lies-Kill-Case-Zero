package game;

import java.util.Random;

import characters.Suspect;
import characters.SuspectManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.ChatBox;

public class GameLauncher extends Application {

    private String playerName = "Investigator"; // Default name
    private String murderLocation;
    private SuspectManager suspectManager;
    private Suspect murderer;

    private static final String[] MURDER_LOCATIONS = {
            "kitchen", "study", "library", "grand hall", "gardens", "wine cellar", "stables"
    };

    public GameLauncher() {
        Random random = new Random();
        this.murderLocation = MURDER_LOCATIONS[random.nextInt(MURDER_LOCATIONS.length)];
        this.suspectManager = new SuspectManager();
        this.suspectManager.assignMurderer();
        for (Suspect suspect : this.suspectManager.getSuspects()) {
            if (suspect.isMurderer()) {
                this.murderer = suspect;
                break;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lies Kill: Case Zero");

        // Input field for player's name
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name");

        // Prologue text area
        TextArea prologueText = new TextArea();
        prologueText.setEditable(false);
        prologueText.setWrapText(true);
        prologueText.setText(generatePrologue());

        // Continue button
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            String enteredName = nameInput.getText().trim();
            if (!enteredName.isEmpty()) {
                playerName = enteredName;
            }
            switchToChatScene(primaryStage);
        });
        continueButton.setDisable(true); // Disable until name is entered (optional)

        nameInput.setOnAction(e -> { // Enable button once name is entered (optional)
            if (!nameInput.getText().trim().isEmpty()) {
                continueButton.setDisable(false);
            }
        });

        VBox layout = new VBox(10, new Label("Enter Your Name:"), nameInput, prologueText, continueButton);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 600, 450);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String generatePrologue() {
        return String.format("""
            You have been called to the grand manor of De Valimont to investigate the shocking murder of the Duke.
            He was found lifeless in the %s last night. The circumstances surrounding his death are suspicious,
            and it's your task to uncover the truth and bring the killer to justice. The suspects present in the manor are:
            - Lady Margaret de Valimont, the Duke's daughter
            - Mr. Daryus Denham, the Chamberlain
            - Miss Anna Joe, the Head Maid
            - Scottie Joe, the Stable Boy
            - Loenna Kammerzell, the Maid
            Speak to them wisely, for lies may kill.
            """, murderLocation);
    }

    private void switchToChatScene(Stage primaryStage) {
        Platform.runLater(() -> {
            ChatBox chatBox = new ChatBox();
            Scene chatScene = chatBox.getChatScene();
            primaryStage.setScene(chatScene);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
