package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.EtteroppgjoerEtterbetalingAutoDtoSelectors.pe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.*
import no.nav.pensjon.brev.maler.fraser.ufoer.HarDuSpoersmaalEtteroppgjoer
import no.nav.pensjon.brev.maler.legacy.fraser.TBU2278_Generated
import no.nav.pensjon.brev.maler.legacy.vedlegg.opplysningerOmETteroppgjoeretUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EtteroppgjoerEtterbetalingAutoLegacy : AutobrevTemplate<EtteroppgjoerEtterbetalingAutoDto> {

    override val kode = Brevkode.AutoBrev.UT_ETTEROPPGJOER_ETTERBETALING_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerEtterbetalingAutoDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om etteroppgjør - etterbetaling",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {

        title {
            text(
                Bokmal to "NAV har gjort et ",
                Nynorsk to "NAV har gjort eit ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt_New = true     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'etterbet'            OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO_New = 'tilbakekr'            )    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser_New = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser_New = true            ) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt_new() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo_new().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser_new() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser_new()))) {
                text(
                    Bokmal to "nytt ",
                    Nynorsk to "nytt ",
                )
            }
            text(
                Bokmal to "etteroppgjør av uføretrygd for ",
                Nynorsk to "etteroppgjer av uføretrygd for ",
            )
            ifNotNull(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()){
                textExpr(
                    Bokmal to it.format(),
                    Nynorsk to it.format()
                )
            }
        }

        outline {
            includePhrase(TBU3301_Generated(pe))
            includePhrase(TBU1091_Generated)
            includePhrase(TBU1092_Generated)
            includePhrase(TBU3317_Generated)

            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)      ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                includePhrase(TBU4018_Generated(pe))
            }

            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                includePhrase(TBU4019_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT  <> 0  AND (PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeTom < PE_UT_lastDay OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom > PE_UT_firstDay))  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodetom().legacyLessThan(pe.ut_lastday()) or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom().legacyGreaterThan(pe.ut_firstday())))){
                includePhrase(TBU3325_Generated(pe))
                includePhrase(TBU4020_Generated(pe))
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 AND PE_UT_PeriodeFomMindreLik0101() = true AND PE_UT_PeriodeTomStorreLik3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and pe.ut_periodefommindrelik0101() and pe.ut_periodetomstorrelik3112())){
                includePhrase(TBU3304_Generated(pe))
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
                includePhrase(TBU4024_Generated(pe))
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
                includePhrase(TBU3307_Generated(pe))
            }
            includePhrase(TBU3305_Generated)

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                includePhrase(TBU3306_Generated)
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true ) )) THEN   INCLUDE ENDIF
            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                includePhrase(TBU4026_Generated)
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                includePhrase(TBU4027_Generated)
            }

            //IF(( PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true ) )) THEN   INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())))){
                includePhrase(TBU4028_Generated)
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt = true       AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = "etterbet"               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = "tilbakekr"              )     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser = true               OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser = true             )     ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser()))){
                includePhrase(TBU4096_Generated(pe))
            }
            includePhrase(TBU3308_Generated)
            includePhrase(TBU3309_Generated(pe))
            includePhrase(TBU2366_Generated)
            includePhrase(TBU2278_Generated(pe))
            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(HarDuSpoersmaalEtteroppgjoer)
        }
        includeAttachment(opplysningerOmETteroppgjoeretUTLegacy, pe, pe.inkludervedleggopplysningerometteroppgjoeret())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}

