package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_BRUKER_LEGEERKLAERING
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori

@TemplateModelHelpers
object BrukerLegeerklaering : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_BRUKER_LEGEERKLAERING
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Brev til brukeren ved innhenting av medisinske opplysninger",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Innhenting av legeerklæring" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi trenger din hjelp til å få helseopplysninger om deg fra din fastlege. " })
            }
            paragraph {
                text(bokmal { +"Du har søkt om uføretrygd " + fritekst("(eller ung ufør/yrkesskadefordel)") + ". For at vi skal kunne behandle søknaden din " + fritekst("(eller vurdere din rett til ung ufør/yrkesskadefordel)") + ", trenger vi oppdaterte " + fritekst("(stryk ev. «oppdaterte»)") + " medisinske opplysninger. " })
            }
            paragraph {
                text(bokmal { +"Vi har flere ganger bedt fastlegen din om å sende oss " + fritekst("legeerklæring/medisinske opplysninger") + ", uten å ha fått dette. Vi ber deg derfor om å kontakte din fastlege og minne om at " + fritekst("legeerklæring/medisinske opplysninger") + " må sendes til Nav så snart som mulig. " })
            }
            paragraph {
                text(bokmal { +"Dersom vi ikke får " + fritekst("legeerklæringen/medisinske opplysninger") + " innen to uker, vil vi behandle saken ut fra opplysningene vi har. " })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
