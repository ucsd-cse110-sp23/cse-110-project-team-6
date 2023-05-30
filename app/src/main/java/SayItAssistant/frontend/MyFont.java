package SayItAssistant.frontend;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyFont {

    private Font myFont;    // the font that is being formatted

    /*
     * Creates a font of the specified type and size
     * 
     * @param fontFile: the file to load the font from
     * @param fontSize: the size that the font should be
     */
    public MyFont (String fontFile, float fontSize) {

        // makes sure that font format is acceptable
        try {
            InputStream inStream = new BufferedInputStream(new FileInputStream(fontFile));

            this.myFont = Font.createFont(Font.TRUETYPE_FONT, inStream);
            this.myFont = this.myFont.deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // sets the size of the font
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