package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.serialization.Deserializer;
import ch.voev.nova.serialization.SerializationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;


@Service
public class SbDrLoader {
    @Autowired private SerializationFactory serializationFactory;
    @Autowired private FileIOFactory fileIOFactory;


    public TransportKontingentDatenrelease load(String drFileOrUrl) throws IOException {
        InputStream in = this.openFileStream(drFileOrUrl);
        Deserializer<TransportKontingentDatenrelease> deserializer = serializationFactory.getDeserializer(TransportKontingentDatenrelease.class, in);
        return deserializer.deserializeFromStream();
    }


    private InputStream openFileStream(String filename) throws IOException {
        InputStream fileStream;

        if (filename.toLowerCase().startsWith("http")) {
            fileStream = this.fileIOFactory.createUrlInputStream(filename);
        } else {
            fileStream = this.fileIOFactory.createFileInputStream(filename);
        }

        return new InflaterInputStream(fileStream);
    }
}
