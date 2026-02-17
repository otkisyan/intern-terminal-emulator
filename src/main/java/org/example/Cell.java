package org.example;

public class Cell {
    public char ch;
    public Attributes attr;

    public Cell(char ch, Attributes attr) {
        this.ch = ch;
        this.attr = attr.copy();
    }

    public static Cell empty() {
        return new Cell(' ', new Attributes());
    }
}
