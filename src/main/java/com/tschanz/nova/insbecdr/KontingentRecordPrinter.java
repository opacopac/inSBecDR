package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.KontingentAngebot;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


public class KontingentRecordPrinter {
    public static String print(KontingentRecord record) {
        return printRecordCount(record.getCount()) + " "
            + printFahrt(record.getFahrt()) + " "
            + printFahrtAbschnitt(record.getFahrtAbschnitt()) + " "
            + printTransportKontingent(record.getTransportKontingent()) + " "
            + printKontingentAngebot(record.getKontingentAngebot());
    }


    private static String printRecordCount(int count) {
        return String.format("%08d", count);
    }


    private static String printFahrt(Fahrt fahrt) {
        return "Datum=" + fahrt.getDatum().format(DateTimeFormatter.ISO_DATE) + " "
            + "Verwaltung=" + fahrt.getVerwaltungCode() + " "
            + "Fahrt=" + fahrt.getFahrtNummer();
    }


    private static String printFahrtAbschnitt(FahrtAbschnitt abschnitt) {
        return "Uic1=" + abschnitt.getHst1Uic() + " "
            + "Uic2=" + abschnitt.getHst2Uic();
    }


    private static String printTransportKontingent(TransportKontingent kontingent) {
        return "Produkte=" + kontingent.getProduktNummern() + " "
            + "Ebene=" + kontingent.getEbene();
    }


    private static String printKontingentAngebot(KontingentAngebot angebot) {
        return "Anzahl=" + angebot.getAnzAngebote() + " "
            + printRabattstufe(angebot.getRabattstufe());
    }


    private static String printRabattstufe(Rabattstufe rabattstufe) {
        if (rabattstufe == null) {
            return "[RABATTSTUFE MISSING]";
        }

        String stufeText = (rabattstufe.getRabatte() != null) ?
            rabattstufe.getRabatte()
                .stream()
                .map(KontingentRecordPrinter::printRabatt)
                .collect(Collectors.joining(", "))
            : "[RABATT MISSING]";

        return "StufeId=" + rabattstufe.getIndex() + ": " + stufeText;
    }


    private static String printRabatt(Rabatt rabatt) {
        return rabatt.getKlasse() + " "
            + rabatt.getKundensegmentCode() + " "
            + "Rabatt=" + rabatt.getRabattProzent() + "% "
            + "Verfall=" + rabatt.getVerfallTageVorReise();
    }
}
