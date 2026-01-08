package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.garantitilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportForbud
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.harOppfyltVedSammenlegging
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArBotidKap19Avdod
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetidKap20Avdod
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdodFnr
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdodNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avtalelandNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.erForstegangsbehandletNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.faktiskBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.fullTrygdtid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.gjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.norgeBehandlendeLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjonAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.opplysningerOmAvdodBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.kravVirkDatoFomSenereEnnOensketUttakstidspunkt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.vanligSkattetrekk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AP2025TidligUttakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BilateralAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.EOSLandAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.GarantitilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.GjenlevendetilleggKap19Hjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HjemlerInnvilgelseForAP2011AP2016
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPForeloepigBeregning
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPUttakEndr
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgetGjRettKap19For2024
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvGjenlevendetillegg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilKlageUtland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkattAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Skatteplikt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkjermingstilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SoktAFPPrivatInfo
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenadAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

// MF_000092 / AP_INNV_MAN
/* Tekster og logikk mht ektefelletillegg og barnetillegg er fjernet fra brevmalen etter en samtale med Ingrid Strand:
innvETAP, innvBTAP, innvETBTAP, innvFTAPIngenUtbInntekt, innvFTAPIngenUtbSamletInntekt, innvETBTAIngenUtbInntekt, innvETBTAIngenUtbSamletInntekt,
innvilgetETAPHjemmel, innvilgetBTAPHjemmel, InnvilgetETBTAHjemmel, innvilgetGjrettOgTilleggKap20 fjernet
hvisFlyttetET, hvisFlyttetBT, hvisFlyttetETogBT */

@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {

    override val featureToggle = FeatureToggles.innvilgelseAvAlderspensjon.toggle

    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val afpPrivatResultatFellesKontoret = pesysData.afpPrivatResultatFellesKontoret.ifNull(then = false)
        val avdodNavn = pesysData.avdodNavn.ifNull(fritekst("Avdødes navn"))
        val avtalelandNavn = pesysData.avtalelandNavn
        val borIAvtaleland = pesysData.borIAvtaleland
        val borINorge = pesysData.borINorge
        val eksportForbud = pesysData.inngangOgEksportVurdering.eksportForbud
        val eksportTrygdeavtaleAvtaleland =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland
        val eksportTrygdeavtaleEOS =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOS
        val erEOSLand = pesysData.erEOSLand
        val erEksportberegnet = pesysData.alderspensjonVedVirk.erEksportberegnet
        val erForstegangsbehandlingNorgeUtland = pesysData.erForstegangsbehandletNorgeUtland
        val faktiskBostedsland = pesysData.faktiskBostedsland.ifNull(fritekst("Bostedsland"))
        val fullTrygdetid = pesysData.fullTrygdtid
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val garantitilleggInnvilget = pesysData.alderspensjonVedVirk.garantitilleggInnvilget
        val gjenlevenderettAnvendt = pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
        val gjenlevendetilleggInnvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val gjenlevendetilleggKap19 = pesysData.gjenlevendetilleggKap19.ifNull(then = Kroner(0))
        val gjenlevendetilleggKap19Innvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
        val godkjentYrkesskade = pesysData.alderspensjonVedVirk.godkjentYrkesskade
        val harAvdod = pesysData.avdodFnr.notNull()
        val harFlereBeregningsperioder = pesysData.harFlereBeregningsperioder
        val harOppfyltVedSammenlegging =
            pesysData.inngangOgEksportVurdering.harOppfyltVedSammenlegging
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val minst20ArBotidKap19Avdod = pesysData.inngangOgEksportVurdering.minst20ArBotidKap19Avdod
        val minst20ArTrygdetid = pesysData.inngangOgEksportVurdering.minst20ArTrygdetid
        val minst20ArTrygdetidKap20Avdod = pesysData.inngangOgEksportVurdering.minst20ArTrygdetidKap20Avdod
        val norgeBehandlendeLand = pesysData.norgeBehandlendeLand
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val regelverkType = pesysData.regelverkType
        val skjermingstilleggInnvilget = pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.ifNull(Kroner(0))
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad


        title {
            text(
                bokmal { +"Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon" },
                nynorsk { +"Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon" },
                english { +"We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension" }
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(gjenlevendetilleggKap19Innvilget) {
                //  beloepApOgGjtvedVirkMedDato_001, beloepApOgGjvedVirkMedDato_002
                paragraph {
                    text(
                        bokmal { +"Du får ".expr() + totalPensjon.format() + " i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt fra ".expr() +
                                    kravVirkDatoFom + "." },
                        nynorsk { +"Du får ".expr() + totalPensjon.format() + " i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt frå ".expr() +
                                    kravVirkDatoFom + "." },
                        english { +"You will receive ".expr() + totalPensjon.format() + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax from ".expr() +
                                kravVirkDatoFom + "." }
                    )
                    showIf(not(gjenlevendetilleggInnvilget)) {
                        text(
                            bokmal { +" Av dette er gjenlevendetillegget ".expr() + gjenlevendetilleggKap19.format() + "." },
                            nynorsk { +" Av dette er attlevandetillegget ".expr() + gjenlevendetilleggKap19.format() + "."},
                            english { +" Of this, the survivor’s supplement is ".expr() + gjenlevendetilleggKap19.format() + "." }
                        )
                    }
                    showIf(uforeKombinertMedAlder) {
                        text(
                            bokmal { +" Dette er i tillegg til uføretrygden din." },
                            nynorsk { +" Dette er i tillegg til uføretrygda di." },
                            english { +" This is in addition to your disability benefit." }
                        )
                    }
                }
            }

            showIf(not(gjenlevendetilleggInnvilget) and not(gjenlevendetilleggKap19Innvilget)) {
                paragraph {
                    text(
                        bokmal { +"Du får ".expr() + totalPensjon.format() + " hver måned før skatt fra ".expr() + kravVirkDatoFom },
                        nynorsk { +"Du får ".expr() + totalPensjon.format() + " kvar månad før skatt frå ".expr() + kravVirkDatoFom },
                        english { +"You will receive ".expr() + totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom }
                    )
                    showIf(uforeKombinertMedAlder and innvilgetFor67) {
                        // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                        text(
                            bokmal { +". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                            nynorsk { +". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                            english { +". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." }
                        )
                    }.orShow {
                        // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                        text(
                            bokmal { +" i alderspensjon fra folketrygden." },
                            nynorsk { +" i alderspensjon frå folketrygda." },
                            english { +" as retirement pension from the National Insurance Scheme." }
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.kravVirkDatoFomSenereEnnOensketUttakstidspunkt) {
                // invilgelseAPVirkfom
                paragraph {
                    text(
                        bokmal { +"Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. Dette går frem av folketrygdloven § 22-13." },
                        nynorsk { +"Vi kan tidlegast innvilge søknaden din månaden etter at du søkte. Dette går fram av folketrygdlova § 22-13." },
                        english { +"We can at the earliest grant your application the month after you applied. This is in accordance with the regulations of § 22-13 of the National Insurance Act." }
                    )
                }
            }

            showIf(harFlereBeregningsperioder and totalPensjon.greaterThan(0)) {
                includePhrase(Felles.FlereBeregningsperioder)
            }

            showIf(harAvdod) {
                showIf(
                    gjenlevenderettAnvendt and not(gjenlevendetilleggKap19Innvilget) and not(
                        gjenlevendetilleggInnvilget
                    )
                ) {
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter ".expr() + avdodNavn + ". Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening." },
                            nynorsk { +"I beregninga vår har vi teke utgangspunkt i pensjonsrettar du har etter ".expr() + avdodNavn + ". Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening." },
                            english { +"We have based the calculation on the pension rights you have after ".expr() + avdodNavn + ". This gives you a higher pension than if we had only based the calculation on your own earnings." }
                        )
                    }
                }

                showIf(gjenlevendetilleggKap19Innvilget) {
                    // beregningAPGjtKap19
                    paragraph {
                        text(
                            bokmal { +"Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr()
                                    + avdodNavn + "." },
                            nynorsk { +"Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr()
                                    + avdodNavn + "." },
                            english { +"You receive a survivor’s supplement in retirement pension because you have pension rights after ".expr()
                                    + avdodNavn + "." }
                        )
                    }
                }
                showIf(gjenlevenderettAnvendt and gjenlevendetilleggKap19.greaterThan(0)) {
                    // beregningAPGjtOpptj
                    paragraph {
                        text(
                            bokmal { +"Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv." },
                            nynorsk { +"Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv." },
                            english { +"The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself." }
                        )
                    }
                }

                showIf(
                    regelverkType.notEqualTo(AlderspensjonRegelverkType.AP2025) and not(gjenlevenderettAnvendt) and not(
                        gjenlevendetilleggKap19Innvilget
                    ) and not(gjenlevendetilleggInnvilget)
                ) {
                    // beregningAPGjRettOpptjEgen_002
                    title1 {
                        text(
                            bokmal { +"Gjenlevenderett i alderspensjon" },
                            nynorsk { +"Attlevenderett i alderspensjon" },
                            english { +"Survivor's rights in retirement pension" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter ".expr()
                                    + avdodNavn + "." },
                            nynorsk { +"I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter ".expr()
                                    + avdodNavn + "." },
                            english { +"We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after ".expr()
                                    + avdodNavn + "." }
                        )
                    }
                }
            }

            showIf(privatAFPErBrukt) { includePhrase(AfpPrivatErBrukt(uttaksgrad)) }

            showIf(afpPrivatResultatFellesKontoret) { includePhrase(SoktAFPPrivatInfo) }

            showIf(
                erEksportberegnet and not(eksportForbud) and not(minst20ArTrygdetid)
                        and not(minst20ArTrygdetidKap20Avdod) and not(minst20ArBotidKap19Avdod)
            ) {
                // innvilgelseAPUnder20aar
                paragraph {
                    text(
                        bokmal { +"Du har ikke vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du bor i ".expr()
                                + faktiskBostedsland + "." },
                        nynorsk { +"Du har ikkje vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr()
                                + faktiskBostedsland + "." },
                        english { +"You have not been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr()
                                + faktiskBostedsland + "." }
                    )
                }
            }

            showIf(
                erEksportberegnet and not(eksportForbud) and harAvdod and not(minst20ArTrygdetidKap20Avdod)
                        and not(minst20ArBotidKap19Avdod)
            ) {
                // innvilgelseAPUnder20aarAvdod
                paragraph {
                    text(
                        bokmal { +"Verken du eller avdøde har vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du når du bor i ".expr()
                                + faktiskBostedsland + "." },
                        nynorsk { +"Verken du eller avdøde har vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr()
                                + faktiskBostedsland + "." },
                        english { +"Neither you nor the deceased have been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr()
                                + faktiskBostedsland + "." }
                    )
                }
            }

            showIf(uttaksgrad.lessThan(100) and not(uforeKombinertMedAlder)) {
                paragraph {
                    text(
                        bokmal { +"Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                        nynorsk { +"Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                        english { +"You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application." }
                    )
                }
            }

            showIf(eksportTrygdeavtaleEOS or eksportTrygdeavtaleAvtaleland) {
                // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
                paragraph {
                    text(
                        bokmal { +"Vi forutsetter at du bor i ".expr() + faktiskBostedsland + ". Hvis du skal flytte til et ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utenfor EØS-området",
                            ifFalse = "annet land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { +"Vi føreset at du bur i ".expr() + faktiskBostedsland + ". Dersom du skal flytte til eit ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utanfor EØS-området",
                            ifFalse = "anna land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { +"We presume that you live in ".expr() + faktiskBostedsland + ". If you are moving to ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "a country outside the EEA region",
                            ifFalse = "another country"
                        ) + ", it is important that you contact Nav. We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            includePhrase(
                HjemlerInnvilgelseForAP2011AP2016(
                    garantipensjonInnvilget,
                    godkjentYrkesskade,
                    innvilgetFor67,
                    pensjonstilleggInnvilget,
                    regelverkType
                )
            )
            includePhrase(SkjermingstilleggHjemmel(skjermingstilleggInnvilget))
            includePhrase(AP2025TidligUttakHjemmel(innvilgetFor67, regelverkType))
            includePhrase(
                EOSLandAvtaleHjemmel(
                    borINorge, eksportTrygdeavtaleEOS, erEOSLand, harOppfyltVedSammenlegging
                )
            )
            includePhrase(
                BilateralAvtaleHjemmel(
                    avtalelandNavn.ifNull(fritekst("avtaleland")),
                    eksportTrygdeavtaleAvtaleland,
                    erEOSLand,
                    harOppfyltVedSammenlegging
                )
            )
            includePhrase(GarantitilleggHjemmel(garantitilleggInnvilget))
            includePhrase(GjenlevendetilleggKap19Hjemmel(gjenlevendetilleggKap19Innvilget))
            includePhrase(InnvilgetGjRettKap19For2024(gjenlevenderettAnvendt, gjenlevendetilleggKap19Innvilget))

            title1 {
                text(
                    bokmal { +"Andre utbetalinger" },
                    nynorsk { +"Andre utbetalingar" },
                    english { +"Other payments" }
                )
            }
            includePhrase(Utbetalingsinformasjon)
            includePhrase(ReguleringAvAlderspensjon)

            showIf(gjenlevendetilleggKap19Innvilget and gjenlevendetilleggKap19.greaterThan(0)) {
                includePhrase(
                    ReguleringAvGjenlevendetillegg
                )
            }

            showIf(uttaksgrad.equalTo(100) and borINorge and not(fullTrygdetid) and not(innvilgetFor67)) {
                includePhrase(SupplerendeStoenadAP)
            }

            showIf(erForstegangsbehandlingNorgeUtland and norgeBehandlendeLand) {
                includePhrase(InnvilgelseAPForeloepigBeregning)
            }

            showIf(borINorge or saksbehandlerValg.vanligSkattetrekk.ifNull(false)) {
                includePhrase(SkattAP)
            }.orShow {
                title1 {
                    text(
                        bokmal { +"Skatteregler for deg som bor i utlandet" },
                        nynorsk { +"Skattereglar for deg som bur i utlandet" },
                        english { +"Tax rules for people who live abroad" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du må i utgangspunktet betale kildeskatt til Norge når du bor i utlandet. Vi trekker derfor 15 prosent i skatt av pensjonen din." },
                        nynorsk { +"Du må i utgangspunktet betale kjeldeskatt til Noreg når du bur i utlandet. Vi trekkjer derfor 15 prosent i skatt av pensjonen din." },
                        english { +"As a general rule you have to pay withholding tax when you live abroad. We therefor deduct 15 percent tax from your pension." }
                    )
                }
                includePhrase(Skatteplikt)
            }

            showIf(saksbehandlerValg.etterbetaling.ifNull(false)) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            includePhrase(InnvilgelseAPUttakEndr(uforeKombinertMedAlder))

            includePhrase(
                ArbeidsinntektOgAlderspensjon(
                    innvilgetFor67, uttaksgrad, uforeKombinertMedAlder
                )
            )

            includePhrase(InfoPensjonFraAndreAP)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage)

            showIf(borIAvtaleland) { includePhrase(RettTilKlageUtland) }

            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025Dto)
        includeAttachmentIfNotNull(
            vedleggOpplysningerBruktIBeregningenAlder,
            pesysData.opplysningerBruktIBeregningenAlderspensjon
        )
        includeAttachmentIfNotNull(
            vedleggOpplysningerBruktIBeregningenAlderAP2025,
            pesysData.opplysningerBruktIBeregningenAlderspensjonAP2025
        )
        includeAttachmentIfNotNull(
            vedleggOpplysningerOmAvdoedBruktIBeregning,
            pesysData.opplysningerOmAvdodBruktIBeregning
        )
    }
}







