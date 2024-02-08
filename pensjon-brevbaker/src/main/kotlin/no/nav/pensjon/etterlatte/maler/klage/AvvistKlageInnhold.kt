package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType


data class AvvistKlageInnholdDTO(
    val sakType: SakType
)

@TemplateModelHelpers
object AvvistKlageInnhold : EtterlatteTemplate<AvvistKlageInnholdDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.AVVIST_KLAGE_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvvistKlageInnholdDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Avvist klage",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    ) {
        title {
            text(Language.Bokmal to "", Language.Nynorsk to "", Language.English to "")
        }

        outline {
            paragraph {
                text(
                    Language.Bokmal to "Dette er en placeholder for vedtaksbrevet for avvisning av klage",
                    Language.Nynorsk to "Dette er en placeholder for vedtaksbrevet for avvisning av klage",
                    Language.English to "Dette er en placeholder for vedtaksbrevet for avvisning av klage"
                )
            }
        }
    }
}