package no.nav.pensjon.etterlatte.maler.barnepensjon.avslag

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTOSelectors.avdoedNavn
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfallDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonAvslagFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonAvslagRedigerbartUtfallDTO(
    val avdoedNavn: String = "<Klarte ikke å finne navn automatisk, du må sette inn her>",
    val erSluttbehandling: Boolean = false,
): RedigerbartUtfallBrevDTO


@TemplateModelHelpers
object BarnepensjonAvslagRedigerbartUtfall : EtterlatteTemplate<BarnepensjonAvslagRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_AVSLAG_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonAvslagRedigerbartUtfallDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - begrunnelse for avslag",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har avslått søknaden din om barnepensjon",
                Language.Nynorsk to "Vi har avslått søknaden din om barnepensjon",
                Language.English to "We have rejected your application for a children's pension",
            )
        }

        outline {
            includePhrase(BarnepensjonAvslagFraser.Vedtak(erSluttbehandling, avdoedNavn))
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(BarnepensjonFellesFraser.FyllInn)
        }
    }
}