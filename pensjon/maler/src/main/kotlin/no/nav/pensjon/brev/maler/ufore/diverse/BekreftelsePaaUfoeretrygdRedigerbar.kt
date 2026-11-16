package no.nav.pensjon.brev.maler.ufore.diverse

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.BekreftelsePaaUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.bekreftelsePaaUfoeretrygdDto.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.bekreftelsePaaUfoeretrygdDto.pesysData.foedselsdato
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.bekreftelsePaaUfoeretrygdDto.pesysData.navn
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object BekreftelsePaaUfoeretrygdRedigerbar : RedigerbarTemplate<BekreftelsePaaUfoeretrygdDto> {

    override val kode = Pesysbrevkoder.Redigerbar.UT_BEKREFTELSE_PAA_UFOERETRYGD
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på at bruker mottar uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Bekreftelse på uføretrygd" },
                nynorsk { +"Bekreftelse på uføretrygd" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi bekrefter at " + pesysData.navn + ", født " + pesysData.foedselsdato.format() },
                    nynorsk { +"Vi stadfestar at " + pesysData.navn + ", fødd " + pesysData.foedselsdato.format() },
                )
                text(
                    bokmal { +", får uføretrygd fra folketrygden med en uføregrad på " + fritekst("uføregrad") + " prosent." },
                    nynorsk { +", får uføretrygd frå folketrygda med ein uføregrad på " + fritekst("uføregrad") + " prosent." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden er " + fritekst("beløp") + " kroner per måned før skatt." },
                    nynorsk { +"Uføretrygda er " + fritekst("beløp") + " kroner per månad før skatt." },
                )
            }
        }
    }
}


