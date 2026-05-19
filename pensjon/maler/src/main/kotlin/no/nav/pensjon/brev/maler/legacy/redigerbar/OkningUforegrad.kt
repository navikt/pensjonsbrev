package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.nyeAvslagBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.nyeInnvilgedeBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.oifuVedVirkningstidspunkt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
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
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

@TemplateModelHelpers
object OkningUforegrad : RedigerbarTemplate<InnvilgelseUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtOkningUforegrad.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_OKNING_UFOREGRAD
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - økning av uføregrad",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om økt uføretrygd" },
                nynorsk { +"Nav har innvilget søknaden din om auka uføretrygd" },
            )
        }
        outline {
            val pe = pesysData.pe

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
            val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(LocalDate.now())
            val onsketvirkningsdato = pe.vedtaksdata_kravhode_onsketvirkningsdato().ifNull(LocalDate.now())
            val skadetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().ifNull(LocalDate.now())
            val bostedutland = (pe.grunnlag_persongrunnlagsliste_personbostedsland().notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))

            val uforegradFraBeregning = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
            val yrkesskadegradFraBeregning = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
            val uforegradFraVilkar = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()
            val yrkesskadegradFraVilkar = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
            val yrkesskadebegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()

            val barnetilleggSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            val barnetilleggFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
            val btInnvilget = barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget
            val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
            val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
            val btSerkullNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
            val btSerkullNetto0 = btSerkullNetto.equalTo(0)
            val btFellesNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
            val btFellesNetto0 = btFellesNetto.equalTo(0)
            val justeringsbelopperar = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
            val justeringsbelopperar0 = justeringsbelopperar.equalTo(0)

            val instopphanvendt = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()
            val instoppholdtype = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()

            val kunBt = btInnvilget and not(ektefelletilleggInnvilget) and not(gjenlevendetilleggInnvilget)
            val kunGt = not(btInnvilget) and not(ektefelletilleggInnvilget) and gjenlevendetilleggInnvilget
            val kunEt = ektefelletilleggInnvilget and not(btInnvilget) and not(gjenlevendetilleggInnvilget)
            val btOgGt = btInnvilget and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget)
            val btOgEt = btInnvilget and ektefelletilleggInnvilget and not(gjenlevendetilleggInnvilget)

            val ifuinntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
            val ifubegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
            val ieuinntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
            val ieuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
            val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
            val oifuMerEnnIfu = oifuKroner.greaterThan(ifuinntekt)

            val txtOgEllerEktefelle = if (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().equals(true)) " og/eller ektefelle" else ""

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).notEqualTo("oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din øker fra " + fritekst("Forrige Uforegrad") + " til " + uforegradFraBeregning.format() + " prosent fra " + virkningstidpunkt.format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om auka uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din aukar frå " + fritekst("Forrige Uforegrad") + " til " + uforegradFraBeregning.format() + " prosent frå " + virkningstidpunkt.format() + "." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din øker fra " + fritekst("Forrige Uforegrad") + " til " + uforegradFraBeregning.format() + " prosent fra " + virkningstidpunkt.format() + ". Hele uføretrygden din er innvilget med rettighet som ung ufør." },
                        nynorsk { + "Vi har innvilga søknaden din om auka uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Uføregraden din aukar frå " + fritekst("Forrige Uforegrad") + " til " + uforegradFraBeregning.format() + " prosent frå " + virkningstidpunkt.format() + ". Heile uføretrygda di er innvilga med rett som ung ufør. " },
                    )
                }
            }

            showIf(((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().notEqualTo("oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og får " + uforegradFraBeregning.format() + " prosent uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga di, og får " + uforegradFraBeregning.format() + " prosent uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt") and (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage") or pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_anke")))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om økt uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og får " + uforegradFraBeregning.format() + " prosent uføretrygd med rettighet som ung ufør fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga di, og får " + uforegradFraBeregning.format() + " prosent uføretrygd med rett som ung ufør frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf(((yrkesskadegradFraVilkar).equalTo((uforegradFraVilkar)))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt") ) THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).lessThan((uforegradFraVilkar)) and (yrkesskadegradFraVilkar).greaterThan(0) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt")))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_i_1"))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at uførheten din ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at uførleiken din ikkje kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt")) and ((yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_i_2"))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold enn din godkjente yrkesskade eller yrkessykdom som er årsak til den økte uførheten din." },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold enn godkjend yrkesskade eller yrkessjukdom som er årsak til den auka uførleiken din." },
                    )
                }
            }

            showIf((pesysData.nyeInnvilgedeBarnetillegg.isNotEmpty())){
                paragraph {
                    text (
                        bokmal { +"Du er innvilget barnetillegg i uføretrygden din for" },
                        nynorsk { +"Du er innvilga barnetillegg i uføretrygda di for" },
                    )
                    includePhrase(Felles.TextOrList(pesysData.nyeInnvilgedeBarnetillegg.map(BarnetilleggFormatter), 0))

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf((barnetilleggFellesInnvilget and (((barnetilleggSerkullInnvilget and btSerkullNetto0) and (barnetilleggFellesInnvilget and btFellesNetto0)) or (barnetilleggSerkullInnvilget and btSerkullNetto0 and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and btFellesNetto0 and not(barnetilleggSerkullInnvilget))))){
                        text (
                            bokmal { + "Tillegget blir ikke utbetalt fordi inntekten til deg og din ektefelle er over grensen for å få utbetalt barnetillegg. " },
                            nynorsk { + "Tillegget blir ikkje utbetalt fordi inntekta di og inntekta til ektefellen din er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }

                    //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  AND  (((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)   AND   (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)   OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false))  ) THEN      INCLUDE ENDIF
                    showIf(((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget)) and (((barnetilleggSerkullInnvilget and btSerkullNetto0) and (barnetilleggFellesInnvilget and btFellesNetto0)) or (barnetilleggSerkullInnvilget and btSerkullNetto0 and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and btFellesNetto0 and not(barnetilleggSerkullInnvilget))))){
                        text (
                            bokmal { + "Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "Tillegget blir ikkje betalt ut fordi inntekta di er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }
            }

            showIf(pesysData.nyeAvslagBarnetillegg.isNotEmpty()) {
                paragraph {
                    text(
                        bokmal { +"Vi har avslått barnetillegg i uføretrygden din for" },
                        nynorsk { +"Vi har avslått barnetillegg i uføretrygda di for" },
                    )
                    includePhrase(Felles.TextOrList(pesysData.nyeAvslagBarnetillegg.map(BarnetilleggFormatter), 0))
                }
            }

            showIf(gjenlevendetilleggInnvilget){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget gjenlevendetillegg i uføretrygden din." },
                        nynorsk { + "Du er innvilga attlevandetillegg i uføretrygda di." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and instopphanvendt)){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er innlagt på institusjon." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er innlagd på institusjon." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and not(instopphanvendt))){
                paragraph {
                    text (
                        bokmal { + "Du er innlagt på institusjon, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er innlagd på institusjon, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_fo") and instopphanvendt)){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er under straffegjennomføring." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_fo") and not(instopphanvendt))){
                paragraph {
                    text (
                        bokmal { + "Du er under straffegjennomføring, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er under straffegjennomføring, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf(not(instopphanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd" },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd" },
                    )
                    showIf(kunBt) {
                        text(bokmal { +" og barnetillegg" }, nynorsk { +" og barnetillegg" })
                    }.orShowIf(kunGt) {
                        text(bokmal { +" og gjenlevendetillegg" }, nynorsk { +" og attlevandetillegg" })
                    }.orShowIf(btOgGt) {
                        text(bokmal { +", barne- og gjenlevendetillegg" }, nynorsk { +", barne- og attlevandetillegg" })
                    }.orShowIf(kunEt) {
                        text(bokmal { +" og ektefelletillegg" }, nynorsk { +" og ektefelletillegg" })
                    }.orShowIf(btOgEt) {
                        text(bokmal { +", barne- og ektefelletillegg" }, nynorsk { +", barne- og ektefelletillegg" })
                    }
                    text(
                        bokmal { +" per måned før skatt." },
                        nynorsk { +" per månad før skatt." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true()){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo("reduksjon_hs")){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }.orShowIf(instoppholdtype.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false()){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            // TODO: Ny tekst for utland (som i innvilgelseUtland
            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "" OR PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) = "nor") THEN      INCLUDE ENDIF
            showIf(bostedutland) {
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { + "Uføretrygda blir betalt ut seinast den 20. kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i " + fritekst("månad og år") + "." },
                    )
                }
            }.orShow {
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { + "Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("Månad og år") + "." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Begrunnelse for vedtaket" },
                    nynorsk { + "Grunngiving for vedtaket" },
                )
            }

            paragraph {
                text (
                    bokmal { + "For å ha rett til økt uføretrygd må du oppfylle disse vilkårene:" },
                    nynorsk { + "For å ha rett til auka uføretrygd må du oppfylle desse vilkåra:" },
                )
                list {
                    item {
                        text (
                            bokmal { + "Du må være mellom 18 og 67 år." },
                            nynorsk { + "Du må vere mellom 18 og 67 år." },
                        )
                    }
                    item {
                        text (
                            bokmal { + "Du må ha vært medlem av folketrygden i de siste tre årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene." },
                            nynorsk { + "Du må ha vore medlem av folketrygda i dei siste tre åra fram til uføretidspunktet, eller oppfylle ein av unntaksreglane." },
                        )
                    }
                    item {
                        text (
                            bokmal { + "Inntektsevnen din må ha blitt ytterligere nedsatt, og denne nedsettelsen må være varig." },
                            nynorsk { + "Inntektsevna di må ha blitt ytterlegare sett ned, og denne nedsetjinga må vere varig." },
                        )
                    }
                    item {
                        text (
                            bokmal { + "Sykdom eller skade må være hovedårsak til at inntektsevnen din har blitt ytterligere nedsatt." },
                            nynorsk { + "Sjukdom eller skade må vere hovudårsak til at inntektsevna di er blitt ytterlegare sett ned." },
                        )
                    }
                    item {
                        text (
                            bokmal { + "Du må ha gjennomført hensiktsmessig behandling og arbeidsrettede tiltak." },
                            nynorsk { + "Du må ha gjennomført formålstenleg behandling og arbeidsretta tiltak." },
                        )
                    }
                }
            }

            paragraph {
                text (
                    bokmal { + "Vi har kommet fram til at du oppfyller disse vilkårene." },
                    nynorsk { + "Vi har kome fram til at du oppfyller desse vilkåra." },
                )
            }

            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("omgj_etter_klage")){
                paragraph {
                    text (
                        bokmal { + "Søknaden din om økt uføretrygd er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { + "Søknaden din om auka uføretrygd er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
                )
            }

            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("ikke_oppfylt"))){
                title1 {
                    text (
                        bokmal { + "Uførhet som skyldes yrkesskade eller yrkessykdom" },
                        nynorsk { + "Uførleik som kjem av yrkesskade eller yrkessjukdom" },
                    )
                }
            }

            showIf((yrkesskadegradFraBeregning.equalTo(uforegradFraBeregning) and yrkesskadebegrunnelse.equalTo("stdbegr_12_17_1_o_1"))){
                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.equalTo(uforegradFraBeregning) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and yrkesskadebegrunnelse.equalTo("stdbegr_12_17_1_o_1") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) = FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).equalTo((uforegradFraVilkar)) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and yrkesskadebegrunnelse.equalTo("stdbegr_12_17_1_o_1") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_2") THEN      INCLUDE ENDIF
            showIf(((yrkesskadegradFraVilkar).lessThan((uforegradFraVilkar)) and (yrkesskadegradFraVilkar).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and yrkesskadebegrunnelse.equalTo("stdbegr_12_17_1_o_2"))){
                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") + "." },
                        nynorsk { + "Vi har kome fram til at " + yrkesskadegradFraVilkar.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") + "." },
                    )
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    paragraph {
                        text (
                            bokmal { + "Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { + "Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                        )
                    }
                }

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                    paragraph {
                        text (
                            bokmal { + "Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { + "Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                        )
                    }
                    paragraph {
                        text (
                            bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                            nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                        )
                    }
                }
            }

            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt") and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_o_3"))){
                paragraph {
                    text (
                        bokmal { + "Du er tidligere innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom. Vi har ut fra opplysninger i saken din vurdert om yrkesskaden eller yrkessykdommen din også er årsak til at uførheten din har økt, eller om dette skyldes andre sykdomsforhold." },
                        nynorsk { + "Du er tidlegare innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningar i saka di vurdert om yrkesskaden eller yrkessjukdommen din også er årsak til at uførleiken din har auka, eller om dette kjem av andre sjukdomsforhold." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. " + fritekst("Konkret begrunnelse") + "" },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. " + fritekst("Konkret begrunnelse") + "" },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_1"))){
                paragraph {
                    text (
                        bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsinntekt().format() + " kroner." },
                        nynorsk { + "Vi har fastsett skadetidspunktet ditt til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsinntekt().format() + " kroner." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))){
                paragraph {
                    text (
                        bokmal { + "Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                        nynorsk { + "Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                    )
                }
            }

            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("ikke_oppfylt") and yrkesskadebegrunnelse.equalTo("stdbegr_12_17_1_i_2"))){
                paragraph {
                    text (
                        bokmal { + "Du har en godkjent yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til den økte uførheten din." },
                        nynorsk { + "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsak til den auka uførleiken din." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at det er andre sykdomsforhold som er årsak til den økte uførheten din. " + fritekst("Konkret begrunnelse") + "" },
                        nynorsk { + "Vi har kome fram til at det er andre sjukdomsforhold som er årsak til den auka uførleiken din. " + fritekst("Konkret begrunnelse") + "" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_o_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                paragraph {
                    text (
                        bokmal { + "Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, noe som gir deg en høyere uføretrygd." },
                        nynorsk { + "Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, noko som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = false) THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_o_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))){
                //[TBU3732NN, TBU3732]

                paragraph {
                    text (
                        bokmal { + "Deler av uføretrygden din vil fortsatt bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Deler av uføretrygda di vil fortsatt bli rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest = false AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt") THEN      INCLUDE ENDIF
            showIf((yrkesskadegradFraBeregning.lessThan(uforegradFraBeregning) and yrkesskadegradFraBeregning.greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_o_3") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("ikke_oppfylt") and (yrkesskadebegrunnelse).equalTo("stdbegr_12_17_1_i_1"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke en godkjent yrkesskade eller yrkessykdom. Uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") + "." },
                        nynorsk { + "Du har ikkje ein godkjend yrkesskade eller yrkessjukdom. Uføretrygda di blir derfor ikkje innvilga etter reglar for yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") + "." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Dette er virkningstidspunktet ditt" },
                    nynorsk { + "Dette er verknadstidspunktet ditt" },
                )
            }


            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_1"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_2"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til " + fritekst("dato for opphør") + " og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til " + fritekst("Dato for opphør") + ", og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til " + fritekst("dato for opphør") + ". I denne måneden får du utbetalt den delen av sykepengene med det beløp som overstiger uføretrygden." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til " + fritekst("Dato for opphør") + ". I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_4"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyller 18 år." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_5"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Vi mottok søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen.<FRITEKST>." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidspunktet. Vi fekk søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd for opptil tre månader før denne datoen.<FRITEKST>." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_13"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget økt uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Du har rett til uføretrygd fra den måneden vilkårene er oppfylt." },
                        nynorsk { + "Du har fått innvilga auka uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidpunktet. Du har rett til uføretrygd frå den månaden vilkåra er oppfylde." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Dette er uføretidspunktet ditt" },
                    nynorsk { + "Dette er uføretidspunktet ditt" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Vi har tidligere fastsatt uføretidspunktet ditt til " + fritekst("Første Uforetidspunkt") + ". Når uføregraden øker, fastsetter vi et nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + uforetidspunkt.format() + "." },
                    nynorsk { + "Vi har tidlegare fastsett uføretidspunktet ditt til " + fritekst("Første Uforetidspunkt") + ". Når uføregraden aukar, fastset vi eit nytt uføretidspunkt. Det nye uføretidspunktet ditt er " + uforetidspunkt.format() + "." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Vi sammenligner beregningen av uføretrygden ut fra det tidspunktet som først ble fastsatt, og det nye uføretidspunktet. Du får det alternativet som gir deg høyest uføretrygd. Det er uføretidspunktet som bestemmer hvilke inntektsår vi legger til grunn for beregningen din." },
                    nynorsk { + "Vi samanliknar berekninga av uføretrygda ut frå det tidspunktet som først blei fastsett, og det nye uføretidspunktet. Du får det alternativet som gir deg høgast uføretrygd. Det er uføretidspunktet som bestemmer kva inntektsår vi legg til grunn for berekninga di." },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt <> FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().notEqualTo((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt())))){
                paragraph {
                    text (
                        bokmal { + "Du får fortsatt beregnet uføretrygden din ut fra tidligere uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { + "Du får framleis rekna ut uføretrygda di ut frå tidlegare uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }

            //IF(DateDiff("d", PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt,   FF_GetArrayElement_Date(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt)) = 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().notEqualTo(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()))){
                paragraph {
                    text (
                        bokmal { + "Du får beregnet uføretrygden din ut fra nytt uføretidspunkt, fordi dette gir deg en høyere uføretrygd." },
                        nynorsk { + "Du får rekna ut uføretrygda di ut frå nytt uføretidspunkt, fordi dette gir deg ei høgare uføretrygd." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Slik har vi fastsatt uføregraden din" },
                    nynorsk { + "Slik har vi fastsett uføregraden din" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og vurdert hvor mye inntektsevnen din er varig nedsatt." },
                    nynorsk { + "Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett." },
                )
            }

            showIf(((ifubegrunnelse).equalTo("stdbegr_12_8_2_1") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". " + fritekst("begrunnelse for fastsatt IFU") + ". " },
                        nynorsk { + "Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". " + fritekst("begrunnelse for fastsatt IFU") + ". " },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + "Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + "Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ifubegrunnelse).equalTo("stdbegr_12_8_2_3") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er derfor tidligere fastsatt til " + ifuinntekt.format() + " kroner på uføretidspunktet ditt. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger grunnbeløpet." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ifubegrunnelse).equalTo("stdbegr_12_8_2_5") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er derfor tidligere fastsatt til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat,1) = true) AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,1) = "stdbegr_12_8_2_4")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse,1) <> "stdbegr_12_8_1_3"))  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt")) and (ifubegrunnelse.equalTo("stdbegr_12_8_2_4")) and (ieuBegrunnelse.notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + ifuinntekt.format() + " kroner. Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                        nynorsk { + "Inntekta di før du blei ufør, er fastsett til " + ifuinntekt.format() + " kroner. Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            //IF((FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "") AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100  AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) = 0  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9"  ) THEN      INCLUDE ENDIF
            showIf((((ifubegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and uforegradFraBeregning.equalTo(100) and (ieuinntekt).equalTo(0) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifubegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifubegrunnelse).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { + "Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            //IF( ( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "" ) AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) OR ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0 )) AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) <> "stdbegr_12_8_1_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_2"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) <> "stdbegr_12_8_2_9" ) THEN      INCLUDE ENDIF
            showIf((((ifubegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and ((uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100)) or (uforegradFraBeregning.equalTo(100) and (ieuinntekt).greaterThan(0))) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifubegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifubegrunnelse).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har en inntekt på " + ieuinntekt.format() + " kroner, og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { + "Du har ei inntekt på " + ieuinntekt.format() + " kroner, og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            showIf((ieuBegrunnelse.equalTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". " + fritekst("begrunnelse for fastsatt IFU") + "." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + ifuinntekt.format() + " kroner på uføretidspunktet i " + uforetidspunkt.format() + ". " + fritekst("begrunnelse for fastsatt IFU") + "." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
                paragraph {
                    text (
                        bokmal { + "Det er dokumentert at du har inntektsmuligheter som du ikke benytter. Disse tar vi med når vi fastsetter inntekten din etter at du ble ufør. Inntekten din etter at du ble ufør er fastsatt til " + ieuinntekt.format() + " kroner, og uføregraden din er derfor fastsatt til " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { + "Det er dokumentert at du har inntektsmoglegheiter som du ikkje nyttar. Desse tek vi med når vi fastset inntekta di etter at du blei ufør. Inntekta di etter at du blei ufør, er fastsett til " + ieuinntekt.format() + " kroner, og uføregraden din er derfor fastsett til " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }

            showIf(((ifubegrunnelse).equalTo("stdbegr_12_8_2_2") or (ifubegrunnelse).equalTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + ifuinntekt.format() + " kroner. Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + ieuinntekt.format() + " kroner. Inntekten din etter at du ble ufør er derfor fastsatt til " + ieuinntekt.format() + " kroner og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + ifuinntekt.format() + " kroner. Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + ieuinntekt.format() + " kroner. Inntekta di etter at du blei ufør, er derfor fastsett til " + ieuinntekt.format() + " kroner, og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Du kan lese mer om dette i vedlegget " },
                    nynorsk { + "Du kan lese meir om dette i vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(bokmal { + "." }, nynorsk { + "." })
            }

            showIf(uforegradFraBeregning.equalTo(100)){
                title1 {
                    text (
                        bokmal { + "Skal du kombinere uføretrygd og inntekt?" },
                        nynorsk { + "Skal du kombinere uføretrygd og inntekt?" },
                    )
                }
            }.orShow{
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

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop*0.4)) THEN      INCLUDE ENDIF
            showIf((uforegradFraBeregning.equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and ieuinntekt.equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            //TODO: 60.000 gjelder ikke lengre
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000) THEN      INCLUDE ENDIF
            showIf((uforegradFraBeregning.equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Dette er inntektsgrensa di." },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda mens du er i varig tilrettelagt arbeid utan at uføretrygda di blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,1) < 100 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,1) > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop) THEN      INCLUDE ENDIF
            showIf(((uforegradFraVilkar).lessThan(100) and (uforegradFraVilkar).greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()))){
                paragraph {
                    text (
                        bokmal { + "Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { + "Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                    )
                }
            }

            // TODO: 60.000 er ikke lengre gyldig
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().equalTo(60000) and uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100))){
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
                    bokmal { + "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din." },
                    nynorsk { + "Blir uføretrygda di redusert på grunn av inntekt beheld du likevel uføregraden din på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di." },
                )
            }

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(bostedutland){
                paragraph {
                    text (
                        bokmal { + "Inntektsgrensen gjelder bare for den norske uføretrygden din. Har du spørsmål om inntektsgrensen i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                        nynorsk { + "Inntektsgrensa gjeld berre for den norske uføretrygda di. Har du spørsmål om inntektsgrensa i eit anna land, må du kontakte trygdestyresmaktene i det landet det gjeld." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Du må melde fra om eventuell inntekt" },
                    nynorsk { + "Du må melde frå om eventuell inntekt" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din." },
                    nynorsk { + "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di." },
                )
            }

            showIf(instoppholdtype.equalTo("reduksjon_hs")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((instoppholdtype.equalTo("reduksjon_hs") and (instopphanvendt or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt. " },
                        nynorsk { + "Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    showIf((ektefelletilleggInnvilget)){
                        text (
                            bokmal { + "Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                            nynorsk { + "Dersom du får ektefelletillegg vil dette tillegget også bli redusert." },
                        )
                    }.orShowIf((gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + "Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { + "Dersom du får attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND  (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  OR  PE_UT_Forsorgeransvar_ingen_er_false() OR  PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = true)) THEN      INCLUDE ENDIF
            showIf((instoppholdtype.equalTo("reduksjon_hs") and (instopphanvendt or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din."},
                        nynorsk { + "Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di."}
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false AND PE_UT_Forsorgeransvar_ingen_er_true() AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((not(instopphanvendt) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and instoppholdtype.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { + "Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            //IF(PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs" AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0) THEN      INCLUDE ENDIF
            showIf((pe.ut_forsorgeransvar_ingen_er_false() and instoppholdtype.equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = false  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((not(instopphanvendt) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and instoppholdtype.equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_UT_Forsorgeransvar_ingen_er_false() AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and instoppholdtype.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs") THEN      INCLUDE ENDIF
            showIf((instopphanvendt and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and instoppholdtype.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOpphAnvendt = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "reduksjon_hs"  AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_InstOpphFasteUtgifterperiodeListe_InstOpphFasteUtgifterperiode_FasteUtgifter = 0  AND PE_UT_Forsorgeransvar_ingen_er_true()) THEN      INCLUDE ENDIF
            showIf((instopphanvendt and instoppholdtype.equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du forsørger ikke barn " + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner."},
                        nynorsk { + "Du forsørgjer ikkje barn " + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            showIf(instoppholdtype.equalTo("reduksjon_fo")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }

                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { +"Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                    )

                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()))){
                        text (
                            bokmal { + " " },
                            nynorsk { + " " },
                        )
                    }

                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + "Da du forsørger barn" },
                            nynorsk { +"Da du forsørgjer barn" },
                        )
                    }

                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and ektefelletilleggInnvilget)){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }

                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + ", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { +", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                        )
                    }
                    text (
                        bokmal { + "Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { + "Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    showIf(ektefelletilleggInnvilget){
                        text (
                            bokmal { + "Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                        )
                    }.orShowIf(gjenlevendetilleggInnvilget){
                        text (
                            bokmal { + "Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du mottar attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf((btInnvilget)){
                title1 {
                    text(
                        bokmal { + "Slik påvirker inntekt barnetillegget ditt " },
                        nynorsk { + "Slik verkar inntekt inn på barnetillegget ditt " },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType <> "overgangsregler_2016" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016,1,1)))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir " },
                        nynorsk { + "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir " },
                    )

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner. " },
                            nynorsk { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner. " },
                        )
                    }

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))){
                        text (
                            bokmal { + "ikke utbetalt. " },
                            nynorsk { + "ikkje utbetalt. " },
                        )
                    }

                    //IF( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT > 0) THEN     INCLUDE END IF
                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt." },
                            nynorsk { + "Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt." },
                        )
                    }
                }
            }

            showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0))){
                includePhrase(TBU3802_Generated(pe))
            }

            //IF (   NOT   (     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0     AND     PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0   ) ) THEN   INCLUDE ENDIF
            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){

                //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)   ) THEN      INCLUDE ENDIF
                showIf(((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget))){
                    includePhrase(TBU2338_Generated(pe))
                }

                showIf(barnetilleggFellesInnvilget){
                    includePhrase(TBU2339_Generated(pe))
                }

                showIf((btInnvilget)){
                    paragraph {
                        text (
                            bokmal { + "Endringer i " },
                            nynorsk { + "Endringar i " },
                        )

                        showIf(barnetilleggFellesInnvilget){
                            text (
                                bokmal { + "inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                                nynorsk { + "inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                            )
                        }

                        showIf((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget)){
                            text (
                                bokmal { + "inntekten din " },
                                nynorsk { + "inntekta di " },
                            )
                        }
                        text (
                            bokmal { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                            nynorsk { + "kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                        )
                    }
                }

                showIf(((barnetilleggFellesInnvilget))){
                    paragraph {
                        text (
                            bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                            nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                        )

                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))){
                            text (
                                bokmal { + "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + ". " },
                                nynorsk { + "Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din. "}
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(barnetilleggSerkullInnvilget) and justeringsbelopperar0 and btFellesNetto.notEqualTo(0)))){
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"Til sammen er inntektene høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Til sammen er inntektene lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Til saman er inntektene lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  AND  PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(barnetilleggSerkullInnvilget) and justeringsbelopperar0 and btFellesNetto.notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)))){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 )) THEN      INCLUDE ENDIF
                        showIf(((not(barnetilleggSerkullInnvilget) and justeringsbelopperar0 and btFellesNetto.notEqualTo(0)))){
                            text (
                                bokmal { + "redusert. " },
                                nynorsk { + "redusert. " },
                            )
                        }

                        showIf(((justeringsbelopperar.notEqualTo(0) and not(barnetilleggSerkullInnvilget)))){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto  = 0 ))THEN     INCLUDE ENDIF
                        showIf(((justeringsbelopperar.notEqualTo(0) and not(barnetilleggSerkullInnvilget) and btFellesNetto0))){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget))){
                    paragraph {
                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er høyere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er høgare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er lavere enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                    nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er lågare enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                )
                            }
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto.greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                            text (
                                bokmal { + "redusert ut fra inntekt. " },
                                nynorsk { + "redusert ut frå inntekt. " },
                            )
                        }

                        //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_UT_BTSBInnvilget_Ikke_BTFBInnvilget() )) THEN      INCLUDE ENDIF
                        showIf(((btSerkullNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                            text (
                                bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                                nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner." },
                            )
                        }

                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa()))){
                            text (
                                bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa() and btSerkullNetto0))){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((pe.ut_tbu1286_del1() or pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3())){
                    paragraph {
                        //IF(PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btSerkullNetto.notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())) {
                                text(
                                    bokmal { +"Inntekten din er høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Inntekten din er lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                    nynorsk { +"Inntekta di er lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)  OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btSerkullNetto.notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                nynorsk { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.notEqualTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + "Til sammen er " },
                                nynorsk { + "Til saman er " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <>  0 AND ( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0) ) AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and justeringsbelopperar0)){
                            text (
                                bokmal { + "også " },
                                nynorsk { + "også " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.notEqualTo(0) and justeringsbelopperar0)){
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().greaterThan(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop())) {
                                text(
                                    bokmal { +"inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " høyere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din høgare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"inntektene til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " lavere enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                    nynorsk { +"inntektene til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din lågare enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                                )
                            }
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and btFellesNetto.notEqualTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + "Dette barnetillegget er derfor " },
                                nynorsk { + "Dette barnetillegget er derfor " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and btFellesNetto.notEqualTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0))  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and btFellesNetto.notEqualTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + "redusert ut fra inntekt. " },
                                nynorsk { + "redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + "Barnetilleggene er derfor" },
                                nynorsk { + "Barnetillegga er derfor" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBbrutto  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBbrutto)  AND  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0)  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and (btFellesNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and btSerkullNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0)) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + " ikke" },
                                nynorsk { + " ikkje" },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag = 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag = 0)   OR   (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBfradrag > 0  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBfradrag > 0)) AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  ) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.notEqualTo(0) and btSerkullNetto.notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and justeringsbelopperar0)){
                            text (
                                bokmal { + " redusert ut fra inntekt. " },
                                nynorsk { + " redusert ut frå inntekt. " },
                            )
                        }

                        //IF( PE_UT_TBU1286_del1() AND  ( PE_UT_TBU1286_del2() OR PE_UT_TBU1286_del3() )) THEN      INCLUDE ENDIF
                        showIf((pe.ut_tbu1286_del1() and (pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3()))){
                            text (
                                bokmal { + " " },
                                nynorsk { + " " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2()))){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2() and btFellesNetto0))){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2() and pe.ut_tbu1286_del3()))){
                            text (
                                bokmal { + " " },
                                nynorsk { + " " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3()))){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3() and btSerkullNetto0))){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((barnetilleggSerkullInnvilget and btSerkullNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (btFellesNetto.notEqualTo(0) or not(barnetilleggFellesInnvilget)))){
                    paragraph {
                        text (
                            bokmal { + "Barnetillegget " },
                            nynorsk { + "Barnetillegget " },
                        )

                        showIf(barnetilleggFellesInnvilget){
                            text (
                                bokmal { + "for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { + "for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text (
                            bokmal { + "blir ikke utbetalt fordi du " },
                            nynorsk { + "blir ikkje utbetalt fordi du " },
                        )

                        showIf(barnetilleggFellesInnvilget){
                            text (
                                bokmal { + "alene " },
                                nynorsk { + "åleine " },
                            )
                        }
                        text (
                            bokmal { + "har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                //IF( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) ) THEN      INCLUDE ENDIF
                showIf((barnetilleggFellesInnvilget and btFellesNetto0 and justeringsbelopperar0 and (btSerkullNetto.notEqualTo(0) or not(barnetilleggSerkullInnvilget)))){
                    paragraph {
                        text (
                            bokmal { + "Barnetillegget" },
                            nynorsk { + "Barnetillegget " },
                        )

                        showIf(barnetilleggSerkullInnvilget){
                            text (
                                bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                                nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text (
                            bokmal { + " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0)  )  THEN      INCLUDE ENDIF
                showIf(((barnetilleggSerkullInnvilget and btSerkullNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) and (barnetilleggFellesInnvilget and btFellesNetto0 and justeringsbelopperar0))){
                    paragraph {
                        text (
                            bokmal { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                showIf((btInnvilget)){
                    paragraph {
                        text(
                            bokmal { + "Du kan lese mer om beregningen av barnetillegg i vedlegget " },
                            nynorsk { + "Du kan lese meir om berekninga av barnetillegg i vedlegget " },
                        )
                        namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                        text(bokmal { + "." }, nynorsk { + "." })
                    }
                }
            }

            //IF (PE_UT_KravLinjeKode_VedtakResultat_forekomst_bt_innv()) THEN INCLUDE ENDIF
            showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))){
                includePhrase(TBU5005_Generated)
                paragraph {
                    text(
                        bokmal { + "Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land." },
                        nynorsk { + "Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. " },
                    )
                }
            }

            showIf(gjenlevendetilleggInnvilget){
                title1 {
                    text (
                        bokmal { + "For deg som mottar gjenlevendetillegg" },
                        nynorsk { + "For deg som mottar tillegg for attlevande ektefelle" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd med gjenlevendetillegg. Tillegget er beregnet etter ditt eget og din avdøde ektefelles beregningsgrunnlag og trygdetid. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. " },
                        nynorsk { + "Du er innvilga uføretrygd med attlevandetillegg. Tillegget er rekna ut etter utrekningsgrunnlaget og trygdetida både for deg og for den avdøde ektefellen din. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med. " },
                    )

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()){
                        text (
                            bokmal { + "Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { + "Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            showIf(gjenlevendetilleggInnvilget){
                paragraph {
                    text (
                        bokmal { + "Du kan lese mer om dette i vedlegget " },
                        nynorsk { + "Du kan lese meir om dette i vedlegget " },
                    )
                    namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                    text(bokmal { + "." }, nynorsk { + "." })
                }
            }

            showIf(pe.vedtaksdata_harLopendealderspensjon()){
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_onsketVirkningsDato < PE_VedtakFattetDato_minus_1mnd) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(uforegradFraBeregning) and onsketvirkningsdato.legacyLessThan(pe.vedtakfattetdato_minus_1mnd()))){
                paragraph {
                    text (
                        bokmal { + "Etterbetaling av uføretrygd" },
                        nynorsk { + "Etterbetaling av uføretrygd" },
                    )
                    text (
                        bokmal { + "Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { + "Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
                    )
                }
            }

            showIf(bostedutland){
                includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)
                paragraph {
                    text (
                        bokmal { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
                        nynorsk { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
                    )
                    text (
                        bokmal { + "En utenlandsk myndighet krever refusjon" },
                        nynorsk { + "Eit utanlandsk organ krev refusjon" },
                    )
                    text (
                        bokmal { + "" + fritekst("land") + " har varslet Nav at de kan ha utbetalt for mye penger til deg. De har mulighet til å kreve dette tilbake i etterbetalingen av den norske uføretrygden din. Vi vil holde tilbake etterbetalingen inntil vi har fått svar fra " + fritekst("land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("nasjonalitet") + " myndigheter." },
                        nynorsk { + "" + fritekst("Land") + " har varsla Nav at dei kan ha betalt ut for mykje pengar til deg. Dei har høve til å krevje dette tilbake i etterbetalinga av den norske uføretrygda di. Vi vil halde tilbake etterbetalinga inntil vi har fått svar frå " + fritekst("Land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("Nasjonalitet") + " styresmakter." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Denne retten har " + fritekst("land") + " etter EØS-forordningen 987/2009 artikkel 72." },
                        nynorsk { + "Denne retten har " + fritekst("Land") + " etter EØS-forordninga 987/2009 artikkel 72." },
                    )
                }
            }
            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.nyeAvslagBarnetillegg))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage)
            showIf(bostedutland){
                paragraph {
                    text (
                        bokmal { + "Du kan sende klagen direkte til Nav eller gjennom " + fritekst("utenlandsk trygdemyndighet") + "." },
                        nynorsk { + "Du kan sende klaga direkte til Nav eller gjennom " + fritekst("Utenlandsk trygdemyndighet") + "." },
                    )
                }
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(!bostedutland))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
