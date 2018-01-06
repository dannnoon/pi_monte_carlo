package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import sample.algorithm.MonteCarloPoint;
import sample.algorithm.PiMonteCarloCalculator;

import java.util.Arrays;

public class Controller {

    @FXML
    private Canvas canvas;
    @FXML
    private TextField samplesCountField;
    @FXML
    private VBox resultContainer;
    @FXML
    private Text resultField;
    @FXML
    private Button calculateButton;
    @FXML
    private ProgressBar progress;
    @FXML
    private CheckBox showPreviewCheckbox;

    @FXML
    public void initialize() {
        initializeCleanCanvas();
        initializeSamplesField();
        initializeCalculateButton();
    }

    private void initializeSamplesField() {
        samplesCountField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int samples = Integer.parseInt(newValue);
                if (samples > 0) {
                    calculateButton.setDisable(false);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                calculateButton.setDisable(true);
            }
        });
    }

    private void initializeCalculateButton() {
        calculateButton.setOnMouseClicked(event -> {
            initializeCleanCanvas();
            countPiNumber(getSamples());
        });
    }

    private int getSamples() {
        return Integer.parseInt(samplesCountField.getText());
    }

    private void countPiNumber(int samples) {
        preCalculationViewActions();

        new Thread(() -> {
            PiMonteCarloCalculator calculator = new PiMonteCarloCalculator(samples);
            WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            calculator.setPointListener(point -> writePixelPoint(image.getPixelWriter(), point));

            double result = calculator.calculate();
            showResult(result);

            if (showPreviewCheckbox.selectedProperty().get()) {
                drawPreview(image);
            }

            postCalculationViewActions();
        }).start();
    }

    private void preCalculationViewActions() {
        Platform.runLater(() -> {
            calculateButton.setDisable(true);
            progress.setVisible(true);
        });
    }

    private void postCalculationViewActions() {
        Platform.runLater(() -> {
            calculateButton.setDisable(false);
            progress.setVisible(false);
        });
    }

    private void showResult(double result) {
        resultContainer.setVisible(true);
        Platform.runLater(() -> resultField.setText(Double.toString(result)));
    }

    private void drawPreview(Image image) {
        Platform.runLater(() -> {
            canvas.getGraphicsContext2D().drawImage(image, 0, 0);

            drawSquare(canvas.getGraphicsContext2D());
            drawCircle(canvas.getGraphicsContext2D());
        });
    }

    private void writePixelPoint(PixelWriter writer, MonteCarloPoint point) {
        int x = (int) (canvas.getWidth() * point.getPoint2D().getX());
        int y = (int) (canvas.getHeight() * point.getPoint2D().getY());

        Color color = getPointColor(point);
        writer.setColor(x, y, color);
    }

    @NotNull
    private Color getPointColor(MonteCarloPoint point) {
        Color color;
        if (point.isInside()) {
            color = new Color(1, 0, 1, 1);
        } else {
            color = new Color(1, 1, 0, 1);
        }
        return color;
    }

    private void initializeCleanCanvas() {
        GraphicsContext context = canvas.getGraphicsContext2D();

        cleanCanvasBackground(context);
        drawSquare(context);
        drawCircle(context);
    }

    private void cleanCanvasBackground(GraphicsContext context) {
        context.setFill(Paint.valueOf(Colors.WHITE));
        context.fillRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    private void drawSquare(GraphicsContext context) {
        context.setStroke(Paint.valueOf(Colors.BLUE));
        context.strokeRect(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    private void drawCircle(GraphicsContext context) {
        context.setStroke(Paint.valueOf(Colors.GREEN));
        context.strokeOval(0, 0, getCanvasWidth(), getCanvasHeight());
    }

    private int getCanvasWidth() {
        return (int) canvas.getWidth();
    }

    private int getCanvasHeight() {
        return (int) canvas.getHeight();
    }
}
