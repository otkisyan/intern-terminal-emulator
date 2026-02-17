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

    private void scrollUp() {
        List<Cell> top = screen.remove(0);
        scrollback.add(top);

        if (scrollback.size() > scrollbackMax) {
            scrollback.removeFirst();
        }

        screen.add(createEmptyLine());
    }

    private void newLine() {
        cursorCol = 0;
        cursorRow++;
        if (cursorRow >= height) {
            scrollUp();
            cursorRow = height - 1;
        }
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

    public void write(String text) {
        char[] chars = text.toCharArray();
        for (int idx = 0; idx < chars.length; idx++) {
            char ch = chars[idx];

            if (ch == '\n') {
                newLine();
                continue;
            }

            screen.get(cursorRow).set(cursorCol, new Cell(ch, currentAttributes));

            cursorCol++;
            if (cursorCol >= width) {
                cursorCol = 0;
                cursorRow++;

                if (cursorRow >= height) {
                    if (idx < chars.length - 1) {
                        scrollUp();
                        cursorRow = height - 1;
                    } else {
                        cursorRow = height - 1;
                    }
                }
            }
        }
    }

    public void insert(String text) {
        char[] chars = text.toCharArray();
        for (int idx = 0; idx < chars.length; idx++) {
            char ch = chars[idx];

            if (ch == '\n') {
                newLine();
                continue;
            }

            List<Cell> line = screen.get(cursorRow);

            for (int i = width - 1; i > cursorCol; i--) {
                line.set(i, line.get(i - 1));
            }

            line.set(cursorCol, new Cell(ch, currentAttributes));

            cursorCol++;
            if (cursorCol >= width) {
                cursorCol = 0;
                cursorRow++;

                if (cursorRow >= height) {
                    if (idx < chars.length - 1) {
                        scrollUp();
                        cursorRow = height - 1;
                    } else {
                        cursorRow = height - 1;
                    }
                }
            }
        }
    }

    public void fillLine(int row, char ch) {
        if (row < 0 || row >= height) return;

        List<Cell> line = screen.get(row);
        for (int i = 0; i < width; i++) {
            line.set(i, new Cell(ch, currentAttributes));
        }
    }

    public void insertEmptyLineAtBottom() {
        scrollUp();
    }

    public void clearScreen() {
        screen.clear();
        for (int i = 0; i < height; i++) {
            screen.add(createEmptyLine());
        }
        cursorRow = 0;
        cursorCol = 0;
    }

    public void clearAll() {
        clearScreen();
        scrollback.clear();
    }

    public char getCharAt(int row, int col, boolean fromScrollback) {
        if (fromScrollback) {
            if (row < 0 || row >= scrollback.size()) return ' ';
            return scrollback.get(row).get(col).ch;
        } else {
            if (row < 0 || row >= height) return ' ';
            return screen.get(row).get(col).ch;
        }
    }

    public Attributes getAttributesAt(int row, int col, boolean fromScrollback) {
        if (fromScrollback) {
            if (row < 0 || row >= scrollback.size()) return null;
            List<Cell> line = scrollback.get(row);
            if (col < 0 || col >= line.size()) return null;
            return line.get(col).attr;
        } else {
            if (row < 0 || row >= height) return null;
            List<Cell> line = screen.get(row);
            if (col < 0 || col >= line.size()) return null;
            return line.get(col).attr;
        }
    }

    public String getLine(int row, boolean fromScrollback) {
        List<Cell> line;

        if (fromScrollback) {
            if (row < 0 || row >= scrollback.size()) return "";
            line = scrollback.get(row);
        } else {
            if (row < 0 || row >= height) return "";
            line = screen.get(row);
        }

        StringBuilder sb = new StringBuilder();
        for (Cell c : line) {
            sb.append(c.ch);
        }
        return sb.toString();
    }

    public String getScreenContent() {
        StringBuilder sb = new StringBuilder();
        for (List<Cell> line : screen) {
            for (Cell c : line) {
                sb.append(c.ch);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String getFullContent() {
        StringBuilder sb = new StringBuilder();

        for (List<Cell> line : scrollback) {
            for (Cell c : line) {
                sb.append(c.ch);
            }
            sb.append('\n');
        }

        for (List<Cell> line : screen) {
            for (Cell c : line) {
                sb.append(c.ch);
            }
            sb.append('\n');
        }

        return sb.toString();
    }

}
