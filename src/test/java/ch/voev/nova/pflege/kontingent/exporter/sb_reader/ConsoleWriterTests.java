package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ConsoleWriterTests {
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
	private ConsoleWriter consoleWriter;


	@BeforeEach
	public void setup() {
		System.setOut(new PrintStream(this.testOutput));
		this.consoleWriter = new ConsoleWriter();
	}


	@AfterEach
	public void tearDown() {
		System.setOut(this.standardOut);
	}


	@Test
	public void println_appends_a_new_line() {
		this.consoleWriter.println("MEEP");

		assertTrue(testOutput.toString().contains("MEEP"));
		assertTrue(testOutput.toString().endsWith("\n"));
	}


	@Test
	public void print_just_prints() {
		this.consoleWriter.print("MEEP");

		assertEquals("MEEP", testOutput.toString());
	}
}
