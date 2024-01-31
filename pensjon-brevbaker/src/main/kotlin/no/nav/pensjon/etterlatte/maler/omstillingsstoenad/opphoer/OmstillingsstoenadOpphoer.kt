package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

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
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeUtland

data class OmstillingsstoenadOpphoerDTO(
    override val innhold: List<Element>,
    val bosattUtland: Boolean,
): BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadOpphoer : EtterlatteTemplate<OmstillingsstoenadOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadOpphoerDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Opphør av omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har opphørt omstillingsstønaden din",
                Nynorsk to "Vi har avvikla omstillingsstønaden din",
                English to "We have terminated your transitional benefits",
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlageAvslagOpphoer)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        // Nasjonal
        includeAttachment(klageOgAnkeNasjonal, innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnkeUtland, innhold, bosattUtland)
    }
}