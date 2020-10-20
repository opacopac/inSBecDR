package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.BefahrungsVariante;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
        this.showDrSummary(dr);

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


    private void showDrSummary(TransportKontingentDatenrelease dr) {
        Collection<Fahrt> fahrten = dr.getFahrten() != null ? dr.getFahrten() : Collections.emptyList();
        System.out.println("Anz Fahrten: " + fahrten.size());

        Collection<String> verwaltungen = fahrten
            .stream()
            .map(Fahrt::getVerwaltungCode)
            .collect(Collectors.toSet())
            .stream().sorted().collect(Collectors.toList());
        System.out.println("Anz Verwaltungen: " + verwaltungen.size() + ": " + verwaltungen.toString());

        Collection<LocalDate> dates = fahrten
                .stream()
                .map(Fahrt::getDatum)
                .collect(Collectors.toSet())
                .stream().sorted().collect(Collectors.toList());
        System.out.println("Anz Tage: " + dates.size() + ": " + dates.toString());

        Collection<Rabattstufe> rabattstufen = dr.getRabattstufen() != null ? dr.getRabattstufen() : Collections.emptyList();
        System.out.println("Anz Rabattstufen: " + rabattstufen.size());

        Collection<BefahrungsVariante> befahrungsVarianten = dr.getBefahrungsVarianten() != null ? dr.getBefahrungsVarianten() : Collections.emptyList();
        System.out.println("Anz Befahrungsvarianten: " + befahrungsVarianten.size());
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
