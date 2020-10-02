package com.tschanz.nova.insbecdr;

import java.time.LocalDate;


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
        if (command.getArgument() == null) {
            this.showEmptyArgumentText(command);
        }

        this.datum = LocalDate.parse(command.getArgument());
    }


    public void setVerwaltung(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null) {
            this.showEmptyArgumentText(command);
        }

        this.verwaltung = command.getArgument();
    }


    public void setFahrt(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null) {
            this.showEmptyArgumentText(command);
        }

        this.fahrt = Integer.parseInt(command.getArgument());
    }


    public void setUic1(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null) {
            this.showEmptyArgumentText(command);
        }

        this.uic1 = Integer.parseInt(command.getArgument());
    }


    public void setUic2(InsbecdrConsoleCommand command) {
        if (command.getArgument() == null) {
            this.showEmptyArgumentText(command);
        }

        this.uic2 = Integer.parseInt(command.getArgument());
    }


    public void clearFilters() {
        this.datum = null;
        this.verwaltung = null;
        this.fahrt = -1;
        this.uic1 = -1;
        this.uic2 = -1;
    }


    private void showEmptyArgumentText(InsbecdrConsoleCommand command) {
        System.out.println("Missing argument for command '" + command.getCommand() + "'. Press 'h' for help.");
    }
}
