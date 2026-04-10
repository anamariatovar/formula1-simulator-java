package org.example.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class OutputCapture {

    private OutputCapture() {
    }

    public static String capture(Runnable action) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream captured = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        PrintStream previous = System.out;
        System.setOut(captured);
        try {
            action.run();
        } finally {
            System.setOut(previous);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
