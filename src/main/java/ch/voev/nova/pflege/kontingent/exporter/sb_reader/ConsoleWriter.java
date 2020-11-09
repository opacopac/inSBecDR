package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.springframework.stereotype.Component;


@Component
public class ConsoleWriter {
    public void println(String text) {
        System.out.println(text);
    }


    public void print(String text) {
        System.out.print(text);
    }
}
