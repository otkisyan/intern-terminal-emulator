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
}
