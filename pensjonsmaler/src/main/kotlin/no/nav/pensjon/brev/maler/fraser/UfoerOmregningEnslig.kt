package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.ufoer.Barnetillegg.DuHarFaattUtbetaltBarnetilleggTidligereIAar
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
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
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val virkningsdato = kravVirkningsDatoFraOgMed.format()
            showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelletilleggOpphoert) {
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    textExpr(
                        Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har regnet om uføretrygden og barnetillegget ditt fra " +
                            virkningsdato + " fordi sivilstanden din er endret.",
                        Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har rekna om uføretrygda di og barnetillegget ditt frå " +
                            virkningsdato + " fordi sivilstanden din har blitt endra.",
                        English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your disability benefit and child supplement from " +
                            virkningsdato + " because your marital status has changed.",
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har regnet om uføretrygden din fra " +
                            virkningsdato + " fordi sivilstanden din er endret.",
                        Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har rekna om uføretrygda di frå " +
                            virkningsdato + " fordi sivilstanden din er endra.",
                        English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your disability benefit from " +
                            virkningsdato + ", because your marital status has changed.",
                    )
                }
            }.orShow {
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    showIf(harBarnOverfoertTilSaerkullsbarn) {
                        textExpr(
                            Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har regnet om barnetillegget ditt fra " +
                                virkningsdato + " fordi sivilstanden din er endret.",
                            Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er død, og vi har rekna om barnetillegget ditt frå " +
                                virkningsdato + " fordi sivilstanden din er endra.",
                            English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your child supplement from " +
                                virkningsdato + " because your marital status has changed.",
                        )
                    }.orShow {
                        textExpr(
                            Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn +
                                " er død. Uføretrygden og barnetillegget ditt endres ikke.",
                            Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn +
                                " er død. Uføretrygda di og barnetillegget ditt blir ikkje endra.",
                            English to "We have received notice that ".expr() + avdoedNavn +
                                " has died. This will not affect your disability benefit or child supplement.",
                        )
                    }
                }.orShow {
                    textExpr(
                        Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn +
                            " er død. Uføretrygden din endres ikke.",
                        Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn +
                            " er død. Uføretrygda di blir ikkje endra.",
                        English to "We have received notice that ".expr() + avdoedNavn +
                            " has died. This will not affect your disability benefit.",
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
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val faarUtbetaltUfoeretrygd = totalUfoereMaanedligBeloep.greaterThan(0)

        showIf(faarUtbetaltUfoeretrygd) {
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + totalUfoereMaanedligBeloep.format() + " kroner i uføretrygd".expr(),
                    Nynorsk to "Du får ".expr() + totalUfoereMaanedligBeloep.format() + " kroner i uføretrygd".expr(),
                    English to "Your monthly disability benefit".expr(),
                )
                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    text(
                        Bokmal to " og barnetillegg",
                        Nynorsk to " og barnetillegg",
                        English to " and child supplement payment",
                    )
                }
                textExpr(
                    Bokmal to " per måned før skatt.".expr(),
                    Nynorsk to " kvar månad før skatt.".expr(),
                    English to " will be NOK ".expr() + totalUfoereMaanedligBeloep.format() + " before tax.".expr(),
                )
                showIf(harFlereUfoeretrygdPerioder) {
                    text(
                        Bokmal to " Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to " Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                        English to " You can read more about other calculation periods in the appendix.",
                    )
                }
            }
        }.orShow {
            paragraph {
                text(
                    Bokmal to "Du får ikke utbetalt uføretrygd ",
                    Nynorsk to "Du får ikkje utbetalt uføretrygd ",
                    English to "You will not receive disability benefit ",
                )

                showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                    text(
                        Bokmal to "og barnetillegg ",
                        Nynorsk to "og barnetillegg ",
                        English to "and child supplement payment ",
                    )
                }

                showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                    text(
                        Bokmal to "fordi du er under straffegjennomføring.",
                        Nynorsk to "fordi du er under straffegjennomføring.",
                        English to "because you are serving a criminal sentence.",
                    )
                }.orShow {
                    text(
                        Bokmal to "på grunn av høy inntekt.",
                        Nynorsk to "på grunn av høg inntekt.",
                        English to "because of your reported income.",
                    )
                }

                showIf(harFlereUfoeretrygdPerioder) {
                    text(
                        Bokmal to " Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to " Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                        English to " You can read more about other calculation periods in the appendix.",
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
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr() +
                    minsteytelseSatsVedvirk.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på $NAV_URL.".expr(),
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr() +
                    minsteytelseSatsVedvirk.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på $NAV_URL.".expr(),
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr() +
                    minsteytelseSatsVedvirk.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at $NAV_URL.".expr(),
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr() +
                    kompensasjonsgradUfoeretrygdVedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Dette kan ha noko å seie for kompensasjonsgraden din som er fastsett til ".expr() +
                    kompensasjonsgradUfoeretrygdVedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "This may affect your degree of compensation, which is determined to be ".expr() +
                    kompensasjonsgradUfoeretrygdVedvirk.format() + " percent. You can read more about this in the appendix.".expr(),
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
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr() +
                    minsteytelseSats.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på $NAV_URL.".expr(),
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr() +
                    minsteytelseSats.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på $NAV_URL.".expr(),
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr() +
                    minsteytelseSats.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at $NAV_URL.".expr(),
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør ".expr() +
                    inntektFoerUfoerhet.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + " kroner. Kompensasjonsgraden din er satt til ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Endringar i sivilstanden påverkar også inntekta di før du blei ufør. Denne utgjer ".expr() +
                    inntektFoerUfoerhet.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + " kroner. Kompensasjonsgraden din er fastsett til ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "The change in your marital status also affects your income prior to disability, which is determined to be NOK ".expr() +
                    inntektFoerUfoerhet.format() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + ". Your degree of compensation is determined to be ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " percent. You can read more about this in the appendix.".expr(),
            )
        }
    }
}

// EndrMinstIFUDodEPS2_001
data class EndretMinstInntektFoerUfoerhetDoedEPS(
    val inntektFoerUfoerhet: Expression<Kroner>,
    val oppjustertInntektFoerUfoerhet: Expression<Kroner>,
    val kompensasjonsgradUfoeretrygd: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til ".expr() +
                    inntektFoerUfoerhet.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + " kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Inntekta di før du blei ufør er fastsett til minstenivå, som er avhengig av sivilstand. For deg er inntekta di før du blei ufør fastsett til ".expr() +
                    inntektFoerUfoerhet.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + " kroner. Dette kan ha noko å seie for kompensasjonsgraden din, som er fastsett til ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "Your income prior to disability is set to the minimum level, which depends on marital status. The change in your marital status affects your income prior to disability, which is determined to be NOK ".expr() +
                    inntektFoerUfoerhet.format() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr() +
                    oppjustertInntektFoerUfoerhet.format() + ". This may affect your degree of compensation, which has been determined to be ".expr() +
                    kompensasjonsgradUfoeretrygd.format() + " percent. You can read more about this in the appendix.".expr(),
            )
        }
    }
}

// HjemmelSivilstandUTMinsteIFUAvkortet_001, HjemmelSivilstandUTMinsteIFU_001, HjemmelSivilstandUT_001, HjemmelSivilstandUTAvkortet_001

data class HjemmelSivilstandUfoeretrygd(
    val harMinsteinntektFoerUfoerhet: Expression<Boolean>,
    val ufoeretrygdErInntektsavkortet: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(harMinsteinntektFoerUfoerhet) {
            showIf(ufoeretrygdErInntektsavkortet) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13, 12-14 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13, 12-14 and 22-12 of the National Insurance Act.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13 and 22-12 of the National Insurance Act.",
                    )
                }
            }
        }.orShow {
            showIf(ufoeretrygdErInntektsavkortet) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13, 12-14 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 12-13, 12-14 and 22-12 of the National Insurance Act.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 12-13 and 22-12 of the National Insurance Act.",
                    )
                }
            }
        }
    }
}

// HjemmelEPSDodUTInstitusjon_001
object HjemmelEPSDoedInstitusjonUfoeretrygd : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon.",
                Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-19 så lenge du er på institusjon.",
                English to "Your disability benefit is calculated according to the provisions of § 12-19 of the National Insurance Act, as long as you are institutionalized.",
            )
        }
}

// HjemmelEPSDodUTFengsel_001
object HjemmelEPSDoedFengselUfoerUfoeretrygd : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring.",
                Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-20 så lenge du er under straffegjennomføring.",
                English to "Your disability benefit is calculated according to the provisions of § 12-20 of the National Insurance Act, as long as you are serving a criminal sentence.",
            )
        }
}

// OpphorETOverskrift_001
object OpphoerEktefelletilleggOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Ektefelletillegget ditt opphører",
                Nynorsk to "Ektefelletillegget ditt avsluttast",
                English to "Your spouse supplement will end",
            )
        }
}

// OpphorET_001
object OpphoerEktefelletillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt.",
                Nynorsk to "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta.",
                English to "You no longer provide for a spousepartnercohabitant. Your spouse supplement will therefore end.",
            )
        }
}

// HjemmelET_001
object HjemmelEktefelletillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8.",
                Nynorsk to "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8.",
                English to "The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit.",
            )
        }
}

// OmregningFBOverskrift_001
object OmregningFellesbarnOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Barnetillegg for fellesbarn er regnet om",
                Nynorsk to "Barnetillegg for fellesbarn er rekna om",
                English to "Your child supplement has been recalculated",
            )
        }
}

data class InfoFellesbarnTilSaerkullsbarn(
    val barnOverfoertTilSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:",
                Nynorsk to "Vi har rekna om barnetillegget for barn som ikkje lenger bur saman med begge foreldre. Dette gjeld:",
                English to "We have recalculated your child supplement for the child/children who no longer lives/live together with both parents. This applies to:",
            )
            list {
                forEach(barnOverfoertTilSaerkullsbarn) { navn ->
                    item {
                        textExpr(
                            Bokmal to navn,
                            Nynorsk to navn,
                            English to navn,
                        )
                    }
                }
            }
        }
}

// InfoTidligereSB_001
data class InfoTidligereSaerkullsbarn(
    val tidligereSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Denne omregningen har også betydning for barnetillegget for:",
                Nynorsk to "Denne omrekninga har også noko å seie for barnetillegget for:",
                English to "This recalculation also affects your child supplement for:",
            )
            list {
                forEach(tidligereSaerkullsbarn) { barn ->
                    item {
                        textExpr(
                            Bokmal to barn,
                            Nynorsk to barn,
                            English to barn,
                        )
                    }
                }
            }
        }
}

// InfoTidligereSBOgEndretUT_001
data class InfoTidligereSaerkullsbarnOgEndretUfoeretrygd(
    val tidligereSaerkullsbarn: Expression<List<String>>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:",
                Nynorsk to "Omrekninga av barnetillegget og endring i uføretrygda di har også noko å seie for barnetillegget for:",
                English to "Recalculation of your child supplement and change in your disability benefit also affects your child supplement for:",
            )
            list {
                forEach(tidligereSaerkullsbarn) { element ->
                    item {
                        textExpr(
                            Bokmal to element,
                            Nynorsk to element,
                            English to element,
                        )
                    }
                }
            }
        }
}

// EndringUTpavirkerBTOverskrift_001
object EndringUfoeretrygdPaavirkerBarnetilleggOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Slik påvirker endring av uføretrygden barnetillegget ditt",
                Nynorsk to "Slik påverkar endringa av uføretrygda barnetillegget ditt",
                English to "How the change in your disability benefit affects your child supplement",
            )
        }
}

// InfoBTSBInntekt_001
object InfoBarnetilleggSaerkullsbarnInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. " +
                    "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribeløp. " +
                    "Du kan enkelt melde fra om inntektsendringer på $NAV_URL.",
                Nynorsk to "Inntekta di påverkar det du får i barnetillegg. " +
                    "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribeløp. " +
                    "Du kan enkelt melde frå om inntektsendringar på $NAV_URL.",
                English to "Your income affects how much child supplement you receive. " +
                    "If your income is over the limit for receiving full child supplement, your child supplement will be reduced. " +
                    "This limit is called the exemption amount. " +
                    "You can easily report changes in your income at $NAV_URL.",
            )
        }
}

// InfoBTOverfortTilSBInntekt_001
object InfoBarnetilleggOverfortTilSaerkullsbarnInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. " +
                    "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribeløp. " +
                    "Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " +
                    "Du kan enkelt melde fra om inntektsendringer på $NAV_URL.",
                Nynorsk to "Inntekta di påverkar det du får i barnetillegg. " +
                    "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribeløp. " +
                    "Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn. " +
                    "Du kan enkelt melde frå om inntektsendringar på $NAV_URL.",
                English to "Your income affects how much child supplement you receive. " +
                    "If your income is over the limit for receiving full child supplement, your child supplement will be reduced. " +
                    "This limit is called the exemption amount. " +
                    "The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child. You can easily report changes in your income at $NAV_URL.",
            )
        }
}

// IkkeRedusBTSBPgaInntekt_001
data class IkkeRedusertBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnFribeloep: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er lavere enn fribeløpet ditt på ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + " kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.".expr(),
                Nynorsk to "Inntekta di på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er lågare enn fribeløpet ditt på ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + " kroner. Derfor er barnetillegget ikkje redusert ut frå inntekt.".expr(),
                English to "Your income of NOK ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " is lower than your exemption amount of NOK ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + ". Therefore, your child supplement has not been reduced on the basis of your income.".expr(),
            )
        }
}

// RedusBTSBPgaInntekt_001
data class RedusertBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnFribeloep: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er høyere enn fribeløpet ditt på ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + " kroner. Derfor er barnetillegget redusert ut fra inntekt.".expr(),
                Nynorsk to "Inntekta di på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er høgare enn fribeløpet ditt på ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + " kroner. Derfor er barnetillegget redusert ut frå inntekt.".expr(),
                English to "Your income of NOK ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " is higher than your exemption amount of NOK ".expr() +
                    barnetilleggSaerkullsbarnFribeloep.format() + ". Therefore, your child supplement has been reduced on the basis of your income.".expr(),
            )
        }
}

// JusterBelopRedusBTPgaInntekt_001
object JusterBeloepRedusertBarnetilleggPgaInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)

            text(
                Bokmal to " Dette ble tatt hensyn til da vi endret barnetillegget.",
                Nynorsk to " Det blei teke omsyn til dette då vi endra barnetillegget.",
                English to " This was taken into account when we changed your child supplement.",
            )
        }
}

// JusterBelopIkkeUtbetaltBTPgaInntekt_001
object IkkeUtbetaltBarnetilleggPgaInntektOgJusteringsbelop : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
            text(
                Bokmal to " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to " You have already received the amount you are entitled to this year, and therefore you will not receive child supplement for the remainder of the year.",
            )
        }
}

// IkkeUtbetaltBTSBPgaInntekt_001
data class IkkeUtbetaltBarnetilleggSaerkullsbarnPgaInntekt(
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Expression<Kroner>,
    val barnetilleggSaerkullsbarnInntektstak: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er over ".expr() +
                    barnetilleggSaerkullsbarnInntektstak.format() + " kroner som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg.".expr(),
                Nynorsk to "Inntekta di på ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner er over ".expr() +
                    barnetilleggSaerkullsbarnInntektstak.format() + " kroner, som er grensa for å få utbetalt barnetillegg. Derfor får du ikkje utbetalt barnetillegg.".expr(),
                English to "Your income of NOK ".expr() +
                    barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " is over the income limit for receiving a child supplement, which is NOK ".expr() +
                    barnetilleggSaerkullsbarnInntektstak.format() + ". Therefore, you will not receive child supplement.".expr(),
            )
        }
}

// HjemmelBT_001
object HjemmelBarnetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15.",
                English to "This decision was made pursuant to the provisions § 12-15 of the National Insurance Act.",
            )
        }
}

// HjemmelBTRedus_001
object HjemmelBarnetilleggRedusert : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16.",
                English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act.",
            )
        }
}

// MerInfoBT_001
object MerInfoBarnetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget.",
                Nynorsk to "Du kan lese meir om utrekninga av barnetillegg i vedlegget.",
                English to "You can read more about how child supplement is calculated in the appendix.",
            )
        }
}

// HvordanSoekerDuOverskrift_001
object HvordanSoekerDuOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Hvordan søker du?",
                Nynorsk to "Korleis søkjer du?",
                English to "How do you apply?",
            )
        }
}

object RettTilUfoeretrygdVedGradertUfoeretrygd : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad",
                Nynorsk to "Når du har gradert uføretrygd, kan du ha rett til omstillingsstønad",
                English to "When you have partial disability benefit, you may be entitled to an adjustment allowance",
            )
        }
        paragraph {
            text(
                Bokmal to "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år. Omstillingsstønad er pensjonsgivende inntekt, og kan derfor påvirke hva du får utbetalt i uføretrygd.",
                Nynorsk to "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Omstillingsstønad er pensjonsgivande inntekt, og kan derfor påverke kva du får utbetalt i uføretrygd.",
                English to "The adjustment allowance is a time-limited benefit that normally only lasts three years. The adjustment allowance is pensionable income and can therefore affect what you receive in disability benefits.",
            )
        }
    }
}

data class StoerrelseOmstillingsstoenad(val grunnbeloepVedVirk: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hvor mye kan du få i omstillingsstønad?",
                Nynorsk to "Hvor mykje kan du få?",
                English to "How much are you entitled to?",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Stønaden er 2,25 ganger grunnbeløpet i folketrygden per år. ".expr() +
                    "Grunnbeløpet er " + grunnbeloepVedVirk.format() + " kroner. " +
                    "Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det påvirke størrelsen.",
                Nynorsk to "Stønaden er 2,25 gongar grunnbeløpet i folketrygda per år. ".expr() +
                    "Grunnbeløpet er " + grunnbeloepVedVirk.format() + " kronar. " +
                    "Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke stønaden.",
                English to "The allowance is 2.25 times the basic amount in the National Insurance Scheme per year. ".expr() +
                    "The basic amount is NOK " + grunnbeloepVedVirk.format() + ". " +
                    "If the deceased lived outside Norway after the age of 16, it may affect the amount.",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du har arbeidsinntekt ved siden av uføretrygden din, blir omstillingsstønaden redusert med 45 prosent av den inntekten som er over halve grunnbeløpet. " +
                    "Noen ytelser, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt.",
                Nynorsk to "Viss du har arbeidsinntekt ved sida av uføretrygda di, blir stønaden redusert med 45 prosent av den inntekta som er over halve grunnbeløpet. " +
                    "Nokre ytingar, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt.",
                English to "If you have earned income in addition to your disability benefit, the adjustment allowance will be reduced by 45 percent of the income that exceeds half the National Insurance basic amount. " +
                    "Some benefits, such as sickness benefits and unemployment benefits, are considered earned income.",
            )
        }
    }
}

object HvemHarRettPaaOmstillingsstoenad : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hvem kan ha rett til omstillingstønad?",
                Nynorsk to "Kven kan ha rett til omstillingsstønad?",
                English to "Who is entitled to an adjustment allowance?",
            )
        }
        paragraph {
            text(
                Bokmal to "For å ha rett til omstillingsstønaden må du ved dødsfallet som hovedregel:",
                Nynorsk to "For å ha rett til stønaden må du ved dødsfallet som hovudregel:",
                English to "To be entitled to an adjustment allowance, you at the time of the death must as a rule:",
            )
            list {
                item {
                    text(
                        Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                        Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                        English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death",
                    )
                }
                item {
                    text(
                        Bokmal to "ha vært gift med den avdøde i minst fem år, eller",
                        Nynorsk to "ha vore gift med den avdøde i minst fem år, eller",
                        English to "have been married to the deceased for at least five years, or",
                    )
                }
                item {
                    text(
                        Bokmal to "ha vært gift eller samboer med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                        Nynorsk to "ha vore gift eller sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                        English to "have been married to or a cohabitant with the deceased, and have/had children together, or",
                    )
                }
                item {
                    text(
                        Bokmal to "ha omsorg for barn minst halvparten av tiden.",
                        Nynorsk to "ha omsorg for barn minst halvparten av full tid.",
                        English to "care for a child at least half the time.",
                    )
                }
            }
        }
    }
}

data class SoekGjenlevendetilleggEtter2024(val borIAvtaleLand: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Vi oppfordrer deg til å søke om omstillingsstønaden så snart som mulig fordi vi vanligvis bare etterbetaler for tre måneder.",
                Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader.",
                English to "We encourage you to apply as soon as possible because we normally only pay retroactively for three months.",
            )
        }
        paragraph {
            text(
                Bokmal to "Du finner mer informasjon og søknad på nav.no/omstillingsstonad.",
                Nynorsk to "Du finn informasjon og søknad på nav.no/omstillingsstonad.",
                English to "You will find information and the application form at nav.no/omstillingsstonad.",
            )
        }
        showIf(borIAvtaleLand) {
            paragraph {
                text(
                    Bokmal to "Hvis du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
                    Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdemyndigheitene i bustadlandet ditt.",
                    English to "If you live outside Norway, you must contact the National Insurance authority in your country of residence.",
                )
            }
        }
    }
}

// AvdodBoddArbUtlandOverskrift_001
object AvdoedBoddArbeidetIUtlandOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                Nynorsk to "Rettar dersom avdøde har budd eller arbeidd i utlandet",
                English to "Rights if the deceased has lived or worked abroad",
            )
        }
}

// AvdodBoddArbUtland2_001
data class AvdoedBoddEllerArbeidetIUtland(val borIAvtaleland: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet, kan det påvirke hvor mye du får utbetalt. Norge har trygdesamarbeid med mange land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. ",
                Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan det påverke kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. ",
                English to "If the deceased has lived or worked abroad, this may affect your payment. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also have rights from other countries. ",
            )
            showIf(not(borIAvtaleland)) {
                text(
                    Bokmal to "Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                    Nynorsk to "Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                    English to "We can assist you with your application to countries with which Norway has a social security agreement.",
                )
            }
        }
}

// PensjonFraAndreOverskrift_001
object PensjonFraAndreOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "Andre pensjonsordninger",
                Nynorsk to "Andre pensjonsordningar",
                English to "Other pension schemes",
            )
        }
}

// InfoAvdodPenFraAndre_001
object InfoAvdoedPenFraAndre : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. " +
                    "Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, må du kontakte arbeidsgivaren til avdøde. " +
                    "Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you must contact the deceased's employer. " +
                    "You can also contact the pension scheme or insurance company.",
            )
        }
}

// HarBarnUnder18Overskrift_001
object HarBarnUnder18Overskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        title1 {
            text(
                Bokmal to "For deg som har barn under 18 år",
                Nynorsk to "For deg som har barn under 18 år",
                English to "If you have children under the age of 18",
            )
        }
}

// HarBarnUtenBT_001
object HarBarnUtenBarnetillegg : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette.",
                Nynorsk to "Har du barn under 18 år, og ikkje er innvilga barnetillegg, kan du søkje om dette.",
                English to "If you have children under the age of 18, and have not been granted a child supplement, you can apply for this.",
            )
        }
}

// HarBarnUnder18_001
object HarBarnUnder18 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finner søknadsskjema og mer informasjon om dette på $NAV_URL.",
                Nynorsk to "Syter du for barn under 18 år, kan du ha rett til utvida barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finn søknadsskjema og meir informasjon om dette på $NAV_URL.",
                English to "If you provide for children under the age of 18, you may be entitled to extended child benefit. " +
                    "In addition, children may be entitled to a children's pension. " +
                    "You will find the application form and more information about this at $NAV_URL.",
            )
        }
}

// VirkTdsPktUT_001
data class VirkningstidspunktUfoeretrygd(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() + kravVirkedatoFom.format() + ".",
                Nynorsk to "Uføretrygda di er rekna om frå ".expr() + kravVirkedatoFom.format() + ".",
                English to "Your disability benefit has been recalculated from ".expr() + kravVirkedatoFom.format() + ".",
            )
        }
}

// VirkTdsPktUTIkkeEndring_001
data class VirkningstidspunktUfoeretrygdIngenEndring(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() +
                    kravVirkedatoFom.format() + ", men dette fører ikke til endring i utbetalingen.".expr(),
                Nynorsk to " Uføretrygda di er rekna om frå ".expr() +
                    kravVirkedatoFom.format() + ", men det fører ikkje til endring i utbetalinga.".expr(),
                English to "We have recalculated your disability benefit from ".expr() +
                    kravVirkedatoFom.format() + ", however this will not lead to change in your payment.".expr(),
            )
        }
}

// VirkTdsPktUTBTOmregn_001
data class VirkningstidspunktOmregningBarnetillegg(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Barnetillegget i uføretrygden din er omregnet fra ".expr() +
                    kravVirkedatoFom.format() + ".".expr(),
                Nynorsk to "Barnetillegget i uføretrygda di er rekna om frå ".expr() +
                    kravVirkedatoFom.format() + ".".expr(),
                English to "We have recalculated the child supplement in your disability benefit from ".expr() +
                    kravVirkedatoFom.format() + ".".expr(),
            )
        }
}

// VirkTdsPktUTAvkortetTil0_001
data class VirkningstidspunktUfoeretrygdAvkortetTil0(val kravVirkedatoFom: Expression<LocalDate>) :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() +
                    kravVirkedatoFom.format() + ", men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr.".expr(),
                Nynorsk to " Uføretrygda di er rekna om frå ".expr() +
                    kravVirkedatoFom.format() + ", men det fører ikkje til endring i utbetalinga då uføretrygda er redusert til 0 kr.".expr(),
                English to "We have recalculated your disability benefit from".expr() +
                    kravVirkedatoFom.format() + ". However this will not lead to change in your payment because your disability benefit is reduced to NOK 0.".expr(),
            )
        }
}
