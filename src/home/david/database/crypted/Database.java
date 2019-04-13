package home.david.database.crypted;

import home.david.cryptlib.Crypt;
import home.david.cryptlibgui.PasswordPopup;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection connection;
    private static Crypt crypt;
    private static Connection mk_connection;

    public static void startConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:secrets.db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (crypt == null) {
            crypt = new Crypt();
            changePassword();
        }
    }

    public static void changePassword() {
        PasswordPopup popup = new PasswordPopup();
        try {
            crypt.setPassword(popup.showPopup());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMainKeyConnection(File file) {
        try {
            mk_connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add(Element element) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("insert into key_pairs values (?,?,?)");
            ps.setString(1, Crypt.getHash(element.getMain_key()));
            ps.setString(2, crypt.getEncryptedHexString(element.getKey()));
            ps.setString(3, crypt.getEncryptedHexString(element.getValue()));
            ps.executeUpdate();
            System.out.println(element.toString() + " added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeMainKey(String oldValue, String newValue) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("update key_pairs set main_key=? where main_key=?");
            ps.setString(1, Crypt.getHash(newValue));
            ps.setString(2, Crypt.getHash(oldValue));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeKey(String newValue, int id) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("update key_pairs set key=? where rowid=?");
            ps.setString(1, crypt.getEncryptedHexString(newValue));
            ps.setInt(2, id);
            int o = ps.executeUpdate();
            if (o == 1) {
                System.out.println("updated key_pairs for rowid:" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeValue(String newValue, int id) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("update key_pairs set value=? where rowid=?");
            ps.setString(1, crypt.getEncryptedHexString(newValue));
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMainKey(String main_key) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("insert into key_pairs values (?,?,?)");
            ps.setString(1, Crypt.getHash(main_key));
            ps.setNull(2, Types.VARCHAR);
            ps.setNull(3, Types.VARCHAR);
            ps.executeUpdate();

            if (mk_connection != null) {
                PreparedStatement ps2 = mk_connection.prepareStatement("insert into main_key values (?)");
                ps2.setString(1, main_key);
                ps2.executeUpdate();
            } else {
                System.out.println("main_key database has not been set. " + main_key + " not added to main_key database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //TODO: add entry in clear in some other location, ie another database
        // -- where do I want this database?
        // -- a flat file? another popup (such as a textarea that I copy values into)
        // -- or a database that I locate on the file system or through a URL
        // -- definitely not a place that is automatically set in the program.

    }

    public static void addKeyValue(Element element) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("insert into key_pairs values (?,?,?)");
            ps.setString(1, Crypt.getHash(element.getMain_key()));
            ps.setString(2, crypt.getEncryptedHexString(element.getKey()));
            ps.setString(3, crypt.getEncryptedHexString(element.getValue()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connect() {
        if (connection == null) {
            startConnection();
        }
    }

    public static void delete(Element element) {
        connect();
        if (element.getId() > 0) {
            try {
                PreparedStatement ps = connection.prepareStatement("delete from key_pairs where rowid=?");
                ps.setInt(1, element.getId());
                ps.executeUpdate();
                System.out.println(element.toString() + " deleted");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    problem here is that I'm not sure the hex string will be the same each time. I have two entries that have
    the same clear text, but different hex strings... so, I'll need to get some way of getting the the id and
    keeping that with the element so I can easily delete the right entry from the database
     */

    public static ArrayList<Element> getKeyValuePairs(String main_key) {
        connect();
        ArrayList<Element> elements = new ArrayList<>();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("select rowid,key,value from key_pairs where main_key=?");
            String hashString = Crypt.getHash(main_key);
            System.out.println(main_key + " =>\nhashed string:" + hashString);
            ps.setString(1, hashString);
            ResultSet rs = ps.executeQuery();
            Element element = null;
            String key = null;
            String value = null;
            int counter = 0;
            while (rs.next()) {
                counter++;
                key = rs.getString("key");
                value = rs.getString("value");
                if (key != null && value != null) {
                    element = new Element(main_key, crypt.getDecryptedFromHexString(rs.getString("key")), crypt.getDecryptedFromHexString(rs.getString("value")));
                } else {
                    element = new Element(main_key, "none", "none");
                }
                element.setId(rs.getInt("rowid"));
                elements.add(element);
            }
            System.out.println(counter + " rows retrieved");
        } catch (BadPaddingException bpe) {
            System.out.println("bad password for " + main_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elements;
    }

    public static ArrayList<String> getAllMainKeys() {
        ArrayList<String> keys = new ArrayList<>();
        if (mk_connection != null) {
            ResultSet rs = null;
            try {
                rs = mk_connection.createStatement().executeQuery("select * from main_key");
                while (rs.next()) {
                    keys.add(rs.getString("key"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return keys;
    }

    public static Crypt getCrypt() {
        return crypt;
    }

}
