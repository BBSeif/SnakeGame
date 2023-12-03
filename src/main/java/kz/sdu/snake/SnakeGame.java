package kz.sdu.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kz.sdu.snake.extra.ConfirmBox;

import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 30;

    private int  speed = 5;

    private Color fColor = fruitColor();

    private LinkedList<Point> snake;
    private Point fruit;
    private Direction direction;


    /**
     * We use Enum to ensure that we have only four direction command
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */

    @Override
    public void start(Stage primaryStage) {


        snake = new LinkedList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        fruit = createFruit();
        direction = Direction.RIGHT;

        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));

        /**
         * Control of animation and speed
         */
        new AnimationTimer() {
            long lastUpdate = 2;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1000_000_000 / speed) {
                    update();
                    render(gc);
                    lastUpdate = now;
                }
            }
        }.start();

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Controll center of Snake
     * it can be controlled by arrow and by W A S D keys!
     *
     */
    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP:
            case W:
                direction = Direction.UP;
                break;
            case DOWN:
            case S:
                direction = Direction.DOWN;
                break;
            case LEFT:
            case A:
                direction = Direction.LEFT;
                break;
            case RIGHT:
            case D:
                direction = Direction.RIGHT;
                break;
        }
    }

    /**
     *  Update method works for movement of snake constantly
     */

    private void update() {
        Point head = snake.getFirst();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

//        to stop program when snake hits the wall!
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            // Game over condition
            Button button = new Button("Game over !" );
            button.setOnAction(event -> {
            boolean result = ConfirmBox.display("Title","Are you sure ?");
            System.out.println(result);
            });
            snake.clear();
            snake.add(new Point(WIDTH / 2, HEIGHT / 2));
            fruit = createFruit();
            direction = Direction.RIGHT;
            speed = 5;
            return;
        }

        snake.addFirst(newHead);

        if (newHead.x == fruit.x && newHead.y == fruit.y) {
            // Snake ate the fruit
            fruit = createFruit();
            fColor = fruitColor();
            speed += 5;
        } else {
            // Remove the tail
            snake.removeLast();
        }
    }

    /**
     * Creating fruit on random places
     */
    private Point createFruit() {
        int x, y;
        x = (int) (Math.random() * WIDTH);
        y = (int) (Math.random() * HEIGHT);
        return new Point(x, y);
    }


    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Draw snake
        gc.setFill(Color.BLACK);
        for (Point point : snake) {
            gc.fillRect(point.x * TILE_SIZE , point.y * TILE_SIZE , TILE_SIZE, TILE_SIZE);
        }

        // Draw fruit
        gc.setFill(fColor);
        gc.fillRect(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE,TILE_SIZE, TILE_SIZE);
    }

    /**
     *  To get random color for fruit after each eating
     */
    private Color fruitColor(){
        Random random = new Random();

        // Generate random values for the RGB components
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();

        // Create and return a Color object
        return new Color(red, green, blue, 1.0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}


