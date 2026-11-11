package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.Constants.FULLMAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.INNSYNSKRAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGERETTIGHETER_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGE_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKTTELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.RettigheterPersonopplysninger
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggDineRettigheterOgMulighetTilAaKlageAfpPrivat =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Rettighetene dine og mulighet til å klage " },
                nynorsk { +"Rettane dine og høve til å klage" },
                english { +"Your rights and appeal options" },
            )
        },
        includeSakspart = false
    ) {
       title2() {
            text(
                bokmal { +"Du kan klage på vedtaket " },
                nynorsk { +"Du kan klage på vedtaket" },
                english { +"You can appeal the decision" },
            )
        }

        paragraph() {
            text(
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket kom fram til deg. " +
                        "Dette følger av AFP-tilskottsloven § 17 andre ledd andre punktum, sammenholdt med folketrygdloven § 21-12. " +
                        "Du finner skjema og informasjon på $KLAGE_URL." },
                nynorsk { +"Om du meiner vedtaket er feil, kan du klage innan 6 veker frå den datoen vedtaket har komme fram til deg. " +
                        "Dette følgjer av AFP-tilskottslova § 17 andre ledd andre punktum, samanhalden med folketrygdlova § 21-12. " +
                        "Du finn skjema og informasjon på $KLAGE_URL." },
                english { +"If you believe that the decision is incorrect, you may appeal within 6 weeks from the date on which this decision was delivered to you. " +
                        "This follows from section 17, second paragraph, second sentence, of the AFP Subsidies Act, read in conjunction with section 21-12 of the National Insurance Act. " +
                        "You will find the form and further information at $KLAGE_URL." },
            )
        }

        paragraph() {
            text(
                bokmal { +"Nav kan veilede deg på telefon om hvordan du sender en klage. " +
                        "Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. Kontakt oss på telefon $KONTAKTTELEFON_PENSJON hvis du trenger hjelp." },
                nynorsk { +"Nav kan rettleie deg på telefon om korleis du sender ein klage. " +
                        "Nav-kontoret ditt kan også hjelpe deg med å skrive ein klage. Kontakt oss på telefon $KONTAKTTELEFON_PENSJON om du treng hjelp." },
                english { +"Nav can guide you over the phone on how to submit an appeal. " +
                        "Your local Nav office can also assist you in writing an appeal. Please call us on +47 $KONTAKTTELEFON_PENSJON if you need help." },
            )
        }

        paragraph() {
            text(
                bokmal { +"Hvis du får medhold i klagen, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket, for eksempel hjelp fra advokat. " +
                        "Du kan ha krav på fri rettshjelp etter rettshjelploven. " +
                        "Du kan få mer informasjon om denne ordningen hos advokater, statsforvalteren eller Nav. " },
                nynorsk { +"Om du får medhald i klagen, kan du få dekt vesentlege utgifter som har vore naudsynte for å få endra vedtaket, til dømes hjelp frå advokat. " +
                        "Du kan ha krav på fri rettshjelp etter rettshjelplova. " +
                        "Du kan få meir informasjon om denne ordninga hos advokatar, statsforvaltaren eller Nav." },
                english { +"If your appeal is successful, you may be entitled to compensation for necessary costs incurred in having the decision overturned, such as legal assistance from a lawyer. " +
                        "You may be eligible for free legal aid under the Legal Aid Act. " +
                        "More information about the legal aid scheme is available from lawyers, the County Governor, or Nav." },
            )
        }

        paragraph() {
            text(
                bokmal { +"Du kan lese om saksomkostninger i forvaltningsloven § 36." },
                nynorsk { +"Du kan lese om sakskostnader i forvaltningslova § 36." },
                english { +"You can read about compensation for costs in section 36 of the Public Administration Act." },
            )
        }

        paragraph() {
            text(
                bokmal { +"Hvis du sender klage i posten, må du signere klagen." },
                nynorsk { +"Om du sender klage i posten, må du signere klagen." },
                english { +"If you send your appeal by post, you must sign the appeal." },
            )
        }

        paragraph() {
            text(
                bokmal { +"Mer informasjon om klagerettigheter finner du på $KLAGERETTIGHETER_URL." },
                nynorsk { +"Meir informasjon om klagerettar finn du på $KLAGERETTIGHETER_URL." },
                english { +"You can find additional information about appeal rights at $KLAGERETTIGHETER_URL." },
            )
        }

        title2() {
            text(
                bokmal { +"Du har rett til innsyn i saken din " },
                nynorsk { +"Du har rett til innsyn i saka di" },
                english { +"You have the right to access your case" },
            )
        }

        paragraph() {
            text(
                bokmal { +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven § 18. " +
                        "Kontakt oss om du vil se dokumentene i saken din.Du kan lese mer om innsynsretten på $INNSYNSKRAV_URL. " },
                nynorsk { +"Du har rett til å sjå dokumenta i saka di. Dette følgjer av forvaltningslova § 18. " +
                        "Kontakt oss om du vil sjå dokumenta i saka di. Du kan lese meir om innsynsretten på $INNSYNSKRAV_URL." },
                english { +"You have the right to access the documents in your case. This follows from section 18 of the Public Administration Act. " +
                        "Contact us if you wish to see the documents in your case. You can read more about your right of access at $INNSYNSKRAV_URL." },
            )
        }

        includePhrase(RettigheterPersonopplysninger)

        title2() {
            text(
                bokmal { +"Du har rett til å få hjelp fra andre " },
                nynorsk { +"Du har rett til å få hjelp frå andre" },
                english { +"You have the right to receive assistance from others" },
            )
        }

        paragraph() {
            text(
                bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel fra en " +
                        "advokat, rettshjelper, en organisasjon du er medlem av eller en myndig person over 18 år. " +
                        "Dette følger av forvaltningsloven § 12. Hvis den som hjelper deg ikke er advokat, må du gi denne personen skriftlig fullmakt. " +
                        "Bruk skjemaet du finner på $FULLMAKT_URL. Ta kontakt på telefon $KONTAKTTELEFON_PENSJON hvis du ikke kan bruke det digitale skjemaet. " },
                nynorsk { +"Du kan be om hjelp frå andre under heile saksbehandlinga, til dømes frå ein " +
                        "advokat, rettshjelpar, ein organisasjon du er medlem av eller ein myndig person over 18 år. " +
                        "Dette følgjer av forvaltningslova § 12. Om den som hjelper deg ikkje er advokat, må du gi denne personen skriftleg fullmakt. " +
                        "Bruk skjemaet du finn på $FULLMAKT_URL. Ta kontakt på telefon $KONTAKTTELEFON_PENSJON om du ikkje kan bruke det digitale skjemaet." },
                english { +"You may request assistance from others throughout the processing of your case, for example, from a lawyer, " +
                        "legal services provider, an organization of which you are a member, or a competent person over the age of 18. " +
                        "This follows from section 12 of the Public Administration Act. If the person assisting you is not a lawyer, " +
                        "you must provide them with a written power of attorney authorising them to act on your behalf. " +
                        "Use the form available at $FULLMAKT_URL. Please contact us on +47 $KONTAKTTELEFON_PENSJON if you are unable to use the digital form." },
            )
        }

        title2() {
            text(
                bokmal { +"Du har rett til å få veiledning fra Nav " },
                nynorsk { +"Du har rett til å få rettleiing frå Nav" },
                english { +"You have the right to receive guidance from Nav" },
            )
        }

        paragraph() {
            text(
                bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. " +
                        "Dette følger av forvaltningsloven § 11. Ta kontakt på telefon $KONTAKTTELEFON_PENSJON eller på $KONTAKT_URL hvis du har spørsmål. " },
                nynorsk { +"Vi har plikt til å rettleie deg om rettar og plikter i saka di, både før, under og etter saksbehandlinga. " +
                        "Dette følgjer av forvaltningslova § 11. Ta kontakt på telefon $KONTAKTTELEFON_PENSJON eller på $KONTAKT_URL om du har spørsmål." },
                english { +"We have a duty to inform you of your rights and obligations in your case before, during, and after the case processing. " +
                        "This follows from section 11 of the Public Administration Act. " +
                        "If you have any questions, please contact us by phone at +47 $KONTAKTTELEFON_PENSJON or via $KONTAKT_URL." },
            )
        }
    }