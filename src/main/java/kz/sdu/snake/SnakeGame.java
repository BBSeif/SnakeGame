package kz.sdu.snake;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//
//public class HelloApplication extends Application {
//    Stage window;
//    Button button;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        window = stage;
//
//        Label label = new Label("Welcome to the First scene !");
//        button = new Button("Start Game!" );
//        button.setOnAction(event -> {
//            boolean result = ConfirmBox.display("Title","Are you sure ?");
//            System.out.println(result);
//        });
////        button1.setOnAction(e -> window.setScene(scene2));
//
//        StackPane layout1 = new StackPane();
//        layout1.getChildren().addAll(label, button);
//        Scene scene = new Scene(layout1, 200, 300);
//
//
//        window.setScene(scene);
//        window.setTitle("Snake Game!");
//        window.show();
//    }

//}



import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;

public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    private LinkedList<Point> snake;
    private Point fruit;
    private Direction direction;

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

        new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1000000000 / 10) {
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

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP:
                direction = Direction.UP;
                break;
            case DOWN:
                direction = Direction.DOWN;
                break;
            case LEFT:
                direction = Direction.LEFT;
                break;
            case RIGHT:
                direction = Direction.RIGHT;
                break;
        }
    }

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

        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            // Game over condition
            snake.clear();
            snake.add(new Point(WIDTH / 2, HEIGHT / 2));
            fruit = createFruit();
            direction = Direction.RIGHT;
            return;
        }

        snake.addFirst(newHead);

        if (newHead.x == fruit.x && newHead.y == fruit.y) {
            // Snake ate the fruit
            fruit = createFruit();
        } else {
            // Remove the tail
            snake.removeLast();
        }
    }

    private Point createFruit() {
        int x, y;
        do {
            x = (int) (Math.random() * WIDTH);
            y = (int) (Math.random() * HEIGHT);
        } while (snake.contains(new Point(x, y)));

        return new Point(x, y);
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // Draw snake
        gc.setFill(Color.GREEN);
        for (Point point : snake) {
            gc.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw fruit
        gc.setFill(Color.RED);
        gc.fillRect(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


