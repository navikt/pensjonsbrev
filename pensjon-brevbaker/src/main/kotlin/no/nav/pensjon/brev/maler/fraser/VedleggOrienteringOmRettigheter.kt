package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Sivilstand.GIFT
import no.nav.pensjon.brev.api.model.Sivilstand.GIFT_LEVER_ADSKILT
import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Phrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.phrase

object VedleggPlikterOgRettigheterOverskriftPesys_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Dine rettigheter og plikter",
                Nynorsk to "Dine rettar og plikter",
                English to ""
            )
        }
    }
}

object VedleggPlikter_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            //TODO support italic and bold text
            text(
                Bokmal to "Plikt til å opplyse om endringer – folketrygdloven § 21-3",
                Nynorsk to "Plikt til å opplyse om endringar – folketrygdlova § 21-3",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du må gi oss beskjed hvis",
                Nynorsk to "Du må alltid melde frå dersom",
                English to ""
            )
        }
    }
}

object VedleggPlikterAP2_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
            Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
            English to ""
        )
    }
}

object VedleggPlikterAP3_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
            Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
            English to ""
        )
    }
}

object VedleggPlikterAP1_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du gifter deg eller inngår samboerskap",
            Nynorsk to "du giftar deg eller inngår sambuarskap",
            English to ""
        )
    }
}

object VedleggPlikterAP4_002 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for ektefellen din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til ektefellen din endrar seg",
            English to ""
        )
    }
}

object VedleggPlikterAP13_002 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for partneren din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til partnaren din endrar seg",
            English to ""
        )
    }
}

object VedleggPlikterAP15_002 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekent endrer seg for samboeren din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til sambuaren din endrar seg",
            English to ""
        )
    }
}

object VedleggPlikterAP6_002 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du og ektefellen din flytter fra hverandre ",
            Nynorsk to "du og ektefellen din flyttar frå kvarandre",
            English to ""
        )
    }
}

object VedleggPlikterAP14_002 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du og partneren din flytter fra hverandre ",
            Nynorsk to "du og partnaren din flyttar frå kvarandre",
            English to ""
        )
    }
}

object VedleggPlikterAP18_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du og samboeren din flytter fra hverandre",
            Nynorsk to "du og sambuaren din flyttar frå kvarandre",
            English to ""
        )
    }
}

object VedleggPlikterAP16_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du gifter deg",
            Nynorsk to "du giftar deg",
            English to ""
        )
    }
}

object VedleggPlikterAP17_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du får barn med samboeren din",
            Nynorsk to "du får barn med sambuaren din",
            English to ""
        )
    }
}

object VedleggPlikterAP19_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "samboeren din dør",
            Nynorsk to "sambuaren din døyr",
            English to ""
        )
    }
}

object VedleggPlikterAP8_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du og ektefellen din flytter sammen igjen",
            Nynorsk to "du og ektefellen din flyttar saman igjen",
            English to ""
        )
    }
}

object VedleggPlikterAP11_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du og partneren din flytter sammen igjen",
            Nynorsk to "du og partnaren din flyttar saman igjen",
            English to ""
        )
    }
}

object VedleggPlikterAP9_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du blir skilt",
            Nynorsk to "du blir skild",
            English to ""
        )
    }
}

object VedleggPlikterAP7_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "ektefellen din dør",
            Nynorsk to "ektefellen din døyr",
            English to ""
        )
    }
}

object VedleggPlikterAP12_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "partneren din dør",
            Nynorsk to "partnaren din døyr",
            English to ""
        )
    }
}

object VedleggPlikterAP10_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du får ny samboer",
            Nynorsk to "du får ny sambuar",
            English to ""
        )
    }
}

object VedleggPlikterAP5_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "en av dere får et varig opphold i institusjon",
            Nynorsk to "ein av dykk får et varig opphald i institusjon",
            English to ""
        )
    }
}

object VedleggPlikterAP26_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du får et varig opphold i institusjon",
            Nynorsk to "du blir innlagd på institusjon",
            English to ""
        )
    }
}

object VedleggPlikterAP27_001 : Phrase<Unit> {
    override val elements = phrase {
        text(
            Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
            Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
            English to ""
        )
    }
}

object VedleggPlikterHvorforMeldeAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Skjer det endringer, kan det få betydning for hvor mye du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mulig.",
                Nynorsk to "Skjer det endringar, kan det få betydning for kor mykje du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mogleg.",
                English to ""
            )
        }
    }
}

object VedleggPlikterRettTilBarnetilleggAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Fordi du får barnetillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
                Nynorsk to "Fordi du får barnetillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
                English to ""
            )
            newline()
            text(
                Bokmal to "Du må gi oss beskjed hvis barn du forsørger",
                Nynorsk to "Du må gi oss beskjed om barn du forsørgjer",
                English to ""
            )
            list {
                text(
                    Bokmal to "får egen inntekt som er mer enn  folketrygden grunnbeløp",
                    Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                    English to ""
                )
                text(
                    Bokmal to "omsorgssituasjonen for barnet endrer seg",
                    Nynorsk to "skjer endringar av omsorgsituasjonen",
                    English to ""
                )
                text(
                    Bokmal to "skal flytte til et annet land",
                    Nynorsk to "skal flytte til eit anna land",
                    English to ""
                )
                text(
                    Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                    Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dagar i løpet av ein tolv månedars periode",
                    English to ""
                )
            }
        }
    }
}

object VedleggPlikterRettTilEktefelletilleggAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Fordi du får ektefelletillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
                Nynorsk to "Fordi du får ektefelletillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
                English to ""
            )
            newline()
            text(
                Bokmal to "Du må gi oss beskjed hvis ektefellen/partneren/samboeren du forsørger",
                Nynorsk to "Du må gi oss beskjed hvis ektefellen/partner/samboer du forsørgjer",
                English to ""
            )
            list {
                text(
                    Bokmal to "får egen inntekt som er mer enn  folketrygden grunnbeløp",
                    Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                    English to ""
                )
                text(
                    Bokmal to "skal flytte til et annet land",
                    Nynorsk to "skal flytte til eit anna land",
                    English to ""
                )
                text(
                    Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                    Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dager i løpet av ein tolv månedars periode",
                    English to ""
                )
            }
        }
    }
}

object VedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Fordi du får ektefelletillegg og barnetillegg må du også melde fra om endringer som kan ha betydning for disse tilleggene.",
                Nynorsk to "Fordi du får ektefelletillegg og barnetillegg må du også melde frå om endringar som kan ha betydning for desse tillegga.",
                English to ""
            )
            newline()
            text(
                Bokmal to "Du må gi oss beskjed hvis barn eller ektefellen/partneren/samboeren du forsørger",
                Nynorsk to "Du må gi oss beskjed om barn eller ektefelle/partner/sambuar du forsørgjer",
                English to ""
            )
            list {
                text(
                    Bokmal to "får egen inntekt som er mer enn  folketrygden grunnbeløp",
                    Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                    English to ""
                )
                text(
                    Bokmal to "omsorgssituasjonen for barnet endrer seg",
                    Nynorsk to "omsorgssituasjonen for barnet endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "skal flytte til et annet land",
                    Nynorsk to "skal flytte til eit anna land",
                    English to ""
                )
                text(
                    Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                    Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dager i løpet av ein tolv månedars periode",
                    English to ""
                )
            }
        }
    }
}

object VedleggPlikterinntektsproevingBTFellesBarnSaerkullsbarnAP_001 :
    Phrase<VedleggPlikterinntektsproevingBTFellesBarnSaerkullsbarnAP_001.Param> {
    override val elements = phrase {
        paragraph {
            // TODO spør ingrid om dette blir riktig ved bruk av sivilstand
            showIf(argument().select(Param::sivilstand).isOneOf(GIFT, GIFT_LEVER_ADSKILT)) {
                text(
                    Bokmal to "Hvor mye du får utbetalt i barnetillegg avhenger av den samlede inntekten du og ektefellen har. Du må derfor også gi beskjed hvis",
                    Nynorsk to "Kor mykje du får utbetalt i barnetillegg er avhengig av den samla inntekta du og ektefellen har. Du må derfor også gi beskjed om",
                    English to ""
                )
            } orShow {
                text(
                    Bokmal to "Hvor mye du får utbetalt i barnetillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
                    Nynorsk to "Kor mykje du får utbetalt i barnetillegg er avhengig av den samla inntekta du har. Du må derfor også gi beskjed om",
                    English to ""
                )
            }


            list {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to ""
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endres",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    English to ""
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to ""
                )
            }
        }


    }

    data class Param(val sivilstand: Sivilstand)
}

object VedleggPlikterinntektsprovingBTOgETAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Hvor mye du får utbetalt i barnetillegg og ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
                Nynorsk to "Kor mykje du får utbetalt i barnetillegg og ektefelletillegg er avhengig av den samla inntekta du har. Du må derfor også gi beskjed om",
                English to ""
            )
            list {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to ""
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endres",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to ""
                )
            }
        }
    }
}

//TODO avklar hvorfor malen til vedlegget er så forskjellig på nynorsk
object VedleggPlikterinntektsprovingETAP_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Hvor mye du får utbetalt i ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
                Nynorsk to "Kor mykje du får utbetalt i ektefelletillegg avhenger av den samla inntekta du har. Du må derfor også gi beskjed om",
                English to ""
            )
            list {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to ""
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endres",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to ""
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to ""
                )
            }
        }
    }
}

object InfoAPBeskjed_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Slik gir du oss beskjed",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan logge deg inn på Din Pensjon og velge «Kontakt NAV om pensjon», eller på nav.no/beskjedtilnav og velge «Send beskjed til NAV». Du kan også sende beskjed til oss i posten. Adressen finner du på nav.no/ettersendelse.",
                Nynorsk to "",
                English to ""
            )
        }

    }
}

object VedleggVeiledning_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 { //TODO italic font support
            text(
                Bokmal to "Veiledning fra NAV – forvaltningsloven § 11",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre vårt beste for å hjelpe deg.",
                Nynorsk to "",
                English to ""
            )
        }
    }
}

object VedleggInnsynSakPensjon_001 : Phrase<VedleggInnsynSakPensjon_001.Param> {
    override val elements = phrase {
        val kontaktinfo_nettsted = argument().select(Param::kontaktinfo_nettsted)
        val kontaktinfo_telefonnummer = argument().select(Param::kontaktinfo_telefonnummer)
        title1 { //TODO italic font support
            text(
                Bokmal to "Innsyn i saken din – forvaltningsloven § 18",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn på ".expr() + kontaktinfo_nettsted + " for å se all kommunikasjon som har vært mellom deg og NAV i saken din. Du kan også ringe oss på telefon ".expr() + kontaktinfo_telefonnummer.format() + ".".expr(),
                Nynorsk to "".expr(),
                English to "".expr()
            )
        }
    }

    data class Param(
        val kontaktinfo_nettsted: String,
        val kontaktinfo_telefonnummer: Telefonnummer
    )
}

object VedleggHjelpFraAndre_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Hjelp fra andre – forvaltningsloven § 12",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på nav.no/fullmakt.",
                Nynorsk to "",
                English to ""
            )
        }
    }
}

object VedleggKlagePensjon_001 : Phrase<VedleggKlagePensjon_001.Param> {
    override val elements = phrase {
        val kontaktinfo_telefonnummer = argument().select(Param::kontaktinfo_telefonnummer)
        title1 {
            text(
                Bokmal to "Klage på vedtaket – folketrygdloven § 21-12",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            paragraph {
                text(
                    Bokmal to "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finner på nav.no/klage. Trenger du hjelp, er du velkommen til å ringe oss på telefon ".expr() + kontaktinfo_telefonnummer.format() + ".".expr(),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                text(
                    Bokmal to "Du må skrive",
                    Nynorsk to "",
                    English to ""
                )
                list {
                    text(
                        Bokmal to "hvilket vedtak du klager på",
                        Nynorsk to "",
                        English to ""
                    )
                    text(
                        Bokmal to "hvilken endring i vedtaket du ber om",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "Du bør også",
                    Nynorsk to "",
                    English to ""
                )
                list {
                    text(
                        Bokmal to "skrive hvorfor du mener vedtaket er feil",
                        Nynorsk to "",
                        English to ""
                    )
                    text(
                        Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Husk å undertegne klagen, ellers må vi sende den i retur til deg.",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos fylkesmannen, advokater eller NAV.",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }

    }

    data class Param(val kontaktinfo_telefonnummer: Telefonnummer)
}

object VedleggKlagePesys_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
                Nynorsk to "",
                English to ""
            )
            newline()
            text(
                Bokmal to "Klagen må være skriftlig og inneholde",
                Nynorsk to "",
                English to ""
            )
            list {
                text(
                    Bokmal to "navn, fødselsnummer og adresse",
                    Nynorsk to "",
                    English to ""
                )
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "",
                    English to ""
                )
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "",
                    English to ""
                )
                text(
                    Bokmal to "din underskrift",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
        paragraph {
            text(
                Bokmal to "Klagen bør også inneholde",
                Nynorsk to "",
                English to ""
            )
            list {
                text(
                    Bokmal to "hvorfor du mener vedtaket er feil",
                    Nynorsk to "",
                    English to ""
                )
                text(
                    Bokmal to "nødvendige erklæringer og andre dokumenter",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
        paragraph {
            text(
                Bokmal to "Bruk gjerne skjemaet du finner på nav.no/klage. NAV kan hjelpe deg med å skrive klagen. Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos fylkesmannen, advokater eller NAV.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
                Nynorsk to "",
                English to ""
            )
        }
    }
}