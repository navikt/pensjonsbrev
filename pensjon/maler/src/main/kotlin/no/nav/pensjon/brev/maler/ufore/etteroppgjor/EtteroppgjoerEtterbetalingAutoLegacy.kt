package no.nav.pensjon.brev.maler.ufore.etteroppgjor

import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.selectors.etteroppgjoerEtterbetalingAutoDto.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.selectors.etteroppgjoerEtterbetalingAutoDto.pe
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.opplysningerOmETteroppgjoeretUTLegacy
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EtteroppgjoerEtterbetalingAutoLegacy : AutobrevTemplate<EtteroppgjoerEtterbetalingAutoDto> {

    // PE_UT_23_101
    override val kode = Pesysbrevkoder.AutoBrev.UT_ETTEROPPGJOER_ETTERBETALING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om etteroppgjør - etterbetaling",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {

        title {
            text(
                bokmal { + "Nav har gjort et " },
                nynorsk { + "Nav har gjort eit " },
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt_New = true     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'etterbet'            OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'tilbakekr'            )    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser_New = true            ) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt_new() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser_new()))) {
                text(
                    bokmal { + "nytt " },
                    nynorsk { + "nytt " },
                )
            }
            text(
                bokmal { + "etteroppgjør av uføretrygd for " },
                nynorsk { + "etteroppgjer av uføretrygd for " },
            )
            ifNotNull(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()){
                text(
                    bokmal { + it.year.format() },
                    nynorsk { + it.year.format() }
                )
            }
        }

        outline {
            paragraph {
                text (
                    bokmal { + "Vi har mottatt " },
                    nynorsk { + "Vi har fått " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt_New = true     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'etterbet'            OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'tilbakekr'            )    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser_New = true            ) ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt_new() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser_new()))){
                    text (
                        bokmal { + "nye " },
                        nynorsk { + "nye " },
                    )
                }
                text (
                    bokmal { + "inntektsopplysninger fra Skatteetaten. Vi har ut fra disse opplysningene vurdert hva du skulle hatt i uføretrygd " },
                    nynorsk { + "inntektsopplysningar frå Skatteetaten. Vi har ut frå desse opplysningane vurdert kva du skulle hatt i uføretrygd " },
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
                    text (
                        bokmal { + "og barnetillegg " },
                        nynorsk { + "og barnetillegg " },
                    )
                }
                text (
                    bokmal { + "i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Resultatet av etteroppgjøret viser at du har fått " + pe.ut_avviksbeloputenminus().format() + " for lite i " },
                    nynorsk { + "i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Resultatet av etteroppgjeret viser at du har fått " + pe.ut_avviksbeloputenminus().format() + " for lite i " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                    text (
                        bokmal { + "uføretrygd" },
                        nynorsk { + "uføretrygd" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                    text (
                        bokmal { + " og " },
                        nynorsk { + " og " },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                    text (
                        bokmal { + "barnetillegg" },
                        nynorsk { + "barnetillegg" },
                    )
                }
                text (
                    bokmal { + ". Dette beløpet får du normalt utbetalt innen 7 dager." },
                    nynorsk { + ". Dette beløpet får du normalt utbetalt innan sju dagar." },
                )
            }

            title1 {
                text (
                    bokmal { + "Begrunnelse for vedtaket" },
                    nynorsk { + "Grunngiving for vedtaket" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Hvert år når skatteoppgjøret er klart gjør vi et etteroppgjør av uføretrygden. Da kontrollerer vi om utbetalingen for året er riktig." },
                    nynorsk { + "Kvart år når skatteoppgjeret er klart, gjer vi eit etteroppgjer av uføretrygda. Etteroppgjeret kontrollerer om du fekk rett utbetalt i fjor. Då kontrollerer vi om utbetalinga for året er riktig." },
                )
            }

            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)      ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                includePhrase(TBU4018_Generated(pe))
            }

            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                paragraph {
                    text (
                        bokmal { + "Vi gjør først en vurdering av om du har fått for mye eller for lite i uføretrygd. Uføretrygden regnes med som personinntekt, og har derfor betydning for om du har fått for mye eller for lite i barnetillegg. " },
                        nynorsk { + "Vi vurderer først om du har fått for mykje eller for lite i uføretrygd. Uføretrygda blir rekna med som personinntekt og har derfor betydning for om du har fått for mykje eller for lite i barnetillegg. " },
                    )

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
                        text (
                            bokmal { + "Fordi du mottok barnetillegg både for barn som bodde sammen med begge sine foreldre og for barn som ikke bodde sammen med begge foreldrene, har vi for begge barnetilleggene vurdert om du har fått for mye eller for lite. " },
                            nynorsk { + "Fordi du fekk barnetillegg både for barn som budde saman med begge foreldra sine, og for barn som ikkje budde saman med begge foreldra, har vi for begge barnetillegga vurdert om du har fått for mykje eller for lite. " },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT  <> 0  AND (PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeTom < PE_UT_lastDay OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom > PE_UT_firstDay))  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyLessThan(pe.ut_lastday()) or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyGreaterThan(pe.ut_firstday())))){
                paragraph {
                    text (
                        bokmal { + "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på " + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                        nynorsk { + "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på " + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                    )

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeFomStorre0101PeriodeTomLik3112() = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodefomstorre0101periodetomlik3112())){
                        text (
                            bokmal { + "Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. " },
                            nynorsk { + "Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeTomMindre3112PeriodeFomLik0101() = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomlik0101())){
                        text (
                            bokmal { + "Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. " },
                            nynorsk { + "Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeTomMindre3112PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomstorre0101())){
                        text (
                            bokmal { + "Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. " },
                            nynorsk { + "Inntekt du hadde før du fekk innvilga uføretrygd, og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj())){
                        text (
                            bokmal { + "Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. " },
                            nynorsk { + "Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "etterslepsinnt_avslt_akt") > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt())){
                        text (
                            bokmal { + "Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. " },
                            nynorsk { + "Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(PE_UT_EtteroppgjorFratrekkListeBrukerEtterbetaling("etterbetaling") = true) THEN      INCLUDE ENDIF
                    showIf((pe.ut_etteroppgjorfratrekklistebrukeretterbetaling())){
                        text (
                            bokmal { + "Etterbetaling du har fått fra Nav trekkes fra dette beløpet. " },
                            nynorsk { + "Etterbetalinga du fekk frå Nav blir trekt frå dette beløpet. " },
                        )
                    }
                    text (
                        bokmal { + "Vi har derfor brukt " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_inntektut().format() + " i vurderingen av om du har fått for mye eller for lite i uføretrygd." },
                        nynorsk { + "Vi har derfor brukt " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_inntektut().format() + " når vi har vurdert om du har fått for mykje eller for lite i uføretrygd." },
                    )
                }
                includePhrase(TBU4020_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 AND PE_UT_PeriodeFomMindreLik0101() = true AND PE_UT_PeriodeTomStorreLik3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and pe.ut_periodefommindrelik0101() and pe.ut_periodetomstorrelik3112())){
                paragraph {
                    text (
                        bokmal { + "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på " + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + ". " },
                        nynorsk { + "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på " + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + ". " },
                    )

                    //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj())){
                        text (
                            bokmal { + "Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. " },
                            nynorsk { + "Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "etterslepsinnt_avslt_akt") > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt())){
                        text (
                            bokmal { + "Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. " },
                            nynorsk { + "Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. " },
                        )
                    }

                    //IF(PE_UT_EtteroppgjorFratrekkListeBrukerEtterbetaling("etterbetaling") = true) THEN      INCLUDE ENDIF
                    showIf((pe.ut_etteroppgjorfratrekklistebrukeretterbetaling())){
                        text (
                            bokmal { + "Etterbetaling du har fått fra Nav trekkes fra dette beløpet. " },
                            nynorsk { + "Etterbetalinga du fekk frå Nav blir trekt frå dette beløpet. " },
                        )
                    }
                    text (
                        bokmal { + "Disse opplysningene viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + ". Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " for " },
                        nynorsk { + "Desse opplysningane viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + ". Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " for " },
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT < 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().lessThan(0))){
                        text (
                            bokmal { + "mye" },
                            nynorsk { + "mykje" },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().greaterThan(0))){
                        text (
                            bokmal { + "lite" },
                            nynorsk { + "lite" },
                        )
                    }
                    text (
                        bokmal { + " i uføretrygd. " },
                        nynorsk { + " i uføretrygd. " },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                includePhrase(TBU4029_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))){
                includePhrase(TBU4030_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT = 0    AND    ((PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true        AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )     OR (PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true             AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0            ))   )  AND PE_UT_TBU4050() = false  AND PE_UT_TBU4051() = false  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().equalTo(0) and ((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) or (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0)))) and not(pe.ut_tbu4050()) and not(pe.ut_tbu4051())){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din har vært riktig beregnet ut fra inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                        nynorsk { + "Uføretrygda di har vore rett berekna ut frå inntekta di i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    ((PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true        AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB = 0    )     OR (PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true             AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB = 0            ))   )   AND PE_UT_TBU4050() = false  AND PE_UT_TBU4051() = false THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and ((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0)) or (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0)))) and not(pe.ut_tbu4050()) and not(pe.ut_tbu4051())){
                includePhrase(TBU4025_Generated(pe))
            }

            //IF(PE_UT_TBU4050() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_tbu4050())){
                includePhrase(TBU4050_Generated(pe))
            }

            //IF(PE_UT_TBU4051() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_tbu4051())){
                includePhrase(TBU4051_Generated(pe))
            }

            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0       AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0            OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ))     OR  (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0            AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 )     ) THEN 		INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) or (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                paragraph {
                    text (
                        bokmal { + "Til sammen har du fått " + pe.ut_avviksbeloputenminus().format() + " for lite i " },
                        nynorsk { + "Til saman har du fått " + pe.ut_avviksbeloputenminus().format() + " for lite i " },
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                        text (
                            bokmal { + "uføretrygd " },
                            nynorsk { + "uføretrygd " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)) THEN 		INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                        text (
                            bokmal { + "og " },
                            nynorsk { + "og " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN 		INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                        text (
                            bokmal { + "barnetillegg " },
                            nynorsk { + "barnetillegg " },
                        )
                    }
                    text (
                        bokmal { + "i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                        nynorsk { + "i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Du kan lese mer om etteroppgjør i vedlegget " },
                    nynorsk { + "Du kan lese meir om etteroppgjer i vedlegget " },
                )
                namedReference(opplysningerOmETteroppgjoeretUTLegacy)
                text(bokmal { + "." }, nynorsk { + "." })
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 12-14." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 12-14." },
                    )
                }
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true ) )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 12-16. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 12-16. " },
                    )
                }
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 12-18. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 12-18. " },
                    )
                }
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true ) )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-16 og 12-18. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-16 og 12-18. " },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt = true       AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = "etterbet"               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = "tilbakekr"              )     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser = true             )     ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser()))){
                title1 {
                    text(
                        bokmal { +"For deg som har fått flere vedtak om etteroppgjør for samme år" },
                        nynorsk { +"For deg som har fått fleire vedtak om etteroppgjer for same år" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har mottatt nye inntektsopplysninger fra Skatteetaten for året " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + " og vi har derfor gjort et nytt etteroppgjør. Ditt nye etteroppgjør erstatter ikke tidligere etteroppgjør for samme år. Det betyr at alle vedtak om etteroppgjør er gjeldende. " },
                        nynorsk { + "Vi har fått nye inntektsopplysningar frå Skatteetaten for året " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + " og vi har derfor gjort eit nytt etteroppgjer. Det nye etteroppgjeret ditt erstattar ikkje tidlegare etteroppgjer for same år. Det betyr at alle vedtak om etteroppgjer gjeld. " },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Etterbetaling av beløpet" },
                    nynorsk { + "Etterbetaling av beløpet" },
                )
            }
            paragraph {
                text (
                    bokmal { + "I etterbetalingen trekker vi skatt etter standardsatser fra Skatteetaten. På utbetalingsmeldingen ser du hvilken skattesats som er brukt. Nav kan redusere etterbetalingen din dersom du har:" },
                    nynorsk { + "I etterbetalinga trekkjer vi skatt etter standardsatsar frå Skatteetaten. På utbetalingsmeldinga ser du kva skattesats som er brukt. Nav kan redusere etterbetalinga di dersom du har:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "Feilutbetalingsgjeld hos Nav" },
                            nynorsk { + "feilutbetalingsgjeld hos Nav" },
                        )
                    }

                    item {
                        text(
                            bokmal { + "Ettergitt feilutbetaling av uføretrygd " },
                            nynorsk { + "ettergitt feilutbetaling av uføretrygd " },
                        )

                        //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())) {
                            text(
                                bokmal { + "og barnetillegg " },
                                nynorsk { + "og barnetillegg " },
                            )
                        }
                        text(
                            bokmal { + "for samme år" },
                            nynorsk { + "for same år" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Bidragsgjeld hos Nav" },
                            nynorsk { + "bidragsgjeld hos Nav" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Utbetaling fra en tjenestepensjonsordning som krever refusjon" },
                            nynorsk { + "utbetaling frå ei tenestepensjonsordning som krev refusjon" },
                        )
                    }
                }
                text (
                    bokmal { + "Refusjon fra en tjenestepensjonsordning ser du på utbetalingsmeldingen." },
                    nynorsk { + "Refusjon frå ei tenestepensjonsordning ser du på utbetalingsmeldinga." },
                )
            }

            title1 {
                text (
                    bokmal { + "Du må melde fra om endringer i inntekten" },
                    nynorsk { + "Du må melde frå om endringar i inntekta" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden " },
                    nynorsk { + "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                    text (
                        bokmal { + "og barnetillegget " },
                        nynorsk { + "og barnetillegget " },
                    )
                }
                text (
                    bokmal { + "blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL." },
                    nynorsk { + "blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet " + quoted("uføretrygd") +" når du loggar deg inn på $NAV_URL." },
                )
            }
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.HarDuSpoersmaal(Constants.ETTEROPPGJOR_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
        includeAttachment(opplysningerOmETteroppgjoeretUTLegacy, pe, pe.inkludervedleggopplysningerometteroppgjoeret())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}

