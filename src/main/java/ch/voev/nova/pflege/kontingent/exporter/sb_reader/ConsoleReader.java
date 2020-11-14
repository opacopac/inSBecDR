package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class ConsoleReader {
    private final Scanner scanner = new Scanner(System.in);


    public String nextLine() {
        return this.scanner.nextLine();
    }
}
