package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.UnaryOperation.SizeOf.format
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingAdopsjonDTO
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingAdopsjonDTOSelectors.adoptertAv
import no.nav.pensjon.etterlatte.maler.BarnepensjonRevurderingAdopsjonDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Adopsjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon

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
            val navn = adoptertAv.format()
            includePhrase(
                Adopsjon.BegrunnelseForVedtaket(
                    virkningsdato = virkningsdato,
                    navn = navn,
                ),
            )

            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
    }
}
