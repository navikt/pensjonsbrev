package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTOSelectors.virkningsdato
import java.time.LocalDate


data class OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
    val virkningsdato: LocalDate,
)

@TemplateModelHelpers
object OmstillingsstoenadOpphoerRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadOpphoerRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_REVURDERING_OPPHOER_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadOpphoerRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Opphør av omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi har opphørt omstillingsstønaden din",
                Nynorsk to "Vi har avvikla omstillingsstønaden din",
                English to "We have terminated your transitional benefits",
            )
        }

        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            paragraph {
                text(
                    Bokmal to "(utfall jamfør tekstbibliotek)",
                    Nynorsk to "(utfall jamfør tekstbibliotek)",
                    English to "(utfall jamfør tekstbibliotek)",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Omstillingsstønaden din opphører fra ".expr() + virkningsdato.format() + ".",
                    Nynorsk to "Omstillingsstønaden din fell bort frå og med ".expr() + virkningsdato.format() + ".",
                    English to "Your transitional benefits will terminate on: ".expr() + virkningsdato.format() + ".",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i " +
                            "folketrygdloven § <riktig paragrafhenvisning> og § 22-12.",
                    Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§ <riktig paragrafhenvisning> og § 22-12. ",
                    English to "This decision has been made pursuant to the provisions regarding transitional " +
                            "benefits in the National Insurance Act – sections  <riktig paragrafhenvisning> " +
                            "og § 22-12. ",
                )
            }
        }
    }
}