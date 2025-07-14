package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.garantitilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.harOppfyltVedSammenlegging_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.avtalelandNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erMellombehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erSluttbehandlingNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.fullTrygdtid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtaksresultatUtland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.innvilgelseAPellerOektUttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.nyBeregningAvInnvilgetAP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.oekningIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.reduksjonIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.supplerendeStoenad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.antallLandVilkarsprovd_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.landNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AP2025TidligUttakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BilateralAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.EOSLandAvtaleHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.GarantitilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.HjemlerInnvilgelseForAP2011AP2016
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InfoPensjonFraAndreAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPForeloepigBeregning
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InnvilgelseAPUttakEndr
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ReguleringAvAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilKlageUtland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkjermingstilleggHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SkattAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Skatteplikt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SoktAFPPrivatInfo
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenadAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
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
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

// MF_000097 / AP_INNV_AVT_MAN

@TemplateModelHelpers
object InnvilgelseAvAlderspensjonTrygdeavtale : RedigerbarTemplate<InnvilgelseAvAlderspensjonTrygdeavtaleDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonTrygdeavtaleDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon (trygdeavtale)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val afpPrivatResultatFellesKontoret = pesysData.afpPrivatResultatFellesKontoret_safe.ifNull(false)
        val antallLandVilkarsprovd = pesysData.vedtaksresultatUtland_safe.antallLandVilkarsprovd_safe.ifNull(then = (0))
        val avtalelandNavn = pesysData.avtalelandNavn_safe.ifNull(then = "AVTALELAND")
        val borIAvtaleland = pesysData.borIAvtaleland
        val borINorge = pesysData.borINorge
        val eksportTrygdeavtaleAvtaleland =
            pesysData.inngangOgEksportVurdering_safe.eksportTrygdeavtaleAvtaleland_safe.ifNull(then = false)
        val eksportTrygdeavtaleEOS =
            pesysData.inngangOgEksportVurdering_safe.eksportTrygdeavtaleEOS_safe.ifNull(then = false)
        val erEOSLand = pesysData.erEOSLand
        val erMellombehandling = pesysData.erMellombehandling
        val erSluttbehandlingNorgeUtland = pesysData.erSluttbehandlingNorgeUtland
        val fullTrygdetid = pesysData.fullTrygdtid
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val garantitilleggInnvilget = pesysData.alderspensjonVedVirk.garantitilleggInnvilget
        val godkjentYrkesskade = pesysData.alderspensjonVedVirk.godkjentYrkesskade
        val harFlereBeregningsperioder = pesysData.harFlereBeregningsperioder
        val harOppfyltVedSammenlegging =
            pesysData.inngangOgEksportVurdering_safe.harOppfyltVedSammenlegging_safe.ifNull(then = false)
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val landNavn = pesysData.vedtaksresultatUtland_safe.landNavn_safe.ifNull(then = "LANDNAVN")
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val regelverkType = pesysData.regelverkType
        val skjermingstilleggInnvilget = pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.ifNull(Kroner(0))
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling


        title {
            showIf(erMellombehandling) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }.orShowIf(
                erSluttbehandlingNorgeUtland or not(erMellombehandling and not(erSluttbehandlingNorgeUtland))
            ) {
                showIf(saksbehandlerValg.innvilgelseAPellerOektUttaksgrad) {
                    textExpr(
                        Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                        Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                        English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                    )
                }
                showIf(saksbehandlerValg.nyBeregningAvInnvilgetAP) {
                    textExpr(
                        Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + kravVirkDatoFom,
                        Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom,
                        English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom
                    )
                }
            }
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            showIf(antallLandVilkarsprovd.greaterThan(0)) {
                // mottattInfoFraEttLand / mottattInfoFraFlereLan
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått opplysninger fra utenlandske trygdemyndigheter om opptjeningen din i ".expr()
                                + landNavn + ".",
                        Nynorsk to "Vi har fått opplysningar frå utanlandske trygdeorgan om oppteninga di i ".expr()
                                + landNavn + ".",
                        English to "We have received information from foreign national insurance authorities regarding your accumulated rights in ".expr()
                                + landNavn + "."
                    )
                }
            }

            showIf(erMellombehandling or saksbehandlerValg.innvilgelseAPellerOektUttaksgrad) {
                // nyBeregningAPInnledn
                paragraph {
                    textExpr(
                        Bokmal to "Dette gjør at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr()
                                + kravVirkDatoFom + ".",
                        Nynorsk to "Dette gjer at du har rett til ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr()
                                + kravVirkDatoFom + ".",
                        English to "This makes you eligible for ".expr() + uttaksgrad.format() + " percent retirement pension from ".expr()
                                + kravVirkDatoFom + ".",
                    )
                }
            }
            showIf(
                erSluttbehandlingNorgeUtland or
                        (not(erSluttbehandlingNorgeUtland) and not(erMellombehandling))
            ) {
                showIf(saksbehandlerValg.oekningIPensjonen) {
                        // nyBeregningAPØkning
                        paragraph {
                            text(
                                Bokmal to "Dette fører til at pensjonen din øker.",
                                Nynorsk to "Dette fører til at pensjonen din aukar.",
                                English to "This leads to an increase in your retirement pension."
                            )
                        }
                    }.orShowIf(saksbehandlerValg.reduksjonIPensjonen) {
                        // nyBeregningAPReduksjon
                        paragraph {
                            text(
                                Bokmal to "Dette fører til at pensjonen din blir redusert.",
                                Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                                English to "This leads to a reduction in your retirement pension."
                            )
                        }
                    }.orShow {
                    // nyBeregningAPIngenEndring
                    paragraph {
                        text(
                            Bokmal to "Dette fører ikke til endringer i alderspensjonen din.",
                            Nynorsk to "Dette fører ikkje til endring av alderspensjonen din.",
                            English to "This does not result in any changes in your retirement pension.",
                        )
                    }
                }
            }
// innvilgelseAPogUTInnledn,innvilgelseAPInnledn,
            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner hver måned før skatt fra ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        ifFalse = " i alderspensjon fra folketrygden."
                    ),
                    Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner kvar månad før skatt frå ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        ifFalse = " i alderspensjon frå folketrygda."
                    ),
                    English to "You will receive NOK ".expr() + totalPensjon.format() + " every month before tax from ".expr()
                            + kravVirkDatoFom + "".expr()
                            + ifElse(
                        uforeKombinertMedAlder,
                        ifTrue = ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit.",
                        ifFalse = " as retirement pension from the National Insurance Scheme."
                    )
                )
            }

            showIf(privatAFPErBrukt) {
                includePhrase(
                    AfpPrivatErBrukt(uttaksgrad)
                )
            }

            showIf(afpPrivatResultatFellesKontoret) { includePhrase(SoktAFPPrivatInfo) }

            includePhrase(Utbetalingsinformasjon)

            showIf(harFlereBeregningsperioder and totalPensjon.greaterThan(0)) {
                includePhrase(Felles.FlereBeregningsperioder)
            }

            includePhrase(
                HjemlerInnvilgelseForAP2011AP2016(
                    garantipensjonInnvilget, godkjentYrkesskade, innvilgetFor67, pensjonstilleggInnvilget, regelverkType
                )
            )
            includePhrase(SkjermingstilleggHjemmel(skjermingstilleggInnvilget))
            includePhrase(AP2025TidligUttakHjemmel(regelverkType))
            includePhrase(GarantitilleggHjemmel(garantitilleggInnvilget))
            includePhrase(
                EOSLandAvtaleHjemmel(
                    borINorge, eksportTrygdeavtaleEOS, erEOSLand, harOppfyltVedSammenlegging
                )
            )
            includePhrase(
                BilateralAvtaleHjemmel(
                    avtalelandNavn, eksportTrygdeavtaleAvtaleland, erEOSLand, borINorge
                )
            )

            showIf(not(erSluttbehandlingNorgeUtland)) {
                includePhrase(InnvilgelseAPForeloepigBeregning)
            }

            showIf(
                saksbehandlerValg.supplerendeStoenad and uttaksgrad.equalTo(100) and borINorge and not(
                    fullTrygdetid
                ) and not(innvilgetFor67)
            ) {
                includePhrase(SupplerendeStoenadAP)
            }
            showIf(borINorge) {

            }
            includePhrase(SkattAP)

            showIf(not(borINorge)) { includePhrase(Skatteplikt) }

            showIf(vedtakEtterbetaling or (saksbehandlerValg.etterbetaling and not(vedtakEtterbetaling))) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            includePhrase(ReguleringAvAlderspensjon)
            includePhrase(InnvilgelseAPUttakEndr(uforeKombinertMedAlder))

            includePhrase(
                ArbeidsinntektOgAlderspensjon(innvilgetFor67, uttaksgrad, uforeKombinertMedAlder)
            )
            includePhrase(InfoPensjonFraAndreAP)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))

            showIf(borIAvtaleland) { includePhrase(RettTilKlageUtland) }

            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkattAp2025,
            pesysData.maanedligPensjonFoerSkattAP2025Dto
        )
        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
// includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.)
// includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.)
// includeAttachmentIfNotNull(vedleggOpplysningerOmAvdodBruktIBeregning, pesysData.
    }
}
