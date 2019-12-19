package org.waal70.canbus.application.ui;

import org.waal70.canbus.application.process.VolvoCANBUSProcess;
import org.waal70.canbus.application.ui.controller.ListController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author awaal
 * This application has three main elements:
 * A listview, a detailview and a monitorview
 * The latter just displays the output of Log4J
 */
public class VolvoCANBUSFXML extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//VolvoCANBUSProcess.doCanbusAlt();
		//first, load the overall fxml:
		BorderPane root = FXMLLoader.load(getClass().getResource("/overall.fxml"));
		
		FXMLLoader listLoader = new FXMLLoader(getClass().getResource("/list.fxml"));
		root.setLeft(listLoader.load());
		ListController listController = listLoader.getController();
		
		FXMLLoader monitorLoader = new FXMLLoader(getClass().getResource("/volvocanbus.fxml"));
        root.setCenter(monitorLoader.load());
        //ListController listController = listLoader.getController();
        VolvoCANBUSProcess.doCanbusAlt();
//        Platform.runLater(() -> {
//			try {
//				VolvoCANBUSProcess.doCanbusAlt();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
        
        DataModel model = new DataModel();
        model.loadData();
        listController.initModel(model);
        
		Scene scene = new Scene(root, 1000, 450);
		scene.getStylesheets().add(getClass().getResource("/volvocanbusfxml.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
