package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class InsbecdrConsole {
    private final int RECORD_PAGE_SIZE = 10;
    private final TransportKontingentDatenrelease dr;
    private KontingentIterator kontingentIterator;
    private final KontingentRecordFilter filter = new KontingentRecordFilter();
    private boolean hasPressedQuit = false;
    private boolean showBefahrungsvarianten = false;


    public InsbecdrConsole(TransportKontingentDatenrelease dr) {
        if (dr == null) {
            throw new IllegalArgumentException("Parameter 'dr' must not be empty!");
        }

        this.dr = dr;
        this.initIterator();
    }


    public void run() {
        this.showWelcomeText();

        Scanner scanner = new Scanner(System.in);
        do {
            this.showPrompt();
            String input = scanner.nextLine();
            this.processCommand(input);
        } while(!this.hasPressedQuit);
    }


    private void processCommand(String input) {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand(input);

        if (command.isEmpty()) {
            this.showNextRecords();
            return;
        }

        switch (command.getCommand()) {
            case "h":
                this.showHelpText();
                break;
            case "q":
                this.hasPressedQuit = true;
                break;
            case "d":
                this.filter.setDatum(command);
                break;
            case "v":
                this.filter.setVerwaltung(command);
                break;
            case "f":
                this.filter.setFahrt(command);
                break;
            case "a":
                this.filter.setUic1(command);
                break;
            case "b":
                this.filter.setUic2(command);
                break;
            case "c":
                this.filter.clearFilters();
                break;
            case "r":
                this.initIterator();
                break;
            case "s":
                this.showBefahrungsvarianten = !this.showBefahrungsvarianten;
                break;
            default:
                this.showUnknownCommandText(command);
                break;
        }
    }


    private void showWelcomeText() {
        System.out.println("Press <enter> to show records & 'h' for help.");
    }


    private void showPrompt() {
        String promptText = List.of(
            this.filter.getDatum() == null ? "" : "d=" + this.filter.getDatum().format(DateTimeFormatter.ISO_DATE),
            this.filter.getVerwaltung() == null ? "" : "v=" + this.filter.getVerwaltung(),
            this.filter.getFahrt() == -1 ? "" : "f=" + this.filter.getFahrt(),
            this.filter.getUic1() == -1 ? "" : "a=" + this.filter.getUic1(),
            this.filter.getUic2() == -1 ? "" : "b=" + this.filter.getUic2()
        )
            .stream()
            .filter(x -> !x.isEmpty())
            .collect(Collectors.joining(","))
            + "> ";
        System.out.print(promptText);
    }


    private void showUnknownCommandText(InsbecdrConsoleCommand command) {
        System.out.println("Unknown command '" + command.getCommand() + "'. Press 'h' for help.");
    }


    private void showHelpText() {
        System.out.println(
            "inSBecDR commands:\n"
            + "-----------------\n"
            + " h        : help\n"
            + " q        : quit\n"
            + " d <date> : set date filter (e.g. 'd 2020-09-28')\n"
            + " v <code> : set verwaltung filter (e.g. 'v 11')\n"
            + " f <nr>   : set fahrt filter (e.g. 'f 123')\n"
            + " a <uic>  : set uic1 filter (e.g. 'a 8507000')\n"
            + " b <uic>  : set uic2 filter (e.g. 'b 8500218')\n"
            + " c        : clear all filters\n"
            + " r        : rewind iterator back to beginning\n"
            + " s        : toggle show/hide befahrungsvarianten\n"
            + " <enter>  : show next " + RECORD_PAGE_SIZE + " records"
        );
    }


    private void initIterator() {
        this.kontingentIterator = new KontingentIterator(this.dr);
    }


    private void showNextRecords() {
        int count = 0;
        while (count < RECORD_PAGE_SIZE && kontingentIterator.hasNext()) {
            KontingentRecord record = kontingentIterator.next();
            if (this.passesFilter(record)) {
                String recordText = KontingentRecordPrinter.print(record, this.showBefahrungsvarianten);
                System.out.println(recordText);
                count++;
            }
        }

        if (!kontingentIterator.hasNext()) {
            System.out.println("(no more records, press 'r' to rewind)");
        }
    }


    private boolean passesFilter(KontingentRecord record) {
        if (this.filter.getDatum() != null && record.getFahrt().getDatum() != this.filter.getDatum()) {
            return false;
        }

        if (this.filter.getVerwaltung() != null && !record.getFahrt().getVerwaltungCode().equals(this.filter.getVerwaltung())) {
            return false;
        }

        if (this.filter.getFahrt() > 0 && record.getFahrt().getFahrtNummer() != this.filter.getFahrt()) {
            return false;
        }

        if (this.filter.getUic1() > 0 && record.getFahrtAbschnitt().getHst1Uic() != this.filter.getUic1()) {
            return false;
        }

        if (this.filter.getUic2() > 0 && record.getFahrtAbschnitt().getHst2Uic() != this.filter.getUic2()) {
            return false;
        }

        return true;
    }
}
