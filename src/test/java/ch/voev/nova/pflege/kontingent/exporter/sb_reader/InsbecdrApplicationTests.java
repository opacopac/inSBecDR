package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class InsbecdrApplicationTests {
    @Autowired private ConsoleWriter conWriter;
    @Autowired private SbDrLoader sbDrLoader;
    @Autowired private InsbecdrConsole console;


    @Test
    void no_args__context_loads__injection_works__exits_automatically() {
        assertNotNull(conWriter);
        assertNotNull(sbDrLoader);
        assertNotNull(console);
    }
}

