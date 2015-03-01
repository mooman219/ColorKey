package com.gmail.mooman219;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Input implements NativeKeyListener {

    public static Input create() {
        Input input = null;
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);

            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
            }
            input = new Input();
            GlobalScreen.getInstance().addNativeKeyListener(input);
        } catch (NativeHookException ex) {
            ex.printStackTrace();
        }
        return input;
    }

    private Input() {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        if (nke.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            } finally {
                System.exit(1);
            }
        }
        Window.enable(NativeKeyEvent.getKeyText(nke.getKeyCode()));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        Window.disable(NativeKeyEvent.getKeyText(nke.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
    }
}
