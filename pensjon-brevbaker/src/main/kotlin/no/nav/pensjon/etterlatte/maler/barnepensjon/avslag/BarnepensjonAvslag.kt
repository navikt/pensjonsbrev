package no.nav.pensjon.etterlatte.maler.barnepensjon.avslag

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
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.klageOgAnkeUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.klageOgAnkeNasjonal

data class BarnepensjonAvslagDTO(
    override val innhold: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
) : BrevDTO

@TemplateModelHelpers
object BarnepensjonAvslag : EtterlatteTemplate<BarnepensjonAvslagDTO>, Hovedmal {
    override val kode = EtterlatteBrevKode.BARNEPENSJON_AVSLAG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonAvslagDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har avslått søknaden din om barnepensjon",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaalNy(brukerUnder18Aar, bosattUtland))
        }

        // Nasjonal
        includeAttachment(klageOgAnkeNasjonal, innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnkeUtland, innhold, bosattUtland)
    }
}