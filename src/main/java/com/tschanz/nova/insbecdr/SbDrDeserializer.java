package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.serialization.SerializationFactory;
import ch.voev.nova.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.zip.InflaterInputStream;


@Service
public class SbDrDeserializer {
    @Autowired
    private final SerializationFactory serializationFactory;


    public SbDrDeserializer(SerializationFactory serializationFactory) {
        this.serializationFactory = serializationFactory;
    }


    public TransportKontingentDatenrelease deserialize(String drFileName) throws FileNotFoundException {
        InputStream in = this.openFileStream(drFileName);
        Deserializer<TransportKontingentDatenrelease> deserializer = serializationFactory.getDeserializer(TransportKontingentDatenrelease.class, in);
        return deserializer.deserializeFromStream();
    }


    private InputStream openFileStream(String filename) throws FileNotFoundException {
        return new InflaterInputStream(
            new FileInputStream(
                new File(filename)
            )
        );
    }
}
