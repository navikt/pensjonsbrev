package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggDineRettigheterAfpEo =
    createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Dine rettigheter" },
                nynorsk { +"Rettane dine" },
            )
        }
    ) {
        title2 {
            text(
                bokmal { +"Du kan klage på vedtaket" },
                nynorsk { +"Du kan klage på vedtaket" }
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage innen seks uker fra vedtaket har kommet fram til deg. Du finner skjema og informasjon på nav.no/klage." },
                nynorsk { +"Hvis du mener vedtaket er feil, kan du klage innen seks uker fra vedtaket har kommet fram til deg. Du finner skjema og informasjon på nav.no/klage." },
            )
        }
        paragraph {
            text(
                bokmal { +"Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. Kontakt oss på telefon 55 55 33 33 <34> hvis du trenger hjelp." },
                nynorsk { +"Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. Kontakt oss på telefon 55 55 33 33 <34> hvis du trenger hjelp." },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du får medhold i klagen, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket, for eksempel hjelp fra advokat. Du kan ha krav på fri rettshjelp etter rettshjelploven. Du kan få mer informasjon om denne ordningen hos advokater, statsforvalteren eller Nav." },
                nynorsk { +"Hvis du får medhold i klagen, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket, for eksempel hjelp fra advokat. Du kan ha krav på fri rettshjelp etter rettshjelploven. Du kan få mer informasjon om denne ordningen hos advokater, statsforvalteren eller Nav." },
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du sender klage i posten, må du signere klagen." },
                nynorsk { +"Hvis du sender klage i posten, må du signere klagen." },
            )
        }
        paragraph {
            text(
                bokmal { +"Mer informasjon finner du på ${Constants.KLAGERETTIGHETER_URL}. Du kan lese om klage i forvaltningsloven kapittel VI." },
                nynorsk { +"Mer informasjon finner du på ${Constants.KLAGERETTIGHETER_URL}. Du kan lese om klage i forvaltningsloven kapittel VI." },
            )
        }

        title2 {
            text(
                bokmal { +"Du har rett til innsyn i saken din" },
                nynorsk { +"Du har rett til innsyn i saken din" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven § 18." },
                nynorsk { +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven § 18." },
            )
        }

        title2 {
            text(
                bokmal { +"Du har rettigheter knyttet til personopplysningene dine" },
                nynorsk { +"Du har rettigheter knyttet til personopplysningene dine" },
            )
        }
        paragraph {
            text(
                bokmal { +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke rettigheter du har, på ${Constants.PERSONVERNERKLAERING_URL}." },
                nynorsk { +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke rettigheter du har, på ${Constants.PERSONVERNERKLAERING_URL}." },
            )
        }

        title2 {
            text(
                bokmal { +"Du har rett til å få hjelp fra andre" },
                nynorsk { +"Du har rett til å få hjelp fra andre" },
            )
        }
        paragraph {
            text(
                bokmal { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel fra en advokat, rettshjelper, en organisasjon du er medlem av, eller en myndig person over 18 år. Dette følger av forvaltningsloven § 12. Hvis den som hjelper deg ikke er advokat, må du gi denne personen skriftlig fullmakt. Bruk skjemaet du finner på ${Constants.FULLMAKT_URL}. Ta kontakt på telefon 55 55 33 34 hvis du ikke kan bruke det digitale skjemaet." },
                nynorsk { +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel fra en advokat, rettshjelper, en organisasjon du er medlem av, eller en myndig person over 18 år. Dette følger av forvaltningsloven § 12. Hvis den som hjelper deg ikke er advokat, må du gi denne personen skriftlig fullmakt. Bruk skjemaet du finner på ${Constants.FULLMAKT_URL}. Ta kontakt på telefon 55 55 33 34 hvis du ikke kan bruke det digitale skjemaet." },
            )
        }

        title2 {
            text(
                bokmal { +"Du har rett til å få veiledning fra Nav" },
                nynorsk { +"Du har rett til å få veiledning fra Nav" },
            )
        }
        paragraph {
            text(
                bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dette følger av forvaltningsloven § 11." },
                nynorsk { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dette følger av forvaltningsloven § 11." },
            )
        }
    }