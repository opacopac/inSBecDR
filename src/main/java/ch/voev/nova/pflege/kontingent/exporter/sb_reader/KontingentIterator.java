package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.*;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class KontingentIterator implements Iterator<KontingentRecord> {
    private final Iterator<Fahrt> fahrtIterator;
    private Iterator<FahrtAbschnitt> fahrtAbschnittIterator;
    private Iterator<TransportKontingent> transportKontingentIterator;
    private Fahrt currentFahrt;
    private FahrtAbschnitt currentFahrtAbschnitt;
    private TransportKontingent currentTransportKontingent;
    private int recordCount = 1;


    public KontingentIterator(TransportKontingentDatenrelease dr) {
        if (dr == null) {
            throw new IllegalArgumentException("Parameter 'dr' must not be empty!");
        }

        this.fahrtIterator = dr.getFahrten() != null ? dr.getFahrten().iterator() : null;
    }


    @Override
    public boolean hasNext() {
        return this.fahrtIterator.hasNext() || this.fahrtAbschnittIterator.hasNext()
            || this.transportKontingentIterator.hasNext();
    }


    @Override
    public KontingentRecord next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        this.nextTransportKontingent();

        return new KontingentRecord(
            this.recordCount++,
            this.currentFahrt,
            this.currentFahrtAbschnitt,
            this.currentTransportKontingent
        );
    }


    private void nextTransportKontingent() {
        if (this.transportKontingentIterator == null || !this.transportKontingentIterator.hasNext()) {
            this.nextFahrtAbschnitt();
        }

        if (this.transportKontingentIterator != null && this.transportKontingentIterator.hasNext()) {
            this.currentTransportKontingent = this.transportKontingentIterator.next();
        }
    }


    private void nextFahrtAbschnitt() {
        if (this.fahrtAbschnittIterator == null || !this.fahrtAbschnittIterator.hasNext()) {
            this.nextFahrt();
        }

        if (this.fahrtAbschnittIterator != null && this.fahrtAbschnittIterator.hasNext()) {
            this.currentFahrtAbschnitt = this.fahrtAbschnittIterator.next();
            this.transportKontingentIterator = (this.currentFahrtAbschnitt.getKontingente() != null)
                ? this.currentFahrtAbschnitt.getKontingente().iterator() : null;
        }
    }


    private void nextFahrt() {
        if (this.fahrtIterator == null || !this.fahrtIterator.hasNext()) {
            throw new NoSuchElementException();
        }

        this.currentFahrt = this.fahrtIterator.next();
        this.fahrtAbschnittIterator = this.currentFahrt.getAbschnitte() != null
            ? this.currentFahrt.getAbschnitte().iterator() : null;
    }
}
