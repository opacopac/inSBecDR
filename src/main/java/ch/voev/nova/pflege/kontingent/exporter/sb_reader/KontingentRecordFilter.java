package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Service
public class KontingentRecordFilter {
    @Autowired private ConsoleWriter conWriter;
    private LocalDate datum = null;
    private String verwaltung = null;
    private int fahrt = -1;
    private int uic1 = -1;
    private int uic2 = -1;


    public LocalDate getDatum() { return datum; }
    public String getVerwaltung() { return verwaltung; }
    public int getFahrt() { return fahrt; }
    public int getUic1() { return uic1; }
    public int getUic2() { return uic2; }


    public void setDatum(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null || command.getArgument().isEmpty()) {
            this.showInvalidArgumentText(command);
        } else {
            try {
                this.datum = LocalDate.parse(command.getArgument());
            } catch (DateTimeParseException exception) {
                this.showInvalidArgumentText(command);
            }
        }
    }


    public void setVerwaltung(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null || command.getArgument().isEmpty()) {
            this.showInvalidArgumentText(command);
        } else {
            this.verwaltung = command.getArgument();
        }
    }


    public void setFahrt(InsbecdrConsoleCommand command) {
        try {
            this.fahrt = Integer.parseInt(command.getArgument());
        } catch (NumberFormatException exception) {
            this.showInvalidArgumentText(command);
        }
    }


    public void setUic1(InsbecdrConsoleCommand command) {
        try {
            this.uic1 = Integer.parseInt(command.getArgument());
        } catch (NumberFormatException exception) {
            this.showInvalidArgumentText(command);
        }
    }


    public void setUic2(InsbecdrConsoleCommand command) {
        try {
            this.uic2 = Integer.parseInt(command.getArgument());
        } catch (NumberFormatException exception) {
            this.showInvalidArgumentText(command);
        }
    }


    public void clearFilters() {
        this.datum = null;
        this.verwaltung = null;
        this.fahrt = -1;
        this.uic1 = -1;
        this.uic2 = -1;
    }




    public boolean recordPassesFilter(KontingentRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("record must not be null");
        }

        // filter by date
        if (this.getDatum() != null) {
            if (record.getFahrt() == null || record.getFahrt().getDatum() == null || !record.getFahrt().getDatum().equals(this.getDatum())) {
                return false;
            }
        }

        // filter by verwaltung
        if (this.getVerwaltung() != null) {
            if (record.getFahrt() == null || record.getFahrt().getVerwaltungCode() == null || !record.getFahrt().getVerwaltungCode().equals(this.getVerwaltung())) {
                return false;
            }
        }

        // filter by fahrtnummer
        if (this.getFahrt() > 0) {
            if (record.getFahrt() == null || record.getFahrt().getFahrtNummer() == null || record.getFahrt().getFahrtNummer() != this.getFahrt()) {
                return false;
            }
        }

        // filter by uic1
        if (this.getUic1() > 0) {
            if (record.getFahrtAbschnitt() == null || record.getFahrtAbschnitt().getHst1Uic() == null || record.getFahrtAbschnitt().getHst1Uic() != this.getUic1()) {
                return false;
            }
        }

        // filter by uic2
        if (this.getUic2() > 0) {
            if (record.getFahrtAbschnitt() == null || record.getFahrtAbschnitt().getHst2Uic() == null || record.getFahrtAbschnitt().getHst2Uic() != this.getUic2()) {
                return false;
            }
        }

        return true;
    }


    private void showInvalidArgumentText(InsbecdrConsoleCommand command) {
        this.conWriter.println("Missing or invalid argument for command '" + command.getCommand() + "'. Press 'h' for help.");
    }
}
