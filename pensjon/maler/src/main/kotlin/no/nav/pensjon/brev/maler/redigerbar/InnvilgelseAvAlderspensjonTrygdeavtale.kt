package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
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
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.InngangOgEksportVurderingSelectors.harOppfyltVedSammenlegging
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.avtalelandNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erMellombehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erSluttbehandlingNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.fullTrygdtid
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderspensjonAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.vedtaksresultatUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.nyBeregningAvInnvilgetAP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.antallLandVilkarsprovd
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.VedtaksresultatUtlandSelectors.landNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

// MF_000097 / AP_INNV_AVT_MAN

@TemplateModelHelpers
object InnvilgelseAvAlderspensjonTrygdeavtale : RedigerbarTemplate<InnvilgelseAvAlderspensjonTrygdeavtaleDto> {

    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon (trygdeavtale)",
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val afpPrivatResultatFellesKontoret = pesysData.safe { afpPrivatResultatFellesKontoret }.ifNull(false)
        val antallLandVilkarsprovd = pesysData.safe { vedtaksresultatUtland }.safe { antallLandVilkarsprovd }.ifNull(then = (0))
        val avtalelandNavn = pesysData.safe { avtalelandNavn }.ifNull(then = fritekst("AVTALELAND"))
        val borIAvtaleland = pesysData.borIAvtaleland
        val borINorge = pesysData.borINorge
        val eksportTrygdeavtaleAvtaleland =
            pesysData.safe { inngangOgEksportVurdering }.safe { eksportTrygdeavtaleAvtaleland }.ifNull(then = false)
        val eksportTrygdeavtaleEOS =
            pesysData.safe { inngangOgEksportVurdering }.safe { eksportTrygdeavtaleEOS }.ifNull(then = false)
        val erEOSLand = pesysData.erEOSLand
        val erMellombehandling = pesysData.erMellombehandling
        val erSluttbehandlingNorgeUtland = pesysData.erSluttbehandlingNorgeUtland
        val fullTrygdetid = pesysData.fullTrygdtid
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val garantitilleggInnvilget = pesysData.alderspensjonVedVirk.garantitilleggInnvilget
        val godkjentYrkesskade = pesysData.alderspensjonVedVirk.godkjentYrkesskade
        val harFlereBeregningsperioder = pesysData.harFlereBeregningsperioder
        val harOppfyltVedSammenlegging =
            pesysData.safe { inngangOgEksportVurdering }.safe { harOppfyltVedSammenlegging }.ifNull(then = false)
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val landNavn = pesysData.safe { vedtaksresultatUtland }.safe { landNavn }
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val regelverkType = pesysData.regelverkType
        val skjermingstilleggInnvilget = pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.ifNull(Kroner(0))
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad


        title {
            showIf(not(erMellombehandling) and saksbehandlerValg.nyBeregningAvInnvilgetAP) {
                includePhrase(BeregnaPaaNytt(pesysData.kravVirkDatoFom))
            }.orShow {
                text(
                    bokmal { + "Vi har innvilget søknaden din om " + uttaksgrad.format() + " prosent alderspensjon" },
                    nynorsk { + "Vi har innvilga søknaden din om " + uttaksgrad.format() + " prosent alderspensjon" },
                    english { + "We have granted your application for " + uttaksgrad.format() + " percent retirement pension" }
                )
            }
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            showIf(antallLandVilkarsprovd.greaterThan(0)) {
                // mottattInfoFraEttLand / mottattInfoFraFlereLan
                paragraph {
                    text(
                        bokmal { + "Vi har fått opplysninger fra utenlandske trygdemyndigheter om opptjeningen din i " },
                        nynorsk { + "Vi har fått opplysningar frå utanlandske trygdeorgan om oppteninga di i " },
                        english { + "We have received information from foreign national insurance authorities regarding your accumulated rights in " }
                    )
                    showIf(landNavn.isNull()) {
                        eval(fritekst("LANDNAVN"))
                    }
                    ifNotNull(landNavn) { land ->
                        showIf(land.isEmpty()) {
                            eval(fritekst("LANDNAVN"))
                        }.orShow {
                            eval(land.format())
                        }
                    }
                    text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                }
            }

            showIf(erMellombehandling) {
                // nyBeregningAPInnledn
                paragraph {
                    text(
                        bokmal { + "Dette gjør at du har rett til " + uttaksgrad.format() + " prosent alderspensjon fra "
                                + kravVirkDatoFom + "." },
                        nynorsk { + "Dette gjer at du har rett til " + uttaksgrad.format() + " prosent alderspensjon frå "
                                + kravVirkDatoFom + "." },
                        english { + "This makes you eligible for " + uttaksgrad.format() + " percent retirement pension from "
                                + kravVirkDatoFom + "." },
                    )
                }
            }.orShow {
                showIf(pesysData.beloepEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                    // nyBeregningAPØkning
                    paragraph {
                        text(
                            bokmal { + "Dette fører til at pensjonen din øker." },
                            nynorsk { + "Dette fører til at pensjonen din aukar." },
                            english { + "This leads to an increase in your retirement pension." }
                        )
                    }
                }.orShowIf(pesysData.beloepEndring.equalTo(BeloepEndring.ENDR_RED)) {
                    // nyBeregningAPReduksjon
                    paragraph {
                        text(
                            bokmal { + "Dette fører til at pensjonen din blir redusert." },
                            nynorsk { + "Dette fører til at pensjonen din blir redusert." },
                            english { + "This leads to a reduction in your retirement pension." }
                        )
                    }
                }.orShow {
                    // nyBeregningAPIngenEndring
                    paragraph {
                        text(
                            bokmal { + "Dette fører ikke til endringer i alderspensjonen din." },
                            nynorsk { + "Dette fører ikkje til endring av alderspensjonen din." },
                            english { + "This does not result in any changes in your retirement pension." },
                        )
                    }
                }
            }
            // innvilgelseAPogUTInnledn,innvilgelseAPInnledn,
            paragraph {
                text(
                    bokmal { + "Du får " + totalPensjon.format() + " hver måned før skatt fra " + kravVirkDatoFom },
                    nynorsk { + "Du får " + totalPensjon.format() + " kvar månad før skatt frå " + kravVirkDatoFom },
                    english { + "You will receive " + totalPensjon.format() + " every month before tax from " + kravVirkDatoFom },
                )
                showIf(uforeKombinertMedAlder) {
                    text(
                        bokmal { + ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                        nynorsk { + ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                        english { + ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." },
                    )
                }.orShow {
                    text(
                        bokmal { + " i alderspensjon fra folketrygden." },
                        nynorsk { + " i alderspensjon frå folketrygda." },
                        english { + " as retirement pension from the National Insurance Scheme." },
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

            showIf(saksbehandlerValg.etterbetaling.ifNull(false)) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            includePhrase(ReguleringAvAlderspensjon)
            includePhrase(InnvilgelseAPUttakEndr(uforeKombinertMedAlder))

            includePhrase(ArbeidsinntektOgAlderspensjon(innvilgetFor67, uttaksgrad, uforeKombinertMedAlder))

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
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningenAlderspensjon)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.opplysningerBruktIBeregningenAlderspensjonAP2025)
    }
}
