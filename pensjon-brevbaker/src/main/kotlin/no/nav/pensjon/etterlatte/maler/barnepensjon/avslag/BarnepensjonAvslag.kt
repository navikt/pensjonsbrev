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
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonAvslagFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

data class BarnepensjonAvslagDTO(
    override val innhold: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val erSluttbehandling: Boolean = false,
) : FerdigstillingBrevDTO

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
                Nynorsk to "Vi har avslått søknaden din om barnepensjon",
                English to "We have rejected your application for a children's pension",
            )
        }
        outline {
            includePhrase(BarnepensjonAvslagFraser.Vedtak(erSluttbehandling))

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), innhold, bosattUtland)
    }
}
