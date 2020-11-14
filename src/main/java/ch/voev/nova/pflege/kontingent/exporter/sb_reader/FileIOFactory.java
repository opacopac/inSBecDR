package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;


@Service
public class FileIOFactory {
    public Writer createFileWriter(String filename) throws IOException {
        return new FileWriter(filename);
    }


    public InputStream createFileInputStream(String fileName) throws FileNotFoundException {
        return new FileInputStream(new File(fileName));
    }


    public InputStream createUrlInputStream(String url) throws IOException {
        return new URL(url).openStream();
    }
}
