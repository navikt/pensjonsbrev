package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonSivilstandAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.BetydningForUtbetaling
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.DuFaarAP
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.EndringYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.InnvilgetYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OmregningGarantiPen
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OpphoerYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OpphoerYtelseEPSOver2G
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.SivilstandHjemler
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstandAuto :
    AutobrevTemplate<EndringAvAlderspensjonSivilstandAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND_AUTO

    override val template =
        createTemplate(
            letterDataType = EndringAvAlderspensjonSivilstandAutoDto::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - endring av alderspensjon (sivilstand)",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            val harInntektOver2G = epsVedVirk.harInntektOver2G

            title {
                text(
                    bokmal { + "Vi har beregnet alderspensjon din på nytt fra " + kravVirkDatoFom.format() },
                    nynorsk { + "Vi har berekna alderspensjonen din på nytt frå " + kravVirkDatoFom.format() },
                    english { + "We have recalculated your retirement pension from " + kravVirkDatoFom.format() },
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(
                    kravAarsak.isOneOf(
                        KravArsakType.EPS_NY_YTELSE,
                        KravArsakType.EPS_NY_YTELSE_UT,
                    ),
                ) {
                    includePhrase(InnvilgetYtelseEPS(sivilstand))
                }.orShowIf(kravAarsak.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                    includePhrase(EndringYtelseEPS(sivilstand))
                }.orShowIf(
                    kravAarsak.isOneOf(
                        KravArsakType.EPS_OPPH_YTELSE_UT,
                        KravArsakType.TILSTOT_OPPHORT,
                    ),
                ) {
                    showIf(not(harInntektOver2G)) {
                        includePhrase(OpphoerYtelseEPS(sivilstand))
                    } orShow {
                        includePhrase(OpphoerYtelseEPSOver2G(sivilstand))
                    }
                }

                includePhrase(OmregningGarantiPen(regelverkType))

                includePhrase(BetydningForUtbetaling(regelverkType, beloepEndring))

                showIf(alderspensjonVedVirk.ufoereKombinertMedAlder) {
                    includePhrase(
                        UfoereAlder.DuFaar(
                            beregnetPensjonPerManedVedVirk.totalPensjon,
                            kravVirkDatoFom,
                        ),
                    )
                }.orShow {
                    includePhrase(
                        DuFaarAP(
                            kravVirkDatoFom = kravVirkDatoFom,
                            totalPensjon = beregnetPensjonPerManedVedVirk.totalPensjon,
                        ),
                    )
                }

                includePhrase(Utbetalingsinformasjon)

                includePhrase(
                    SivilstandHjemler(
                        regelverkType = regelverkType,
                        kravArsakType = kravAarsak,
                        sivilstand = sivilstand,
                        saertilleggInnvilget = alderspensjonVedVirk.saertilleggInnvilget,
                        pensjonstilleggInnvilget = alderspensjonVedVirk.pensjonstilleggInnvilget,
                        minstenivaaIndividuellInnvilget = alderspensjonVedVirk.minstenivaaIndividuellInnvilget,
                        minstenivaaPensjonistParInnvilget = alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget,
                        garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget,
                        saerskiltSatsErBrukt = saerskiltSatsErBrukt,
                    ),
                )

                showIf(beloepEndring.isOneOf(BeloepEndring.ENDR_RED, BeloepEndring.ENDR_OKT)) {
                    includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
                }

                includePhrase(
                    ArbeidsinntektOgAlderspensjon(
                        innvilgetFor67 = alderspensjonVedVirk.innvilgetFor67,
                        uttaksgrad = alderspensjonVedVirk.uttaksgrad.ifNull(then = (0)),
                        uforeKombinertMedAlder = alderspensjonVedVirk.ufoereKombinertMedAlder,
                    ),
                )

                includePhrase(InformasjonOmAlderspensjon)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }

            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikter,
                orienteringOmRettigheterOgPlikterDto,
            )

            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkatt,
                maanedligPensjonFoerSkattDto,
            )

            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkattAp2025,
                maanedligPensjonFoerSkattAP2025Dto,
            )
        }
}
