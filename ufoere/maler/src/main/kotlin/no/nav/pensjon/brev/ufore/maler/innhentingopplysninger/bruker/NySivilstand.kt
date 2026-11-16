package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger.bruker

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_NY_SIVILSTAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object NySivilstand : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_NY_SIVILSTAND
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om sivilstand",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val arsak = saksbehandlervalg("arsak", "Velg årsak:").enum<ArsakNySivilstand>()

        title {
            text (bokmal { + "Orientering om sivilstand" },
                nynorsk { + "Orientering om sivilstand" })
        }
        outline {
            showIf(arsak.equalTo(ArsakNySivilstand.NY_SIVILSTAND)) {
                paragraph {
                    text(
                        bokmal { +"Vi har fått opplysninger om at du har blitt " + fritekst("enslig/skilt/separert") + ". Som enslig kan du ha krav på økt uføretrygd. Den aktuelle lovhjemmelen for denne type endringer fremgår av folketrygdloven § 12-13. " },
                        nynorsk { +"Vi har fått opplysningar om at du har blitt " + fritekst("einsleg/skild/separert") + ". Som einsleg kan du ha krav på auka uføretrygd. Den aktuelle lovheimelen for denne typen endringar kjem fram av folketrygdlova § 12-13. " })
                }
                paragraph {
                    text(
                        bokmal { +"Våre opplysninger fra folkeregisteret viser at du og " + fritekst("fraseparert ektefelle /samboer") + " fortsatt bor på samme adresse. Det er den faktiske bosituasjonen din som avgjør om du har krav på økt uføretrygd. Vi ber deg melde fra skriftlig til Nav når du, eller din partner, har endret adresse hos folkeregisteret. " },
                        nynorsk { +"Opplysningane våre frå folkeregisteret viser at du og " + fritekst("fraseparert ektefelle /samboer") + " framleis bur på same adresse. Det er den faktiske busituasjonen din som avgjer om du har krav på auka uføretrygd. Vi ber deg melde frå skriftleg til Nav når du, eller partneren din, har endra adresse hos folkeregisteret. " })
                }
                paragraph {
                    text(
                        bokmal { +"Vi minner om at vi ikke kan gjøre noe i din sak, før vi har fått opplysninger om endring av den faktiske bosituasjonen din. " },
                        nynorsk { +"Vi minner om at vi ikkje kan gjere noko i saka di, før vi har fått opplysningar om endring av den faktiske busituasjonen din. " })
                }
                paragraph {
                    text(
                        bokmal { +"Får du barnetillegg, og du og barnet ikke har felles folkeregistrert adresse, må du sende oss dokumentasjon på faktisk forsørgelse. Dokumentasjon på forsørgelse kan være avtale om bidrag og/eller samvær som må være undertegnet av begge foreldrene. " },
                        nynorsk { +"Får du barnetillegg, og du og barnet ikkje har felles folkeregistrert adresse, må du sende oss dokumentasjon på faktisk forsørging. Dokumentasjon på forsørging kan vere avtale om bidrag og/eller samvær som må vere underteikna av begge foreldra. " })
                }
                paragraph {
                    text(
                        bokmal { +"Selv om uføretrygden din ikke blir endret som følge av sivilstandsendringen, må du melde fra til oss om alle hendelser som kan få betydning for uføretrygden din. Dette fremgår av folketrygdloven § 21-3. " },
                        nynorsk { +"Sjølv om uføretrygda di ikkje blir endra som følgje av sivilstandsendringa, må du melde frå til oss om alle hendingar som kan få tyding for uføretrygda di. Dette kjem fram av folketrygdlova § 21-3. " })
                }
            }.orShowIf(arsak.equalTo(ArsakNySivilstand.EKTEFELLE_UTLAND)) {
                paragraph {
                    text(bokmal { +"Vi har fått informasjon om at du har ektefelle som bor i utlandet. Hvis din ektefelle skal flytte til Norge, er det viktig at du gir oss beskjed med en gang, slik at vi beregner riktig utbetaling av din uføretrygd. Vi ber deg i så fall gi oss informasjon med " + fritekst("flyttedato/planlagt flyttedato") + ". Dette er viktig for å forhindre eventuell feilutbetaling. " },
                        nynorsk { +"Vi har fått informasjon om at du har ektefelle som bur i utlandet. Dersom ektefellen din skal flytte til Noreg, er det viktig at du gir oss beskjed med ein gong, slik at vi reknar ut riktig utbetaling av uføretrygda di. Vi ber deg i så fall gi oss informasjon med " + fritekst("flyttedato/planlagt flyttedato") + ". Dette er viktig for å forhindre eventuell feilutbetaling. " })
                }
            }

            includePhrase(Felles.HarDuSporsmal)
        }
    }
}

enum class ArsakNySivilstand(override val displayText: String) : SaksbehandlerValgEnum {
    NY_SIVILSTAND("Ny sivilstand"),
    EKTEFELLE_UTLAND("Ektefelle bo i utlandet"),
}

