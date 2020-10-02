package com.tschanz.nova.insbecdr;

import java.util.Scanner;


public class InsbecdrConsole {
    private final int RECORD_PAGE_SIZE = 40;
    private final KontingentIterator kontingentIterator;
    private final KontingentRecordFilter filter = new KontingentRecordFilter();
    private boolean hasPressedQuit = false;


    public InsbecdrConsole(KontingentIterator kontingentIterator) {
        if (kontingentIterator == null) {
            throw new IllegalArgumentException("Parameter 'kontingentIterator' must not be empty!");
        }

        this.kontingentIterator = kontingentIterator;
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
            case "r":
                this.filter.reset();
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
        System.out.print("> ");
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
            + " r        : reset all filters\n"
            + " <enter>  : show next " + RECORD_PAGE_SIZE + " records"
        );
    }


    private void showNextRecords() {
        int count = 0;
        while (count < RECORD_PAGE_SIZE && kontingentIterator.hasNext()) {
            KontingentRecord record = kontingentIterator.next();
            if (this.passesFilter(record)) {
                String recordText = KontingentRecordPrinter.print(record);
                System.out.println(recordText);
                count++;
            }
        }

        if (!kontingentIterator.hasNext()) {
            System.out.println("(no more records)");
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
