package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.nyeAvslagBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.nyeInnvilgedeBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.oifuVedVirkningstidspunkt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.SaksbehandlervalgSelectors.barnetilleggInfo
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.SaksbehandlervalgSelectors.refusjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERE_SOK_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
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
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

@TemplateModelHelpers
object InnvilgelseUforetrygd : RedigerbarTemplate<InnvilgelseUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            showIf(pesysData.pe.vedtaksdata_kravhode_kravgjelder().equalTo("mellombh")){
                text(
                    bokmal { +"Nav har innvilget forskudd på søknaden din om uføretrygd" },
                    nynorsk { +"Nav har innvilga forskot på søknaden din om uføretrygd" },
                )
            }.orShow {
                text(
                    bokmal { +"Nav har innvilget søknaden din om uføretrygd" },
                    nynorsk { +"Nav har innvilget søknaden din om uføretrygd " },
                )
            }

        }
        outline {
            val pe = pesysData.pe

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
            val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(LocalDate.now())
            val virkningbegrunnelseStdbegr_22_12_1_5 = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse().equalTo("stdbegr_22_12_1_5")
            val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()

            val btSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            val btFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
            val btSerkullNetto0 = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
            val btFellesNetto0 = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)

            val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
            val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
            val txtParagraf_22_12_eller_22_13 = ifElse (virkningbegrunnelseStdbegr_22_12_1_5,"22-13", "22-12")
            val txtOvergangsregler2016Bokmal = ifElse (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016"), " og forskrift om overgangsregler for barnetillegg i uføretrygden", "")
            val txtOvergangsregler2016Nynorsk = ifElse (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().equalTo("overgangsregler_2016")," og forskrift om overgangsreglar for barnetillegg i uføretrygda", "")
            val txtOgEllerEktefelle = ifElse (ektefelletilleggInnvilget, " og/eller ektefelle", "")

            val ungUforResultat = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()
            val kravarsak = pe.vedtaksdata_kravhode_kravarsaktype()
            val avtaletypeEos = pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaletype().equalTo("eos_nor")

            val beregningsvilkarYrkesskadegrad = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
            val beregnetYrkesskadegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
            val yrkesskadeResultat = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()

            val beregningsvilkarUforegrad = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()

            val instoppholdType = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()
            val instoppholdAnvendt = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()

            val totalNettoUforeberegning = pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()

            val kravGjelder = pe.vedtaksdata_kravhode_kravgjelder()
            val bosattUtland = kravGjelder.equalTo("f_bh_bo_utl")
            val oppfyltvedsammenlegging = pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()
            val txtEllerEtAvtaleland = ifElse(bosattUtland and oppfyltvedsammenlegging, " eller et avtaleland", "")
            val trygdeavtale = fritekst("sett inn rett trygdeavtale")
            val txtEosAvtale = " og EØS-avtalens bestemmelser om trygd i forordning 883/2004 artikkel 6, artikkel 7 og artikkel 45"

            val ifuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
            val ifuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
            val ieuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
            val ieuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()

            val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
            val oifuMerEnnIfu = oifuKroner.greaterThan(ifuInntekt)

            showIf((ungUforResultat.notEqualTo("oppfylt") and (kravarsak.notEqualTo("omgj_etter_klage") and kravarsak.notEqualTo("omgj_etter_anke")))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du får " + uforegrad.format() + " prosent uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd, som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du får " + uforegrad.format() + " prosent uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt") and (kravarsak.notEqualTo("omgj_etter_klage") and kravarsak.notEqualTo("omgj_etter_anke")))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du får " + uforegrad.format() + " prosent uføretrygd med rettighet som ung ufør fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd, som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du får " + uforegrad.format() + " prosent uføretrygd med rett som ung ufør frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf((ungUforResultat.notEqualTo("oppfylt") and (kravarsak.equalTo("omgj_etter_klage") or kravarsak.equalTo("omgj_etter_anke")))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og du får " + uforegrad.format() + " prosent uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga di, og du får " + uforegrad.format() + " prosent uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt") and (kravarsak.equalTo("omgj_etter_klage") or kravarsak.equalTo("omgj_etter_anke")))){
                paragraph {
                    text (
                        bokmal { + "Vi har innvilget søknaden din om uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhold i klagen din, og du får " + uforegrad.format() + " prosent uføretrygd med rettighet som ung ufør fra " + pe.vedtaksdata_virkningfom().format() + "." },
                        nynorsk { + "Vi har innvilga søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Du har fått medhald i klaga din, og du får " + uforegrad.format() + " prosent uføretrygd med rett som ung ufør frå " + pe.vedtaksdata_virkningfom().format() + "." },
                    )
                }
            }

            showIf(kravGjelder.equalTo("mellombh")){
                paragraph {
                    text(
                        bokmal {
                            +"Vi viser til vedtak fra " + fritekst("vedtaksdato") + " om foreløpig avslag på søknaden din om uføretrygd. Vi har innvilget søknaden din ut fra opplysninger vi har fått fra " +
                                    fritekst("land") + ". Du får " + uforegrad.format() + " prosent uføretrygd fra " + virkningstidpunkt.format() + "."
                               },
                        nynorsk {
                            +"Vi viser til vedtak frå " + fritekst("vedtaksdato") + " om førebels avslag på søknaden din om uføretrygd. Vi har innvilga søknaden din ut ifrå opplysningar vi har fått frå " +
                                    fritekst("land") + ". Du får " + uforegrad.format() + " prosent uføretrygd frå " + virkningstidpunkt.format() + "."
                        },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).equalTo((beregningsvilkarUforegrad)))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and ((yrkesskadeResultat).equalTo("oppfylt") or (yrkesskadeResultat).equalTo("ikke_oppfylt")))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + beregningsvilkarYrkesskadegrad.format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at " + beregningsvilkarYrkesskadegrad.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).equalTo("ikke_oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at uførheten din ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
                        nynorsk { + "Vi har kome fram til at uførleiken din ikkje kjem av ein godkjend yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("ikke_oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at du ikke har rettighet som ung ufør." },
                        nynorsk { + "Vi har kome fram til at du ikkje har rett som ung ufør." },
                    )
                }
            }

            showIf((pesysData.nyeInnvilgedeBarnetillegg.isNotEmpty())){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget barnetillegg i uføretrygden din for" },
                        nynorsk { + "Du er innvilga barnetillegg i uføretrygda di for" },
                    )
                    includePhrase(Felles.TextOrList(pesysData.nyeInnvilgedeBarnetillegg.map(BarnetilleggFormatter), 0))

                    showIf(btFellesInnvilget and btFellesNetto0 and (not(btSerkullInnvilget) or btSerkullNetto0)){
                        text (
                            bokmal { + " Tillegget blir ikke utbetalt fordi inntekten til deg og din ektefelle er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { + "Tillegget blir ikkje utbetalt fordi inntekta di og inntekta til ektefellen din er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }

                    showIf((btSerkullInnvilget and btSerkullNetto0 and not(btFellesInnvilget))){
                        text (
                            bokmal { + " Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
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

            showIf((instoppholdType.equalTo("reduksjon_hs") and instoppholdAnvendt)){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er innlagt på institusjon." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er innlagd på institusjon." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_hs") and not(instoppholdAnvendt))){
                paragraph {
                    text (
                        bokmal { + "Du er innlagt på institusjon, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er innlagd på institusjon, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_fo") and instoppholdAnvendt)){
                paragraph {
                    text (
                        bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er under straffegjennomføring." },
                        nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_fo") and not(instoppholdAnvendt))){
                paragraph {
                    text (
                        bokmal { + "Du er under straffegjennomføring, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
                        nynorsk { + "Du er under straffegjennomføring, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
                    )
                }
            }

            showIf((not(ektefelletilleggInnvilget) and not(btSerkullInnvilget) and not(btFellesInnvilget) and not(gjenlevendetilleggInnvilget) and not(instoppholdAnvendt))){
                paragraph {
                    text(
                        bokmal { +"Du får " + totalNettoUforeberegning.format() + " i uføretrygd per måned før skatt." },
                        nynorsk { +"Du får " + totalNettoUforeberegning.format() + " i uføretrygd per månad før skatt." },
                    )
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and not(ektefelletilleggInnvilget) and not(gjenlevendetilleggInnvilget) and not(instoppholdAnvendt))){
                paragraph {
                    text (
                        bokmal { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd og barnetillegg per måned før skatt." },
                        nynorsk { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd og barnetillegg per månad før skatt." },
                    )
                }
            }

            showIf((not(ektefelletilleggInnvilget) and not(btSerkullInnvilget) and not(btFellesInnvilget) and gjenlevendetilleggInnvilget and not(instoppholdAnvendt))){
                paragraph {
                    text (
                        bokmal { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd og gjenlevendetillegg per måned før skatt." },
                        nynorsk { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and not(instoppholdAnvendt))){
                paragraph {
                    text (
                        bokmal { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                        nynorsk { + "Du får " + totalNettoUforeberegning.format() + " i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and instoppholdAnvendt)){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_hs") and pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().notEqualTo(totalNettoUforeberegning) and instoppholdAnvendt)){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + totalNettoUforeberegning.format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + totalNettoUforeberegning.format() + " kroner." },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and instoppholdAnvendt)){
                paragraph {
                    text (
                        bokmal { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + totalNettoUforeberegning.format() + " kroner." },
                        nynorsk { + "Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + totalNettoUforeberegning.format() + " kroner." },
                    )
                }
            }

            showIf(bosattUtland) {
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling så snart som mulig." },
                        nynorsk { + "Uføretrygda blir betalt ut seinast den 20. kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di så snart som mogleg." },
                    )
                }
            }.orShow {
                paragraph {
                    text (
                        bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { + "Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di i " + fritekst("Månad og år") + "." },
                    )
                }
            }

            showIf(kravGjelder.equalTo("f_bh_med_utl")){
                title1 {
                    text (
                        bokmal { + "Dette er en foreløpig beregning" },
                        nynorsk { + "Dette er ei førebels berekning" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Fordi du har jobbet eller bodd i et annet EØS-land, er dette en foreløpig beregning basert på trygdetiden du har opparbeidet deg i Norge. Vi venter på informasjon fra " + fritekst("land") + ". Når vi har fått den informasjonen vi trenger, sender vi deg et vedtak med en endelig beregning av uføretrygden din. Der vil du se den totale summen du får utbetalt fra både Norge og eventuelt " + fritekst("land") + "." },
                        nynorsk { + "Fordi du har jobba eller budd i eit anna EØS-land, er dette ei førebels berekning basert på trygdetida du har opparbeidd deg i Noreg. Vi ventar på informasjon frå " + fritekst("land") + ". Når vi har fått den informasjonen vi treng, sender vi deg eit vedtak med ei endeleg berekning av uføretrygda di. Der vil du sjå den totale summen du får betalt ut frå både Noreg og eventuelt " + fritekst("land") + "." },
                    )
                }
            }

            showIf(kravGjelder.equalTo("mellombh")){
                title1 {
                    text (
                        bokmal { + "Dette er et forskudd" },
                        nynorsk { + "Dette er eit forskot" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Forskuddet ditt tilsvarer uføretrygd beregnet etter " + fritekst("navn trygdeavtale") + " på bakgrunn av norsk trygdetid og opplysninger om trygdetid fra " + fritekst("land") + ". Vi utbetaler dette forskuddet fram til vi har foretatt en endelig beregning av uføretrygden din. Det betyr at beløpet kan bli endret når det endelige vedtaket er klart." },
                        nynorsk { + "Forskotet ditt svarer til uføretrygd rekna ut etter " + fritekst("navn trygdeavtale") + " på bakgrunn av norsk trygdetid og opplysningar om trygdetid frå " + fritekst("land") + ". Vi betaler ut dette forskotet fram til vi har rekna ut uføretrygda di endeleg. Det vil seie at beløpet kan bli endra når det endelege vedtaket er klart." },
                    )
                }
            }

            showIf((kravGjelder.notEqualTo("mellombh"))){
                title1 {
                    text (
                        bokmal { + "Begrunnelse for vedtaket" },
                        nynorsk { + "Grunngiving for vedtaket" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "For å ha rett til uføretrygd må du oppfylle disse vilkårene:" },
                        nynorsk { + "For å ha rett til uføretrygd må du oppfylle desse vilkåra:" },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Du må være mellom 18 og 67 år." },
                                nynorsk { +"Du må vere mellom 18 og 67 år." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Du må ha vært medlem av folketrygden" + txtEllerEtAvtaleland + " i de siste " + pe.aars_trygdetid() + " årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene." },
                                nynorsk { +"Du må ha vore medlem av folketrygda" + txtEllerEtAvtaleland + " i dei siste " + pe.aars_trygdetid() + " åra fram til uføretidspunktet, eller oppfylle ein av unntaksreglane." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Inntektsevnen din må være varig nedsatt med minst 50 prosent på grunn av sykdom og/eller skade, eller du må oppfylle en av unntaksreglene." },
                                nynorsk { +"Inntektsevna di må vere varig sett ned med minst 50 prosent på grunn av sjukdom og/eller skade, eller oppfylle ein av unntaksreglane." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Sykdommen eller skaden din må være hovedårsak til din nedsatte inntektsevne." },
                                nynorsk { +"Sjukdommen eller skaden din må vere hovudårsaka til at inntektsevna di er sett ned." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Du må ha gjennomført hensiktsmessig behandling og arbeidsrettede tiltak." },
                                nynorsk { +"Du må ha gjennomført formålstenleg behandling og arbeidsretta tiltak." },
                            )
                        }
                    }
                }

                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at du oppfyller vilkårene for å få uføretrygd." },
                        nynorsk { + "Vi har kome fram til at du oppfyller vilkåra for å få uføretrygd." },
                    )
                }
            }

            showIf(kravGjelder.equalTo("mellombh")){
                title1 {
                    text (
                        bokmal { + "Begrunnelse for vedtaket" },
                        nynorsk { + "Grunngiving for vedtaket" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi kan på grunnlag av bestemmelsene i " + fritekst("Trygdeavtale") + " gi unntak fra folketrygdlovens vilkår om medlemskap fram til uføretidspunktet, ved å legge sammen perioder med medlemskap i folketrygden og medlemskap i et annet land Norge har trygdeavtale med." },
                        nynorsk { + "Vi kan på grunnlag av reglane i " + fritekst("Trygdeavtale") + " gi unntak frå vilkåra i folketrygdlova om medlemskap fram til uføretidspunktet, ved å leggje saman periodar med medlemstid i Noreg og medlemstid i eit anna land Noreg har trygdeavtale med." },
                    )
                }
                ifNotNull(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom()) { trygdetidfom ->
                    ifNotNull(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidtom()) { trygdetidtom ->
                        paragraph {
                            text(
                                bokmal {+"Du har vært medlem av folketrygden fra " + trygdetidfom.format() + " til " + trygdetidtom.format() + ". " +
                                        "Vi har fått opplyst at du har vært medlem av den " + fritekst("nasjonalitet") + " trygdeordningen fra " + fritekst("fom") + " til " + fritekst("tom") + ". " +
                                        "Uføretidspunktet ditt er satt til " + uforetidspunkt.format() + ". " +
                                        "Du har derfor vært medlem av folketrygden og den " + fritekst("nasjonalitet") + " trygdeordningen sammenhengende i fem år eller mer fram til uføretidspunktet ditt. Fordi vi har lagt sammen perioder med medlemskap i folketrygden og i " + fritekst("land") + ", får du unntak fra vilkåret om medlemskap i folketrygden."
                                },
                                nynorsk {+"Du har vore medlem av den norske folketrygda frå " + trygdetidfom.format() + " til " + trygdetidtom.format() + ". " +
                                        "Vi har fått opplyst at du har vore medlem av den " + fritekst("Nasjonalitet") + " trygdeordninga frå " + fritekst("fom") + " til " + fritekst("tom") + ". " +
                                        "Uføretidspunktet ditt er sett til " + uforetidspunkt.format() + ". " +
                                        "Du har derfor vore medlem av den norske folketrygda og den " + fritekst("Nasjonalitet") + " trygdeordninga samanhengande i fem år eller meir fram til uføretidspunktet ditt. Fordi vi har lagt saman periodar med medlemstid i Noreg og i " + fritekst("land") + ", får du unntak frå vilkåret om medlemskap i folketrygda."
                                },
                            )
                        }
                    }
                }
                paragraph {
                    text (
                        bokmal { + "Vedtaket er gjort etter EØS-avtalen artikkel 7 i forordning 883/2004 og folketrygdloven § 12-2." },
                        nynorsk { + "Vedtaket er gjort etter EØS-avtalen artikkel 7 i forordning 883/2004 og folketrygdlova § 12-2." },
                    )
                }
            }

            showIf(kravarsak.equalTo("omgj_etter_klage")){
                paragraph {
                    text (
                        bokmal { + "Søknaden din er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { + "Søknaden din er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget)) and ((yrkesskadeResultat).equalTo("ikke_oppfylt") or (yrkesskadeResultat).equalTo("oppfylt")) and (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")) and not(gjenlevendetilleggInnvilget)){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((not(btSerkullInnvilget) and not(btFellesInnvilget)) and (yrkesskadeResultat).equalTo("ikke_vurdert") and not(gjenlevendetilleggInnvilget) and (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and
                    (yrkesskadeResultat).equalTo("ikke_vurdert") and
                    not(gjenlevendetilleggInnvilget) and
                    (instoppholdType.notEqualTo("reduksjon_hs")
                            and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and
                    ((yrkesskadeResultat).equalTo("ikke_oppfylt") or (yrkesskadeResultat).equalTo("oppfylt")) and
            (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((not(btSerkullInnvilget) and not(btFellesInnvilget) and (yrkesskadeResultat).equalTo("ikke_vurdert") and gjenlevendetilleggInnvilget and (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((not(btSerkullInnvilget) and not(btFellesInnvilget)) and ((yrkesskadeResultat).equalTo("ikke_oppfylt") or (yrkesskadeResultat).equalTo("oppfylt")) and gjenlevendetilleggInnvilget and (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-18 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-18 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-18 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-18 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and (yrkesskadeResultat).equalTo("ikke_vurdert") and gjenlevendetilleggInnvilget and (instoppholdType.notEqualTo("reduksjon_hs") and instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and
                    ((yrkesskadeResultat).equalTo("ikke_oppfylt") or (yrkesskadeResultat).equalTo("oppfylt")) and
                    gjenlevendetilleggInnvilget and (instoppholdType.notEqualTo("reduksjon_hs") and
                    instoppholdType.notEqualTo("reduksjon_fo")))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-18 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 og 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 og 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 og 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 og 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and not(gjenlevendetilleggInnvilget) and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17 til 12-19 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf((not(btFellesInnvilget) and not(btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-18 og 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-18 og 12-20 og " + txtParagraf_22_12_eller_22_13 + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-14, 12-17, 12-18 og 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-14, 12-17, 12-18 og 12-20 og " + txtParagraf_22_12_eller_22_13 + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.equalTo(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-16, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-16, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf(((btFellesInnvilget or btSerkullInnvilget) and gjenlevendetilleggInnvilget and beregnetYrkesskadegrad.greaterThan(0) and instoppholdType.equalTo("reduksjon_fo"))){
                paragraph {
                    showIf(bosattUtland) {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + " " + trygdeavtale + txtEosAvtale + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + " " + trygdeavtale + txtEosAvtale + "." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-2 til 12-17, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Bokmal + "." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-2 til 12-17, 12-18, 12-20 og " + txtParagraf_22_12_eller_22_13 + txtOvergangsregler2016Nynorsk + "." },
                        )
                    }
                }
            }

            showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()).equalTo("stdbegr_12_7_2_o_2")) or ((beregningsvilkarUforegrad).lessThan(50) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()).equalTo("stdbegr_12_7_2_o_3")))){
                title1 {
                    text (
                        bokmal { + "Du oppfyller unntaksregel om nedsatt inntektsevne" },
                        nynorsk { + "Du oppfyller unntaksregel om nedsett inntektsevne" },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()).equalTo("stdbegr_12_7_2_o_2"))){
                paragraph {
                    text (
                        bokmal { + "Du mottok arbeidsavklaringspenger da du søkte om uføretrygd. Det er derfor tilstrekkelig at inntektsevnen din er varig nedsatt med minst 40 prosent." },
                        nynorsk { + "Du fekk arbeidsavklaringspengar då du søkte om uføretrygd. Det er derfor tilstrekkeleg at inntektsevna di er varig sett ned med minst 40 prosent." },
                    )
                }
            }

            showIf(((beregningsvilkarUforegrad).lessThan(50) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()).equalTo("stdbegr_12_7_2_o_3"))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom. Det er derfor tilstrekkelig at inntektsevnen din er varig nedsatt med minst 30 prosent." },
                        nynorsk { + "Du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom. Det er derfor tilstrekkeleg at inntektsevna di er varig sett ned med minst 30 prosent." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt") or ungUforResultat.equalTo("ikke_oppfylt"))){
                title1 {
                    text (
                        bokmal { + "Rettighet som ung ufør" },
                        nynorsk { + "Rett som ung ufør" },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt"))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget rettighet som ung ufør fordi du ble alvorlig og varig syk før du fylte 26 år. Dette betyr at uføretrygden din vil bli beregnet etter regler som gjelder ung ufør." },
                        nynorsk { + "Du er innvilga rett som ung ufør fordi du blei alvorleg og varig sjuk før du fylte 26 år. Dette betyr at uføretrygda di vil bli rekna ut etter reglar som gjeld ung ufør." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt"))){
                includePhrase(TBU1133_Generated)
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse()).equalTo("stdbegr_12_13_1_i_1"))){
                paragraph {
                    text(
                        bokmal { +"For å bli innvilget rettighet som ung ufør, er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert." },
                        nynorsk { +"For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade, som er klart dokumentert." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du oppfyller ikke vilkåret om rettighet som ung ufør, fordi det ikke er dokumentert at du ble alvorlig og varig syk før du fylte 26 år. " + fritekst("Konkret begrunnelse") },
                        nynorsk { + "Du oppfyller ikkje vilkåret om rett som ung ufør fordi det ikkje er dokumentert at du blei alvorleg og varig sjuk før du fylte 26 år. " + fritekst("Konkret begrunnelse") },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse()).equalTo("stdbegr_12_13_1_i_2"))){
                paragraph {
                    text (
                        bokmal { + "For å bli innvilget rettighet som ung ufør er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert." },
                        nynorsk { + "For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade, som er klart dokumentert." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du var over 26 år på uføretidspunktet og kan derfor ikke innvilges rettighet som ung ufør." },
                        nynorsk { + "Du var over 26 år på uføretidspunktet, og vi kan derfor ikkje innvilge deg rett som ung ufør." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse()).equalTo("stdbegr_12_13_1_i_3"))){
                paragraph {
                    text (
                        bokmal { + "For å bli innvilget rettighet som ung ufør er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert. Dersom du har vært mer enn 50 prosent yrkesaktiv etter at du fylte 26 år, må du ha søkt om uføretrygd før du fyller 36 år." },
                        nynorsk { + "For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade som er klart dokumentert. Dersom du har vore meir enn 50 prosent yrkesaktiv etter at du fylte 26 år, må du ha søkt om uføretrygd før du fyller 36 år." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du har jobbet mer enn 50 prosent over en lengre periode etter at du fylte 26 år. Du søkte om uføretrygd etter at du fylte 36 år og oppfyller derfor ikke dette vilkåret." },
                        nynorsk { + "Du har arbeidd i meir enn 50 prosent over ein lengre periode etter at du fylte 26 år. Du søkte om uføretrygd etter at du fylte 36 år og oppfyller derfor ikkje dette vilkåret." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_unntakfraforutgaendemedlemskap()))){
                title1 {
                    text (
                        bokmal { + "Du oppfyller unntaksregel om medlemskap" },
                        nynorsk { + "Du oppfyller unntaksregel om medlemskap" },
                    )
                }
            }

            showIf(((pe.grunnlag_persongrunnlagsliste_brukerflyktning()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_unntakfraforutgaendemedlemskap()))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget flyktningstatus fra Utlendingsdirektoratet, og oppfyller derfor vilkåret om medlemskap i folketrygden. " },
                        nynorsk { + "Du er innvilga flyktningstatus frå Utlendingsdirektoratet, og oppfyller derfor vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).equalTo("oppfylt") and not((pe.grunnlag_persongrunnlagsliste_brukerflyktning())) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_unntakfraforutgaendemedlemskap()))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom, og oppfyller derfor vilkåret om medlemskap i folketrygden." },
                        nynorsk { + "Du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom, og oppfyller derfor vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf((ungUforResultat.equalTo("oppfylt") and (yrkesskadeResultat).notEqualTo("oppfylt") and not((pe.grunnlag_persongrunnlagsliste_brukerflyktning())) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_unntakfraforutgaendemedlemskap()))){
                paragraph {
                    text (
                        bokmal { + "Du ble ufør før du fylte 26 år. Du var medlem i folketrygden, og hadde vært det i minst ett år før du søkte om uføretrygd. Du oppfyller derfor vilkåret om medlemskap i folketrygden." },
                        nynorsk { + "Du blei ufør før du fylte 26 år. Du var medlem i folketrygda og hadde vore det i minst eitt år før du søkte om uføretrygd. Du oppfyller derfor vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf(pe.vedtaksbrev_vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_inngangunntak().equalTo("mindre5ar_uforep")){
                paragraph {
                    text (
                        bokmal { + "Du var medlem i folketrygden i minst ett år før du søkte om uføretrygd. Da hadde du vært medlem siden du var 16 år og fram til uføretidspunktet, men ikke bodd i utlandet i mer enn fem år. Du oppfyller derfor vilkåret om medlemskap i folketrygden." },
                        nynorsk { + "Du var medlem i folketrygda i minst eitt år før du søkte om uføretrygd. Då hadde du vore medlem sidan du var 16 år og fram til uføretidspunktet, men ikkje budd i utlandet i meir enn fem år. Du oppfyller derfor vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf(pe.vedtaksbrev_vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_inngangunntak().equalTo("halv_minpen_ufp_ut")){
                paragraph {
                    text (
                        bokmal { + "Du var medlem i folketrygden på uføretidspunktet og har tjent opp rett til minst halvparten av full minsteytelse for uføretrygd. Du oppfyller derfor vilkåret om medlemskap i folketrygden." },
                        nynorsk { + "Du var medlem i folketrygda på uføretidspunktet og har tent opp rett til minst halvparten av full minsteyting for uføretrygd. Du oppfyller derfor vilkåret om medlemskap i folketrygda." },
                    )
                }
            }

            showIf((bosattUtland and oppfyltvedsammenlegging)){
                title1 {
                    text (
                        bokmal { +"Du oppfyller vilkår om medlemskap gjennom sammenlegging"},
                        nynorsk { +"Du oppfyller vilkår om medlemskap gjennom samanslåing" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Et vilkår for rett til uføretrygd er at du har vært medlem i folketrygden de siste " + pe.aars_trygdetid() + " årene fram til uføretidspunktet. Dette vilkåret kan oppfylles ved å regne med trygdeperioder i andre EØS-land. Du fyller dette vilkåret gjennom sammenlegging med " + fritekst("land") + " tid." },
                        nynorsk { + "Eit vilkår for rett til uføretrygd er at du har vore medlem i folketrygda dei siste " + pe.aars_trygdetid() + " åra fram til uføretidspunktet. Dette vilkåret kan oppfyllast ved å rekne med trygdeperiodar i andre EØS-land. Du fyller dette vilkåret gjennom samanslåing med " + fritekst("land") + " tid." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).notEqualTo("ikke_vurdert"))){
                title1 {
                    text (
                        bokmal { + "Uførhet som skyldes yrkesskade eller yrkessykdom" },
                        nynorsk { + "Uførleik som kjem av yrkesskade eller yrkessjukdom" },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).equalTo((beregningsvilkarUforegrad)))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom." },
                        nynorsk { + "Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).equalTo((beregningsvilkarUforegrad)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest())){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar som gir deg ei høgare uføretrygd." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).equalTo((beregningsvilkarUforegrad)) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()))){
                paragraph {
                    text (
                        bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar, dersom dette er til fordel for deg." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).equalTo((beregningsvilkarUforegrad)) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).greaterThan(0) and
                    (beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and
                    ((yrkesskadeResultat).equalTo("oppfylt")))){
                paragraph {
                    text (
                        bokmal { + "Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen er årsak til uførheten din." },
                        nynorsk { + "Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut i frå opplysningar i saka di vurdert at også andre sjukdomsforhold er årsak til uførleiken din." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).equalTo("oppfylt") and (beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and (beregningsvilkarYrkesskadegrad).greaterThan(0))){
                paragraph {
                    text (
                        bokmal { + "Vi har kommet fram til at " + beregningsvilkarYrkesskadegrad.format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") + "." },
                        nynorsk { + "Vi har kome fram til at " + beregningsvilkarYrkesskadegrad.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") + "." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest())){
                paragraph {
                    text (
                        bokmal { + "Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                        nynorsk { + "Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()))){
                paragraph {
                    text (
                        bokmal { + "Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                        nynorsk { + "Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                    )
                }
            }

            showIf(((beregningsvilkarYrkesskadegrad).lessThan((beregningsvilkarUforegrad)) and (beregningsvilkarYrkesskadegrad).greaterThan(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                        nynorsk { + "Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).equalTo("oppfylt"))) {
                ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->
                    paragraph {
                        text(
                            bokmal {+"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner."},
                            nynorsk {+"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner."},
                        )
                    }
                }
            }

            showIf(((yrkesskadeResultat).equalTo("ikke_oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_i_2"))){
                paragraph {
                    text (
                        bokmal { + "Du er ikke innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom, fordi vi har vurdert at andre medisinske årsaker har ført til din varige nedsatte inntektsevne " + fritekst("konkret begrunnelse") + "." },
                        nynorsk { + "Du er ikkje innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom fordi vi har vurdert at andre medisinske årsaker har ført til at du har varig nedsett inntektsevne " + fritekst("konkret begrunnelse") + "." },
                    )
                }
            }

            showIf(((yrkesskadeResultat).equalTo("ikke_oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_1_i_1"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke en godkjent yrkesskade eller yrkessykdom. Uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom. " + fritekst("Konkret begrunnelse") +"." },
                        nynorsk { + "Du har ikkje ein godkjend yrkesskade eller yrkessjukdom. Uføretrygda di blir derfor ikkje innvilga etter reglar for yrkesskade eller yrkessjukdom. " + fritekst("Konkret begrunnelse") +"." },
                    )
                }
            }

            showIf(bosattUtland){
                title1 {
                    text (
                        bokmal { + "Uføretrygd for deg som er bosatt i utlandet" },
                        nynorsk { + "Uføretrygd for deg som er busett i utlandet" },
                    )
                }
                showIf( oppfyltvedsammenlegging) {
                    paragraph {
                        text(
                            bokmal { +"Du må som hovedregel være bosatt i Norge for å ha rett til uføretrygd. Etter reglene i " + fritekst("trygdeavtale") + " mellom Norge og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_bostedslandbeskrivelse() + " kan imidlertid uføretrygden din utbetales helt eller delvis selv om du ikke bor i Norge." },
                            nynorsk { +"Du må som hovudregel vere busett i Noreg for å ha rett til uføretrygd. Etter reglane i " + fritekst("Trygdeavtale") + " mellom Noreg og " + pe.grunnlag_persongrunnlagsliste_trygdeavtaler_bostedslandbeskrivelse() + " kan likevel uføretrygda di betalast ut heilt eller delvis sjølv om du ikkje bur i Noreg." },
                        )
                    }
                }

                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemskap_minst20arbotid()) {
                    paragraph {
                        text(
                            bokmal { +"Du har rett til uføretrygd fordi du har minst 20 års samlet botid i Norge i perioden etter fylte 16 år." },
                            nynorsk { +"Du har rett til uføretrygd fordi du har minst 20 års samla butid i Noreg i perioden etter at du fylte 16 år." },
                        )
                    }
                }

                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskap_minsttrearsfmnorge()){
                    paragraph {
                        text (
                            bokmal { + "Du har rett til uføretrygd fordi du har hatt minst ett år med inntekt i Norge over folketrygdens grunnbeløp." },
                            nynorsk { + "Du har rett til uføretrygd fordi du har hatt minst ett år med inntekt i Noreg over grunnbeløpet i folketrygda." },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er beregnet på grunnlag av at du er bosatt i " + fritekst("land") + ". Hvis du flytter til et annet land må du gi beskjed til Nav. Uføretrygden din kan da bli beregnet på nytt." },
                        nynorsk { +"Uføretrygda di er rekna ut på grunnlag av at du er busett i " + fritekst("land") + ". Viss du flyttar til eit anna land, må du melde frå til Nav. Uføretrygda di kan då bli rekna ut på nytt." },
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
                ifNotNull (pe.vedtaksdata_kravhode_onsketvirkningsdato()) { virkningsdato ->
                    paragraph {
                        text(
                            bokmal {+"Du har fått innvilget uføretrygd fra " + virkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger."},
                            nynorsk {+"Du har fått innvilga uføretrygd frå " + virkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar."},
                        )
                    }
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_2"))){
                ifNotNull (pe.vedtaksdata_kravhode_onsketvirkningsdato()) { virkningsdato ->
                    paragraph {
                        text(
                            bokmal {+"Du har fått innvilget uføretrygd fra " + virkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til " + fritekst("dato for opphør") + ", og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "."},
                            nynorsk {+"Du har fått innvilga uføretrygd frå " + virkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til " + fritekst("dato for opphør") + ", og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "."},
                        )
                    }
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_3"))){
                ifNotNull (pe.vedtaksdata_kravhode_onsketvirkningsdato()) { virkningsdato ->
                    paragraph {
                        text(
                            bokmal { +"Du har fått innvilget uføretrygd fra " + virkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til " + fritekst("dato for opphør") + ". I denne måneden får du utbetalt den delen av sykepengene som overstiger uføretrygden." },
                            nynorsk { +"Du har fått innvilga uføretrygd frå " + virkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til " + fritekst("dato for opphør") + ". I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                        )
                    }
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_4"))){
                ifNotNull (pe.vedtaksdata_kravhode_onsketvirkningsdato()) { virkningsdato ->
                    paragraph {
                        text (
                            bokmal { + "Du har fått innvilget uføretrygd fra " + virkningsdato.format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                            nynorsk { + "Du har fått innvilga uføretrygd frå " + virkningsdato.format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyljer 18 år." },
                        )
                    }
                }
            }

            showIf(virkningbegrunnelseStdbegr_22_12_1_5){
                ifNotNull (pe.vedtaksdata_kravhode_onsketvirkningsdato()) { virkningsdato ->
                    paragraph {
                        text(
                            bokmal {+"Du har fått innvilget uføretrygd fra " + virkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Vi mottok søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen. "},
                            nynorsk {+"Du har fått innvilga uføretrygd frå " + virkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Vi fekk søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd opptil tre månader før denne datoen. "},
                        )
                    }
                }
            }

            showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()).equalTo("stdbegr_22_12_1_13"))){
                paragraph {
                    text (
                        bokmal { + "Du har fått innvilget uføretrygd fra " + pe.vedtaksdata_virkningfom().format() + ". Dette kaller vi virkningstidspunktet. Du har rett til uføretrygd fra den måneden vilkårene er oppfylt." },
                        nynorsk { + "Du har fått innvilga uføretrygd frå " + pe.vedtaksdata_virkningfom().format() + ". Dette kallar vi verknadstidpunktet. Du har rett til uføretrygd frå den månaden vilkåra er oppfylde." },
                    )
                }
            }

            showIf((kravGjelder.notEqualTo("mellombh"))){
                title1 {
                    text (
                        bokmal { + "Dette er uføretidspunktet ditt" },
                        nynorsk { + "Dette er uføretidspunktet ditt" },
                    )
                }

                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunktbegrunnelse().equalTo("stdbegr_12_7_1_1")){
                    paragraph {
                        text(
                            bokmal { +"Du ble ufør i " + uforetidspunkt.formatMonthYear() + ". Da ble inntektsevnen din varig nedsatt med minst halvparten." },
                            nynorsk { +"Du blei ufør i " + uforetidspunkt.formatMonthYear() + ". Då blei inntektsevna di varig sett ned med minst halvparten." },
                        )
                    }
                }.orShowIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunktbegrunnelse().equalTo("stdbegr_12_7_1_2")){
                    paragraph {
                        text(
                            bokmal {+"Du ble ufør i " + uforetidspunkt.formatMonthYear() + ". Da ble inntektsevnen din varig nedsatt med minst 40 prosent."},
                            nynorsk {+"Du blei ufør i " + uforetidspunkt.formatMonthYear() + ". Då blei inntektsevna di varig sett ned med minst 40 prosent."},
                        )
                    }
                }.orShowIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunktbegrunnelse().equalTo("stdbegr_12_7_1_3")){
                    paragraph {
                        text(
                            bokmal {+"Du ble ufør i " + uforetidspunkt.formatMonthYear() + ". Da ble inntektsevnen din varig nedsatt med minst 30 prosent."},
                            nynorsk {+"Du blei ufør i " + uforetidspunkt.formatMonthYear() + ". Då blei inntektsevna di varig sett ned med minst 30 prosent."},
                        )
                    }
                }
                paragraph {
                    text (
                        bokmal { + "Dette er uføretidspunktet ditt, og avgjør hvilke inntektsår vi legger til grunn for beregningen din." },
                        nynorsk { + "Dette er uføretidspunktet ditt, og det avgjer kva inntektsår vi legg til grunn for berekninga di." },
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

            showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_1") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + ifuInntekt.format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                        nynorsk { + "Inntekta di før du blei ufør er fastsett til " + ifuInntekt.format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ungUforResultat.equalTo("oppfylt")) and ((ifuBegrunnelse).equalTo("stdbegr_12_8_2_4")) and ((ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3")))){
                paragraph {
                    text (
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + ifuInntekt.format() + " kroner. Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                        nynorsk { + "Inntekta di før du blei ufør, er fastsett til " + ifuInntekt.format() + " kroner. Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_3") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + ifuInntekt.format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger folketrygdens grunnbeløp." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + ifuInntekt.format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_5") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + ifuInntekt.format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                        nynorsk { + "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + ifuInntekt.format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                    )
                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf((((ifuBegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and uforegrad.equalTo(100) and (ieuInntekt).equalTo(0) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + uforegrad.format() + " prosent." },
                        nynorsk { + "Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + uforegrad.format() + " prosent." },
                    )
                }
            }

            showIf((((ifuBegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and ((uforegrad.greaterThan(0) and uforegrad.lessThan(100)) or (uforegrad.equalTo(100) and (ieuInntekt).greaterThan(0))) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Du har en inntekt på " + ieuInntekt.format() + " kroner, og vi har derfor fastsatt uføregraden din til " + uforegrad.format() + " prosent." },
                        nynorsk { + "Du har ei inntekt på " + ieuInntekt.format() + " kroner, og vi har derfor fastsett uføregraden din til " + uforegrad.format() + " prosent." },
                    )
                }
            }

            showIf(((ieuBegrunnelse).equalTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + ifuInntekt.format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + ifuInntekt.format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                    )

                    showIf(oifuMerEnnIfu){
                        text (
                            bokmal { + " Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                            nynorsk { + " Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                        )
                    }
                }
            }

            showIf(((ieuBegrunnelse).equalTo("stdbegr_12_8_1_3"))){
                paragraph {
                    text (
                        bokmal { + "Det er dokumentert at du har inntektsmuligheter som du ikke benytter. Disse tar vi med når vi fastsetter inntekten din etter at du ble ufør. Inntekten din etter at du ble ufør er fastsatt til " + ieuInntekt.format() + " kroner, og uføregraden din er derfor fastsatt til " + uforegrad.format() + " prosent." },
                        nynorsk { + "Det er dokumentert at du har inntektsmoglegheiter som du ikkje nyttar. Desse tek vi med når vi fastset inntekta di etter at du blei ufør. Inntekta di etter at du blei ufør, er fastsett til " + ieuInntekt.format() + " kroner, og uføregraden din er derfor fastsett til " + uforegrad.format() + " prosent." },
                    )
                }
            }

            showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_2") or (ifuBegrunnelse).equalTo("stdbegr_12_8_2_9"))){
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt inntekten din før du ble ufør til " + ifuInntekt.format() + " kroner. Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + ieuInntekt.format() + " kroner. Inntekten din etter at du ble ufør er derfor fastsatt til " + ieuInntekt.format() + " kroner og uføregraden din blir " + uforegrad.format() + " prosent." },
                        nynorsk { + "Vi har fastsett inntekta di før du blei ufør til " + ifuInntekt.format() + " kroner. Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + ieuInntekt.format() + " kroner. Inntekta di etter at du blei ufør, er derfor fastsett til " + ieuInntekt.format() + " kroner, og uføregraden din blir " + uforegrad.format() + " prosent." },
                    )
                }
            }
            includePhrase(TBU1133_Generated)

            showIf((kravGjelder.equalTo("mellombh") or kravGjelder.equalTo("f_bh_med_utl"))){
                paragraph {
                    text (
                        bokmal { + "Dette skjer videre med søknaden din" },
                        nynorsk { + "Dette skjer vidare med søknaden din" },
                    )
                }
            }

            showIf((kravGjelder.equalTo("mellombh") or kravGjelder.equalTo("f_bh_med_utl"))){
                paragraph {
                    text (
                        bokmal { + "Når vi mottar vedtak fra " + fritekst("land") + ", vil vi fatte et vedtak med en endelig beregning. Du mottar da et samlet vedtak fra Norge og " + fritekst("land") + "." },
                        nynorsk { + "Når vi får vedtak frå " + fritekst("land") + ", kjem vi til å gjere eit vedtak med ei endeleg berekning. Du får då eit samla vedtak frå Noreg og " + fritekst("land") + "." },
                    )
                }
            }

            showIf(uforegrad.equalTo(100)){
                title1 {
                    text (
                        bokmal { + "Skal du kombinere uføretrygd og inntekt?" },
                        nynorsk { + "Skal du kombinere uføretrygd og inntekt?" },
                    )
                }
            }

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

           showIf((uforegrad.equalTo(100) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and ieuInntekt.equalTo(0))) {
               paragraph {
                   text(
                       bokmal { +"Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensen din." },
                       nynorsk { +"Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensa di." },
                   )
               }
           }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()))){
                paragraph {
                    text (
                        bokmal { + "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { + "Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda mens du er i varig tilrettelagt arbeid utan at uføretrygda di blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            showIf((((beregningsvilkarUforegrad).lessThan(100) and (beregningsvilkarUforegrad).greaterThan(0)) or ((ieuInntekt).greaterThan(0) and (beregningsvilkarUforegrad).equalTo(100)))){
                paragraph {
                    text (
                        bokmal { + "Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { + "Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
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

            showIf(bosattUtland){
                paragraph {
                    text (
                        bokmal { + "Inntektsgrensen gjelder bare for den norske uføretrygden din. Har du spørsmål om inntektsgrensen i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                        nynorsk { + "Inntektsgrensa gjeld berre for den norske uføretrygda di. Har du spørsmål om inntektsgrensa i eit anna land, må du kontakte trygdestyresmaktene i det landet det gjeld." },
                    )
                }
            }

            title1 {
                text (
                    bokmal { + "Du må melde fra hvis du har inntekt" },
                    nynorsk { + "Du må melde frå om du har inntekt" },
                )
            }
            paragraph {
                text (
                    bokmal { + "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din." },
                    nynorsk { + "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet " + quoted("uføretrygd") +" når du logger deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di." },
                )
            }

            showIf( bosattUtland){
                paragraph {
                    text (
                        bokmal { +"Hvis du ikke har mulighet til å logge deg på $NAV_URL, må du sende opplysninger om eventuell arbeidsinntekt i posten. Ved arbeid i andre land enn Norge, må du i tillegg sende oss skatteligning når denne er mottatt det påfølgende året."},
                        nynorsk { +"Dersom du ikkje har moglegheit til å logge deg på $NAV_URL, må du sende opplysningar om eventuell arbeidsinntekt i posten. Ved arbeid i andre land enn Noreg, må du i tillegg sende oss skatteligning når denne er mottatt det påfølgjande året." },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Adresse:" },
                        nynorsk { + "Adresse:" },
                    )
                    newline()
                    text (
                        bokmal { + "Nav Arbeid og ytelser Oslo" },
                        nynorsk { + "Nav Arbeid og ytelser Oslo" },
                    )
                    newline()
                    text (
                        bokmal { + "Postboks 6600 Etterstad," },
                        nynorsk { + "Postboks 6600 Etterstad," },
                    )
                    newline()
                    text (
                        bokmal { + "NO-0607 Oslo" },
                        nynorsk { + "NO-0607 Oslo" },
                    )
                }
            }

            showIf(instoppholdType.equalTo("reduksjon_hs")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_hs") and (instoppholdAnvendt or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt." },
                        nynorsk { + "Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + " Dersom du mottar " },
                            nynorsk { + "Dersom du får " },
                        )
                    }

                    showIf(ektefelletilleggInnvilget){
                        text (
                            bokmal { + "ektefelletillegg" },
                            nynorsk { + "ektefelletillegg" },
                        )
                    }

                    showIf(gjenlevendetilleggInnvilget){
                        text (
                            bokmal { + "gjenlevendetillegg" },
                            nynorsk { + "attlevandetillegg" },
                        )
                    }

                    showIf((ektefelletilleggInnvilget or gjenlevendetilleggInnvilget)){
                        text (
                            bokmal { + " vil dette tillegget også bli redusert." },
                            nynorsk { + " vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf((instoppholdType.equalTo("reduksjon_hs") and (instoppholdAnvendt or pe.ut_forsorgeransvar_ingen_er_false() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(Kroner(0))))){
                paragraph {
                    text (
                        bokmal { + "Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. " +
                                "Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din."},
                        nynorsk { + "Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. " +
                                "Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di."},
                    )
                }
            }

            showIf((not(instoppholdAnvendt) and pe.ut_forsorgeransvar_ingen_er_true() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { + "Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            showIf((pe.ut_forsorgeransvar_ingen_er_false() and instoppholdType.equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0))){
                paragraph {
                    text (
                        bokmal { + "Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((not(instoppholdAnvendt) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and instoppholdType.equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            showIf((instoppholdAnvendt and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().notEqualTo(0) and instoppholdType.equalTo("reduksjon_hs"))){
                paragraph {
                    text (
                        bokmal { + "Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + totalNettoUforeberegning.format() + " kroner." },
                        nynorsk { + "Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + totalNettoUforeberegning.format() + " kroner." },
                    )
                }
            }

            showIf((instoppholdAnvendt and instoppholdType.equalTo("reduksjon_hs") and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter().equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())){
                paragraph {
                    text (
                        bokmal { + "Du forsørger ikke barn" + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + totalNettoUforeberegning.format() + " kroner."},
                        nynorsk { + "Du forsørgjer ikkje barn" + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + totalNettoUforeberegning.format() + " kroner." },
                    )
                }
            }

            showIf(instoppholdType.equalTo("reduksjon_fo")){
                title1 {
                    text (
                        bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { + "Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                    )

                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + " Da du forsørger barn" },
                            nynorsk { + " Da du forsørgjer barn" },
                        )
                    }

                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and ektefelletilleggInnvilget)){
                        text (
                            bokmal { + " og/eller ektefelle" },
                            nynorsk { + " og/eller ektefelle" },
                        )
                    }

                    showIf((pe.ut_forsorgeransvar_siste_er_true())){
                        text (
                            bokmal { + ", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { + ", vil utbetalinga av uføretrygda di reduserast med 50 prosent." },
                        )
                    }
                    text (
                        bokmal { + " Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { + " Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    showIf(ektefelletilleggInnvilget){
                        text (
                            bokmal { + "Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                            nynorsk { + "Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert." },
                        )
                    }

                    showIf(gjenlevendetilleggInnvilget){
                        text (
                            bokmal { + "Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { + "Dersom du mottar attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf(((btSerkullInnvilget or btFellesInnvilget) and pe.vedtaksdata_kravhode_sokerbt())){
                title1 {
                    text(
                        bokmal { + "Slik påvirker inntekt barnetillegget ditt " },
                        nynorsk { + "Slik verkar inntekt inn på barnetillegget ditt" },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype().notEqualTo("overgangsregler_2016") and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().legacyGreaterThanOrEqual(LocalDate.of(2016,1,1)))){
                paragraph {
                    text (
                        bokmal { + "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir " },
                        nynorsk { + "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kroner. Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir " },
                    )

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner. " },
                            nynorsk { + "derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + " kroner.  " },
                        )
                    }

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))){
                        text (
                            bokmal { + "ikke utbetalt. " },
                            nynorsk { + "ikkje utbetalt. " },
                        )
                    }

                    showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().greaterThan(0))){
                        text (
                            bokmal { + "Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt." },
                            nynorsk { + "Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt." },
                        )
                    }
                }
            }

            showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))){

                showIf(((not(btFellesInnvilget) and btSerkullInnvilget))){
                    includePhrase(TBU2338_Generated(pe))
                }

                showIf(btFellesInnvilget){
                    includePhrase(TBU2339_Generated(pe))
                }

                showIf((btSerkullInnvilget or btFellesInnvilget)){
                    paragraph {
                        showIf(btFellesInnvilget){
                            text (
                                bokmal { + "Endringer i inntektene til deg og ektefellen, partneren eller samboeren din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                                nynorsk { + "Endringar i inntektene til deg og ektefella, partneren eller samboaren din kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                            )
                        }.orShow {
                            text (
                                bokmal { + "Endringer i inntekten din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                                nynorsk { + "Endringar i inntekta di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                            )
                        }
                    }
                }

                showIf(((btFellesInnvilget))){
                    paragraph {
                        text (
                            bokmal { + "Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til ektefellen, partneren eller samboeren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                            nynorsk { + "Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til ektefella, partnaren eller sambuaren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                        )

                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))){
                            text (
                                bokmal { + "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til ektefellen, partneren eller samboeren din. " },
                                nynorsk { + "Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til ektefella, partnaren eller sambuaren din. " },
                            )
                        }

                        showIf(((not(btSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))){
                            text (
                                bokmal { + "Til sammen er inntektene " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                nynorsk { + "Til saman er inntektene " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                            )
                        }

                        showIf(((not(btSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)))){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        showIf(((not(btSerkullInnvilget) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)))){
                            text (
                                bokmal { + "redusert. " },
                                nynorsk { + "redusert. " },
                            )
                        }

                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(btSerkullInnvilget)))){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0) and not(btSerkullInnvilget) and btFellesNetto0))){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            )
                        }
                    }
                }

                showIf((btSerkullInnvilget and not(btFellesInnvilget))){
                    paragraph {
                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                            text (
                                bokmal { + "Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                nynorsk { + "Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                            )
                        }

                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))){
                            text (
                                bokmal { + "redusert ut fra inntekt. " },
                                nynorsk { + "redusert ut frå inntekt. " },
                            )
                        }

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

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Inntekten din er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { + "Inntekta di er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                nynorsk { + "Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Til sammen er " },
                                nynorsk { + "Til saman er " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "også " },
                                nynorsk { + "også " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "inntektene til deg og ektefellen, partneren eller samboeren din " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { + "inntektene til deg og ektefella, partnaren eller sambuaren din " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Dette barnetillegget er derfor " },
                                nynorsk { + "Dette barnetillegget er derfor " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "ikke " },
                                nynorsk { + "ikkje " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "redusert ut fra inntekt. " },
                                nynorsk { + "redusert ut frå inntekt. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + "Barnetilleggene er derfor" },
                                nynorsk { + "Desse barnetillegga er derfor" },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + " ikke" },
                                nynorsk { + " ikkje" },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) and ((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().equalTo(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().equalTo(0)) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag().greaterThan(0))) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                            text (
                                bokmal { + " redusert ut fra inntekt. " },
                                nynorsk { + " redusert ut frå inntekt. " },
                            )
                        }

                        showIf(pe.ut_tbu1286_del2()){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf(pe.ut_tbu1286_del2() and btFellesNetto0){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        showIf(pe.ut_tbu1286_del3()){
                            text (
                                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf(pe.ut_tbu1286_del3() and btSerkullNetto0){
                            text (
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                showIf((btSerkullInnvilget and btSerkullNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0) or not(btFellesInnvilget)))){
                    includePhrase(TBU1286_1_Generated(pe))
                }

                showIf((btFellesInnvilget and btFellesNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0) or not(btSerkullInnvilget)))){
                    includePhrase(TBU1286_2_Generated(pe))
                }

                showIf(((btSerkullInnvilget and btSerkullNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0)) and (btFellesInnvilget and btFellesNetto0 and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0)))){
                    includePhrase(TBU2490_Generated(pe))
                }

                showIf((btSerkullInnvilget or btFellesInnvilget)){
                    includePhrase(TBU1288_Generated)
                }
            }

            showIf((btFellesInnvilget or btSerkullInnvilget)){
                showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))){
                    includePhrase(TBU5005_Generated)
                    paragraph {
                        text(
                            bokmal { + "Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land." },
                            nynorsk { + "Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. " },
                        )
                    }
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
                includePhrase(TBU1133_Generated)
            }

            showIf(pe.vedtaksdata_harLopendealderspensjon()){
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(uforegrad) and pe.vedtaksdata_kravhode_onsketvirkningsdato().legacyLessThan(pe.vedtakfattetdato_minus_1mnd())){
                ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { onsketvirkningsdato ->
                    title1 {
                        text(
                            bokmal { +"Etterbetaling av uføretrygd" },
                            nynorsk { +"Etterbetaling av uføretrygd" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {+"Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen."},
                            nynorsk {+"Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga."},
                        )
                    }
                }
            }

            showIf((kravGjelder.equalTo("mellombh") or kravGjelder.equalTo("f_bh_med_utl"))){
                includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)
            }

            showIf(bosattUtland and saksbehandlerValg.refusjon){
                title1 {
                    text(
                        bokmal { +"En utenlandsk myndighet krever refusjon" },
                        nynorsk { +"Eit utanlandsk organ krev refusjon" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + fritekst("land") + " har varslet Nav at de kan ha utbetalt for mye penger til deg. De har mulighet til å kreve dette tilbake i etterbetalingen av den norske uføretrygden din. Vi vil holde tilbake etterbetalingen inntil vi har fått svar fra " + fritekst("land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("nasjonalitet") + " myndigheter." },
                        nynorsk { + fritekst("land") + " har varsla Nav at dei kan ha betalt ut for mykje pengar til deg. Dei har høve til å krevje dette tilbake i etterbetalinga av den norske uføretrygda di. Vi vil halde tilbake etterbetalinga inntil vi har fått svar frå " + fritekst("land") + ". Har du spørsmål om dette, kan du ta kontakt med " + fritekst("nasjonalitet") + " styresmakter." },
                    )
                }
                paragraph {
                    showIf(avtaletypeEos) {
                        text(
                            bokmal { +"Denne retten har " + fritekst("land") + " etter EØS-forordningen 987/2009 artikkel 72." },
                            nynorsk { +"Denne retten har " + fritekst("land") + " etter EØS-forordninga 987/2009 artikkel 72." },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Denne retten har " + fritekst("land") + " etter " + fritekst("avtaletype") + "." },
                            nynorsk { +"Denne retten har " + fritekst("land") + " etter " + fritekst("avtaletype") + "." },
                        )
                    }
                }
            }
            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.nyeAvslagBarnetillegg))

            showIf((uforegrad.greaterThanOrEqual(50) and !bosattUtland)){
                title1 {
                    text (
                        bokmal { + "Honnørkort" },
                        nynorsk { + "Honnørkort" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du har rett til honnørrabatt når du bruker offentlig transport på innenlandsreiser. Vi sender honnørkortet i posten hvis du får dette vedtaket digitalt." },
                        nynorsk { + "Du har rett til honnørrabatt når du bruker offentleg transportmiddel på innanlandsreiser. Vi sender honnørkortet i posten dersom du får dette vedtaket digitalt." },
                    )
                }
            }
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            showIf(saksbehandlerValg.barnetilleggInfo) {
                title1 {
                    text(
                        bokmal { +"Du kan ha rett til barnetillegg" },
                        nynorsk { +"Du kan ha rett til barnetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Forsørger du barn under 18 år kan du har rett til barnetillegg. Du må søke om å få barnetillegg. Barnetillegget blir beregnet ut fra din inntekt. Du finner søknadsskjema for barnetillegg på $UFOERE_SOK_URL. Trenger du hjelp til å søke, kan du kontakte oss." },
                        nynorsk { + "Forsørgjer du barn under 18 år kan du har rett til barnetillegg. Du må søkje om å få barnetillegg. Barnetillegget blir rekna ut frå inntekta di. Du finn søknadsskjema for barnetillegg på $UFOERE_SOK_URL. Treng du hjelp til å søkje, kan du kontakte oss." },
                    )
                }
            }
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(not(bosattUtland)))
            includePhrase(Felles.HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON, utland = bosattUtland))
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore)
    }
}
