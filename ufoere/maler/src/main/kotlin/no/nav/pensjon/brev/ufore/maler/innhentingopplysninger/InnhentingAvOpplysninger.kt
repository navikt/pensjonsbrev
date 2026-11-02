package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNHENTING_AV_OPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InnhentingAvOpplysninger : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNHENTING_AV_OPPLYSNINGER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val arsak = saksbehandlervalg("arsak", "Velg årsak:").enum<ArsakEnum>()

        title {
            text (bokmal { + "Innhenting av opplysninger" })
        }
        outline {
            showIf(arsak.equalTo(ArsakEnum.UNDER_18)) {
                title1 {
                    text(bokmal { +"Du er under 18 år på søknadstidspunktet, søknaden må derfor framsettes av foresatte/verge" })
                }
                paragraph {
                    text(bokmal { +"Vi viser til din søknad om uføretrygd. " })
                }
                paragraph {
                    text(bokmal { +"Du er under 18 år på søknadstidspunktet, og søknaden må derfor framsettes av foresatte/verge. " })
                }
                paragraph {
                    text(bokmal { +"Kopi av søknaden sendes i retur per post for signering av foresatte/verge. Vedlagte forside bør benyttes når du sender søknaden på nytt. " })
                }
                paragraph {
                    text(bokmal { +"Søknaden kan ikke behandles som et gyldig krav før vi har mottatt søknad i retur signert av foresatte/verge. " })
                }
                paragraph {
                    text(bokmal { +"Dersom vi ikke mottar søknaden underskrevet av foresatte/verge innen to uker etter at du har mottatt dette brevet, vil saken henlegges. Det må da eventuelt søkes på nytt dersom du fortsatt vil søke om uføretrygd. " })
                }
            }.orShowIf(arsak.equalTo(ArsakEnum.UNDERSKREVET_AV_ANDRE)) {
                title1 {
                    text(bokmal { + "Du er over 18 år og søknaden ikke er skrevet under av deg" })
                }
                paragraph {
                    text(bokmal { + "Du har søkt om uføretrygd. Vi kan ikke behandle søknaden fordi du er over 18 år og søknaden ikke er skrevet under av deg. " })
                }
                paragraph {
                    text(bokmal { + "Hvis du ikke selv er i stand til å søke om uføretrygd, trenger vi dokumentasjon på at det finnes et gyldig vergeforhold mellom deg og den som har underskrevet denne søknaden. Sender du dette, kan søknaden anses som gyldig. " })
                }
                paragraph {
                    text(bokmal { + "Kopi av søknaden som vi mottok følger vedlagt. " })
                }
                paragraph {
                    text(bokmal { + "Vedlagte forside kan benyttes ved innsendelse av fullmakt/dokumentasjon på vergeforhold. " })
                }
                paragraph {
                    text(bokmal { + "Hvis vi ikke får bekreftelse på vergeforhold eller søknad underskrevet av deg selv innen to uker etter at du har mottatt dette brevet, vil saken din henlegges. Det må da eventuelt søkes på nytt dersom du fortsatt vil søke om uføretrygd. " })
                }
            }.orShowIf(arsak.equalTo(ArsakEnum.MANGLENDE_FORMKRAV_KLAGE)) {
                title1 {
                    text(bokmal { + "Manglende formkrav klage" })
                }
                paragraph {
                    text(bokmal { + "Vi har den " + fritekst("dato") + " mottatt klagen din over vedtaket vårt av " + fritekst("dato") + ". " })
                }
                paragraph {
                    text(bokmal { + "Det følger av forvaltningsloven § 32 at klagen skal være skriftlig og underskrevet. Klagen skal også nevne hvilket vedtak det klages på og hvilken endring som ønskes. Dersom klagen er mangelfull, setter vi en kort frist slik at klager kan rette opp dette. Dersom manglene ikke blir rettet innen fristen og vilkårene for å behandle klagen ikke er oppfylt, skal klagen avvises. Dette følger av forvaltningsloven § 33. " })
                }
                paragraph {
                    text(bokmal { + fritekst("Tilpass etter hva som er mangelen i klagen. Vi ber derfor om at du underskriver på vedlagt kopi av klagen / opplyser om hvilket vedtak du klager på / opplyser om hvilken endring du ønsker i vedtaket/ hvorfor vedtaket er feil.") })
                }
                paragraph {
                    text(bokmal { + "Klagen må sendes inn til skanning sammen med vedlagte forside til adressen oppgitt på forsidearket. På nav.no kan du sende inn dokumenter elektronisk ved bruk av BankID. Det beste er om klagen sendes til oss på nytt på denne måten. " })
                }
                paragraph {
                    text(bokmal { + "Vi ber deg sende klagen innen " + fritekst("klagefristenes utløp/14 dager") + ". Dersom du trenger hjelp til å utforme klagen ber vi deg ta kontakt med ditt lokale Nav-kontor. " })
                }
                paragraph {
                    text(bokmal { + "Klagen din kan bli avvist dersom vi ikke mottar klagen innen fristen. " })
                }
            }.orShowIf(arsak.equalTo(ArsakEnum.MANGLENDE_UNDERSKRIFT)) {
                title1 {
                    text(bokmal { + "Manglende underskrift" })
                }
                paragraph {
                    text(bokmal { + "Vi har mottatt din søknad om uføretrygd uten underskrift på søknadsskjemaet. " })
                }
                paragraph {
                    text(bokmal { + "Søknaden må underskrives slik at det sikres at rett person fremmer søknaden. Søknaden behandles på bakgrunn av de opplysningene som framkommer i skjemaet. Underskriften din bekrefter at disse opplysningene er korrekte. " })
                }
                paragraph {
                    text(bokmal { + "Vi ber deg om å underskrive på vedlagte kopi av søknadsskjemaet. Dette brevet kan du lese på Ditt Nav, men vi har sendt både brev, kopi av søknaden og forsideark til deg i posten. " })
                }
                paragraph {
                    text(bokmal { + "Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider nav.no/soknad eller nav.no/ettersende " })
                }
                paragraph {
                    text(bokmal { + "Dersom vi ikke mottar underskrevet søknadsskjema " })
                    text(bokmal{ + "innen to uker" }, BOLD)
                    text(bokmal{ +" etter at du har mottatt dette brevet, vil saken henlegges. Du må da eventuelt søke på nytt. " })
                }
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}

enum class ArsakEnum(override val displayText: String) : SaksbehandlerValgEnum {
    UNDER_18("Under 18, må framsettes av foresatte/verge"),
    UNDERSKREVET_AV_ANDRE("Over 18, underskrevet av andre"),
    MANGLENDE_FORMKRAV_KLAGE("Manglende formkrav klage"),
    MANGLENDE_UNDERSKRIFT("Manglende underskrift"),
}
