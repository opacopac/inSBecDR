package ch.voev.nova.pflege.kontingent.exporter.sb_reader;

import ch.voev.nova.pflege.kontingent.sb.api.*;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.BefahrungsVariante;
import ch.voev.nova.pflege.kontingent.sb.api.befahrungsVariante.TransportAbschnitt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.KlasseTyp;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabatt;
import ch.voev.nova.pflege.kontingent.sb.api.rabatte.Rabattstufe;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;


public final class ExampleDr {
    private ExampleDr() {
    }

    public static TransportKontingentDatenrelease createDr() {
        return new TransportKontingentDatenrelease(
            createMetaData(),
            List.of(createFahrt1(), createFahrt2()),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }


    public static MetaData createMetaData() {
        return new MetaData(
            null,
            "1",
            "http://www.tschanz.com/",
            ZonedDateTime.of(2020, 9, 29, 19, 41, 13, 0, ZoneId.of("CET")),
            Collections.emptyList(),
            LocalDate.of(2020, 12, 12),
            LocalDate.of(9999, 12, 31),
            "TEST",
            "1.0.0"
        );
    }


    public static Fahrt createFahrt1() {
        return new Fahrt(
            "1",
            LocalDate.of(2020, 9, 28),
            7,
            "11",
            List.of(createFahrtAbschnitt1(), createFahrtAbschnitt2())
        );
    }


    public static Fahrt createFahrt2() {
        return new Fahrt(
            "5",
            LocalDate.of(2020, 9, 29),
            123,
            "72",
            List.of(createFahrtAbschnitt1(), createFahrtAbschnitt2())
        );
    }


    public static FahrtAbschnitt createFahrtAbschnitt1() {
        return new FahrtAbschnitt(
            "2",
            8507000,
            8500218,
            List.of(createTransportKontingent1(), createTransportKontingent2())
        );
    }


    public static FahrtAbschnitt createFahrtAbschnitt2() {
        return new FahrtAbschnitt(
            "6",
            8500218,
            8503000,
            List.of(createTransportKontingent1(), createTransportKontingent2())
        );
    }


    public static TransportKontingent createTransportKontingent1() {
        return new TransportKontingent(
            "3",
            List.of(createBefahrungsvariante1(), createBefahrungsvariante2()),
            Ebene.TV,
            List.of(4004),
            List.of(createKontingentAngebot1(), createKontingentAngebot2())
        );
    }


    public static TransportKontingent createTransportKontingent2() {
        return new TransportKontingent(
            "7",
            Collections.emptyList(),
            Ebene.ZAS,
            List.of(4004),
            List.of(createKontingentAngebot1(), createKontingentAngebot2())
        );
    }


    public static KontingentAngebot createKontingentAngebot1() {
        return new KontingentAngebot(
            "4",
            5,
            createRabattStufe1()
        );
    }


    public static KontingentAngebot createKontingentAngebot2() {
        return new KontingentAngebot(
            "8",
            999,
            createRabattStufe2()
        );
    }


    public static Rabattstufe createRabattStufe1() {
        return new Rabattstufe(
            "9",
            "1_6C7rdRBvCn",
            1,
            List.of(createRabatt1(), createRabatt2())
        );
    }


    public static Rabattstufe createRabattStufe2() {
        return new Rabattstufe(
            "10",
            "2_6C7rdRBvCn",
            2,
            List.of(createRabatt1(), createRabatt2())
        );
    }


    public static Rabatt createRabatt1() {
        return new Rabatt(
            "11",
            List.of(KlasseTyp.KLASSE_1),
            List.of("VOLLPREIS"),
            BigDecimal.valueOf(25.5),
            10
        );
    }


    public static Rabatt createRabatt2() {
        return new Rabatt(
            "12",
            List.of(KlasseTyp.KLASSE_2),
            List.of("1/2"),
            BigDecimal.valueOf(15),
            15
        );
    }


    public static BefahrungsVariante createBefahrungsvariante1() {
        return new BefahrungsVariante(
            "13",
            List.of(createTransportAbschnitt1(), createTransportAbschnitt2())
        );
    }


    public static BefahrungsVariante createBefahrungsvariante2() {
        return new BefahrungsVariante(
            "15",
            List.of(createTransportAbschnitt2(), createTransportAbschnitt1())
        );
    }


    public static TransportAbschnitt createTransportAbschnitt1() {
        return new TransportAbschnitt(
            "14",
            7,
            8507000,
            8500218,
            "11"
        );
    }


    public static TransportAbschnitt createTransportAbschnitt2() {
        return new TransportAbschnitt(
            "16",
            7,
            8500218,
            8503000,
            "11"
        );
    }
}
