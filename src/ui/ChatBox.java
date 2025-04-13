package ui;

import java.util.stream.Collectors;

import ai.AIDialogueGenerator;
import characters.Suspect;
import characters.SuspectManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private boolean introductionsComplete = false;
    private final Map<String, String> suspectNotes = new HashMap<>();
    private Button notepadButton;

    private final Map<String, String> unusualDemeanors = new HashMap<>();

    public ChatBox(SuspectManager suspectManager, String playerName, Suspect murderer, String murderLocation) {
        this.suspectManager = suspectManager;
        this.playerName = playerName;
        this.murderer = murderer;
        this.murderLocation = murderLocation;
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
        HBox controls = new HBox(10, notepadButton);
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setPadding(new Insets(10));

        mainLayout = new BorderPane();
        mainLayout.setLeft(npcMenu);
        mainLayout.setCenter(chatInputArea);
        mainLayout.setBottom(controls);

        chatScene = new Scene(mainLayout, 800, 550);
    }

    private void processInput() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty() && !selectedNPC.equals("Unknown") && introductionsComplete) {
            appendMessage("You to " + selectedNPC + ": " + userInput);

            new Thread(() -> {
                String response;
                try {
                    Suspect suspect = suspectManager.suspects(selectedNPC);
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