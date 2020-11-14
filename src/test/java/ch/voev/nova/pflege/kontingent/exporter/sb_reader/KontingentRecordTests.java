package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.Ebene;
import ch.voev.nova.pflege.kontingent.sb.api.Fahrt;
import ch.voev.nova.pflege.kontingent.sb.api.FahrtAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


class KontingentRecordTests {
	@BeforeEach
	public void setup() {
	}


	@Test
	public void create() {
		TransportKontingent kontingent = new TransportKontingent("1", Collections.emptyList(), Ebene.ZAS, List.of(84004), Collections.emptyList());
		FahrtAbschnitt abschnitt = new FahrtAbschnitt("2", 8507000, 8500218, List.of(kontingent));
		Fahrt fahrt = new Fahrt("3", LocalDate.of(2020, 11, 10), 100, "000011", List.of(abschnitt));

		KontingentRecord record = new KontingentRecord(1, fahrt, abschnitt, kontingent);

		assertEquals(1, record.getCount());
		assertEquals(fahrt, record.getFahrt());
		assertEquals(abschnitt, record.getFahrtAbschnitt());
		assertEquals(kontingent, record.getTransportKontingent());
	}
}
