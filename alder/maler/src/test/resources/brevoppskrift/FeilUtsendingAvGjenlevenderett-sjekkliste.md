# FeilUtsendingAvGjenlevenderett – utviklersjekkliste
- Målmodul: ALDER
- Kildecommit: a28fc5d93d45be0097e749040a0d463df78de91d
- Importprofil: tekstendringer med låst Kotlin-struktur; ikke dokumentasjonsimport
- [ ] Kontroller brevkode, modulregistrering, kategori og sakstyper. Lokal kodeenum må flyttes til modulens register ved innføring av ny mal.
- [ ] DTO-endring i publisert api-model: publiser lokalt, oppdater versjon og bygg målmodulen med KSP.
- [ ] Kjør målmodulens tester og kontroller faktisk rendring på alle språk.
- [ ] Kontroller syntetiske scenarier, vilkår, nullable felt, fraser og vedlegg.
- [ ] Sammenlign med grunnlinjen og kontroller upstream før PR.
- [ ] Rull tilbake ved å reversere PR-en. Ingen automatisk deploy eller merge.
