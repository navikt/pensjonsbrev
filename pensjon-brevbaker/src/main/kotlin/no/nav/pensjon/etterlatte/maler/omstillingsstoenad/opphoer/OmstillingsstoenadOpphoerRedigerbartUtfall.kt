package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
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
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTOSelectors.virkningsdato
import java.time.LocalDate


data class OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
    val virkningsdato: LocalDate,
    val feilutbetaling: FeilutbetalingType
)

@TemplateModelHelpers
object OmstillingsstoenadOpphoerRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadOpphoerRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER_UTFALL

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
                English to "We have terminated your adjustment allowance",
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
                    English to "Your adjustment allowance will terminate on: ".expr() + virkningsdato.format() + ".",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i " +
                            "folketrygdloven § <riktig paragrafhenvisning> og § 22-12.",
                    Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§ <riktig paragrafhenvisning> og § 22-12. ",
                    English to "This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – sections  <riktig paragrafhenvisning> " +
                            "og § 22-12. ",
                )
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
            }
        }
    }
}