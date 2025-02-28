package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.tidligereFamiliepleier
import java.time.LocalDate

data class OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO(
    val virkningsdato: LocalDate,
    val utbetalingsbeloep: Kroner,
    val avdoed: Avdoed?,
    val harUtbetaling: Boolean,
    val beregning: OmstillingsstoenadBeregning,
    val etterbetaling: Boolean,
    val erSluttbehandling: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
    val datoVedtakOmgjoering: LocalDate? = null,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInnvilgelseRedigerbartUtfall : EtterlatteTemplate<OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNVILGELSE_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Vedtak - begrunnelse for innvilgelse",
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
            showIf(datoVedtakOmgjoering.isNull()) {
                includePhrase(
                    OmstillingsstoenadInnvilgelseFraser.Vedtak(
                        avdoed,
                        beregning,
                        harUtbetaling,
                        tidligereFamiliepleier,
                        erSluttbehandling
                    )
                )
            }.orShow {
                paragraph {
                    text(
                        Bokmal to
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">",
                        Nynorsk to
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">",
                        English to
                            "<Fritekst / tekst fra tekstbiblioteket \"omgjøring etter klage\">",)
                }
            }

            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(
                OmstillingsstoenadInnvilgelseFraser.BegrunnelseForVedtaketRedigerbart(etterbetaling, tidligereFamiliepleier),
            )
        }
    }
}
