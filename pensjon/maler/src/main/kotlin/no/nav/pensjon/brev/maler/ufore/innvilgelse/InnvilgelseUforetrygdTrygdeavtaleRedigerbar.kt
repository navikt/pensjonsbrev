package no.nav.pensjon.brev.maler.ufore.innvilgelse

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.PeriodisertInntektBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.trygdetidsgrunnlag.fom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.trygdetidsgrunnlag.land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.innvilgelseUfoeretrygdTrygdeavtaleDto.trygdetidsgrunnlag.tom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.uforeVedtaksinfo.vedtakFattetDatoEllerIdag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.uforeVedtaksinfo.virkningsdatoTidligereMnd
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
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
import no.nav.pensjon.brev.template.namedReference
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
        val paragraf_12_2_forste_ledd = saksbehandlervalg("paragraf_12_2_forste_ledd", "§12-2 første ledd").bool()
        val paragraf_12_2_andre_ledd_a = saksbehandlervalg("paragraf_12_2_andre_ledd_a", "§12-2 andre ledd a)").bool()
        val paragraf_12_2_andre_ledd_b = saksbehandlervalg("paragraf_12_2_andre_ledd_b", "§12-2 andre ledd b)").bool()
        val paragraf_12_2_tredje_ledd = saksbehandlervalg("paragraf_12_2_tredje_ledd", "§12-2 tredje ledd").bool()
        val hjemmelTredjelandsborger = saksbehandlervalg("hjemmelTredjelandsborger", "Hjemmel tredjelandsborger og Nordisk konvensjon").bool()
        val etterBetalingRenter = saksbehandlervalg("etterBetalingRenter", "Info om renter på etterbetaling").bool()
        val sluttbehandling = saksbehandlervalg("sluttbehandling", "Sluttbehandling").bool()
        val barnetilleggIkkeBehandlet = saksbehandlervalg("barnetilleggIkkeBehandlet", "Barnetillegg ikke behandlet").bool()
        val behovsprøvingAvBarnetilleggUtenlandskPensjon = saksbehandlervalg("barnetilleggUtenlandskPensjon", "Behovsprøving av barnetillegg - utenlandsk pensjon").bool()


        val pe = pesysData.pe

        val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(localDateNow)
        val virkningstidpunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt().ifNull(localDateNow)
        val virkningfom = pe.vedtaksdata_virkningfom()
        val virkningbegrunnelseStdbegr_22_12_1_5 = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse().equalTo("stdbegr_22_12_1_5")
        val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
        val kravmottatdato = pe.vedtaksdata_kravhode_kravmottatdato()

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
            paragraph {
                text(
                    bokmal { +"Vi viser til vårt brev av " + fritekst("dato") + " med informasjon om videre behandling av din søknad om uføretrygd. Ut fra opplysninger vi har fått fra " + fritekst("land") + ", har vi nå innvilget søknaden din om uføretrygd som vi mottok " + kravmottatdato.format() + ". "},
                    nynorsk { +"Vi viser til vårt brev av " + fritekst("dato") + " med informasjon om vidare handsaming av søknaden din om uføretrygd. Ut frå opplysningar vi har fått frå " + fritekst("land") + ", har vi no innvilga søknaden din om uføretrygd som vi mottok " + kravmottatdato.format() + ". "},
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får " + uforegrad.format() + " prosent uføretrygd fra " + virkningfom.format() + "." },
                    nynorsk { +"Du får " + uforegrad.format() + " prosent uføretrygd frå " + virkningfom.format() + "." },
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
                    bokmal { + "Du innvilges uføretrygd med unntak i " + fritekst("EØS-avtalen/trygdeavtalen med LAND") + ". Det at din uføretrygd ikke er beregnet utelukkende ut fra din inntekt og trygdetid i Norge, påvirker størrelsen på beløpet. Du kan lese mer om hvordan uføretrygden din er beregnet i vedlegget " },
                    nynorsk { + "Du innvilgast uføretrygd med unntak i " + fritekst("EØS-avtalen/trygdeavtalen med LAND") + ". Det at uføretrygda di ikkje er rekna utelukkande ut frå inntekta og trygdetida di i Noreg, påverkar storleiken på beløpet. Du kan lese meir om korleis uføretrygda di er rekna ut i vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(
                   bokmal { +"." },
                   nynorsk { +"." }
                )
            }

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
                    nynorsk { + "Vi kan gjere unntak frå hovudregelen om tidlegare medlemskap dersom: " },
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

            showIf(pesysData.trygdetidsgrunnlag.isNotEmpty()) {
                paragraph {
                    text(
                        bokmal { +"Dette er trygdetiden din før uføretidspunktet: " },
                        nynorsk { +"Dette er trygdetida di før uføretidspunktet: " },
                    )
                    table(
                        header = {
                            column {
                                text(bokmal { +"Land" }, nynorsk { +"Land" })
                            }
                            column {
                                text(bokmal { +"Fra og med" }, nynorsk { +"Frå og med" })
                            }
                            column {
                                text(bokmal { +"Til og med" }, nynorsk { +"Til og med" })
                            }
                        }
                    ) {
                        forEach(pesysData.trygdetidsgrunnlag) { trygdetid ->
                            row {
                                cell { eval(trygdetid.land.format()) }
                                cell { eval(trygdetid.fom.format()) }
                                cell { eval(trygdetid.tom.format()) }
                            }
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +fritekst("Individuell begrunnelse ved lovvalg, overlapp og fastsettelse av norsk trygdetid.") },
                    nynorsk { +fritekst("Individuell begrunnelse ved lovvalg, overlapp og fastsettelse av norsk trygdetid.") },
                )
            }

            showIf(paragraf_12_2_forste_ledd) {
                paragraph {
                    text(
                        bokmal { + "Du har sammenhengende trygdetid i Norge og utlandet i de siste fem årene fram til uføretidspunktet, og fyller hovedregelen om forutgående medlemskap gjennom sammenlegging. " },
                        nynorsk { + "Du har samanhengande trygdetid i Noreg og utlandet i dei siste fem åra fram til uføretidspunktet, og fyller hovudregelen om førehandsgodkjent medlemskap gjennom samanslåing. " },
                    )
                }
            }.orShowIf(paragraf_12_2_andre_ledd_a) {
                paragraph {
                    text(
                        bokmal { + "Du har ikke sammenhengende trygdetid i Norge og utlandet de siste fem årene fram til uføretidspunktet, og fyller fortsatt ikke hovedregelen om forutgående medlemskap. Du fyller imidlertid en unntaksregel om medlemskap gjennom sammenlegging, fordi du ble ufør før fylte 26 år, og trygdemyndighetene i utlandet bekrefter at du på uføretidspunktet i " + fritekst("måned og år") + " tilhørte deres trygdeordning. " },
                        nynorsk { + "Du har ikkje samanhengande trygdetid i Noreg og utlandet dei siste fem åra fram til uføretidspunktet, og fyller framleis ikkje hovudregelen om førehandsgodkjent medlemskap. Du fyller likevel ei unntaksregel om medlemskap gjennom samanslåing, fordi du vart ufør før fylte 26 år, og trygdemyndigheitene i utlandet stadfestar at du på uføretidspunktet i " + fritekst("månad og år") + " høyrde til deira trygdeordning. " },
                    )
                }
            }.orShowIf(paragraf_12_2_andre_ledd_b) {
                paragraph {
                    text(
                        bokmal { + "Du har ikke sammenhengende trygdetid i Norge og utlandet de siste fem årene fram til uføretidspunktet, og fyller fortsatt ikke hovedregelen om forutgående medlemskap. Du fyller imidlertid en unntaksregel om medlemskap gjennom sammenlegging, fordi når vi legger sammen alle periodene med trygdetid i Norge og utlandet, har du mindre enn fem år uten medlemskap etter fylte 16 år. " },
                        nynorsk { + "Du har ikkje samanhengande trygdetid i Noreg og utlandet dei siste fem åra fram til uføretidspunktet, og fyller framleis ikkje hovudregelen om førehandsgodkjent medlemskap. Du fyller likevel ei unntaksregel om medlemskap gjennom samanslåing, fordi når vi legg saman alle periodane med trygdetid i Noreg og utlandet, har du mindre enn fem år utan medlemskap etter fylte 16 år. " },
                    )
                }
            }.orShowIf(paragraf_12_2_tredje_ledd) {
                paragraph {
                    text(
                        bokmal { + "Du har ikke sammenhengende trygdetid i Norge og utlandet de siste fem årene fram til uføretidspunktet, og fyller fortsatt ikke hovedregelen om forutgående medlemskap. Du fyller imidlertid en unntaksregel om medlemskap gjennom sammenlegging, fordi du har en opptjening i Norge og i utlandet før du ble ufør som til sammen " + fritekst("svarer til minst halvparten av full minsteytelse/utgjør minst 20 års trygdetid. Du har 19 år og X måneder med trygdetid, noe som rundes opp til 20 år.") },
                        nynorsk { + "Du har ikkje samanhengande trygdetid i Noreg og utlandet dei siste fem åra fram til uføretidspunktet, og fyller framleis ikkje hovudregelen om førehandsgodkjent medlemskap. Du fyller likevel ei unntaksregel om medlemskap gjennom samanslåing, fordi du har ei opptening i Noreg og i utlandet før du vart ufør som til saman " + fritekst("svarar til minst halvparten av full minsteyting/utgjer minst 20 års trygdetid. Du har 19 år og X månader med trygdetid, noko som rundast opp til 20 år.") },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(true)) + ". " },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(true)) + ". " },
                )
            }
            showIf(pesysData.eosNordisk) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er også gjort etter EØS-avtalen, i henhold til forordning 883/2004 artikkel 6 og artikkel 52. " },
                        nynorsk { +"Vedtaket er òg gjort etter EØS-avtala, i henhold til forordning 883/2004 artikkel 6 og artikkel 52. " },
                    )
                }
            }.orShowIf(pesysData.avtaletype.notNull() and pesysData.artikkel.notNull()) {
                showIf(pesysData.avtaletype.equalTo("Storbritannia")) {
                    paragraph {
                        text(bokmal { + "Vedtaket har vi også gjort etter konvensjonen mellom Island, Liechtenstein, Norge og Storbritannia artikkel 10 og artikkel 46." },
                            nynorsk { + "Vedtaket har vi òg gjort etter konvensjonen mellom Island, Liechtenstein, Noreg og Storbritannia artikkel 10 og artikkel 46." },)

                    }
                }.orShow {
                    paragraph {
                        text(bokmal { + "Vedtaket har vi også gjort etter trygdeavtalen med " + pesysData.avtaletype.ifNull("avtaletype") + " artikkel " + pesysData.artikkel.ifNull("X") + "." },
                            nynorsk { + "Vedtaket har vi òg gjort etter trygdeavtalen med " + pesysData.avtaletype.ifNull("avtaletype") + " artikkel " + pesysData.artikkel.ifNull("X") + "." },)
                    }
                }
            }.orShow {
                paragraph {
                    text(bokmal { + fritekst("Du har valgt en avtaletype hvor vi ikke har en forhåndsutfylt tekst. Her må du sette inn tekst som passer til saken.") },
                        nynorsk { + fritekst("Du har valt ein avtaletype der vi ikkje har ein forhåndsutfylt tekst. Her må du setje inn tekst som passar til saka.") },)
                }
            }

            showIf(hjemmelTredjelandsborger) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er også gjort etter Nordisk konvensjon artikkel 3 og artikkel 4, og EØS-avtalen, i henhold til forordning 883/2004 artikkel 6 og artikkel 52. " },
                        nynorsk { +"Vedtaket er òg gjort etter Nordisk konvensjon artikkel 3 og artikkel 4, og EØS-avtalen, i etter forordning 883/2004 artikkel 6 og artikkel 52. " }, // TODO: Blir dette riktig nynorsk? "i henhold til" = "i etter"?
                    )
                }
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

            showIf(pesysData.kravType.equalTo(InnvilgelseUfoeretrygdTrygdeavtaleDto.KravType.MELLOMBEHANDLING) or not(sluttbehandling)){
                title1 {
                    text (
                        bokmal { + "Dette skjer videre med søknaden din" },
                        nynorsk { + "Dette skjer vidare med søknaden din" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Når vi mottar vedtak fra " + fritekst("land") + ", vil vi fatte et vedtak med en endelig beregning. Du mottar da et samlet vedtak fra Norge og " + fritekst("land") + ". " },
                        nynorsk { + "Når vi får vedtak frå " + fritekst("land") + ", kjem vi til å gjere eit vedtak med ei endeleg berekning. Du får då eit samla vedtak frå Noreg og " + fritekst("land") + ". " },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Hvis det viser seg at du har fått utbetalt mer enn du skulle, kan vi kreve at du betaler tilbake det du skylder. Hvis du får etterbetalt penger fra en utenlandsk trygdemyndighet, kan vi trekke det du skylder fra etterbetalingen. " },
                        nynorsk { + "Om det viser seg at du har fått utbetalt meir enn du skulle, kan vi krevje at du betalar tilbake det du skuldar. Om du får etterbetalt pengar frå ein utanlandsk trygdemyndigheit, kan vi trekkje det du skuldar frå etterbetalinga. " },
                    )
                }
            }.orShowIf(pesysData.kravType.equalTo(InnvilgelseUfoeretrygdTrygdeavtaleDto.KravType.SLUTTBEHANDLING) or sluttbehandling){
                title1 {
                    text (
                        bokmal { + "Vedtak fra utenlandske trygdemyndigheter" },
                        nynorsk { + "Vedtak frå utanlandske trygdemyndigheiter" },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Vi har mottatt melding fra " + fritekst("land") + " om at de har " + fritekst("innvilget/avslått") + " søknaden din om uføreytelse/alderspensjon. De har fattet vedtaket etter egne nasjonale regler" + fritekst(", og du vil få utbetalt ytelsen/pensjonen direkte fra dem") + ". Du kan lese mer om dette i det utenlandske vedtaket som du skal ha mottatt. " },
                        nynorsk { + "Vi har mottatt melding frå " + fritekst("land") + " om at dei har " + fritekst("innvilga/avslått") + " søknaden din om uføreytelse/alderspensjon. Dei har fatta vedtaket etter eigne nasjonale reglar" + fritekst(", og du vil få utbetalt ytelsen/pensjonen direkte frå dei") + ". Du kan lese meir om dette i det utanlandske vedtaket som du skal ha mottatt. " },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Du finner samlet vedtak fra Norge og " + fritekst("land") + " i vedlegget «P1- Samlet melding om pensjonsvedtak». " },
                        nynorsk { + "Du finn samla vedtak frå Noreg og " + fritekst("land") + " i vedlegget «P1- Samla melding om pensjonsvedtak». " },
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Hvis du mener uføretrygden din har blitt negativt påvirket av at flere lands trygdemyndigheter er involvert, kan du be om en ny vurdering. Du kan lese mer om dette i vedleggene «P1 - Samlet melding om pensjonsvedtak» og «Informasjon om skjemaet P1 og hvordan det brukes». " },
                        nynorsk { + "Om du meiner uføretrygda di har blitt negativt påverka av at fleire lands trygdemyndigheiter er involvert, kan du be om ei ny vurdering. Du kan lese meir om dette i vedlegga «P1 - Samla melding om pensjonsvedtak» og «Informasjon om skjemaet P1 og korleis det blir brukt». " }
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
                        ektefelletilleggInnvilget = ektefelletilleggInnvilget,
                        gjenlevendetilleggInnvilget = gjenlevendetilleggInnvilget,
                        sisteForsorgeransvarErTrue = pe.ut_forsorgeransvar_siste_er_true(),
                        forsteForsorgeransvarErTrue = pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar(),
                    )
                )
            }

            showIf(behovsprøvingAvBarnetilleggUtenlandskPensjon) {
                title1 {
                    text(
                        bokmal { +"Behovsprøving av barnetillegg" },
                        nynorsk { +"Behovsprøving av barnetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du mottar barnetillegg for barn som du forsørger. Barnetillegget er behovsprøvd, og alle typer inntekter kan få betydning for beregningen av det, også ytelser fra utlandet. " },
                        nynorsk { +"Du mottek barnetillegg for barn som du forsørgjer. Barnetillegget er behovsprøvd, og alle typar inntekter kan få tyding for rekninga av det, òg ytingar frå utlandet. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt melding om at du er innvilget ytelse fra " + fritekst("land") + ". Det du har fått etterbetalt, holder vi utenfor behovsprøvingen av barnetillegget. " },
                        nynorsk { +"Vi har fått melding om at du er innvilga yting frå " + fritekst("land") + ". Det du har fått etterbetalt, held vi utanfor behovsprøvingen av barnetillegget. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at du i år får utbetalt " + fritekst("beløp i utenlandsk valuta") + ". Beløpet tilsvarer " + fritekst("beløp i NOK") + ". " },
                        nynorsk { +"Vi har lagt til grunn at du i år får utbetalt " + fritekst("beløp i utanlandsk valuta") + ". Beløpet tilsvarar " + fritekst("beløp i NOK") + ". " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du må selv registrere beløpet du forventer å få utbetalt fra utlandet neste år i inntektsplanleggeren på nav.no i desember. Se «Du må melde fra om eventuell inntekt». " },
                        nynorsk { +"Du må sjølv registrere beløpet du forventar å få utbetalt frå utlandet neste år i inntektsplanleggeren på nav.no i desember. Sjå «Du må melde frå om eventuell inntekt». " },
                    )
                }
            }

            // TODO: Må kanskje inn med et eget avsnitt imellom her om inntekt fra Norge og utland?
            includePhrase(Innvilgelse.BarnetilleggOgInntekt(
                pe = pe,
                btInnvilget = btInnvilget,
                btFellesInnvilget = btFellesInnvilget,
                btSerkullInnvilget = btSerkullInnvilget,
                btSerkullNetto0 = btSerkullNetto0,
                btFellesNetto0 = btFellesNetto0,
                periodisertInntekt = periodisertInntekt
            ))

            showIf(barnetilleggIkkeBehandlet) {
                title1 {
                    text(
                        bokmal { +"Barnetillegg som ikke er behandlet" },
                        nynorsk { +"Barnetillegg som ikkje er behandla" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du forsørger barn under 18 år kan du ha krav på barnetillegg i uføretrygden. Dersom du ønsker å få vurdert retten din til barnetillegg må du søke om dette. Søknadskjema ‘SKJEMA NAV 12-15.01 Søknad om barnetillegg for deg som har uføretrygd’ finner du på $NAV_URL. Dokumentasjon på eventuell forsørgelse for barn du ikke bor sammen med må vedlegges. Slik dokumentasjon kan være en skriftlig avtale undertegnet av begge barnas foreldre på at du har privat bidragsavtale og/eller formalisert samværsavtale. " },
                        nynorsk { +"Om du forsørgjer barn under 18 år kan du ha krav på barnetillegg i uføretrygda. Dersom du ønskjer å få vurdert retten din til barnetillegg må du søkje om dette. Søknadsskjema ‘SKJEMA NAV 12-15.01 Søknad om barnetillegg for deg som har uføretrygd’ finn du på $NAV_URL. Dokumentasjon på eventuell forsørging for barn du ikkje bur saman med må leggast ved. Slik dokumentasjon kan vere ein skriftleg avtale underteikna av begge barna sine foreldre på at du har privat bidragsavtale og/eller formalisert samværsavtale. " },
                    )
                }
            }

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


            showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad().equalTo(uforegrad) and pesysData.vedtaksinfo.virkningsdatoTidligereMnd) {
                ifNotNull(pe.vedtaksdata_kravhode_onsketvirkningsdato()) { onsketvirkningsdato ->
                    title1 {
                        text(
                            bokmal { +"Etterbetaling av uføretrygd" },
                            nynorsk { +"Etterbetaling av uføretrygd" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                            nynorsk { +"Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju yrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
                        )
                    }
                    showIf(etterBetalingRenter) {
                        paragraph {
                            text(
                                bokmal { +"Vi vil vurdere om du kan ha rett på renter på eventuelt etterbetalt beløp. Du vil få et eget vedtak om renter. " },
                                nynorsk { +"Vi vil vurdere om du kan ha rett på renter på eventuelt etterbetalt beløp. Du vil få eit eige vedtak om renter. " },
                            )
                        }
                    }
                }
            }

            // TODO: Er denne aktuell her?
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
