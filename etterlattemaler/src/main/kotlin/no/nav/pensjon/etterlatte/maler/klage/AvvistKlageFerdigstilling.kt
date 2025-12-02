package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.klage.AvvistKlageInnholdDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke


data class AvvistKlageFerdigDTO(
    override val innhold: List<Element>,
    val data: AvvistKlageInnholdDTO,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object AvvistKlageFerdigstilling : EtterlatteTemplate<AvvistKlageFerdigDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.AVVIST_KLAGE_FERDIG

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Avvist klage",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vi har avvist klagen din" },
                nynorsk { +"Vi har avvist klaga di" },
                english { +"We have rejected your appeal" }
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Felles.DuHarRettTilAaKlage)

            showIf(data.sakType.equalTo(SakType.BARNEPENSJON)) {
                includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(true.expr(), data.bosattUtland))
            } orShow {
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = true),  data.bosattUtland)

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = false), data.bosattUtland.not())
    }
}