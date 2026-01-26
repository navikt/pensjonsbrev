package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_NY_SIVILSTAND
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object NySivilstand : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_NY_SIVILSTAND
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Ny sivilstand",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Ny sivilstand" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har fått opplysninger om at du har blitt " + fritekst("enslig/skilt/separert") + ". Som enslig kan du ha krav på økt uføretrygd. Den aktuelle lovhjemmelen for denne type endringer fremgår av folketrygdloven § 12-13. " })
            }
            paragraph {
                text(bokmal { +"Våre opplysninger fra folkeregisteret viser at du og " + fritekst("fraseparert ektefelle /samboer") + " fortsatt bor på samme adresse. Det er den faktiske bosituasjonen din som avgjør om du har krav på økt uføretrygd. Vi ber deg melde fra skriftlig til Nav når du, eller din partner, har endret adresse hos folkeregisteret. " })
            }
            paragraph {
                text(bokmal { +"Vi minner om at vi ikke kan gjøre noe i din sak, før vi har fått opplysninger om endring av den faktiske bosituasjonen din. " })
            }
            paragraph {
                text(bokmal { +"Får du barnetillegg, og du og barnet ikke har felles folkeregistrert adresse, må du sende oss dokumentasjon på faktisk forsørgelse. Dokumentasjon på forsørgelse kan være avtale om bidrag og/eller samvær som må være undertegnet av begge foreldrene. " })
            }
            paragraph {
                text(bokmal { +"Selv om uføretrygden din ikke blir endret som følge av sivilstandsendringen, er du fortsatt pliktig til å melde ifra til Nav om endringer, slik det fremgår av folketrygdloven § 21-3. " })
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
