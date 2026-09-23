package no.nav.pensjon.brev.maler.klageOgAnke

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageOversendelsesbrevTilKlageinstansUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_OVERSENDELSESBREV_TIL_KLAGEINSTANS
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - oversendelsesbrev til Nav klageinstans",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Oversendelsesbrev til Nav klageinstans - uføretrygd" },
                nynorsk { +"Oversendelsesbrev til Nav klageinstans - uføretrygd" },
                english { +"Referal letter to the Nav Appeals Management Unit - Disability Benefit" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"" },
                    nynorsk { +"" },
                    english { +"" }
                )

            }
        }
    }
}