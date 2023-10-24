package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.harForeldelse
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.harRenter
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.harStrafferettslig
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.summer
import java.time.LocalDate

data class TilbakekrevingInnholdDTO(
    val sakType: SakType,
    val harRenter: Boolean,
    val harStrafferettslig: Boolean,
    val harForeldelse: Boolean,
    val perioder: List<TilbakekrevingPeriode>,
    val summer: TilbakekrevingBeloeper
)

enum class SakType {
    BP,
    OMS
}

data class TilbakekrevingPeriode(
    val maaned: LocalDate,
    val beloeper: TilbakekrevingBeloeper,
    val resulatat: String
)

data class TilbakekrevingBeloeper(
    val feilutbetaling: Kroner,
    val bruttoTilbakekreving: Kroner,
    val nettoTilbakekreving: Kroner,
    val fradragSkatt: Kroner,
    val renteTillegg: Kroner
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
            paragraph {
                val feilutbetaling = summer.feilutbetaling.format()
                textExpr(
                    Bokmal to "Det feilutbetalte beløpet er ".expr() + feilutbetaling + " kroner inkludert skatt.",
                    Nynorsk to "Det feilutbetalte beløpet er ".expr() + feilutbetaling + " kroner inkludert skatt.",
                    English to "Det feilutbetalte beløpet er ".expr() + feilutbetaling + " kroner inkludert skatt.",
                )
            }
            showIf(harRenter) {
                title2 {
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
            showIf(harStrafferettslig) {
                title2 {
                    text(
                        Bokmal to """
                    INNHOLD OM STRAFFERETTSLIG
                """.trimIndent(),
                        Nynorsk to """
                    INNHOLD OM STRAFFERETTSLIG
                """.trimIndent(),
                        English to """
                    INNHOLD OM STRAFFERETTSLIG
                """.trimIndent()
                    )
                }
                showIf(harForeldelse) {
                    paragraph {
                        text(
                            Bokmal to """
                    INNHOLD OM FORELDELSE
                """.trimIndent(),
                            Nynorsk to """
                    INNHOLD OM FORELDELSE
                """.trimIndent(),
                            English to """
                    INNHOLD OM FORELDELSE
                """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
