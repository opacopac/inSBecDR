package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ConsoleReaderTests {
	private final InputStream standardIn = System.in;
	private final PipedOutputStream pipedOutputStream = new PipedOutputStream();
	private final PipedInputStream pipedInputStream = new PipedInputStream();
	private ConsoleReader consoleReader;


	@BeforeEach
	public void setup() throws IOException {
		this.pipedInputStream.connect(this.pipedOutputStream);
		System.setIn(this.pipedInputStream);
		this.consoleReader = new ConsoleReader();
	}


	@AfterEach
	public void tearDown() {
		System.setIn(this.standardIn);
	}


	@Test
	public void nextLine_get_single_line_from_input_stream() throws IOException {
		this.pipedOutputStream.write("MEEP\n".getBytes());

		String result = this.consoleReader.nextLine();

		assertEquals("MEEP", result);
	}


	@Test
	public void nextLine_get_multiple_lines_from_input_stream() throws IOException {
		this.pipedOutputStream.write("MEEP\n".getBytes());
		this.pipedOutputStream.write("MAAP\n".getBytes());
		this.pipedOutputStream.write("MUUP\n".getBytes());

		String result1 = this.consoleReader.nextLine();
		String result2 = this.consoleReader.nextLine();
		String result3 = this.consoleReader.nextLine();

		assertEquals("MEEP", result1);
		assertEquals("MAAP", result2);
		assertEquals("MUUP", result3);
	}
}
