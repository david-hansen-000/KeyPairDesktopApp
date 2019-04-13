package home.david.database.crypted;

public class TestPassword {
    public static void main(String[] args) {
        String password = "anothertest:b(@)";
        String result = "";
        String buffer = "";
        if (password.matches(".+:(b|buf|buffer)\\(.\\)$")) {
            buffer=password.substring(password.length()-2, password.length()-1);
        }
        result = password.replaceAll("(.+):(b|buf|buffer)\\(.\\)","$1");
        System.out.println("buffer:"+buffer);
        System.out.println("password:"+result);
    }
}
