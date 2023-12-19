package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.Innvilgelse
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avkortingsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.etterbetalinginfo
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.*
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.utbetalingsinfo

data class OMSInnvilgelseFoerstegangsvedtakDTO(
    override val innhold: List<Element>,
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo,
    val etterbetalinginfo: EtterbetalingDTO? = null,
    val beregningsinfo: Beregningsinfo,
    val avdoed: Avdoed,
): BrevDTO

@TemplateModelHelpers
object InnvilgelseFoerstegangsvedtak  : EtterlatteTemplate<OMSInnvilgelseFoerstegangsvedtakDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_FOERSTEGANGSVEDTAK_INNVILGELSE

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OMSInnvilgelseFoerstegangsvedtakDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
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
                Nynorsk to "",
                English to "",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            includePhrase(
                Innvilgelse.BegrunnelseForVedtaket(utbetalingsinfo.virkningsdato, avdoed)
            )

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Innvilgelse.Utbetaling(utbetalingsinfo.beregningsperioder, etterbetalinginfo))
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
        includeAttachmentIfNotNull(etterbetaling, etterbetalinginfo)
    }
}