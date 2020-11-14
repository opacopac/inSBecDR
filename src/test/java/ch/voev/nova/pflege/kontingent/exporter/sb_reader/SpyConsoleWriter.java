package ch.voev.nova.pflege.kontingent.exporter.sb_reader;


public class SpyConsoleWriter extends ConsoleWriter {
    public String text = "";


    @Override
    public void println(String text) {
        this.text += text;
    }


    @Override
    public void print(String text) {
        this.text += text;
    }
}
