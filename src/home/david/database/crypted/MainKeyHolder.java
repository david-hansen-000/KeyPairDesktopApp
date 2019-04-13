package home.david.database.crypted;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class MainKeyHolder {
    private static ClickState state;
    private HBox hbox;
    private HashMap<String, String> key_values;
    private String main_key;
    private VBox key_values_box;
    private RefreshFunction refresh;
    private ContextMenu contextMenu;
    private MenuItem copy_menu;
    private MenuItem edit_menu;
    private MenuItem add_menu;
    private MenuItem delete_menu;

    //TODO: change all of this to incorporate Elements with rowid values that need to be
    //-- tracked so that when we want to change or delete a value in the database
    //-- it is easier to do.

    public MainKeyHolder(String main_key) {
        this.main_key = main_key;
        key_values = new HashMap<>();
        key_values_box = new VBox();

        contextMenu = new ContextMenu();
        copy_menu=new MenuItem("Copy");
        add_menu=new MenuItem("Add");
        edit_menu=new MenuItem("Edit");
        delete_menu=new MenuItem("Delete");
        contextMenu.getItems().addAll(copy_menu, add_menu, edit_menu, delete_menu);

        hbox = new HBox();
        hbox.setPadding(new Insets(5));
        hbox.setSpacing(5);
        //hbox.setStyle("-fx-border-color:black");
        hbox.setBorder(new Border(new BorderStroke(Color.AQUA, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        Label label = new Label(main_key);
        label.setId("main_key_label");
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> mouseClicked(mouseEvent, "main_key", null));
        hbox.getChildren().addAll(label, key_values_box);
        state = ClickState.COPY;
        hbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> mouseClicked(mouseEvent, "main_key_box", null));


    }

    public MainKeyHolder(String main_key, ArrayList<Element> elements) {
        this(main_key);
        for (Element element : elements) {
            addKeyValue(element);
        }
    }

    public static void setState(ClickState state) {
        MainKeyHolder.state = state;
    }

    private void mouseClicked(MouseEvent mouseEvent, String parent, Element element) {
        mouseEvent.consume();
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            Node node=(Node)mouseEvent.getSource();
            copy_menu.setOnAction(e-> copyAction(parent, element));
            add_menu.setOnAction(e-> addAction(parent, element));
            edit_menu.setOnAction(e-> editAction(parent, element));
            delete_menu.setOnAction(e->deleteAction(parent, element));
            contextMenu.show(node, mouseEvent.getScreenX()+2, mouseEvent.getScreenY()+2);
        }
        if (mouseEvent.getButton() == MouseButton.PRIMARY) { //left-click
            switch (state) {
                case COPY: {
                    copyAction(parent, element);
                    break;
                }
                case EDIT: {
                    editAction(parent, element);
                    break;
                }
                case DELETE: {
                    deleteAction(parent, element);
                    break;
                }
                case ADD: {
                   addAction(parent, element);
                   break;
                }
            }
        }
    }

    private void copyAction(String parent, Element element) {
        if (!parent.equals("main_key")) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(element.getValue());
            clipboard.setContent(content);
            System.out.println("value has been copied to clipboard");
        }
    }

    private void editAction(String parent, Element element) {
        String oldValue = null;
        switch (parent) {
            case "main_key": {
                oldValue = main_key;
                break;
            }
            case "key": {
                oldValue = element.getKey();
                break;
            }
            case "value": {
                oldValue = element.getValue();
                break;
            }
        }
        TextInputDialog dialog = new TextInputDialog(oldValue);
        dialog.setTitle("Editing");
        dialog.setContentText("new value:");
        Optional<String> result = dialog.showAndWait();
        String newValue = null;
        if (result.isPresent()) {
            newValue = result.get();
        }
        if (parent.equals("main_key")) {
            System.out.println("main key will be:" + newValue);
            Database.changeMainKey(oldValue, newValue);
        } else {
            System.out.println(parent + " will be:" + newValue);
            if (parent.equals("key")) {
                Database.changeKey(newValue, element.getId());
                System.out.println(element.toString());
            }
            if (parent.equals("value")) {
                Database.changeValue(newValue, element.getId());
                System.out.println(element.toString());
            }
        }
        System.out.println("editing:" + parent);
        refresh.refresh();
    }

    private void deleteAction(String parent, Element element) {
        if (parent.equals("main_key")) {
            System.out.println("flag!!");
        }
        System.out.println("deleting:" + parent);
        refresh.refresh();
    }

    private void addAction(String parent, Element element) {
        System.out.println("popup for new key/value pair");
        NewEntryPopup popup = new NewEntryPopup(NewEntryPopup.Type.Key);
        Element test = popup.show();
        test.setMain_key(main_key);
        Database.addKeyValue(test);
        System.out.println("result:\n" + test);
        refresh.refresh();
    }

    public void setRefresh(RefreshFunction refresh) {
        this.refresh = refresh;
    }

    public void addKeyValue(Element element) {
        Border label_border = new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT));
        Label key_label = new Label(element.getKey());
        key_label.setBorder(label_border);
        key_label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> mouseClicked(mouseEvent, "key", element));
        Label value_label = new Label(element.getValue());
        value_label.setBorder(label_border);
        value_label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> mouseClicked(mouseEvent, "value", element));
        key_values.put(element.getKey(), element.getValue());
        HBox key_value_box = new HBox(key_label, value_label);
        key_value_box.setPrefWidth(250);
        key_value_box.setBorder(new Border(new BorderStroke(Color.YELLOWGREEN, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        key_value_box.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> mouseClicked(mouseEvent, "key_value_box", element));
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
