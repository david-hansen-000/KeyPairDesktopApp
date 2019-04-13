package home.david.database.crypted;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DatabaseController {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public TextField key_tf;
    @FXML
    public Button search_btn;
    @FXML
    public VBox key_holder;
    private EventHandler<ActionEvent> handler;

    @FXML
    public void menuEvent(ActionEvent event) {
        handler.handle(event);
    }

    public void setActionListener(EventHandler<ActionEvent> handler) {
        this.handler = handler;
    }
}
