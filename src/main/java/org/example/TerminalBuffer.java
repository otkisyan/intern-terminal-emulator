package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TerminalBuffer {

    private final int width;
    private final int height;
    private final int scrollbackMax;

    private final List<List<Cell>> screen;
    private final LinkedList<List<Cell>> scrollback;
    private final Attributes currentAttributes = new Attributes();

    private int cursorRow = 0;
    private int cursorCol = 0;

    public TerminalBuffer(int width, int height, int scrollbackMax) {
        this.width = width;
        this.height = height;
        this.scrollbackMax = scrollbackMax;

        this.screen = new ArrayList<>();
        this.scrollback = new LinkedList<>();

        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
    }

    private List<Cell> createEmptyLine() {
        List<Cell> line = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            line.add(Cell.empty());
        }
        return line;
    }

    private void ensureCursorInBounds() {
        cursorRow = Math.max(0, Math.min(cursorRow, height - 1));
        cursorCol = Math.max(0, Math.min(cursorCol, width - 1));
    }

    public void setAttributes(Color fg, Color bg, boolean bold, boolean italic, boolean underline) {
        currentAttributes.fg = fg;
        currentAttributes.bg = bg;
        currentAttributes.bold = bold;
        currentAttributes.italic = italic;
        currentAttributes.underline = underline;
    }

    public int getCursorRow() { return cursorRow; }
    public int getCursorCol() { return cursorCol; }

    public void setCursor(int row, int col) {
        this.cursorRow = row;
        this.cursorCol = col;
        ensureCursorInBounds();
    }

    public void moveUp(int n) {
        cursorRow -= n;
        ensureCursorInBounds();
    }

    public void moveDown(int n) {
        cursorRow += n;
        ensureCursorInBounds();
    }

    public void moveLeft(int n) {
        cursorCol -= n;
        ensureCursorInBounds();
    }

    public void moveRight(int n) {
        cursorCol += n;
        ensureCursorInBounds();
    }
}
