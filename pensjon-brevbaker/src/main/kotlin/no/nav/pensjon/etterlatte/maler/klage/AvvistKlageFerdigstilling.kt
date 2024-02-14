package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.SakType


data class AvvistKlageFerdigDTO(
    override val innhold: List<Element>,
    val data: AvvistKlageInnholdDTO
) : BrevDTO

@TemplateModelHelpers
object AvvistKlageFerdigstilling : EtterlatteTemplate<AvvistKlageFerdigDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.AVVIST_KLAGE_FERDIG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvvistKlageFerdigDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Avvist klage",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vi har avvist klagen din",
                Language.Nynorsk to "Vi har avvist klagen din",
                Language.English to "Vi har avvist klagen din"
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            showIf(data.sakType.equalTo(SakType.BARNEPENSJON)) {
                includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
                includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal)
            } orShow {
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
    }
}