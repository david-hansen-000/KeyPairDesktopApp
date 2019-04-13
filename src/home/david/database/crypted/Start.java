package home.david.database.crypted;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

public class Start extends Application {

    private DatabaseController controller;
    private Connection connection;
    private Stage stage;
    private ArrayList<String> main_keys;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Class.forName("org.sqlite.JDBC");
        main_keys = new ArrayList<>();
        //Font.loadFont(getClass().getResourceAsStream("OpensSans-Regular.ttf"), 10);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("database_open.fxml"));

        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Open+Sans");
        root.setStyle("-fx-font-family: Open Sans; -fx-font-size: 10;");
        stage.setScene(scene);
        controller.search_btn.setOnAction(actionEvent -> searchAction());
        controller.key_tf.setOnAction(actionEvent -> searchAction());
        controller.setActionListener(this::menuEvent);
        stage.show();
        //Database.startConnection();
        //testFill();
    }

    private void menuEvent(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        switch (item.getId()) {
            case "add_menu": {
                System.out.println("add menu was clicked");
                MainKeyHolder.setState(MainKeyHolder.ClickState.ADD);
                break;
            }
            case "edit_menu": {
                System.out.println("edit menu was clicked");
                MainKeyHolder.setState(MainKeyHolder.ClickState.EDIT);
                break;
            }
            case "delete_menu": {
                System.out.println("delete menu was clicked");
                MainKeyHolder.setState(MainKeyHolder.ClickState.DELETE);
                break;
            }
            case "copy_menu": {
                System.out.println("copy menu was clicked");
                MainKeyHolder.setState(MainKeyHolder.ClickState.COPY);
                break;
            }
            case "new_main_key_menu": {
                NewEntryPopup popup = new NewEntryPopup(NewEntryPopup.Type.MainKey);
                Element e = popup.show();
                Database.addMainKey(e.getMain_key());
                refresh();
                break;
            }
            case "open_database_menu": {
                //TODO: filedialog that sets location of database of clear main_keys
                FileChooser fc = new FileChooser();
                File file = fc.showOpenDialog(stage);
                Database.setMainKeyConnection(file);
                break;
            }
            case "change_password_menu": {
                Database.changePassword();
                break;
            }
            case "test_fill_menu": {
                testFill();
                break;
            }
            case "refresh_menu": {
                refresh();
                break;
            }
        }
    }

    private void testFill() {
        try {
            Database.startConnection();
            //String main_key="windows";
            //MainKeyHolder keyHolder=new MainKeyHolder(main_key, Database.getKeyValuePairs(main_key));
            main_keys = Database.getAllMainKeys();
            fillAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillAll() {
        for (String main_key : main_keys) {
            fill(main_key);
        }
    }

    private void searchAction() {
        clearAll();
        main_keys.clear();
        String search = controller.key_tf.getText();
        main_keys.add(search);
        fill(search);
    }

    private void fill(String main_key) {
        MainKeyHolder keyHolder = new MainKeyHolder(main_key, Database.getKeyValuePairs(main_key));
        keyHolder.setRefresh(this::refresh);
        controller.key_holder.getChildren().add(keyHolder.getHbox());
    }

    private void refresh() {
        clearAll();
        fillAll();
    }

    private void clearAll() {
        controller.key_holder.getChildren().clear();
    }
}
