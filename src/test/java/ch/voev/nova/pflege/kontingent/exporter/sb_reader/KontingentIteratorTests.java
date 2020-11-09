package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;


class KontingentIteratorTests {
	private KontingentIterator iterator;


	@BeforeEach
	public void setup() {
		TransportKontingentDatenrelease mockDr = TestMockDr.createDr();
		this.iterator = new KontingentIterator(mockDr);
	}


	@Test
	public void initial_hasNext_is_true() {
		assertTrue(this.iterator.hasNext());
	}


	@Test
	public void initial_next_returns_first_record() {
		KontingentRecord result = this.iterator.next();

		assertEquals(1, result.getCount());
		assertEquals(result.getFahrt(), TestMockDr.createFahrt1());
		assertEquals(result.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt1());
		assertEquals(result.getTransportKontingent(), TestMockDr.createTransportKontingent1());
	}


	@Test
	public void last_hasNext_is_false() {
		for (int i = 0; i < 8; i++) {
			this.iterator.next();
		}

		assertFalse(this.iterator.hasNext());
	}


	@Test
	public void next_after_end_throws_error() {
		assertThrows(NoSuchElementException.class, () -> {
			for (int i = 0; i < 9; i++) {
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
		boolean hasResult9 = this.iterator.hasNext();

		// result1
		assertEquals(result1.getFahrt(), TestMockDr.createFahrt1());
		assertEquals(result1.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt1());
		assertEquals(result1.getTransportKontingent(), TestMockDr.createTransportKontingent1());

		// result2
		assertEquals(result2.getFahrt(), TestMockDr.createFahrt1());
		assertEquals(result2.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt1());
		assertEquals(result2.getTransportKontingent(), TestMockDr.createTransportKontingent2());

		// result3
		assertEquals(result3.getFahrt(), TestMockDr.createFahrt1());
		assertEquals(result3.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt2());
		assertEquals(result3.getTransportKontingent(), TestMockDr.createTransportKontingent1());

		// result4
		assertEquals(result4.getFahrt(), TestMockDr.createFahrt1());
		assertEquals(result4.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt2());
		assertEquals(result4.getTransportKontingent(), TestMockDr.createTransportKontingent2());

		// result5
		assertEquals(result5.getFahrt(), TestMockDr.createFahrt2());
		assertEquals(result5.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt1());
		assertEquals(result5.getTransportKontingent(), TestMockDr.createTransportKontingent1());

		// result6
		assertEquals(result6.getFahrt(), TestMockDr.createFahrt2());
		assertEquals(result6.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt1());
		assertEquals(result6.getTransportKontingent(), TestMockDr.createTransportKontingent2());

		// result7
		assertEquals(result7.getFahrt(), TestMockDr.createFahrt2());
		assertEquals(result7.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt2());
		assertEquals(result7.getTransportKontingent(), TestMockDr.createTransportKontingent1());

		// result8
		assertEquals(result8.getFahrt(), TestMockDr.createFahrt2());
		assertEquals(result8.getFahrtAbschnitt(), TestMockDr.createFahrtAbschnitt2());
		assertEquals(result8.getTransportKontingent(), TestMockDr.createTransportKontingent2());

		// result9
		assertFalse(hasResult9);
	}
}
