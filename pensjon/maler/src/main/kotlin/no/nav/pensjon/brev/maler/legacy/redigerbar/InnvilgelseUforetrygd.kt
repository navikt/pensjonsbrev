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
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.SaksbehandlervalgSelectors.barnetilleggInfo
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.SaksbehandlervalgSelectors.innvilget_etter_12_2_tredjeledd
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Innvilgelse
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
                    bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                    nynorsk { + "Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di i " + fritekst("Månad og år") + "." },
                )
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

            includePhrase(Innvilgelse.BegrunnelseForVedtaket(
                pe = pe,
                txtEllerEtAvtaleland = txtEllerEtAvtaleland,
            ))

            includePhrase(Innvilgelse.InnvilgetEtterKlage(
                kravarsak = kravarsak,
            ))

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(true)) + "." },
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

            includePhrase(Innvilgelse.UnntaksregelMedlemskap(
                pe = pe,
                yrkesskadeResultat = yrkesskadeResultat,
                ungUforResultat = ungUforResultat,
                innvilgetEtter12_2_tredjeledd = saksbehandlerValg.innvilget_etter_12_2_tredjeledd,
            ))

            includePhrase(Innvilgelse.YrkesskadeEllerYrkessykdom(
                pe = pe,
                beregningsvilkarYrkesskadegrad = beregningsvilkarYrkesskadegrad,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                yrkesskadeResultat = yrkesskadeResultat,
            ))

            includePhrase(Innvilgelse.Virkningstidspunkt(
                pe = pe,
                virkningbegrunnelseStdbegr_22_12_1_5 = virkningbegrunnelseStdbegr_22_12_1_5,
            ))

            includePhrase(
                Innvilgelse.Uforetidspunkt(
                    pe = pe,
                    uforetidspunkt = uforetidspunkt,
                )
            )

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

            showIf(kravGjelder.equalTo("f_bh_med_utl")){
                paragraph {
                    text (
                        bokmal { + "Dette skjer videre med søknaden din" },
                        nynorsk { + "Dette skjer vidare med søknaden din" },
                    )
                }
            }

            showIf(kravGjelder.equalTo("f_bh_med_utl")){
                paragraph {
                    text (
                        bokmal { + "Når vi mottar vedtak fra " + fritekst("land") + ", vil vi fatte et vedtak med en endelig beregning. Du mottar da et samlet vedtak fra Norge og " + fritekst("land") + "." },
                        nynorsk { + "Når vi får vedtak frå " + fritekst("land") + ", kjem vi til å gjere eit vedtak med ei endeleg berekning. Du får då eit samla vedtak frå Noreg og " + fritekst("land") + "." },
                    )
                }
            }

            includePhrase(Innvilgelse.KombinereUforetrygdOgInntekt(
                pe = pe,
                uforegrad = uforegrad,
                ieuInntekt = ieuInntekt,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
            ))

            includePhrase(Innvilgelse.MeldeFraOmInntekt)

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

            showIf(kravGjelder.equalTo("f_bh_med_utl")){
                includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)
            }

            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.nyeAvslagBarnetillegg))

            includePhrase(Innvilgelse.Honnoerkort(
                uforegrad = uforegrad,
            ))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Innvilgelse.RettTilBarnetillegg(barnetilleggInfo = saksbehandlerValg.barnetilleggInfo))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(not(bosattUtland)))
            includePhrase(Felles.HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON, utland = bosattUtland))
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
