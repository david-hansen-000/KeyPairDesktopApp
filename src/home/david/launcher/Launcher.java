package home.david.launcher;

import java.io.File;
import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {
	// write your code here
        //TODO: look at platform os
        // -- get the java_home
        File java_home=new File(System.getProperty("java.home"));
        File java_executable=new File(java_home,"bin/java");
        //TODO: pull version number from name, particularly the ##.#.# or ## part

        String javaNumVersion=null;
        String java_home_name=java_home.getName();
        if (java_home_name.matches(".+\\d+\\.\\d+\\.\\d+$")) {
            javaNumVersion=java_home_name.substring(java_home_name.length()-6);
        }
        if (java_home_name.matches(".+\\d+$")) {
            javaNumVersion=java_home_name.substring(java_home_name.length()-2);
        }

        //TODO: find the javafx sdk directory in
        // -- directory parent of java_home
        File java_dir=java_home.getParentFile();
        File javafx_sdk = null;
        if (java_dir.exists() && java_dir.isDirectory()) {
            for (File f:java_dir.listFiles()) {
                if (f.getName().matches("javafx-sdk-"+javaNumVersion)) {
                    javafx_sdk=f;
                }
            }
        }
        if (javafx_sdk==null) {
            javafx_sdk=new File(java_dir, "javafx-sdk-11.0.2");
        }
        //TODO: pool the modules together to set up the classpath
        String sep=System.getProperty("path.separator");
        StringBuilder classpath=new StringBuilder();
        for (File f: new File(javafx_sdk, "lib").listFiles()) {
            if (f.getName().endsWith(".jar")) {
                classpath.append(f.getAbsolutePath()).append(sep);
            }
        }
        //classpath.deleteCharAt(classpath.length()-1);
        File user_dir=new File(System.getProperty("user.dir"));

        classpath.append(new File(user_dir, "KeyPair.jar").getAbsolutePath()).append(sep);
        classpath.append(new File(user_dir, "sqlite-jdbc-3.23.1.jar").getAbsolutePath()).append(sep);
        //File crypt_file=new File(user_dir.getParentFile(),"/CryptLib.jar");
        classpath.append(new File(user_dir,"CryptLib.jar").getAbsolutePath());



        //String process=java_executable.getAbsolutePath()+" -p "+classpath.toString()+" -m home.david.database.crypted.Start";

        //System.out.println(process);
        ProcessBuilder pb=new ProcessBuilder(java_executable.getAbsolutePath(), "-p", classpath.toString(), "-m", "database/home.david.database.crypted.Start");
        try {
            pb.inheritIO();
            Process p=pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //TODO: start a new jvm with the attributes gathered
    }
}
