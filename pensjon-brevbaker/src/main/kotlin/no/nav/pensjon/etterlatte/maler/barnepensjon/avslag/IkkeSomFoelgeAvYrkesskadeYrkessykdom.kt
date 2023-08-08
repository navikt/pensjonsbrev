package no.nav.pensjon.etterlatte.maler.barnepensjon.avslag

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
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagIkkeYrkesskadeDTOSelectors.dinForelder
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagIkkeYrkesskadeDTOSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagIkkeYrkesskadeDTOSelectors.yrkesskadeEllerYrkessykdom
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.avslag.IkkeSomFoelgeAvYrkesskadeYrkessykdomFraser
import java.time.LocalDate

data class BarnepensjonAvslagIkkeYrkesskadeDTO(
    val dinForelder: String,
    val doedsdato: LocalDate,
    val yrkesskadeEllerYrkessykdom: String,
)

@TemplateModelHelpers
object IkkeSomFoelgeAvYrkesskadeYrkessykdom : EtterlatteTemplate<BarnepensjonAvslagIkkeYrkesskadeDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_AVSLAG_IKKEYRKESSKADE

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonAvslagIkkeYrkesskadeDTO::class,
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
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            includePhrase(
                IkkeSomFoelgeAvYrkesskadeYrkessykdomFraser.BegrunnelseForVedtaket(
                    dinForelder,
                    doedsdato,
                    yrkesskadeEllerYrkessykdom,
                ),
            )
        }
    }
}
