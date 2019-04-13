package home.david.database.crypted;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class InfoHolder {
    private static InfoState state;
    private HBox hbox;
    private Label key_label;
    private Label value_label;
    private UpdateListener updater;

    public InfoHolder(String key, String value) {
        state=InfoState.COPY;
        hbox=new HBox();
        hbox.setPadding(new Insets(5));
        hbox.setSpacing(5);
        key_label=new Label(key);
        value_label=new Label(value);
        hbox.getChildren().addAll(key_label, value_label);
        //TODO: the above doesn't work
        // -- need to create a vbox that holds one or more hbox's with key/value pairs.

        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton()== MouseButton.PRIMARY) { //left-click
                switch (state) {
                    case COPY: {
                        //TODO: copy value to clipboard
                        System.out.println("here's where we'll copy the value");
                        break;
                    }
                    case ADD: {
                        //TODO: this shouldn't be here

                        break;
                    }
                    case EDIT: {
                        //TODO: open a window with values to edit
                        System.out.println("here's where we'll edit the selected item");
                        break;
                    }
                    case DELETE: {
                        //TODO: delete the key/vaue pair from the database
                        System.out.println("here's where we'll delete the item");
                        break;
                    }
                }
            }

        });
    }

    public static void setState(InfoState state) {
        InfoHolder.state = state;
    }

   /*
     get method for hbox which is set up already with key and value

     another class created to edit values in these
      -- if edited values, updates values in database
      --- a way to trigger an update in database if edited
      ---- set some way to connect this to database, I don't want
      ---- to add another object reference to database
      ---- perhaps something like the fragments in android
      ---- where an interface is subclassed in this class
      ---- and then attached where this object gets initiated??
      ---- or like a listener

     a handler for
     - right-click to edit
     - left-click to copy value to clipboard

     */

    protected interface UpdateListener {
        void update(String key, String value);
    }

    public enum InfoState {
        EDIT, ADD, DELETE, COPY
    }
}
