package com.gmail.mooman219;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JWindow;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Window extends JWindow {

    private static final int width;
    private static final int height;
    private static final Object lock = new Object();
    private static final HashMap<String, Window> pool = new HashMap<String, Window>();
    private static final ConcurrentLinkedQueue<Operation> queue = new ConcurrentLinkedQueue<Operation>();

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();
    }

    public static void enable(String window) {
        queue.add(new Operation(Operation.Type.ENABLE, window));
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public static void disable(String window) {
        queue.add(new Operation(Operation.Type.DISABLE, window));
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public static void update() {
        Operation operation;
        while ((operation = queue.poll()) != null) {
            Window frame = pool.get(operation.getWindow());
            switch (operation.getType()) {
                case ENABLE:
                    if (frame == null) {
                        frame = new Window();
                        pool.put(operation.getWindow(), frame);
                    }
                    frame.setVisible(true);
                    break;
                case DISABLE:
                    if (frame != null) {
                        frame.setVisible(false);
                        frame.reset();
                    }
                    break;
            }
        }
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                // Eat it
            }
        }
    }

    private final Random random = new Random();

    private Window() {
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setEnabled(false);
        setFocusable(false);
        setSize(100, height);
        reset();
    }

    private void reset() {
        setLocation(random.nextInt(width - 100), 0);
        getContentPane().setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
    }
}
