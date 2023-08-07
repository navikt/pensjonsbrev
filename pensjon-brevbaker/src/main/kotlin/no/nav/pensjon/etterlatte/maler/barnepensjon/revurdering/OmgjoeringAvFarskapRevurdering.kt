package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.forrigeFar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.naavaerendeFar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.opprinneligInnvilgelsesdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingOmgjoeringAvFarskapDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.OmgjoeringAvFarskap
import java.time.LocalDate

data class BarnepensjonRevurderingOmgjoeringAvFarskapDTO(
    val virkningsdato: LocalDate,
    val naavaerendeFar: Navn,
    var forrigeFar: Navn,
    val opprinneligInnvilgelsesdato: LocalDate,
)

@TemplateModelHelpers
object OmgjoeringAvFarskapRevurdering : EtterlatteTemplate<BarnepensjonRevurderingOmgjoeringAvFarskapDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_OMGJOERING_AV_FARSKAP

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingOmgjoeringAvFarskapDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør på grunn av omgjøring av farskap",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi opphører barnepensjonen din",
                Nynorsk to "Vi stansar barnepensjonen din",
                English to "We cease your child pension",
            )
        }
        outline {
            includePhrase(
                OmgjoeringAvFarskap.BegrunnelseForVedtaket(
                    naaevaerendeFar = naavaerendeFar,
                    forrigeFar = forrigeFar,
                    opprinneligInnvilgelsesdato = opprinneligInnvilgelsesdato,
                ),
            )

            includePhrase(Barnepensjon.BarnepensjonenDinErDerforOpphoert(virkningsdato))
            includePhrase(Lover.Folketrygdloven18ogforvaltningsloven35_1_c)
            includePhrase(Barnepensjon.Feilutbetaling)
        }
    }
}
