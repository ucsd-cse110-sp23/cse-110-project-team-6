/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package SayItAssistant;

import java.io.IOException;
import javax.swing.UIManager;

import SayItAssistant.middleware.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AppManager();
    }
}