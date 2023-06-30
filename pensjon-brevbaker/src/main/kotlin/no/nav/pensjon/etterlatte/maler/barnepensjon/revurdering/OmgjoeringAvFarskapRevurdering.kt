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
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.forrigeFar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.forrigeVirkningsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.naavaerendeFar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.vedtaksdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.OmgjoeringAvFarskap
import java.time.LocalDate

data class BarnepensjonRevurderingOmgjoeringAvFarskapDTO(
    val vedtaksdato: LocalDate,
    val virkningsdato: LocalDate,
    val naavaerendeFar: Navn,
    var forrigeFar: Navn,
    val forrigeVirkningsdato: LocalDate,
)

@TemplateModelHelpers
object OmgjoeringAvFarskapRevurdering : EtterlatteTemplate<BarnepensjonRevurderingOmgjoeringAvFarskapDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_OMGJOERING_AV_FARSKAP

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingOmgjoeringAvFarskapDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør på grunn av omgjøring av farskap",
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
                OmgjoeringAvFarskap.BegrunnelseForVedtaket(
                    vedtaksdato = vedtaksdato,
                    virkningsdato = virkningsdato,
                    naaevaerendeFar = naavaerendeFar,
                    forrigeFar = forrigeFar,
                    forrigeVirkningsdato = forrigeVirkningsdato,
                ),
            )

            includePhrase(Barnepensjon.BarnepensjonenDinErDerforOpphoert(virkningsdato))
            includePhrase(Lover.Folketrygdloven18ogforvaltningsloven35_1_c)
            includePhrase(Barnepensjon.Feilutbetaling)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
    }
}
