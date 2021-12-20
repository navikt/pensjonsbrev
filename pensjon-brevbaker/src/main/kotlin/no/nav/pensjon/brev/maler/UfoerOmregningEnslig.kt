package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object UfoerOmregningEnslig : StaticTemplate {
    override val template = createTemplate(
        name = "UT_DOD_ENSLIG_AUTO",
        base = PensjonLatex,
        letterDataType = UfoerOmregningEnsligDto::class,
        lang = languages(Bokmal),
        title = newText(
            Bokmal to "NAV har regnet om uføretrygden din",
        ),
        letterMetadata = LetterMetadata(
            "Vedtak – omregning til enslig uføretrygdet (automatisk)",
            isSensitiv = true
        )
    ) {

        val BARNETILLEGG_ER_REDUSERT_MOT_TAK =
            argument().select(UfoerOmregningEnsligDto::BARNETILLEGG_ER_REDUSERT_MOT_TAK)
        val BARNETILLEGG_IKKE_UTBETALT_PGA_TAK =
            argument().select(UfoerOmregningEnsligDto::BARNETILLEGG_IKKE_UTBETALT_PGA_TAK)
        val BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK =
            argument().select(UfoerOmregningEnsligDto::BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK)
        val BARN_OVERFOERT_TIL_SAERKULLSBARN =
            argument().select(UfoerOmregningEnsligDto::BARN_OVERFOERT_TIL_SAERKULLSBARN)
        val BOR_I_AVTALELAND = argument().select(UfoerOmregningEnsligDto::BOR_I_AVTALELAND)
        val BOR_I_NORGE = argument().select(UfoerOmregningEnsligDto::BOR_I_NORGE)
        val BT_SB_INNTEKTSJUSTERT = argument().select(UfoerOmregningEnsligDto::BT_SB_INNTEKTSJUSTERT)
        val EKTEFELLETILLEGG_OPPHOERT = argument().select(UfoerOmregningEnsligDto::EKTEFELLETILLEGG_OPPHOERT)
        val ER_EPS_IKKE_PARAGRAF_3_2_SAMBOER =
            argument().select(UfoerOmregningEnsligDto::ER_EPS_IKKE_PARAGRAF_3_2_SAMBOER)
        val ER_PAA_HELSEINSTITUSJON = argument().select(UfoerOmregningEnsligDto::ER_PAA_HELSEINSTITUSJON)
        val ER_PARAGRAF_3_2_SAMBOER = argument().select(UfoerOmregningEnsligDto::ER_PARAGRAF_3_2_SAMBOER)
        val GJELDENDE_BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT =
            argument().select(UfoerOmregningEnsligDto::GJELDENDE_BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT)
        val GJELDENDE_UFOERETRYGD_PER_MAANED_ER_INNTEKTSAVKORTET =
            argument().select(UfoerOmregningEnsligDto::GJELDENDE_UFOERETRYGD_PER_MAANED_ER_INNTEKTSAVKORTET)
        val HAR_BARNETILLEGG_FOR_SAERKULLSBARN =
            argument().select(UfoerOmregningEnsligDto::HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
        val HAR_BARNETILLEGG_SAERKULLSBARN_JUSTERINGSBELOEP_AR_VEDVIRK =
            argument().select(UfoerOmregningEnsligDto::HAR_BARNETILLEGG_SAERKULLSBARN_JUSTERINGSBELOEP_AR_VEDVIRK)
        val HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK =
            argument().select(UfoerOmregningEnsligDto::HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK)
        val HAR_BARNETILLEGG_VEDVIRK = argument().select(UfoerOmregningEnsligDto::HAR_BARNETILLEGG_VEDVIRK)
        val HAR_BARN_SOM_TIDLIGERE_VAR_SAERKULLSBARN =
            argument().select(UfoerOmregningEnsligDto::HAR_BARN_SOM_TIDLIGERE_VAR_SAERKULLSBARN)
        val HAR_FELLES_BARN_UTEN_BARNETILLEGG_MED_AVDOD =
            argument().select(UfoerOmregningEnsligDto::HAR_FELLES_BARN_UTEN_BARNETILLEGG_MED_AVDOD)
        val HAR_FLERE_DELYTELSER_I_TILLEGG_TIL_ORDINAER_UFOERETRYGD =
            argument().select(UfoerOmregningEnsligDto::HAR_FLERE_DELYTELSER_I_TILLEGG_TIL_ORDINAER_UFOERETRYGD)
        val HAR_FLERE_UFOERETRYGD_PERIODER = argument().select(UfoerOmregningEnsligDto::HAR_FLERE_UFOERETRYGD_PERIODER)
        val HAR_MINSTEYTELSE_VEDVIRK = argument().select(UfoerOmregningEnsligDto::HAR_MINSTEYTELSE_VEDVIRK)
        val HAR_MINSTEYT_VEDVIRK = argument().select(UfoerOmregningEnsligDto::HAR_MINSTEYT_VEDVIRK)
        val HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD =
            argument().select(UfoerOmregningEnsligDto::HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD)
        val HAR_UFOEREMAANED = argument().select(UfoerOmregningEnsligDto::HAR_UFOEREMAANED)
        val INNTEKT_UFOERE_ENDRET = argument().select(UfoerOmregningEnsligDto::INNTEKT_UFOERE_ENDRET)
        val I_FENGSEL_VED_VIRK = argument().select(UfoerOmregningEnsligDto::I_FENGSEL_VED_VIRK)
        val UFOERETRYGD_MED_BARNETILLEGG_ER_OVER_95_PROSENT_AV_INNTEKT_FOER_UFOERHET =
            argument().select(UfoerOmregningEnsligDto::UFOERETRYGD_MED_BARNETILLEGG_ER_OVER_95_PROSENT_AV_INNTEKT_FOER_UFOERHET)
        val UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET =
            argument().select(UfoerOmregningEnsligDto::UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET)


        outline {
            title1 { text(Bokmal to "Vedtak") }
            showIf(
                (HAR_MINSTEYTELSE_VEDVIRK
                        or INNTEKT_UFOERE_ENDRET
                        or EKTEFELLETILLEGG_OPPHOERT)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
            ) {
                // omregnUTDodEPSInnledn1_001
                paragraph {
                    text(Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om uføretrygden din fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.")
                }
            }


            showIf(
                not(HAR_MINSTEYTELSE_VEDVIRK)
                        and not(INNTEKT_UFOERE_ENDRET)
                        and not(EKTEFELLETILLEGG_OPPHOERT)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
            ) {
                // omregnUTDodEPSInnledn2_001
                paragraph {
                    text(Bokmal to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygden din endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.")
                }
            }

            showIf(
                (HAR_MINSTEYTELSE_VEDVIRK
                        or INNTEKT_UFOERE_ENDRET
                        or EKTEFELLETILLEGG_OPPHOERT)
                        and HAR_BARNETILLEGG_FOR_SAERKULLSBARN
            ) {
                //omregnUTBTDodEPSInnledn_001
                paragraph {
                    text(Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om uføretrygden og barnetillegget ditt fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.")
                }
            }

            showIf(
                not(HAR_MINSTEYT_VEDVIRK)
                        and not(INNTEKT_UFOERE_ENDRET)
                        and not(EKTEFELLETILLEGG_OPPHOERT)
                        and HAR_BARNETILLEGG_FOR_SAERKULLSBARN
                        and not(BARN_OVERFOERT_TIL_SAERKULLSBARN)
            ) {
                //omregnUTBTSBDodEPSInnledn_001
                paragraph {
                    text(Bokmal to "Vi har mottatt melding om at <avdod.navn> er død. Uføretrygden og barnetillegget ditt endres ikke, men vi vil informere deg om rettigheter du kan ha etter avdøde.")
                }
            }

            showIf(
                not(HAR_MINSTEYT_VEDVIRK)
                        and not(INNTEKT_UFOERE_ENDRET)
                        and not(EKTEFELLETILLEGG_OPPHOERT)
                        and BARN_OVERFOERT_TIL_SAERKULLSBARN
            ) {
                // omregnBTDodEPSInnledn_001
                paragraph {
                    text(Bokmal to "Vi har mottatt melding om at <avdod.navn> er død, og vi har regnet om barnetillegget ditt fra <krav.virkDatoFom> fordi sivilstanden din er endret. Vi vil også informere deg om rettigheter du kan ha etter avdøde.")
                }
            }

            showIf(
                HAR_UFOEREMAANED
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and not(HAR_FLERE_UFOERETRYGD_PERIODER)
            ) {
                //belopUT_001
                paragraph {
                    text(Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt.")
                }
            }

            showIf(
                HAR_UFOEREMAANED
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and HAR_FLERE_UFOERETRYGD_PERIODER
            ) {
                //belopUTVedlegg_001
                paragraph {
                    text(Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }

            showIf(
                HAR_UFOEREMAANED
                        and HAR_BARNETILLEGG_FOR_SAERKULLSBARN
                        and not(HAR_FLERE_UFOERETRYGD_PERIODER)
            ) {
                //belopUTBT_001
                paragraph {
                    text(Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }

            showIf(
                HAR_UFOEREMAANED
                        and HAR_BARNETILLEGG_FOR_SAERKULLSBARN
                        and HAR_FLERE_UFOERETRYGD_PERIODER
            ) {
                // belopUTBTVedlegg_001>
                paragraph {
                    text(Bokmal to "Du får <uforetrygdVedVirk.totalUforeMnd> kroner i uføretrygd og barnetillegg per måned før skatt. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and not(HAR_FLERE_UFOERETRYGD_PERIODER)
                        and not(I_FENGSEL_VED_VIRK)
            ) {
                // belopUTIngenUtbetaling_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and HAR_FLERE_UFOERETRYGD_PERIODER
                        and not(I_FENGSEL_VED_VIRK)
            ) {
                // belopUTIngenUtbetalingVedlegg_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK
                        and not(HAR_FLERE_UFOERETRYGD_PERIODER)
            ) {
                // belopUTBTIngenUtbetaling_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK
                        and HAR_FLERE_UFOERETRYGD_PERIODER
            ) {
                //belopUTBTIngenUtbetalingVedlegg_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd og barnetillegg på grunn av høy inntekt. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and not(HAR_FLERE_UFOERETRYGD_PERIODER)
                        and I_FENGSEL_VED_VIRK
            ) {
                // belopUTIngenUtbetalingFengsel_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring.")
                }
            }

            showIf(
                not(HAR_UFOEREMAANED)
                        and not(HAR_BARNETILLEGG_FOR_SAERKULLSBARN)
                        and HAR_FLERE_UFOERETRYGD_PERIODER
                        and I_FENGSEL_VED_VIRK
            ) {
                // belopUTIngenUtbetalingFengselVedlegg_001
                paragraph {
                    text(Bokmal to "Du får ikke utbetalt uføretrygd fordi du er under straffegjennomføring. Du kan lese mer om andre beregningsperioder i vedlegget.")
                }
            }


            //begrunnOverskrift_001
            //HVIS
            //HAR_MINSTEYTELSE_VEDVIRK
            //ELLER
            //INNTEKT_UFOERE_ENDRET

            title1 {
                text(Bokmal to "Begrunnelse for vedtaket")
            }

            //endrMYDodEPS2_001
            //HAR_MINSTEYTELSE_VEDVIRK
            //OG
            //!INNTEKT_UFOERE_ENDRET
            paragraph {
                text(Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen <minsteytelseVedVirk.sats> ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.")
            }
            paragraph {
                text(Bokmal to "Dette kan ha betydning for kompensasjonsgraden din som er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.")
            }//<


            showIf(HAR_MINSTEYTELSE_VEDVIRK and INNTEKT_UFOERE_ENDRET) {
                //endrMYOgMinstIFUDodEPS2_001
                paragraph {
                    text(Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. For deg utgjør minsteytelsen <minsteytelseVedVirk.sats> ganger folketrygdens grunnbeløp. Du kan lese mer om grunnbeløp på nav.no.")
                }
                paragraph {
                    text(Bokmal to "Sivilstandsendring har også betydning for inntekten din før du ble ufør. Denne utgjør <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Kompensasjonsgraden din er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.")
                }
            }


            showIf(not(HAR_MINSTEYTELSE_VEDVIRK) and INNTEKT_UFOERE_ENDRET) {
                //endrMinstIFUDodEPS2_001
                paragraph {
                    text(Bokmal to "Inntekten din før du ble ufør er fastsatt til minstenivå som er avhengig av sivilstand. For deg er inntekten din før du ble ufør satt til <inntektForUforeVedVirk.ifuInntekt> kroner som oppjustert til virkningstidspunktet tilsvarer en inntekt på <inntektForUforeVedVirk.oppjustertIFU> kroner. Dette kan ha betydning for kompensasjonsgraden din som er satt til <uforetrygdVedVirk.kompensasjonsgrad> prosent. Du kan lese mer om dette i vedlegget.")
                }
            }


            showIf(
                not(HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD)
                        and not(UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET)
            ) {
                // hjemmelSivilstandUT_001
                paragraph {
                    text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13 og 22-12.")
                }
            }

            showIf(
                HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD
                        and not(UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET)
            ) {
                //hjemmelSivilstandUTMinsteIFU_001
                paragraph {
                    text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13 og 22-12.")
                }
            }

            showIf(
                not(HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD)
                        and UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET
            ) {
                //hjemmelSivilstandUTAvkortet_001>
                paragraph {
                    text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-13, 12-14 og 22-12.")
                }
            }

            showIf(
                HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD
                        and UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET
            ) {
                //hjemmelSivilstandUTMinsteIFUAvkortet_001
                paragraph {
                    text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-9, 12-13, 12-14 og 22-12.")
                }
            }

            showIf(ER_PAA_HELSEINSTITUSJON) {
                //hjemmelEPSDodUTInstitusjon_001
                paragraph {
                    text(Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-19 så lenge du er på institusjon.")
                }
            }

            showIf(I_FENGSEL_VED_VIRK) {
                //hjemmelEPSDodUTFengsel_001
                paragraph {
                    text(Bokmal to "Uføretrygden din beregnes etter folketrygdloven § 12-20 så lenge du er under straffegjennomføring.")
                }
            }


            //EKTEFELLETILLEGG_OPPHOERT
            showIf(EKTEFELLETILLEGG_OPPHOERT) {
                //opphorETOverskrift_001
                title1 {
                    text(Bokmal to "Ektefelletillegget ditt opphører")
                }


                //opphorET_001
                paragraph {
                    text(Bokmal to "Du forsørger ikke lenger en ektefellepartnersamboer. Derfor opphører ektefelletillegget ditt.")
                }


                //hjemmelET_001
                paragraph {
                    text(Bokmal to "Vedtaket er gjort etter forskrift om omregning av uførepensjon til uføretrygd § 8.")
                }
            }



            showIf(BARN_OVERFOERT_TIL_SAERKULLSBARN) {
                //omregningFBOverskrift_001
                title1 {
                    text(Bokmal to "Barnetillegg for fellesbarn er regnet om")
                }

                //infoFBTilSB_001>
                //<navn><
                paragraph {
                    text(Bokmal to "Vi har regnet om barnetillegget for barn som ikke lenger bor sammen med begge foreldre. Dette gjelder:")
                    //TODO vis liste med barnOverfortTilSB.enkeltbarnListe
                }

                showIf(
                    BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK
                            and HAR_BARN_SOM_TIDLIGERE_VAR_SAERKULLSBARN
                            and not(INNTEKT_UFOERE_ENDRET and HAR_MINSTEYTELSE_VEDVIRK)
                ) {
                    //infoTidligereSB_001>
                    //<navn><
                    paragraph {
                        text(Bokmal to "Denne omregningen har også betydning for barnetillegget for:")
                        //TODO vis liste med barnTidligereSB.enkeltbarnListe
                    }
                }

                showIf(
                    BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK
                            and (INNTEKT_UFOERE_ENDRET or HAR_MINSTEYTELSE_VEDVIRK)
                            and (BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK or BARNETILLEGG_ER_REDUSERT_MOT_TAK)
                ) {
                    //infoTidligereSBOgEndretUT_001>
                    paragraph {
                        text(Bokmal to "Omregningen av barnetillegg og endring i uføretrygden din har også betydning for barnetillegget for:")
                        //TODO vis liste med barnTidligereSB.enkeltbarnListe
                    }
                }
            }


            //endringUTpavirkerBTOverskrift_001
            title1 {
                text(Bokmal to "Slik påvirker endring av uføretrygden barnetillegget ditt")
            }
            //ikkeRedusBTPgaTak_001
            paragraph {
                text(Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen lavere enn dette. Derfor er barnetillegget fastsatt til 40 prosent av folketrygdens grunnbeløp for hvert barn.")
            }
            //redusBTPgaTak_001
            paragraph {
                text(Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor er barnetillegget redusert fra <barnetilleggGrunnlagVedVirk.belopForReduksjon> kroner til <barnetilleggGrunnlagVedVirk.belopEtterReduksjon> kroner.")
            }
            //ikkeUtbetaltBTPgaTak_001
            paragraph {
                text(Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av inntekten din før du ble ufør. <barnetilleggGrunnlagVedVirk.prosentsatsGradertOIFU> prosent av den inntekten du hadde før du ble ufør tilsvarer i dag <barnetilleggGrunnlagVedVirk.gradertOIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Derfor får du ikke utbetalt barnetillegg.")
            }
            //infoBTSBInntekt_001
            paragraph {
                text(Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Du kan enkelt melde fra om inntektsendringer på nav.no.")
            }
            //infoBTOverfortTilSBInntekt_001
            paragraph {
                text(Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Fribeløpet for ett barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. Du kan enkelt melde fra om inntektsendringer på nav.no.")
            }
            //ikkeRedusBTSBPgaInntekt_001
            paragraph {
                text(Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er lavere enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget ikke redusert ut fra inntekt.")
            }
            //redusBTSBPgaInntekt_001
            paragraph {
                text(Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er høyere enn fribeløpet ditt på <barnetilleggSBVedVirk.fribelop> kroner. Derfor er barnetillegget redusert ut fra inntekt.")
            }
            //justerBelopRedusBTPgaInntekt_001
            paragraph {
                text(Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.")
            }
            //justerBelopIkkeUtbetaltBTPgaInntekt_001
            paragraph {
                text(Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.")
            }
            //ikkeUtbetaltBTSBPgaInntekt_001
            paragraph {
                text(Bokmal to "Inntekten din på <barnetilleggSBVedVirk.inntektBruktIAvkortning> kroner er over <barnetilleggSBVedVirk.inntektstak> kroner som er grensen for å få utbetalt barnetillegg. Derfor får du ikke utbetalt barnetillegg.")
            }
            //hjemmelBT_001
            paragraph {
                text(Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15.")
            }
            //hjemmelBTOvergangsregler_001
            paragraph {
                text(Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-15 og forskrift om overgangsregler for barnetillegg i uføretrygden.")
            }
            //hjemmelBTRedus_001
            paragraph {
                text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16.")
            }
            //hjemmelBTRedusOvergangsregler_001
            paragraph {
                text(Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15 og 12-16 og forskrift om overgangsregler for barnetillegg i uføretrygden.")
            }
            //merInfoBT_001
            paragraph {
                text(Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget.")
            }


            showIf(ER_PARAGRAF_3_2_SAMBOER) {
                // gjRettSamboerOverskrift
                title1 {
                    text(Bokmal to "Rettigheter du kan ha som tidligere samboer med <avdod.navn>")
                }
                // gjRettUTSamboer_001
                paragraph {
                    text(Bokmal to "Samboere som tidligere har vært gift, eller som har eller har hatt felles barn, kan ha rett til gjenlevendetillegg i uføretrygden. Du finner mer informasjon og søknadsskjema for gjenlevende ektefelle, partner eller samboer på nav.no.")
                }
            }
            showIf(ER_EPS_IKKE_PARAGRAF_3_2_SAMBOER) {
                // rettTilUTGJTOverskrift_001
                title1 {
                    text(Bokmal to "Du kan ha rett til gjenlevendetillegg i uføretrygden")
                }

                //TODO point list support
                //hvemUTGJTVilkar_001>For å ha rett til gjenlevendetillegg i uføretrygden, må du som hovedregel:
                //•	være medlem i folketrygden, og avdøde må ha vært medlem i folketrygden de siste fem årene fram til dødsfallet
                //•	ha vært gift med den avdøde i minst fem år, eller
                //•	ha vært gift eller vært samboer med den avdøde og har eller ha hatt barn med den avdøde, eller
                //•	ha hatt omsorgen for den avdødes barn på dødsfallstidspunktet. Ekteskapet og omsorgen for barnet etter dødsfallet må til sammen ha vart minst fem år.
                //Selv om du ikke har rett til ytelsen etter hovedreglene, kan du likevel ha rettigheter etter avdøde. Du kan lese mer om dette på nav.no.<

                //hvordanSøkerDuOverskrift_001
                title1 {
                    text(Bokmal to "Hvordan søker du?")
                }
                //søkUTGJT_001
                paragraph {
                    text(Bokmal to "Vi oppfordrer deg til å søke om gjenlevendetillegg i uføretrygden så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. Du finner informasjon og søknadsskjemaet for gjenlevende ektefelle, partner eller samboer på nav.no/gjenlevendeektefelle")
                }
                //søkAvtaleLandUT_001
                paragraph {
                    text(Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheter i bostedslandet ditt og søke om ytelser etter avdøde.")
                }
                //avdodBoddArbUtlandOverskrift_001
                title1 {
                    text(Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet")
                }
                //avdodBoddArbUtland2_001
                paragraph {
                    text(Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får ubetalt i gjenlevendetillegg. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.")
                }
                //pensjonFraAndreOverskrift_001
                paragraph {
                    text(Bokmal to "Andre pensjonsordninger")
                }
                //infoAvdodPenFraAndre_001
                paragraph {
                    text(Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, må du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.")
                }
            }


            //harBarnUnder18Overskrift_001
            title1 {
                text(Bokmal to "For deg som har barn under 18 år")
            }
            //harBarnUtenBT_001
            paragraph {
                text(Bokmal to "Har du barn under 18 år, og ikke er innvilget barnetillegg, kan du søke om dette.")
            }
            //harBarnUnder18_001
            paragraph {
                text(Bokmal to "Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadsskjema og mer informasjon om dette på nav.no.")
            }

            //virknTdsPktOverskrift_001
            title1 {
                text(Bokmal to "Dette er virkningstidspunktet ditt")
            }
            //virkTdsPktUT_001
            paragraph {
                text(Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>.")
            }
            //virkTdsPktUTIkkeEndring_001
            paragraph {
                text(Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>, men dette fører ikke til endring i utbetalingen.")
            }
            //virkTdsPktUTBTOmregn_001
            paragraph {
                text(Bokmal to "Barnetillegget i uføretrygden din er omregnet fra <krav.virkDatoFom>.")
            }
            //virkTdsPktUTAvkortetTil0_001
            paragraph {
                text(Bokmal to "Uføretrygden din er omregnet fra <krav.virkDatoFom>, men dette fører ikke til endring i utbetalingen da uføretrygden er redusert til 0 kr.")
            }

            //meldInntektUTOverskrift_001
            title1 {
                text(Bokmal to "Du må melde fra om eventuell inntekt")
            }

            //meldInntektUT_001
            paragraph {
                text(Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd.")
            }

            //meldInntektUTBT_001
            paragraph {
                text(Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd og barnetillegg. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd og barnetillegg.")
            }

            //meldEndringerPesys_001
            title1 {
                text(Bokmal to "Du må melde fra om endringer")
            }
            paragraph {
                text(Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om.")
            }
            paragraph {
                text(Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV.")
            }//<
            //rettTilKlagePesys_001
            title1 {
                text(Bokmal to "Du har rett til å klage")
            }
            paragraph {
                text(Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.")
            }
            paragraph {
                text(Bokmal to "I vedlegget får du vite mer om hvordan du går fram.")
            }//<

            //rettTilInnsynPesys_001
            title1 {
                text(Bokmal to "Du har rett til innsyn")
            }
            paragraph {
                text(Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.")
            }//<

            //sjekkUtbetalingeneOverskrift_001
            title1 {
                text(Bokmal to "Sjekk utbetalingene dine")
            }
            //sjekkUtbetalingeneUT_001
            paragraph {
                text(Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på nav.no/dittnav. Her kan du også endre kontonummeret ditt.")
            }


            //skattekortOverskrift_001><
            title1 {
                text(Bokmal to "Skattekort")
            }
            //skattekortUT_001
            paragraph {
                text(Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Skattekortet kan du endre på skatteetaten.no. På nettjenesten Ditt NAV på nav.no kan du se hvilket skattetrekk som er registrert hos NAV og legge inn tilleggstrekk om du ønsker det.")
            }

            //skattBorIUtlandPesys_001>
            title1 {
                text(Bokmal to "Skatt for deg som bor i utlandet")
            }
            paragraph {
                text(Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.")
            }//<
        }
    }
}


data class UfoerOmregningEnsligDto(
    val BARNETILLEGG_ER_REDUSERT_MOT_TAK: Boolean,
    val BARNETILLEGG_IKKE_UTBETALT_PGA_TAK: Boolean,
    val BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK: Boolean,
    val BARN_OVERFOERT_TIL_SAERKULLSBARN: Boolean,
    val BOR_I_AVTALELAND: Boolean,
    val BOR_I_NORGE: Boolean,
    val BT_SB_INNTEKTSJUSTERT: Boolean,
    val EKTEFELLETILLEGG_OPPHOERT: Boolean,
    val ER_EPS_IKKE_PARAGRAF_3_2_SAMBOER: Boolean,
    val ER_PAA_HELSEINSTITUSJON: Boolean,
    val ER_PARAGRAF_3_2_SAMBOER: Boolean,
    val GJELDENDE_BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT: Boolean,
    val GJELDENDE_UFOERETRYGD_PER_MAANED_ER_INNTEKTSAVKORTET: Boolean,
    val HAR_BARNETILLEGG_FOR_SAERKULLSBARN: Boolean,
    val HAR_BARNETILLEGG_SAERKULLSBARN_JUSTERINGSBELOEP_AR_VEDVIRK: Boolean,
    val HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK: Boolean,
    val HAR_BARNETILLEGG_VEDVIRK: Boolean,
    val HAR_BARN_SOM_TIDLIGERE_VAR_SAERKULLSBARN: Boolean,
    val HAR_FELLES_BARN_UTEN_BARNETILLEGG_MED_AVDOD: Boolean,
    val HAR_FLERE_DELYTELSER_I_TILLEGG_TIL_ORDINAER_UFOERETRYGD: Boolean,
    val HAR_FLERE_UFOERETRYGD_PERIODER: Boolean,
    val HAR_MINSTEYTELSE_VEDVIRK: Boolean,
    val HAR_MINSTEYT_VEDVIRK: Boolean,
    val HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD: Boolean,
    val HAR_UFOEREMAANED: Boolean,
    val INNTEKT_UFOERE_ENDRET: Boolean,
    val I_FENGSEL_VED_VIRK: Boolean,
    val UFOERETRYGD_MED_BARNETILLEGG_ER_OVER_95_PROSENT_AV_INNTEKT_FOER_UFOERHET: Boolean,
    val UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET: Boolean,
) {
    constructor() : this(
        BARNETILLEGG_ER_REDUSERT_MOT_TAK = false,
        BARNETILLEGG_IKKE_UTBETALT_PGA_TAK = false,
        BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT_VEDVIRK = false,
        BARN_OVERFOERT_TIL_SAERKULLSBARN = false,
        BOR_I_AVTALELAND = false,
        BOR_I_NORGE = false,
        BT_SB_INNTEKTSJUSTERT = false,
        EKTEFELLETILLEGG_OPPHOERT = false,
        ER_EPS_IKKE_PARAGRAF_3_2_SAMBOER = false,
        ER_PAA_HELSEINSTITUSJON = false,
        ER_PARAGRAF_3_2_SAMBOER = false,
        GJELDENDE_BARNETILLEGG_SAERKULLSBARN_ER_REDUSERT_MOT_INNTEKT = false,
        GJELDENDE_UFOERETRYGD_PER_MAANED_ER_INNTEKTSAVKORTET = false,
        HAR_BARNETILLEGG_FOR_SAERKULLSBARN = false,
        HAR_BARNETILLEGG_SAERKULLSBARN_JUSTERINGSBELOEP_AR_VEDVIRK = false,
        HAR_BARNETILLEGG_SAERKULLSBARN_NETTOBELOEP_VEDVIRK = false,
        HAR_BARNETILLEGG_VEDVIRK = false,
        HAR_BARN_SOM_TIDLIGERE_VAR_SAERKULLSBARN = false,
        HAR_FELLES_BARN_UTEN_BARNETILLEGG_MED_AVDOD = false,
        HAR_FLERE_DELYTELSER_I_TILLEGG_TIL_ORDINAER_UFOERETRYGD = false,
        HAR_FLERE_UFOERETRYGD_PERIODER = false,
        HAR_MINSTEYTELSE_VEDVIRK = false,
        HAR_MINSTEYT_VEDVIRK = false,
        HAR_MINSTE_INNTEKTSNIVAA_FOER_UFOERETRYGD = false,
        HAR_UFOEREMAANED = false,
        INNTEKT_UFOERE_ENDRET = false,
        I_FENGSEL_VED_VIRK = false,
        UFOERETRYGD_MED_BARNETILLEGG_ER_OVER_95_PROSENT_AV_INNTEKT_FOER_UFOERHET = false,
        UFOERETRYGD_VEDVIRK_ER_INNTEKTSAVKORTET = false,
    )
}
