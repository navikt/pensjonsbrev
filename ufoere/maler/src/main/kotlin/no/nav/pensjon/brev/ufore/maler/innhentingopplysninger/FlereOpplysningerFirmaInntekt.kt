package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_FLERE_OPPL_FIRMAINNTEKT
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object FlereOpplysningerFirmaInntekt : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_FLERE_OPPL_FIRMAINNTEKT
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhentingsbrev ved tilfeller der inntekter ikke samsvarer med avklaring",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Du må sende oss flere opplysninger" },
                nynorsk { + "Du må sende oss fleire opplysningar" })
        }
        outline {
            paragraph {
                text(bokmal { +"Din søknad om uføretrygd er under behandling. Vi trenger flere opplysninger fra deg for å kunne behandle søknaden din. " },
                    nynorsk { +"Søknaden din om uføretrygd er under behandling. Vi treng fleire opplysningar frå deg for å kunne behandle søknaden din. " })
            }
            paragraph {
                text(bokmal { +"Du eier " + fritekst("firma AS") + " og du har " + fritekst("prosenttall") + " prosent eierandel av aksjene. " },
                    nynorsk { +"Du eig " + fritekst("firma AS") + " og du har " + fritekst("prosenttall") + " prosent eigarandel av aksjane. " })
            }
            paragraph {
                text(bokmal { +"Vi vil utelukke at du har inntektsmuligheter som du ikke benytter deg av. Vi trenger derfor opplysninger fra deg, for å se om inntektene du forventer stemmer med arbeidsmengde og inntektsmuligheter." },
                    nynorsk { +"Vi vil utelukke at du har inntektsmoglegheiter som du ikkje nyttar deg av. Vi treng derfor opplysningar frå deg, for å sjå om inntektene du forventar stemmer med arbeidsmengde og inntektsmoglegheiter." })
            }
            paragraph {
                text(bokmal { + fritekst("Tilpass dette selv: (Opplysningene vi har mottatt fra deg, viser at det er økt omsetning i firmaet etter at du ble syk. Du er innmeldt i xx prosent stilling og du har opplyst forventet inntekt på xxxxxx kroner per år. Det er ingen andre ansatte i firmaet enn deg selv og xx. Hen er ifølge ansettelsesregisteret ansatt som xxxx. Det er ikke benyttet innleid arbeidskraft av særlig omfang hverken i 20xx eller 20xx jf opplysningene dere har levert inn.)") },
                    nynorsk { + fritekst("Tilpass dette selv: (Opplysningane vi har motteke frå deg, viser at det er auka omsetning i firmaet etter at du vart sjuk. Du er innmeldt i xx prosent stilling og du har opplyst forventa inntekt på xxxxxx kroner per år. Det er ingen andre tilsette i firmaet enn deg sjølv og xx. Hen er ifølgje tilsetjingsregisteret tilsett som xxxx. Det er ikkje nytta innleigd arbeidskraft av særleg omfang verken i 20xx eller 20xx jf opplysningane de har levert inn.)") })
            }
            paragraph {
                text(bokmal { +"Vi stiller strenge krav til dokumentasjon av arbeidsforhold og inntekter der du er ansatt i eget, eller nær families, aksjeselskap. Bakgrunnen for dette er at du i større grad enn andre arbeidstakere har anledning til å påvirke egne lønns- og ansettelsesvilkår. Vi trenger flere opplysninger om selskapsdriften, din arbeidsevne, og ansettelsesforhold i firmaet. Vi ber derfor om at du svarer på samtlige spørsmål som er stilt under, og at du svarer så utfyllende som mulig på hvert spørsmål. Her er det bedre at du svarer «for mye» enn for lite. " },
                    nynorsk { +"Vi stiller strenge krav til dokumentasjon av arbeidsforhold og inntekter der du er tilsett i eige, eller nær families, aksjeselskap. Bakgrunnen for dette er at du i større grad enn andre arbeidstakarar har høve til å påverke eigne lønns- og tilsetjingsvilkår. Vi treng fleire opplysningar om selskapsdrifta, di arbeidsevne, og tilsetjingsforhold i firmaet. Vi ber derfor om at du svarar på samtlege spørsmål som er stilt under, og at du svarar så utfyllande som mogleg på kvart spørsmål. Her er det betre at du svarar «for mykje» enn for lite. " })
            }
            paragraph {
                text(bokmal { + fritekst("Saksbehandler:(Ta bort det du ikke trenger/tilpass til din sak, listen er heller ikke uttømmende)") },
                    nynorsk { + fritekst("Saksbehandlar:(Ta bort det du ikkje treng/tilpass til saka di, lista er heller ikkje uttømande)") })
            }
            paragraph {
                list {
                    item {text(bokmal { +"Arbeidsavtale for den aktuelle stillingen." },
                        nynorsk { +"Arbeidsavtale for den aktuelle stillinga." }) }
                    item {
                        text(
                            bokmal { +"Hva skyldes inntektsendringen som inntraff i " + fritekst("dato") + "? Hvis dette skyldes formell stillingsendring ber vi om at det vedlegges dokumentasjon på dette." },
                        nynorsk { +"Kva skuldast inntektsendringa som skjedde " + fritekst("dato") + "? Dersom dette skuldast ei formell stillingsendring, ber vi om at det blir lagt ved dokumentasjon på dette." }) }
                    item {
                        text(
                            bokmal { +"Dersom du ikke lenger skal arbeide fullt, er det da ansatt en annen person i din stilling? Vi ber om kopi av arbeidsavtalen på denne personen. (eventuelt hvem har overtatt dine arbeidsoppgaver)" },
                        nynorsk { +"Dersom du ikkje lenger skal arbeide fullt, er det då tilsett ein annan person i stillinga di? Vi ber om kopi av arbeidsavtalen på denne personen. (eventuelt kven har overteke arbeidsoppgåvene dine)" }) }
                    item {text(bokmal { +"Timelistene dine fra " + fritekst("år") + " og " + fritekst("år") + "." },
                        nynorsk { +"Timelistene dine frå " + fritekst("år") + " og " + fritekst("år") + "." }) }
                    item {
                        text(
                            bokmal { +"Hvilke arbeidsoppgaver har du utført for firmaet? Ifølge foreløpig driftsresultat er omsetningen økt betraktelig fra " + fritekst("år") + " til " + fritekst("år") + ". Firmaet har ikke benyttet innleid arbeidskraft i særlig grad ut ifra opplysningene i foreløpig resultatregnskap. Dette var også tilfellet i " + fritekst("år") + "." },
                        nynorsk { +"Kva arbeidsoppgåver har du utført for firmaet? Ifølgje førebels driftsresultat er omsetninga auka betydeleg frå " + fritekst("år") + " til " + fritekst("år") + ". Firmaet har ikkje nytta innleigd arbeidskraft i særleg grad ut ifrå opplysningane i førebels resultatrekneskap. Dette var også tilfellet i " + fritekst("år") + "." }) }
                    item {text(bokmal { +"Beskriv dine arbeidsoppgaver som styremedlem, samt uttak av styrehonorar." },
                        nynorsk { +"Skildre arbeidsoppgåvene dine som styremedlem, samt uttak av styrehonorar." }) }
                    item {text(bokmal { +"Hvor mye forventer du å tjene fremover? Her er det viktig at alle inntekter oppgis, både pensjonsgivende og kapital-inntekter." },
                        nynorsk { +"Kor mykje forventar du å tene framover? Her er det viktig at alle inntekter blir oppgjevne, både pensjonsgjevande og kapital-inntekter." }) }
                    item {text(bokmal { +"Skal du eller andre ta ut utbytte fra firmaet? Eventuelt hvor mye forventer du at det tas ut i " + fritekst("år") + "?" },
                        nynorsk { +"Skal du eller andre ta ut utbyte frå firmaet? Eventuelt kor mykje forventar du at det vert teke ut i " + fritekst("år") + "?" }) }
                }
            }
            paragraph {
                text(bokmal { +"Fristen for å sende inn nødvendige opplysninger settes til " + fritekst("dato") + "." },
                    nynorsk { +"Fristen for å sende inn nødvendige opplysningar vert sett til " + fritekst("dato") + "." })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
