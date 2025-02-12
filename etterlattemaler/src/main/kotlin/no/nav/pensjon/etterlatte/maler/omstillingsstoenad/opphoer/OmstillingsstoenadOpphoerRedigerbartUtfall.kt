package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
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
    val feilutbetaling: FeilutbetalingType,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadOpphoerRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadOpphoerRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER_UTFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadOpphoerRedigerbartUtfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - opphør",
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
                includePhrase(Vedtak.BegrunnelseForVedtaket)
                paragraph {
                    text(
                        Bokmal to "(utfall jamfør tekstbibliotek)",
                        Nynorsk to "(utfall jamfør tekstbibliotek)",
                        English to "(utfall jamfør tekstbibliotek)",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om omstillingsstønad i " +
                            "folketrygdloven § <riktig paragrafhenvisning>.",
                        Nynorsk to "Vedtaket er fatta etter føresegnene om omstillingsstønad i folketrygdlova " +
                            "§ <riktig paragrafhenvisning>.",
                        English to "This decision has been made pursuant to the provisions regarding adjustment " +
                            "allowance in the National Insurance Act – sections <riktig paragrafhenvisning>.",
                    )
                }
                showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_4RG_UTEN_VARSEL)) {
                    includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUnder4RGUtenVarselOpphoer)
                }
            }
        }
}
