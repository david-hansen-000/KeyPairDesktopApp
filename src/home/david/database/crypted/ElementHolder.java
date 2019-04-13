package home.david.database.crypted;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ElementHolder {
    private HBox hbox;
    private Element element;
    private ArrayList<Element> elements;
    private VBox key_values_box;
    private static ClickState state;

    //TODO: still need database interaction
    //TODO: need popups for adding/editing values
    //TODO: need to change structure a bit so that the initializer isn't just with element
    // -- since elements probably aren't going to be known at the start, just the main_key
    // -- and I want the structure to be dynamic with updating based on what the main_key
    // -- is, not just for the start
    // -- ok, that's a start

    public ElementHolder(Element element) {
        //TODO: maybe not have the creation tied to the element.
        // -- it should contain elements, not be based on elements
        // -- how I had it before with MainKeyHolder makes more sense
        // -- for this ...
        key_values_box=new VBox();
        hbox=new HBox();
        hbox.setPadding(new Insets(5));
        hbox.setSpacing(5);
        Label label=new Label(element.getMain_key());
        hbox.getChildren().addAll(label, key_values_box);
        state= ElementHolder.ClickState.COPY;
        //TODO: at this level the click events should be for the main_key, not for the
        // -- attached key/value pairs.
        // -- copy doesn't need to happen at this level.
        // -- delete should raise a flag since at this level it should
        // -- delete all the key/value pair entries with this particular
        // -- main_key
        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton()== MouseButton.PRIMARY) { //left-click
                switch (state) {

                    case DELETE: {
                        //TODO: delete the key/vaue pair from the database
                        System.out.println("here's where we'll delete the item");
                        break;
                    }
                    case ADD: {
                        System.out.println("we'll add a key/value to this main key");

                        break;
                    }
                }
            }
        });
    }

    public void addKeyValue(Element element) {
        Label key_label=new Label(element.getKey());
        Label value_label=new Label(element.getValue());
        elements.add(element);
        HBox key_value_box=new HBox(key_label,value_label);

        //TODO: here's where the majority of click events should be happening
        // -- and I'll need to verify that the click isn't being
        // -- processed at both levels because that would be
        // -- bad. so if it happens at this level it needs to be consumed
        // -- so it doesn't also happen at the upper level.
        key_value_box.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (state == ElementHolder.ClickState.COPY) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(element.getValue());
                clipboard.setContent(content);
                System.out.println("value has been copied to clipboard");
            }
            if (state == ElementHolder.ClickState.DELETE) {

            }
        });
        key_value_box.setPadding(new Insets(5));
        key_value_box.setSpacing(5);
        key_values_box.getChildren().add(key_value_box);
    }


    public HBox getHbox() {
        return hbox;
    }

    public enum ClickState {
        EDIT, DELETE, COPY, ADD
    }
}
