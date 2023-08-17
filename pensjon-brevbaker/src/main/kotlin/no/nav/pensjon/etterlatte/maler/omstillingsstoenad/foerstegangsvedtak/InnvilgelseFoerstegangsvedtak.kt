package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.EtterbetalingDTOSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.fraser.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avkortingsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.etterbetalinginfo
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.*

data class OMSInnvilgelseFoerstegangsvedtakDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo,
    val etterbetalinginfo: EtterbetalingDTO,
    val beregningsinfo: Beregningsinfo,
    val avdoed: Avdoed
)

@TemplateModelHelpers
object InnvilgelseFoerstegangsvedtak  : EtterlatteTemplate<OMSInnvilgelseFoerstegangsvedtakDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_INNVILGELSE_FOERSTEGANGSVEDTAK

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OMSInnvilgelseFoerstegangsvedtakDTO::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Innvilget omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har innvilget søknaden din om omstillingsstønad",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            includePhrase(
                OMSInnvilgelse.Vedtak(
                    avkortingsinfo.virkningsdato,
                    avdoed.navn,
                    avdoed.doedsdato,
                    etterbetalinginfo.beregningsperioder
                )
            )
            includePhrase(OMSInnvilgelse.Utbetaling)
            includePhrase(OMSInnvilgelse.Aktivitetsplikt)
            includePhrase(OMSInnvilgelse.Inntektsendring)
            includePhrase(OMSInnvilgelse.Etteroppgjoer)
            includePhrase(OMSFelles.MeldFraOmEndringer)
            includePhrase(OMSFelles.DuHarRettTilAaKlage)
            includePhrase(OMSFelles.HarDuSpoersmaal)
        }

        includeAttachment(beregningAvOmstillingsstoenad, beregningsinfo)
        includeAttachment(informasjonOmOvergangsstoenad, avkortingsinfo)
        includeAttachment(dineRettigheterOgPlikterOMS, avkortingsinfo)
        includeAttachment(informasjonOmYrkesskade, avkortingsinfo)
        includeAttachment(etterbetaling, etterbetalinginfo)

    }
}