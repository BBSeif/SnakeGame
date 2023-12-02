module kz.sdu.snake {
    requires javafx.controls;
    requires javafx.fxml;


    opens kz.sdu.snake to javafx.fxml;
    exports kz.sdu.snake;
}