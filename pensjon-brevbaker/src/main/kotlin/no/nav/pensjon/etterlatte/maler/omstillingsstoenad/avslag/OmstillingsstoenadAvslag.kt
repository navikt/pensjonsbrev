package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

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
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.klageOgAnkeUtenTilbakekreving


data class OmstillingstoenadAvslagDTO(
    override val innhold: List<Element>,
    val avdoedNavn: String,
): BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslag : EtterlatteTemplate<OmstillingstoenadAvslagDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSTOENAD_AVSLAG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingstoenadAvslagDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har avslått søknaden din om omstillingsstønad",
                Nynorsk to "",
                English to "",
            )
        }

        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlageOpphoer)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        includeAttachment(klageOgAnkeUtenTilbakekreving, innhold)
    }
}