package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
        lang = languages(Bokmal, Nynorsk, English),
        title = newText(
            Bokmal to "NAV har regnet om uføretrygden din",
            Nynorsk to "", // TODO
            English to "" // TODO
        ),
        letterMetadata = LetterMetadata(
            "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true
        )
    ) {

        val barnetillegg_er_redusert_mot_tak =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_er_redusert_mot_tak)
        val barnetillegg_ikke_utbetalt_pga_tak =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_ikke_utbetalt_pga_tak)
        val barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk =
            argument().select(UfoerOmregningEnsligDto::barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
        val barn_overfoert_til_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::barn_overfoert_til_saerkullsbarn)
        val bor_i_avtaleland = argument().select(UfoerOmregningEnsligDto::bor_i_avtaleland)
        val bor_i_norge = argument().select(UfoerOmregningEnsligDto::bor_i_norge)
        val ektefelletillegg_opphoert = argument().select(UfoerOmregningEnsligDto::ektefelletillegg_opphoert)
        val er_eps_ikke_paragraf_3_2_samboer =
            argument().select(UfoerOmregningEnsligDto::er_eps_ikke_paragraf_3_2_samboer)
        val er_paa_helseinstitusjon = argument().select(UfoerOmregningEnsligDto::er_paa_helseinstitusjon)
        val er_paragraf_3_2_samboer = argument().select(UfoerOmregningEnsligDto::er_paragraf_3_2_samboer)
        val gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt =
            argument().select(UfoerOmregningEnsligDto::gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt)
        val gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet =
            argument().select(UfoerOmregningEnsligDto::gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet)
        val har_barnetillegg_for_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_for_saerkullsbarn)
        val har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk)
        val har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk =
            argument().select(UfoerOmregningEnsligDto::har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
        val har_barnetillegg_vedvirk = argument().select(UfoerOmregningEnsligDto::har_barnetillegg_vedvirk)
        val har_barn_som_tidligere_var_saerkullsbarn =
            argument().select(UfoerOmregningEnsligDto::har_barn_som_tidligere_var_saerkullsbarn)
        val har_felles_barn_uten_barnetillegg_med_avdod =
            argument().select(UfoerOmregningEnsligDto::har_felles_barn_uten_barnetillegg_med_avdod)
        val har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd =
            argument().select(UfoerOmregningEnsligDto::har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd)
        val har_flere_ufoeretrygd_perioder = argument().select(UfoerOmregningEnsligDto::har_flere_ufoeretrygd_perioder)
        val har_minsteytelse_vedvirk = argument().select(UfoerOmregningEnsligDto::har_minsteytelse_vedvirk)
        val har_minste_inntektsnivaa_foer_ufoeretrygd =
            argument().select(UfoerOmregningEnsligDto::har_minste_inntektsnivaa_foer_ufoeretrygd)
        val har_ufoeremaaned_vedvirk = argument().select(UfoerOmregningEnsligDto::har_ufoeremaaned_vedvirk)
        val inntekt_ufoere_endret = argument().select(UfoerOmregningEnsligDto::inntekt_ufoere_endret)
        val i_fengsel_ved_virk = argument().select(UfoerOmregningEnsligDto::i_fengsel_ved_virk)
        val ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet =
            argument().select(UfoerOmregningEnsligDto::ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
        val ufoeretrygd_vedvirk_er_inntektsavkortet =
            argument().select(UfoerOmregningEnsligDto::ufoeretrygd_vedvirk_er_inntektsavkortet)


        outline {
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak", // TODO
                    English to "Decision" // TODO
                )
            }
            showIf(
                (har_minsteytelse_vedvirk
                        or inntekt_ufoere_endret
                        or ektefelletillegg_opphoert)
                        and not(har_barnetillegg_for_saerkullsbarn)
            ) {
                // omregnUTDodEPSInnledn1_001
                paragraph {
                    text(
                        Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om uføretrygden din fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.",
                        Nynorsk to "Vi har mottatt melding om at <avdod.navn> er død, og vi har rekna om uføretrygda di frå <krav.virkDatoFom> fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.",
                        English to "We have received notice that <avdod.navn> has died, and we have recalculated your disability benefit from <krav.virkDatoFom>, because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse."
                    )
                }
            }


            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(har_barnetillegg_for_saerkullsbarn)
            ) {
                // omregnUTDodEPSInnledn2_001
                paragraph {
                    text(
                        Bokmal to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygden din endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.",
                        Nynorsk to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygda di blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.",
                        English to "We have received notice that <avdod.navn> has died. This will not affect your disability benefit, but we would like to inform you about rights you may have as a surviving spouse."
                    )
                }
            }

            showIf(
                (har_minsteytelse_vedvirk
                        or inntekt_ufoere_endret
                        or ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn
            ) {
                //omregnUTBTDodEPSInnledn_001
                paragraph {
                    text(
                        Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om uføretrygden og barnetillegget ditt fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.",
                        Nynorsk to "Vi har mottatt melding om at <avdod.navn> er død, og vi har rekna om uføretrygda di og barnetillegget ditt frå <krav.virkDatoFom> fordi sivilstanden din har blitt endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.",
                        English to "We have received notice that <avdod.navn> has died, and we have recalculated your disability benefit and child supplement from <krav.virkDatoFom> because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse."
                    )
                }
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and har_barnetillegg_for_saerkullsbarn
                        and not(barn_overfoert_til_saerkullsbarn)
            ) {
                //omregnUTBTSBDodEPSInnledn_001
                paragraph {
                    text(
                        Bokmal to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygden og barnetillegget ditt endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.",
                        Nynorsk to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygda di og barnetillegget ditt blir ikkje endra, men vi vil informere deg om kva rettar du kan ha etter avdøde.",
                        English to "We have received notice that <avdod.navn> has died. This will not affect your disability benefit or child supplement, but we would like to inform you about rights you may have as a surviving spouse." // TODO
                    )
                }
            }

            showIf(
                not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and barn_overfoert_til_saerkullsbarn
            ) {
                // omregnBTDodEPSInnledn_001
                paragraph {
                    text(
                        Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om barnetillegget ditt fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.",
                        Nynorsk to "Vi har mottatt melding om at <avdod.navn> er død, og vi har rekna om barnetillegget ditt frå <krav.virkDatoFom> fordi sivilstanden din er endra. Vi vil også informere deg om kva rettar du kan ha etter avdøde.",
                        English to "We have received notice that <avdod.navn> has died, and we have recalculated your child supplement from <krav.virkDatoFom> because your marital status has changed. We would also like to inform you about rights you may have as a surviving spouse."
                    )
                }
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                //belopUT_001
                paragraph {
                    text(
                        Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt.",
                        Nynorsk to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd kvar månad før skatt.",
                        English to "Your monthly disability benefit payment will be NOK <uforetrygdVedVirk.totalUforeMnd> before tax."
                    )
                }
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
            ) {
                //belopUTVedlegg_001
                paragraph {
                    text(
                        Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.", // TODO
                        English to "Your monthly disability benefit payment will be NOK <uforetrygdVedVirk.totalUforeMnd> before tax. You can read more about other calculation periods in the appendix." // TODO
                    )
                }
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                //belopUTBT_001
                paragraph {
                    text(
                        Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd og barnetillegg kvar månad før skatt.", // TODO
                        English to "Your monthly disability benefit and child supplement payment will be NOK <uforetrygdVedVirk.totalUforeMnd> before tax." // TODO
                    )
                }
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and har_barnetillegg_for_saerkullsbarn
                        and har_flere_ufoeretrygd_perioder
            ) {
                // belopUTBTVedlegg_001>
                paragraph {
                    text(
                        Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd og barnetillegg per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd og barnetillegg kvar månad før skatt. Du kan lese meir om andre utrekningsperiodar i vedlegget.", // TODO
                        English to "Your monthly disability benefit and child supplement payment will be NOK <uforetrygdVedVirk.totalUforeMnd> before tax. You can read more about other calculation periods in the appendix." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
                        and not(i_fengsel_ved_virk)
            ) {
                // belopUTIngenUtbetaling_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt.", // TODO
                        English to "You will not receive disability benefit payment because of your reported income." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
                        and not(i_fengsel_ved_virk)
            ) {
                // belopUTIngenUtbetalingVedlegg_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.", // TODO
                        English to "You will not receive disability benefit payment because of your reported income. You can read more about other calculation periods in the appendix." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                        and not(har_flere_ufoeretrygd_perioder)
            ) {
                // belopUTBTIngenUtbetaling_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt.", // TODO
                        English to "You will not receive disability benefit and child supplement payment because of your reported income." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                        and har_flere_ufoeretrygd_perioder
            ) {
                //belopUTBTIngenUtbetalingVedlegg_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd og barnetillegg på grunn av høg inntekt. Du kan lese meir om andre utrekningsperiodar i vedlegget.", // TODO
                        English to "You will not receive disability benefit and child supplement payment because of your reported income. You can read more about other calculation periods in the appendix." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and not(har_flere_ufoeretrygd_perioder)
                        and i_fengsel_ved_virk
            ) {
                // belopUTIngenUtbetalingFengsel_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring.", // TODO
                        English to "You will not receive disability benefit because you are serving a criminal sentence." // TODO
                    )
                }
            }

            showIf(
                not(har_ufoeremaaned_vedvirk)
                        and not(har_barnetillegg_for_saerkullsbarn)
                        and har_flere_ufoeretrygd_perioder
                        and i_fengsel_ved_virk
            ) {
                // belopUTIngenUtbetalingFengselVedlegg_001
                paragraph {
                    text(
                        Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese meir om andre utrekningsperiodar i vedlegget.", // TODO
                        English to "You will not receive disability benefit because you are serving a criminal sentence. You can read more about other calculation periods in the appendix." // TODO
                    )
                }
            }


            //begrunnOverskrift_001
            //HVIS
            //HAR_MINSTEYTELSE_VEDVIRK
            //ELLER
            //INNTEKT_UFOERE_ENDRET

            title1 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "Grunngiving for vedtaket", // TODO
                    English to "Grounds for the decision" // TODO
                )
            }

            //endrMYDodEPS2_001
            //HAR_MINSTEYTELSE_VEDVIRK
            //OG
            //!INNTEKT_UFOERE_ENDRET
            paragraph {
                text(
                    Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen <minsteytelseVedVirk.sats> ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.",
                    Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga <minsteytelseVedVirk.sats> gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.", // TODO
                    English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is <minsteytelseVedVirk.sats> times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no." // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.",
                    Nynorsk to "Dette kan ha noko å seie for kompensasjonsgraden din som er fastsett til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese meir om dette i vedlegget.", // TODO
                    English to "This may affect your degree of compensation, which is determined to be <uforetrygdVedVirk.kompensasjonsgrad> percent. You can read more about this in the appendix." // TODO
                )
            }//<


            showIf(har_minsteytelse_vedvirk and inntekt_ufoere_endret) {
                //endrMYOgMinstIFUDodEPS2_001
                paragraph {
                    text(
                        Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen <minsteytelseVedVirk.sats> ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.",
                        Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Satsen på minsteytinga avheng av sivilstand. For deg utgjer minsteytinga <minsteytelseVedVirk.sats> gonger grunnbeløpet i folketrygda. Du kan lese meir om grunnbeløp på nav.no.", // TODO
                        English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. Your minimum benefit is <minsteytelseVedVirk.sats> times the National Insurance basic amount. You can read more about the National Insurance basic amount at nav.no." // TODO
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Kompensasjonsgraden din er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.",
                        Nynorsk to "Endringar i sivilstanden påverkar også inntekta di før du blei ufør. Denne utgjer <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Kompensasjonsgraden din er fastsett til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese meir om dette i vedlegget.", // TODO
                        English to "The change in your marital status also affects your income prior to disability, which is determined to be NOK <inntektForUforeVedVirk.ifuInntekt>. Adjusted to today’s value, this is equivalent to an income of NOK <inntektForUforeVedVirk.oppjustertIFU>. Your degree of compensation is determined to be <uforetrygdVedVirk.kompensasjonsgrad> percent. You can read more about this in the appendix." // TODO
                    )
                }//<
            }


            showIf(not(har_minsteytelse_vedvirk) and inntekt_ufoere_endret) {
                //endrMinstIFUDodEPS2_001
                paragraph {
                    text(
                        Bokmal to "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.",
                        Nynorsk to "Inntekta di før du blei ufør er fastsett til minstenivå, som er avhengig av sivilstand. For deg er inntekta di før du blei ufør fastsett til <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til verknadstidspunktet svarer til ei inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Dette kan ha noko å seie for kompensasjonsgraden din, som er fastsett til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese meir om dette i vedlegget.", // TODO
                        English to "Your income prior to disability is set to the minimum level, which depends on marital status. The change in your marital status affects your income prior to disability, which is determined to be NOK <inntektForUforeVedVirk.ifuInntekt>. Adjusted to today’s value, this is equivalent to an income of NOK <inntektForUforeVedVirk.oppjustertIFU>. This may affect your degree of compensation, which has been determined to be <uforetrygdVedVirk.kompensasjonsgrad> percent. You can read more about this in the appendix." // TODO
                    )
                }
            }


            showIf(
                not(har_minste_inntektsnivaa_foer_ufoeretrygd)
                        and not(ufoeretrygd_vedvirk_er_inntektsavkortet)
            ) {
                // hjemmelSivilstandUT_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13 og 22-12.", // TODO
                        English to "This decision was made pursuant to the provisions of §§ 12-13 and 22-12 of the National Insurance Act." // TODO
                    )
                }
            }

            showIf(
                har_minste_inntektsnivaa_foer_ufoeretrygd
                        and not(ufoeretrygd_vedvirk_er_inntektsavkortet)
            ) {
                //hjemmelSivilstandUTMinsteIFU_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13 og 22-12.", // TODO
                        English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13 and 22-12 of the National Insurance Act." // TODO
                    )
                }
            }

            showIf(
                not(har_minste_inntektsnivaa_foer_ufoeretrygd)
                        and ufoeretrygd_vedvirk_er_inntektsavkortet
            ) {
                //hjemmelSivilstandUTAvkortet_001>
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-13, 12-14 og 22-12.", // TODO
                        English to "This decision was made pursuant to the provisions of §§ 12-13, 12-14 and 22-12 of the National Insurance Act." // TODO
                    )
                }
            }

            showIf(
                har_minste_inntektsnivaa_foer_ufoeretrygd
                        and ufoeretrygd_vedvirk_er_inntektsavkortet
            ) {
                //hjemmelSivilstandUTMinsteIFUAvkortet_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-9, 12-13, 12-14 og 22-12.", // TODO
                        English to "This decision was made pursuant to the provisions of §§ 12-9, 12-13, 12-14 and 22-12 of the National Insurance Act." // TODO
                    )
                }
            }

            showIf(er_paa_helseinstitusjon) {
                //hjemmelEPSDodUTInstitusjon_001
                paragraph {
                    text(
                        Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon.",
                        Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-19 så lenge du er på institusjon.", // TODO
                        English to "Your disability benefit is calculated according to the provisions of § 12-19 of the National Insurance Act, as long as you are institutionalized." // TODO
                    )
                }
            }

            showIf(i_fengsel_ved_virk) {
                //hjemmelEPSDodUTFengsel_001
                paragraph {
                    text(
                        Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring.",
                        Nynorsk to "Uføretrygda di reknast ut etter folketrygdlova § 12-20 så lenge du er under straffegjennomføring.", // TODO
                        English to "Your disability benefit is calculated according to the provisions of § 12-20 of the National Insurance Act, as long as you are serving a criminal sentence." // TODO
                    )
                }
            }


            //EKTEFELLETILLEGG_OPPHOERT
            showIf(ektefelletillegg_opphoert) {
                //opphorETOverskrift_001
                title1 {
                    text(
                        Bokmal to "Ektefelletillegget ditt opphører",
                        Nynorsk to "Ektefelletillegget ditt avsluttast", // TODO
                        English to "Your spouse supplement will end" // TODO
                    )
                }


                //opphorET_001
                paragraph {
                    text(
                        Bokmal to "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt.",
                        Nynorsk to "Du syter ikkje lenger for ein ektefelle partnar sambuar. Derfor vert ektefelletillegget ditt avslutta.", // TODO
                        English to "You no longer provide for a spousepartnercohabitant. Your spouse supplement will therefore end." // TODO
                    )
                }


                //hjemmelET_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8.",
                        Nynorsk to "Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8.", // TODO
                        English to "The decision has been made pursuant to Section 8 of the transitional provisions for the implementation of disability benefit." // TODO
                    )
                }
            }



            showIf(barn_overfoert_til_saerkullsbarn) {
                //omregningFBOverskrift_001
                title1 {
                    text(
                        Bokmal to "Barnetillegg for fellesbarn er regnet om",
                        Nynorsk to "Barnetillegg for fellesbarn er rekna om", // TODO
                        English to "Your child supplement has been recalculated" // TODO
                    )
                }

                //infoFBTilSB_001>
                //<navn><
                paragraph {
                    text(
                        Bokmal to "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:",
                        Nynorsk to "Vi har rekna om barnetillegget for barn som ikkje lenger bur saman med begge foreldre. Dette gjeld:", // TODO
                        English to "We have recalculated your child supplement for the child/children who no longer lives/live together with both parents. This applies to:" // TODO
                    )
                    //TODO vis liste med barnOverfortTilSB.enkeltbarnListe
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and har_barn_som_tidligere_var_saerkullsbarn
                            and not(inntekt_ufoere_endret and har_minsteytelse_vedvirk)
                ) {
                    //infoTidligereSB_001>
                    //<navn><
                    paragraph {
                        text(
                            Bokmal to "Denne omregningen har også betydning for barnetillegget for:",
                            Nynorsk to "Denne omrekninga har også noko å seie for barnetillegget for:", // TODO
                            English to "This recalculation also affects your child supplement for:" // TODO
                        )
                        //TODO vis liste med barnTidligereSB.enkeltbarnListe
                    }
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and (inntekt_ufoere_endret or har_minsteytelse_vedvirk)
                            and (barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk or barnetillegg_er_redusert_mot_tak)
                ) {
                    //infoTidligereSBOgEndretUT_001>
                    paragraph {
                        text(
                            Bokmal to "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:",
                            Nynorsk to "Omrekninga av barnetillegget og endring i uføretrygda di har også noko å seie for barnetillegget for:", // TODO
                            English to "Recalculation of your child supplement and change in your disability benefit also affects your child supplement for:" // TODO
                        )
                        //TODO vis liste med barnTidligereSB.enkeltbarnListe
                    }
                }
            }


            showIf(
                har_barnetillegg_for_saerkullsbarn
                        and (
                        har_minsteytelse_vedvirk
                                or inntekt_ufoere_endret
                                or ektefelletillegg_opphoert
                        )
            ) {
                //endringUTpavirkerBTOverskrift_001
                title1 {
                    text(
                        Bokmal to "Slik påvirker endring av uføretrygden barnetillegget ditt",
                        Nynorsk to "Slik påverkar endringa av uføretrygda barnetillegget ditt", // TODO
                        English to "How the change in your disability benefit affects your child supplement" // TODO
                    )
                }

                showIf(not(barnetillegg_er_redusert_mot_tak)) {
                    //ikkeRedusBTPgaTak_001
                    paragraph {
                        text(
                            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen lavere enn dette. Derfor er barnetillegget fastsatt til 40 prosent av folketrygdens grunnbeløp for hvert barn.",
                            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekta di før du blei ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekta du hadde før du blei ufør, svarer i dag til <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygda di og barnetillegget ditt er til saman lågare enn dette. Derfor er barnetillegget fastsett til 40 prosent av grunnbeløpet i folketrygda for kvart barn.", // TODO
                            English to "Your disability benefit and child supplement together cannot exceed more than <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of your income before you became disabled. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of the income you had before you became disabled is equivalent today to an income of NOK <barnetilleggGrunnlagVedVirk.gradertOIFU>. Your disability benefit and child supplement together are lower than this. Therefore, your child supplement has been determined to be 40 percent of the National Insurance basic amount for each child." // TODO
                        )
                    }
                }

                showIf(barnetillegg_er_redusert_mot_tak and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    //redusBTPgaTak_001
                    paragraph {
                        text(
                            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor er barnetillegget redusert fra <barnetilleggGrunnlagVedVirk.belopForReduksjon> kroner til <barnetilleggGrunnlagVedVirk.belopEtterReduksjon> kroner.",
                            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekta di før du blei ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekta du hadde før du blei ufør, svarer i dag til <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor er barnetillegget redusert frå <barnetilleggGrunnlagVedVirk.belopForReduksjon> kroner til <barnetilleggGrunnlagVedVirk.belopEtterReduksjon> kroner.", // TODO
                            English to "Your disability benefit and child supplement together cannot exceed more than <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of your income before you became disabled. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of the income you had prior to disability is equivalent today to an income of NOK <barnetilleggGrunnlagVedVirk.gradertOIFU>. Your disability benefit and child supplement together are higher than this. Therefore, your child supplement has been reduced from NOK <barnetilleggGrunnlagVedVirk.belopForReduksjon> to NOK <barnetilleggGrunnlagVedVirk.belopEtterReduksjon>." // TODO
                        )
                    }
                }

                showIf(barnetillegg_ikke_utbetalt_pga_tak) {
                    //ikkeUtbetaltBTPgaTak_001
                    paragraph {
                        text(
                            Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor får du ikke utbetalt barnetillegg.",
                            Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekta di før du blei ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekta du hadde før du blei ufør, svarer i dag til <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygda og barnetillegget ditt er til saman høgare enn dette. Derfor får du ikkje utbetalt barnetillegg.", // TODO
                            English to "Your disability benefit and child supplement together cannot exceed more than <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of your income before you became disabled. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> percent of the income you had prior to disability is equivalent today to an income of NOK <barnetilleggGrunnlagVedVirk.gradertOIFU>. Your disability benefit and child supplement together are higher than this. Therefore, you will not receive child supplement." // TODO
                        )
                    }
                }

                showIf(
                    not(barn_overfoert_til_saerkullsbarn) and not(barnetillegg_ikke_utbetalt_pga_tak)
                ) {
                    //infoBTSBInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Du kan enkelt melde fra om inntektsendringer på nav.no.",
                            Nynorsk to "Inntekta di påverkar det du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Du kan enkelt melde frå om inntektsendringar på nav.no.", // TODO
                            English to "Your income affects how much child supplement you receive. If your income is over the limit for receiving full child supplement, your child supplement will be reduced. This limit is called the exemption amount. You can easily report changes in your income at nav.no." // TODO
                        )
                    }
                }

                showIf(barn_overfoert_til_saerkullsbarn and not(barnetillegg_ikke_utbetalt_pga_tak)) {
                    //infoBTOverfortTilSBInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. Du kan enkelt melde fra om inntektsendringer på nav.no.",
                            Nynorsk to "Inntekta di påverkar det du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn. Du kan enkelt melde frå om inntektsendringar på nav.no.", // TODO
                            English to "Your income affects how much child supplement you receive. If your income is over the limit for receiving full child supplement, your child supplement will be reduced. This limit is called the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child. You can easily report changes in your income at nav.no." // TODO
                        )
                    }
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and not(barnetillegg_ikke_utbetalt_pga_tak)
                ) {
                    //ikkeRedusBTSBPgaInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er lavere enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.",
                            Nynorsk to "Inntekta di på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er lågare enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget ikkje redusert ut frå inntekt.", // TODO
                            English to "Your income of NOK <barnetilleggSBVedVirk.inntektBruktIAvkortning> is lower than your exemption amount of NOK <barnetilleggSBVedVirk.fribelop>. Therefore, your child supplement has not been reduced on the basis of your income." // TODO
                        )
                    }
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and (har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                            or (not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk) and har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk))
                ) {
                    //redusBTSBPgaInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er høyere enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget redusert ut fra inntekt.",
                            Nynorsk to "Inntekta di på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er høgare enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget redusert ut frå inntekt.", // TODO
                            English to "Your income of NOK <barnetilleggSBVedVirk.inntektBruktIAvkortning> is higher than your exemption amount of NOK <barnetilleggSBVedVirk.fribelop>. Therefore, your child supplement has been reduced on the basis of your income." // TODO
                        )
                    }
                }


                showIf(
                    har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk
                            and har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk
                ) {
                    //justerBelopRedusBTPgaInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
                            Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. Det blei teke omsyn til dette då vi endra barnetillegget.", // TODO
                            English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. This was taken into account when we changed your child supplement." // TODO
                        )
                    }
                }

                showIf(
                    har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk
                            and not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
                ) {
                    //justerBelopIkkeUtbetaltBTPgaInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                            Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år, påverkar også det du får i barnetillegg framover. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.", // TODO
                            English to "The amount you have received in child supplement earlier this year also affects how much child supplement you will receive henceforth. You have already received the amount you are entitled to this year, and therefore you will not receive child supplement for the remainder of the year." // TODO
                        )
                    }
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and not(har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk)
                            and not(har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk)
                ) {
                    //ikkeUtbetaltBTSBPgaInntekt_001
                    paragraph {
                        text(
                            Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er over <barnetilleggSBVedVirk.inntektstak> kroner som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg.",
                            Nynorsk to "Inntekta di på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er over <barnetilleggSBVedVirk.inntektstak> kroner, som er grensa for å få utbetalt barnetillegg. Derfor får du ikkje utbetalt barnetillegg.", // TODO
                            English to "Your income of NOK <barnetilleggSBVedVirk.inntektBruktIAvkortning> is over the income limit for receiving a child supplement, which is NOK <barnetilleggSBVedVirk.inntektstak>. Therefore, you will not receive child supplement." // TODO
                        )
                    }
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and not(ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
                ) {
                    //hjemmelBT_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15.", // TODO
                            English to "This decision was made pursuant to the provisions § 12-15 of the National Insurance Act." // TODO
                        )
                    }
                }

                showIf(
                    not(barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk)
                            and ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet
                ) {
                    //hjemmelBTOvergangsregler_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-15 og forskrift om overgangsreglar for barnetillegg i uføretrygda.", // TODO
                            English to "This decision was made pursuant to the provisions of § 12-15 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit." // TODO
                        )
                    }
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and not(ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet)
                ) {
                    //hjemmelBTRedus_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16.", // TODO
                            English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act." // TODO
                        )
                    }
                }

                showIf(
                    barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk
                            and ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet
                ) {
                    //hjemmelBTRedusOvergangsregler_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15 og 12-16 og forskrift om overgangsreglar for barnetillegg i uføretrygda.", // TODO
                            English to "This decision was made pursuant to the provisions of §§ 12-15 and 12-16 of the National Insurance Act and of the Regulation on transitional rules for child supplement in disability benefit." // TODO
                        )
                    }
                }

                showIf(gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt) {
                    //merInfoBT_001
                    paragraph {
                        text(
                            Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget.",
                            Nynorsk to "Du kan lese meir om utrekninga av barnetillegg i vedlegget.", // TODO
                            English to "You can read more about how child supplement is calculated in the appendix." // TODO
                        )
                    }
                }
            }


            showIf(er_paragraf_3_2_samboer) {
                // gjRettSamboerOverskrift
                title1 {
                    text(
                        Bokmal to "Rettigheter du kan ha som tidligere samboer med <avdod.navn>",
                        Nynorsk to "Rettar du kan ha som tidlegare sambuar med <avdod.navn>", // TODO
                        English to "Rights you may be entitled to as a former cohabitant with <avdod.navn>" // TODO
                    )
                }
                // gjRettUTSamboer_001
                paragraph {
                    text(
                        Bokmal to "Samboere som tidligere har vært gift, eller som har eller har hatt felles barn, kan ha rett til gjenlevendetillegg i uføretrygden. Du finner mer informasjon og søknadsskjema for gjenlevende ektefelle, partner eller samboer på nav.no.",
                        Nynorsk to "Sambuarar som tidlegare har vore gift, eller som har eller har hatt felles barn, kan ha rett til attlevandetillegg i uføretrygda. Du finn meir informasjon og søknadsskjema for attlevande ektefelle, partnar eller sambuar på nav.no.", // TODO
                        English to "Cohabitants who have previously been married, or who have or have had children together, may be entitled to survivor's supplement to disability benefit. You will find more information and the application form for benefits for surviving spouse, partner or cohabitant at nav.no." // TODO
                    )
                }
            }
            showIf(er_eps_ikke_paragraf_3_2_samboer) {
                // rettTilUTGJTOverskrift_001
                title1 {
                    text(
                        Bokmal to "Du kan ha rett til gjenlevendetillegg i uføretrygden",
                        Nynorsk to "Du kan ha rett til attlevandetillegg i uføretrygda", // TODO
                        English to "You might be entitled to a survivor's supplement to disability benefit" // TODO
                    )
                }

                //TODO point list support

                paragraph {
                    //hvemUTGJTVilkar_001
                    text(
                        Bokmal to "For å ha rett til gjenlevendetillegg i uføretrygden, må du som hovedregel:",
                        Nynorsk to "For å ha rett til attlevandetillegg i uføretrygda, må du som hovudregel:",
                        English to "To be entitled to a survivor's supplement to disability benefit, you must as a rule:",
                    )
                    list {
                        text(
                            Bokmal to "være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet",
                            Nynorsk to "vere medlem i folketrygda, og avdøde må ha vore medlem i folketrygda dei siste fem åra fram til dødsfallet",
                            English to "test3"
                        )

                        text(
                            Bokmal to "ha vært gift med den avdøde i minst fem år, eller",
                            Nynorsk to "ha vore gift med den avdøde i minst fem år, eller",
                            English to "test3"
                        )
                        text(
                            Bokmal to "ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller",
                            Nynorsk to "ha vore gift eller vore sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller",
                            English to "test3"
                        )
                        text(
                            Bokmal to "ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år.",
                            Nynorsk to "ha hatt omsorga for barna til den avdøde på dødsfallstidspunktet. Ekteskapet og omsorga for barnet etter dødsfallet må til saman ha vart minst fem år.",
                            English to "test3"
                        )
                    }
                    text(
                        Bokmal to "Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. Du kan lese mer om dette på nav.no.",
                        Nynorsk to "Sjølv om du ikkje har rett til ytinga etter hovudreglane, kan du likevel ha rettar etter avdøde. Du kan lese meir om dette på nav.no.",
                        English to "Even if you are not entitled to benefits in accordance with the general rules, you may nevertheless have rights as a surviving spouse. You can read more about this at nav.no."
                    )
                }
                //hvordanSøkerDuOverskrift_001
                title1 {
                    text(
                        Bokmal to "Hvordan søker du?",
                        Nynorsk to "Korleis søkjer du?", // TODO
                        English to "How do you apply?" // TODO
                    )
                }
                //søkUTGJT_001
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke om gjenlevendetillegg i uføretrygden så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. Du finner informasjon og søknadsskjemaet for gjenlevende ektefelle, partner eller samboer på nav.no/gjenlevendeektefelle",
                        Nynorsk to "Vi oppmodar deg til å søkje om attlevandetillegg i uføretrygda så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader. Du finn informasjon og søknadsskjemaet for attlevande ektefelle, partner eller sambuar på nav.no/gjenlevendeektefelle", // TODO
                        English to "We encourage you to apply for survivor's supplement to disability benefit as soon as possible because we normally only pay retroactively for three months. You will find information and the application form for a surviving spouse, partner or cohabitant at nav.no/gjenlevendeektefelle" // TODO
                    )
                }

                showIf(bor_i_avtaleland) {
                    //søkAvtaleLandUT_001
                    paragraph {
                        text(
                            Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.",
                            Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdeorgana i bustadslandet ditt og søkje om ytingar etter avdøde.", // TODO
                            English to "If you live outside Norway, you must contact the National Insurance authorities in your country of residence and apply for a pension." // TODO
                        )
                    }
                }
                //avdodBoddArbUtlandOverskrift_001
                title1 {
                    text(
                        Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                        Nynorsk to "Rettar dersom avdøde har budd eller arbeidd i utlandet", // TODO
                        English to "Rights if the deceased has lived or worked abroad" // TODO
                    )
                }

                //avdodBoddArbUtland2_001
                paragraph {
                    text(
                        Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i gjenlevendetillegg. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                        Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får ubetalt i attlevandetillegg. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.", // TODO
                        English to "If the deceased has lived or worked abroad, this may affect the amount of your survivor's supplement. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement." // TODO
                    )
                }
                //pensjonFraAndreOverskrift_001
                paragraph {
                    text(
                        Bokmal to "Andre pensjonsordninger",
                        Nynorsk to "Andre pensjonsordningar", // TODO
                        English to "Other pension schemes" // TODO
                    )
                }
                //infoAvdodPenFraAndre_001
                paragraph {
                    text(
                        Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, må du kontakte arbeidsgivaren til avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.", // TODO
                        English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you must contact the deceased's employer. You can also contact the pension scheme or insurance." // TODO
                    )
                }
            }


            showIf(har_felles_barn_uten_barnetillegg_med_avdod) {
                //harBarnUnder18Overskrift_001
                title1 {
                    text(
                        Bokmal to "For deg som har barn under 18 år",
                        Nynorsk to "For deg som har barn under 18 år", // TODO
                        English to "If you have children under the age of 18" // TODO
                    )
                }
                //harBarnUtenBT_001
                paragraph {
                    text(
                        Bokmal to "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette.",
                        Nynorsk to "Har du barn under 18 år, og ikkje er innvilga barnetillegg, kan du søkje om dette.", // TODO
                        English to "If you have children under the age of 18, and have not been granted a child supplement, you can apply for this." // TODO
                    )
                }
                //harBarnUnder18_001
                paragraph {
                    text(
                        Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadsskjema og mer informasjon om dette på nav.no.",
                        Nynorsk to "Syter du for barn under 18 år, kan du ha rett til utvida barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finn søknadsskjema og meir informasjon om dette på nav.no.", // TODO
                        English to "If you provide for children under the age of 18, you may be entitled to extended child benefit. In addition, children may be entitled to a children's pension. You will find the application form and more information about this at nav.no." // TODO
                    )
                }
            }

            //virknTdsPktOverskrift_001
            title1 {
                text(
                    Bokmal to "Dette er virkningstidspunktet ditt",
                    Nynorsk to "Dette er verknadstidspunktet ditt", // TODO
                    English to "This is your effective date" // TODO
                )
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and (har_minsteytelse_vedvirk or inntekt_ufoere_endret or ektefelletillegg_opphoert)
            ) {
                //virkTdsPktUT_001
                paragraph {
                    text(
                        Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>.",
                        Nynorsk to "Uføretrygda di er rekna om frå <krav.virkDatoFom>.", // TODO
                        English to "Your disability benefit has been recalculated from <krav.virkDatoFom>." // TODO
                    )
                }
            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and not(barn_overfoert_til_saerkullsbarn)
            ) {
                //virkTdsPktUTIkkeEndring_001
                paragraph {
                    text(
                        Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>, men dette fører ikke til endring i utbetalingen.",
                        Nynorsk to " Uføretrygda di er rekna om frå <krav.virkDatoFom>, men det fører ikkje til endring i utbetalinga.", // TODO
                        English to "We have recalculated your disability benefit from <krav.virkDatoFom>, however this will not lead to change in your payment." // TODO
                    )
                }

            }

            showIf(
                har_ufoeremaaned_vedvirk
                        and not(har_minsteytelse_vedvirk)
                        and not(inntekt_ufoere_endret)
                        and not(ektefelletillegg_opphoert)
                        and barn_overfoert_til_saerkullsbarn
            ) {
                //virkTdsPktUTBTOmregn_001
                paragraph {
                    text(
                        Bokmal to "Barnetillegget i uføretrygden din er omregnet fra <krav.virkDatoFom>.",
                        Nynorsk to "Barnetillegget i uføretrygda di er rekna om frå <krav.virkDatoFom>.", // TODO
                        English to "We have recalculated the child supplement in your disability benefit from <krav.virkDatoFom>." // TODO
                    )
                }
            }

            showIf(not(har_ufoeremaaned_vedvirk)) {
                //virkTdsPktUTAvkortetTil0_001
                paragraph {
                    text(
                        Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>, men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr.",
                        Nynorsk to " Uføretrygda di er rekna om frå <krav.virkDatoFom>, men det fører ikkje til endring i utbetalinga då uføretrygda er redusert til 0 kr.", // TODO
                        English to "We have recalculated your disability benefit from<krav.virkDatoFom>. However this will not lead to change in your payment because your disability benefit is reduced to NOK 0." // TODO
                    )
                }
            }


            //meldInntektUTOverskrift_001
            title1 {
                text(
                    Bokmal to "Du må melde fra om eventuell inntekt",
                    Nynorsk to "Du må melde frå om eventuell inntekt", // TODO
                    English to "You must report all changes in income" // TODO
                )
            }

            showIf(not(har_barnetillegg_vedvirk)) {
                //meldInntektUT_001
                paragraph {
                    text(
                        Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd.",
                        Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd.", // TODO
                        English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive." // TODO
                    )
                }
            }

            showIf(har_barnetillegg_vedvirk) {
                //meldInntektUTBT_001
                paragraph {
                    text(
                        Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg.",
                        Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd og barnetillegg. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd og barnetillegg.", // TODO
                        English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit and child supplement payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit and child supplement you will receive." // TODO
                    )
                }
            }

            //meldEndringerPesys_001
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar", // TODO
                    English to "You must notify NAV if anything changes" // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget ser du kva endringar du må seie frå om.", // TODO
                    English to "If your circumstances change, you must inform NAV immediately. The appendix includes information on how to proceed." // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.",
                    Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til NAV.", // TODO
                    English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to NAV." // TODO
                )
            }//<

            //rettTilKlagePesys_001
            title1 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage", // TODO
                    English to "You have the right to appeal" // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klagen skal vera skriftleg. Du finn skjema og informasjon på nav.no/klage.", // TODO
                    English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more information about appeals at nav.no." // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                    Nynorsk to "I vedlegget får du vite meir om korleis du går fram.", // TODO
                    English to "The appendix includes information on how to proceed." // TODO
                )
            }//<

            //rettTilInnsynPesys_001
            title1 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn", // TODO
                    English to "You have the right to access your file" // TODO
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.", // TODO
                    English to "You have the right to access all documents pertaining to your case. The appendix includes information on how to proceed." // TODO
                )
            }//<

            //sjekkUtbetalingeneOverskrift_001
            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine",
                    Nynorsk to "Sjekk utbetalingane dine", // TODO
                    English to "Information about your payments" // TODO
                )
            }
            //sjekkUtbetalingeneUT_001
            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt.",
                    Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. Du kan sjå alle utbetalingar du har fått på nav.no/dittnav. Her kan du også endre kontonummeret ditt.", // TODO
                    English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. To see all the payments you have received, go to: nav.no/ dittnav. You may also change your account number here." // TODO
                )
            }


            //skattekortOverskrift_001><
            title1 {
                text(
                    Bokmal to "Skattekort",
                    Nynorsk to "Skattekort", // TODO
                    English to "Tax card" // TODO
                )
            }
            //skattekortUT_001
            paragraph {
                text(
                    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Skattekortet kan du endre på skatteetaten.no. På nettjenesten Ditt NAV på nav.no kan du se hvilket skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønsker det.",
                    Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Skattekortet kan du endre på skatteetaten.no. På nettenesten Ditt NAV på nav.no kan du sjå kva skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønskjer det.", // TODO
                    English to "The tax rules for disability benefit are the same as the tax rules for regular income. You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. You may change your tax card at skatteetaten.no. At the online service “Ditt NAV” at nav.no, you may see your registered income tax rate and change it if you wish." // TODO
                )
            }

            showIf(not(bor_i_norge)) {
                //skattBorIUtlandPesys_001>
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet",
                        Nynorsk to "Skatt for deg som bur i utlandet", // TODO
                        English to "Tax for people who live abroad" // TODO
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.", // TODO
                        English to "You can find more information about withholding tax to Norway at skatteetaten.no. For information about taxation from your country of residence, you can contact the locale tax authorities." // TODO
                    )
                }//<
            }
        }
    }
}