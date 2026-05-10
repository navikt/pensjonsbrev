package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.dineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.nyeAvslagBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.nyeInnvilgedeBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.oifuVedVirkningstidspunkt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.SaksbehandlervalgSelectors.barnetilleggInfo
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.SaksbehandlervalgSelectors.innvilget_etter_12_2_tredjeledd
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.SaksbehandlervalgSelectors.refusjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Innvilgelse
import no.nav.pensjon.brev.maler.fraser.ufoer.Innvilgelse.UnntaksregelMedlemskap
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
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
object InnvilgelseUforetrygdUtland : RedigerbarTemplate<InnvilgelseUfoeretrygdUtlandDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD_UTLAND
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
        val pe = pesysData.pe

        val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
        val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(LocalDate.now())
        val virkningbegrunnelseStdbegr_22_12_1_5 = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse().equalTo("stdbegr_22_12_1_5")
        val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()

        val btSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
        val btFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
        val btInnvilget = btSerkullInnvilget or btFellesInnvilget
        val btSerkullNetto0 = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
        val btFellesNetto0 = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)

        val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
        val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
        val txtOgEllerEktefelle = ifElse (ektefelletilleggInnvilget, " og/eller ektefelle", "")

        val ungUforResultat = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()
        val kravarsak = pe.vedtaksdata_kravhode_kravarsaktype()
        val avtaletypeEos = pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaletype().equalTo("eos_nor")

        val beregningsvilkarYrkesskadegrad = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
        val yrkesskadeResultat = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()

        val beregningsvilkarUforegrad = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()

        val instoppholdType = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()
        val instoppholdAnvendt = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()

        val totalNettoUforeberegning = pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()

        val kravGjelder = pe.vedtaksdata_kravhode_kravgjelder()
        val bosattUtland = kravGjelder.equalTo("f_bh_bo_utl")
        val oppfyltvedsammenlegging = pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()
        val txtEllerEtAvtaleland = ifElse(bosattUtland and oppfyltvedsammenlegging, " eller et avtaleland", "")

        val ifuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
        val ifuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
        val ieuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
        val ieuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()

        val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
        val oifuMerEnnIfu = oifuKroner.greaterThan(ifuInntekt)

        title {
            text(
                bokmal { +"Nav har innvilget søknaden din om uføretrygd" },
                nynorsk { +"Nav har innvilget søknaden din om uføretrygd " },
            )
        }

        outline {
            includePhrase(Innvilgelse.InnvilgetSoeknad(
                ungUforResultat = ungUforResultat,
                kravarsak = kravarsak,
                kravGjelder = kravGjelder,
                kravmottatdato = pe.vedtaksdata_kravhode_kravmottatdato(),
                uforegrad = uforegrad,
                virkningfom = pe.vedtaksdata_virkningfom(),
                virkningstidpunkt = virkningstidpunkt,
            ))

            includePhrase(Innvilgelse.YrkesskadeOgUngUfoerResultat(
                beregningsvilkarYrkesskadegrad = beregningsvilkarYrkesskadegrad,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                yrkesskadeResultat = yrkesskadeResultat,
                ungUforResultat = ungUforResultat,
            ))

            includePhrase(Innvilgelse.InnvilgelseDetaljer(
                pe = pe,
                nyeInnvilgedeBarnetillegg = pesysData.nyeInnvilgedeBarnetillegg,
                nyeAvslagBarnetillegg = pesysData.nyeAvslagBarnetillegg,
                btFellesInnvilget = btFellesInnvilget,
                btFellesNetto0 = btFellesNetto0,
                btSerkullInnvilget = btSerkullInnvilget,
                btSerkullNetto0 = btSerkullNetto0,
                btInnvilget = btInnvilget,
                gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                instoppholdType = instoppholdType,
                instoppholdAnvendt = instoppholdAnvendt,
                totalNettoUforeberegning = totalNettoUforeberegning,
            ))


            paragraph {
                text (
                    bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling så snart som mulig." },
                    nynorsk { + "Uføretrygda blir betalt ut seinast den 20. kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di så snart som mogleg." },
                )
            }

            includePhrase(Innvilgelse.BegrunnelseForVedtaket(
                pe = pe,
                txtEllerEtAvtaleland = txtEllerEtAvtaleland,
            ))

            includePhrase(Innvilgelse.InnvilgetEtterKlage(
                kravarsak = kravarsak,
            ))

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(false)) + " og EØS-avtalens bestemmelser om trygd i forordning 883/2004 artikkel 6, artikkel 7 og artikkel 45." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(false)) + " og EØS-avtala sine føresegner om trygd i forordning 883/2004 artikkel 6, artikkel 7 og artikkel 45." },
                )
            }

            includePhrase(Innvilgelse.UnntaksregelNedsattInntektsevne(
                pe = pe,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                beregningsvilkarYrkesskadegrad = beregningsvilkarYrkesskadegrad,
            ))

            includePhrase(Innvilgelse.RettighetSomUngUfoer(
                ungUforResultat = ungUforResultat,
                pe = pe,
                vedleggOpplysningerBruktIBeregningUT = vedleggOpplysningerBruktIBeregningUTLegacy,
            ))

            includePhrase(
                UnntaksregelMedlemskap(
                    pe = pe,
                    yrkesskadeResultat = yrkesskadeResultat,
                    ungUforResultat = ungUforResultat,
                    innvilgetEtter12_2_tredjeledd = saksbehandlerValg.innvilget_etter_12_2_tredjeledd
                )
            )

            showIf(oppfyltvedsammenlegging){
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

            includePhrase(Innvilgelse.YrkesskadeEllerYrkessykdom(
                pe = pe,
                beregningsvilkarYrkesskadegrad = beregningsvilkarYrkesskadegrad,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                yrkesskadeResultat = yrkesskadeResultat,
            ))

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

            includePhrase(Innvilgelse.Virkningstidspunkt(
                pe = pe,
                virkningbegrunnelseStdbegr_22_12_1_5 = virkningbegrunnelseStdbegr_22_12_1_5,
            ))

            includePhrase(Innvilgelse.Uforetidspunkt(
                pe = pe,
                uforetidspunkt = uforetidspunkt,
            ))

            includePhrase(Innvilgelse.FastsattUforegrad(
                ifuBegrunnelse = ifuBegrunnelse,
                ieuBegrunnelse = ieuBegrunnelse,
                ifuInntekt = ifuInntekt,
                ieuInntekt = ieuInntekt,
                oifuMerEnnIfu = oifuMerEnnIfu,
                oifuKroner = oifuKroner,
                uforegrad = uforegrad,
                ungUforResultat = ungUforResultat,
                vedleggOpplysningerBruktIBeregningUT = vedleggOpplysningerBruktIBeregningUTLegacy,
            ))

            includePhrase(Innvilgelse.KombinereUforetrygdOgInntekt(
                pe = pe,
                uforegrad = uforegrad,
                ieuInntekt = ieuInntekt,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
            ))

            paragraph {
                text (
                    bokmal { + "Inntektsgrensen gjelder bare for den norske uføretrygden din. Har du spørsmål om inntektsgrensen i et annet land, må du kontakte trygdemyndighetene i det landet det gjelder." },
                    nynorsk { + "Inntektsgrensa gjeld berre for den norske uføretrygda di. Har du spørsmål om inntektsgrensa i eit anna land, må du kontakte trygdestyresmaktene i det landet det gjeld." },
                )
            }

            includePhrase(Innvilgelse.MeldeFraOmInntekt)

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

            includePhrase(Innvilgelse.InstitusjonReduksjon(
                pe = pe,
                instoppholdType = instoppholdType,
                instoppholdAnvendt = instoppholdAnvendt,
                ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                txtOgEllerEktefelle = txtOgEllerEktefelle,
                totalNettoUforeberegning = totalNettoUforeberegning,
            ))

            includePhrase(Innvilgelse.Straffegjennomfoering(
                pe = pe,
                instoppholdType = instoppholdType,
                ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
            ))

            includePhrase(Innvilgelse.BarnetilleggOgInntekt(
                pe = pe,
                btInnvilget = btInnvilget,
                btFellesInnvilget = btFellesInnvilget,
                btSerkullInnvilget = btSerkullInnvilget,
                btSerkullNetto0 = btSerkullNetto0,
                btFellesNetto0 = btFellesNetto0,
            ))

            includePhrase(Innvilgelse.BarnetilleggOgUtland(
                pe = pe,
                btInnvilget = btInnvilget,
            ))

            includePhrase(Innvilgelse.Gjenlevendetillegg(
                pe = pe,
                gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
            ))

            includePhrase(Innvilgelse.KombinereUforetrygdOgAlderspensjon(
                pe = pe,
            ))

            includePhrase(Innvilgelse.EtterbetalingUforetrygd(
                pe = pe,
                uforegrad = uforegrad,
            ))

            showIf(saksbehandlerValg.refusjon){
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

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Innvilgelse.RettTilBarnetillegg(barnetilleggInfo = saksbehandlerValg.barnetilleggInfo))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(not(bosattUtland)))
            includePhrase(Felles.HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON, utland = bosattUtland))
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
