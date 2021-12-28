package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Phrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.phrase
import java.time.LocalDate


object VedtakOverskriftPesys_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision"
            )
        }
    }
}

object OmregnUTDodEPSInnledn1_001 : Phrase<OmregnUTDodEPSInnledn1_001.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har regnet om uføretrygden din fra ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
                Nynorsk to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har rekna om uføretrygda di frå ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
                English to "We have received notice that ".expr() + avdod_navn + " has died, and we have recalculated your disability benefit from ".expr() + krav_virkedato_fom.format() + ", because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
            )
        }
    }

    data class Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)
}

object OmregnUTDodEPSInnledn2_001 : Phrase<OmregnUTDodEPSInnledn2_001.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        paragraph {
            textExpr(
                Bokmal to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død. Uføretrygden din endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.".expr(),
                Nynorsk to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død. Uføretrygda di blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.".expr(),
                English to "We have received notice that ".expr() + avdod_navn + " has died. This will not affect your disability benefit, but we would like to inform you about rights you may have as a surviving spouse.".expr()
            )
        }
    }

    data class Dto(val avdod_navn: String)
}

object OmregnUTBTDodEPSInnledn_001 : Phrase<OmregnUTBTDodEPSInnledn_001.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har regnet om uføretrygden og barnetillegget ditt fra ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
                Nynorsk to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har rekna om uføretrygda di og barnetillegget ditt frå ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din har blitt endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
                English to "We have received notice that ".expr() + avdod_navn + " has died, and we have recalculated your disability benefit and child supplement from ".expr() + krav_virkedato_fom.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
            )
        }
    }

    data class Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)
}

object OmregnUTBTSBDodEPSInnledn_001 : Phrase<OmregnUTBTSBDodEPSInnledn_001.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        paragraph {
            textExpr(
                Bokmal to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død. Uføretrygden og barnetillegget ditt endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.".expr(),
                Nynorsk to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død. Uføretrygda di og barnetillegget ditt blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.".expr(),
                English to "We have received notice that ".expr() + avdod_navn + " has died. This will not affect your disability benefit or child supplement, but we would like to inform you about rights you may have as a surviving spouse.".expr()
            )
        }
    }

    data class Dto(val avdod_navn: String)
}

object OmregnBTDodEPSInnledn_001 : Phrase<OmregnBTDodEPSInnledn_001.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har regnet om barnetillegget ditt fra ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.".expr(),
                Nynorsk to "Vi har mottatt melding om at ".expr() + avdod_navn + " er død, og vi har rekna om barnetillegget ditt frå ".expr() + krav_virkedato_fom.format() + " fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.".expr(),
                English to "We have received notice that ".expr() + avdod_navn + " has died, and we have recalculated your child supplement from ".expr() + krav_virkedato_fom.format() + " because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse.".expr()
            )
        }
    }

    data class Dto(val avdod_navn: String, val krav_virkedato_fom: LocalDate)
}

object BelopUT_001 : Phrase<BelopUT_001.Dto> {
    override val elements = phrase {
        val total_ufoeremaaneder = argument().select(Dto::total_ufoeremaaneder)
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd kvar månad før skatt.".expr(),
                English to "Your monthly disability benefit payment will be NOK ".expr() + total_ufoeremaaneder.str() + " before tax.".expr()
            )
        }

    }

    data class Dto(val total_ufoeremaaneder: Number)
}

object BelopUTVedlegg_001 : Phrase<BelopUTVedlegg_001.Dto> {
    override val elements = phrase {
        val total_ufoeremaaneder = argument().select(Dto::total_ufoeremaaneder)
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                Nynorsk to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.".expr(),
                English to "Your monthly disability benefit payment will be NOK ".expr() + total_ufoeremaaneder.str() + " before tax. You can read more about other calculation periods in the appendix.".expr()
            )
        }
    }

    data class Dto(val total_ufoeremaaneder: Number)
}

object BelopUTBT_001 : Phrase<BelopUTBT_001.Dto> {
    override val elements = phrase {
        val total_ufoeremaaneder = argument().select(Dto::total_ufoeremaaneder)
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                Nynorsk to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd og barnetillegg kvar månad før skatt.".expr(),
                English to "Your monthly disability benefit and child supplement payment will be NOK ".expr() + total_ufoeremaaneder.str() + " before tax.".expr()
            )
        }
    }

    data class Dto(val total_ufoeremaaneder: Number)
}

object BelopUTBTVedlegg_001 : Phrase<BelopUTBTVedlegg_001.Dto> {
    override val elements = phrase {
        val total_ufoeremaaneder = argument().select(Dto::total_ufoeremaaneder)
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd og barnetillegg per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.".expr(),
                Nynorsk to "Du får ".expr() + total_ufoeremaaneder.str() + " kroner i uføretrygd og barnetillegg kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.".expr(),
                English to "Your monthly disability benefit and child supplement payment will be NOK ".expr() + total_ufoeremaaneder.str() + " before tax. You can read more about other calculation periods in the appendix.".expr()
            )
        }
    }

    data class Dto(val total_ufoeremaaneder: Number)
}

object BelopUTIngenUtbetaling_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt.",
                English to "You will not receive disability benefit payment because of your reported income."
            )
        }
    }
}

object BelopUTIngenUtbetalingVedlegg_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                English to "You will not receive disability benefit payment because of your reported income. You can read more about other calculation periods in the appendix."
            )
        }
    }
}

object BelopUTBTIngenUtbetaling_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt.",
                English to "You will not receive disability benefit and child supplement payment because of your reported income."
            )
        }
    }
}

object BelopUTBTIngenUtbetalingVedlegg_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                English to "You will not receive disability benefit and child supplement payment because of your reported income. You can read more about other calculation periods in the appendix."
            )
        }
    }
}

object BelopUTIngenUtbetalingFengsel_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring.",
                English to "You will not receive disability benefit because you are serving a criminal sentence."
            )
        }
    }
}

object BelopUTIngenUtbetalingFengselVedlegg_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese mer om andre beregningsperioder i vedlegget.",
                Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese meir om andre utrekningsperiodar i vedlegget.",
                English to "You will not receive disability benefit because you are serving a criminal sentence. You can read more about other calculation periods in the appendix."
            )
        }
    }
}

object BegrunnOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
                English to "Grounds for the decision"
            )
        }
    }
}

object EndrMYDodEPS2_001 : Phrase<EndrMYDodEPS2_001.Dto> {
    override val elements = phrase {
        val minsteytelse_sats_vedvirk = argument().select(Dto::minsteytelse_sats_vedvirk)
        val kompensasjonsgrad_ufoeretrygd_vedvirk = argument().select(Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr() + minsteytelse_sats_vedvirk.str() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.".expr(),
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr() + minsteytelse_sats_vedvirk.str() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.".expr(),
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr() + minsteytelse_sats_vedvirk.str() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Dette kan ha noko å seie for kompensasjonsgraden din som er fastsett til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "This may affect your degree of compensation, which is determined to be ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " percent. You can read more about this in the appendix.".expr()
            )
        }
    }

    data class Dto(val minsteytelse_sats_vedvirk: Number, val kompensasjonsgrad_ufoeretrygd_vedvirk: Number)
}

object EndrMYOgMinstIFUDodEPS2_001 : Phrase<EndrMYOgMinstIFUDodEPS2_001.Dto> {
    override val elements = phrase {
        val minsteytelse_sats_vedvirk = argument().select(Dto::minsteytelse_sats_vedvirk)
        val inntekt_foer_ufoerhet_vedvirk = argument().select(Dto::inntekt_foer_ufoerhet_vedvirk)
        val oppjustert_inntekt_foer_ufoerhet_vedvirk = argument().select(Dto::oppjustert_inntekt_foer_ufoerhet_vedvirk)
        val kompensasjonsgrad_ufoeretrygd_vedvirk = argument().select(Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen ".expr() + minsteytelse_sats_vedvirk.str() + " ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.".expr(),
                Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga ".expr() + minsteytelse_sats_vedvirk.str() + " gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.".expr(),
                English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is ".expr() + minsteytelse_sats_vedvirk.str() + " times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no.".expr()
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + " kroner. Kompensasjonsgraden din er satt til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Endringar i sivilstanden påverkar også inntekta di før du blei ufør. Denne utgjer ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + " kroner. Kompensasjonsgraden din er fastsett til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "The change in your marital status also affects your income prior to disability, which is determined to be NOK ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + ". Your degree of compensation is determined to be ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " percent. You can read more about this in the appendix.".expr()
            )
        }
    }

    data class Dto(
        val minsteytelse_sats_vedvirk: Number,
        val inntekt_foer_ufoerhet_vedvirk: Number,
        val oppjustert_inntekt_foer_ufoerhet_vedvirk: Number,
        val kompensasjonsgrad_ufoeretrygd_vedvirk: Number
    )
}

object EndrMinstIFUDodEPS2_001 : Phrase<EndrMinstIFUDodEPS2_001.Dto> {
    override val elements = phrase {
        val inntekt_foer_ufoerhet_vedvirk = argument().select(Dto::inntekt_foer_ufoerhet_vedvirk)
        val oppjustert_inntekt_foer_ufoerhet_vedvirk = argument().select(Dto::oppjustert_inntekt_foer_ufoerhet_vedvirk)
        val kompensasjonsgrad_ufoeretrygd_vedvirk = argument().select(Dto::kompensasjonsgrad_ufoeretrygd_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + " kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + " kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese mer om dette i vedlegget.".expr(),
                Nynorsk to "Inntekta di før du blei ufør er fastsett til minstenivå, som er avhengig av sivilstand. For deg er inntekta di før du blei ufør fastsett til ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + " kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + " kroner. Dette kan ha noko å seie for kompensasjonsgraden din, som er fastsett til ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " prosent. Du kan lese meir om dette i vedlegget.".expr(),
                English to "Your income prior to disability is set to the minimum level, which depends on marital status. The change in your marital status affects your income prior to disability, which is determined to be NOK ".expr() + inntekt_foer_ufoerhet_vedvirk.str() + ". Adjusted to today’s value, this is equivalent to an income of NOK ".expr() + oppjustert_inntekt_foer_ufoerhet_vedvirk.str() + ". This may affect your degree of compensation, which has been determined to be ".expr() + kompensasjonsgrad_ufoeretrygd_vedvirk.str() + " percent. You can read more about this in the appendix.".expr()
            )
        }
    }

    data class Dto(
        val inntekt_foer_ufoerhet_vedvirk: Number,
        val oppjustert_inntekt_foer_ufoerhet_vedvirk: Number,
        val kompensasjonsgrad_ufoeretrygd_vedvirk: Number,
    )
}

object HjemmelSivilstandUT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13 og 22-12.",
                English to "This decision was made pursuant to the provisions of §§ 12-13 and 22-12 of the National Insurance Act."
            )
        }
    }
}

object HjemmelSivilstandUTMinsteIFU_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13 og 22-12.",
                English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13 and 22-12 of the National Insurance Act."
            )
        }
    }
}

object HjemmelSivilstandUTAvkortet_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13, 12-14 og 22-12.",
                English to "This decision was made pursuant to the provisions of §§ 12-13, 12-14 and 22-12 of the National Insurance Act."
            )
        }
    }
}

object HjemmelSivilstandUTMinsteIFUAvkortet_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13, 12-14 og 22-12.",
                English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13, 12-14 and 22-12 of the National Insurance Act."
            )
        }
    }
}

object HjemmelEPSDodUTInstitusjon_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon.",
                Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-19 så lenge du er på institusjon.",
                English to "Your disability benefit is calculated according to the provisions of § 12-19 of the National Insurance Act, as long as you are institutionalized."
            )
        }
    }
}

object HjemmelEPSDodUTFengsel_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring.",
                Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-20 så lenge du er under straffegjennomføring.",
                English to "Your disability benefit is calculated according to the provisions of § 12-20 of the National Insurance Act, as long as you are serving a criminal sentence."
            )
        }
    }
}

object OpphorETOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Ektefelletillegget ditt opphører",
                Nynorsk to "Ektefelletillegget ditt avsluttast",
                English to "Your spouse supplement will end"
            )
        }
    }
}

object OpphorET_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt.",
                Nynorsk to "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta.",
                English to "You no longer provide for a spousepartnercohabitant. Your spouse supplement will therefore end."
            )
        }
    }
}

object HjemmelET_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8.",
                Nynorsk to "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8.",
                English to "The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit."
            )
        }
    }
}

object OmregningFBOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Barnetillegg for fellesbarn er regnet om",
                Nynorsk to "Barnetillegg for fellesbarn er rekna om",
                English to "Your child supplement has been recalculated"
            )
        }
    }
}

object InfoFBTilSB_001 : Phrase<InfoFBTilSB_001.Dto> {
    override val elements = phrase {
        val barn_overfoert_til_saerkullsbarn = argument().select(Dto::barn_overfoert_til_saerkullsbarn)
        paragraph {
            text(
                Bokmal to "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:",
                Nynorsk to "Vi har rekna om barnetillegget for barn som ikkje lenger bur saman med begge foreldre. Dette gjeld:",
                English to "We have recalculated your child supplement for the child/children who no longer lives/live together with both parents. This applies to:"
            )
            //TODO vis liste med barn_overfoert_til_saerkullsbarn
        }

    }
    data class Dto(val barn_overfoert_til_saerkullsbarn: List<String>)
}

object InfoTidligereSB_001 : Phrase<InfoTidligereSB_001.Dto> {
    override val elements = phrase {
        val tidligere_saerkullsbarn = argument().select(Dto::tidligere_saerkullsbarn)
        paragraph {
            text(
                Bokmal to "Denne omregningen har også betydning for barnetillegget for:",
                Nynorsk to "Denne omrekninga har også noko å seie for barnetillegget for:",
                English to "This recalculation also affects your child supplement for:"
            )
            //TODO vis liste med tidligere_saerkullsbarn
        }
    }
    data class Dto(val tidligere_saerkullsbarn: List<String>)
}

object InfoTidligereSBOgEndretUT_001 : Phrase<InfoTidligereSBOgEndretUT_001.Dto> {
    override val elements = phrase {
        val tidligere_saerkullsbarn = argument().select(Dto::tidligere_saerkullsbarn)
        paragraph {
            text(
                Bokmal to "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:",
                Nynorsk to "Omrekninga av barnetillegget og endring i uføretrygda di har også noko å seie for barnetillegget for:",
                English to "Recalculation of your child supplement and change in your disability benefit also affects your child supplement for:"
            )
            //TODO vis liste med tidligere_saerkullsbarn
        }
    }
    data class Dto(val tidligere_saerkullsbarn: List<String>)
}

object EndringUTpavirkerBTOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Slik påvirker endring av uføretrygden barnetillegget ditt",
                Nynorsk to "Slik påverkar endringa av uføretrygda barnetillegget ditt",
                English to "How the change in your disability benefit affects your child supplement"
            )
        }
    }
}

object IkkeRedusBTPgaTak_001 : Phrase<IkkeRedusBTPgaTak_001.Dto> {
    override val elements = phrase {
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk)
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekten din før du ble ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygden og barnetillegget ditt er til sammen lavere enn dette. Derfor er barnetillegget fastsatt til 40 prosent av folketrygdens grunnbeløp for hvert barn.".expr(),
                Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekta di før du blei ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygda di og barnetillegget ditt er til saman lågare enn dette. Derfor er barnetillegget fastsett til 40 prosent av grunnbeløpet i folketrygda for kvart barn.".expr(),
                English to "Your disability benefit and child supplement together cannot exceed more than ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of your income before you became disabled. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of the income you had before you became disabled is equivalent today to an income of NOK ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + ". Your disability benefit and child supplement together are lower than this. Therefore, your child supplement has been determined to be 40 percent of the National Insurance basic amount for each child.".expr()
            )
        }
    }

    data class Dto(
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
    )
}

object RedusBTPgaTak_001 : Phrase<RedusBTPgaTak_001.Dto> {
    override val elements = phrase {
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk)
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk)
        val barnetillegg_beloep_foer_reduksjon_vedvirk =
            argument().select(Dto::barnetillegg_beloep_foer_reduksjon_vedvirk)
        val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekten din før du ble ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor er barnetillegget redusert fra ".expr() + barnetillegg_beloep_foer_reduksjon_vedvirk.str() + " kroner til ".expr() + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.str() + " kroner.".expr(),
                Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekta di før du blei ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor er barnetillegget redusert frå ".expr() + barnetillegg_beloep_foer_reduksjon_vedvirk.str() + " kroner til ".expr() + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.str() + " kroner.".expr(),
                English to "Your disability benefit and child supplement together cannot exceed more than ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of your income before you became disabled. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of the income you had prior to disability is equivalent today to an income of NOK ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + ". Your disability benefit and child supplement together are higher than this. Therefore, your child supplement has been reduced from NOK ".expr() + barnetillegg_beloep_foer_reduksjon_vedvirk.str() + " to NOK ".expr() + barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk.str() + ".".expr()
            )
        }
    }

    data class Dto(
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_beloep_foer_reduksjon_vedvirk: Number,
        val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk: Number,
    )
}

object IkkeUtbetaltBTPgaTak_001 : Phrase<IkkeUtbetaltBTPgaTak_001.Dto> {
    override val elements = phrase {
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk)
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk =
            argument().select(Dto::barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekten din før du ble ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor får du ikke utbetalt barnetillegg.".expr(),
                Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av inntekta di før du blei ufør. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " prosent av den inntekta du hadde før du blei ufør, svarer i dag til ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor får du ikkje utbetalt barnetillegg.".expr(),
                English to "Your disability benefit and child supplement together cannot exceed more than ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of your income before you became disabled. ".expr() + barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk.str() + " percent of the income you had prior to disability is equivalent today to an income of NOK ".expr() + barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk.str() + ". Your disability benefit and child supplement together are higher than this. Therefore, you will not receive child supplement.".expr()
            )
        }
    }

    data class Dto(
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
    )
}

object InfoBTSBInntekt_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Du kan enkelt melde fra om inntektsendringer på nav.no.",
                Nynorsk to "Inntekta di påverkar det du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Du kan enkelt melde frå om inntektsendringar på nav.no.",
                English to "Your income affects how much child supplement you receive. If your income is over the limit for receiving full child supplement, your child supplement will be reduced. This limit is called the exemption amount. You can easily report changes in your income at nav.no."
            )
        }
    }
}

object InfoBTOverfortTilSBInntekt_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. Du kan enkelt melde fra om inntektsendringer på nav.no.",
                Nynorsk to "Inntekta di påverkar det du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn. Du kan enkelt melde frå om inntektsendringar på nav.no.",
                English to "Your income affects how much child supplement you receive. If your income is over the limit for receiving full child supplement, your child supplement will be reduced. This limit is called the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child. You can easily report changes in your income at nav.no."
            )
        }
    }
}

object IkkeRedusBTSBPgaInntekt_001 : Phrase<IkkeRedusBTSBPgaInntekt_001.Dto> {
    override val elements = phrase {
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
        val barnetillegg_saerkullsbarn_fribeloep_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_fribeloep_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er lavere enn fribeløpet ditt på ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + " kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.".expr(),
                Nynorsk to "Inntekta di på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er lågare enn fribeløpet ditt på ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + " kroner. Derfor er barnetillegget ikkje redusert ut frå inntekt.".expr(),
                English to "Your income of NOK ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " is lower than your exemption amount of NOK ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + ". Therefore, your child supplement has not been reduced on the basis of your income.".expr()
            )
        }

    }

    data class Dto(
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Number,
        val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Number,
    )
}

object RedusBTSBPgaInntekt_001 : Phrase<RedusBTSBPgaInntekt_001.Dto> {
    override val elements = phrase {
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
        val barnetillegg_saerkullsbarn_fribeloep_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_fribeloep_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er høyere enn fribeløpet ditt på ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + " kroner. Derfor er barnetillegget redusert ut fra inntekt.".expr(),
                Nynorsk to "Inntekta di på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er høgare enn fribeløpet ditt på ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + " kroner. Derfor er barnetillegget redusert ut frå inntekt.".expr(),
                English to "Your income of NOK ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " is higher than your exemption amount of NOK ".expr() + barnetillegg_saerkullsbarn_fribeloep_vedvirk.str() + ". Therefore, your child supplement has been reduced on the basis of your income.".expr()
            )
        }

    }

    data class Dto(
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Number,
        val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Number,
    )
}

object JusterBelopRedusBTPgaInntekt_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. Det blei teke omsyn til dette då vi endra barnetillegget.",
                English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. This was taken into account when we changed your child supplement."
            )
        }
    }
}

object JusterBelopIkkeUtbetaltBTPgaInntekt_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. You have already received the amount you are entitled to this year, and therefore you will not receive child supplement for the remainder of the year."
            )
        }
    }
}

object IkkeUtbetaltBTSBPgaInntekt_001 : Phrase<IkkeUtbetaltBTSBPgaInntekt_001.Dto> {
    override val elements = phrase {
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk)
        val barnetillegg_saerkullsbarn_inntektstak_vedvirk =
            argument().select(Dto::barnetillegg_saerkullsbarn_inntektstak_vedvirk)
        paragraph {
            textExpr(
                Bokmal to "Inntekten din på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er over ".expr() + barnetillegg_saerkullsbarn_inntektstak_vedvirk.str() + " kroner som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg.".expr(),
                Nynorsk to "Inntekta di på ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " kroner er over ".expr() + barnetillegg_saerkullsbarn_inntektstak_vedvirk.str() + " kroner, som er grensa for å få utbetalt barnetillegg. Derfor får du ikkje utbetalt barnetillegg.".expr(),
                English to "Your income of NOK ".expr() + barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk.str() + " is over the income limit for receiving a child supplement, which is NOK ".expr() + barnetillegg_saerkullsbarn_inntektstak_vedvirk.str() + ". Therefore, you will not receive child supplement.".expr()
            )
        }

    }

    data class Dto(
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Number,
        val barnetillegg_saerkullsbarn_inntektstak_vedvirk: Number,
    )
}

object HjemmelBT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15.",
                English to "This decision was made pursuant to the provisions § 12-15 of the National Insurance Act."
            )
        }
    }
}

object HjemmelBTOvergangsregler_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                English to "This decision was made pursuant to the provisions of § 12-15 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
            )
        }
    }
}

object HjemmelBTRedus_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16.",
                English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act."
            )
        }
    }
}

object HjemmelBTRedusOvergangsregler_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit."
            )
        }
    }
}

object MerInfoBT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget.",
                Nynorsk to "Du kan lese meir om utrekninga av barnetillegg i vedlegget.",
                English to "You can read more about how child supplement is calculated in the appendix."
            )
        }
    }
}

object GjRettSamboerOverskrift : Phrase<GjRettSamboerOverskrift.Dto> {
    override val elements = phrase {
        val avdod_navn = argument().select(Dto::avdod_navn)
        title1 {
            textExpr(
                Bokmal to "Rettigheter du kan ha som tidligere samboer med ".expr() + avdod_navn,
                Nynorsk to "Rettar du kan ha som tidlegare sambuar med ".expr() + avdod_navn,
                English to "Rights you may be entitled to as a former cohabitant with ".expr() + avdod_navn
            )
        }
    }

    data class Dto(val avdod_navn: String)
}

object GjRettUTSamboer_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Samboere som tidligere har vært gift, eller som har eller har hatt felles barn, kan ha rett til gjenlevendetillegg i uføretrygden. Du finner mer informasjon og søknadsskjema for gjenlevende ektefelle, partner eller samboer på nav.no.",
                Nynorsk to "Sambuarar som tidlegare har vore gift, eller som har eller har hatt felles barn, kan ha rett til attlevandetillegg i uføretrygda. Du finn meir informasjon og søknadsskjema for attlevande ektefelle, partnar eller sambuar på nav.no.",
                English to "Cohabitants who have previously been married, or who have or have had children together, may be entitled to survivor's supplement to disability benefit. You will find more information and the application form for benefits for surviving spouse, partner or cohabitant at nav.no."
            )
        }
    }
}

object RettTilUTGJTOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Du kan ha rett til gjenlevendetillegg i uføretrygden",
                Nynorsk to "Du kan ha rett til attlevandetillegg i uføretrygda",
                English to "You might be entitled to a survivor's supplement to disability benefit"
            )
        }
    }
}

object HvemUTGJTVilkar_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "For å ha rett til gjenlevendetillegg i uføretrygden, må du som hovedregel:",
                Nynorsk to "For å ha rett til attlevandetillegg i uføretrygda, må du som hovudregel:",
                English to "To be entitled to a survivor's supplement to disability benefit, you must as a rule:",
            )
            list {
                text(
                    Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                    Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                    English to "be a member of the National Insurance Scheme, and the deceased must have been a member of the National Insurance Scheme for the last five years prior to death "
                )

                text(
                    Bokmal to "ha vært gift med den avdøde i minst fem år, eller",
                    Nynorsk to "ha vore gift med den avdøde i minst fem år, eller",
                    English to "have been married to the deceased for at least five years, or "
                )
                text(
                    Bokmal to "ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller",
                    Nynorsk to "ha vore gift eller vore sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                    English to "have been married to or a cohabitant with the deceased, and have/had children together, or "
                )
                text(
                    Bokmal to "ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år.",
                    Nynorsk to "ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år.",
                    English to "have had care of the children of the deceased at the time of the death. The marriage and care of the child after the death must have lasted for at least five years."
                )
            }

        }
        paragraph {
            text(
                Bokmal to "Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. Du kan lese mer om dette på nav.no.",
                Nynorsk to "Sjølv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avdøde. Du kan lese meir om dette på nav.no.",
                English to "Even if you are not entitled to benefits in accordance with the general rules, you may nevertheless have rights as a surviving spouse. You can read more about this at nav.no."
            )
        }
    }
}

object HvordanSoekerDuOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Hvordan søker du?",
                Nynorsk to "Korleis søkjer du?",
                English to "How do you apply?"
            )
        }
    }
}

object SoekUTGJT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Vi oppfordrer deg til å søke om gjenlevendetillegg i uføretrygden så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. Du finner informasjon og søknadsskjemaet for gjenlevende ektefelle, partner eller samboer på nav.no/gjenlevendeektefelle",
                Nynorsk to "Vi oppmodar deg til å søkje om attlevandetillegg i uføretrygda så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader. Du finn informasjon og søknadsskjemaet for attlevande ektefelle, partner eller sambuar på nav.no/gjenlevendeektefelle",
                English to "We encourage you to apply for survivor's supplement to disability benefit as soon as possible because we normally only pay retroactively for three months. You will find information and the application form for a surviving spouse, partner or cohabitant at nav.no/gjenlevendeektefelle"
            )
        }

    }
}

object SoekAvtaleLandUT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
                Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdeorgana i bustadslandet ditt og søkje om ytingar etter avdøde.",
                English to "If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension."
            )
        }
    }
}

object AvdodBoddArbUtlandOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                Nynorsk to "Rettar dersom avdøde har budd eller arbeidd i utlandet",
                English to "Rights if the deceased has lived or worked abroad"
            )
        }
    }
}

object AvdodBoddArbUtland2_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i gjenlevendetillegg. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får ubetalt i attlevandetillegg. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                English to "If the deceased has lived or worked abroad, this may affect the amount of your survivor's supplement. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement."
            )
        }
    }
}

object PensjonFraAndreOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Andre pensjonsordninger",
                Nynorsk to "Andre pensjonsordningar",
                English to "Other pension schemes"
            )
        }
    }
}

object InfoAvdodPenFraAndre_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, må du kontakte arbeidsgivaren til avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you must contact the deceased's employer. You can also contact the pension scheme or insurance."
            )
        }
    }
}

object HarBarnUnder18Overskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "For deg som har barn under 18 år",
                Nynorsk to "For deg som har barn under 18 år",
                English to "If you have children under the age of 18"
            )
        }
    }
}

object HarBarnUtenBT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette.",
                Nynorsk to "Har du barn under 18 år, og ikkje er innvilga barnetillegg, kan du søkje om dette.",
                English to "If you have children under the age of 18, and have not been granted a child supplement, you can apply for this."
            )
        }
    }
}

object HarBarnUnder18_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadsskjema og mer informasjon om dette på nav.no.",
                Nynorsk to "Syter du for barn under 18 år, kan du ha rett til utvida barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finn søknadsskjema og meir informasjon om dette på nav.no.",
                English to "If you provide for children under the age of 18, you may be entitled to extended child benefit. In addition, children may be entitled to a children's pension. You will find the application form and more information about this at nav.no."
            )
        }
    }
}

object VirknTdsPktOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Dette er virkningstidspunktet ditt",
                Nynorsk to "Dette er verknadstidspunktet ditt",
                English to "This is your effective date"
            )
        }
    }
}

object VirkTdsPktUT_001 : Phrase<VirkTdsPktUT_001.Dto> {
    override val elements = phrase {
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() + krav_virkedato_fom.format() + ".".expr(),
                Nynorsk to "Uføretrygda di er rekna om frå ".expr() + krav_virkedato_fom.format() + ".".expr(),
                English to "Your disability benefit has been recalculated from ".expr() + krav_virkedato_fom.format() + ".".expr()
            )
        }
    }

    data class Dto(val krav_virkedato_fom: LocalDate)
}

object VirkTdsPktUTIkkeEndring_001 : Phrase<VirkTdsPktUTIkkeEndring_001.Dto> {
    override val elements = phrase {
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() + krav_virkedato_fom.format() + ", men dette fører ikke til endring i utbetalingen.".expr(),
                Nynorsk to " Uføretrygda di er rekna om frå ".expr() + krav_virkedato_fom.format() + ", men det fører ikkje til endring i utbetalinga.".expr(),
                English to "We have recalculated your disability benefit from ".expr() + krav_virkedato_fom.format() + ", however this will not lead to change in your payment.".expr()
            )
        }
    }

    data class Dto(val krav_virkedato_fom: LocalDate)
}

object VirkTdsPktUTBTOmregn_001 : Phrase<VirkTdsPktUTBTOmregn_001.Dto> {
    override val elements = phrase {
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Barnetillegget i uføretrygden din er omregnet fra ".expr() + krav_virkedato_fom.format() + ".".expr(),
                Nynorsk to "Barnetillegget i uføretrygda di er rekna om frå ".expr() + krav_virkedato_fom.format() + ".".expr(),
                English to "We have recalculated the child supplement in your disability benefit from ".expr() + krav_virkedato_fom.format() + ".".expr()
            )
        }
    }

    data class Dto(val krav_virkedato_fom: LocalDate)
}

object VirkTdsPktUTAvkortetTil0_001 : Phrase<VirkTdsPktUTAvkortetTil0_001.Dto> {
    override val elements = phrase {
        val krav_virkedato_fom = argument().select(Dto::krav_virkedato_fom)
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er omregnet fra ".expr() + krav_virkedato_fom.format() + ", men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr.".expr(),
                Nynorsk to " Uføretrygda di er rekna om frå ".expr() + krav_virkedato_fom.format() + ", men det fører ikkje til endring i utbetalinga då uføretrygda er redusert til 0 kr.".expr(),
                English to "We have recalculated your disability benefit from".expr() + krav_virkedato_fom.format() + ". However this will not lead to change in your payment because your disability benefit is reduced to NOK 0.".expr()
            )
        }
    }

    data class Dto(val krav_virkedato_fom: LocalDate)
}

object MeldInntektUTOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Du må melde fra om eventuell inntekt",
                Nynorsk to "Du må melde frå om eventuell inntekt",
                English to "You must report all changes in income"
            )
        }
    }
}

object MeldInntektUT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd.",
                Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd.",
                English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive."
            )
        }
    }
}

object MeldInntektUTBT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg.",
                Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd og barnetillegg. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd og barnetillegg.",
                English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit and child supplement you will receive."
            )
        }
    }
}

object MeldEndringerPesys_001 : Phrase<Unit> {
    override val elements = phrase {
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
                Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.",
                Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til NAV.",
                English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to NAV."
            )
        }
    }
}

object RettTilKlagePesys_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "Du har rett til å klage",
                English to "You have the right to appeal"
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
                Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klagen skal vera skriftleg. Du finn skjema og informasjon på nav.no/klage.",
                English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more information about appeals at nav.no."
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
}

object RettTilInnsynPesys_001 : Phrase<Unit> {
    override val elements = phrase {
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
}

object SjekkUtbetalingeneOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Sjekk utbetalingene dine",
                Nynorsk to "Sjekk utbetalingane dine",
                English to "Information about your payments"
            )
        }
    }
}

object SjekkUtbetalingeneUT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt.",
                Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. Du kan sjå alle utbetalingar du har fått på nav.no/dittnav. Her kan du også endre kontonummeret ditt.",
                English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. To see all the payments you have received, go to: nav.no/ dittnav. You may also change your account number here."
            )
        }
    }
}

object SkattekortOverskrift_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Skattekort",
                Nynorsk to "Skattekort",
                English to "Tax card"
            )
        }
    }
}

object SkattekortUT_001 : Phrase<Unit> {
    override val elements = phrase {
        paragraph {
            text(
                Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Skattekortet kan du endre på skatteetaten.no. På nettjenesten Ditt NAV på nav.no kan du se hvilket skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønsker det.",
                Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Skattekortet kan du endre på skatteetaten.no. På nettenesten Ditt NAV på nav.no kan du sjå kva skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønskjer det.",
                English to "The tax rules for disability benefit are the same as the tax rules for regular income. You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. You may change your tax card at skatteetaten.no. At the online service “Ditt NAV” at nav.no, you may see your registered income tax rate and change it if you wish."
            )
        }
    }
}

object SkattBorIUtlandPesys_001 : Phrase<Unit> {
    override val elements = phrase {
        title1 {
            text(
                Bokmal to "Skatt for deg som bor i utlandet",
                Nynorsk to "Skatt for deg som bur i utlandet",
                English to "Tax for people who live abroad"
            )
        }
        paragraph {
            text(
                Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
                English to "You can find more information about withholding tax to Norway at skatteetaten.no. For information about taxation from your country of residence, you can contact the locale tax authorities."
            )
        }
    }
}