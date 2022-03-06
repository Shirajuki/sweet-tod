package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class GameApp extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent fxmlLocation = FXMLLoader.load(getClass().getResource("App.fxml"));
		Scene scene = new Scene(fxmlLocation);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("MENU");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(final String[] args) {
		GameApp.launch(args);
	}
}