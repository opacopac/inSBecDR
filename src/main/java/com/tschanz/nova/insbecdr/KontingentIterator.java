package com.tschanz.nova.insbecdr;


import ch.voev.nova.pflege.kontingent.sb.api.*;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class KontingentIterator implements Iterator<KontingentRecord> {
    private final Iterator<Fahrt> fahrtIterator;
    private Iterator<FahrtAbschnitt> fahrtAbschnittIterator;
    private Iterator<TransportKontingent> transportKontingentIterator;
    private Iterator<KontingentAngebot> kontingentAngebotIterator;
    private Fahrt currentFahrt;
    private FahrtAbschnitt currentFahrtAbschnitt;
    private TransportKontingent currentTransportKontingent;
    private KontingentAngebot currentKontingentAngebot;
    private int recordCount = 0;


    public KontingentIterator(TransportKontingentDatenrelease dr) {
        if (dr == null) {
            throw new IllegalArgumentException("Parameter 'dr' must not be empty!");
        }

        this.fahrtIterator = dr.getFahrten() != null ? dr.getFahrten().iterator() : null;
    }


    public boolean hasNext() {
        return this.fahrtIterator.hasNext() || this.fahrtAbschnittIterator.hasNext()
            || this.transportKontingentIterator.hasNext() || this.kontingentAngebotIterator.hasNext();
    }


    public KontingentRecord next() {
        this.nextKontingentAngebot();

        return new KontingentRecord(
            this.recordCount++,
            this.currentFahrt,
            this.currentFahrtAbschnitt,
            this.currentTransportKontingent,
            this.currentKontingentAngebot
        );
    }


    private void nextKontingentAngebot() {
        if (this.kontingentAngebotIterator == null || !this.kontingentAngebotIterator.hasNext()) {
            this.nextTransportKontingent();
        }

        if (this.kontingentAngebotIterator != null) {
            this.currentKontingentAngebot = this.kontingentAngebotIterator.next();
        }
    }


    private void nextTransportKontingent() {
        if (this.transportKontingentIterator == null || !this.transportKontingentIterator.hasNext()) {
            this.nextFahrtAbschnitt();
        }

        if (this.transportKontingentIterator != null) {
            this.currentTransportKontingent = this.transportKontingentIterator.next();
            this.kontingentAngebotIterator = (this.currentTransportKontingent.getAngebote() != null)
                ? this.currentTransportKontingent.getAngebote().iterator() : null;
        }
    }


    private void nextFahrtAbschnitt() {
        if (this.fahrtAbschnittIterator == null || !this.fahrtAbschnittIterator.hasNext()) {
            this.nextFahrt();
        }

        if (this.fahrtAbschnittIterator != null) {
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
