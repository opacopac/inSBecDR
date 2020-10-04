package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;


class KontingentIteratorTests {
	private KontingentIterator iterator;


	@BeforeEach
	public void setup() {
		TransportKontingentDatenrelease mockDr = MockDr1.createDr();
		this.iterator = new KontingentIterator(mockDr);
	}


	@Test
	public void initial_next_returns_first_record() {
		KontingentRecord result = this.iterator.next();

		assertEquals(result.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result.getTransportKontingent(), MockDr1.createTransportKontingent1());
	}


	@Test
	public void next_iterates_correctly_over_multiple_records() {
		KontingentRecord result1 = this.iterator.next();
		KontingentRecord result2 = this.iterator.next();
		KontingentRecord result3 = this.iterator.next();
		KontingentRecord result4 = this.iterator.next();
		KontingentRecord result5 = this.iterator.next();
		KontingentRecord result6 = this.iterator.next();
		KontingentRecord result7 = this.iterator.next();
		KontingentRecord result8 = this.iterator.next();
		KontingentRecord result9 = this.iterator.next();

		// result1
		assertEquals(result1.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result1.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result1.getTransportKontingent(), MockDr1.createTransportKontingent1());

		// result2
		assertEquals(result2.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result2.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result2.getTransportKontingent(), MockDr1.createTransportKontingent1());

		// result3
		assertEquals(result3.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result3.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result3.getTransportKontingent(), MockDr1.createTransportKontingent2());

		// result4
		assertEquals(result4.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result4.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result4.getTransportKontingent(), MockDr1.createTransportKontingent2());

		// result5
		assertEquals(result5.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result5.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt2());
		assertEquals(result5.getTransportKontingent(), MockDr1.createTransportKontingent1());

		// result6
		assertEquals(result6.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result6.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt2());
		assertEquals(result6.getTransportKontingent(), MockDr1.createTransportKontingent1());

		// result7
		assertEquals(result7.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result7.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt2());
		assertEquals(result7.getTransportKontingent(), MockDr1.createTransportKontingent2());

		// result8
		assertEquals(result8.getFahrt(), MockDr1.createFahrt1());
		assertEquals(result8.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt2());
		assertEquals(result8.getTransportKontingent(), MockDr1.createTransportKontingent2());

		// result9
		assertEquals(result9.getFahrt(), MockDr1.createFahrt2());
		assertEquals(result9.getFahrtAbschnitt(), MockDr1.createFahrtAbschnitt1());
		assertEquals(result9.getTransportKontingent(), MockDr1.createTransportKontingent1());
	}


	@Test
	public void initial_hasNext_is_true() {
		assertTrue(this.iterator.hasNext());
	}


	@Test
	public void last_hasNext_is_false() {
		for (int i = 0; i < 16; i++) {
			this.iterator.next();
		}

		assertFalse(this.iterator.hasNext());
	}


	@Test
	public void next_after_end_throws_error() {
		assertThrows(NoSuchElementException.class, () -> {
			for (int i = 0; i < 17; i++) {
				this.iterator.next();
			}
		});
	}


	@Test
	public void null_argument_in_constructor_throws_error() {
		assertThrows(IllegalArgumentException.class, () -> {
			new KontingentIterator(null);
		});
	}
}
