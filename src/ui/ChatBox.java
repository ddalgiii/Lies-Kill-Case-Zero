package ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import ai.AIDialogueGenerator;
import characters.Suspect;
import characters.SuspectManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChatBox {
    private final BorderPane mainLayout;
    private final Scene chatScene;
    private final TextArea chatArea;
    private final TextField inputField;
    private final ListView<String> npcList;
    private String selectedNPC = "Unknown";
    private final SuspectManager suspectManager;
    private final String playerName;
    private final Suspect murderer;
    private final String murderLocation;
    private final String bodyDiscoveredBy;
    private final String murderMethod;
    private boolean introductionsComplete = false;
    private final Map<String, String> suspectNotes = new HashMap<>();
    private Button notepadButton;
    private Button accuseButton; // Added accuse button

    private final Map<String, String> unusualDemeanors = new HashMap<>();

    public ChatBox(SuspectManager suspectManager, String playerName, Suspect murderer, String murderLocation, String murderMethod, String bodyDiscoveredBy) {
        this.suspectManager = suspectManager;
        this.playerName = playerName;
        this.murderer = murderer;
        this.murderLocation = murderLocation;
        this.murderMethod = murderMethod;
        this.bodyDiscoveredBy = bodyDiscoveredBy;
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Ask a question...");
        inputField.setOnAction(e -> processInput());
        inputField.setDisable(true); // Disable input until introductions are done

        npcList = new ListView<>();
        npcList.getItems().addAll(suspectManager.getSuspects().stream()
                .map(Suspect::getName)
                .collect(Collectors.toList()));
        npcList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedNPC = newVal;
                inputField.setDisable(false); // Enable input when an NPC is selected
            } else {
                inputField.setDisable(true);
            }
        });
        npcList.setPrefWidth(150);

        Label npcLabel = new Label("Suspects:");
        VBox npcMenu = new VBox(10, npcLabel, npcList);
        npcMenu.setAlignment(Pos.TOP_LEFT);
        npcMenu.setPrefWidth(180);
        npcMenu.setPadding(new Insets(10));

        chatArea.setPrefHeight(400);
        VBox chatInputArea = new VBox(10, chatArea, inputField);
        chatInputArea.setPadding(new Insets(10));

        notepadButton = new Button("Notepad");
        notepadButton.setOnAction(e -> showNotepad());

        accuseButton = new Button("Accuse");
        accuseButton.setOnAction(e -> {
            VerdictScreen verdictScreen = new VerdictScreen(suspectManager, murderer, murderMethod, murderLocation, bodyDiscoveredBy);
            verdictScreen.setOnGameEnd(() -> {
                // Handle what happens after the verdict screen is closed, e.g., exit the application
                Platform.exit();
            });
            verdictScreen.showVerdictScreen();
        });

        HBox controls = new HBox(10, notepadButton, accuseButton);
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setPadding(new Insets(10));

        mainLayout = new BorderPane();
        mainLayout.setLeft(npcMenu);
        mainLayout.setCenter(chatInputArea);
        mainLayout.setBottom(controls);

        chatScene = new Scene(mainLayout, 800, 550);

        startIntroductions();
    }

    private void startIntroductions() {
        Suspect daryus = suspectManager.getSuspectByName("Daryus Denham");
        Suspect margaret = suspectManager.getSuspectByName("Lady Margaret de Valimont");
        Suspect anna = suspectManager.getSuspectByName("Anna Joe");
        Suspect loenna = suspectManager.getSuspectByName("Loenna Kammerzell");
        Suspect scottie = suspectManager.getSuspectByName("Scottie Joe");
        Random random = new Random();

        // Determine unusual demeanors (excluding Loenna and Daryus)
        for (Suspect suspect : suspectManager.getSuspects()) {
            if (!suspect.getName().equals("Loenna Kammerzell") && !suspect.getName().equals("Daryus Denham")) {
                String[] demeanors = {"acting overly friendly", "seeming unusually nervous", "avoiding eye contact", "being surprisingly cheerful", "appearing quite agitated"};
                if (!suspect.equals(murderer) || random.nextDouble() < 0.7) { // Murderer has a higher chance
                    unusualDemeanors.put(suspect.getName(), demeanors[random.nextInt(demeanors.length)]);
                }
            }
        }

        appendMessage(String.format("Daryus Denham: Welcome, %s. I am Daryus Denham, the Chamberlain of the late Duke.", playerName));
        appendMessage(String.format("Daryus Denham leads you to Lady Margaret de Valimont's study."));
        appendMessage(String.format("Lady Margaret de Valimont: You must be the investigator. Thank you for coming. With us are Anna Joe, the head maid, and Loenna Kammerzell, a maid of the household."));
        appendMessage(String.format("Anna Joe (%s): A pleasure to meet you, Investigator %s.", unusualDemeanors.getOrDefault("Anna Joe", ""), playerName));
        appendMessage(String.format("Loenna Kammerzell: ... (nods silently in greeting)"));
        appendMessage(String.format("Daryus Denham: Now, let's head to the stables."));
        appendMessage(String.format("You arrive at the stables."));
        appendMessage(String.format("Daryus Denham: This is our stable boy, Scottie Joe."));
        appendMessage(String.format("Scottie Joe (%s): G'day, Investigator. Hope you can sort this mess out.", unusualDemeanors.getOrDefault("Scottie Joe", "acting casually"), playerName));
        appendMessage(String.format("You are now back with Lady Margaret and can begin your questioning. Select a suspect from the list."));

        introductionsComplete = true;
        inputField.setDisable(false); // Enable input after introductions
    }

    private void processInput() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty() && !selectedNPC.equals("Unknown") && introductionsComplete) {
            appendMessage("You to " + selectedNPC + ": " + userInput);

            new Thread(() -> {
                String response;
                try {
                    Suspect suspect = suspectManager.getSuspectByName(selectedNPC);
                    if (suspect != null) {
                        String prompt = buildNpcPrompt(suspect, userInput);
                        response = AIDialogueGenerator.generateText(prompt);

                        if (suspect.equals(murderer)) {
                            response = processMurdererResponse(response);
                        }
                        // Store the NPC's response for potential later analysis
                        suspectNotes.put(selectedNPC, suspectNotes.getOrDefault(selectedNPC, "") + "\nYou: " + userInput + "\n" + selectedNPC + ": " + response);

                    } else {
                        response = "Error: Suspect not found in the game.";
                    }
                } catch (Exception e) {
                    response = "I don't understand the question right now.";
                }

                String finalResponse = selectedNPC + ": " + response;
                Platform.runLater(() -> appendMessage(finalResponse));
            }).start();

            inputField.clear();
        } else if (!introductionsComplete) {
            appendMessage("Please wait until the introductions are complete before questioning.");
        } else if (selectedNPC.equals("Unknown")) {
            appendMessage("Please select a suspect from the list.");
        }
    }

    private String buildNpcPrompt(Suspect suspect, String question) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are ").append(suspect.getName()).append(".\n");
        promptBuilder.append("You are a ").append(suspect.getSocialStanding()).append(" with the title of ").append(suspect.getTitle()).append(".\n");
        promptBuilder.append("Your core personality traits are: ").append(suspect.getPersonality()).append(".\n");
        promptBuilder.append("Your backstory is: ").append(suspect.getBackstory()).append(".\n");
        promptBuilder.append("Your current motivations and goals include: ").append(suspect.getMotivations()).append(".\n");
        promptBuilder.append("The Duke was murdered last night in the ").append(murderLocation).append(".\n");
        promptBuilder.append("You are currently being questioned by Investigator ").append(playerName).append(" about the murder.\n");
        promptBuilder.append("Remember to speak and react in a way that is consistent with your personality, background, and motivations.\n");
        if (unusualDemeanors.containsKey(suspect.getName())) {
            promptBuilder.append("You are currently ").append(unusualDemeanors.get(suspect.getName())).append(".\n");
        }
        promptBuilder.append("Investigator ").append(playerName).append(": ").append(question).append("\n");
        promptBuilder.append(suspect.getName()).append(": ");
        return promptBuilder.toString();
    }

    private String processMurdererResponse(String rawResponse) {
        Random random = new Random();
        if (random.nextDouble() < 0.7) {
            String[] evasiveResponses = {"I'm not sure I remember clearly.", "It was all so chaotic.", "I'd rather not talk about it.", "Can you ask me something else?"};
            return evasiveResponses[random.nextInt(evasiveResponses.length)];
        } else {
            return rawResponse;
        }
    }

    private void appendMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public Scene getChatScene() {
        return chatScene;
    }

    private void showNotepad() {
        if (selectedNPC.equals("Unknown")) {
            appendMessage("Please select a suspect to take notes on.");
            return;
        }

        Stage notepadStage = new Stage();
        notepadStage.initModality(Modality.APPLICATION_MODAL);
        notepadStage.setTitle("Notes on " + selectedNPC);

        TextArea notesArea = new TextArea(suspectNotes.getOrDefault(selectedNPC, ""));
        Button saveButton = new Button("Save Notes");
        saveButton.setOnAction(event -> {
            suspectNotes.put(selectedNPC, notesArea.getText());
            notepadStage.close();
        });

        VBox notepadLayout = new VBox(10, notesArea, saveButton);
        notepadLayout.setPadding(new Insets(10));
        Scene notepadScene = new Scene(notepadLayout, 400, 300);
        notepadStage.setScene(notepadScene);
        notepadStage.showAndWait();
    }
}
