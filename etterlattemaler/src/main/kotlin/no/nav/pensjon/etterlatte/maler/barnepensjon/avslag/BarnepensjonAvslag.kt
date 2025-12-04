package no.nav.pensjon.etterlatte.maler.barnepensjon.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

data class BarnepensjonAvslagDTO(
    override val innhold: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object BarnepensjonAvslag : EtterlatteTemplate<BarnepensjonAvslagDTO>, Hovedmal {
    override val kode = EtterlatteBrevKode.BARNEPENSJON_AVSLAG

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Vi har avslått søknaden din om barnepensjon" },
                nynorsk { +"Vi har avslått søknaden din om barnepensjon" },
                english { +"We have rejected your application for a children's pension" },
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), bosattUtland)
    }
}
