package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.Ebene;
import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


public class KontingentRecordFilterTests {
    private SpyConsoleWriter spyWriter;
    private KontingentRecordFilter filter;
    private TransportKontingent kontingent = new TransportKontingent("1", Collections.emptyList(), Ebene.ZAS, List.of(84004), Collections.emptyList());
    private FahrtAbschnitt abschnitt = new FahrtAbschnitt("2", 8507000, 8500218, Collections.emptyList());
    private Fahrt fahrt = new Fahrt("3", LocalDate.of(2020, 11, 10), 100, "000011", List.of(abschnitt));
    private KontingentRecord record = new KontingentRecord(1, fahrt, abschnitt, kontingent);



    @BeforeEach
    public void setup() {
        this.spyWriter = new SpyConsoleWriter();
        this.filter = new KontingentRecordFilter();
        ReflectionTestUtils.setField(filter, "conWriter", spyWriter);
    }

    
    // region setDatum

    @Test
    public void setDatum__happy_day() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("d 2020-11-10");
        this.filter.setDatum(command);
        assertEquals(LocalDate.of(2020, 11, 10), this.filter.getDatum());
        assertEquals(0, spyWriter.text.length());
    }


    @Test
    public void setDatum__no_argument() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("d");
        this.filter.setDatum(command);
        assertNull(this.filter.getDatum());
        assertTrue(spyWriter.text.length() > 0);
    }


    @Test
    public void setDatum__invalid_format() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("d 2020-99-WTF");
        this.filter.setDatum(command);
        assertNull(this.filter.getDatum());
        assertTrue(spyWriter.text.length() > 0);
    }
    
    // endregion

    
    // region setVerwaltung

    @Test
    public void setVerwaltung__happy_day() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("v 000011");
        this.filter.setVerwaltung(command);
        assertEquals("000011", this.filter.getVerwaltung());
        assertEquals(0, spyWriter.text.length());
    }


    @Test
    public void setVerwaltung__no_argument() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("v");
        this.filter.setVerwaltung(command);
        assertNull(this.filter.getVerwaltung());
        assertTrue(spyWriter.text.length() > 0);
    }
    
    // endregion

    
    // region setFahrt

    @Test
    public void setFahrt__happy_day() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("f 100");
        this.filter.setFahrt(command);
        assertEquals(100, this.filter.getFahrt());
        assertEquals(0, spyWriter.text.length());
    }


    @Test
    public void setFahrt__no_argument() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("f");
        this.filter.setFahrt(command);
        assertEquals(-1, this.filter.getFahrt());
        assertTrue(spyWriter.text.length() > 0);
    }


    @Test
    public void setFahrt__invalid_format() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("f MEEP");
        this.filter.setFahrt(command);
        assertEquals(-1, this.filter.getFahrt());
        assertTrue(spyWriter.text.length() > 0);
    }
    
    // endregion


    // region setUic1

    @Test
    public void setUic1__happy_day() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a 8507000");
        this.filter.setUic1(command);
        assertEquals(8507000, this.filter.getUic1());
        assertEquals(0, spyWriter.text.length());
    }


    @Test
    public void setUic1__no_argument() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a");
        this.filter.setUic1(command);
        assertEquals(-1, this.filter.getUic1());
        assertTrue(spyWriter.text.length() > 0);
    }


    @Test
    public void setUic1__invalid_format() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a MEEP");
        this.filter.setUic1(command);
        assertEquals(-1, this.filter.getUic1());
        assertTrue(spyWriter.text.length() > 0);
    }

    // endregion


    // region setUic2

    @Test
    public void setUic2__happy_day() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a 8500218");
        this.filter.setUic2(command);
        assertEquals(8500218, this.filter.getUic2());
        assertEquals(0, spyWriter.text.length());
    }


    @Test
    public void setUic2__no_argument() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a");
        this.filter.setUic2(command);
        assertEquals(-1, this.filter.getUic2());
        assertTrue(spyWriter.text.length() > 0);
    }


    @Test
    public void setUic2__invalid_format() {
        InsbecdrConsoleCommand command = new InsbecdrConsoleCommand("a MEEP");
        this.filter.setUic2(command);
        assertEquals(-1, this.filter.getUic2());
        assertTrue(spyWriter.text.length() > 0);
    }

    // endregion


    // region clearFilters

    @Test
    public void clearFilters__set_multiple_filters_then_clear_all() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("d 2020-10-10");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("v 000011");
        InsbecdrConsoleCommand command3 = new InsbecdrConsoleCommand("f 100");

        this.filter.setDatum(command1);
        this.filter.setVerwaltung(command2);
        this.filter.setFahrt(command3);

        assertEquals(LocalDate.of(2020, 10, 10), this.filter.getDatum());
        assertEquals("000011", this.filter.getVerwaltung());
        assertEquals(100, this.filter.getFahrt());

        this.filter.clearFilters();

        assertNull(this.filter.getDatum());
        assertNull(this.filter.getVerwaltung());
        assertEquals(-1, this.filter.getFahrt());
        assertEquals(0, spyWriter.text.length());
    }

    // endregion


    // region recordPassesFilter

    @Test
    public void recordPassesFilter__datum() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("d 2020-11-10");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("d 2020-10-10");

        this.filter.setDatum(command1);
        boolean result1 = this.filter.recordPassesFilter(record);
        this.filter.setDatum(command2);
        boolean result2 = this.filter.recordPassesFilter(record);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void recordPassesFilter__verwaltung() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("v 000011");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("v 000801");

        this.filter.setVerwaltung(command1);
        boolean result1 = this.filter.recordPassesFilter(record);
        this.filter.setVerwaltung(command2);
        boolean result2 = this.filter.recordPassesFilter(record);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void recordPassesFilter__fahrt() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("f 100");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("f 999");

        this.filter.setFahrt(command1);
        boolean result1 = this.filter.recordPassesFilter(record);
        this.filter.setFahrt(command2);
        boolean result2 = this.filter.recordPassesFilter(record);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void recordPassesFilter__uic1() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("a 8507000");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("a 8500218");

        this.filter.setUic1(command1);
        boolean result1 = this.filter.recordPassesFilter(record);
        this.filter.setUic1(command2);
        boolean result2 = this.filter.recordPassesFilter(record);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void recordPassesFilter__uic2() {
        InsbecdrConsoleCommand command1 = new InsbecdrConsoleCommand("b 8500218");
        InsbecdrConsoleCommand command2 = new InsbecdrConsoleCommand("b 8507000");

        this.filter.setUic2(command1);
        boolean result1 = this.filter.recordPassesFilter(record);
        this.filter.setUic2(command2);
        boolean result2 = this.filter.recordPassesFilter(record);

        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    public void recordPassesFilter__null_throws_error() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.filter.recordPassesFilter(null);
        });
    }


    // endregion
}
