package no.nav.pensjon.etterlatte.maler.barnepensjon.endring

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.EndringHovedmalDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.ny.BeregningsinfoBP
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.etterbetalingAvBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet

data class EndringHovedmalDTO(
    val erEndret: Boolean,
    val etterbetaling: EtterbetalingDTO? = null,
    val utbetalingsinfo: Utbetalingsinfo,
    val beregningsinfo: BeregningsinfoBP,
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
                Nynorsk to "Vi har ",
                English to "We have ",
            )
            showIf(erEndret) {
                text(
                    Bokmal to "endret",
                    Nynorsk to "endra",
                    English to "changed",
                )
            } orShow {
                text(
                    Bokmal to "vurdert",
                    Nynorsk to "vurdert",
                    English to "evaluated",
                )
            }
            text(
                Bokmal to " barnepensjonen din",
                Nynorsk to " barnepensjonen din",
                English to " your children's pension",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)
            includePhrase(
                BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon(
                    utbetalingsinfo.beregningsperioder,
                    etterbetaling
                )
            )
            includePhrase(BarnepensjonInnvilgelseFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonInnvilgelseFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(true.expr(), false.expr()))
        }
        includeAttachment(beregningAvBarnepensjon, beregningsinfo)
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, innhold)
        includeAttachment(dineRettigheterOgPlikter, true.expr())
        includeAttachmentIfNotNull(etterbetalingAvBarnepensjon, etterbetaling)
    }
}
