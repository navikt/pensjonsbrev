package no.nav.pensjon.brev.alder.maler.endring

import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.RettTilAAKlage
import no.nav.pensjon.brev.alder.maler.felles.RettTilInnsyn
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningeromavdodbruktiberegningen.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.endring.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.endring.selectors.beregnetPensjonPerMaanedVedVirk.*
import no.nav.pensjon.brev.alder.model.endring.selectors.endringPgaOpptjeningAutoDto.*
import no.nav.pensjon.brev.alder.model.endring.selectors.opptjening.*
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
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ENDRING_PGA_OPPTJENING_AUTO

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
            includePhrase(RettTilAAKlage)
            includePhrase(RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(HarDuSpoersmaal.alder)
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