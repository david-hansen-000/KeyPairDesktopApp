package home.david.database.crypted;

import home.david.cryptlib.Crypt;
import home.david.cryptlibgui.PasswordPopup;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test1 {
    public Test1() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection=DriverManager.getConnection("jdbc:sqlite:secrets.db");
            connection.createStatement().execute("create table if not exists key_pairs (main_key text, key text, value text)");
            Crypt crypt=new Crypt();
            PasswordPopup popup=new PasswordPopup();
            String hash = Crypt.getHash("windows");
            String pw1 = popup.showPopup();
            crypt.setPassword(pw1);
            //String key1 = new String(crypt.encrypt("opening"));
            String key1 = crypt.getEncryptedHexString("opening");
            //String value1 = new String(crypt.encrypt("westborrow231!"));
            String value1 = crypt.getEncryptedHexString("westborrow231!");

            String sql = "insert into key_pairs values (?,?,?)";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,hash);
            ps.setString(2,key1);
            ps.setString(3, value1);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Test1();
    }
}
