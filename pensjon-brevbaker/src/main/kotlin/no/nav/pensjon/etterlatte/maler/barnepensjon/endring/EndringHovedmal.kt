package no.nav.pensjon.etterlatte.maler.barnepensjon.endring

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.etterbetalingAvBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonOmYrkesskade
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet
import java.time.LocalDate

data class EtterbetalingDTO(
    val beregningsperioder: List<Etterbetalingsperiode>,
)

data class Etterbetalingsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val grunnbeloep: Kroner,
    val stoenadFoerReduksjon: Kroner,
    var utbetaltBeloep: Kroner,
)

data class EndringHovedmalDTO(
    val erEndret: Boolean,
    val etterbetaling: EtterbetalingDTO,
    val utbetalingsinfo: Utbetalingsinfo,
    override val innhold: List<Element>,
) :
    BrevDTO

@TemplateModelHelpers
object Endring : EtterlatteTemplate<EndringHovedmalDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_ENDRING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringHovedmalDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har ",
                Nynorsk to "",
                English to "",
            )
            showIf(erEndret) {
                text(
                    Bokmal to "endret",
                    Nynorsk to "",
                    English to "",
                )
            } orShow {
                text(
                    Bokmal to "vurdert",
                    Nynorsk to "",
                    English to "",
                )
            }
            text(
                Bokmal to " barnepensjonen din",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)
            includePhrase(Barnepensjon.BeregnetPensjonTabell(utbetalingsinfo.beregningsperioder))
            includePhrase(Barnepensjon.UtbetalingOgRegulering)
            includePhrase(Barnepensjon.DuMaaMeldeFraOmEndringer)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, innhold)
        includeAttachment(dineRettigheterOgPlikter, innhold)
        includeAttachment(informasjonOmYrkesskade, innhold)
        includeAttachment(etterbetalingAvBarnepensjon, etterbetaling)
    }
}
