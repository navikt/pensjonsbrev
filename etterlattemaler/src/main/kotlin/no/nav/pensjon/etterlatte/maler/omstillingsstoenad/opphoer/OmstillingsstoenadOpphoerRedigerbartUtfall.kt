package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerRedigerbartUtfallDTOSelectors.feilutbetaling


data class OmstillingsstoenadOpphoerRedigerbartUtfallDTO(
    val feilutbetaling: FeilutbetalingType
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadOpphoerRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadOpphoerRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER_UTFALL

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }

        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            paragraph {
                text(
                    bokmal { +"(utfall jamfør tekstbibliotek)" },
                    nynorsk { +"(utfall jamfør tekstbibliotek)" },
                    english { +"(utfall jamfør tekstbibliotek)" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter bestemmelsene om omstillingsstønad i " +
                            "folketrygdloven § <riktig paragrafhenvisning>." },
                    nynorsk { +"Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§ <riktig paragrafhenvisning>." },
                    english { +"This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – sections <riktig paragrafhenvisning>." },
                )
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_4RG_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUnder4RGUtenVarselOpphoer)
            }
        }
    }
}