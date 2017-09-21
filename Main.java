package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {

    BorderPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));

    static ChoiceBox deleteBox;

    protected static final int WIDTH = 620;
    protected static final int HEIGHT = 620;
    protected static final int MARGIN = 20;

    static Timeline gameLoop = new Timeline();

    public static Canvas canvas = new Canvas(WIDTH, HEIGHT);
    static Image ball = new Image(Main.class.getResourceAsStream("krug02.png"));
    static Image ball2 = new Image(Main.class.getResourceAsStream("krug01.png"));

    public Main() throws IOException {}

    public static Canvas getCanvas() {
        return canvas;
    }
    public static Image getBall() {
        return ball;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene theScene = new Scene(root);
        theScene.getStylesheets().add(0, "styles/Style.css");
        primaryStage.setScene(theScene);
        root.setCenter(canvas);


        deleteBox = new ChoiceBox();
        deleteBox.setPrefSize(90, 30);
        root.setLeft(deleteBox);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        deleteBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() >= 0) {
                    AdditionWindow.balls.remove(newValue.intValue());
                    ObservableList<Model> models = FXCollections.observableArrayList(AdditionWindow.balls);
                    deleteBox.getItems().clear();
                    deleteBox.setItems(models);
                    gc.clearRect(MARGIN + 1, MARGIN + 1, WIDTH - MARGIN - 2, HEIGHT - MARGIN - 2);
                    for (Model model : AdditionWindow.balls) {
                        gc.drawImage(model.img, model.center.getX() - Controller.ballSize / 2 + MARGIN, model.center.getY() - Controller.ballSize / 2 + MARGIN, Controller.ballSize, Controller.ballSize);
                    }
                }
            }
        });

        gc.strokeLine(5, 5, 5, 100);
        gc.strokeLine(5, 5, 100, 5);
        gc.strokeLine(5, 100, 10, 90);
        gc.strokeLine(5, 100, 0, 90);
        gc.strokeLine(100, 5, 90, 0);
        gc.strokeLine(100, 5, 90, 10);
        gc.strokeLine(MARGIN, MARGIN, MARGIN, HEIGHT);
        gc.strokeLine(MARGIN, MARGIN, WIDTH, MARGIN);
        gc.strokeLine(WIDTH - 1, HEIGHT - 1, WIDTH - 1, MARGIN);
        gc.strokeLine(WIDTH - 1, HEIGHT - 1, MARGIN, HEIGHT - 1);
        gc.strokeText("X,м", 110, 10);
        gc.strokeText("Y,м", 0, 115);

        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.01),                // 60 FPS
                ae -> {
                    Model.moveBalls();

                    for (int i = 0; i < AdditionWindow.balls.size(); i++) {
                        Model.detection(AdditionWindow.balls.get(i));
                        for (int j = i + 1; j < AdditionWindow.balls.size(); j++) {
                            Model.ballToBallDetect(AdditionWindow.balls.get(i), AdditionWindow.balls.get(j));
                        }
                    }
                    gc.clearRect(0, 0, WIDTH - 1, HEIGHT - 1);
                    gc.strokeLine(5, 5, 5, 100);
                    gc.strokeLine(5, 5, 100, 3);
                    gc.strokeLine(5, 100, 6, 90);
                    gc.strokeLine(5, 100, 0, 90);
                    gc.strokeLine(100, 5, 90, 0);
                    gc.strokeLine(100, 5, 90, 6);
                    gc.strokeLine(MARGIN, MARGIN, MARGIN, HEIGHT);
                    gc.strokeLine(MARGIN, MARGIN, WIDTH, MARGIN);
                    gc.strokeLine(WIDTH - 1, HEIGHT - 1, WIDTH - 1, MARGIN);
                    gc.strokeLine(WIDTH - 1, HEIGHT - 1, MARGIN, HEIGHT - 1);
                    gc.strokeText("X,м", 110, 10);
                    gc.strokeText("Y,м", 0, 115);
                    for (Model model : AdditionWindow.balls) {
                        model.center.changeX(model.tempx);
                        model.center.changeY(model.tempy);
                        gc.drawImage(model.img, model.center.getX() - Controller.ballSize / 2 + MARGIN, model.center.getY() - Controller.ballSize / 2 + MARGIN, Controller.ballSize, Controller.ballSize);
                    }
                });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.setAutoReverse(true);

        primaryStage.setTitle("Модель идеального газа");
        primaryStage.show();
    }
}