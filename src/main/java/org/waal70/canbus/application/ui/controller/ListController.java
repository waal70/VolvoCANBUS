package org.waal70.canbus.application.ui.controller;


import org.waal70.canbus.application.ui.DataModel;
import org.waal70.canbus.application.ui.Message;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListController {

    @FXML
    private ListView<Message> listView ;

    private DataModel model ;

    public void initModel(DataModel model) {
        // ensure model is only set once:
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model ;
        listView.setItems(model.getMessageList());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            model.setCurrentPerson(newSelection));

        model.currentMessageProperty().addListener((obs, oldMessage, newMessage) -> {
            if (newMessage == null) {
                listView.getSelectionModel().clearSelection();
            } else {
                listView.getSelectionModel().select(newMessage);
            }
        });
        
        listView.setCellFactory(lv -> new ListCell<Message>() {
            @Override
            public void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(msg.getCanId() + " " + msg.getMessage());
                }
            }
        });
    }
}