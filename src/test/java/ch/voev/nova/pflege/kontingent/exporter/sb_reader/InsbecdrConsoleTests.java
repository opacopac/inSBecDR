package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.Ebene;
import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsbecdrConsoleTests {
    private final TransportKontingent kontingent = new TransportKontingent("1", Collections.emptyList(), Ebene.ZAS, List.of(84004), Collections.emptyList());
    private final FahrtAbschnitt abschnitt = new FahrtAbschnitt("2", 8507000, 8500218, List.of(kontingent));
    private final Fahrt fahrt = new Fahrt("3", LocalDate.of(2020, 11, 10), 100, "000011", List.of(abschnitt));
    private final TransportKontingentDatenrelease dr = new TransportKontingentDatenrelease(null, List.of(fahrt), Collections.emptyList(), Collections.emptyList());

    private SpyConsoleReader spyReader;
    private SpyConsoleWriter spyWriter;
    private SpyFileIOFactory spyFileFactory;
    private KontingentRecordFilter filter;
    private InsbecdrConsole console;


    @BeforeEach
    public void setup() {
        this.spyReader = new SpyConsoleReader();
        this.spyWriter = new SpyConsoleWriter();
        this.spyFileFactory = new SpyFileIOFactory();
        this.filter = new KontingentRecordFilter();
        this.console = new InsbecdrConsole();
        ReflectionTestUtils.setField(console, "conWriter", this.spyWriter);
        ReflectionTestUtils.setField(console, "conReader", this.spyReader);
        ReflectionTestUtils.setField(console, "fileIOFactory", this.spyFileFactory);
        ReflectionTestUtils.setField(console, "filter", this.filter);
    }


    @Test
    public void setDr__null_throws_error() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.console.setDr(null);
        });
    }


    @Test
    public void run__show_welcome_text_help_text_and_quit() {
        this.spyReader.simulateInput("h\nq\n");
        this.console.run();

        assertTrue(spyWriter.text.length() > 0);
    }


    @Test
    public void run__d_sets_datum_filter() {
        this.spyReader.simulateInput("d 2020-11-12\nq\n");
        this.console.run();

        assertEquals(LocalDate.of(2020, 11, 12), this.filter.getDatum());
    }


    @Test
    public void run__v_sets_verwaltung_filter() {
        this.spyReader.simulateInput("v 000011\nq\n");
        this.console.run();

        assertEquals("000011", this.filter.getVerwaltung());
    }

    @Test
    public void run__f_sets_fahrt_filter() {
        this.spyReader.simulateInput("f 100\nq\n");
        this.console.run();

        assertEquals(100, this.filter.getFahrt());
    }


    @Test
    public void run__a_b_set_uic1_uic2_filter() {
        this.spyReader.simulateInput("a 8507000\nb 8500218\nq\n");
        this.console.run();

        assertEquals(8507000, this.filter.getUic1());
        assertEquals(8500218, this.filter.getUic2());
    }


    @Test
    public void run__c_clears_filters() {
        this.spyReader.simulateInput("a 8507000\nb 8500218\nc\nq\n");
        this.console.run();

        assertEquals(-1, this.filter.getUic1());
        assertEquals(-1, this.filter.getUic2());
    }


    @Test
    public void run__s_turns_on_befahrungsvarianten() {
        this.spyReader.simulateInput("s\nq\n");
        this.console.run();

        Object flag = ReflectionTestUtils.getField(this.console, "showBefahrungsvarianten");

        assertEquals(true, flag);
    }


    @Test
    public void run__k_turns_off_kontingente() {
        this.spyReader.simulateInput("k\nq\n");
        this.console.run();

        Object flag = ReflectionTestUtils.getField(this.console, "showTransportkontingente");

        assertEquals(false, flag);
    }


    @Test
    public void run__y_shows_unknown_command() {
        this.spyReader.simulateInput("y\nq\n");
        this.console.run();

        assertTrue(this.spyWriter.text.toLowerCase().contains("unknown"));
    }


    @Test
    public void run__enter_shows_next_fahrten() {
        this.spyReader.simulateInput("\nq\n");
        this.console.setDr(this.dr);
        this.console.run();

        assertTrue(this.spyWriter.text.contains(this.fahrt.getVerwaltungCode()));
        assertTrue(this.spyWriter.text.contains(this.fahrt.getFahrtNummer().toString()));
        assertTrue(this.spyWriter.text.contains(this.abschnitt.getHst1Uic().toString()));
        assertTrue(this.spyWriter.text.contains(this.abschnitt.getHst2Uic().toString()));
    }


    @Test
    public void writeRecordsToFile__contains_data_from_fahrten() {
        this.spyReader.simulateInput("w test.txt\nq\n");
        this.console.setDr(this.dr);;
        this.console.run();

        assertEquals("test.txt", this.spyFileFactory.openedFilename);
        assertTrue(this.spyFileFactory.spyWriter.text.contains(this.fahrt.getVerwaltungCode()));
        assertTrue(this.spyFileFactory.spyWriter.text.contains(this.fahrt.getFahrtNummer().toString()));
        assertTrue(this.spyFileFactory.spyWriter.text.contains(this.abschnitt.getHst1Uic().toString()));
        assertTrue(this.spyFileFactory.spyWriter.text.contains(this.abschnitt.getHst2Uic().toString()));
    }
}
