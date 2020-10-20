package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.KontingentAngebot;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.BefahrungsVariante;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.TransportAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public final class KontingentRecordPrinter {
    private KontingentRecordPrinter() {
    }


    public static String print(KontingentRecord record, boolean showTransportkontingente, boolean showBefahrungsvarianten) {
        return printRecordCount(record.getCount()) + " "
            + printFahrt(record.getFahrt()) + " "
            + printFahrtAbschnitt(record.getFahrtAbschnitt()) + " "
            + printTransportKontingent(record.getTransportKontingent(), showTransportkontingente, showBefahrungsvarianten);
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


    private static String printTransportKontingent(TransportKontingent kontingent, boolean showTransportkontingente, boolean showBefahrungsvarianten) {
        if (kontingent == null) {
            return "[NO KONTINGENTE]";
        }

        return "Produkte=" + kontingent.getProduktNummern() + " "
            + "Ebene=" + kontingent.getEbene()
            + " Befahrungsvarianten=" + (kontingent.getBefahrungsvariante() == null ? 0 : kontingent.getBefahrungsvariante().size())
            + " Kontingente=" + (kontingent.getAngebote() == null ? 0 : kontingent.getAngebote().size())
            + (showBefahrungsvarianten ? "\n" + printBefahrungsvarianteList(kontingent.getBefahrungsvariante()) : "")
            + (showTransportkontingente ? "\n" + printKontingentAngebotList(kontingent.getAngebote()) : "");
    }


    private static String printKontingentAngebotList(List<KontingentAngebot> angebotList) {
        if (angebotList == null || angebotList.size() == 0) {
            return "[NO ANGEBOTE]";
        }

        return angebotList
            .stream()
            .map(KontingentRecordPrinter::printKontingentAngebot)
            .collect(Collectors.joining("\n"));
    }


    private static String printKontingentAngebot(KontingentAngebot angebot) {
        return "  Anzahl=" + angebot.getAnzAngebote() + " "
            + printRabattstufe(angebot.getRabattstufe());
    }


    private static String printRabattstufe(Rabattstufe rabattstufe) {
        if (rabattstufe == null) {
            return "[RABATTSTUFE MISSING]";
        }

        return "Code=" + rabattstufe.getCode() + ": "
            + printRabattList(rabattstufe.getRabatte());
    }


    private static String printRabattList(Collection<Rabatt> rabattList) {
        if (rabattList == null || rabattList.size() == 0) {
            return "[NO RABATTE]";
        }

        return rabattList
            .stream()
            .map(KontingentRecordPrinter::printRabatt)
            .collect(Collectors.joining(", "));
    }


    private static String printRabatt(Rabatt rabatt) {
        return rabatt.getKlasse() + " "
            + rabatt.getKundensegmentCode() + " "
            + "Rabatt=" + rabatt.getRabattProzent() + "% "
            + "Verfall=" + rabatt.getVerfallTageVorReise();
    }


    private static String printBefahrungsvarianteList(Collection<BefahrungsVariante> befahrungsVarianteList) {
        if (befahrungsVarianteList == null || befahrungsVarianteList.size() == 0) {
            return "";
        }

        return befahrungsVarianteList
            .stream()
            .map(KontingentRecordPrinter::printBefahrungsvariante)
            .collect(Collectors.joining("\n")) + "\n";
    }


    private static String printBefahrungsvariante(BefahrungsVariante befahrungsVariante) {
        return "  Befahrungsvariante [Verwaltung, Fahrt, Uic1, Uic2]: "
            + printTransportAbschnittList(befahrungsVariante.getAbschnitte());
    }


    private static String printTransportAbschnittList(List<TransportAbschnitt> transportAbschnittList) {
        if (transportAbschnittList == null || transportAbschnittList.size() == 0) {
            return "[NO TRANSPORTABSCHNITTE]";
        }

        return transportAbschnittList
            .stream()
            .map(KontingentRecordPrinter::printTransportAbschnitt)
            .collect(Collectors.joining(", "));
    }


    private static String printTransportAbschnitt(TransportAbschnitt transportAbschnitt) {
        return "[" + transportAbschnitt.getVerwaltungCode() + ", "
            + transportAbschnitt.getFahrtNummer() + ", "
            + transportAbschnitt.getHst1Uic() + ", "
            + transportAbschnitt.getHst2Uic() + "]";
    }
}
