package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDtoSelectors.PesysDataSelectors.address
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDtoSelectors.PesysDataSelectors.foedselsnummer
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaMottakAvPensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Erstatte PE_IY_03_bekreftelse_på_pensjon_uføretrygd_169-174


@TemplateModelHelpers
object BekreftelsePaaMottakAvPensjon : RedigerbarTemplate<BekreftelsePaaMottakAvPensjonDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_BEKREFTELSE_PAA_MOTTAK_AV_PENSJON
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om revurdering av pensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Bekreftelse på pensjon" },
                nynorsk { +"Bekreftelse på pensjon" },
                english { +"Confirmation of pension" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi bekrefter at " + pesysData.navn + " født " + pesysData.foedselsnummer.format() + ", bosatt på: " },
                    nynorsk { +"Vi stadfestar at " + pesysData.navn + " fødd " + pesysData.foedselsnummer.format() + ", busett på:"},
                    english { +"We hereby confirm that " + pesysData.navn + " born " + pesysData.foedselsnummer.format() + ", resident at:" },
                )
            }
            paragraph {
                text(
                    bokmal { +pesysData.address },
                    nynorsk { +pesysData.address },
                    english { +pesysData.address }
                )
            }
            paragraph {
                text(
                    bokmal { +"mottar en månedlig pensjon på " + fritekst("beløp") + " Kr." },
                    nynorsk { +"få ei månadleg pensjon på " + fritekst("beløp") + " Kr." },
                    english { +"receives a monthly pension of NOK " + fritekst("beløp") + "." }
                )
            }
        }
    }
}


