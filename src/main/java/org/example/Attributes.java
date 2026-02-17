package org.example;


public class Attributes {
    public Color fg = Color.DEFAULT;
    public Color bg = Color.DEFAULT;
    public boolean bold = false;
    public boolean italic = false;
    public boolean underline = false;

    public Attributes copy() {
        Attributes a = new Attributes();
        a.fg = this.fg;
        a.bg = this.bg;
        a.bold = this.bold;
        a.italic = this.italic;
        a.underline = this.underline;
        return a;
    }
}
