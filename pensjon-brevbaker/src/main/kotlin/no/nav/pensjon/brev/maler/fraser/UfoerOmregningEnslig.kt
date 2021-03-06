package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class OmregnEPSInnledningDto(
    val harMinsteytelseVedVirk: Boolean,
    val inntektFoerUfoereErSannsynligEndret: Boolean,
    val ektefelletilleggOpphoert: Boolean,
    val harBarnetilleggForSaerkullsbarnVedVirk: Boolean,
    val harBarnOverfoertTilSaerkullsbarn: Boolean,
    val avdoedNavn: String,
    val krav_virkningsDatoFraOgMed: LocalDate,
)
// omregnUTBTDodEPSInnledn_001, omregnUTDodEPSInnledn1_001, omregnBTDodEPSInnledn_001, omregnUTBTSBDodEPSInnledn_001, omregnUTDodEPSInnledn2_001
val omregnEPSInnledning = OutlinePhrase<LangBokmalNynorskEnglish, OmregnEPSInnledningDto> { data ->
    val harMinsteytelseVedVirk = data.select(OmregnEPSInnledningDto::harMinsteytelseVedVirk)
    val inntektFoerUfoereErSannsynligEndret = data.select(OmregnEPSInnledningDto::inntektFoerUfoereErSannsynligEndret)
    val ektefelletilleggOpphoert = data.select(OmregnEPSInnledningDto::ektefelletilleggOpphoert)
    val harBarnetilleggForSaerkullsbarnVedVirk = data.select(OmregnEPSInnledningDto::harBarnetilleggForSaerkullsbarnVedVirk)
    val harBarnOverfoertTilSaerkullsbarn = data.select(OmregnEPSInnledningDto::harBarnOverfoertTilSaerkullsbarn)
    val avdoedNavn = data.select(OmregnEPSInnledningDto::avdoedNavn)
    val virkningsDatoFraOgMed = data.select(OmregnEPSInnledningDto::krav_virkningsDatoFraOgMed)

    paragraph {
        showIf(harMinsteytelseVedVirk or inntektFoerUfoereErSannsynligEndret or ektefelletilleggOpphoert) {
            showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                textExpr(
                    Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har regnet om uf??retrygden og barnetillegget ditt fra "
                            + virkningsDatoFraOgMed.format() + " fordi sivilstanden din er endret. Vi vil ogs?? informere deg om rettigheter du kan ha etter avd??de.",
                    Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har rekna om uf??retrygda di og barnetillegget ditt fr?? "
                            + virkningsDatoFraOgMed.format() + " fordi sivilstanden din har blitt endra. Vi vil ogs?? informere deg om kva rettar du kan ha etter avd??de.",
                    English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your disability benefit and child supplement from "
                            + virkningsDatoFraOgMed.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.",
                )
            }.orShow {
                textExpr(
                    Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har regnet om uf??retrygden din fra "
                            + virkningsDatoFraOgMed.format() + " fordi sivilstanden din er endret. Vi vil ogs?? informere deg om rettigheter du kan ha etter avd??de.",
                    Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har rekna om uf??retrygda di fr?? "
                            + virkningsDatoFraOgMed.format() + " fordi sivilstanden din er endra. Vi vil ogs?? informere deg om kva rettar du kan ha etter avd??de.",
                    English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your disability benefit from "
                            + virkningsDatoFraOgMed.format() + ", because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.",
                )
            }
        }.orShow {
            showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                showIf(harBarnOverfoertTilSaerkullsbarn) {
                    textExpr(
                        Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har regnet om barnetillegget ditt fra "
                                + virkningsDatoFraOgMed.format() + " fordi sivilstanden din er endret. Vi vil ogs?? informere deg om rettigheter du kan ha etter avd??de.",
                        Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn + " er d??d, og vi har rekna om barnetillegget ditt fr?? "
                                + virkningsDatoFraOgMed.format() + " fordi sivilstanden din er endra. Vi vil ogs?? informere deg om kva rettar du kan ha etter avd??de.",
                        English to "We have received notice that ".expr() + avdoedNavn + " has died, and we have recalculated your child supplement from "
                                + virkningsDatoFraOgMed.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.",
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn
                                + " er d??d. Uf??retrygden og barnetillegget ditt endres ikke, men vi vil informere deg om rettigheter du kan ha etter avd??de.",
                        Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn
                                + " er d??d. Uf??retrygda di og barnetillegget ditt blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avd??de.",
                        English to "We have received notice that ".expr() + avdoedNavn
                                + " has died. This will not affect your disability benefit or child supplement, but we would like to inform you about rights you may have as a surviving spouse.",
                    )
                }
            }.orShow {
                textExpr(
                    Bokmal to "Vi har mottatt melding om at ".expr() + avdoedNavn
                            + " er d??d. Uf??retrygden din endres ikke, men vi vil informere deg om rettigheter du kan ha etter avd??de.",
                    Nynorsk to "Vi har mottatt melding om at ".expr() + avdoedNavn
                            + " er d??d. Uf??retrygda di blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avd??de.",
                    English to "We have received notice that ".expr() + avdoedNavn
                            + " has died. This will not affect your disability benefit, but we would like to inform you about rights you may have as a surviving spouse.",
                )
            }
        }
    }
}

data class BeloepUTDto(
    val totalUfoereMaanedligBeloep: Kroner,
    val harBarnetilleggForSaerkullsbarnVedVirk: Boolean,
    val harFlereUfoeretrygdPerioder: Boolean,
    val institusjonsoppholdVedVirk: Institusjon,
)

// TODO: Sl?? sammen med Ufoeretrygd.beloep
// BelopUT_001, BelopUTVedlegg_001, BelopUTBT_001, BelopUTBTVedlegg_001, belopUTIngenUtbetaling_001 ,belopUTIngenUtbetalingVedlegg_001 ,belopUTBTIngenUtbetaling_001 ,belopUTBTIngenUtbetalingVedlegg_001 ,belopUTIngenUtbetalingFengsel_001 ,belopUTIngenUtbetalingFengselVedlegg_001
val utbetalingUfoeretrygd = OutlinePhrase<LangBokmalNynorskEnglish, BeloepUTDto> { arg ->

    val totalUfoereMaanedligBeloep = arg.select(BeloepUTDto::totalUfoereMaanedligBeloep)
    val harBarnetilleggForSaerkullsbarnVedVirk = arg.select(BeloepUTDto::harBarnetilleggForSaerkullsbarnVedVirk)
    val harFlereUfoeretrygdPerioder = arg.select(BeloepUTDto::harFlereUfoeretrygdPerioder)
    val institusjonsoppholdVedVirk = arg.select(BeloepUTDto::institusjonsoppholdVedVirk)
    val faarUtbetaltUfoeretrygd = totalUfoereMaanedligBeloep.map{it.value > 0}

    showIf(faarUtbetaltUfoeretrygd){
        paragraph {
            textExpr(
                Bokmal to "Du f??r ".expr() + totalUfoereMaanedligBeloep.format() + " kroner i uf??retrygd".expr(),
                Nynorsk to "Du f??r ".expr() + totalUfoereMaanedligBeloep.format() + " kroner i uf??retrygd".expr(),
                English to "Your monthly disability benefit".expr(),
            )
            showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                    English to " and child supplement payment"
                )
            }
            textExpr(
                Bokmal to " per m??ned f??r skatt.".expr(),
                Nynorsk to " kvar m??nad f??r skatt.".expr(),
                English to " will be NOK ".expr() + totalUfoereMaanedligBeloep.format() + " before tax.".expr()
            )
            showIf(harFlereUfoeretrygdPerioder) {
                text(
                    Bokmal to " Du kan lese mer om andre beregningsperioder i vedlegget.",
                    Nynorsk to " Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                    English to " You can read more about other calculation periods in the appendix."
                )
            }
        }
    }.orShow{
        paragraph {
            text(
                Bokmal to "Du f??r ikke utbetalt uf??retrygd " ,
                Nynorsk to "Du f??r ikkje utbetalt uf??retrygd ",
                English to "You will not receive disability benefit ",
            )

            showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
                text(
                    Bokmal to "og barnetillegg ",
                    Nynorsk to "og barnetillegg ",
                    English to "and child supplement payment "
                )
            }

            showIf(institusjonsoppholdVedVirk.isOneOf(Institusjon.FENGSEL)) {
                text(
                    Bokmal to "fordi du er under straffegjennomf??ring.",
                    Nynorsk to "fordi du er under straffegjennomf??ring.",
                    English to "because you are serving a criminal sentence."
                )
            }.orShow {
                text(
                    Bokmal to "p?? grunn av h??y inntekt.",
                    Nynorsk to "p?? grunn av h??g inntekt.",
                    English to "because of your reported income."
                )
            }

            showIf(harFlereUfoeretrygdPerioder) {
                text(
                    Bokmal to " Du kan lese mer om andre beregningsperioder i vedlegget.",
                    Nynorsk to " Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                    English to " You can read more about other calculation periods in the appendix."
                )
            }
        }
    }

}

data class EndrMYDodEPS2_001Dto(
    val minsteytelse_sats_vedvirk: Double,
    val kompensasjonsgrad_ufoeretrygd_vedvirk: Double
)

val endrMYDodEPS2_001 = OutlinePhrase<LangBokmalNynorskEnglish, EndrMYDodEPS2_001Dto> {
    val minsteytelse_sats_vedvirk = it.select(EndrMYDodEPS2_001Dto::minsteytelse_sats_vedvirk)
    val kompensasjonsgrad_ufoeretrygd_vedvirk = it.select(EndrMYDodEPS2_001Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minsteniv??et for uf??retrygd. Satsen p?? minsteytelsen avhenger av sivilstand. For deg utgj??r minsteytelsen ".expr()
                    + minsteytelse_sats_vedvirk.format() + " ganger folketrygdens grunnbel??p. Du kan lese mer om grunnbel??p p?? nav.no.".expr(),
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut fr?? den eigenopptente inntekta di er l??gare enn minsteniv??et for uf??retrygd. Satsen p?? minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr()
                    + minsteytelse_sats_vedvirk.format() + " gonger grunnbel??pet i folketrygda. Du kan lese meir om grunnbel??p p?? nav.no.".expr(),
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr()
                    + minsteytelse_sats_vedvirk.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Dette kan ha noko ?? seie for kompensasjonsgraden din som er fastsett til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
            English to "This may affect your degree of compensation, which is determined to be ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " percent. You can read more about this in the appendix.".expr()
        )
    }
}


data class EndrMYOgMinstIFUDodEPS2_001Dto(
    val minsteytelse_sats_vedvirk: Double,
    val inntekt_foer_ufoerhet_vedvirk: Kroner,
    val oppjustert_inntekt_foer_ufoerhet_vedvirk: Kroner,
    val kompensasjonsgrad_ufoeretrygd_vedvirk: Double
)

val endrMYOgMinstIFUDodEPS2_001 = OutlinePhrase<LangBokmalNynorskEnglish, EndrMYOgMinstIFUDodEPS2_001Dto> { dto ->
    val minsteytelse_sats_vedvirk = dto.select(EndrMYOgMinstIFUDodEPS2_001Dto::minsteytelse_sats_vedvirk)


    val kompensasjonsgrad_ufoeretrygd_vedvirk =
        dto.select(EndrMYOgMinstIFUDodEPS2_001Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minsteniv??et for uf??retrygd. Satsen p?? minsteytelsen avhenger av sivilstand. For deg utgj??r minsteytelsen ".expr()
                    + minsteytelse_sats_vedvirk.format() + " ganger folketrygdens grunnbel??p. Du kan lese mer om grunnbel??p p?? nav.no.".expr(),
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut fr?? den eigenopptente inntekta di er l??gare enn minsteniv??et for uf??retrygd. Satsen p?? minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr()
                    + minsteytelse_sats_vedvirk.format() + " gonger grunnbel??pet i folketrygda. Du kan lese meir om grunnbel??p p?? nav.no.".expr(),
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr()
                    + minsteytelse_sats_vedvirk.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
        )
    }

    val oppjustert_inntekt_foer_ufoerhet_vedvirk = dto.map { it.oppjustert_inntekt_foer_ufoerhet_vedvirk }
    val inntekt_foer_ufoerhet_vedvirk = dto.map { it.inntekt_foer_ufoerhet_vedvirk }
    paragraph {
        textExpr(
            Bokmal to "Sivilstandsendring har ogs?? betydning for inntekten din f??r du ble uf??r. Denne utgj??r ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt p?? ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Kompensasjonsgraden din er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Endringar i sivilstanden p??verkar ogs?? inntekta di f??r du blei uf??r. Denne utgjer ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt p?? ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Kompensasjonsgraden din er fastsett til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
            English to "The change in your marital status also affects your income prior to disability, which is determined to be NOK ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + ". Adjusted to today???s value, this is equivalent to an income of NOK ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + ". Your degree of compensation is determined to be ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " percent. You can read more about this in the appendix.".expr()
        )
    }
}

data class EndrMinstIFUDodEPS2_001Dto(
    val inntekt_foer_ufoerhet_vedvirk: Kroner,
    val oppjustert_inntekt_foer_ufoerhet_vedvirk: Kroner,
    val kompensasjonsgrad_ufoeretrygd_vedvirk: Double,
)

val endrMinstIFUDodEPS2_001 = OutlinePhrase<LangBokmalNynorskEnglish, EndrMinstIFUDodEPS2_001Dto> {
    val inntekt_foer_ufoerhet_vedvirk = it.select(EndrMinstIFUDodEPS2_001Dto::inntekt_foer_ufoerhet_vedvirk)
    val oppjustert_inntekt_foer_ufoerhet_vedvirk =
        it.select(EndrMinstIFUDodEPS2_001Dto::oppjustert_inntekt_foer_ufoerhet_vedvirk)
    val kompensasjonsgrad_ufoeretrygd_vedvirk =
        it.select(EndrMinstIFUDodEPS2_001Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Inntekten din f??r du ble uf??r er fastsatt til minsteniv?? som er avhengig av sivilstand. For deg er inntekten din f??r du ble uf??r satt til ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt p?? ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Inntekta di f??r du blei uf??r er fastsett til minsteniv??, som er avhengig av sivilstand. For deg er inntekta di f??r du blei uf??r fastsett til ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt p?? ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Dette kan ha noko ?? seie for kompensasjonsgraden din, som er fastsett til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
            English to "Your income prior to disability is set to the minimum level, which depends on marital status. The change in your marital status affects your income prior to disability, which is determined to be NOK ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + ". Adjusted to today???s value, this is equivalent to an income of NOK ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + ". This may affect your degree of compensation, which has been determined to be ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " percent. You can read more about this in the appendix.".expr()
        )
    }
}

data class HjemmelSivilstandUfoeretrygdDto(
    val harMinsteinntektFoerUfoerhet: Boolean,
    val ufoeretrygdVedvirkErInntektsavkortet: Boolean
)

val hjemmelSivilstandUfoeretrygd = OutlinePhrase<LangBokmalNynorskEnglish, HjemmelSivilstandUfoeretrygdDto> {
    val harMinsteinntektFoerUfoerhet = it.select(HjemmelSivilstandUfoeretrygdDto::harMinsteinntektFoerUfoerhet)
    val ufoeretrygdVedvirkErInntektsavkortet =
        it.select(HjemmelSivilstandUfoeretrygdDto::ufoeretrygdVedvirkErInntektsavkortet)


    showIf(harMinsteinntektFoerUfoerhet) {
        showIf(ufoeretrygdVedvirkErInntektsavkortet) {
            //HjemmelSivilstandUTMinsteIFUAvkortet_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-9, 12-13, 12-14 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-9, 12-13, 12-14 og 22-12.",
                    English to "This decision was made pursuant to the provisions of ???? 12-9, 12-13, 12-14 and 22-12 of the National Insurance Act."
                )
            }
        }.orShow {
            //HjemmelSivilstandUTMinsteIFU_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-9, 12-13 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-9, 12-13 og 22-12.",
                    English to "This decision was made pursuant to the provisions of ???? 12-9, 12-13 and 22-12 of the National Insurance Act."
                )
            }
        }
    }.orShow {
        showIf(ufoeretrygdVedvirkErInntektsavkortet) {
            //HjemmelSivilstandUTAvkortet_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-13, 12-14 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-13, 12-14 og 22-12.",
                    English to "This decision was made pursuant to the provisions of ???? 12-13, 12-14 and 22-12 of the National Insurance Act."
                )
            }
        }.orShow {
            //HjemmelSivilstandUT_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-13 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-13 og 22-12.",
                    English to "This decision was made pursuant to the provisions of ???? 12-13 and 22-12 of the National Insurance Act."
                )
            }
        }
    }
}

val hjemmelEPSDodUTInstitusjon_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Uf??retrygden din beregnes etter folketrygdloven ?? 12-19 s?? lenge du er p?? institusjon.",
            Nynorsk to "Uf??retrygda di reknast ut etter folketrygdlova ?? 12-19 s?? lenge du er p?? institusjon.",
            English to "Your disability benefit is calculated according to the provisions of ?? 12-19 of the National Insurance Act, as long as you are institutionalized."
        )
    }
}

val hjemmelEPSDodUTFengsel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Uf??retrygden din beregnes etter folketrygdloven ?? 12-20 s?? lenge du er under straffegjennomf??ring.",
            Nynorsk to "Uf??retrygda di reknast ut etter folketrygdlova ?? 12-20 s?? lenge du er under straffegjennomf??ring.",
            English to "Your disability benefit is calculated according to the provisions of ?? 12-20 of the National Insurance Act, as long as you are serving a criminal sentence."
        )
    }
}

val opphorETOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Ektefelletillegget ditt opph??rer",
            Nynorsk to "Ektefelletillegget ditt avsluttast",
            English to "Your spouse supplement will end"
        )
    }
}

val opphorET_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du fors??rger ikke lenger en ektefellepartnersamboer. Derfor opph??rer ektefelletillegget ditt.",
            Nynorsk to "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta.",
            English to "You no longer provide for a spousepartnercohabitant. Your spouse supplement will therefore end."
        )
    }
}

val hjemmelET_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter forskrift om omregning av uf??repensjon til uf??retrygd ?? 8.",
            Nynorsk to "Vedtaket er gjort etter forskrift om overgangsreglar ved innf??ringa av uf??retrygd ?? 8.",
            English to "The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit."
        )
    }
}

val omregningFBOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Barnetillegg for fellesbarn er regnet om",
            Nynorsk to "Barnetillegg for fellesbarn er rekna om",
            English to "Your child supplement has been recalculated"
        )
    }
}

val infoFBTilSB_001 = OutlinePhrase<LangBokmalNynorskEnglish, List<String>> { barnOverfoertTilSaerkullsbarn ->
    paragraph {
        text(
            Bokmal to "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:",
            Nynorsk to "Vi har rekna om barnetillegget for barn som ikkje lenger bur saman med begge foreldre. Dette gjeld:",
            English to "We have recalculated your child supplement for the child/children who no longer lives/live together with both parents. This applies to:"
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

val infoTidligereSB_001 = OutlinePhrase<LangBokmalNynorskEnglish, List<String>> { tidligereSaerkullsbarn ->
    paragraph {
        text(
            Bokmal to "Denne omregningen har ogs?? betydning for barnetillegget for:",
            Nynorsk to "Denne omrekninga har ogs?? noko ?? seie for barnetillegget for:",
            English to "This recalculation also affects your child supplement for:"
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

val infoTidligereSBOgEndretUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, List<String>> { tidligereSaerkullsbarn ->
    paragraph {
        text(
            Bokmal to "Omregningen av barnetillegg og endring i uf??retrygden din har ogs?? betydning for barnetillegget for:",
            Nynorsk to "Omrekninga av barnetillegget og endring i uf??retrygda di har ogs?? noko ?? seie for barnetillegget for:",
            English to "Recalculation of your child supplement and change in your disability benefit also affects your child supplement for:"
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

val endringUTpavirkerBTOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Slik p??virker endring av uf??retrygden barnetillegget ditt",
            Nynorsk to "Slik p??verkar endringa av uf??retrygda barnetillegget ditt",
            English to "How the change in your disability benefit affects your child supplement"
        )
    }
}

data class IkkeRedusBTPgaTak_001Dto(
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Int,
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Kroner,
)

val ikkeRedusBTPgaTak_001 = OutlinePhrase<LangBokmalNynorskEnglish, IkkeRedusBTPgaTak_001Dto> {
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
        it.select(IkkeRedusBTPgaTak_001Dto::barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk)
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
        it.select(IkkeRedusBTPgaTak_001Dto::barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden og barnetillegget ditt kan til sammen ikke utgj??re mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din f??r du ble uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde f??r du ble uf??r tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygden og barnetillegget ditt er til sammen lavere enn dette. Derfor er barnetillegget fastsatt til 40 prosent av folketrygdens grunnbel??p for hvert barn.".expr(),
            Nynorsk to "Uf??retrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di f??r du blei uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde f??r du blei uf??r, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygda di og barnetillegget ditt er til saman l??gare enn dette. Derfor er barnetillegget fastsett til 40 prosent av grunnbel??pet i folketrygda for kvart barn.".expr(),
            English to "Your disability benefit and child supplement together cannot exceed more than ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of your income before you became disabled. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of the income you had before you became disabled is equivalent today to an income of NOK ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + ". Your disability benefit and child supplement together are lower than this. Therefore, your child supplement has been determined to be 40 percent of the National Insurance basic amount for each child.".expr()
        )
    }


}

data class RedusBTPgaTak_001Dto(
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Int,
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Kroner,
    val barnetillegg_beloep_foer_reduksjon_vedvirk: Kroner,
    val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk: Kroner,
)

val redusBTPgaTak_001 = OutlinePhrase<LangBokmalNynorskEnglish, RedusBTPgaTak_001Dto> { dto ->
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
        dto.map { it.barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk }
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
        dto.map { it.barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk }
    val barnetillegg_beloep_foer_reduksjon_vedvirk =
        dto.map { it.barnetillegg_beloep_foer_reduksjon_vedvirk }
    val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk =
        dto.map { it.barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk }
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden og barnetillegget ditt kan til sammen ikke utgj??re mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din f??r du ble uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde f??r du ble uf??r tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygden og barnetillegget ditt er til sammen h??yere enn dette. Derfor er barnetillegget redusert fra ".expr()
                    + barnetillegg_beloep_foer_reduksjon_vedvirk.format() + " kroner til ".expr()
                    + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.format() + " kroner.".expr(),
            Nynorsk to "Uf??retrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di f??r du blei uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde f??r du blei uf??r, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygda og barnetillegget ditt er til saman h??gare enn dette. Derfor er barnetillegget redusert fr?? ".expr()
                    + barnetillegg_beloep_foer_reduksjon_vedvirk.format() + " kroner til ".expr()
                    + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.format() + " kroner.".expr(),
            English to "Your disability benefit and child supplement together cannot exceed more than ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of your income before you became disabled. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of the income you had prior to disability is equivalent today to an income of NOK ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + ". Your disability benefit and child supplement together are higher than this. Therefore, your child supplement has been reduced from NOK ".expr()
                    + barnetillegg_beloep_foer_reduksjon_vedvirk.format() + " to NOK ".expr()
                    + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.format() + ".".expr()
        )
    }


}

data class IkkeUtbetaltBTPgaTak_001Dto(
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Int,
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Kroner,
)

val ikkeUtbetaltBTPgaTak_001 = OutlinePhrase<LangBokmalNynorskEnglish, IkkeUtbetaltBTPgaTak_001Dto> {
    val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
        it.select(IkkeUtbetaltBTPgaTak_001Dto::barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk)
    val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
        it.select(IkkeUtbetaltBTPgaTak_001Dto::barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden og barnetillegget ditt kan til sammen ikke utgj??re mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din f??r du ble uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde f??r du ble uf??r tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygden og barnetillegget ditt er til sammen h??yere enn dette. Derfor f??r du ikke utbetalt barnetillegg.".expr(),
            Nynorsk to "Uf??retrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di f??r du blei uf??r. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde f??r du blei uf??r, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uf??retrygda og barnetillegget ditt er til saman h??gare enn dette. Derfor f??r du ikkje utbetalt barnetillegg.".expr(),
            English to "Your disability benefit and child supplement together cannot exceed more than ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of your income before you became disabled. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " percent of the income you had prior to disability is equivalent today to an income of NOK ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + ". Your disability benefit and child supplement together are higher than this. Therefore, you will not receive child supplement.".expr()
        )
    }
}

val infoBTSBInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Inntekten din har betydning for hva du f??r i barnetillegg. " +
                    "Er inntekten din over grensen for ?? f?? utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribel??p. " +
                    "Du kan enkelt melde fra om inntektsendringer p?? nav.no.",
            Nynorsk to "Inntekta di p??verkar det du f??r i barnetillegg. " +
                    "Er inntekta di over grensa for ?? f?? utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribel??p. " +
                    "Du kan enkelt melde fr?? om inntektsendringar p?? nav.no.",
            English to "Your income affects how much child supplement you receive. " +
                    "If your income is over the limit for receiving full child supplement, your child supplement will be reduced. " +
                    "This limit is called the exemption amount. " +
                    "You can easily report changes in your income at nav.no."
        )
    }
}

val infoBTOverfortTilSBInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Inntekten din har betydning for hva du f??r i barnetillegg. " +
                    "Er inntekten din over grensen for ?? f?? utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribel??p. " +
                    "Fribel??pet for ett barn er 3,1 ganger folketrygdens grunnbel??p og det ??ker med 40 prosent av folketrygdens grunnbel??p for hvert ekstra barn. " +
                    "Du kan enkelt melde fra om inntektsendringer p?? nav.no.",

            Nynorsk to "Inntekta di p??verkar det du f??r i barnetillegg. " +
                    "Er inntekta di over grensa for ?? f?? utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribel??p. " +
                    "Fribel??pet for eit barn er 3,1 gonger grunnbel??pet i folketrygda og det aukar med 40 prosent av grunnbel??pet for kvart ekstra barn. " +
                    "Du kan enkelt melde fr?? om inntektsendringar p?? nav.no.",

            English to "Your income affects how much child supplement you receive. " +
                    "If your income is over the limit for receiving full child supplement, your child supplement will be reduced. " +
                    "This limit is called the exemption amount. " +
                    "The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child. You can easily report changes in your income at nav.no."
        )
    }
}

data class IkkeRedusBTSBPgaInntekt_001Dto(
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Kroner,
    val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Kroner,
)

val ikkeRedusBTSBPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, IkkeRedusBTSBPgaInntekt_001Dto> {
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
        it.select(IkkeRedusBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
    val barnetillegg_saerkullsbarn_fribeloep_vedvirk =
        it.select(IkkeRedusBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_fribeloep_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Inntekten din p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er lavere enn fribel??pet ditt p?? ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.".expr(),
            Nynorsk to "Inntekta di p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er l??gare enn fribel??pet ditt p?? ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget ikkje redusert ut fr?? inntekt.".expr(),
            English to "Your income of NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " is lower than your exemption amount of NOK ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + ". Therefore, your child supplement has not been reduced on the basis of your income.".expr()
        )
    }
}

data class RedusBTSBPgaInntekt_001Dto(
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Kroner,
    val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Kroner,
)

val redusBTSBPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, RedusBTSBPgaInntekt_001Dto> {
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
        it.select(RedusBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
    val barnetillegg_saerkullsbarn_fribeloep_vedvirk =
        it.select(RedusBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_fribeloep_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Inntekten din p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er h??yere enn fribel??pet ditt p?? ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget redusert ut fra inntekt.".expr(),
            Nynorsk to "Inntekta di p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er h??gare enn fribel??pet ditt p?? ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget redusert ut fr?? inntekt.".expr(),
            English to "Your income of NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " is higher than your exemption amount of NOK ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + ". Therefore, your child supplement has been reduced on the basis of your income.".expr()
        )
    }
}

val justerBelopRedusBTPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Det du har f??tt utbetalt i barnetillegg tidligere i ??r har ogs?? betydning for hva du f??r i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
            Nynorsk to "Det du har f??tt utbetalt i barnetillegg tidlegare i ??r, p??verkar ogs?? det du f??r i barnetillegg framover. Det blei teke omsyn til dette d?? vi endra barnetillegget.",
            English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. This was taken into account when we changed your child supplement."
        )
    }
}

val justerBelopIkkeUtbetaltBTPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Det du har f??tt utbetalt i barnetillegg tidligere i ??r har ogs?? betydning for hva du f??r i barnetillegg framover. " +
                    "Du har allerede f??tt utbetalt det du har rett til i ??r, og f??r derfor ikke utbetalt barnetillegg for resten av ??ret.",

            Nynorsk to "Det du har f??tt utbetalt i barnetillegg tidlegare i ??r, p??verkar ogs?? det du f??r i barnetillegg framover. " +
                    "Du har allereie f??tt utbetalt det du har rett til i ??r, og f??r derfor ikkje utbetalt barnetillegg for resten av ??ret.",

            English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. " +
                    "You have already received the amount you are entitled to this year, and therefore you will not receive child supplement for the remainder of the year."
        )
    }
}

data class IkkeUtbetaltBTSBPgaInntekt_001Dto(
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Kroner,
    val barnetillegg_saerkullsbarn_inntektstak_vedvirk: Kroner,
)

val ikkeUtbetaltBTSBPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, IkkeUtbetaltBTSBPgaInntekt_001Dto> {
    val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
        it.select(IkkeUtbetaltBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
    val barnetillegg_saerkullsbarn_inntektstak_vedvirk =
        it.select(IkkeUtbetaltBTSBPgaInntekt_001Dto::barnetillegg_saerkullsbarn_inntektstak_vedvirk)
    paragraph {
        textExpr(
            Bokmal to "Inntekten din p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er over ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + " kroner som er grensen for ?? f?? utbetalt barnetillegg. Derfor f??r du ikke utbetalt barnetillegg.".expr(),
            Nynorsk to "Inntekta di p?? ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er over ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + " kroner, som er grensa for ?? f?? utbetalt barnetillegg. Derfor f??r du ikkje utbetalt barnetillegg.".expr(),
            English to "Your income of NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " is over the income limit for receiving a child supplement, which is NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + ". Therefore, you will not receive child supplement.".expr()
        )
    }
}

val hjemmelBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven ?? 12-15.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova ?? 12-15.",
            English to "This decision was made pursuant to the provisions ?? 12-15 of the National Insurance Act."
        )
    }
}

val hjemmelBTOvergangsregler_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven ?? 12-15 og forskrift om overgangsregler for barnetillegg i uf??retrygden.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova ?? 12-15 og forskrift om overgangsreglar for barnetillegg i uf??retrygda.",
            English to "This decision was made pursuant to the provisions of ?? 12-15 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
        )
    }
}

val hjemmelBTRedus_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-15 og 12-16.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-15 og 12-16.",
            English to "This decision was made pursuant to the provisions of ???? 12-15 and 12-16 of the National Insurance Act."
        )
    }
}

val hjemmelBTRedusOvergangsregler_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven ???? 12-15 og 12-16 og forskrift om overgangsregler for barnetillegg i uf??retrygden.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova ???? 12-15 og 12-16 og forskrift om overgangsreglar for barnetillegg i uf??retrygda.",
            English to "This decision was made pursuant to the provisions of ???? 12-15 and 12-16 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
        )
    }
}

val merInfoBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget.",
            Nynorsk to "Du kan lese meir om utrekninga av barnetillegg i vedlegget.",
            English to "You can read more about how child supplement is calculated in the appendix."
        )
    }
}

val gjRettSamboerOverskrift = OutlinePhrase<LangBokmalNynorskEnglish, String> { avdoed_navn ->
    title1 {
        textExpr(
            Bokmal to "Rettigheter du kan ha som tidligere samboer med ".expr()
                    + avdoed_navn,
            Nynorsk to "Rettar du kan ha som tidlegare sambuar med ".expr()
                    + avdoed_navn,
            English to "Rights you may be entitled to as a former cohabitant with ".expr()
                    + avdoed_navn
        )
    }

}

val gjRettUTSamboer_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Samboere som tidligere har v??rt gift, eller som har eller har hatt felles barn, kan ha rett til gjenlevendetillegg i uf??retrygden. " +
                    "Du finner mer informasjon og s??knadsskjema for gjenlevende ektefelle, partner eller samboer p?? nav.no.",
            Nynorsk to "Sambuarar som tidlegare har vore gift, eller som har eller har hatt felles barn, kan ha rett til attlevandetillegg i uf??retrygda. " +
                    "Du finn meir informasjon og s??knadsskjema for attlevande ektefelle, partnar eller sambuar p?? nav.no.",
            English to "Cohabitants who have previously been married, or who have or have had children together, may be entitled to survivor's supplement to disability benefit. " +
                    "You will find more information and the application form for benefits for surviving spouse, partner or cohabitant at nav.no."
        )
    }
}

val rettTilUTGJTOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du kan ha rett til gjenlevendetillegg i uf??retrygden",
            Nynorsk to "Du kan ha rett til attlevandetillegg i uf??retrygda",
            English to "You might be entitled to a survivor's supplement to disability benefit"
        )
    }
}

val hvemUTGJTVilkar_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "For ?? ha rett til gjenlevendetillegg i uf??retrygden, m?? du som hovedregel:",
            Nynorsk to "For ?? ha rett til attlevandetillegg i uf??retrygda, m?? du som hovudregel:",
            English to "To be entitled to a survivor's supplement to disability benefit, you must as a rule:",
        )
        list {
            item {
                text(
                    Bokmal to "v??re medlem i folketrygden, og avd??de m?? ha v??rt medlem i folketrygden de siste fem ??rene fram til d??dsfallet",
                    Nynorsk to "vere medlem i folketrygda, og avd??de m?? ha vore medlem i folketrygda dei siste fem ??ra fram til d??dsfallet",
                    English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death "
                )
            }
            item {
                text(
                    Bokmal to "ha v??rt gift med den avd??de i minst fem ??r, eller",
                    Nynorsk to "ha vore gift med den avd??de i minst fem ??r, eller",
                    English to "have been married to the deceased for at least five years, or "
                )
            }
            item {
                text(
                    Bokmal to "ha v??rt gift eller v??rt samboer med den avd??de og har eller ha hatt barn med den avd??de, eller",
                    Nynorsk to "ha vore gift eller vore sambuar med den avd??de og ha eller ha hatt barn med den avd??de, eller",
                    English to "have been married to or a cohabitant with the deceased, and have/had children together, or "
                )
            }
            item {
                text(
                    Bokmal to "ha hatt omsorgen for den avd??des barn p?? d??dsfallstidspunktet. " +
                            "Ekteskapet og omsorgen for barnet etter d??dsfallet m?? til sammen ha vart minst fem ??r.",
                    Nynorsk to "ha hatt omsorga for barna til den avd??de p?? d??dsfallstidspunktet. " +
                            "Ekteskapet og omsorga for barnet etter d??dsfallet m?? til saman ha vart minst fem ??r.",
                    English to "have had care of the children of the deceased at the time of the death. " +
                            "The marriage and care of the child after the death must have lasted for at least five years."
                )
            }
        }

    }
    paragraph {
        text(
            Bokmal to "Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avd??de. " +
                    "Du kan lese mer om dette p?? nav.no.",
            Nynorsk to "Sj??lv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avd??de. " +
                    "Du kan lese meir om dette p?? nav.no.",
            English to "Even if you are not entitled to benefits in accordance with the general rules, you may nevertheless have rights as a surviving spouse. " +
                    "You can read more about this at nav.no."
        )
    }
}

val hvordanSoekerDuOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Hvordan s??ker du?",
            Nynorsk to "Korleis s??kjer du?",
            English to "How do you apply?"
        )
    }
}

val soekUTGJT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi oppfordrer deg til ?? s??ke om gjenlevendetillegg i uf??retrygden s?? snart som mulig fordi vi vanligvis kun etterbetaler for tre m??neder. " +
                    "Du finner informasjon og s??knadsskjemaet for gjenlevende ektefelle, partner eller samboer p?? nav.no/gjenlevendeektefelle",
            Nynorsk to "Vi oppmodar deg til ?? s??kje om attlevandetillegg i uf??retrygda s?? snart som mogleg fordi vi vanlegvis berre etterbetaler for tre m??nader. " +
                    "Du finn informasjon og s??knadsskjemaet for attlevande ektefelle, partner eller sambuar p?? nav.no/gjenlevendeektefelle",
            English to "We encourage you to apply for survivor's supplement to disability benefit as soon as possible because we normally only pay retroactively for three months. " +
                    "You will find information and the application form for a surviving spouse, partner or cohabitant at nav.no/gjenlevendeektefelle"
        )
    }

}

val soekAvtaleLandUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Dersom du bor i utlandet, m?? du kontakte trygdemyndigheter i bostedslandet ditt og s??ke om ytelser etter avd??de.",
            Nynorsk to "Dersom du bur i utlandet, m?? du kontakte trygdeorgana i bustadslandet ditt og s??kje om ytingar etter avd??de.",
            English to "If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension."
        )
    }
}

val avdodBoddArbUtlandOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Rettigheter hvis avd??de har bodd eller arbeidet i utlandet",
            Nynorsk to "Rettar dersom avd??de har budd eller arbeidd i utlandet",
            English to "Rights if the deceased has lived or worked abroad"
        )
    }
}

val avdodBoddArbUtland2_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvis avd??de har bodd eller arbeidet i utlandet kan dette f?? betydning for hvor mye du f??r ubetalt i gjenlevendetillegg. " +
                    "Norge har trygdesamarbeid med en rekke land gjennom E??S-avtalen og andre avtaler. " +
                    "Derfor kan du ogs?? ha rett til pensjon fra andre land. " +
                    "Vi kan hjelpe deg med s??knad til land Norge har trygdeavtale med.",

            Nynorsk to "Dersom avd??de har budd eller arbeidd i utlandet, kan dette f?? noko ?? seie for kor mykje du f??r ubetalt i attlevandetillegg. " +
                    "Noreg har trygdesamarbeid med ei rekkje land gjennom E??S-avtalen og andre avtalar. " +
                    "Derfor kan du ogs?? ha rett til pensjon fr?? andre land. " +
                    "Vi kan hjelpe deg med s??knad til land Noreg har trygdeavtale med.",

            English to "If the deceased has lived or worked abroad, this may affect the amount of your survivor's supplement. " +
                    "Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. " +
                    "You may therefore also be entitled to a pension from other countries. " +
                    "We can assist you with your application to countries with which Norway has a social security agreement."
        )
    }
}

val pensjonFraAndreOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Andre pensjonsordninger",
            Nynorsk to "Andre pensjonsordningar",
            English to "Other pension schemes"
        )
    }
}

val infoAvdodPenFraAndre_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Dersom avd??de hadde en privat eller offentlig pensjonsordning og du har sp??rsm??l om dette, m?? du kontakte avd??des arbeidsgiver. " +
                    "Du kan ogs?? ta kontakt med pensjonsordningen eller forsikringsselskapet.",
            Nynorsk to "Dersom avd??de hadde ei privat eller offentleg pensjonsordning og du har sp??rsm??l om dette, m?? du kontakte arbeidsgivaren til avd??de. " +
                    "Du kan ogs?? ta kontakt med pensjonsordninga eller forsikringsselskapet.",
            English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you must contact the deceased's employer. " +
                    "You can also contact the pension scheme or insurance."
        )
    }
}

val harBarnUnder18Overskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "For deg som har barn under 18 ??r",
            Nynorsk to "For deg som har barn under 18 ??r",
            English to "If you have children under the age of 18"
        )
    }
}

val harBarnUtenBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Har du barn under 18 ??r, og ikke er innvilget barnetillegg, kan du s??ke om dette.",
            Nynorsk to "Har du barn under 18 ??r, og ikkje er innvilga barnetillegg, kan du s??kje om dette.",
            English to "If you have children under the age of 18, and have not been granted a child supplement, you can apply for this."
        )
    }
}

val harBarnUnder18_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Fors??rger du barn under 18 ??r, kan du ha rett til utvidet barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finner s??knadsskjema og mer informasjon om dette p?? nav.no.",
            Nynorsk to "Syter du for barn under 18 ??r, kan du ha rett til utvida barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finn s??knadsskjema og meir informasjon om dette p?? nav.no.",
            English to "If you provide for children under the age of 18, you may be entitled to extended child benefit. " +
                    "In addition, children may be entitled to a children's pension. " +
                    "You will find the application form and more information about this at nav.no."
        )
    }
}

val virknTdsPktOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Dette er virkningstidspunktet ditt",
            Nynorsk to "Dette er verknadstidspunktet ditt",
            English to "This is your effective date"
        )
    }
}

val virkTdsPktUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            Nynorsk to "Uf??retrygda di er rekna om fr?? ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            English to "Your disability benefit has been recalculated from ".expr()
                    + krav_virkedato_fom.format() + ".".expr()
        )
    }
}

val virkTdsPktUTIkkeEndring_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ", men dette f??rer ikke til endring i utbetalingen.".expr(),
            Nynorsk to " Uf??retrygda di er rekna om fr?? ".expr()
                    + krav_virkedato_fom.format() + ", men det f??rer ikkje til endring i utbetalinga.".expr(),
            English to "We have recalculated your disability benefit from ".expr()
                    + krav_virkedato_fom.format() + ", however this will not lead to change in your payment.".expr()
        )
    }
}

val virkTdsPktUTBTOmregn_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Barnetillegget i uf??retrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            Nynorsk to "Barnetillegget i uf??retrygda di er rekna om fr?? ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            English to "We have recalculated the child supplement in your disability benefit from ".expr()
                    + krav_virkedato_fom.format() + ".".expr()
        )
    }
}

val virkTdsPktUTAvkortetTil0_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Uf??retrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ", men dette f??rer ikke til endring i utbetalingen da uf??retrygden er redusert til 0 kr.".expr(),
            Nynorsk to " Uf??retrygda di er rekna om fr?? ".expr()
                    + krav_virkedato_fom.format() + ", men det f??rer ikkje til endring i utbetalinga d?? uf??retrygda er redusert til 0 kr.".expr(),
            English to "We have recalculated your disability benefit from".expr()
                    + krav_virkedato_fom.format() + ". However this will not lead to change in your payment because your disability benefit is reduced to NOK 0.".expr()
        )
    }
}

val meldInntektUTOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du m?? melde fra om eventuell inntekt",
            Nynorsk to "Du m?? melde fr?? om eventuell inntekt",
            English to "You must report all changes in income"
        )
    }
}

val meldInntektUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Dersom du er i jobb eller har planer om ?? jobbe, m?? du melde fra om eventuelle endringer i inntekten din." +
                    " Det er viktig at du melder fra s?? tidlig som mulig, slik at du f??r riktig utbetaling av uf??retrygd." +
                    " Dette kan du gj??re under menyvalget ??uf??retrygd?? n??r du logger deg inn p?? nav.no." +
                    " Her kan du legge inn hvor mye du forventer ?? tjene i l??pet av ??ret." +
                    " Du vil da kunne se hvor mye du vil f?? utbetalt i uf??retrygd.",

            Nynorsk to "Dersom du er i jobb eller har planar om ?? jobbe, m?? du melde fr?? om eventuelle endringar i inntekta di." +
                    " Det er viktig at du melder fr?? s?? tidleg som r??d, slik at du f??r rett utbetaling av uf??retrygd." +
                    " Dette kan du gjere under menyvalet ??uf??retrygd?? n??r du logger deg inn p?? nav.no." +
                    " Her kan du leggje inn kor mykje du forventar ?? tene i l??pet av ??ret." +
                    " Du vil d?? kunne sj?? kor mykje du kjem til ?? f?? betalt ut i uf??retrygd.",

            English to "If you are working or are planning to work, you must report any changes in your income." +
                    " It is important that you report this as soon as possible, so that you receive the correct disability benefit payments." +
                    " You can register your change in income under the option ???uf??retrygd??? at nav.no." +
                    " You can register how much you expect to earn in the calendar year." +
                    " You will then be able to see how much disability benefit you will receive."
        )
    }
}

val meldInntektUTBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(

            Bokmal to "Dersom du er i jobb eller har planer om ?? jobbe, m?? du melde fra om eventuelle endringer i inntekten din. " +
                    "Det er viktig at du melder fra s?? tidlig som mulig, slik at du f??r riktig utbetaling av uf??retrygd og barnetillegg. " +
                    "Dette kan du gj??re under menyvalget ??uf??retrygd?? n??r du logger deg inn p?? nav.no. " +
                    "Her kan du legge inn hvor mye du forventer ?? tjene i l??pet av ??ret. " +
                    "Du vil da kunne se hvor mye du vil f?? utbetalt i uf??retrygd og barnetillegg.",

            Nynorsk to "Dersom du er i jobb eller har planar om ?? jobbe, m?? du melde fr?? om eventuelle endringar i inntekta di. " +
                    "Det er viktig at du melder fr?? s?? tidleg som r??d, slik at du f??r rett utbetaling av uf??retrygd og barnetillegg. " +
                    "Dette kan du gjere under menyvalet ??uf??retrygd?? n??r du logger deg inn p?? nav.no. " +
                    "Her kan du leggje inn kor mykje du forventar ?? tene i l??pet av ??ret. " +
                    "Du vil d?? kunne sj?? kor mykje du kjem til ?? f?? betalt ut i uf??retrygd og barnetillegg.",

            English to "If you are working or are planning to work, you must report any changes in your income. " +
                    "It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. " +
                    "You can register your change in income under the option ???uf??retrygd??? at nav.no. " +
                    "You can register how much you expect to earn in the calendar year. " +
                    "You will then be able to see how much disability benefit and child supplement you will receive."
        )
    }
}

val skattekortOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Skattekort",
            Nynorsk to "Skattekort",
            English to "Tax card"
        )
    }
}

val skattekortUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Uf??retrygd skattlegges som l??nnsinntekt. " +
                    "Du trenger ikke levere skattekortet til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. " +
                    "Skattekortet kan du endre p?? skatteetaten.no. " +
                    "P?? nettjenesten Ditt NAV p?? nav.no kan du se hvilket skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ??nsker det.",
            Nynorsk to "Uf??retrygd blir skattlagd som l??nsinntekt. " +
                    "Du treng ikkje levere skattekortet til NAV, fordi skatteopplysningane dine blir sende elektronisk fr?? Skatteetaten. " +
                    "Skattekortet kan du endre p?? skatteetaten.no. " +
                    "P?? nettenesten Ditt NAV p?? nav.no kan du sj?? kva skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ??nskjer det.",
            English to "The tax rules for disability benefit are the same as the tax rules for regular income. " +
                    "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. " +
                    "You may change your tax card at skatteetaten.no. " +
                    "At the online service ???Ditt NAV??? at nav.no, you may see your registered income tax rate and change it if you wish."
        )
    }
}

val skattBorIUtlandPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Skatt for deg som bor i utlandet",
            Nynorsk to "Skatt for deg som bur i utlandet",
            English to "Tax for people who live abroad"
        )
    }
    paragraph {
        text(
            Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt p?? skatteetaten.no. " +
                    "Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
            Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt p?? skatteetaten.no. " +
                    "Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
            English to "You can find more information about withholding tax to Norway at skatteetaten.no. " +
                    "For information about taxation from your country of residence, you can contact the locale tax authorities."
        )
    }
}