package org.waal70.canbus.application.ui;


import org.waal70.canbus.features.queue.CanBusMessage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
	
    private final CanBusMessage myMessage;
    
    public final StringProperty canidProperty() {
    	StringProperty canid = new SimpleStringProperty("dummy");
        return canid;
    }

   public final StringProperty messageProperty() {
	   StringProperty message = new SimpleStringProperty(myMessage.getMessage());
        return message;
    }

    public Message(CanBusMessage msg) {
        this.myMessage = msg;
    }

  	public final String getMessage() 
	{
		return this.messageProperty().get();
	}

	public final String getCanId() {
		return this.canidProperty().get();
	}
}