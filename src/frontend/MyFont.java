package frontend;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MyFont {

    private Font myFont;    // the font that is being formatted

    /*
     * Creates a font of the specified type and size
     * 
     * @param fontFile: the file to load the font from
     * @param fontSize: the size that the font should be
     */
    public MyFont (File fontFile, float fontSize) {

        // makes sure that font format is acceptable
        try {
            this.myFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // sets the size of the font
        this.myFont = this.myFont.deriveFont(fontSize);
   } 

   /*
    * Returns the font.
    *
    * @return Font: the font that was specified
    */
   public Font getFont() {
        return this.myFont;
   }

}
