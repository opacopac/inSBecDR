package com.tschanz.nova.insbecdr;


public class InsbecdrConsoleCommand {
    private boolean isEmpty;
    private String command;
    private String argument;


    public boolean isEmpty() { return isEmpty; }
    public String getCommand() { return command; }
    public String getArgument() { return argument; }


    public InsbecdrConsoleCommand(String value) {
        this.parseCommand(value);
    }


    private void parseCommand(String value) {
        if (value == null || value.length() == 0) {
            this.isEmpty = true;
            return;
        }

        String[] parts = value.split(" ", 2);
        this.command = parts[0];
        this.argument = parts.length > 1 ? parts[1] : null;
    }
}
