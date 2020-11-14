## InSBecDR
Simple console application for deserializing and searching the NOVA Sparbillett DR kryo files.

### Working Principle
The reader iterates over the TransportKontingent entries of the DR and displays its
linked KontingentAngebot, Rabattstufe, Rabatt and opt. Befahrungsvariante. Filters can be used to restrict the iterations to certain data
of interest (date, Verwaltung, Fahrt, UIC1/2). The iterations can be viewed on the screen using the 'enter' key, or written to a file using
the 'w' command. 

### How to
Start the Sparbillett DR reader locally:
```bash
java -jar insbecdr-x.y.z.jar -example
java -jar insbecdr-x.y.z.jar <url>
java -jar insbecdr-x.y.z.jar <filename>
```

Example Session:
```bash
java -jar insbecdr-x.y.z.jar -example

Welcome to inSBecDR
===================
Compatible interface version: 0.0.6

Reading Mock DR...
Anz Fahrten: 2
Anz Verwaltungen: 2: [11, 72]
Anz Tage: 2: [2020-09-28, 2020-09-29]
Anz Rabattstufen: 2
Anz Befahrungsvarianten: 2
Press <enter> to show records & 'h' for help.
>
```
set filters (e.g. 'd' and 'v' for date & verwaltung, use 'h' for help on all filters and their syntax):
```bash
> d 2020-09-28
d=2020-09-28> v 11
d=2020-09-28,v=11>
```
press 'enter' to show next 10 records:
```bash
d=2020-09-28,v=11>
00000001 Datum=2020-09-28 Verwaltung=11 Fahrt=7 Uic1=8507000 Uic2=8500218 Produkte=[4004] Ebene=TV Befahrungsvarianten=2 Kontingente=2
  Anzahl=5 Stufe=1 Code=1_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
  Anzahl=999 Stufe=2 Code=2_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
00000002 Datum=2020-09-28 Verwaltung=11 Fahrt=7 Uic1=8507000 Uic2=8500218 Produkte=[4004] Ebene=ZAS Befahrungsvarianten=0 Kontingente=2
  Anzahl=5 Stufe=1 Code=1_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
  Anzahl=999 Stufe=2 Code=2_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
00000003 Datum=2020-09-28 Verwaltung=11 Fahrt=7 Uic1=8500218 Uic2=8503000 Produkte=[4004] Ebene=TV Befahrungsvarianten=2 Kontingente=2
  Anzahl=5 Stufe=1 Code=1_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
  Anzahl=999 Stufe=2 Code=2_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
00000004 Datum=2020-09-28 Verwaltung=11 Fahrt=7 Uic1=8500218 Uic2=8503000 Produkte=[4004] Ebene=ZAS Befahrungsvarianten=0 Kontingente=2
  Anzahl=5 Stufe=1 Code=1_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
  Anzahl=999 Stufe=2 Code=2_6C7rdRBvCn: [KLASSE_1] [VOLLPREIS] Rabatt=25.5% Verfall=10, [KLASSE_2] [1/2] Rabatt=15% Verfall=15
(no more records, press 'r' to rewind)
d=2020-09-28,v=11>
```
use 'q' command to exit.
```bash
d=2020-09-28,v=11> q
Exiting.
```
