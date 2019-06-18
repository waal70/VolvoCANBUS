package org.waal70.canbus.application.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VolvoCANBUSFXML extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/volvocanbus.fxml"));
		Scene scene = new Scene(root, 800, 450);
		scene.getStylesheets().add(getClass().getResource("/volvocanbusfxml.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		//VolvoCANBUSProcess.doCanbusAlt();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
