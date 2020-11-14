package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class SpyConsoleReader extends ConsoleReader {
    private final PipedOutputStream pipedOutputStream = new PipedOutputStream();
    private final PipedInputStream pipedInputStream = new PipedInputStream();
    private final Scanner scanner = new Scanner(this.pipedInputStream);


    public SpyConsoleReader() {
        try {
            this.pipedInputStream.connect(this.pipedOutputStream);
        } catch (IOException ignored) {
        }
    }


    public void simulateInput(String text) {
        try {
            this.pipedOutputStream.write(text.getBytes());
        } catch (IOException ignored) {
        }
    }


    @Override
    public String nextLine() {
        return this.scanner.nextLine();
    }
}
