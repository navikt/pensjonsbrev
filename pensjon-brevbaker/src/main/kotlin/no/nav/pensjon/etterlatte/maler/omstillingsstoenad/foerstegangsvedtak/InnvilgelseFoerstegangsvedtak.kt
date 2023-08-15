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
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.AvkortingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.OMSInnvilgelse
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.avkortingsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.etterbetalinginfo
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.*
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTOSelectors.innhold

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
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_INNVILGELSE_FOERSTEGANGSVEDTAK

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

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(
                OMSInnvilgelse.Vedtak(
                    avkortingsinfo.virkningsdato,
                    avdoed.navn,
                    avdoed.doedsdato,
                    etterbetalinginfo
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
        includeAttachmentIfNotNull(etterbetaling, etterbetalinginfo)

    }
}