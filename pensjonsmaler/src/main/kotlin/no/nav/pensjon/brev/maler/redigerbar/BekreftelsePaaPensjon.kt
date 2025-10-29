package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.PesysDataSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.pensjonLatexSettings
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Erstatte PE_IY_03_bekreftelse_på_pensjon_uføretrygd_169-174


@TemplateModelHelpers
object BekreftelsePaaPensjon : RedigerbarTemplate<BekreftelsePaaPensjonDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_BEKREFTELSE_PAA_PENSJON
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på at bruker mottar pensjon",
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
                    bokmal { +"Vi bekrefter at " + pesysData.navn + ", født " + pesysData.foedselsdato.format() },
                    nynorsk { +"Vi stadfestar at " + pesysData.navn + ", fødd " + pesysData.foedselsdato.format() },
                    english { +"We confirm that " + pesysData.navn + ", born on " + pesysData.foedselsdato.format() }
                )
                text(
                    bokmal { +", får en månedlig pensjon før skatt på " + fritekst("beløp") + " kroner." },
                    nynorsk { +", får ei månadleg pensjon før skatt på " + fritekst("beløp") + " kroner." },
                    english { +", receives a monthly pension before tax of NOK " + fritekst("beløp") + "." }
                )

            }
        }
    }
}


