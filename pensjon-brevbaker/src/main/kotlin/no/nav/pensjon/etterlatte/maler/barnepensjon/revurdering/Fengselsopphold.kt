package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonFengselsoppholdDTOSelectors.fraDato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonFengselsoppholdDTOSelectors.tilDato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonFengselsoppholdDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Fengselsoppholdfraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

data class BarnepensjonFengselsoppholdDTO(
    val virkningsdato: LocalDate,
    val fraDato: LocalDate,
    val tilDato: LocalDate,
)

@TemplateModelHelpers
object Fengselsopphold : EtterlatteTemplate<BarnepensjonFengselsoppholdDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_FENGSELSOPPHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonFengselsoppholdDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør på grunn av omgjøring av farskap",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har stanset barnepensjonen din",
                Language.Nynorsk to "Vi har stansa barnepensjonen din",
                Language.English to "We have ceased your child pension",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(
                Fengselsoppholdfraser.Opphold(
                    virkningsdato = virkningsdato,
                    fraDato = fraDato,
                    tilDato = tilDato,
                ),
            )
        }
    }
}
