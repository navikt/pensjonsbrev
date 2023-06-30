package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.adoptertAv
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Adopsjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import java.time.LocalDate

data class BarnepensjonRevurderingAdopsjonDTO(
    val virkningsdato: LocalDate,
    val adoptertAv: Navn,
)

@TemplateModelHelpers
object AdopsjonRevurdering : EtterlatteTemplate<BarnepensjonRevurderingAdopsjonDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_ADOPSJON

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingAdopsjonDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør på grunn av adopsjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi stanser barnepensjonen din",
            )
        }
        outline {
            includePhrase(
                Adopsjon.BegrunnelseForVedtaket(
                    virkningsdato = virkningsdato,
                    navn = adoptertAv,
                ),
            )

            includePhrase(Lover.Folketrygdloven18_7og22_12)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
    }
}
