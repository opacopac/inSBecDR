package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.serialization.Deserializer;
import ch.voev.nova.serialization.SerializationFactory;
import ch.voev.nova.serialization.Serializer;

import java.io.InputStream;
import java.io.OutputStream;


public class SpySerializationFactory implements SerializationFactory {
    public Deserializer<TransportKontingentDatenrelease> mockDeserializer = new MockDeserializer();


    @Override
    public Serializer getSerializer(Object o, OutputStream outputStream) {
        return null; // not implemented
    }


    @Override
    public <T> Deserializer<T> getDeserializer(Class<T> aClass, InputStream inputStream) {
        return (Deserializer<T>) this.mockDeserializer;
    }


    public static class MockDeserializer implements Deserializer<TransportKontingentDatenrelease> {
        public TransportKontingentDatenrelease simulateDr;


        @Override
        public TransportKontingentDatenrelease deserializeFromStream() {
            return TestMockDr.createDr();
        }
    }
}
