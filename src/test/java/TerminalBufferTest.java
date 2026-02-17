import org.example.Attributes;
import org.example.Color;
import org.example.TerminalBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferTest {

    @Test
    @DisplayName("setCursor() should move cursor to valid position within bounds")
    void setCursorShouldMoveToPosition() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.setCursor(1, 2);
        assertEquals(1, buffer.getCursorRow());
        assertEquals(2, buffer.getCursorCol());
    }

    @Test
    @DisplayName("setCursor() should allow cursor to be set at screen boundaries")
    void setCursorAtScreenBoundaries() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.setCursor(0, 0);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

        buffer.setCursor(0, 4);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());

        buffer.setCursor(2, 0);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

        buffer.setCursor(2, 4);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());
    }

    @Test
    @DisplayName("moveUp/moveDown/moveLeft/moveRight should move cursor correctly within bounds")
    void moveCursorShouldUpdatePosition() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.setCursor(1, 2);

        buffer.moveUp(1);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(2, buffer.getCursorCol());

        buffer.moveDown(2);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(2, buffer.getCursorCol());

        buffer.moveLeft(1);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(1, buffer.getCursorCol());

        buffer.moveRight(2);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(3, buffer.getCursorCol());
    }

    @Test
    @DisplayName("move*() should allow cursor to be moved to screen boundaries")
    void moveCursorFromZeroToEdges() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.setCursor(0, 0);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

        buffer.moveRight(4);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());

        buffer.moveDown(2);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());

        buffer.moveLeft(4);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

        buffer.moveUp(2);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

    }

    @Test
    @DisplayName("setCursor() should clamp cursor within screen boundaries")
    void setCursorShouldRespectBounds() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.setCursor(-10, -10);
        assertEquals(0, buffer.getCursorRow());
        assertEquals(0, buffer.getCursorCol());

        buffer.setCursor(100, 100);
        assertEquals(2, buffer.getCursorRow());
        assertEquals(4, buffer.getCursorCol());
    }

    @Test
    @DisplayName("moveUp/moveDown/moveLeft/moveRight should clamp cursor within screen boundaries")
    void moveCursorShouldRespectBounds() {
        TerminalBuffer buffer = new TerminalBuffer(5, 3, 10);

        buffer.moveUp(5);
        assertEquals(0, buffer.getCursorRow());

        buffer.moveDown(10);
        assertEquals(2, buffer.getCursorRow());

        buffer.moveLeft(10);
        assertEquals(0, buffer.getCursorCol());

        buffer.moveRight(10);
        assertEquals(4, buffer.getCursorCol());
    }
}



