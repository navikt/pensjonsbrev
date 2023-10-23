package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.harRenter

data class TilbakekrevingInnholdDTO(
    val harRenter: Boolean // TODO EY-2806
)

@TemplateModelHelpers
object TilbakekrevingInnhold : EtterlatteTemplate<TilbakekrevingInnholdDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = TilbakekrevingInnholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Tilbakekreving",
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
            includePhrase(RedigerbartInnhold(harRenter))
        }
    }
}

private data class RedigerbartInnhold(
    val harRenter: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to """
                    INNHOLD KAN FYLLES INN HER
                """.trimIndent(),
                Nynorsk to """
                    INNHOLD KAN FYLLES INN HER
                """.trimIndent(),
                English to """
                    INNHOLD KAN FYLLES INN HER
                """.trimIndent()
            )
            showIf(harRenter) {
                text(
                    Bokmal to """
                    INNHOLD OM RENTER..
                """.trimIndent(),
                    Nynorsk to """
                    INNHOLD OM RENTER..
                """.trimIndent(),
                    English to """
                    INNHOLD OM RENTER..
                """.trimIndent()
                )
            }
        }
    }
}