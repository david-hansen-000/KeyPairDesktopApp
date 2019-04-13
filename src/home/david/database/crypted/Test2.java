package home.david.database.crypted;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //new Test1();
        new Test3();
    }

    /*
    need a format

    main-key    key       value
                key       value
                key       value

    main-key    key       value
                key       value
                key       value

    etc.

    a database (separate file) of all the main-keys that I create

    an add/edit/delete option for every main-key (and thus all the key/value pairs with it)
    and for every key/value pair

    list all main-keys with key/value pairs

    a search for main-key(s)

    (I don't want to try to remember exactly how I saved it, so a sort of suggested name maybe?)
    --- if the main-key database or flat file is found then allow that to be used for suggested main-keys

    maybe also a set of no main-keys, the key-value pairs help identify what they go to.

     */
}
