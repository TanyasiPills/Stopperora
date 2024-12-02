package com.example.stopperora;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    public Button top;
    public Button bot;
    public ListView<String> listView;


    @FXML
    private Label time;

    private LocalDateTime now;

    private boolean stopped = true;
    private Duration timeAlready = Duration.ZERO;
    private Duration duration;

    private Timer timer;

    @FXML
    public void initialize() {
        listView.getItems().clear();
        duration = Duration.ZERO;
    }


    @FXML
    protected void onStart() {
        stopped = !stopped;

        if (stopped) {
            if (timer != null) {
                timer.cancel();
            }
            timeAlready = duration != null ? duration.plus(timeAlready) : timeAlready;
            top.setText("Start");
            bot.setText("Reset");
        } else {
            now = LocalDateTime.now();
            top.setText("Stop");
            bot.setText("Részidő");

            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    LocalDateTime notNow = LocalDateTime.now();
                    duration = Duration.between(now, notNow);

                    long millis = duration.toMillis() + timeAlready.toMillis();
                    Platform.runLater(() -> time.setText(String.format("%02d:%02d.%03d", millis / (60 * 1000), (millis / 1000) % 60, millis % 1000)));
                }
            }, 0, 10);
        }
    }

    public void onStop() {
        if(stopped){
            timeAlready = Duration.ZERO;
            time.setText("00:00.000");
            listView.setItems(null);
        }
        else{
            long millis = duration.toMillis() + timeAlready.toMillis();
            listView.getItems().add(String.format("%02d:%02d.%03d", millis / (60 * 1000), (millis / 1000) % 60, millis % 1000));
        }
    }
}