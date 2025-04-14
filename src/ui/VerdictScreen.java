package ui;

import characters.Suspect;
import characters.SuspectManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VerdictScreen {

    private final Stage verdictStage;
    private final SuspectManager suspectManager;
    private final Suspect actualMurderer;
    private final String murderMethod;
    private final String murderLocation;
    private final String bodyDiscoveredBy;
    private Suspect chosenMurderer;
    private Runnable onGameEnd; // Callback for when the game ends

    public VerdictScreen(SuspectManager suspectManager, Suspect actualMurderer, String murderMethod, String murderLocation, String bodyDiscoveredBy) {
        this.suspectManager = suspectManager;
        this.actualMurderer = actualMurderer;
        this.murderMethod = murderMethod;
        this.murderLocation = murderLocation;
        this.bodyDiscoveredBy = bodyDiscoveredBy;
        this.verdictStage = new Stage();
        verdictStage.initModality(Modality.APPLICATION_MODAL);
        verdictStage.setTitle("Accuse the Murderer");
    }

    public void setOnGameEnd(Runnable onGameEnd) {
        this.onGameEnd = onGameEnd;
    }

    public void showVerdictScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Who do you think is the murderer?");
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        ListView<Suspect> suspectListView = new ListView<>();
        suspectListView.setItems(FXCollections.observableArrayList(suspectManager.getSuspects()));
        suspectListView.setPrefHeight(200);

        // Set the cell factory to display only name, age, and title
        suspectListView.setCellFactory(param -> new ListCell<Suspect>() {
            @Override
            protected void updateItem(Suspect item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (Age: %d, Title: %s)", item.getName(), item.getAge(), item.getTitle()));
                }
            }
        });

        Button accuseButton = new Button("Accuse");
        accuseButton.setOnAction(e -> {
            chosenMurderer = suspectListView.getSelectionModel().getSelectedItem();
            if (chosenMurderer != null) {
                showOutcomeScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Suspect Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a suspect to accuse.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(titleLabel, suspectListView, accuseButton);

        Scene scene = new Scene(layout, 400, 350);
        verdictStage.setScene(scene);
        verdictStage.showAndWait();
    }

    private void showOutcomeScreen() {
        Stage outcomeStage = new Stage();
        outcomeStage.initModality(Modality.APPLICATION_MODAL);
        outcomeStage.setTitle("Verdict Outcome");

        VBox outcomeLayout = new VBox(20);
        outcomeLayout.setAlignment(Pos.CENTER);
        outcomeLayout.setPadding(new Insets(20));

        Label outcomeTitleLabel = new Label("Verdict:");
        outcomeTitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        TextArea summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setWrapText(true);
        summaryArea.setPrefWidth(350);
        summaryArea.setPrefHeight(200);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            outcomeStage.close();
            if (onGameEnd != null) {
                onGameEnd.run();
            }
        });

        if (chosenMurderer == actualMurderer) {
            outcomeTitleLabel.setStyle("-fx-text-fill: green;");
            outcomeTitleLabel.setText("Correct!");
            summaryArea.setText(String.format("You have correctly identified the murderer: %s.\n\nSummary of Events:\n%s %s in the %s. The body was discovered by %s.",
                    actualMurderer.getName(), actualMurderer.getName(), murderMethod, murderLocation, bodyDiscoveredBy));
        } else {
            outcomeTitleLabel.setStyle("-fx-text-fill: red;");
            outcomeTitleLabel.setText("Incorrect!");
            summaryArea.setText(String.format("You accused %s, but the actual murderer was %s.\n\nSummary of Events:\n%s %s in the %s. The body was discovered by %s.",
                    chosenMurderer.getName(), actualMurderer.getName(), actualMurderer.getName(), murderMethod, murderLocation, bodyDiscoveredBy));
        }

        outcomeLayout.getChildren().addAll(outcomeTitleLabel, summaryArea, closeButton);

        Scene outcomeScene = new Scene(outcomeLayout, 400, 350);
        outcomeStage.setScene(outcomeScene);
        outcomeStage.showAndWait();
    }
}
