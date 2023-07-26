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
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.adopsjonsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.adoptertAv1
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.adoptertAv2
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingAdopsjonDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Adopsjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import java.time.LocalDate

data class BarnepensjonRevurderingAdopsjonDTO(
    val virkningsdato: LocalDate,
    val adopsjonsdato: LocalDate,
    val adoptertAv1: Navn,
    val adoptertAv2: Navn? = null,
)

@TemplateModelHelpers
object AdopsjonRevurdering : EtterlatteTemplate<BarnepensjonRevurderingAdopsjonDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_ADOPSJON

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingAdopsjonDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør på grunn av adopsjon",
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
                Adopsjon.BegrunnelseForVedtaket(
                    virkningsdato = virkningsdato,
                    adopsjonsdato = adopsjonsdato,
                    adoptertAv1 = adoptertAv1,
                    adoptertAv2 = adoptertAv2,
                ),
            )

            includePhrase(Lover.Folketrygdloven18_7og22_12)
        }
    }
}
