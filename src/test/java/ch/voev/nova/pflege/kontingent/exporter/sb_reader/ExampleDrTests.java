package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ExampleDrTests {
	@BeforeEach
	public void setup() {
	}


	@Test
	public void createDr() {
		TransportKontingentDatenrelease exampleDr = ExampleDr.createDr();

		assertNotNull(exampleDr);
		assertEquals(ExampleDr.createMetaData(), exampleDr.getMetaData());
		assertNotNull(exampleDr.getFahrten());
		assertEquals(2, exampleDr.getFahrten().size());
		assertTrue(exampleDr.getFahrten().contains(ExampleDr.createFahrt1()));
		assertTrue(exampleDr.getFahrten().contains(ExampleDr.createFahrt2()));
	}
}
