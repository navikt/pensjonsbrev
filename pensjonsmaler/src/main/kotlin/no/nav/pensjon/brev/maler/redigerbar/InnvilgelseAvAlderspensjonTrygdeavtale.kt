package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
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
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erMellombehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erSluttbehandlingNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.fullTrygdtid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjonAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtaksresultatUtland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.nyBeregningAvInnvilgetAP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.antallLandVilkarsprovd_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.landNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AP2025TidligUttakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BeregnaPaaNytt
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
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isEmpty
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
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
        val avtalelandNavn = pesysData.avtalelandNavn_safe.ifNull(then = fritekst("AVTALELAND"))
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
        val landNavn = pesysData.vedtaksresultatUtland_safe.landNavn_safe
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val regelverkType = pesysData.regelverkType
        val skjermingstilleggInnvilget = pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.ifNull(Kroner(0))
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling


        title {
            showIf(not(erMellombehandling) and saksbehandlerValg.nyBeregningAvInnvilgetAP) {
                includePhrase(BeregnaPaaNytt(pesysData.kravVirkDatoFom))
            }.orShow {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            showIf(antallLandVilkarsprovd.greaterThan(0)) {
                // mottattInfoFraEttLand / mottattInfoFraFlereLan
                paragraph {
                    text(
                        Bokmal to "Vi har fått opplysninger fra utenlandske trygdemyndigheter om opptjeningen din i ",
                        Nynorsk to "Vi har fått opplysningar frå utanlandske trygdeorgan om oppteninga di i ",
                        English to "We have received information from foreign national insurance authorities regarding your accumulated rights in "
                    )
                    showIf(landNavn.isNull()) {
                        eval(fritekst("LANDNAVN"))
                    }
                    ifNotNull(landNavn) { land ->
                        showIf (land.isEmpty()) {
                            eval(fritekst("LANDNAVN"))
                        }.orShow {
                            eval(land.format())
                        }
                    }
                    text(Bokmal to ".", Nynorsk to ".", English to ".")
                }
            }

            showIf(erMellombehandling) {
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
            }.orShow {
                showIf(saksbehandlerValg.beloepEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                        // nyBeregningAPØkning
                        paragraph {
                            text(
                                Bokmal to "Dette fører til at pensjonen din øker.",
                                Nynorsk to "Dette fører til at pensjonen din aukar.",
                                English to "This leads to an increase in your retirement pension."
                            )
                        }
                    }.orShowIf(saksbehandlerValg.beloepEndring.equalTo(BeloepEndring.ENDR_RED)) {
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
                    Bokmal to "Du får ".expr() + totalPensjon.format() + " hver måned før skatt fra ".expr() + kravVirkDatoFom,
                    Nynorsk to "Du får ".expr() + totalPensjon.format() + " kvar månad før skatt frå ".expr() + kravVirkDatoFom,
                    English to "You will receive ".expr() + totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom,
                )
                showIf(uforeKombinertMedAlder) {
                    text(
                        Bokmal to ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit.",
                    )
                }.orShow {
                    text(
                        Bokmal to " i alderspensjon fra folketrygden.",
                        Nynorsk to " i alderspensjon frå folketrygda.",
                        English to " as retirement pension from the National Insurance Scheme.",
                    )
                }
            }

            showIf(privatAFPErBrukt) {
                includePhrase(AfpPrivatErBrukt(uttaksgrad))
            }

            showIf(afpPrivatResultatFellesKontoret) { includePhrase(SoktAFPPrivatInfo) }

            includePhrase(Utbetalingsinformasjon)

            showIf(harFlereBeregningsperioder and totalPensjon.greaterThan(0)) {
                includePhrase(Felles.FlereBeregningsperioder)
            }

            includePhrase(
                HjemlerInnvilgelseForAP2011AP2016(
                    garantipensjonInnvilget = garantipensjonInnvilget,
                    godkjentYrkesskade = godkjentYrkesskade,
                    innvilgetFor67 = innvilgetFor67,
                    pensjonstilleggInnvilget = pensjonstilleggInnvilget,
                    regelverkType = regelverkType
                )
            )
            includePhrase(SkjermingstilleggHjemmel(skjermingstilleggInnvilget))
            includePhrase(AP2025TidligUttakHjemmel(innvilgetFor67 = innvilgetFor67, regelverkType = regelverkType))
            includePhrase(GarantitilleggHjemmel(garantitilleggInnvilget))
            includePhrase(
                EOSLandAvtaleHjemmel(
                    borINorge = borINorge,
                    eksportTrygdeavtaleEOS = eksportTrygdeavtaleEOS,
                    erEOSLand = erEOSLand,
                    harOppfyltVedSammenlegging = harOppfyltVedSammenlegging
                )
            )
            includePhrase(
                BilateralAvtaleHjemmel(
                    avtalelandNavn = avtalelandNavn,
                    eksportTrygdeavtaleAvtaleland = eksportTrygdeavtaleAvtaleland,
                    erEOSLand = erEOSLand,
                    harOppfyltVedSammenlegging = harOppfyltVedSammenlegging
                )
            )

            showIf(not(erSluttbehandlingNorgeUtland)) {
                includePhrase(InnvilgelseAPForeloepigBeregning)
            }

            showIf(uttaksgrad.equalTo(100) and borINorge and not(fullTrygdetid) and not(innvilgetFor67)) {
                includePhrase(SupplerendeStoenadAP)
            }
            includePhrase(SkattAP)

            showIf(not(borINorge)) { includePhrase(Skatteplikt) }

            showIf(vedtakEtterbetaling) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            includePhrase(ReguleringAvAlderspensjon)
            includePhrase(InnvilgelseAPUttakEndr(uforeKombinertMedAlder))

            includePhrase(ArbeidsinntektOgAlderspensjon(innvilgetFor67, uttaksgrad, uforeKombinertMedAlder))

            includePhrase(InfoPensjonFraAndreAP)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))

            showIf(borIAvtaleland) { includePhrase(RettTilKlageUtland) }

            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025Dto)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningenAlderspensjon)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.opplysningerBruktIBeregningenAlderspensjonAP2025)
    }
}
