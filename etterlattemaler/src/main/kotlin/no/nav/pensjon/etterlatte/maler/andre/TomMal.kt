package no.nav.pensjon.etterlatte.maler.andre

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
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO

@TemplateModelHelpers
object TomMal : EtterlatteTemplate<ManueltBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TOM_MAL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Tom mal",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Dette er en tom mal som du må benytte for å fylle inn brevet",
                    Nynorsk to "",
                    English to ""
                )
            }

        }
    }
}
