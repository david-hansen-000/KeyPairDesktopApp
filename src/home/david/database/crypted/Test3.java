package home.david.database.crypted;

import home.david.cryptlib.Crypt;
import home.david.cryptlibgui.PasswordPopup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Test3 {
    public Test3() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection= DriverManager.getConnection("jdbc:sqlite:secrets.db");
            Crypt crypt=new Crypt();
            PasswordPopup popup=new PasswordPopup();
            crypt.setPassword(popup.showPopup());
            String hash=Crypt.getHash("windows");
            ResultSet rs=connection.createStatement().executeQuery("select key,value from key_pairs where main_key='"+hash+"'");
            String key=null;
            String value=null;
            String decryptedKey=null;
            String decryptedValue=null;
            //System.out.println("going to do first pull, then pass it");
            //rs.next();
            //System.out.println("ok, now going to second pull");
            while (rs.next()) {
                key=rs.getString("key");
                value=rs.getString("value");
                //decryptedKey=crypt.decrypt(crypt.decodeHexString(key));
                decryptedKey=crypt.getDecryptedFromHexString(key);
                //decryptedValue=crypt.decrypt(crypt.decodeHexString(value));
                decryptedValue=crypt.getDecryptedFromHexString(value);
                System.out.println("key:"+key);
                System.out.println("decrypted:"+decryptedKey);
                System.out.println("value:"+value);
                System.out.println("decrypted:"+decryptedValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
