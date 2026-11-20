package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger.bruker

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object UforetrygdNavBerOmDokumentasjon : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Ufoerebrevkoder.Redigerbar.UT_UFORETRYGD_NAV_BER_OM_DOKUMENTASJON
    override val featureToggle = FeatureToggles.uforetrygdNavBerOmDokumentasjon.toggle
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Uføretrygd - Nav ber om dokumentasjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        val trygdetid = saksbehandlervalg("trygdetid", "Forutgående trygdetid - arbeidsdokumentasjon").bool()
        val arbeidsrettedeTiltak = saksbehandlervalg("arbeidsrettedeTiltak", "Arbeidsrettede tiltak").bool()
        val nedsattInntektsevne = saksbehandlervalg("nedsattInntektsevne", "Dokumentasjon på arbeid og inntekt").bool()
        val uforetidspunkt = saksbehandlervalg("uforetidspunkt", "Informasjon om ytelser").bool()
        val uforetrygdensStorrelse = saksbehandlervalg("uforetrygdensStorrelse", "Sivilstatus").bool()
        val bankopplysninger = saksbehandlervalg("bankopplysninger", "Innhenting av bankopplysninger").bool()
        val purring = saksbehandlervalg("purring", "Viktig informasjon (brukes ved purring)").bool()

        title {
            text(bokmal { +"Uføretrygd - Nav ber om dokumentasjon" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi viser til din søknad om uføretrygd, som vi mottok den " + fritekst("dato") + ". For å kunne behandle søknaden, trenger vi flere opplysninger fra deg." })
            }
            showIf(trygdetid) {
                title1 {
                    text(bokmal { +"Folketrygdloven §12-2 - Forutgående trygdetid" })
                }
                paragraph {
                    text(bokmal { +"For å ha rett til uføretrygd må du som hovedregel ha vært medlem i folketrygden de siste fem årene før du ble ufør, eller ha vært medlem i trygdeordning i et annet EØS-land." })
                }
                paragraph {
                    text(bokmal { +"Vi trenger derfor:" })
                    list {
                        item { text(bokmal { +"Dokumentasjon på arbeidsforhold i Norge i perioden " + fritekst("XX–XX") + ", med eksakt start- og sluttdato (f.eks. arbeidsattest, sluttattest, lønns- og trekkoppgave)." }) }
                        item { text(bokmal { +"Dato for når du flyttet fra Norge (dag, måned og år)" }) }
                    }
                }
            }
            showIf(arbeidsrettedeTiltak) {
                title1 {
                    text(bokmal { +"Folketrygdloven §12-5 - Arbeidsrettede tiltak" })
                }
                paragraph {
                    text(bokmal { +"Du må ha forsøkt hensiktsmessige tiltak for å bedre inntektsevnen din." })
                }
                paragraph {
                    text(bokmal { +"Vi ber om dokumentasjon dersom du har:" })
                    list {
                        item { text(bokmal { +"Deltatt i arbeidsrettede tiltak" }) }
                        item { text(bokmal { +"Tilpasset arbeidet ditt" }) }
                        item { text(bokmal { +"Startet i en lettere jobb på grunn av helsen" }) }
                    }
                }
            }
            showIf(nedsattInntektsevne) {
                title1 {
                    text(bokmal { +"Folketrygdloven §12-7 - Nedsatt inntektsevne" })
                }
                paragraph {
                    text(bokmal { +"For å få uføretrygd må inntektsevnen din være varig redusert med minst 50 % på grunn av sykdom eller skade." })
                }
                paragraph {
                    text(bokmal { +"Vi trenger:" })
                    list {
                        item { text(bokmal { +"Dokumentasjon på din årlige inntekt i perioden " + fritekst("XX – XX") + " fra din arbeidsgiver eller skattemyndigheter." }) }
                        item { text(bokmal { +"Informasjon om nåværende arbeid: Hvor mange timer jobber du per uke? Hva er månedslønnen før skatt? Hva slags jobb har du?" }) }
                    }
                }
            }
            showIf(uforetidspunkt) {
                title1 {
                    text(bokmal { +"Folketrygdloven §12-8 - Uføretidspunkt" })
                }
                paragraph {
                    text(bokmal { +"Uføretidspunktet er det tidspunktet inntektsevnen din ble varig nedsatt med minst halvparten." })
                }
                paragraph {
                    text(bokmal { +"Vi trenger informasjon om hvilke ytelser du har mottatt i " + fritekst("land") + ":" })
                    list {
                        item { text(bokmal { +"Har du mottatt sykepenger, rehabiliteringspenger eller arbeidsledighetspenger? Hvilke ytelser og hvilke perioder?" }) }
                    }
                }
            }
            showIf(uforetrygdensStorrelse) {
                title1 {
                    text(bokmal { +"Folketrygdloven §12-13 - Uføretrygdens størrelse" })
                }
                paragraph {
                    text(bokmal { +"Sivilstatusen din kan påvirke hvor mye du får i uføretrygd." })
                }
                paragraph {
                    list {
                        item { text(bokmal { +"Hvis du er gift eller har samboer: Oppgi dato for når dere ble gift/samboere, samt navn, fødselsdato, statsborgerskap og kopi av pass eller nasjonalt ID-kort til ektefelle/samboer." }) }
                        item { text(bokmal { +"Hvis du er skilt: Oppgi dato for skilsmissen." }) }
                    }
                }
            }
            showIf(bankopplysninger) {
                title1 {
                    text(bokmal { +"Utbetaling av uføretrygd" })
                }
                paragraph {
                    text(bokmal { +"For å kunne betale ut uføretrygden trenger vi at du sender oss dine bankopplysninger. Vi ber om at du fyller ut vedlagte skjema vedrørende bankopplysninger og returnerer dette til oss. Dette kan du sende via " + fritekst("land") + " trygdemyndigheter, per post sammen med kopi av legitimasjon, eller logge inn på \"Ditt Nav\" og registrere opplysningene dine der." })
                }
            }
            title1 {
                text(bokmal { +"Frist for innsending" })
            }
            paragraph {
                text(bokmal { +"Vennligst send oss dokumentasjonen innen " + fritekst("dato") + ". Bruk vedlagte førsteside og send til adressen som står der eller du kan laste det opp på \"Ditt Nav\"." })
            }
            showIf(purring) {
                title1 {
                    text(bokmal { +"Viktig informasjon" })
                }
                paragraph {
                    text(bokmal { +"Vi gjør deg oppmerksom på at enhver som fremsetter et krav om uføretrygd plikter å levere de opplysninger som er nødvendig for saksbehandlingen, jf. folketrygdloven § 21-3. Hvis vi ikke får svar, vil vi behandle saken med de opplysningene vi har. Det kan føre til at søknaden blir avslått." })
                }
                paragraph {
                    text(bokmal { +"We would like to inform you that anyone applying for disability benefits is required to provide the information necessary to process the case, in accordance with folketrygdloven § 21-3. If we do not receive a response, your claim will be processed based on the information currently available to us." })
                }
            }
            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
