package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG_UTLAND
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadBarnetilleggUtland : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_SOKNAD_BARNETILLEGG_UTLAND
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Søkt barnetillegg Barn som ikke bor i Norge",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Din søknad om barnetillegg i uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Du har søkt om barnetillegg for barn som ikke bor i Norge. For å kunne behandle søknaden trenger vi flere opplysninger fra deg. " })
            }
            paragraph {
                text(bokmal { +"Vi trenger" })
                list {
                    item { text(bokmal { +"bekreftet kopi av fødselsattest for barnet/barna" }) }
                    item { text(bokmal { +"kopi av identifikasjonspapir med bilde av barnet/barna" }) }
                    item { text(bokmal { +"dokumentasjon som viser at du er med på å forsørge barnet/barna" }) }
                    item { text(bokmal { +"dokumentasjon som viser hvor barnet bor i dag" }) }
                }
            }
            paragraph {
                text(bokmal { +"Hvis fødselsattesten ikke er på engelsk eller et av de nordiske språkene, må vi ha en oversatt verifisert versjon i tillegg. " })
            }
            paragraph {
                text(bokmal { +"Hvis du overfører jevnlig penger for å forsørge barnet, må vi få kopi av bankutskrift og dokumentasjon på hvem du sender disse pengene til. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL} " })
            }
            paragraph {
                text(bokmal { +"Vi ber om at opplysningene sendes oss innen " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { +"Hvis vi ikke får nødvendige opplysninger innen fristen, kan søknaden bli avslått på grunn av manglende opplysninger. Gi oss beskjed dersom det tar lenger tid å innhente opplysningene." })
            }
            paragraph {
                text(bokmal { +"I folketrygdloven § 21-3 finner du informasjon om opplysningsplikten din til Nav. " })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
