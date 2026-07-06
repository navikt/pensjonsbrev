package no.nav.pensjon.brev.maler.alder.omregning.opptjening

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.selectors.beregnetPensjonPerMaanedVedVirk.*
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.selectors.endringPgaOpptjeningAutoDto.*
import no.nav.pensjon.brev.api.model.maler.alderApi.selectors.opptjening.*
import no.nav.pensjon.brev.maler.alder.omregning.opptjening.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Konvertert tidligere 120-brev fra Doksys
@TemplateModelHelpers
object EndringPgaOpptjeningAuto : AutobrevTemplate<EndringPgaOpptjeningAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_PGA_OPPTJENING_AUTO

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon fordi opptjening er endret",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Vi har beregnet alderspensjonen din på nytt fra " + virkFom.format() },
                nynorsk { +"Vi har berekna alderspensjonen din på nytt frå " + virkFom.format() },
                english { +"We have recalculated your retirement pension from " + virkFom.format() },
            )
        }
        outline {
            includePhrase(AvsnittBeskrivelse(opptjeningType, opptjening))
            includePhrase(AvsnittEndringPensjon(belopEndring))
            includePhrase(AvsnittUtbetalingPerMaaned(uforeKombinertMedAlder, beregnetPensjonPerMaanedGjeldende))
            includePhrase(
                AvsnittFlereBeregningsperioder(
                    beregnetPensjonPerMaaned,
                    beregnetPensjonPerMaanedVedVirk,
                    regelverkType
                )
            )
            includePhrase(
                AvsnittHjemmel(
                    opptjeningType,
                    regelverkType,
                    beregnetPensjonPerMaanedVedVirk,
                    erFoerstegangsbehandling
                )
            )
            includePhrase(
                AvsnittBegrunnelseForVedtaket(
                    opptjeningType,
                    opptjening.antallAarEndretOpptjening,
                    regelverkType
                )
            )
            includePhrase(
                AvsnittEtterbetaling(
                    virkFom,
                    opptjeningType,
                    belopEndring,
                    opptjening.antallAarEndretOpptjening
                )
            )
            includePhrase(AvsnittSkattApEndring(borINorge))
            includePhrase(AvsnittArbeidsinntekt(beregnetPensjonPerMaanedVedVirk.uttaksgrad, uforeKombinertMedAlder))
            includePhrase(AvsnittLesMerOmAlderspensjon())
            includePhrase(AvsnittMeldFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }

        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikter)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkatt)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, maanedligPensjonFoerSkattAP2025)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder,opplysningerBruktIBeregningenAlder)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, opplysningerBruktIBeregningenAlderAP2025)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening, opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening)
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning, opplysningerOmAvdoedBruktIBeregning)
    }
}