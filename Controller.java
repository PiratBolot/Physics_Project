package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Random;

public class Controller {

    static int ballSize = 30;
    static double massFirstSet = 2;
    static double massSecondSet = 32;
    static double temperature = 273.15;
    static int sizeFirstSet = 5;
    static int sizeSecondSet = 5;

    @FXML
    TextField FirstSet_Size;

    @FXML
    TextField SecondSet_Size;

    @FXML
    TextField WeightFirst_Field;

    @FXML
    TextField WeightSecond_Field;

    @FXML
    TextField TemperatureField;

    @FXML
    TextField Size_Field;

    @FXML
    Button Add_Button;

    @FXML
    Button Save_Button;

    @FXML
    Button FirstSet_Button;

    @FXML
    Button SecondSet_Button;

    @FXML
    Button Set1_Generator;

    @FXML
    Button Set2_Generator;

    @FXML
    Label Warning;

    class LessThanZero extends Exception {
        @Override
        public String getMessage() {
            return "Значение должно быть больше 0";
        }
    }

    @FXML
    private void reverseVector(ActionEvent event) {
        Main.gameLoop.stop();
        for (Model ball : AdditionWindow.balls) {
            ball.movement.x *= -1;
            ball.movement.y *= -1;
        }
        Main.gameLoop.play();
    }

    @FXML
    private void start(ActionEvent event) {
        Main.deleteBox.setDisable(true);
        Set1_Generator.setDisable(true);
        Set2_Generator.setDisable(true);
        Save_Button.setDisable(true);
        FirstSet_Button.setDisable(true);
        SecondSet_Button.setDisable(true);
        Add_Button.setDisable(true);
        Main.gameLoop.play();
    }

    @FXML
    private void stop(ActionEvent event) {
        ObservableList<Model> models = FXCollections.observableArrayList(AdditionWindow.balls);
        Main.deleteBox.getItems().clear();
        Main.deleteBox.setItems(models);
        Main.deleteBox.setDisable(false);
        Set1_Generator.setDisable(false);
        Set2_Generator.setDisable(false);
        FirstSet_Button.setDisable(false);
        SecondSet_Button.setDisable(false);
        Save_Button.setDisable(false);
        Add_Button.setDisable(false);
        Main.gameLoop.stop();
    }

    @FXML
    private void clear(ActionEvent event) {
        Warning.setText("");
        stop(event);
        Main.deleteBox.getItems().clear();
        AdditionWindow.balls.clear();
        Main.getCanvas().getGraphicsContext2D().clearRect(Main.MARGIN + 1, Main.MARGIN + 1, Main.WIDTH - Main.MARGIN - 2, Main.HEIGHT - Main.MARGIN - 2);
    }

    @FXML
    private void addVertex(ActionEvent event) {
        AdditionWindow.newWindow("Новая частица");
    }

    @FXML
    private void saveSize(ActionEvent event) {
        try {
            if (!Size_Field.getText().isEmpty()) {
                ballSize = Integer.parseInt(Size_Field.getText());
                if (ballSize <= 0) {
                    throw new LessThanZero();
                }
                Size_Field.clear();
                Size_Field.setPromptText(Integer.toString(ballSize));
                Warning.setTextFill(Color.GREEN);
                Warning.setText("Принято");
            }
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

    @FXML
    private void saveTemperature(ActionEvent event) {
        try {
            if (!TemperatureField.getText().isEmpty()) {
                double temp = Double.parseDouble(TemperatureField.getText());
                if (temp < 0) {
                    throw new LessThanZero();
                }
                double k = Math.sqrt(temp / temperature);
                for (Model model : AdditionWindow.balls) {
                    model.changeSpeed(k);
                }
                temperature = Double.parseDouble(TemperatureField.getText());
                TemperatureField.clear();
                TemperatureField.setPromptText(Double.toString(temperature));
                Warning.setTextFill(Color.GREEN);
                Warning.setText("Принято");
            }
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

    @FXML
    private void saveWeightFirstSet(ActionEvent event) {
        try {
            if (!WeightFirst_Field.getText().isEmpty()) {
                double temp = Double.parseDouble(WeightFirst_Field.getText());
                if (temp <= 0) {
                    throw new LessThanZero();
                }
                massFirstSet = temp;
                WeightFirst_Field.clear();
                WeightFirst_Field.setPromptText(Double.toString(massFirstSet));
                Warning.setTextFill(Color.GREEN);
                Warning.setText("Принято");
            }
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

    @FXML
    private void saveWeightSecondSet(ActionEvent event) {
        try {
            if (!WeightSecond_Field.getText().isEmpty()) {
                double temp = Double.parseDouble(WeightSecond_Field.getText());
                if (temp <= 0) {
                    throw new LessThanZero();
                }
                massSecondSet = temp;
                WeightSecond_Field.clear();
                WeightSecond_Field.setPromptText(Double.toString(massSecondSet));
                Warning.setTextFill(Color.GREEN);
                Warning.setText("Принято");
            }
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

    @FXML
    private void generateSet1(ActionEvent event) {
        try {
            if (!FirstSet_Size.getText().isEmpty()) {
                int temp = Integer.parseInt(FirstSet_Size.getText());
                if (temp < 0) {
                    throw new LessThanZero();
                }
                sizeFirstSet = temp;
                FirstSet_Size.clear();
                FirstSet_Size.setPromptText(Integer.toString(sizeFirstSet));
            }
            int k = 0;
            Random rand = new Random();
            double mostProbablySpeed = Math.sqrt(2 * 8.31 * temperature * 1000 / massFirstSet);
            double averageSpeed = Math.sqrt(8 * 8.31 * temperature * 1000 / (massFirstSet * 3.14));
            double difference = averageSpeed - mostProbablySpeed;
            while (k != sizeFirstSet) {
                int x = rand.nextInt(Main.WIDTH);
                int y = rand.nextInt(Main.HEIGHT);
                if (AdditionWindow.isIntersectsWall(x, y) && AdditionWindow.isIntersectsBall(x, y)) {
                    k++;
                    double speed = rand.nextInt(2 * (int)Math.round(difference) + 1) + mostProbablySpeed - difference;
                    double moveX = rand.nextInt((int) speed);
                    double moveY = Math.sqrt(Math.pow(speed, 2) - Math.pow(moveX, 2)) * Math.pow(-1, k);
                    AdditionWindow.balls.add(new Model(x, y, new Vec2d(moveX / 200, moveY / 200), Main.ball, massFirstSet));
                }
            }
            ObservableList<Model> models = FXCollections.observableArrayList(AdditionWindow.balls);
            Main.deleteBox.getItems().clear();
            Main.deleteBox.setItems(models);
            Main.getCanvas().getGraphicsContext2D().clearRect(Main.MARGIN + 1, Main.MARGIN + 1, Main.WIDTH - Main.MARGIN - 2, Main.HEIGHT - Main.MARGIN - 2);
            for (Model model : AdditionWindow.balls) {
                Main.getCanvas().getGraphicsContext2D().drawImage(model.img, model.center.getX() - Controller.ballSize / 2 + Main.MARGIN, model.center.getY() - Controller.ballSize / 2 + Main.MARGIN, Controller.ballSize, Controller.ballSize);
            }
            Warning.setTextFill(Color.GREEN);
            Warning.setText("Принято");
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

    @FXML
    private void generateSet2(ActionEvent event) {
        try {
            if (!SecondSet_Size.getText().isEmpty()) {
                int temp = Integer.parseInt(SecondSet_Size.getText());
                if (temp < 0) {
                    throw new LessThanZero();
                }
                sizeSecondSet = temp;
                SecondSet_Size.clear();
                SecondSet_Size.setPromptText(Integer.toString(sizeSecondSet));
            }
            int k = 0;
            int i = 0; // Если долгое время не удается найти подходящее место, то уменьшаем все молекулы
            Random rand = new Random();
            double mostProbablySpeed = Math.sqrt(2 * 8.31 * temperature * 1000 / massSecondSet);
            double averageSpeed = Math.sqrt(8 * 8.31 * temperature * 1000 / (massSecondSet * 3.14));
            double difference = averageSpeed - mostProbablySpeed;
            while (k != sizeSecondSet) {
                int x = rand.nextInt(Main.WIDTH);
                int y = rand.nextInt(Main.HEIGHT);
                if (AdditionWindow.isIntersectsWall(x, y) && AdditionWindow.isIntersectsBall(x, y)) {
                    k++;
                    double speed = rand.nextInt(2 * (int) Math.round(difference) + 1) + mostProbablySpeed - difference;
                    double moveX = rand.nextInt((int) speed);
                    double moveY = Math.sqrt(Math.pow(speed, 2) - Math.pow(moveX, 2)) * Math.pow(-1, k);
                    AdditionWindow.balls.add(new Model(x, y, new Vec2d(moveX / 200, moveY / 200), Main.ball2, massSecondSet));
                }
            }
            ObservableList<Model> models = FXCollections.observableArrayList(AdditionWindow.balls);
            Main.deleteBox.getItems().clear();
            Main.deleteBox.setItems(models);
            Main.getCanvas().getGraphicsContext2D().clearRect(Main.MARGIN + 1, Main.MARGIN + 1, Main.WIDTH - Main.MARGIN - 2, Main.HEIGHT - Main.MARGIN - 2);
            for (Model model : AdditionWindow.balls) {
                Main.getCanvas().getGraphicsContext2D().drawImage(model.img, model.center.getX() - Controller.ballSize / 2 + Main.MARGIN, model.center.getY() - Controller.ballSize / 2 + Main.MARGIN, Controller.ballSize, Controller.ballSize);
            }
            Warning.setTextFill(Color.GREEN);
            Warning.setText("Принято");
        }
        catch (NumberFormatException e) {
            Warning.setTextFill(Color.RED);
            Warning.setText("Неккоректный ввод");
        }
        catch (LessThanZero zero) {
            Warning.setTextFill(Color.RED);
            Warning.setText(zero.getMessage());
        }
    }

}