package com.tschanz.nova.insbecdr;

import ch.voev.nova.pflege.kontingent.sb.api.TransportKontingentDatenrelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class InsbecdrApplication implements CommandLineRunner {
    @Autowired
    private SbDrDeserializer deserializer;


    public static void main(String[] args) {
         SpringApplication.run(InsbecdrApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //String drFileName = "res/SB_12723-sparbillett-exporter-poc-with-mock-dr-SNAPSHOT_1344_09112020-T.340";
        //String drFileName = "res/SB_126.2.4-SNAPSHOT_1828_09242020-T.553";
        //String drFileName = "res/SB_126.2.4-SNAPSHOT_1446_09212020-T.522";
        String drFileName = "res/SB_126.2.4-SNAPSHOT_1540_09292020-T.565";


        System.out.println("Reading SB DR...");
        TransportKontingentDatenrelease dr = this.deserializer.deserialize(drFileName);
        //TransportKontingentDatenrelease dr = MockDr1.createDr();
        System.out.println("done.");

        System.out.println("Anz Fahrten: " + dr.getFahrten().size());

        KontingentIterator kontingentIterator = new KontingentIterator(dr);
        InsbecdrConsole console = new InsbecdrConsole(kontingentIterator);
        console.run();

        System.out.println("Exiting.");
    }
}
