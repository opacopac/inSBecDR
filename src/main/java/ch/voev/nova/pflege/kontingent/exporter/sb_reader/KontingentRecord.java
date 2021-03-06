package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;


public class KontingentRecord {
    private final int count;
    private final Fahrt fahrt;
    private final FahrtAbschnitt fahrtAbschnitt;
    private final TransportKontingent transportKontingent;


    public int getCount() { return count; }
    public Fahrt getFahrt() { return fahrt; }
    public FahrtAbschnitt getFahrtAbschnitt() { return fahrtAbschnitt; }
    public TransportKontingent getTransportKontingent() { return transportKontingent; }


    public KontingentRecord(
        int count,
        Fahrt fahrt,
        FahrtAbschnitt fahrtAbschnitt,
        TransportKontingent transportKontingent
    ) {
        this.count = count;
        this.fahrt = fahrt;
        this.fahrtAbschnitt = fahrtAbschnitt;
        this.transportKontingent = transportKontingent;
    }
}
