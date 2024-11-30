package com.example.stopperora;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    public Button top;
    public Button bot;
    public ListView listView;

    private List<String> items;

    @FXML
    private Label time;

    private LocalDateTime now;

    private boolean stopped = true;
    private boolean reset = true;

    private Duration timeAlready = Duration.ZERO;
    private Duration duration;

    @FXML
    public void initialize() {
        items = new ArrayList<String>();
        listView.getItems().clear();
    }


    @FXML
    protected void onStart(ActionEvent actionEvent) {
        stopped = !stopped;

        if(stopped) {
            timeAlready = duration.plus(timeAlready);
            top.setText("Start");
            bot.setText("Reset");
        }
        else{
            top.setText("Stop");
            bot.setText("Részidő");
        }

        new Thread(() -> {
            now = LocalDateTime.now();
            while (!stopped) {
                LocalDateTime realnow = LocalDateTime.now();
                duration = Duration.between(now, realnow);

                long millis = duration.toMillis() + timeAlready.toMillis();
                Platform.runLater(() -> time.setText(String.format("%02d:%02d.%03d", millis / (60 * 1000), (millis / 1000) % 60, millis % 1000)));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void onStop(ActionEvent actionEvent) {
        if(stopped){
            timeAlready = Duration.ZERO;
            time.setText("00:00.000");
        }
        else{
            long millis = duration.toMillis() + timeAlready.toMillis();
            listView.getItems().add(String.format("%02d:%02d.%03d", millis / (60 * 1000), (millis / 1000) % 60, millis % 1000));
        }
    }
}