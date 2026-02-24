package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDataSelectors.tidligereFamiliepleier
import java.time.LocalDate

data class OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO(
    val data: OmstillingsstoenadInnvilgelseRedigerbartUtfallData
) : RedigerbartUtfallBrevDTO

data class OmstillingsstoenadInnvilgelseRedigerbartUtfallData(
    val virkningsdato: LocalDate,
    val utbetalingsbeloep: Kroner,
    val avdoed: Avdoed?,
    val harUtbetaling: Boolean,
    val beregning: OmstillingsstoenadBeregning,
    val etterbetaling: Boolean,
    val erSluttbehandling: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
    val datoVedtakOmgjoering: LocalDate? = null,
)

@TemplateModelHelpers
object OmstillingsstoenadInnvilgelseRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNVILGELSE_UTFALL

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - begrunnelse for innvilgelse",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {
            showIf(data.datoVedtakOmgjoering.isNull()) {
                includePhrase(
                    OmstillingsstoenadInnvilgelseFraser.Vedtak(
                        data.avdoed,
                        data.beregning,
                        data.harUtbetaling,
                        data.tidligereFamiliepleier,
                        data.erSluttbehandling
                    )
                )
            }.orShow {
                paragraph {
                    text(
                        bokmal { +
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">" },
                        nynorsk { +
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">" },
                        english { +
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">" },)
                }
            }

            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(
                OmstillingsstoenadInnvilgelseFraser.BegrunnelseForVedtaketRedigerbart(data.etterbetaling, data.tidligereFamiliepleier),
            )
        }
    }
}
