package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.serialization.SerializationFactory;
import ch.voev.nova.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;
import java.util.zip.InflaterInputStream;


@Service
public class SbDrLoader {
    @Autowired
    private final SerializationFactory serializationFactory;


    public SbDrLoader(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }


    public TransportKontingentDatenrelease load(String drFileOrUrl) throws IOException {
        InputStream in = this.openFileStream(drFileOrUrl);
        Deserializer<TransportKontingentDatenrelease> deserializer = serializationFactory.getDeserializer(TransportKontingentDatenrelease.class, in);
        return deserializer.deserializeFromStream();
    }


    private InputStream openFileStream(String filename) throws IOException {
        InputStream fileStream;

        if (filename.toLowerCase().startsWith("http")) {
            fileStream = new URL(filename).openStream();
        } else {
            fileStream = new FileInputStream(new File(filename));
        }

        return new InflaterInputStream(fileStream);
    }
}
