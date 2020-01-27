/**
 * 
 */
package org.waal70.canbus.application.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.waal70.canbus.application.process.VolvoCANBUSProcess;
import org.waal70.canbus.util.TextAreaAppender;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author awaal
 *
 */
public class MainController implements Initializable{
	private static Logger log = LogManager.getLogger(MainController.class);

	/**
	 * 
	 */

	@FXML
    public TextArea txtMain;
	
	@FXML
    private Label btnOK;
	
	@FXML
    private Label lblCount;

	
	

	@FXML
	void buttonQuit(MouseEvent event) {
		log.info("Quit clicked" + event.toString());

		Stage stage = (Stage) txtMain.getScene().getWindow();
		// do what you have to do
		stage.close();

	}
	
	public void updateCount(String text) {
		this.lblCount.setText(text);
		
	}
	
	@FXML
    void btnGoClicked(MouseEvent event) {
		try {
			VolvoCANBUSProcess.doCanbusAlt();
			//Window owner = btnOK.getScene().getWindow();
			//AlertHelper.showAlert(AlertType.INFORMATION, owner, "Confirm", "You clicked GO");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TextAreaAppender.addLog4j2TextAreaAppender(new JTextArea(txtMain.getText()));
		
		
	}
	
}
