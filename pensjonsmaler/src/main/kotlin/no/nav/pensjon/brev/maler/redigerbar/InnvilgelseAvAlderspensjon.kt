package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet_safe
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
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportForbud_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.harOppfyltVedSammenlegging_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArBotidKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetidKap20_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetid_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdodNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avtalelandNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.erForstegangsbehandletNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.faktiskBostedsland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.fullTrygdtid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.harAvdod_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.norgeBehandlendeLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.egenOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevenderett
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.ikkeKildeskatt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.kildeskatt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.kravVirkDatoFomSenereEnnOensketUttakstidspunkt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.supplerendeStoenad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HjemlerInnvilgelseForAP2011AP2016
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoSkattAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPForeloepigBeregning
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPUttakEndr
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvGjenlevendetillegg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilKlageUtland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Skatteplikt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SoktAFPPrivatInfo
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenadAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// MF_000092 / AP_INNV_MAN
/* Tekster og logikk mht ektefelletillegg og barnetillegg er fjernet fra brevmalen etter en samtale med Ingrid Strand:
innvETAP, innvBTAP, innvETBTAP, innvFTAPIngenUtbInntekt, innvFTAPIngenUtbSamletInntekt, innvETBTAIngenUtbInntekt, innvETBTAIngenUtbSamletInntekt,
innvilgetETAPHjemmel, innvilgetBTAPHjemmel, InnvilgetETBTAHjemmel, innvilgetGjrettOgTilleggKap20 fjernet
hvisFlyttetET, hvisFlyttetBT, hvisFlyttetETogBT */

@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val afpPrivatResultatFellesKontoret = pesysData.afpPrivatResultatFellesKontoret_safe.ifNull(then = false)
        val avdodNavn = pesysData.avdodNavn_safe.ifNull(then = "AVDØDSNAVN")
        val avtalelandNavn = pesysData.avtalelandNavn_safe.ifNull(then = "AVTALELAND")
        val borIAvtaleland = pesysData.borIAvtaleland
        val brukerBorINorge = pesysData.borINorge
        val eksportForbud = pesysData.inngangOgEksportVurdering.eksportForbud_safe.ifNull(then = false)
        val eksportTrygdeavtaleAvtaleland =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland_safe.ifNull(then = false)
        val eksportTrygdeavtaleEOS =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOS_safe.ifNull(then = false)
        val erEOSLand = pesysData.erEOSLand
        val erEksportberegnet = pesysData.alderspensjonVedVirk.erEksportberegnet_safe.ifNull(then = false)
        val erForstegangsbehandlingNorgeUtland = pesysData.erForstegangsbehandletNorgeUtland
        val fakstiskBostedsland = pesysData.faktiskBostedsland_safe.ifNull(then = "BOSTEDSLAND")
        val fullTrygdetid = pesysData.fullTrygdtid
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val garantitilleggInnvilget = pesysData.alderspensjonVedVirk.garantitilleggInnvilget
        val gjenlevenderettAnvendt = pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
        val gjenlevendetilleggInnvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val gjenlevendetilleggKap19 = pesysData.gjenlevendetilleggKap19_safe.ifNull(then = Kroner(0))
        val gjenlevendetilleggKap19Innvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggKap19Innvilget
        val godkjentYrkesskade = pesysData.alderspensjonVedVirk.godkjentYrkesskade
        val harAvdod = pesysData.harAvdod_safe.ifNull(then = false)
        val harFlereBeregningsperioder = pesysData.harFlereBeregningsperioder
        val harOppfyltVedSammenlegging =
            pesysData.inngangOgEksportVurdering.harOppfyltVedSammenlegging_safe.ifNull(then = false)
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val minst20ArBotidKap19 = pesysData.inngangOgEksportVurdering.minst20ArBotidKap19_safe.ifNull(then = false)
        val minst20ArTrygdetid = pesysData.inngangOgEksportVurdering.minst20ArTrygdetid_safe.ifNull(then = false)
        val minst20ArTrygdetidKap20 =
            pesysData.inngangOgEksportVurdering.minst20ArTrygdetidKap20_safe.ifNull(then = false)
        val norgeBehandlendeLand = pesysData.norgeBehandlendeLand
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val regelverkType = pesysData.regelverkType
        val skjermingstilleggInnvilget = pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.ifNull(Kroner(0))
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling


        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
            )
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {
            showIf(gjenlevendetilleggKap19Innvilget) {
                //  beloepApOgGjtvedVirkMedDato_001, beloepApOgGjvedVirkMedDato_002
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt fra ".expr() +
                                kravVirkDatoFom + ".",
                        Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt frå ".expr() +
                                kravVirkDatoFom + ".",
                        English to "You will receive NOK ".expr() + totalPensjon.format() + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax from ".expr() +
                                kravVirkDatoFom + "."
                    )
                    showIf(gjenlevendetilleggInnvilget) {
                        textExpr(
                            Bokmal to " Av dette er gjenlevendetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            Nynorsk to " Av dette er attlevandetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            English to " Of this, the survivor’s supplement is NOK ".expr() + gjenlevendetilleggKap19.format() + "."
                        )
                    }
                    showIf(uforeKombinertMedAlder) {
                        text(
                            Bokmal to " Dette er i tillegg til uføretrygden din.",
                            Nynorsk to " Dette er i tillegg til uføretrygda di.",
                            English to " This is in addition to your disability benefit."
                        )
                    }
                }
            }

            showIf(not(gjenlevendetilleggInnvilget) and not(gjenlevendetilleggKap19Innvilget)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom,
                        Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom,
                        English to "You will receive NOK ".expr() + totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom
                    )
                    showIf(
                        uforeKombinertMedAlder and innvilgetFor67
                    ) {
                        // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                        text(
                            Bokmal to ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                            Nynorsk to ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                            English to ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                        )
                    }.orShow {
                        // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                        text(
                            Bokmal to " i alderspensjon fra folketrygden.",
                            Nynorsk to " i alderspensjon frå folketrygda.",
                            English to " as retirement pension from the National Insurance Scheme."
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.kravVirkDatoFomSenereEnnOensketUttakstidspunkt) {
                // invilgelseAPVirkfom
                paragraph {
                    text(
                        Bokmal to "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. Dette går frem av folketrygdloven § 22-13.",
                        Nynorsk to "Vi kan tidlegast innvilge søknaden din månaden etter at du søkte. Dette går fram av folketrygdlova § 22-13.",
                        English to "We can at the earliest grant your application the month after you applied. This is in accordance with the regulations of § 22-13 of the National Insurance Act."
                    )
                }
            }

            showIf(harFlereBeregningsperioder and totalPensjon.greaterThan(0)) {
                includePhrase(Felles.FlereBeregningsperioder)
            }

            showIf(harAvdod) {
                showIf(
                    saksbehandlerValg.harGjenlevenderett
                            and gjenlevenderettAnvendt and not(gjenlevendetilleggKap19Innvilget) and not(
                        gjenlevendetilleggInnvilget
                    )
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter ".expr() + avdodNavn + ". Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening.",
                            Nynorsk to "I beregninga vår har vi teke utgangspunkt i pensjonsrettar du har etter ".expr() + avdodNavn + ". Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening.",
                            English to "We have based the calculation on the pension rights you have after ".expr() + avdodNavn + ". This gives you a higher pension than if we had only based the calculation on your own earnings."
                        )
                    }
                }

                showIf(
                    saksbehandlerValg.harGjenlevendetillegg and gjenlevenderettAnvendt and gjenlevendetilleggKap19.greaterThan(
                        0
                    )
                ) {
                    // beregningAPGjtOpptj
                    paragraph {
                        text(
                            Bokmal to "Fra januar 2024 er gjenlevenderett i alderspensjonen din skilt ut som et eget gjenlevendetillegg." +
                                    " Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                            Nynorsk to "Frå januar 2024 er attlevanderett i alderspensjonen din skild ut som eit eige attlevandetillegg." +
                                    " Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                            English to "From January 2024, the survivor’s right in your retirement pension is separated out as a separate survivor’s supplement." +
                                    " The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år.",
                            Nynorsk to "Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år.",
                            English to "The survivor's supplement will not be adjusted when the pension increases from 1 May every year."
                        )
                    }
                }

                showIf(saksbehandlerValg.harGjenlevendetilleggKap19 and gjenlevendetilleggKap19Innvilget) {
                    // beregningAPGjtKap19
                    paragraph {
                        textExpr(
                            Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr()
                                    + avdodNavn + ".",
                            Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr()
                                    + avdodNavn + ".",
                            English to "You receive a survivor’s supplement in retirement pension because you have pension rights after ".expr()
                                    + avdodNavn + "."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                            Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                            English to "The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself."
                        )
                    }
                }

                showIf(saksbehandlerValg.egenOpptjening and not(gjenlevenderettAnvendt)) {
                    // beregningAPGjRettOpptjEgen_002
                    title1 {
                        text(
                            Bokmal to "Gjenlevenderett i alderspensjon",
                            Nynorsk to "Attlevenderett i alderspensjon",
                            English to "Survivor's rights in retirement pension"
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter ".expr()
                                    + avdodNavn + ".",
                            Nynorsk to "I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter ".expr()
                                    + avdodNavn + ".",
                            English to "We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after ".expr()
                                    + avdodNavn + "."
                        )
                    }
                }
            }

            showIf(privatAFPErBrukt) {
                includePhrase(
                    AfpPrivatErBrukt(uttaksgrad = uttaksgrad)
                )
            }

            showIf(afpPrivatResultatFellesKontoret) { includePhrase(SoktAFPPrivatInfo) }

            showIf(erEksportberegnet and not(eksportForbud) and not(minst20ArTrygdetid)) {
                // innvilgelseAPUnder20aar
                paragraph {
                    textExpr(
                        Bokmal to "Du har ikke vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du bor i ".expr()
                                + fakstiskBostedsland + ".",
                        Nynorsk to "Du har ikkje vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr()
                                + fakstiskBostedsland + ".",
                        English to "You have not been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr()
                                + fakstiskBostedsland + "."
                    )
                }
            }

            showIf(
                erEksportberegnet and not(eksportForbud) and harAvdod and not(minst20ArTrygdetidKap20) and not(
                    minst20ArBotidKap19
                )
            ) {
                // innvilgelseAPUnder20aarAvdod
                paragraph {
                    textExpr(
                        Bokmal to "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du når du bor i ".expr()
                                + fakstiskBostedsland + ".",
                        Nynorsk to "Verken du eller avdøde har vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr()
                                + fakstiskBostedsland + ".",
                        English to "Neither you nor the deceased have been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr()
                                + fakstiskBostedsland + "."
                    )
                }
            }

            showIf(uttaksgrad.lessThan(100) and not(uforeKombinertMedAlder)) {
                paragraph {
                    text(
                        Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                        Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                        English to "You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                    )
                }
            }

            showIf(eksportTrygdeavtaleEOS or eksportTrygdeavtaleAvtaleland) {
                // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
                paragraph {
                    textExpr(
                        Bokmal to "Vi forutsetter at du bor i ".expr() + fakstiskBostedsland + ". Hvis du skal flytte til et ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utenfor EØS-området",
                            ifFalse = "annet land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                        Nynorsk to "Vi føreset at du bur i ".expr() + fakstiskBostedsland + ". Dersom du skal flytte til eit ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utanfor EØS-området",
                            ifFalse = "anna land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon.",
                        English to "We presume that you live in ".expr() + fakstiskBostedsland + ". If you are moving to ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "a country outside the EEA region",
                            ifFalse = "another country"
                        ) + ", it is important that you contact Nav. We will then reassess your eligibility for retirement pension."
                    )
                }
            }

            includePhrase(
                HjemlerInnvilgelseForAP2011AP2016(
                    garantipensjonInnvilget = garantipensjonInnvilget,
                    godkjentYrkesskade = godkjentYrkesskade,
                    innvilgetFor67 = innvilgetFor67,
                    pensjonstilleggInnvilget = pensjonstilleggInnvilget,
                    regelverkType = regelverkType,
                )
            )

            showIf(skjermingstilleggInnvilget) {
                // skjermingstilleggHjemmel
                paragraph {
                    text(
                        Bokmal to "Du er også innvilget skjermingstillegg etter folketrygdloven § 19-9a.",
                        Nynorsk to "Du er også innvilga skjermingstillegg etter folketrygdlova § 19-9a.",
                        English to "You have also been granted the supplement for the disabled pursuant to the provisions of § 19-9a of the National Insurance Act."
                    )
                }
            }

            showIf(regelverkType.isOneOf(AP2025) and innvilgetFor67) {
                // AP2025TidligUttakHjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                        English to "This decision was made pursuant to the provisions of §§ 20-2, 20-3, 20-9 to 20-15, 22-12 and 22-13 of the National Insurance Act."
                    )
                }
            }

            showIf(garantitilleggInnvilget) {
                // garantitilleggHjemmel
                paragraph {
                    text(
                        Bokmal to "Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20.",
                        Nynorsk to "Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20.",
                        English to "You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act."
                    )
                }
            }

            showIf(gjenlevendetilleggKap19Innvilget) {
                // innvilgetGjtKap19Hjemmel
                paragraph {
                    text(
                        Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                        Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                        English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 " +
                                "and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                    )
                }
            }

            showIf(gjenlevenderettAnvendt and not(gjenlevendetilleggKap19Innvilget)) {
                // innvilgetGjRettKap19For2024
                paragraph {
                    text(
                        Bokmal to "Gjenlevenderett er innvilget etter § 19-16 i folketrygdloven.",
                        Nynorsk to "Attlevanderett er innvilga etter § 19-16 i folketrygdlova.",
                        English to "The survivor's rights in your retirement pension has been granted pursuant to the provisions of § 19-16 of the National Insurance Act"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                        Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                        English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the " +
                                "National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                    )
                }
            }

            showIf(erEOSLand) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004,",
                        Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004,",
                        English to "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004,"
                    )
                    showIf(harOppfyltVedSammenlegging and brukerBorINorge) {
                        // euArt6Og7Hjemmel
                        text(Bokmal to " artikkel 6.", Nynorsk to " artikkel 6.", English to " article 6.")
                    }.orShowIf(harOppfyltVedSammenlegging and eksportTrygdeavtaleEOS and not(brukerBorINorge)) {
                        text(
                            Bokmal to " artikkel 6 og 7.",
                            Nynorsk to " artikkel 6 og 7.",
                            English to " articles 6 and 7."
                        )
                    }
                        .orShowIf(not(harOppfyltVedSammenlegging) and eksportTrygdeavtaleEOS and not(brukerBorINorge)) {
                            text(Bokmal to " artikkel 7.", Nynorsk to " artikkel 7.", English to " article 7.")
                        }
                }
            }

// bilateralAvtaleHjemmel
            showIf((harOppfyltVedSammenlegging or eksportTrygdeavtaleAvtaleland) and not(erEOSLand)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vedtaket er også gjort etter reglene i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                        Nynorsk to "Vedtaket er også gjort etter reglane i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                        English to "This decision was also made pursuant the provisions of the Social Security Agreement with ".expr() + avtalelandNavn + "."
                    )
                }
            }

            includePhrase(Utbetalingsinformasjon)
            includePhrase(ReguleringAvAlderspensjon)

            showIf(gjenlevendetilleggKap19Innvilget) { includePhrase(ReguleringAvGjenlevendetillegg) }

            showIf(
                saksbehandlerValg.supplerendeStoenad and uttaksgrad.equalTo(100) and brukerBorINorge and not(
                    fullTrygdetid
                ) and not(innvilgetFor67)
            ) {
                includePhrase(SupplerendeStoenadAP)
            }

            showIf(erForstegangsbehandlingNorgeUtland and norgeBehandlendeLand) {
                includePhrase(InnvilgelseAPForeloepigBeregning)
            }

            showIf(brukerBorINorge) {
                includePhrase(InfoSkattAP)
            }.orShowIf(saksbehandlerValg.kildeskatt) {
                title1 {
                    text(
                        Bokmal to "Skatteregler for deg som bor i utlandet",
                        Nynorsk to "Skattereglar for deg som bur i utlandet",
                        English to "Tax rules for people who live abroad"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du må i utgangspunktet betale kildeskatt til Norge når du bor i utlandet. Vi trekker derfor 15 prosent i skatt av pensjonen din.",
                        Nynorsk to "Du må i utgangspunktet betale kjeldeskatt til Noreg når du bur i utlandet. Vi trekkjer derfor 15 prosent i skatt av pensjonen din.",
                        English to "As a general rule you have to pay withholding tax when you live abroad. We therefor deduct 15 percent tax from your pension."
                    )
                }
                includePhrase(Skatteplikt)
            }.orShowIf(saksbehandlerValg.ikkeKildeskatt) {
                title1 {
                    text(
                        Bokmal to "Det er egne skatteregler for pensjon",
                        Nynorsk to "Det er eigne skattereglar for pensjon",
                        English to "Pensions are subject to special tax rules"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL." +
                                " Der får du også mer informasjon om skattekort for pensjonister. Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss.",
                        Nynorsk to "Du bør endre skattekortet når du byrjar å ta ut alderspensjon. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL." +
                                " Der får du også meir informasjon om skattekort for pensjonistar. Vi får skattekortet elektronisk. Du skal derfor ikkje sende det til oss.",
                        English to "When you start draw retirement pension, you should change your tax deduction card. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL." +
                                " There you will find more information regarding tax deduction card for pensioners. We will receive the tax card directly from the Norwegian Tax Administration, meaning you do not need to send it to us.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "På $DIN_PENSJON_URL kan du se hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det." +
                                " Dersom du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed.",
                        Nynorsk to "På $DIN_PENSJON_URL kan du sjå kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det." +
                                " Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                        English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want." +
                                " If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                    )
                }
                includePhrase(Skatteplikt)
            }

            showIf(saksbehandlerValg.etterbetaling or vedtakEtterbetaling) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            includePhrase(InnvilgelseAPUttakEndr)

            showIf(uforeKombinertMedAlder and innvilgetFor67) {
                // innvilgelseAPUttakEndrUT
                paragraph {
                    text(
                        Bokmal to "Summen av uføregraden og alderspensjonen din kan ikke overstige 100 prosent.",
                        Nynorsk to "Summen av uføregraden og alderspensjonen din kan ikkje gå over 100 prosent.",
                        English to "The percentage of disability benefit and the percentage of retirement pension combined may not exceed 100 percent."
                    )
                }
            }

            includePhrase(
                ArbeidsinntektOgAlderspensjon(
                    uttaksgrad = uttaksgrad, uforeKombinertMedAlder = uforeKombinertMedAlder
                )
            )

            includePhrase(InfoPensjonFraAndreAP)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))

            showIf(borIAvtaleland) { includePhrase(RettTilKlageUtland) }

            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025Dto)
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
        // includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.)
        // includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.)
        // includeAttachmentIfNotNull(vedleggOpplysningerOmAvdodBruktIBeregning, pesysData.
    }
}







