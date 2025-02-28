package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

data class OmstillingstoenadAvslagDTO(
    override val innhold: List<Element>,
    val bosattUtland: Boolean,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslag : EtterlatteTemplate<OmstillingstoenadAvslagDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingstoenadAvslagDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Vedtak - avslag",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har avslått søknaden din om omstillingsstønad",
                Nynorsk to "Vi har avslått søknaden din om omstillingsstønad",
                English to "We have rejected your application for adjustment allowance",
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlageAvslagOpphoer)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), innhold, bosattUtland)
    }
}
