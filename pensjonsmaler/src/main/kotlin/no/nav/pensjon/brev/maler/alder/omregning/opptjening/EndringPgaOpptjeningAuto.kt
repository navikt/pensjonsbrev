package no.nav.pensjon.brev.maler.alder.omregning.opptjening

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.antallAarEndretOpptjening
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.belopEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.beregnetPensjonPerMaaned
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.beregnetPensjonPerMaanedGjeldende
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.beregnetPensjonPerMaanedVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.erFoerstegangsbehandling
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.opptjening
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.sisteGyldigeOpptjeningsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringPgaOpptjeningAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.omregning.opptjening.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndringPgaOpptjeningAuto : AutobrevTemplate<EndringPgaOpptjeningAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_PGA_OPPTJENING_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringPgaOpptjeningAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon fordi opptjening er endret",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            textExpr(
                Language.Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + virkFom.format(),
                Language.Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + virkFom.format(),
                Language.English to "We have recalculated your retirement pension from ".expr() + virkFom.format(),
            )
        }
        outline {
            includePhrase(AvsnittBeskrivelse(opptjening, sisteGyldigeOpptjeningsAar, antallAarEndretOpptjening))
            includePhrase(AvsnittEndringPensjon(belopEndring))
            includePhrase(AvsnittUtbetalingPerMaaned(uforeKombinertMedAlder, beregnetPensjonPerMaanedGjeldende))
            includePhrase(AvsnittFlereBeregningsperioder(beregnetPensjonPerMaaned, beregnetPensjonPerMaanedVedVirk, regelverkType))
            includePhrase(AvsnittHjemmel(opptjening, regelverkType, beregnetPensjonPerMaanedVedVirk, erFoerstegangsbehandling))
            includePhrase(AvsnittBegrunnelseForVedtaket(opptjening, antallAarEndretOpptjening, regelverkType))
            includePhrase(AvsnittEtterbetaling(virkFom, opptjening, belopEndring, antallAarEndretOpptjening))
            includePhrase(AvsnittSkattApEndring(borINorge))
            includePhrase(AvsnittArbeidsinntekt(beregnetPensjonPerMaanedVedVirk.uttaksgrad, uforeKombinertMedAlder))
            includePhrase(AvsnittLesMerOmAlderspensjon())
            includePhrase(AvsnittMeldFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
            includePhrase(Felles.RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}