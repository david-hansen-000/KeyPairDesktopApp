package home.david.database.crypted;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewEntryPopup {
    private Type type;
    private Stage stage;

    public NewEntryPopup(Type type) {
        this.type=type;
        stage=new Stage();
    }

    public Element show() {
        Element result =null;
        VBox vBox=new VBox();
        TextField mainKey=new TextField();
        TextField key=new TextField();
        TextField value=new TextField();
        if (type==Type.MainKey) {
            HBox hBox=setupHbox("Main Key:", mainKey);
            vBox.getChildren().add(hBox);
        } else {
            HBox key_box=setupHbox("Key:", key);
            HBox value_box=setupHbox("Value:", value);
            vBox.getChildren().addAll(key_box, value_box);
        }
        stage.setScene(new Scene(vBox));
        stage.showAndWait();
            result=new Element(mainKey.getText(), key.getText().replaceAll("\\s",""), value.getText());
        return result;
    }

    public HBox setupHbox(String labelText, TextField tf) {
        HBox hBox = new HBox();
        tf.setOnAction(actionEvent -> stage.hide());
        hBox.setPrefWidth(300);
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5));
        Label label=new Label(labelText);
        HBox.setHgrow(tf, Priority.ALWAYS);
        hBox.getChildren().addAll(label, tf);
        return hBox;
    }

    public static enum Type {
        MainKey, Key
    }
}
