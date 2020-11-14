package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.*;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.BefahrungsVariante;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.TransportAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.KlasseTyp;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


class KontingentRecordPrinterTests {


	@BeforeEach
	public void setup() {
	}


	@Test
	public void print_full_entry() {
		TransportAbschnitt transportAbschnitt = new TransportAbschnitt("1", 100, 8507000, 8500218, "000011");
		BefahrungsVariante befahrungsVariante = new BefahrungsVariante("2", List.of(transportAbschnitt));
		Rabatt rabatt = new Rabatt("1", List.of(KlasseTyp.KLASSE_2), List.of("1/2"), BigDecimal.valueOf(10), 5);
		Rabattstufe rabattstufe = new Rabattstufe("1", "RS3", 3, List.of(rabatt));
		KontingentAngebot angebot = new KontingentAngebot("1", 20, rabattstufe);
		TransportKontingent kontingent = new TransportKontingent("1", List.of(befahrungsVariante), Ebene.ZAS, List.of(84004), List.of(angebot));
		FahrtAbschnitt abschnitt = new FahrtAbschnitt("2", 8507000, 8500218, List.of(kontingent));
		Fahrt fahrt = new Fahrt("3", LocalDate.of(2020, 11, 10), 100, "000011", List.of(abschnitt));
		KontingentRecord record = new KontingentRecord(1, fahrt, abschnitt, kontingent);

		String result1 = KontingentRecordPrinter.print(record, true, true);
		String result2 = KontingentRecordPrinter.print(record, true, false);
		String result3 = KontingentRecordPrinter.print(record, false, true);
		String result4 = KontingentRecordPrinter.print(record, false, false);

		assertTrue(result1.length() > result2.length());
		assertTrue(result1.length() > result3.length());
		assertTrue(result2.length() > result4.length());
		assertTrue(result3.length() > result4.length());
	}


	@Test
	public void print_entry_without_befahrungsvariante_without_kontingent_angebote() {
		TransportKontingent kontingent = new TransportKontingent("1", Collections.emptyList(), Ebene.ZAS, List.of(84004), Collections.emptyList());
		FahrtAbschnitt abschnitt = new FahrtAbschnitt("2", 8507000, 8500218, List.of(kontingent));
		Fahrt fahrt = new Fahrt("3", LocalDate.of(2020, 11, 10), 100, "000011", List.of(abschnitt));
		KontingentRecord record = new KontingentRecord(1, fahrt, abschnitt, kontingent);


		String result = KontingentRecordPrinter.print(record, true, true);

		assertNotNull(result);
	}
}
