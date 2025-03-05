package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat

@TemplateModelHelpers
object OmstillingsstoenadVarsel : EtterlatteTemplate<ManueltBrevMedTittelDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VARSEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevMedTittelDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varselbrev omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "",
                Language.Nynorsk to "",
                Language.English to ""
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)
        }
    }
}
