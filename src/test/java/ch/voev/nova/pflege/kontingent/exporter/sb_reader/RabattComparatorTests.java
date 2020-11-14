package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.rabatte.KlasseTyp;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


class RabattComparatorTests {
	@BeforeEach
	public void setup() {
	}


	@Test
	public void sorts_klasse_alphabetically() {
		Rabatt r1 = new Rabatt("1", List.of(KlasseTyp.KLASSE_1), List.of("VOLLPREIS"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_2), List.of("VOLLPREIS"), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result < 0);
	}


	@Test
	public void sorts_kundensegment_alphabetically() {
		Rabatt r1 = new Rabatt("1", List.of(KlasseTyp.KLASSE_1), List.of("VOLLPREIS"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_1), List.of("HALBTAX"), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result > 0);
	}


	@Test
	public void sorts_first_by_klasse_then_kundensegment() {
		Rabatt r1 = new Rabatt("1", List.of(KlasseTyp.KLASSE_1), List.of("VOLLPREIS"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_2), List.of("HALBTAX"), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result < 0);
	}


	@Test
	public void sorts_multiple_klasse_and_kundensegment() {
		Rabatt r1 = new Rabatt("1", List.of(KlasseTyp.KLASSE_1, KlasseTyp.KLASSENWECHSEL), List.of("VOLLPREIS", "PERSON_16+"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_2), List.of("HALBTAX", "1/2"), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result < 0);
	}


	@Test
	public void sorts_null_rabatts() {
		Rabatt r1 = new Rabatt("1", List.of(KlasseTyp.KLASSE_1, KlasseTyp.KLASSENWECHSEL), List.of("VOLLPREIS", "PERSON_16+"), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(null, null);
		int result2 = comparator.compare(r1, null);
		int result3 = comparator.compare(null, r1);

		assertEquals(0, result);
		assertTrue(result2 < 0);
		assertTrue(result3 > 0);
	}


	@Test
	public void sorts_null_klasse_and_null_kundensegment() {
		Rabatt r1 = new Rabatt("1", null, List.of("VOLLPREIS", "PERSON_16+"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_2), null, BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result > 0);
	}


	@Test
	public void sorts_empty_klasse_and_empty_kundensegment_lists() {
		Rabatt r1 = new Rabatt("1", Collections.emptyList(), List.of("VOLLPREIS", "PERSON_16+"), BigDecimal.valueOf(10), 5);
		Rabatt r2 = new Rabatt("2", List.of(KlasseTyp.KLASSE_2), Collections.emptyList(), BigDecimal.valueOf(10), 5);
		RabattComparator comparator = new RabattComparator();

		int result = comparator.compare(r1, r2);

		assertTrue(result > 0);
	}
}
