package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

val vedtakOverskriftPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Vedtak",
            Nynorsk to "Vedtak",
            English to "Decision"
        )
    }
}

data class OmregnUTDodEPSInnledn1001Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)

val omregnUTDodEPSInnledn1_001 = OutlinePhrase<LangBokmalNynorskEnglish, OmregnUTDodEPSInnledn1001Dto> {
    val avdod_navn = it.select(OmregnUTDodEPSInnledn1001Dto::avdod_navn)
    val krav_virkedato_fom = it.select(OmregnUTDodEPSInnledn1001Dto::krav_virkedato_fom)
    paragraph {
        textExpr(
            Bokmal to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har regnet om uføretrygden din fra ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
            Nynorsk to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har rekna om uføretrygda di frå ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
            English to "We have received notice that ".expr()
                    + avdod_navn + " has died, and we have recalculated your disability benefit from ".expr()
                    + krav_virkedato_fom.format() + ", because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
        )
    }
}

val omregnUTDodEPSInnledn2_001 = OutlinePhrase<LangBokmalNynorskEnglish, String> { avdod_navn ->
    paragraph {
        textExpr(
            Bokmal to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død. Uføretrygden din endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.".expr(),
            Nynorsk to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død. Uføretrygda di blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.".expr(),
            English to "We have received notice that ".expr()
                    + avdod_navn + " has died. This will not affect your disability benefit, but we would like to inform you about rights you may have as a surviving spouse.".expr()
        )
    }
}

data class OmregnUTBTDodEPSInnledn_001Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)

val omregnUTBTDodEPSInnledn_001 = OutlinePhrase<LangBokmalNynorskEnglish, OmregnUTBTDodEPSInnledn_001Dto> {
    val avdod_navn = it.select(OmregnUTBTDodEPSInnledn_001Dto::avdod_navn)
    val krav_virkedato_fom = it.select(OmregnUTBTDodEPSInnledn_001Dto::krav_virkedato_fom)
    paragraph {
        textExpr(
            Bokmal to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har regnet om uføretrygden og barnetillegget ditt fra ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
            Nynorsk to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har rekna om uføretrygda di og barnetillegget ditt frå ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din har blitt endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
            English to "We have received notice that ".expr()
                    + avdod_navn + " has died, and we have recalculated your disability benefit and child supplement from ".expr()
                    + krav_virkedato_fom.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
        )
    }


}

val omregnUTBTSBDodEPSInnledn_001 = OutlinePhrase<LangBokmalNynorskEnglish, String> { avdod_navn ->
    paragraph {
        textExpr(
            Bokmal to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død. Uføretrygden og barnetillegget ditt endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.".expr(),
            Nynorsk to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død. Uføretrygda di og barnetillegget ditt blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.".expr(),
            English to "We have received notice that ".expr()
                    + avdod_navn + " has died. This will not affect your disability benefit or child supplement, but we would like to inform you about rights you may have as a surviving spouse.".expr()
        )
    }


}

data class OmregnBTDodEPSInnledn_001Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)

val omregnBTDodEPSInnledn_001 = OutlinePhrase<LangBokmalNynorskEnglish, OmregnBTDodEPSInnledn_001Dto> {
    val avdod_navn = it.select(OmregnBTDodEPSInnledn_001Dto::avdod_navn)
    val krav_virkedato_fom = it.select(OmregnBTDodEPSInnledn_001Dto::krav_virkedato_fom)
    paragraph {
        textExpr(
            Bokmal to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har regnet om barnetillegget ditt fra ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
            Nynorsk to "Vi har mottatt melding om at ".expr()
                    + avdod_navn + " er død, og vi har rekna om barnetillegget ditt frå ".expr()
                    + krav_virkedato_fom.format() + " fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
            English to "We have received notice that ".expr()
                    + avdod_navn + " has died, and we have recalculated your child supplement from ".expr()
                    + krav_virkedato_fom.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
        )
    }
}

data class BeloepUTDto(
    val totalUfoereMaanedligBeloep: Kroner,
    val harBarnetilleggForSaerkullsbarnVedVirk: Boolean,
    val harFlereUfoeretrygdPerioder: Boolean,
)

// BelopUT_001, BelopUTVedlegg_001, BelopUTBT_001, BelopUTBTVedlegg_001
val beloepUT = OutlinePhrase<LangBokmalNynorskEnglish, BeloepUTDto> {
    val totalUforeMaanedligBeloep = it.map { it.totalUfoereMaanedligBeloep }
    val harBarnetilleggForSaerkullsbarnVedVirk = it.map { it.harBarnetilleggForSaerkullsbarnVedVirk }
    val harFlereUfoeretrygdPerioder = it.map { it.harFlereUfoeretrygdPerioder }

    showIf(harBarnetilleggForSaerkullsbarnVedVirk) {
        showIf(harFlereUfoeretrygdPerioder) {
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd og barnetillegg per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                    Nynorsk to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd og barnetillegg kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.".expr(),
                    English to "Your monthly disability benefit and child supplement payment will be NOK ".expr()
                            + totalUforeMaanedligBeloep.format() + " before tax. You can read more about other calculation periods in the appendix.".expr()
                )
            }
        }.orShow {
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                    Nynorsk to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd og barnetillegg kvar månad før skatt.".expr(),
                    English to "Your monthly disability benefit and child supplement payment will be NOK ".expr()
                            + totalUforeMaanedligBeloep.format() + " before tax.".expr()
                )
            }
        }
    }.orShow {
        showIf(harFlereUfoeretrygdPerioder) {
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                    Nynorsk to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.".expr(),
                    English to "Your monthly disability benefit payment will be NOK ".expr()
                            + totalUforeMaanedligBeloep.format() + " before tax. You can read more about other calculation periods in the appendix.".expr()
                )
            }
        }.orShow {
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd per måned før skatt.".expr(),
                    Nynorsk to "Du får ".expr()
                            + totalUforeMaanedligBeloep.format() + " kroner i uføretrygd kvar månad før skatt.".expr(),
                    English to "Your monthly disability benefit payment will be NOK ".expr()
                            + totalUforeMaanedligBeloep.format() + " before tax.".expr()
                )
            }
        }
    }
}

val belopUTIngenUtbetaling_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt.",
            English to "You will not receive disability benefit payment because of your reported income."
        )
    }
}

val belopUTIngenUtbetalingVedlegg_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
            English to "You will not receive disability benefit payment because of your reported income. You can read more about other calculation periods in the appendix."
        )
    }
}

val belopUTBTIngenUtbetaling_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt.",
            English to "You will not receive disability benefit and child supplement payment because of your reported income."
        )
    }
}

val belopUTBTIngenUtbetalingVedlegg_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
            English to "You will not receive disability benefit and child supplement payment because of your reported income. You can read more about other calculation periods in the appendix."
        )
    }
}

val belopUTIngenUtbetalingFengsel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring.",
            English to "You will not receive disability benefit because you are serving a criminal sentence."
        )
    }
}

val belopUTIngenUtbetalingFengselVedlegg_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese mer om andre beregningsperioder i vedlegget.",
            Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
            English to "You will not receive disability benefit because you are serving a criminal sentence. You can read more about other calculation periods in the appendix."
        )
    }
}

val begrunnOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Begrunnelse for vedtaket",
            Nynorsk to "Grunngiving for vedtaket",
            English to "Grounds for the decision"
        )
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
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr()
                    + minsteytelse_sats_vedvirk.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.".expr(),
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr()
                    + minsteytelse_sats_vedvirk.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.".expr(),
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr()
                    + minsteytelse_sats_vedvirk.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Dette kan ha noko å seie for kompensasjonsgraden din som er fastsett til ".expr()
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
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr()
                    + minsteytelse_sats_vedvirk.format() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.".expr(),
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr()
                    + minsteytelse_sats_vedvirk.format() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.".expr(),
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr()
                    + minsteytelse_sats_vedvirk.format() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
        )
    }

    val oppjustert_inntekt_foer_ufoerhet_vedvirk = dto.map { it.oppjustert_inntekt_foer_ufoerhet_vedvirk }
    val inntekt_foer_ufoerhet_vedvirk = dto.map { it.inntekt_foer_ufoerhet_vedvirk }
    paragraph {
        textExpr(
            Bokmal to "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Kompensasjonsgraden din er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Endringar i sivilstanden påverkar også inntekta di før du blei ufør. Denne utgjer ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Kompensasjonsgraden din er fastsett til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
            English to "The change in your marital status also affects your income prior to disability, which is determined to be NOK ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr()
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
            Bokmal to "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
            Nynorsk to "Inntekta di før du blei ufør er fastsett til minstenivå, som er avhengig av sivilstand. For deg er inntekta di før du blei ufør fastsett til ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr()
                    + oppjustert_inntekt_foer_ufoerhet_vedvirk.format() + " kroner. Dette kan ha noko å seie for kompensasjonsgraden din, som er fastsett til ".expr()
                    + kompensasjonsgrad_ufoeretrygd_vedvirk.format() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
            English to "Your income prior to disability is set to the minimum level, which depends on marital status. The change in your marital status affects your income prior to disability, which is determined to be NOK ".expr()
                    + inntekt_foer_ufoerhet_vedvirk.format() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr()
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
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13, 12-14 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13, 12-14 and 22-12 of the National Insurance Act."
                )
            }
        }.orShow {
            //HjemmelSivilstandUTMinsteIFU_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13 and 22-12 of the National Insurance Act."
                )
            }
        }
    }.orShow {
        showIf(ufoeretrygdVedvirkErInntektsavkortet) {
            //HjemmelSivilstandUTAvkortet_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13, 12-14 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 12-13, 12-14 and 22-12 of the National Insurance Act."
                )
            }
        }.orShow {
            //HjemmelSivilstandUT_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 12-13 and 22-12 of the National Insurance Act."
                )
            }
        }
    }
}

val hjemmelEPSDodUTInstitusjon_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon.",
            Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-19 så lenge du er på institusjon.",
            English to "Your disability benefit is calculated according to the provisions of § 12-19 of the National Insurance Act, as long as you are institutionalized."
        )
    }
}

val hjemmelEPSDodUTFengsel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring.",
            Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-20 så lenge du er under straffegjennomføring.",
            English to "Your disability benefit is calculated according to the provisions of § 12-20 of the National Insurance Act, as long as you are serving a criminal sentence."
        )
    }
}

val opphorETOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Ektefelletillegget ditt opphører",
            Nynorsk to "Ektefelletillegget ditt avsluttast",
            English to "Your spouse supplement will end"
        )
    }
}

val opphorET_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt.",
            Nynorsk to "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta.",
            English to "You no longer provide for a spousepartnercohabitant. Your spouse supplement will therefore end."
        )
    }
}

val hjemmelET_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8.",
            Nynorsk to "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8.",
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
            Bokmal to "Denne omregningen har også betydning for barnetillegget for:",
            Nynorsk to "Denne omrekninga har også noko å seie for barnetillegget for:",
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
            Bokmal to "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:",
            Nynorsk to "Omrekninga av barnetillegget og endring i uføretrygda di har også noko å seie for barnetillegget for:",
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
            Bokmal to "Slik påvirker endring av uføretrygden barnetillegget ditt",
            Nynorsk to "Slik påverkar endringa av uføretrygda barnetillegget ditt",
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
            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din før du ble ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen lavere enn dette. Derfor er barnetillegget fastsatt til 40 prosent av folketrygdens grunnbeløp for hvert barn.".expr(),
            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di før du blei ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygda di og barnetillegget ditt er til saman lågare enn dette. Derfor er barnetillegget fastsett til 40 prosent av grunnbeløpet i folketrygda for kvart barn.".expr(),
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
            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din før du ble ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor er barnetillegget redusert fra ".expr()
                    + barnetillegg_beloep_foer_reduksjon_vedvirk.format() + " kroner til ".expr()
                    + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.format() + " kroner.".expr(),
            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di før du blei ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor er barnetillegget redusert frå ".expr()
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
            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekten din før du ble ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor får du ikke utbetalt barnetillegg.".expr(),
            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av inntekta di før du blei ufør. ".expr()
                    + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr()
                    + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.format() + " kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor får du ikkje utbetalt barnetillegg.".expr(),
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
            Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. " +
                    "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribeløp. " +
                    "Du kan enkelt melde fra om inntektsendringer på nav.no.",
            Nynorsk to "Inntekta di påverkar det du får i barnetillegg. " +
                    "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribeløp. " +
                    "Du kan enkelt melde frå om inntektsendringar på nav.no.",
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
            Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. " +
                    "Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensen kaller vi for fribeløp. " +
                    "Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " +
                    "Du kan enkelt melde fra om inntektsendringer på nav.no.",

            Nynorsk to "Inntekta di påverkar det du får i barnetillegg. " +
                    "Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. " +
                    "Denne grensa kallar vi for fribeløp. " +
                    "Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn. " +
                    "Du kan enkelt melde frå om inntektsendringar på nav.no.",

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
            Bokmal to "Inntekten din på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er lavere enn fribeløpet ditt på ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.".expr(),
            Nynorsk to "Inntekta di på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er lågare enn fribeløpet ditt på ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget ikkje redusert ut frå inntekt.".expr(),
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
            Bokmal to "Inntekten din på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er høyere enn fribeløpet ditt på ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget redusert ut fra inntekt.".expr(),
            Nynorsk to "Inntekta di på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er høgare enn fribeløpet ditt på ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + " kroner. Derfor er barnetillegget redusert ut frå inntekt.".expr(),
            English to "Your income of NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " is higher than your exemption amount of NOK ".expr()
                    + barnetillegg_saerkullsbarn_fribeloep_vedvirk.format() + ". Therefore, your child supplement has been reduced on the basis of your income.".expr()
        )
    }
}

val justerBelopRedusBTPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
            Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. Det blei teke omsyn til dette då vi endra barnetillegget.",
            English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. This was taken into account when we changed your child supplement."
        )
    }
}

val justerBelopIkkeUtbetaltBTPgaInntekt_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. " +
                    "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",

            Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. " +
                    "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",

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
            Bokmal to "Inntekten din på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er over ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + " kroner som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg.".expr(),
            Nynorsk to "Inntekta di på ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " kroner er over ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + " kroner, som er grensa for å få utbetalt barnetillegg. Derfor får du ikkje utbetalt barnetillegg.".expr(),
            English to "Your income of NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.format() + " is over the income limit for receiving a child supplement, which is NOK ".expr()
                    + barnetillegg_saerkullsbarn_inntektstak_vedvirk.format() + ". Therefore, you will not receive child supplement.".expr()
        )
    }
}

val hjemmelBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15.",
            English to "This decision was made pursuant to the provisions § 12-15 of the National Insurance Act."
        )
    }
}

val hjemmelBTOvergangsregler_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
            English to "This decision was made pursuant to the provisions of § 12-15 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
        )
    }
}

val hjemmelBTRedus_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16.",
            English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act."
        )
    }
}

val hjemmelBTRedusOvergangsregler_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
            English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
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

val gjRettSamboerOverskrift = OutlinePhrase<LangBokmalNynorskEnglish, String> { avdod_navn ->
    title1 {
        textExpr(
            Bokmal to "Rettigheter du kan ha som tidligere samboer med ".expr()
                    + avdod_navn,
            Nynorsk to "Rettar du kan ha som tidlegare sambuar med ".expr()
                    + avdod_navn,
            English to "Rights you may be entitled to as a former cohabitant with ".expr()
                    + avdod_navn
        )
    }

}

val gjRettUTSamboer_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Samboere som tidligere har vært gift, eller som har eller har hatt felles barn, kan ha rett til gjenlevendetillegg i uføretrygden. " +
                    "Du finner mer informasjon og søknadsskjema for gjenlevende ektefelle, partner eller samboer på nav.no.",
            Nynorsk to "Sambuarar som tidlegare har vore gift, eller som har eller har hatt felles barn, kan ha rett til attlevandetillegg i uføretrygda. " +
                    "Du finn meir informasjon og søknadsskjema for attlevande ektefelle, partnar eller sambuar på nav.no.",
            English to "Cohabitants who have previously been married, or who have or have had children together, may be entitled to survivor's supplement to disability benefit. " +
                    "You will find more information and the application form for benefits for surviving spouse, partner or cohabitant at nav.no."
        )
    }
}

val rettTilUTGJTOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du kan ha rett til gjenlevendetillegg i uføretrygden",
            Nynorsk to "Du kan ha rett til attlevandetillegg i uføretrygda",
            English to "You might be entitled to a survivor's supplement to disability benefit"
        )
    }
}

val hvemUTGJTVilkar_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "For å ha rett til gjenlevendetillegg i uføretrygden, må du som hovedregel:",
            Nynorsk to "For å ha rett til attlevandetillegg i uføretrygda, må du som hovudregel:",
            English to "To be entitled to a survivor's supplement to disability benefit, you must as a rule:",
        )
        list {
            item {
                text(
                    Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                    Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                    English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death "
                )
            }
            item {
                text(
                    Bokmal to "ha vært gift med den avdøde i minst fem år, eller",
                    Nynorsk to "ha vore gift med den avdøde i minst fem år, eller",
                    English to "have been married to the deceased for at least five years, or "
                )
            }
            item {
                text(
                    Bokmal to "ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller",
                    Nynorsk to "ha vore gift eller vore sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                    English to "have been married to or a cohabitant with the deceased, and have/had children together, or "
                )
            }
            item {
                text(
                    Bokmal to "ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. " +
                            "Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år.",
                    Nynorsk to "ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. " +
                            "Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år.",
                    English to "have had care of the children of the deceased at the time of the death. " +
                            "The marriage and care of the child after the death must have lasted for at least five years."
                )
            }
        }

    }
    paragraph {
        text(
            Bokmal to "Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. " +
                    "Du kan lese mer om dette på nav.no.",
            Nynorsk to "Sjølv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avdøde. " +
                    "Du kan lese meir om dette på nav.no.",
            English to "Even if you are not entitled to benefits in accordance with the general rules, you may nevertheless have rights as a surviving spouse. " +
                    "You can read more about this at nav.no."
        )
    }
}

val hvordanSoekerDuOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Hvordan søker du?",
            Nynorsk to "Korleis søkjer du?",
            English to "How do you apply?"
        )
    }
}

val soekUTGJT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi oppfordrer deg til å søke om gjenlevendetillegg i uføretrygden så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. " +
                    "Du finner informasjon og søknadsskjemaet for gjenlevende ektefelle, partner eller samboer på nav.no/gjenlevendeektefelle",
            Nynorsk to "Vi oppmodar deg til å søkje om attlevandetillegg i uføretrygda så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader. " +
                    "Du finn informasjon og søknadsskjemaet for attlevande ektefelle, partner eller sambuar på nav.no/gjenlevendeektefelle",
            English to "We encourage you to apply for survivor's supplement to disability benefit as soon as possible because we normally only pay retroactively for three months. " +
                    "You will find information and the application form for a surviving spouse, partner or cohabitant at nav.no/gjenlevendeektefelle"
        )
    }

}

val soekAvtaleLandUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
            Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdeorgana i bustadslandet ditt og søkje om ytingar etter avdøde.",
            English to "If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension."
        )
    }
}

val avdodBoddArbUtlandOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
            Nynorsk to "Rettar dersom avdøde har budd eller arbeidd i utlandet",
            English to "Rights if the deceased has lived or worked abroad"
        )
    }
}

val avdodBoddArbUtland2_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i gjenlevendetillegg. " +
                    "Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                    "Derfor kan du også ha rett til pensjon fra andre land. " +
                    "Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",

            Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får ubetalt i attlevandetillegg. " +
                    "Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. " +
                    "Derfor kan du også ha rett til pensjon frå andre land. " +
                    "Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",

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
            Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. " +
                    "Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
            Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, må du kontakte arbeidsgivaren til avdøde. " +
                    "Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
            English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you must contact the deceased's employer. " +
                    "You can also contact the pension scheme or insurance."
        )
    }
}

val harBarnUnder18Overskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "For deg som har barn under 18 år",
            Nynorsk to "For deg som har barn under 18 år",
            English to "If you have children under the age of 18"
        )
    }
}

val harBarnUtenBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette.",
            Nynorsk to "Har du barn under 18 år, og ikkje er innvilga barnetillegg, kan du søkje om dette.",
            English to "If you have children under the age of 18, and have not been granted a child supplement, you can apply for this."
        )
    }
}

val harBarnUnder18_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finner søknadsskjema og mer informasjon om dette på nav.no.",
            Nynorsk to "Syter du for barn under 18 år, kan du ha rett til utvida barnetrygd. " +
                    "I tillegg kan barn ha rett til barnepensjon. " +
                    "Du finn søknadsskjema og meir informasjon om dette på nav.no.",
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
            Bokmal to "Uføretrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            Nynorsk to "Uføretrygda di er rekna om frå ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            English to "Your disability benefit has been recalculated from ".expr()
                    + krav_virkedato_fom.format() + ".".expr()
        )
    }
}

val virkTdsPktUTIkkeEndring_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Uføretrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ", men dette fører ikke til endring i utbetalingen.".expr(),
            Nynorsk to " Uføretrygda di er rekna om frå ".expr()
                    + krav_virkedato_fom.format() + ", men det fører ikkje til endring i utbetalinga.".expr(),
            English to "We have recalculated your disability benefit from ".expr()
                    + krav_virkedato_fom.format() + ", however this will not lead to change in your payment.".expr()
        )
    }
}

val virkTdsPktUTBTOmregn_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Barnetillegget i uføretrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            Nynorsk to "Barnetillegget i uføretrygda di er rekna om frå ".expr()
                    + krav_virkedato_fom.format() + ".".expr(),
            English to "We have recalculated the child supplement in your disability benefit from ".expr()
                    + krav_virkedato_fom.format() + ".".expr()
        )
    }
}

val virkTdsPktUTAvkortetTil0_001 = OutlinePhrase<LangBokmalNynorskEnglish, LocalDate> { krav_virkedato_fom ->
    paragraph {
        textExpr(
            Bokmal to "Uføretrygden din er omregnet fra ".expr()
                    + krav_virkedato_fom.format() + ", men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr.".expr(),
            Nynorsk to " Uføretrygda di er rekna om frå ".expr()
                    + krav_virkedato_fom.format() + ", men det fører ikkje til endring i utbetalinga då uføretrygda er redusert til 0 kr.".expr(),
            English to "We have recalculated your disability benefit from".expr()
                    + krav_virkedato_fom.format() + ". However this will not lead to change in your payment because your disability benefit is reduced to NOK 0.".expr()
        )
    }
}

val meldInntektUTOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du må melde fra om eventuell inntekt",
            Nynorsk to "Du må melde frå om eventuell inntekt",
            English to "You must report all changes in income"
        )
    }
}

val meldInntektUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din." +
                    " Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd." +
                    " Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no." +
                    " Her kan du legge inn hvor mye du forventer å tjene i løpet av året." +
                    " Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd.",

            Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di." +
                    " Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd." +
                    " Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no." +
                    " Her kan du leggje inn kor mykje du forventar å tene i løpet av året." +
                    " Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd.",

            English to "If you are working or are planning to work, you must report any changes in your income." +
                    " It is important that you report this as soon as possible, so that you receive the correct disability benefit payments." +
                    " You can register your change in income under the option “uføretrygd” at nav.no." +
                    " You can register how much you expect to earn in the calendar year." +
                    " You will then be able to see how much disability benefit you will receive."
        )
    }
}

val meldInntektUTBT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(

            Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. " +
                    "Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. " +
                    "Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. " +
                    "Her kan du legge inn hvor mye du forventer å tjene i løpet av året. " +
                    "Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg.",

            Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. " +
                    "Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd og barnetillegg. " +
                    "Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. " +
                    "Her kan du leggje inn kor mykje du forventar å tene i løpet av året. " +
                    "Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd og barnetillegg.",

            English to "If you are working or are planning to work, you must report any changes in your income. " +
                    "It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. " +
                    "You can register your change in income under the option “uføretrygd” at nav.no. " +
                    "You can register how much you expect to earn in the calendar year. " +
                    "You will then be able to see how much disability benefit and child supplement you will receive."
        )
    }
}

val meldEndringerPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du må melde fra om endringer",
            Nynorsk to "Du må melde frå om endringar",
            English to "You must notify NAV if anything changes"
        )
    }
    paragraph {
        text(
            Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.",
            Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om.",
            English to "If your circumstances change, you must inform NAV immediately. The appendix includes information on how to proceed."
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                    "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.",
            Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. " +
                    "Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til NAV.",
            English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. " +
                    "It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to NAV."
        )
    }
}

val rettTilKlagePesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du har rett til å klage",
            Nynorsk to "Du har rett til å klage",
            English to "You have the right to appeal"
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                    "Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
            Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                    "Klagen skal vera skriftleg. Du finn skjema og informasjon på nav.no/klage.",
            English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. " +
                    "Your appeal must be made in writing. You will find a form you can use and more information about appeals at nav.no."
        )
    }
    paragraph {
        text(
            Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
            Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
            English to "The appendix includes information on how to proceed."
        )
    }
}

val rettTilInnsynPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du har rett til innsyn",
            Nynorsk to "Du har rett til innsyn",
            English to "You have the right to access your file"
        )
    }
    paragraph {
        text(
            Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
            Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
            English to "You have the right to access all documents pertaining to your case. The appendix includes information on how to proceed."
        )
    }
}

val sjekkUtbetalingeneOverskrift_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Sjekk utbetalingene dine",
            Nynorsk to "Sjekk utbetalingane dine",
            English to "Information about your payments"
        )
    }
}

val sjekkUtbetalingeneUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                    "Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt.",
            Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                    "Du kan sjå alle utbetalingar du har fått på nav.no/dittnav. Her kan du også endre kontonummeret ditt.",
            English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. " +
                    "To see all the payments you have received, go to: nav.no/ dittnav. You may also change your account number here."
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
            Bokmal to "Uføretrygd skattlegges som lønnsinntekt. " +
                    "Du trenger ikke levere skattekortet til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. " +
                    "Skattekortet kan du endre på skatteetaten.no. " +
                    "På nettjenesten Ditt NAV på nav.no kan du se hvilket skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønsker det.",
            Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. " +
                    "Du treng ikkje levere skattekortet til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. " +
                    "Skattekortet kan du endre på skatteetaten.no. " +
                    "På nettenesten Ditt NAV på nav.no kan du sjå kva skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønskjer det.",
            English to "The tax rules for disability benefit are the same as the tax rules for regular income. " +
                    "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. " +
                    "You may change your tax card at skatteetaten.no. " +
                    "At the online service “Ditt NAV” at nav.no, you may see your registered income tax rate and change it if you wish."
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
            Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. " +
                    "Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
            Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. " +
                    "Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
            English to "You can find more information about withholding tax to Norway at skatteetaten.no. " +
                    "For information about taxation from your country of residence, you can contact the locale tax authorities."
        )
    }
}