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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_FLERE_OPPLYSNINGER
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object FlereOpplysninger : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_FLERE_OPPLYSNINGER
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Du må sende oss flere opplysninger",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Du må sende oss flere opplysninger" })
        }
        outline {
            paragraph {
                text(bokmal { +"Din søknad om uføretrygd er under behandling. Vi trenger flere opplysninger fra deg for å kunne behandle søknaden din. " })
            }
            paragraph {
                text(bokmal { +"Du eier xx AS og du har " + fritekst("firmanavn") + " prosent eierandel av aksjene. " })
            }
            paragraph {
                text(bokmal { +"Vi vil utelukke at du har inntektsmuligheter som du ikke benytter deg av. Vi trenger derfor opplysninger fra deg, for å se om inntektene du forventer stemmer med arbeidsmengde og inntektsmuligheter." })
            }
            paragraph {
                text(bokmal { + fritekst("Tilpass dette selv: (Opplysningene vi har mottatt fra deg, viser at det er økt omsetning i firmaet etter at du ble syk. Du er innmeldt i xx prosent stilling og du har opplyst forventet inntekt på xxxxxx kroner per år. Det er ingen andre ansatte i firmaet enn deg selv og xx. Hen er ifølge ansettelsesregisteret ansatt som xxxx. Det er ikke benyttet innleid arbeidskraft av særlig omfang hverken i 20xx eller 20xx jf opplysningene dere har levert inn.)") })
            }
            paragraph {
                text(bokmal { +"Vi stiller strenge krav til dokumentasjon av arbeidsforhold og inntekter der du er ansatt i eget, eller nær families, aksjeselskap. Bakgrunnen for dette er at du i større grad enn andre arbeidstakere har anledning til å påvirke egne lønns- og ansettelsesvilkår. Vi trenger flere opplysninger om selskapsdriften, din arbeidsevne, og ansettelsesforhold i firmaet. Vi ber derfor om at du svarer på samtlige spørsmål som er stilt under, og at du svarer så utfyllende som mulig på hvert spørsmål. Her er det bedre at du svarer «for mye» enn for lite. " })
            }
            paragraph {
                text(bokmal { + fritekst("Saksbehandler:(Ta bort det du ikke trenger/tilpass til din sak, listen er heller ikke uttømmende)") })
            }
            paragraph {
                list {
                    item {text(bokmal { +"Arbeidsavtale for den aktuelle stillingen." })                    }
                    item {text(bokmal { +"Hva skyldes inntektsendringen som inntraff i " + fritekst("xxxx xxxx") + ".? Hvis dette skyldes formell stillingsendring ber vi om at det vedlegges dokumentasjon på dette." })                    }
                    item {text(bokmal { +"Dersom du ikke lenger skal arbeide fullt, er det da ansatt en annen person i din stilling? Vi ber om kopi av arbeidsavtalen på denne personen. (eventuell hvem har overtatt dine arbeidsoppgaver)" })                    }
                    item {text(bokmal { +"Timelistene dine fra 20" + fritekst("xx") + " og 20" + fritekst("xx") + "." })                    }
                    item {text(bokmal { +"Hvilke arbeidsoppgaver du har utført for firmaet? Ifølge foreløpig driftsresultat er omsetningen økt betraktelig fra 20" + fritekst("xx") + " til 20" + fritekst("xx") + ". Firmaet har ikke benyttet innleid arbeidskraft i særlig grad ut ifra opplysningene i foreløpig resultatregnskap. Dette var også tilfellet i 20" + fritekst("xx") + "." })                    }
                    item {text(bokmal { +"Beskriv dine arbeidsoppgaver som styremedlem, samt uttak av styrehonorar." })                    }
                    item {text(bokmal { +"Hvor mye forventer du å tjene fremover? Her er det viktig at alle inntekter oppgis, både pensjonsgivende og kapital-inntekter." })                    }
                    item {text(bokmal { +"Skal du eller andre ta ut utbytte fra firmaet? Eventuelt hvor mye forventer du at det tas ut i 20" + fritekst("xx") + "?" })                    }
                }
            }
            paragraph {
                text(bokmal { +"Fristen for å sende inn nødvendige opplysninger settes til " + fritekst("dato") + "." })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
