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
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.avslag.AvslagFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object BarnepensjonAvslagRedigerbartUtfall : EtterlatteTemplate<ManueltBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_AVSLAG_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse",
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
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(AvslagFraser.FyllInn)
        }
    }
}