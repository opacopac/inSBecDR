package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.BefahrungsVariante;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;
import com.esotericsoftware.kryo.KryoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.lang.System.exit;


@SpringBootApplication
public class InsbecdrApplication implements CommandLineRunner {
    private Properties properties;
    @Autowired private ConsoleWriter conWriter;
    @Autowired private SbDrLoader sbDrLoader;
    @Autowired private InsbecdrConsole console;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InsbecdrApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }


    @Override
    public void run(String... args) throws IOException {
        this.properties = this.loadProperties();

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

        this.console.setDr(dr);
        this.console.run();

        this.showByeByeText();
        exit(0);
    }


    private Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "application.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        return prop;
    }


    private void showWelcomeText() {
        this.conWriter.println("\n\n");
        this.conWriter.println("Welcome to inSBecDR " + this.properties.get("insbecdr.version"));
        this.conWriter.println("=========================");
        this.conWriter.println("Compatible interface version: " + this.properties.get("sb_dr_interface.version"));
        this.conWriter.println("\n");
    }


    private void showInvalidArgumentsText() {
        this.conWriter.println("Missing argument: please specify <file name> or <url> of a Sparbillett DR or '-mock' to use mock data!");
    }


    private void showDrSummary(TransportKontingentDatenrelease dr) {
        Collection<Fahrt> fahrten = dr.getFahrten() != null ? dr.getFahrten() : Collections.emptyList();
        this.conWriter.println("Anz Fahrten: " + fahrten.size());

        Collection<String> verwaltungen = fahrten
            .stream()
            .map(Fahrt::getVerwaltungCode)
            .collect(Collectors.toSet())
            .stream().sorted().collect(Collectors.toList());
        this.conWriter.println("Anz Verwaltungen: " + verwaltungen.size() + ": " + verwaltungen.toString());

        Collection<LocalDate> dates = fahrten
                .stream()
                .map(Fahrt::getDatum)
                .collect(Collectors.toSet())
                .stream().sorted().collect(Collectors.toList());
        this.conWriter.println("Anz Tage: " + dates.size() + ": " + dates.toString());

        Collection<Rabattstufe> rabattstufen = dr.getRabattstufen() != null ? dr.getRabattstufen() : Collections.emptyList();
        this.conWriter.println("Anz Rabattstufen: " + rabattstufen.size());

        Collection<BefahrungsVariante> befahrungsVarianten = dr.getBefahrungsVarianten() != null ? dr.getBefahrungsVarianten() : Collections.emptyList();
        this.conWriter.println("Anz Befahrungsvarianten: " + befahrungsVarianten.size());
    }


    private void showByeByeText() {
        this.conWriter.println("Exiting.");
    }


    private TransportKontingentDatenrelease loadDr(String argument) {
        if (argument.toLowerCase().equals("-mock")) {
            this.conWriter.println("Reading Mock DR...");
            return MockDr.createDr();
        } else {
            try {
                this.conWriter.println("Reading DR " + argument + "...");
                TransportKontingentDatenrelease dr = this.sbDrLoader.load(argument);
                this.conWriter.println("Done.\n");
                return dr;
            } catch (IOException exception) {
                this.conWriter.println("Error: opening DR file '" + argument + "': " + exception.getMessage());
                return null;
            } catch (KryoException exception2) {
                this.conWriter.println("Error: deserializing DR file '" + argument + "': " + exception2.getMessage());
                this.conWriter.println("  (only files with interface version " + this.properties.get("sb_dr_interface.version") +  " supported)");
                return null;
            }
        }
    }
}
