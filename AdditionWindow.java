package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AdditionWindow {

    static ArrayList<Model> balls = new ArrayList<>();

    public static TextField coordX = new TextField();
    public static TextField coordY = new TextField();
    public static TextField vectorX = new TextField();
    public static TextField vectorY = new TextField();

    public static boolean isIntersectsWall(double x, double y) {
        if (x >= Controller.ballSize / 2 && x + Main.MARGIN <= Main.WIDTH - Controller.ballSize / 2
                && y >= Controller.ballSize / 2 && y + Main.MARGIN <= Main.HEIGHT - Controller.ballSize / 2) {
            return true;
        }
        return false;
    }

    public static boolean isIntersectsBall(double x, double y) {
        for (Model model : balls) {
            if ((Math.pow(model.center.getX() - x, 2) + Math.pow(model.center.getY() - y, 2)) < Math.pow(Controller.ballSize, 2)) {
                return false;
            }
        }
        return true;
    }

    public static void newWindow(String title) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);


        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(5);
        pane.setVgap(5);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(pane);

        Button closeWindow = new Button("Отмена");
        closeWindow.setPrefSize(80, 30);
        Button add = new Button("Добавить");
        add.setPrefSize(80, 30);
        Button clear = new Button("Очистить");
        clear.setPrefSize(80, 30);

        coordX.setPromptText("Координата X:");
        GridPane.setConstraints(coordX, 0, 0);
        pane.getChildren().add(coordX);

        coordY.setPromptText("Координата Y:");
        GridPane.setConstraints(coordY, 0, 1);
        pane.getChildren().add(coordY);

        vectorX.setPromptText("Горизонтальная составляющая вектора скорости:");
        GridPane.setConstraints(vectorX, 0, 2);
        pane.getChildren().add(vectorX);

        Label comment = new Label();
        GridPane.setConstraints(comment, 0, 3);
        pane.getChildren().add(comment);

        vectorY.setPromptText("Вертикальная составляющая вектора скорости:");
        GridPane.setConstraints(vectorY, 0, 3);
        pane.getChildren().add(vectorY);

        GridPane.setConstraints(closeWindow, 1, 0);
        GridPane.setConstraints(add, 1, 1);
        GridPane.setConstraints(clear, 1, 2);

        Label warning = new Label();
        warning.setTextFill(Color.RED);
        GridPane.setConstraints(warning, 0, 4);
        pane.getChildren().add(warning);

        closeWindow.setOnAction(event -> window.close());
        add.setOnAction(event -> {
            if (!coordX.getText().isEmpty() && !coordY.getText().isEmpty() && !vectorX.getText().isEmpty() && !vectorY.getText().isEmpty()) {
                if (isIntersectsWall(Double.parseDouble(coordX.getText()), Double.parseDouble(coordY.getText())) && isIntersectsBall(Double.parseDouble(coordX.getText()), Double.parseDouble(coordY.getText()))) {
                    Vec2d vec2d = new Vec2d(Double.parseDouble(vectorX.getText()), Double.parseDouble(vectorY.getText()));
                    balls.add(new Model(Double.parseDouble(coordX.getText()), Double.parseDouble(coordY.getText()), vec2d, Main.ball, Controller.massFirstSet));
                    Main.getCanvas().getGraphicsContext2D().drawImage(Main.getBall(), Double.parseDouble(coordX.getText()) - Controller.ballSize / 2 + Main.MARGIN, Double.parseDouble(coordY.getText()) - Controller.ballSize / 2 + Main.MARGIN, Controller.ballSize, Controller.ballSize);
                    Main.deleteBox.getItems().add(balls.get(balls.size() - 1));
                    window.close();
                } else {
                    warning.setText("Нельзя добавить частицу в эту область!!!");
                }
            } else {
                warning.setText("Должны быть заполнены все поля!!!");
            }
        });
        clear.setOnAction(event -> {
            coordX.clear();
            coordY.clear();
            vectorX.clear();
            vectorY.clear();
        });

        pane.getChildren().addAll(closeWindow, add, clear);
        Scene scene = new Scene(anchorPane, 400, 300);
        window.setScene(scene);
        window.setTitle(title);
        window.showAndWait();
    }
}
