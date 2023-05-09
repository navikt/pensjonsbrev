package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.*
import no.nav.pensjon.etterlatte.maler.EtterlatteBrevDtoSelectors.navn

data class EtterlatteBrevDto(val navn: String)

@TemplateModelHelpers
object EtterlatteBrev: EtterlatteTemplate<EtterlatteBrevDto> {
    override val kode = EtterlatteBrevKode.A_LETTER

    override val template = createTemplate(
        name = kode.name,
        letterDataType =  EtterlatteBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata("Et etterlatte brev", false, LetterMetadata.Distribusjonstype.VEDTAK, LetterMetadata.Brevtype.VEDTAKSBREV),
    ) {
        title { text(Bokmal to "Et eksempel brev") }

        outline {
            paragraph {
                textExpr(Bokmal to "Dette er et eksempelbrev til ".expr() + navn + ".")
            }
        }
    }
}