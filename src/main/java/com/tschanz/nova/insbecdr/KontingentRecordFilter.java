package com.tschanz.nova.insbecdr;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class KontingentRecordFilter {
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


    public KontingentRecordFilter() {
    }


    public void setDatum(InsbecdrConsoleCommand command) {
        try {
            this.datum = LocalDate.parse(command.getArgument());
        } catch (DateTimeParseException | NullPointerException exception) {
            this.showInvalidArgumentText(command);
        }
    }


    public void setVerwaltung(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null || command.getArgument().isEmpty()) {
            this.showInvalidArgumentText(command);
        }

        this.verwaltung = command.getArgument();
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


    private void showInvalidArgumentText(InsbecdrConsoleCommand command) {
        System.out.println("Missing or invalid argument for command '" + command.getCommand() + "'. Press 'h' for help.");
    }
}
