package no.nav.pensjon.brev.maler.ufore.fraser

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.OMSTILLINGSSTOENAD_URL
import no.nav.pensjon.brev.maler.ufore.fraser.Barnetillegg.DuHarFaattUtbetaltBarnetilleggTidligereIAar
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

// omregnUTBTDodEPSInnledn_001, omregnUTDodEPSInnledn1_001, omregnBTDodEPSInnledn_001, omregnUTBTSBDodEPSInnledn_001, omregnUTDodEPSInnledn2_001
data class OmregnEPSInnledning(
    val harMinsteytelseVedVirk: Expression<Boolean>,
    val inntektFoerUfoereErSannsynligEndret: Expression<Boolean>,
    val ektefelletilleggOpphoert: Expression<Boolean>,
    val harBarnetilleggForSaerkullsbarnVedVirk: Expression<Boolean>,
    val harBarnOverfoertTilSaerkullsbarn: Expression<Boolean>,
    val avdoedNavn: Expression<String>,
    val kravVirkningsDatoFraOgMed: Expression<LocalDate>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            val virkningsdato = kravVirkningsDatoFraOgMed.format()
            showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelletilleggOpphoert) {
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    text(
                        bokmal { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har regnet om uføretrygden og barnetillegget ditt fra "
                                + virkningsdato + " fordi sivilstanden din er endret." },
                        nynorsk { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har rekna om uføretrygda di og barnetillegget ditt frå "
                                + virkningsdato + " fordi sivilstanden din har blitt endra." },
                    )
                }.orShow {
                    text(
                        bokmal { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har regnet om uføretrygden din fra "
                                + virkningsdato + " fordi sivilstanden din er endret." },
                        nynorsk { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har rekna om uføretrygda di frå "
                                + virkningsdato + " fordi sivilstanden din er endra." },
                    )
                }
            }.orShow {
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    showIf(harBarnOverfoertTilSaerkullsbarn) {
                        text(
                            bokmal { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har regnet om barnetillegget ditt fra "
                                    + virkningsdato + " fordi sivilstanden din er endret." },
                            nynorsk { + "Vi har mottatt melding om at " + avdoedNavn + " er død, og vi har rekna om barnetillegget ditt frå "
                                    + virkningsdato + " fordi sivilstanden din er endra." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vi har mottatt melding om at " + avdoedNavn
                                    + " er død. Uføretrygden og barnetillegget ditt endres ikke." },
                            nynorsk { + "Vi har mottatt melding om at " + avdoedNavn
                                    + " er død. Uføretrygda di og barnetillegget ditt blir ikkje endra." },
                        )
                    }
                }.orShow {
                    text(
                        bokmal { + "Vi har mottatt melding om at " + avdoedNavn
                                + " er død. Uføretrygden din endres ikke." },
                        nynorsk { + "Vi har mottatt melding om at " + avdoedNavn
                                + " er død. Uføretrygda di blir ikkje endra." },
                    )
                }
            }
        }
    }
}

// TODO: Slå sammen med Ufoeretrygd.beloep
// BelopUT_001, BelopUTVedlegg_001, BelopUTBT_001, BelopUTBTVedlegg_001, belopUTIngenUtbetaling_001 ,belopUTIngenUtbetalingVedlegg_001 ,belopUTBTIngenUtbetaling_001 ,belopUTBTIngenUtbetalingVedlegg_001 ,belopUTIngenUtbetalingFengsel_001 ,belopUTIngenUtbetalingFengselVedlegg_001
data class UtbetalingUfoeretrygd(
    val totalUfoereMaanedligBeloep: Expression<Kroner>,
    val harBarnetilleggForSaerkullsbarnVedVirk: Expression<Boolean>,
    val harFlereUfoeretrygdPerioder: Expression<Boolean>,
    val institusjonsoppholdVedVirk: Expression<Institusjon>,
) : OutlinePhrase<LangBokmalNynorsk>() {

    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        val faarUtbetaltUfoeretrygd = totalUfoereMaanedligBeloep.greaterThan(0)

        showIf(faarUtbetaltUfoeretrygd) {
            paragraph {
                text(
                    bokmal { + "Du får " + totalUfoereMaanedligBeloep.format() + " i uføretrygd" },
                    nynorsk { + "Du får " + totalUfoereMaanedligBeloep.format() + " i uføretrygd" },
                )
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    text(
                        bokmal { + " og barnetillegg" },
                        nynorsk { + " og barnetillegg" },
                    )
                }
                text(
                    bokmal { + " per måned før skatt." },
                    nynorsk { + " kvar månad før skatt." },
                )
                showIf(harFlereUfoeretrygdPerioder) {
                    text(
                        bokmal { + " Du kan lese mer om andre beregningsperioder i vedlegget." },
                        nynorsk { + " Du kan lese meir om andre utrekningsperiodar i vedlegget." },
                    )
                }
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { + "Du får ikke utbetalt uføretrygd " },
                    nynorsk { + "Du får ikkje utbetalt uføretrygd " },
                )

                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    text(
                        bokmal { + "og barnetillegg " },
                        nynorsk { + "og barnetillegg " },
                    )
                }

                showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                    text(
                        bokmal { + "fordi du er under straffegjennomføring." },
                        nynorsk { + "fordi du er under straffegjennomføring." },
                    )
                }.orShow {
                    text(
                        bokmal { + "på grunn av høy inntekt." },
                        nynorsk { + "på grunn av høg inntekt." },
                    )
                }

                showIf(harFlereUfoeretrygdPerioder) {
                    text(
                        bokmal { + " Du kan lese mer om andre beregningsperioder i vedlegget." },
                        nynorsk { + " Du kan lese meir om andre utrekningsperiodar i vedlegget." },
                    )
                }
            }
        }
    }
}

// EndrMYDodEPS2_001
data class EndretMinsteytelseDoedEPS(
    val minsteytelseSatsVedvirk: Expression<Double>,
    val kompensasjonsgradUfoeretrygdVedvirk: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen "
                        + minsteytelseSatsVedvirk.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på $NAV_URL." },
                nynorsk { + "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga "
                        + minsteytelseSatsVedvirk.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på $NAV_URL." },
            )
        }
        paragraph {
            text(
                bokmal { + "Dette kan ha betydning for reduksjonsprosenten din som er satt til "
                        + kompensasjonsgradUfoeretrygdVedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget." },
                nynorsk { + "Dette kan ha noko å seie for reduksjonsprosenten din som er fastsett til "
                        + kompensasjonsgradUfoeretrygdVedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget." },
            )
        }
    }
}

// EndrMYOgMinstIFUDodEPS2_001
data class EndringMinsteytelseOgMinstInntektFoerUfoerhetDoedEPS(
    val minsteytelseSats: Expression<Double>,
    val inntektFoerUfoerhet: Expression<Kroner>,
    val oppjustertInntektFoerUfoerhet: Expression<Kroner>,
    val kompensasjonsgradUfoeretrygd: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorsk>() {

    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen "
                        + minsteytelseSats.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på $NAV_URL." },
                nynorsk { + "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga "
                        + minsteytelseSats.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på $NAV_URL." },
            )
        }

        paragraph {
            text(
                bokmal { + "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør "
                        + inntektFoerUfoerhet.format() + " som oppjustert til virkningstidspunktet tilsvarer en inntekt på "
                        + oppjustertInntektFoerUfoerhet.format() + ". Reduksjonsprosenten din er satt til "
                        + kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese mer om dette i vedlegget." },
                nynorsk { + "Endringar i sivilstanden påverkar også inntekta di før du blei ufør. Denne utgjer "
                        + inntektFoerUfoerhet.format() + " som oppjustert til verknadstidspunktet svarer til ei inntekt på "
                        + oppjustertInntektFoerUfoerhet.format() + ". Reduksjonsprosenten din er fastsett til "
                        + kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese meir om dette i vedlegget." },
            )
        }
    }
}

// EndrMinstIFUDodEPS2_001
data class EndretMinstInntektFoerUfoerhetDoedEPS(
    val inntektFoerUfoerhet: Expression<Kroner>,
    val oppjustertInntektFoerUfoerhet: Expression<Kroner>,
    val kompensasjonsgradUfoeretrygd: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til "
                        + inntektFoerUfoerhet.format() + " som oppjustert til virkningstidspunktet tilsvarer en inntekt på "
                        + oppjustertInntektFoerUfoerhet.format() + ". Dette kan ha betydning for reduksjonsprosenten din som er satt til "
                        + kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese mer om dette i vedlegget." },
                nynorsk { + "Inntekta di før du blei ufør er fastsett til minstenivå, som er avhengig av sivilstand. For deg er inntekta di før du blei ufør fastsett til "
                        + inntektFoerUfoerhet.format() + " som oppjustert til verknadstidspunktet svarer til ei inntekt på "
                        + oppjustertInntektFoerUfoerhet.format() + ". Dette kan ha noko å seie for reduksjonsprosenten din, som er fastsett til "
                        + kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese meir om dette i vedlegget." },
            )
        }
    }
}

//HjemmelSivilstandUTMinsteIFUAvkortet_001, HjemmelSivilstandUTMinsteIFU_001, HjemmelSivilstandUT_001, HjemmelSivilstandUTAvkortet_001

data class HjemmelSivilstandUfoeretrygd(
    val harMinsteinntektFoerUfoerhet: Expression<Boolean>,
    val ufoeretrygdErInntektsavkortet: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(harMinsteinntektFoerUfoerhet) {
            showIf(ufoeretrygdErInntektsavkortet) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13, 12-14 og 22-12." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13 og 22-12." },
                    )
                }
            }
        }.orShow {
            showIf(ufoeretrygdErInntektsavkortet) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-13, 12-14 og 22-12." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-13 og 22-12." },
                    )
                }
            }
        }
    }
}

// HjemmelEPSDodUTInstitusjon_001
object HjemmelEPSDoedInstitusjonUfoeretrygd : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon." },
                nynorsk { + "Uføretrygda di reknast ut etter folketrygdlova § 12-19 så lenge du er på institusjon." },
            )
        }
}

// HjemmelEPSDodUTFengsel_001
object HjemmelEPSDoedFengselUfoerUfoeretrygd : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring." },
                nynorsk { + "Uføretrygda di reknast ut etter folketrygdlova § 12-20 så lenge du er under straffegjennomføring." },
            )
        }
}

// OpphorETOverskrift_001
object OpphoerEktefelletilleggOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Ektefelletillegget ditt opphører" },
                nynorsk { + "Ektefelletillegget ditt avsluttast" },
            )
        }
}

// OpphorET_001
object OpphoerEktefelletillegg : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt." },
                nynorsk { + "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta." },
            )
        }
}

// HjemmelET_001
object HjemmelEktefelletillegg : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8." },
                nynorsk { + "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
            )
        }
}

// OmregningFBOverskrift_001
object OmregningFellesbarnOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Barnetillegg for fellesbarn er regnet om" },
                nynorsk { + "Barnetillegg for fellesbarn er rekna om" },
            )
        }
}

data class InfoFellesbarnTilSaerkullsbarn(
    val barnOverfoertTilSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:" },
                nynorsk { + "Vi har rekna om barnetillegget for barn som ikkje lenger bur saman med begge foreldre. Dette gjeld:" },
            )
            list {
                forEach(barnOverfoertTilSaerkullsbarn) { navn ->
                    item {
                        text(
                            bokmal { + navn },
                            nynorsk { + navn },
                        )
                    }
                }
            }
        }
}

// InfoTidligereSB_001
data class InfoTidligereSaerkullsbarn(
    val tidligereSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Denne omregningen har også betydning for barnetillegget for:" },
                nynorsk { + "Denne omrekninga har også noko å seie for barnetillegget for:" },
            )
            list {
                forEach(tidligereSaerkullsbarn) { barn ->
                    item {
                        text(
                            bokmal { + barn },
                            nynorsk { + barn },
                        )
                    }
                }
            }
        }
}

// InfoTidligereSBOgEndretUT_001
data class InfoTidligereSaerkullsbarnOgEndretUfoeretrygd(
    val tidligereSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:" },
                nynorsk { + "Omrekninga av barnetillegget og endring i uføretrygda di har også noko å seie for barnetillegget for:" },
            )
            list {
                forEach(tidligereSaerkullsbarn) { element ->
                    item {
                        text(
                            bokmal { + element },
                            nynorsk { + element },
                        )
                    }
                }
            }
        }
}

// EndringUTpavirkerBTOverskrift_001
object EndringUfoeretrygdPaavirkerBarnetilleggOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Slik påvirker endring av uføretrygden barnetillegget ditt" },
                nynorsk { + "Slik påverkar endringa av uføretrygda barnetillegget ditt" },
            )
        }
}

// InfoBTSBInntekt_001
object InfoBarnetilleggSaerkullsbarnInntekt : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Inntekten din har betydning for hva du får i barnetillegg. " +
                        "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                        "Denne grensen kaller vi for fribeløp. " +
                        "Du kan enkelt melde fra om inntektsendringer på $NAV_URL." },
                nynorsk { + "Inntekta di påverkar det du får i barnetillegg. " +
                        "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                        "Denne grensa kallar vi for fribeløp. " +
                        "Du kan enkelt melde frå om inntektsendringar på $NAV_URL." },
            )
        }
}

// InfoBTOverfortTilSBInntekt_001
object InfoBarnetilleggOverfortTilSaerkullsbarnInntekt : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Inntekten din har betydning for hva du får i barnetillegg. " +
                        "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                        "Denne grensen kaller vi for fribeløp. " +
                        "Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " +
                        "Du kan enkelt melde fra om inntektsendringer på $NAV_URL." },

                nynorsk { + "Inntekta di påverkar det du får i barnetillegg. " +
                        "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                        "Denne grensa kallar vi for fribeløp. " +
                        "Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn. " +
                        "Du kan enkelt melde frå om inntektsendringar på $NAV_URL." },
            )
        }
}

// IkkeRedusBTSBPgaInntekt_001
data class IkkeRedusertBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnFribeloep: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Inntekten din på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er lavere enn fribeløpet ditt på "
                        + barnetilleggSaerkullsbarnFribeloep.format() + ". Derfor er barnetillegget ikke redusert ut fra inntekt." },
                nynorsk { + "Inntekta di på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er lågare enn fribeløpet ditt på "
                        + barnetilleggSaerkullsbarnFribeloep.format() + ". Derfor er barnetillegget ikkje redusert ut frå inntekt." },
            )
        }
}

// RedusBTSBPgaInntekt_001
data class RedusertBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnFribeloep: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Inntekten din på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er høyere enn fribeløpet ditt på "
                        + barnetilleggSaerkullsbarnFribeloep.format() + ". Derfor er barnetillegget redusert ut fra inntekt." },
                nynorsk { + "Inntekta di på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er høgare enn fribeløpet ditt på "
                        + barnetilleggSaerkullsbarnFribeloep.format() + ". Derfor er barnetillegget redusert ut frå inntekt." },
            )
        }
}

// JusterBelopRedusBTPgaInntekt_001
object JusterBeloepRedusertBarnetilleggPgaInntekt : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)

            text(
                bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget." },
                nynorsk { + " Det blei teke omsyn til dette då vi endra barnetillegget." },
            )
        }
}

// JusterBelopIkkeUtbetaltBTPgaInntekt_001
object IkkeUtbetaltBarnetilleggPgaInntektOgJusteringsbelop : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
            text(
                bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
            )
        }
}

// IkkeUtbetaltBTSBPgaInntekt_001
data class IkkeUtbetaltBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnInntektstak: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Inntekten din på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er over "
                        + barnetilleggSaerkullsbarnInntektstak.format() + " som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg." },
                nynorsk { + "Inntekta di på "
                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " er over "
                        + barnetilleggSaerkullsbarnInntektstak.format() + ", som er grensa for å få utbetalt barnetillegg. Derfor får du ikkje utbetalt barnetillegg." },
            )
        }
}

// HjemmelBT_001
object HjemmelBarnetillegg : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Vedtaket er gjort etter folketrygdloven § 12-15." },
                nynorsk { + "Vedtaket er gjort etter folketrygdlova § 12-15." },
            )
        }
}

// HjemmelBTRedus_001
object HjemmelBarnetilleggRedusert : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16." },
                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16." },
            )
        }
}

// MerInfoBT_001
object MerInfoBarnetillegg : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Du kan lese mer om beregningen av barnetillegg i vedlegget." },
                nynorsk { + "Du kan lese meir om utrekninga av barnetillegg i vedlegget." },
            )
        }
}

// HvordanSoekerDuOverskrift_001
object HvordanSoekerDuOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Hvordan søker du?" },
                nynorsk { + "Korleis søkjer du?" },
            )
        }
}

object RettTilUfoeretrygdVedGradertUfoeretrygd : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad" },
                nynorsk { + "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad" },
            )
        }
        paragraph {
            text(
                bokmal { + "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Omstillingsstønad er pensjonsgivende inntekt, og kan derfor påvirke hva du får utbetalt i uføretrygd." },
                nynorsk { + "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Omstillingsstønad er pensjonsgivande inntekt, og kan derfor påverke kva du får utbetalt i uføretrygd." },
            )
        }
    }
}

data class StoerrelseOmstillingsstoenad(val grunnbeloepVedVirk: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Hvor mye kan du få i omstillingsstønad?" },
                nynorsk { + "Hvor mykje kan du få?" },
            )
        }
        paragraph {
            text(
                bokmal { + "Stønaden er 2,25 ganger grunnbeløpet i folketrygden per år. " +
                        "Grunnbeløpet er " + grunnbeloepVedVirk.format() + ". " +
                        "Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det påvirke størrelsen." },

                nynorsk { + "Stønaden er 2,25 gongar grunnbeløpet i folketrygda per år. " +
                        "Grunnbeløpet er " + grunnbeloepVedVirk.format() + ". " +
                        "Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke stønaden." },
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du har arbeidsinntekt ved siden av uføretrygden din, blir omstillingsstønaden redusert med 45 prosent av den inntekten som er over halve grunnbeløpet. " +
                        "Noen ytelser, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt." },

                nynorsk { + "Viss du har arbeidsinntekt ved sida av uføretrygda di, blir stønaden redusert med 45 prosent av den inntekta som er over halve grunnbeløpet. " +
                        "Nokre ytingar, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt." },
            )
        }
    }
}

object HvemHarRettPaaOmstillingsstoenad : OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Hvem kan ha rett til omstillingstønad?" },
                nynorsk { + "Kven kan ha rett til omstillingsstønad?" },
            )
        }
        paragraph {
            text(
                bokmal { + "For å ha rett til omstillingsstønaden må du ved dødsfallet som hovedregel:" },
                nynorsk { + "For å ha rett til stønaden må du ved dødsfallet som hovudregel:" },
            )
            list {
                item {
                    text(
                        bokmal { + "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet" },
                        nynorsk { + "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet" },
                    )
                }
                item {
                    text(
                        bokmal { + "ha vært gift med den avdøde i minst fem år, eller" },
                        nynorsk { + "ha vore gift med den avdøde i minst fem år, eller" },
                    )
                }
                item {
                    text(
                        bokmal { + "ha vært gift eller samboer med den avdøde og ha eller ha hatt barn med den avdøde, eller" },
                        nynorsk { + "ha vore gift eller sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller" },
                    )
                }
                item {
                    text(
                        bokmal { + "ha omsorg for barn minst halvparten av tiden." },
                        nynorsk { + "ha omsorg for barn minst halvparten av full tid." },
                    )
                }
            }
        }
    }
}

data class SoekGjenlevendetilleggEtter2024(val borIAvtaleLand: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Vi oppfordrer deg til å søke om omstillingsstønaden så snart som mulig fordi vi vanligvis bare etterbetaler for tre måneder." },
                nynorsk { + "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader." },
            )
        }
        paragraph {
            text(
                bokmal { + "Du finner mer informasjon og søknad på $OMSTILLINGSSTOENAD_URL." },
                nynorsk { + "Du finn informasjon og søknad på $OMSTILLINGSSTOENAD_URL." },
            )
        }
        showIf(borIAvtaleLand) {
            paragraph {
                text(
                    bokmal { + "Hvis du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde." },
                    nynorsk { + "Dersom du bur i utlandet, må du kontakte trygdemyndigheitene i bustadlandet ditt." },
                )
            }
        }
    }
}

// AvdodBoddArbUtlandOverskrift_001
object AvdoedBoddArbeidetIUtlandOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet" },
                nynorsk { + "Rettar dersom avdøde har budd eller arbeidd i utlandet" },
            )
        }
}

// AvdodBoddArbUtland2_001
data class AvdoedBoddEllerArbeidetIUtland(val borIAvtaleland: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Hvis avdøde har bodd eller arbeidet i utlandet, kan det påvirke hvor mye du får utbetalt. Norge har trygdesamarbeid med mange land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. " },
                nynorsk { + "Dersom avdøde har budd eller arbeidd i utlandet, kan det påverke kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. " },
            )
            showIf(not(borIAvtaleland)) {
                text(
                    bokmal { + "Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med." },
                    nynorsk { + "Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med." },
                )
            }
        }
}
// PensjonFraAndreOverskrift_001
object PensjonFraAndreOverskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "Andre pensjonsordninger" },
                nynorsk { + "Andre pensjonsordningar" },
            )
        }
}

// InfoAvdodPenFraAndre_001
object InfoAvdoedPenFraAndre : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. " +
                        "Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet." },
                nynorsk { + "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, må du kontakte arbeidsgivaren til avdøde. " +
                        "Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet." },
            )
        }
}

// HarBarnUnder18Overskrift_001
object HarBarnUnder18Overskrift : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        title1 {
            text(
                bokmal { + "For deg som har barn under 18 år" },
                nynorsk { + "For deg som har barn under 18 år" },
            )
        }
}

// HarBarnUtenBT_001
object HarBarnUtenBarnetillegg : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette." },
                nynorsk { + "Har du barn under 18 år, og ikkje er innvilga barnetillegg, kan du søkje om dette." },
            )
        }
}

// HarBarnUnder18_001
object HarBarnUnder18 : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. " +
                        "I tillegg kan barn ha rett til barnepensjon. " +
                        "Du finner søknadsskjema og mer informasjon om dette på $NAV_URL." },
                nynorsk { + "Syter du for barn under 18 år, kan du ha rett til utvida barnetrygd. " +
                        "I tillegg kan barn ha rett til barnepensjon. " +
                        "Du finn søknadsskjema og meir informasjon om dette på $NAV_URL." },
            )
        }
}

// VirkTdsPktUT_001
data class VirkningstidspunktUfoeretrygd(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Uføretrygden din er omregnet fra " + kravVirkedatoFom.format() + "." },
                nynorsk { + "Uføretrygda di er rekna om frå " + kravVirkedatoFom.format() + "." },
            )
        }
}

// VirkTdsPktUTIkkeEndring_001
data class VirkningstidspunktUfoeretrygdIngenEndring(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Uføretrygden din er omregnet fra "
                        + kravVirkedatoFom.format() + ", men dette fører ikke til endring i utbetalingen." },
                nynorsk { + " Uføretrygda di er rekna om frå "
                        + kravVirkedatoFom.format() + ", men det fører ikkje til endring i utbetalinga." },
            )
        }
}

// VirkTdsPktUTBTOmregn_001
data class VirkningstidspunktOmregningBarnetillegg(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Barnetillegget i uføretrygden din er omregnet fra "
                        + kravVirkedatoFom.format() + "." },
                nynorsk { + "Barnetillegget i uføretrygda di er rekna om frå "
                        + kravVirkedatoFom.format() + "." },
            )
        }
}

// VirkTdsPktUTAvkortetTil0_001
data class VirkningstidspunktUfoeretrygdAvkortetTil0(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { + "Uføretrygden din er omregnet fra "
                        + kravVirkedatoFom.format() + ", men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr." },
                nynorsk { + " Uføretrygda di er rekna om frå "
                        + kravVirkedatoFom.format() + ", men det fører ikkje til endring i utbetalinga då uføretrygda er redusert til 0 kr." },
            )
        }
}