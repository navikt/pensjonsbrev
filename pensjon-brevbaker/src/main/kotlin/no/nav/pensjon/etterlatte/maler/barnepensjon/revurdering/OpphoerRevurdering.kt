package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

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
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat

@TemplateModelHelpers
object OpphoerRevurdering : EtterlatteTemplate<ManueltBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_OPPHOER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi opphører barnepensjonen din",
                Nynorsk to "Vi stansar barnepensjonen din",
                English to "We cease your child pension",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
    }
}
