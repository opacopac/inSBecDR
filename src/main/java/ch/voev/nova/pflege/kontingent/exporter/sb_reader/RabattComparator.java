package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;

import java.util.Comparator;
import java.util.stream.Collectors;


public class RabattComparator implements Comparator<Rabatt> {
    @Override
    public int compare(Rabatt r1, Rabatt r2) {
        String r1String = this.getKlasseString(r1) + "_" + this.getKundensegmentString(r1);
        String r2String = this.getKlasseString(r2) + "_" + this.getKundensegmentString(r2);

        return r1String.compareTo(r2String);
    }


    private String getKlasseString(Rabatt rabatt) {
        if (rabatt == null || rabatt.getKlasse() == null || rabatt.getKlasse().size() == 0) {
            return "";
        }

        return rabatt.getKlasse()
            .stream()
            .map(Enum::name)
            .sorted()
            .collect(Collectors.joining("_"));
    }


    private String getKundensegmentString(Rabatt rabatt) {
        if (rabatt == null || rabatt.getKundensegmentCode() == null || rabatt.getKundensegmentCode().size() == 0) {
            return "";
        }

        return rabatt.getKundensegmentCode()
            .stream()
            .sorted()
            .collect(Collectors.joining("_"));
    }
}
