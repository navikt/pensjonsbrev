package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.PesysDataSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Erstatte PE_IY_03_bekreftelse_på_pensjon_uføretrygd_169-174


@TemplateModelHelpers
object BekreftelsePaaUfoeretrygd : RedigerbarTemplate<BekreftelsePaaUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.bekreftelsePaaUfoeretrygd.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_BEKREFTELSE_PAA_UFOERETRYGD
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på at bruker mottar uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Bekreftelse på uføretrygd" },
                nynorsk { +"Bekreftelse på uføretrygd" },
                english { +"Confirmation of disability benefit" }
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
                    bokmal { +", får uføretrygd fra folketrygden med en uføregrad på " + fritekst("uføregrad") + " prosent." },
                    nynorsk { +", får uføretrygd frå folketrygda med ein uføregrad på " + fritekst("uføregrad") + " prosent." },
                    english { +", has a " + fritekst("uføregrad") + " percent work incapacity and is receiving disability benefit from the National Insurance Scheme." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden er " + fritekst("beløp") + " kroner per måned før skatt." },
                    nynorsk { +"Uføretrygda er " + fritekst("beløp") + " kroner per månad før skatt." },
                    english { + "The benefit amounts to NOK " + fritekst("beløp") + " per month before tax."},
                )
            }
        }
    }
}


