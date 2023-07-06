package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

@TemplateModelHelpers
object OMSOpphoerManuell : EtterlatteTemplate<ManueltBrevDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_OPPHOER_MANUELL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Manuelt brev for omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har opphørt omstillingsstønaden din",
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(Language.Bokmal)

            includePhrase(OMSFelles.DuHarRettTilAaKlageOpphoer)
            includePhrase(OMSFelles.DuHarRettTilInnsyn)
            includePhrase(OMSFelles.HarDuSpoersmaal)
        }
        includeAttachment(klageOgAnke, innhold)
    }
}
