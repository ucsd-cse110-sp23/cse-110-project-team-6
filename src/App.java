import frontend.*;
import backend.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class AppFrame extends JFrame {
    public AppFrame() {
        setTitle("SayIt Assistant");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        HistoryPanel historyPanel = new HistoryPanel();
        add(historyPanel.getScrollPane(), BorderLayout.WEST);
        DisplayPanel displayPanel = new DisplayPanel();
        add(displayPanel, BorderLayout.CENTER);
        setIconImage(new ImageIcon("src/icon.png").getImage());
        setVisible(true);

        //Implement OpenSans ttf file to panel font
        File fontFile = new File("fonts/OpenSans-Regular.ttf");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        Font myfont = font.deriveFont(16f);

        for (int i = 0; i < 1000; i++) {
            HistoryButton h = new HistoryButton(i, "History Button " + i);
            h.setFont(myfont);
            h.addActionListener(e -> {
                displayPanel.display.setText("This is history for question number " + h.id);
                displayPanel.display.setFont(myfont);
                displayPanel.display.setForeground(new Color(255, 255, 255));

            });
            historyPanel.addHistoryButton(h);
        }
        revalidate();

    }
}

public class App {
    public static void main(String[] args) {
        new AppFrame();
        new questionButton();
    }
}
