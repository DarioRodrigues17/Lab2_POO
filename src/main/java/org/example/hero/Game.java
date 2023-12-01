package org.example.hero;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private final TerminalScreen screen;
    private final Arena arena;

    public Game(int width, int heigt) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(width, heigt)).createTerminal();
        screen = new TerminalScreen(terminal);

        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();

        arena = new Arena(width, heigt);
    }

    private void draw() throws IOException {
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }

    private void processKey(KeyStroke key)
    {
        arena.processKey(key);
    }

    public void run() throws IOException {
        int FPS = 25;
        int frameTime = 1000 / FPS;
        long lastMonsterMovement = 0;

        while (true) {
            long startTime = System.currentTimeMillis();

            draw();
            KeyStroke key = screen.pollInput();
            if (key != null) {
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q')
                    screen.close();
                if (key.getKeyType() == KeyType.EOF)
                    break;
                processKey(key);

            }

            if (startTime - lastMonsterMovement > 505) {
                arena.moveMonsters();
                arena.verifyMonsterCollisions();
                lastMonsterMovement = startTime;
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = frameTime - elapsedTime;

            if (sleepTime > 0) try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }


        }
    }
}
