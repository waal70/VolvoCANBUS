/**
 * 
 */
package org.waal70.canbus.application.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.waal70.canbus.application.process.VolvoCANBUSProcess;
import org.waal70.canbus.util.TextAreaAppender;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author awaal
 *
 */
public class MainController implements Initializable{
	private static Logger log = Logger.getLogger(MainController.class);

	/**
	 * 
	 */

	@FXML
	private MenuItem mnuQuit;

	@FXML
	private MenuBar mainScreen;
	@FXML
	private Pane mainPane;
	@FXML
    public TextArea txtMain;
	
	@FXML
    private Button btnOK;

	
	

	@FXML
	void buttonQuit(ActionEvent event) {
		log.info("Quit clicked" + event.toString());

		Stage stage = (Stage) mainScreen.getScene().getWindow();
		// do what you have to do
		stage.close();

	}
	
	@FXML
    void btnGoClicked(ActionEvent event) {
		try {
			VolvoCANBUSProcess.doCanbusAlt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TextAreaAppender.setTextArea(txtMain);
		
	}
}
