package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon


@TemplateModelHelpers
val dineRettigheterOgPlikterBosattUtland = createAttachment(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Rettane og pliktene dine",
        English to "Your rights and obligations",
    ),
    includeSakspart = false,
) {
    meldFraOmEndringer()
    veiledningFraNavForvaltningsloven11()
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)
    duHarRettTilInnsynISakenDin()
    klagePaaVedtaketFolketrygdloven2112(true.expr())
}

@TemplateModelHelpers
val dineRettigheterOgPlikterNasjonal = createAttachment(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Rettane og pliktene dine",
        English to "Your rights and obligations",
    ),
    includeSakspart = false,
) {
    meldFraOmEndringer()
    veiledningFraNavForvaltningsloven11()
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)
    duHarRettTilInnsynISakenDin()
    klagePaaVedtaketFolketrygdloven2112(false.expr())
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, Any>.meldFraOmEndringer() {
    title2 {
        text(
            bokmal { +"Meld fra om endringer" },
            nynorsk { +"Meld frå om endringar" },
            english { +"Report changes" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du må melde fra med en gang det skjer viktige endringer, som" },
            nynorsk { +"Du må melde frå med ein gong det skjer viktige endringar. Døme på slike endringar kan vere" },
            english { +"You must report any important changes as soon as they occur, such as" },
        )
        list {
            item {
                text(
                    bokmal { +"endringer av familie- eller omsorgsforhold" },
                    nynorsk { +"endra familie- eller omsorgsforhold" },
                    english { +"changes in family or care relationships" },
                )
            }
            item {
                text(
                    bokmal { +"flytting eller opphold i et annet land over tid" },
                    nynorsk { +"flytting eller langvarig opphald i eit anna land" },
                    english { +"relocation or residence in another country over time" },
                )
            }
            item {
                text(
                    bokmal { +"varig opphold i institusjon" },
                    nynorsk { +"varig opphald på ein institusjon" },
                    english { +"permanent residence in an institution" },
                )
            }
            item {
                text(
                    bokmal { +"du blir innvilget uføretrygd (barnepensjonen skal reduseres etter det du får utbetalt i uføretrygd)" },
                    nynorsk { +"du blir innvilga uføretrygd (barnepensjonen skal reduserast etter det du får utbetalt i uføretrygd)" },
                    english { +"you are granted disability benefits (children's pension will be reduced according to what you receive in disability benefits)" },
                )
            }
        }
        text(
            bokmal { +"Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av barnepensjon, " +
                "og du må straks melde fra om eventuelle feil til Nav. Er det utbetalt for mye barnepensjon fordi " +
                "Nav ikke har fått beskjed, må pengene vanligvis betales tilbake." },
            nynorsk { +"Du er ansvarleg for å følgje med på bevegelsar på kontoen for utbetaling av barnepensjon, " +
                    "og må straks melde frå til Nav dersom du blir merksam på feil. " +
                    "Viss det har blitt utbetalt for mykje barnepensjon fordi Nav ikkje har fått beskjed om endringar, " +
                    "må pengane vanlegvis betalast tilbake." },
            english { +"You are responsible for staying informed of the transactions in your bank account " +
                    "regarding the payment of the children's pension, and you must immediately report any errors to Nav. " +
                    "If too much children's pension has been paid because Nav has not been notified, " +
                    "the money must normally be repaid." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledningFraNavForvaltningsloven11() {
    title2 {
        text(
            bokmal { +"Veiledning fra Nav - forvaltningsloven § 11" },
            nynorsk { +"Rettleiing frå Nav – forvaltingslova § 11" },
            english { +"Guidance from Nav – Section 11 of the Public Administration Act" },
        )
    }
    paragraph {
        text(
            bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                "vårt beste for å hjelpe deg." },
            nynorsk { +"Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, " +
                    "vil vi gjere vårt beste for å hjelpe deg." },
            english { +"We have a duty to advise you of your rights and obligations in your case – before, " +
                    "during and after the case has been processed. " +
                    "If you have any questions or are unsure about anything, we will do our best to help you." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duHarRettTilInnsynISakenDin() {
    title2 {
        text(
            bokmal { +"Du har rett til innsyn i saken din - forvaltningsloven § 18 " },
            nynorsk { +"Du har rett til innsyn i saka di – forvaltingslova § 18" },
            english { +"You have the right to access the documents in your case – Section 18 of the Public Administration Act" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du har som hovedregel rett til å se dokumentene i saken din. Hvis du ønsker innsyn, kan du kontakte oss på telefon eller per post." },
            nynorsk { +"Du har som hovudregel rett til å sjå dokumenta i saka di. Kontakt oss på telefon eller per post dersom du ønskjer innsyn." },
            english { +"As a general rule, you have the right to see the documents in your case. If you want access, you can contact us by phone or mail." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.klagePaaVedtaketFolketrygdloven2112(bosattUtland: Expression<Boolean>) {
    title2 {
        text(
            bokmal { +"Klage på vedtaket - folketrygdloven § 21-12" },
            nynorsk { +"Klage på vedtaket – folketrygdlova § 21-12" },
            english { +"Appealing decisions – Section 21-12 of the National Insurance Act" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                "fattet vedtaket vil da vurdere saken din på nytt." },
            nynorsk { +"Du kan klage på vedtaket innan seks veker frå du får det. " +
                    "Kontoret som fatta vedtaket, vil då vurdere saka di på nytt." },
            english { +"You may appeal a decision within six weeks of receiving it. " +
                    "The office that made the decision will then reconsider your case." },
        )
    }

    paragraph {
        text(
            bokmal { +"Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet som du finner på ${Constants.KLAGE_URL}. " +
                    "Trenger du hjelp, er du velkommen til å ringe oss på telefon " },
            nynorsk { +"Du må setje fram klaga skriftleg og oppgi namn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet du finn på ${Constants.KLAGE_URL}. " +
                    "Viss du treng hjelp, må du gjerne ringje oss på telefon " },
            english { +"The appeal must be made in writing and contain your name, national identity number and address. " +
                    "Feel free to use the form that you find online: ${Constants.Engelsk.KLAGE_URL}. " +
                    "If you need help, you are welcome to call us by phone " },
        )
        kontakttelefonPensjon(bosattUtland)
        text(
            bokmal { +"." },
            nynorsk { +"." },
            english { +"." },
        )
    }
}
