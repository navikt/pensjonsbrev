package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.Innvilgelse
import java.time.LocalDate

data class FoerstegangsvedtakUtfallDTO(
    val virkningsdato: LocalDate,
    val avdoed: Avdoed,
    val utbetalingsbeloep: Kroner
)

@TemplateModelHelpers
object FoerstegangsvedtakUtfall : EtterlatteTemplate<FoerstegangsvedtakUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_FOERSTEGANGSVEDTAK_INNVILGELSE_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = FoerstegangsvedtakUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
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
            includePhrase(
                Innvilgelse.BegrunnelseForVedtaketRedigerbart,
            )

        }
    }
}
