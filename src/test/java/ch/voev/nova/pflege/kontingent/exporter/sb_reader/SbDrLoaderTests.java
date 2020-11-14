package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SbDrLoaderTests {
	private SpySerializationFactory spySerializationFactory;
	private SpyFileIOFactory spyFileIOFactory;
	private SbDrLoader loader;


	@BeforeEach
	public void setup() {
		this.spySerializationFactory = new SpySerializationFactory();
		this.spyFileIOFactory = new SpyFileIOFactory();
		this.loader = new SbDrLoader();
		ReflectionTestUtils.setField(loader, "serializationFactory", this.spySerializationFactory);
		ReflectionTestUtils.setField(loader, "fileIOFactory", this.spyFileIOFactory);
	}


	@Test
	public void load__reads_from_file() throws IOException {
		this.spyFileIOFactory.simulateInput = "dummy".getBytes();

		TransportKontingentDatenrelease result = this.loader.load("sb_dr.bin");

		assertEquals("sb_dr.bin", this.spyFileIOFactory.openedFilename);
		assertEquals(TestMockDr.createDr(), result);
	}


	@Test
	public void load__reads_from_url() throws IOException {
		this.spyFileIOFactory.simulateInput = "dummy".getBytes();

		TransportKontingentDatenrelease result = this.loader.load("http://x.y.z/sb_dr.bin");

		assertEquals("http://x.y.z/sb_dr.bin", this.spyFileIOFactory.openedUrl);
		assertEquals(TestMockDr.createDr(), result);
	}
}
