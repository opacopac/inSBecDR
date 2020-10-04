package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Collection;

import static java.lang.System.exit;


@SpringBootApplication
public class InsbecdrApplication implements CommandLineRunner {
    @Autowired
    private SbDrLoader sbDrLoader;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InsbecdrApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }


    @Override
    public void run(String... args) {
        this.showWelcomeText();
        if (args == null || args.length == 0) {
            this.showInvalidArgumentsText();
            this.showByeByeText();
            return;
        }

        TransportKontingentDatenrelease dr = this.loadDr(args[0]);
        if (dr == null) {
            this.showByeByeText();
            exit(1);
        }
        this.showAnzahlFahrten(dr.getFahrten());

        InsbecdrConsole console = new InsbecdrConsole(dr);
        console.run();

        this.showByeByeText();
        exit(0);
    }


    private void showWelcomeText() {
        System.out.println("\n");
        System.out.println("Welcome to inSBecDR V1.0!");
        System.out.println("=========================");
    }


    private void showInvalidArgumentsText() {
        System.out.println("Missing argument: please specify <file name> or <url> of a Sparbillett DR or '-mock' to use mock data!");
    }


    private void showAnzahlFahrten(Collection<Fahrt> fahrten) {
        System.out.println("Anz Fahrten: " + (fahrten != null ? fahrten.size() : 0));
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
                System.out.println("Reading DR " + argument + "...");
                TransportKontingentDatenrelease dr = this.sbDrLoader.load(argument);
                System.out.println("Done.");
                return dr;
            } catch (IOException exception) {
                System.out.println("Error: reading DR '" + argument + "': " + exception.getMessage());
                return null;
            }
        }
    }
}
