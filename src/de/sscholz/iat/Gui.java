package de.sscholz.iat;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static java.lang.System.out;

public class Gui extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hello World!");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Button btna = new Button();
		btna.setText("Say 'Hello World'");
		btna.setOnAction(event -> {
			out.println("Hello World!");
			out.println(event.getEventType().getName());
		});
		Button btnb = new Button();
		btnb.setText("bll");
		btna.setOnAction(event -> btnb.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;"));

		grid.add(btna, 1, 1);
		grid.add(btnb, 1, 2);

		primaryStage.setScene(new Scene(grid, 300, 250));
		primaryStage.show();
	}
}
