package SayItAssistant.frontend;

import java.awt.*;

public class MyFont {

    private Font myFont;    // the font that is being formatted

    /*
     * Creates a font of the specified type and size
     * 
     * @param fontFile: the file to load the font from
     * @param fontSize: the size that the font should be
     */
    public MyFont (String fontName, float fontSize) {

        // makes sure that font format is acceptable
        this.myFont = new Font(fontName, Font.PLAIN, 12);
        // Sets the size of the font
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

   public Font getBoldFont() {
        return this.myFont.deriveFont(Font.BOLD);
   }

}