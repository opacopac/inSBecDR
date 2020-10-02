package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;


@SpringBootApplication
public class InsbecdrApplication implements CommandLineRunner {
    @Autowired
    private SbDrDeserializer deserializer;


    public static void main(String[] args) {
         SpringApplication.run(InsbecdrApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        this.showWelcomeText();
        if (args == null || args.length == 0) {
            this.showInvalidArgumentsText();
            this.showByeByeText();
            return;
        }

        TransportKontingentDatenrelease dr = this.loadDr(args[0]);
        if (dr == null) {
            this.showByeByeText();
            return;
        }
        this.showAnzahlFahrten(dr.getFahrten().size());

        KontingentIterator kontingentIterator = new KontingentIterator(dr);
        InsbecdrConsole console = new InsbecdrConsole(kontingentIterator);
        console.run();

        this.showByeByeText();
    }


    private void showWelcomeText() {
        System.out.println("Welcome to inSBecDR V1.0!");
        System.out.println("=========================");
    }


    private void showInvalidArgumentsText() {
        System.out.println("Missing argument: please specify <file name> of SB DR or '-mock' to use mock data!");
    }


    private void showAnzahlFahrten(int anzFahrten) {
        System.out.println("Anz Fahrten: " + anzFahrten);
    }


    private void showByeByeText() {
        System.out.println("Exiting.");
    }


    private TransportKontingentDatenrelease loadDr(String argument) {
        if (argument.toLowerCase().equals("-mock")) {
            System.out.println("Reading Mock DR...");
            return MockDr1.createDr();
        } else {
            try {
                System.out.println("Reading DR file " + argument + "...");
                return this.deserializer.deserialize(argument);
            } catch (FileNotFoundException exception) {
                System.out.println("Error: SB DR File '" + argument + "' not found!");
                return null;
            }
        }
    }
}
