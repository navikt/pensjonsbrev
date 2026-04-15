package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnvilgelseUforetrygdBosattNorgeEtterUtland : RedigerbarTemplate<InnvilgelseUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD_NORGE_UTLAND
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av uføretrygd bosatt Norge etter utland",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om uføretrygd" },
                nynorsk { +"Nav har innvilget søknaden din om uføretrygd " },
            )
        }
        outline {
            val pe = pesysData.pe

            val trygdetideosland = pe.vedtaksbrev_grunnlag_persongrunnlagsliste_trygdetidsgrunnlageos_trygdetidsgrunnlageos_trygdetideosland().notEqualTo("")
            val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
            val barnetilleggSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            val barnetilleggFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
            val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
            val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()

            val txtOgEllerEktefelle = if (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().equals(true)) " og/eller ektefelle" else ""

            paragraph {
                text (
                    bokmal { + "Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget uføretrygd med en foreløpig beregning. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen fører ikke til endringer i uføretrygden din." },
                    nynorsk { + "Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget uføretrygd med en foreløpig beregning. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen fører ikke til endringer i uføretrygden din." },
                )
                text (
                    bokmal { + "<Eller>" },
                    nynorsk { + "<Eller>" },
                )
                text (
                    bokmal { + "Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget et forskudd på uføretrygd. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen fører ikke til endringer i uføretrygden din." },
                    nynorsk { + "Vi viser til vedtak fra " + fritekst("vedtaksdato") + " der du fikk innvilget et forskudd på uføretrygd. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå gjort en endelig beregning av uføretrygden din. Den endelige beregningen fører ikke til endringer i uføretrygden din." },)
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(ektefelletilleggInnvilget) and not(barnetilleggSerkullInnvilget) and not(barnetilleggFellesInnvilget) and not(gjenlevendetilleggInnvilget) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du får fortsatt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd per måned før skatt." },
                        nynorsk { + "Du får framleis " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd per månad før skatt." },
                    )
                }
            }

            //IF(  ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true ) AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  ) THEN      INCLUDE ENDIF
            showIf(((barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget) and not(gjenlevendetilleggInnvilget) and not(ektefelletilleggInnvilget) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du får fortsatt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og barnetillegg per måned før skatt." },
                        nynorsk { + "Du får framleis " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og barnetillegg per månad før skatt." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf((not(barnetilleggSerkullInnvilget) and not(barnetilleggFellesInnvilget) and not(ektefelletilleggInnvilget) and gjenlevendetilleggInnvilget and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du får fortsatt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt." },
                        nynorsk { + "Du får framleis " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false) THEN      INCLUDE ENDIF
            showIf(((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget)) or (not(barnetilleggSerkullInnvilget) and barnetilleggFellesInnvilget) or (barnetilleggSerkullInnvilget and barnetilleggFellesInnvilget)) and (gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()))){
                paragraph {
                    text (
                        bokmal { + "Du får fortsatt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                        nynorsk { + "Du får framleis " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_true()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo" AND PE_UT_Forsorgeransvar_ingen_er_false()  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt())){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                        nynorsk { + "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad." },
                    )
                }
            }

            paragraph {
                text (
                    bokmal { + "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
                    nynorsk { + "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
                )
            }

            title1 {
                text (
                    bokmal { + "Begrunnelse for vedtaket" },
                    nynorsk { + "Grunngiving for vedtaket" },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidEOSLand,1) <> "") THEN      INCLUDE ENDIF
            showIf(trygdetideosland){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din har vært foreløpig beregnet etter folketrygdlovens regler. Etter EØS- avtalens regler har du rett til en alternativ beregning." },
                        nynorsk { + "Uføretrygda di har vore førebels rekna ut etter folketrygdlova sine reglar. Etter EØS-avtalen sine reglar har du rett til ei alternativ rekning." },
                    )
                    text (
                        bokmal { + "Vi har sammenlignet hvor mye du får i uføretrygd beregnet både etter folketrygdlovens regler og EØS-avtalens regler. Du får den beregningen som er mest gunstig for deg. Uføretrygden din er derfor beregnet etter reglene i folketrygdloven." },
                        nynorsk { + "Vi har samanlikna kor mykje du får i uføretrygd rekna både etter folketrygdlova sine reglar og EØS-avtalen sine reglar. Du får den rekninga som er mest gunstig for deg. Uføretrygda di er derfor rekna ut etter reglane i folketrygdlova." },
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidEOSLand,1) = "") THEN      INCLUDE ENDIF
            showIf(not (trygdetideosland)){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er beregnet etter folketrygdlovens regler. Fordi du ikke har opparbeidet deg trygdetid i et annet EØS-land, har du ikke rett på en alternativ beregning etter EØS-avtalens regler." },
                        nynorsk { + "Uføretrygda di er rekna ut etter folketrygdlova sine reglar. Fordi du ikkje har opparbeidd deg trygdetid i eit anna EØS-land, har du ikkje rett på ei alternativ rekning etter EØS-avtalen sine reglar." },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 52." },
                    nynorsk { + "Vedtaket er gjort etter EØS-forordning 883/2004 artikkel 52." },
                )
            }
            title1 {
                text (
                    bokmal { + "Vedtak fra utenlandske trygdemyndigheter" },
                    nynorsk { + "Vedtak frå utanlandske trygdemyndigheiter" },
                )
            }

            paragraph {
                text (
                    bokmal { + "<Fritekst: velg riktig avsnitt>" },
                    nynorsk { + "<Fritekst: velg riktig avsnitt>" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Alt 1: Vi har mottatt melding fra " + fritekst("land") + " om at de har innvilget søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler, og du vil få utbetalt trygd direkte fra dem. Du kan lese mer om dette i det utenlandske vedtaket." },
                    nynorsk { + "Alt 1: Vi har mottatt melding fra " + fritekst("land") + " om at de har innvilget søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler, og du vil få utbetalt trygd direkte fra dem. Du kan lese mer om dette i det utenlandske vedtaket." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Alt 2: Vi har mottatt melding fra " + fritekst("land") + " om at de har avslått søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler. Du kan lese mer om dette i det utenlandske vedtaket." },
                    nynorsk { + "Alt 2: Vi har mottatt melding fra " + fritekst("land") + " om at de har avslått søknaden din om uføretrygd. De har fattet vedtaket etter egne nasjonale regler. Du kan lese mer om dette i det utenlandske vedtaket." },
                )
            }
            paragraph {
                text (
                    bokmal { + "Du finner samlet vedtak fra Norge og " + fritekst("land") + " i vedlegget «P1- Samlet melding om pensjonsvedtak». Vi har også lagt ved kopi av vedtak fra " + fritekst("utenlandsk trygdemyndighet") + "." },
                    nynorsk { + "Du finner samlet vedtak fra Norge og " + fritekst("land") + " i vedlegget «P1- Samlet melding om pensjonsvedtak». Vi har også lagt ved kopi av vedtak fra " + fritekst("utenlandsk trygdemyndighet") + "." },
                )
            }

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
            showIf(uforegrad.equalTo(100)){
                title1 {
                    text (
                        bokmal { + "Skal du kombinere uføretrygd og inntekt?" },
                        nynorsk { + "Skal du kombinere uføretrygd og inntekt?" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0) THEN      INCLUDE ENDIF
            showIf((uforegrad.lessThan(100) and uforegrad.greaterThan(0))){
                title1 {
                    text (
                        bokmal { + "For deg som kombinerer uføretrygd og inntekt" },
                        nynorsk { + "For deg som kombinerer uføretrygd og inntekt" },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene." },
                    nynorsk { + "Det er mogleg for deg å ha inntekt ved sida av uføretrygda di. Det lønner seg å jobbe fordi inntekt og uføretrygd alltid vil vere høgare enn uføretrygd åleine." },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = FF_Round(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop * 0.4) AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1) = 0) THEN      INCLUDE ENDIF
            showIf((uforegrad.equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000) THEN      INCLUDE ENDIF
            showIf((uforegrad.equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Dette er inntektsgrensa di." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda mens du er i varig tilrettelagt arbeid utan at uføretrygda di blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            //IF(  (FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) < 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) > 0)  OR  (FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt ) > 0 AND  FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) = 100) ) THEN      INCLUDE ENDIF
            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).lessThan(100) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).greaterThan(0)) or ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()).equalTo(100)))){
                paragraph {
                    text (
                        bokmal { + "Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { + "Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and uforegrad.greaterThan(0) and uforegrad.lessThan(100))){
                paragraph {
                    text (
                        bokmal { + "Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " per år. Du kan i tillegg ha en årlig inntekt på 60 000 kroner, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { + "Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad." },
                    nynorsk { + "Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "For deg utgjør kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er bare den delen av inntekten din som overstiger " + pe.ut_inntektsgrense_faktisk().format()
                        + ", som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av den inntekten du har over " + pe.ut_inntektsgrense_faktisk().format() + " trekkes fra uføretrygden din." },
                    nynorsk { + "For deg utgjer kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er berre den delen av inntekta di som overstig " + pe.ut_inntektsgrense_faktisk().format()
                        + ", som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av inntekta du har over " + pe.ut_inntektsgrense_faktisk().format() + " blir trekt frå uføretrygda di." },
                )
            }
            paragraph {
                text (
                    bokmal { + "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på " + uforegrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din." },
                    nynorsk { + "Blir uføretrygda di redusert på grunn av inntekt beheld du likevel uføregraden din på " + uforegrad.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di." },
                )
            }
            paragraph {
                text (
                    bokmal { + "Reglene for inntektsgrensen og hvordan inntekt reduserer uføretrygd, gjelder bare for den norske uføretrygden din. Har du spørsmål om inntektsgrensen i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                    nynorsk { + "Reglane for inntektsgrensa og korleis inntekt reduserer uføretrygd, gjeld berre for den norske uføretrygda di. Har du spørsmål om inntektsgrensa i eit anna land, må du kontakte trygdemyndigheitene i det landet det gjeld." },
                )
            }
            title1 {
                text (
                    bokmal { + "Du må melde fra om eventuell inntekt" },
                    nynorsk { + "Du må melde frå om eventuell inntekt" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Dersom du er i jobb eller har planer om å jobbe må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din. " },
                    nynorsk { + "Dersom du er i jobb eller har planar om å jobbe må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som mogleg, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du vil få utbetalt i uføretrygd ved sida av inntekta di. " },
                )
            }

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                        nynorsk { + "Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + " Dersom du mottar " },
                            nynorsk { + "Dersom du får " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(ektefelletilleggInnvilget){
                        text (
                            bokmal { + "ektefelletillegg" },
                            nynorsk { + "ektefelletillegg" },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(gjenlevendetilleggInnvilget){
                        text (
                            bokmal { + "gjenlevendetillegg" },
                            nynorsk { + "attlevandetillegg" },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + " vil dette tillegget også bli redusert." },
                            nynorsk { + " vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til NAV. Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { + "Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til NAV. Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di."},
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { + "Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
            showIf((pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast."},
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres."},
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast."},
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du forsørger ikke barn" + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner."},
                        nynorsk { + "Du forsørgjer ikkje barn" + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"
            showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo")){
                paragraph {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND PE_UT_Forsorgeransvar_ingen_er_false()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false())){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { + "Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                    )

                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()))){
                        text (
                            bokmal { + " " },
                            nynorsk { + " " }
                        )
                    }

                    //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + "Da du forsørger barn" },
                            nynorsk { + "Da du forsørgjer barn" },
                        )
                    }

                    //IF(FF_GetArrayElement_Boolean(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphReduksjonsperiodeListe_InstOpphReduksjonsperiode_Forsorgeransvar) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and ektefelletilleggInnvilget)){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { + " og/eller ektefelle" },
                        )
                    }

                    //IF(PE_UT_Forsorgeransvar_siste_er_true()) THEN      INCLUDE ENDIF
                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + ", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { + ", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                        )
                    }
                    text (
                        bokmal { + "Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { + "Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + "Dersom du mottar " },
                            nynorsk { + "Dersom du mottar " },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
                    showIf(ektefelletilleggInnvilget){
                        text (
                            bokmal { + "ektefelletillegg" },
                            nynorsk { + "ektefelletillegg" },
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                    showIf(gjenlevendetilleggInnvilget){
                        text (
                            bokmal { + "gjenlevendetillegg" },
                            nynorsk { + "attlevandetillegg" },
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true OR PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + " vil dette tillegget også bli redusert." },
                            nynorsk { + " vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_fo"  AND  PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype().equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du er under straffegjennomføring og uføretrygden din kommer derfor ikke til utbetaling. Vi vil sette i gang utbetalingene igjen når straffegjennomføringen er avsluttet." },
                        nynorsk { + "Du er under straffegjennomføring og uføretrygda di kommer derfor ikkje til utbetaling. Vi vil sette i gang utbetalingane igjen når straffegjennomføringa er avslutta." },
                    )
                }
            }

            //PE_SaksData_SakAPogUP = true
            showIf(pe.saksdata_sakapogup()){
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Ufoeretrygd.RettTilNyVurdering)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
