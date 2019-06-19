package org.waal70.canbus.application.ui;


import java.io.File;

import org.waal70.canbus.features.queue.CanBusMessage;
import org.waal70.canbus.features.queue.CanMessageQueue;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

    private final ObservableList<Message> messageList = FXCollections.observableArrayList(message -> 
        new Observable[] {message.canidProperty(), message.messageProperty()});

    private final ObjectProperty<Message> currentMessage = new SimpleObjectProperty<>(null);

    public ObjectProperty<Message> currentMessageProperty() {
        return currentMessage ;
    }

    public final Message getCurrentMessage() {
        return currentMessageProperty().get();
    }

    public final void setCurrentPerson(Message msg) {
        currentMessageProperty().set(msg);
    }

    public ObservableList<Message> getMessageList() {
        return messageList ;
    }
    
    public void loadData() {
        // mock...
    	CanMessageQueue myQ = CanMessageQueue.getInstance();
    	int msgCounter = 0;
    	CanBusMessage cm = null;
    	//myQ contains CanBusMessages
    	while (msgCounter != 5) {
			cm = myQ.get();
			if (cm != null)
			{
				messageList.add(new Message(cm));
				msgCounter++;
			}
			//messageList.add(new Message(new CanMessage("1234"+msgCounter, "123456")));
			//Thread.sleep(100);
    	}
        
    }
    
    public void saveData(File file) { }
}