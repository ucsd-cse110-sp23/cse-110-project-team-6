package frontend;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SecondaryButtonsPanel extends AppPanels{

    public SecondaryButtonsPanel() {
        this.setPreferredSize(new Dimension(400,20));
        this.setLayout(new GridLayout(1,2)); 
       addDeleteButton();
       addClearAllButton(); 
       
    }

    public void addDeleteButton() {
        JTextArea jt = new JTextArea();
        myFont = new MyFont(new File("src/fonts/OpenSans-Regular.ttf"), 18);
        jt.setFont(myFont.getFont());
        jt.setText("Delete Button Area");
        jt.setBackground(TEAL);
        jt.setBorder(BorderFactory.createLineBorder(BLACK));
        add(jt);
    }

    public void addClearAllButton() {
        JTextArea jt2 = new JTextArea();
        jt2.setFont(myFont.getFont());
        jt2.setBorder(BorderFactory.createLineBorder(BLACK));
        jt2.setText("Clear All Button Area");
        jt2.setBackground(TEAL);
        add(jt2);
    }

}
