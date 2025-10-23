package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.PesysDataSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaPensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.PesysDataSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.PesysDataSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDtoSelectors.pesysData
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

    override val kode = Pesysbrevkoder.Redigerbar.PE_BEKREFTELSE_PAA_UFOERETRYGD
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
                    bokmal { +"Vi bekrefter herved at " + pesysData.navn + ", født " + pesysData.foedselsdato.format() },
                    nynorsk { +"Vi stadfestar herved at " + pesysData.navn + ", fødd " + pesysData.foedselsdato.format() },
                    english { +"We hereby confirm that " + pesysData.navn + ", born on " + pesysData.foedselsdato.format() }
                )
                text(
                    bokmal { +", mottar uføretrygd fra folketrygden med en uføregrad på " + fritekst("uføregrad") + " prosent." },
                    nynorsk { +", få ei månadleg pensjon på " + fritekst("beløp") + " kroner." },
                    english { +", has a " + fritekst("uføregrad") + "% work incapacity and is receiving disability benefit from the National Insurance Scheme. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Størrelsen på uføretrygden " + fritekst("beløp") + " kroner per måned." },
                    nynorsk { +"Storleiken på uføretrygda er " + fritekst("beløp") + " kroner per månad." },
                    english { + "The gross monthly amount of the benefit is NOK " + fritekst("beløp") + "."},
                )
            }
        }
    }
}


