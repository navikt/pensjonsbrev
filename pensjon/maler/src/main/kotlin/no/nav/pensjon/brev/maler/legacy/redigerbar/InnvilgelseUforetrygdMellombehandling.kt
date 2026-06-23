package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdMellombehandlingDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdMellombehandlingDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdMellombehandlingDto.saksbehandlervalg.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdMellombehandlingDto.trygdetidsgrunnlag.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdMellombehandlingDto.*
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
object InnvilgelseUforetrygdMellombehandling : RedigerbarTemplate<InnvilgelseUfoeretrygdMellombehandlingDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD_MELLOMBEHANDLING
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

        val oppfyltvedsammenlegging = pe.vedtaksbrev_vedtaksdata_vilkarsvedtak_vilkar_medlemskapforutettertrygdeavtaler_oppfyltvedsammenlegging()

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

        val ifuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
        val ifuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
        val ieuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
        val ieuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()

        val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
        val oifuMerEnnIfu = oifuKroner.greaterThan(ifuInntekt)

        title {
            text(
                bokmal { +"Nav har innvilget forskudd på søknaden din om uføretrygd" },
                nynorsk { +"Nav har innvilga forskot på søknaden din om uføretrygd" },
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
            ifNotNull(pesysData.sisteTrygdetidsgrunnlag) { trygdetid ->
                    paragraph {
                        text(
                            bokmal {+"Du har vært medlem av folketrygden fra " + trygdetid.fom.format() + " til " + trygdetid.tom.format() + ". " +
                                    "Vi har fått opplyst at du har vært medlem av den " + fritekst("nasjonalitet") + " trygdeordningen fra " + fritekst("fom") + " til " + fritekst("tom") + ". " +
                                    "Uføretidspunktet ditt er satt til " + uforetidspunkt.format() + ". " +
                                    "Du har derfor vært medlem av folketrygden og den " + fritekst("nasjonalitet") + " trygdeordningen sammenhengende i " + pe.aars_trygdetid() + " år eller mer fram til uføretidspunktet ditt. Fordi vi har lagt sammen perioder med medlemskap i folketrygden og i " + fritekst("land") + ", får du unntak fra vilkåret om medlemskap i folketrygden."
                            },
                            nynorsk {+"Du har vore medlem av den norske folketrygda frå " + trygdetid.fom.format() + " til " + trygdetid.tom.format() + ". " +
                                    "Vi har fått opplyst at du har vore medlem av den " + fritekst("Nasjonalitet") + " trygdeordninga frå " + fritekst("fom") + " til " + fritekst("tom") + ". " +
                                    "Uføretidspunktet ditt er sett til " + uforetidspunkt.format() + ". " +
                                    "Du har derfor vore medlem av den norske folketrygda og den " + fritekst("Nasjonalitet") + " trygdeordninga samanhengande i " + pe.aars_trygdetid() + " år eller meir fram til uføretidspunktet ditt. Fordi vi har lagt saman periodar med medlemstid i Noreg og i " + fritekst("land") + ", får du unntak frå vilkåret om medlemskap i folketrygda."
                            },
                        )
                    }
            }
            paragraph {
                text (
                    bokmal { + "Vedtaket er gjort etter EØS-avtalen artikkel 7 i forordning 883/2004 og folketrygdloven § 12-2." },
                    nynorsk { + "Vedtaket er gjort etter EØS-avtalen artikkel 7 i forordning 883/2004 og folketrygdlova § 12-2." },
                )
            }

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

            includePhrase(Innvilgelse.UnntaksregelMedlemskapUtland(
                pe = pe,
                oppfyltvedsammenlegging = oppfyltvedsammenlegging,
                yrkesskadeResultat = yrkesskadeResultat,
                innvilgetEtter12_2_andreledd = saksbehandlerValg.innvilgetEtter12_2Andreledd,
                innvilgetEtter12_2_tredjeledd = saksbehandlerValg.innvilgetEtter12_2Tredjeledd
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

            title1 {
                text (
                    bokmal { + "Dette skjer videre med søknaden din" },
                    nynorsk { + "Dette skjer vidare med søknaden din" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Når vi mottar vedtak fra " + fritekst("land") + ", vil vi fatte et vedtak med en endelig beregning. Du mottar da et samlet vedtak fra Norge og " + fritekst("land") + "." },
                    nynorsk { + "Når vi får vedtak frå " + fritekst("land") + ", kjem vi til å gjere eit vedtak med ei endeleg berekning. Du får då eit samla vedtak frå Noreg og " + fritekst("land") + "." },
                )
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

            includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)

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
