package no.nav.pensjon.brev.maler.ufore.innvilgelse

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.felles.selectors.uforeVedtaksinfo.vedtakFattetDatoEllerIdag
import no.nav.pensjon.brev.api.model.maler.felles.selectors.uforeVedtaksinfo.virkningsdatoTidligereMnd
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TemplateRootScope.RedigerbartVedlegg
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnvilgelseUforetrygdTrygdeavtaleRedigerbar : RedigerbarTemplate<InnvilgelseUfoeretrygdTrygdeavtaleDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelseTrygdeavtale.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_INNVILGELSE_UFOERETRYGD_TRYGDEAVTALE
    override val kategori = Brevkategori.SLUTTBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av uføretrygd - trygdeavtale",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val forskudd = saksbehandlervalg("forskudd", "Forskudd ved EØS sammelegging").bool()
        val barnetilleggInfo = saksbehandlervalg("barnetilleggInfo", "Info om rett til barnetillegg").bool()
        val periodisertInntekt = saksbehandlervalg("periodisertInntekt", "Periodisert inntekt barnetillegg").enum<PeriodisertInntektBarnetillegg>()
        val flytterTilNorge = saksbehandlervalg("flytterTilNorge", "Bruker flytter til Norge").bool()
        val flytterTilbakeTilNorge = saksbehandlervalg("flytterTilbakeTilNorge", "Bruker flytter tilbake til Norge").bool()


        val pe = pesysData.pe

        val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(localDateNow)
        val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(localDateNow)
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
            showIf(forskudd) {
                text(
                    bokmal { +"Nav har innvilget et forskudd på søknaden din om uføretrygd" },
                    nynorsk { +"Nav har innvilga eit forskot på søknaden din om uføretrygd " },
                )
            }.orShow {
                text(
                    bokmal { +"Nav har innvilget søknaden din om uføretrygd" },
                    nynorsk { +"Nav har innvilga søknaden din om uføretrygd " },
                )
            }
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
                nyeAvslagBarnetillegg = pesysData.avslagBarnetilleggNye,
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

            showIf(forskudd){
                title1 {
                    text (
                        bokmal { + "Dette er et forskudd" },
                        nynorsk { + "Dette er eit forskot" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Dette forskuddet er beregnet etter reglene i EØS-avtalen, på bakgrunn av trygdetid i Norge og trygdetid i andre EØS-land. Vi utbetaler dette forskuddet fram til vi har mottatt alle nødvendige opplysninger fra utlandet og kan foreta en endelig beregning av uføretrygden din. Beløpet du får utbetalt kan bli det samme eller det kan bli endret når det endelige vedtaket er klart. Da får du samtidig informasjon om eventuell ytelse fra utlandet. " },
                        nynorsk { + "Dette forskotet er rekna ut etter reglane i EØS-avtalen, på bakgrunn av trygdetid i Noreg og trygdetid i andre EØS-land. Vi utbetaler dette forskotet fram til vi har fått alle nødvendige opplysningar frå utlandet og kan gjere ei endeleg rekning av uføretrygda di. Beløpet du får utbetalt kan bli det same eller det kan bli endra når det endelege vedtaket er klart. Då får du samtidig informasjon om eventuell ytelse frå utlandet. " },
                    )
                }
            }

            includePhrase(Innvilgelse.BegrunnelseForVedtaket(
                pe = pe,
                txtEllerEtAvtaleland = txtEllerEtAvtaleland,
                konklusjon = false.expr()
            ))
            paragraph {
                text (
                    bokmal { + "Du fyller vilkårene for uføretrygd, unntatt vilkår om medlemskap i folketrygden. " },
                    nynorsk { + "Du fyller vilkåra for uføretrygd, unntatt vilkår om medlemskap i folketrygda. " },
                )
            }
            showIf(flytterTilNorge) {
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt uføretidspunktet ditt til " + uforetidspunkt.formatMonthYear() + ". Du flyttet til Norge " + fritekst("dato") + " og ble da medlem av folketrygden. Datoen er fastsatt ut fra " + fritekst("individuell begrunnelse, ev. kilde") + ". " },
                        nynorsk { + "Vi har fastsett uføretidspunktet ditt til " + uforetidspunkt.formatMonthYear() + ". Du flytta til Noreg " + fritekst("dato") + " og vart då medlem av folketrygda. Datoen er fastsett ut frå " + fritekst("individuell grunngjeving, ev. kjelde") + ". " },
                    )
                }
            }.orShowIf(flytterTilbakeTilNorge) {
                paragraph {
                    text (
                        bokmal { + "Vi har fastsatt uføretidspunktet ditt til " + uforetidspunkt.formatMonthYear() + ". Du flyttet tilbake til Norge " + fritekst("dato") + " og ble da medlem av folketrygden. Datoen er fastsatt ut fra " + fritekst("individuell begrunnelse, ev. kilde") + ". " },
                        nynorsk { + "Vi har fastsett uføretidspunktet ditt til " + uforetidspunkt.formatMonthYear() + ". Du flytta tilbake til Noreg " + fritekst("dato") + " og vart då medlem av folketrygda. Datoen er fastsett ut frå " + fritekst("individuell grunngjeving, ev. kjelde") + ". " },
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "Du har ikke vært medlem av folketrygden sammenhengende i de siste fem årene fram til uføretidspunktet ditt. " },
                    nynorsk { + "Du har ikkje vore medlem av folketrygda samanhengande i dei siste fem åra fram til uføretidspunktet ditt. " },
                )
            }
            paragraph {
                text (
                    bokmal { + "Vi kan gjøre unntak fra hovedregelen om forutgående medlemskap dersom: " },
                    nynorsk { + "Vi kan gjere unntak frå hovudregelen om førehandsgodkjent medlemskap dersom: " },
                )
                list {
                    item {
                        text(
                            bokmal { +"uførheten din skyldes en godkjent yrkesskade eller yrkessykdom. " },
                            nynorsk { +"Uførleiken din skuldast ein godkjend yrkesskade eller yrkessjukdom. " },
                        )
                    }
                }
            }
            paragraph {
                text (
                    bokmal { + "Vi kan også gjøre unntak dersom du var medlem av folketrygden i minst ett år umiddelbart før du satte fram krav om uføretrygd, og du: " },
                    nynorsk { + "Vi kan òg gjere unntak dersom du var medlem av folketrygda i minst eitt år umiddelbart før du sette fram krav om uføretrygd, og du: " },
                )
                list {
                    item {
                        text(
                            bokmal { +"ble ufør før du fylte 26 år og da var medlem av folketrygden, eller " },
                            nynorsk { +"blei ufør før du fylte 26 år og då var medlem av folketrygda, eller " },
                        )
                    }
                    item {
                        text(
                            bokmal { +"etter fylte 16 år har vært medlem av folketrygden med unntak av maksimum fem år. " },
                            nynorsk { +"etter fylte 16 år har vore medlem av folketrygda med unntak av maksimum fem år. " },
                        )
                    }
                }
            }
            paragraph {
                text (
                    bokmal { + "Vi kan gjøre ytterligere unntak dersom du var medlem av folketrygden på uføretidspunktet, og før du ble ufør hadde: " },
                    nynorsk { + "Vi kan gjere ytterlegare unntak dersom du var medlem av folketrygda på uføretidspunktet, og før du vart ufør hadde: " },
                )
                list {
                    item {
                        text(
                            bokmal { +"inntekt og trygdetid som gir deg en uføretrygd som svarer til minst halvparten av full minsteytelse, eller " },
                            nynorsk { +"inntekt og trygdetid som gir deg ein uføretrygd som svarer til minst halvparten av full minsteyting, eller " },
                        )
                    }
                    item {
                        text(
                            bokmal { +"minst 20 års trygdetid. " },
                            nynorsk { +"minst 20 års trygdetid. " },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du oppfyller ingen av unntaksreglene om medlemskap i folketrygden. " },
                    nynorsk { +"Du oppfyller ingen av unntaksreglane om medlemskap i folketrygda. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Sammenlegging etter trygdeavtale " },
                    nynorsk { +"Samanslåing etter trygdeavtale " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Etter bestemmelsene i EØS-avtalen / trygdeavtale med " + fritekst("land") + ", kan vi gi unntak fra folketrygdlovens vilkår om medlemskap ved å legge sammen trygdetid i Norge og trygdetid i andre land. Nav må forholde seg til trygdetiden som trygdemyndighetene i andre land har fastsatt basert på sin nasjonale lovgivning. " },
                    nynorsk { +"Etter bestemmelsane i EØS-avtalen / trygdeavtale med " + fritekst("land") + ", kan vi gi unntak frå folketrygdlovens vilkår om medlemskap ved å leggje saman trygdetid i Noreg og trygdetid i andre land. Nav må forhalde seg til trygdetida som trygdemyndigheitene i andre land har fastsett basert på sin nasjonale lovgiving. " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette er trygdetiden din før uføretidspunktet: " },
                    nynorsk { +"Dette er trygdetida di før uføretidspunktet: " },
                )
            }
            paragraph {
                text(
                    bokmal { +fritekst("Individuell begrunnelse ved lovvalg, overlapp og fastsettelse av norsk trygdetid.") },
                    nynorsk { +fritekst("Individuell begrunnelse ved lovvalg, overlapp og fastsettelse av norsk trygdetid.") },
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

            includePhrase(Innvilgelse.UnntaksregelMedlemskap(
                pe = pe,
                yrkesskadeResultat = yrkesskadeResultat,
                ungUforResultat = ungUforResultat,
            ))

            includePhrase(Innvilgelse.YrkesskadeEllerYrkessykdom(
                pe = pe,
                beregningsvilkarYrkesskadegrad = beregningsvilkarYrkesskadegrad,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                yrkesskadeResultat = yrkesskadeResultat,
            ))

            includePhrase(Innvilgelse.Virkningstidspunkt(
                pe = pe,
                kravFremsattDato = pesysData.kravFremsattDato,
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
            }

            includePhrase(Innvilgelse.KombinereUforetrygdOgInntekt(
                pe = pe,
                uforegrad = uforegrad,
                ieuInntekt = ieuInntekt,
                vedtakFattetDatoEllerIdag = pesysData.vedtaksinfo.vedtakFattetDatoEllerIdag,
                beregningsvilkarUforegrad = beregningsvilkarUforegrad,
                harVTA = pesysData.harVTA
            ))

            includePhrase(Innvilgelse.InstitusjonReduksjon(
                pe = pe,
                instoppholdType = instoppholdType,
                instoppholdAnvendt = instoppholdAnvendt,
                ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                txtOgEllerEktefelle = txtOgEllerEktefelle,
                totalNettoUforeberegning = totalNettoUforeberegning,
            ))

            showIf(instoppholdType.equalTo("reduksjon_fo")) {
                includePhrase(
                    Ufoeretrygd.Straffegjennomfoering(
                        pe = pe,
                        ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                        gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                    )
                )
            }

            includePhrase(Innvilgelse.BarnetilleggOgInntekt(
                pe = pe,
                btInnvilget = btInnvilget,
                btFellesInnvilget = btFellesInnvilget,
                btSerkullInnvilget = btSerkullInnvilget,
                btSerkullNetto0 = btSerkullNetto0,
                btFellesNetto0 = btFellesNetto0,
                periodisertInntekt = periodisertInntekt
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
                virkningsdatoTidligereMnd = pesysData.vedtaksinfo.virkningsdatoTidligereMnd,
            ))

            showIf(kravGjelder.equalTo("f_bh_med_utl")){
                includePhrase(Ufoeretrygd.BeregningenDinKanBliEndret)
            }

            includePhrase(Ufoeretrygd.AvslagBarnetillegg(pesysData.avslagBarnetilleggNye))

            includePhrase(Innvilgelse.Honnoerkort(
                uforegrad = uforegrad,
            ))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Innvilgelse.RettTilBarnetillegg(barnetilleggInfo = barnetilleggInfo))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(not(bosattUtland)))
            includePhrase(Felles.HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON, utland = bosattUtland))
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        @OptIn(RedigerbartVedlegg::class)
        includeAttachmentRedigerbar(VedleggId("innvilgelseUTBeregningVedlegg"),vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore, pesysData.dineRettigheterOgPlikterUfore)
    }
}
