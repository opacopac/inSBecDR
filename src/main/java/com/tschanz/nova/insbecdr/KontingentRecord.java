package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.KontingentAngebot;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;


public class KontingentRecord {
    private final int count;
    private final Fahrt fahrt;
    private final FahrtAbschnitt fahrtAbschnitt;
    private final TransportKontingent transportKontingent;
    private final KontingentAngebot kontingentAngebot;


    public int getCount() { return count; }
    public Fahrt getFahrt() { return fahrt; }
    public FahrtAbschnitt getFahrtAbschnitt() { return fahrtAbschnitt; }
    public KontingentAngebot getKontingentAngebot() { return kontingentAngebot; }
    public TransportKontingent getTransportKontingent() { return transportKontingent; }


    public KontingentRecord(
        int count,
        Fahrt fahrt,
        FahrtAbschnitt fahrtAbschnitt,
        TransportKontingent transportKontingent,
        KontingentAngebot kontingentAngebot
    ) {
        this.count = count;
        this.fahrt = fahrt;
        this.fahrtAbschnitt = fahrtAbschnitt;
        this.transportKontingent = transportKontingent;
        this.kontingentAngebot = kontingentAngebot;
    }
}
