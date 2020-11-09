package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Component
public class InsbecdrConsole {
    private final static int MaxRecordsPerPage = 10;
    private TransportKontingentDatenrelease dr;
    private KontingentIterator kontingentIterator;
    private boolean hasPressedQuit = false;
    private boolean showBefahrungsvarianten = false;
    private boolean showTransportkontingente = true;
    @Autowired private KontingentRecordFilter filter;
    @Autowired private ConsoleWriter conWriter;


    public void setDr(TransportKontingentDatenrelease dr) {
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
            case "k":
                this.showTransportkontingente = !this.showTransportkontingente;
                break;
            case "w":
                this.writeRecordsToFile(command);
                break;
            default:
                this.showUnknownCommandText(command);
                break;
        }
    }


    private void showWelcomeText() {
        this.conWriter.println("Press <enter> to show records & 'h' for help.");
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
        this.conWriter.print(promptText);
    }


    private void showUnknownCommandText(InsbecdrConsoleCommand command) {
        this.conWriter.println("Unknown command '" + command.getCommand() + "'. Press 'h' for help.");
    }


    private void showHelpText() {
        this.conWriter.println(
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
            + " k        : toggle show/hide transportkontingente\n"
            + " s        : toggle show/hide befahrungsvarianten\n"
            + " w <file> : write all records to file (e.g. 'w output.txt')\n"
            + " <enter>  : show next " + MaxRecordsPerPage + " records"
        );
    }


    private void initIterator() {
        this.kontingentIterator = new KontingentIterator(this.dr);
    }


    private void showNextRecords() {
        int count = 0;
        while (count < MaxRecordsPerPage && kontingentIterator.hasNext()) {
            KontingentRecord record = kontingentIterator.next();
            if (this.filter.recordPassesFilter(record)) {
                String recordText = KontingentRecordPrinter.print(record, this.showTransportkontingente, this.showBefahrungsvarianten);
                this.conWriter.println(recordText);
                count++;
            }
        }

        if (!kontingentIterator.hasNext()) {
            this.conWriter.println("(no more records, press 'r' to rewind)");
        }
    }


    private void writeRecordsToFile(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null || command.getArgument().isEmpty()) {
            this.conWriter.println("missing or invalid filename");
            return;
        }

        FileWriter fileWriter = null;
        try {
            this.conWriter.println("writing to file '" + command.getArgument() + "'...");
            fileWriter = new FileWriter(command.getArgument());
            int count = 0;
            while (kontingentIterator.hasNext()) {
                KontingentRecord record = kontingentIterator.next();
                if (this.filter.recordPassesFilter(record)) {
                    String recordText = KontingentRecordPrinter.print(record, this.showTransportkontingente, this.showBefahrungsvarianten);
                    fileWriter.write(recordText + "\n");
                    count++;
                }
            }
            fileWriter.close();
            this.conWriter.println("successfully written " + count + " records.");
        } catch (IOException exception) {
            this.conWriter.println("error writing to file: " + exception.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException exception) {
                this.conWriter.println("error writing to file: " + exception.getMessage());
            }
        }
    }
}
