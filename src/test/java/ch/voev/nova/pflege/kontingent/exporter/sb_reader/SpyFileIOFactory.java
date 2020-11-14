package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;


public class SpyFileIOFactory extends FileIOFactory {
    public SpyWriter spyWriter = new SpyWriter();
    public byte[] simulateInput;
    public String openedFilename;
    public String openedUrl;


    @Override
    public Writer createFileWriter(String filename) {
        this.openedFilename = filename;
        return this.spyWriter;
    }


    @Override
    public InputStream createFileInputStream(String filename) {
        this.openedFilename = filename;
        return new ByteArrayInputStream(simulateInput);
    }


    @Override
    public InputStream createUrlInputStream(String url) {
        this.openedUrl = url;
        return new ByteArrayInputStream(simulateInput);
    }


    public static class SpyWriter extends Writer {
        public String text = "";
        public boolean hasFlushed;
        public boolean hasClosed;


        @Override
        public void write(char[] cbuf, int off, int len) {
            this.text += new String(cbuf, off, len);
        }


        @Override
        public void flush() {
            this.hasFlushed = true;
        }


        @Override
        public void close() {
            this.hasClosed = true;
        }
    }
}
