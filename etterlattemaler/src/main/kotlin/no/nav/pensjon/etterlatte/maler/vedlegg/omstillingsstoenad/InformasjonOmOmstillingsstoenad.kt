package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadDataSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadDataSelectors.tidligereFamiliepleier


data class InformasjonOmOmstillingsstoenadData(
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
)

fun informasjonOmOmstillingsstoenad(): AttachmentTemplate<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData> {
    return createAttachment(
        title = newText(
            Bokmal to "Informasjon til deg som mottar omstillingsstønad",
            Nynorsk to "Informasjon til deg som får omstillingsstønad",
            English to "Information for recipients of adjustment allowance",
        ),
        includeSakspart = false,
    ) {
        aktivitet(argument.tidligereFamiliepleier)
        hvisDuIkkeFyllerAktivitetsplikten()
        inntektOgOmstillingsstoenad()
        endretInntekt()
        hvilkenInntektReduseresEtter()
        hvordanMeldeEndringer(argument.bosattUtland)
        utbetalingTilKontonummer()
        skatt()
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.aktivitet(
    tidligereFamiliepleier: Expression<Boolean>
) {
    title2 {
        text(
            Bokmal to "Du må være i aktivitet når du mottar omstillingsstønad",
            Nynorsk to "Du må vere i aktivitet når du får omstillingsstønad",
            English to "You must be active while receiving adjustment allowances",
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Når det er gått seks måneder etter ".expr() +
                    ifElse(tidligereFamiliepleier, "pleieforholdet opphørte", "dødsfallet") +
                    " er det et krav for å motta omstillingsstønad at du er i minst 50 prosent arbeid eller annen " +
                    "aktivitet med sikte på å komme i arbeid. Etter et år kan det forventes at du er i 100 prosent aktivitet. " +
                    "Dette kalles for aktivitetsplikt.",
            Nynorsk to "For å kunne halde fram med å få omstillingsstønad når det har gått seks månader sidan ".expr() +
                    ifElse(tidligereFamiliepleier, "pleieforholdet opphøyrde", "dødsfallet") +
                    ", må du vere i minst 50 prosent arbeid eller annan aktivitet med sikte på å kome i arbeid. Etter " +
                    "eitt år er det forventa at du er i 100 prosent aktivitet. Dette blir kalla aktivitetsplikt.",
            English to "Once six months have passed after the ".expr() +
                    ifElse(tidligereFamiliepleier, "care period ended", "death") +
                    ", receiving adjustment allowance is contingent upon working at least 50 percent or involved in " +
                    "another activity with the aim of finding employment. After one year, you will be expected to be " +
                    "active 100 percent. This is called the activity obligation.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du fyller aktivitetsplikten hvis du",
            Nynorsk to "Du innfrir aktivitetsplikta dersom du",
            English to "You meet the activity obligation if you",
        )
        list {
            item {
                text(
                    Bokmal to "jobber",
                    Nynorsk to "jobbar",
                    English to "are working",
                )
            }
            item {
                text(
                    Bokmal to "er selvstendig næringsdrivende",
                    Nynorsk to "er sjølvstendig næringsdrivande",
                    English to "are self-employed/sole proprietor",
                )
            }
            item {
                text(
                    Bokmal to "etablerer egen virksomhet",
                    Nynorsk to "etablerer eiga verksemd",
                    English to "setting up your own business",
                )
            }
            item {
                text(
                    Bokmal to "tar utdanning som er nødvendig og hensiktsmessig",
                    Nynorsk to "tek utdanning som er nødvendig og føremålstenleg",
                    English to "taking a necessary and suitable education",
                )
            }
            item {
                text(
                    Bokmal to "er reell arbeidssøker",
                    Nynorsk to "er reell arbeidssøkjar",
                    English to "are a genuine job seeker",
                )
            }
            item {
                text(
                    Bokmal to "har fått tilbud om jobb",
                    Nynorsk to "har fått tilbod om jobb",
                    English to "have received a job offer",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Det er unntak fra aktivitetsplikten som gir rett til omstillingsstønad. Dette gjelder " +
                    "blant annet hvis du har omsorgen for barn under ett år, om du har dokumentert sykdom som " +
                    "forhindrer deg i å være i aktivitet, eller om du er innvilget etter unntaksregelen for de " +
                    "født i 1963 eller tidligere med lav inntekt.",
            Nynorsk to "Det finst unntak frå aktivitetsplikta som gir rett til omstillingsstønad. Dette gjeld " +
                    "mellom anna dersom du har omsorg for barn under eitt år, eller du har dokumentert sjukdom som " +
                    "hindrar deg i å vere i aktivitet.",
            English to "There are some exemptions to the activity obligation which entitle you to adjustment " +
                    "allowance. This applies e.g. if you are caring for children under one year of age, or if you " +
                    "have documented an illness that prevents you from being active. ",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese mer om aktivitetsplikten og unntakene på " + Constants.OMS_AKTIVITET_URL + ".",
            Nynorsk to "Du kan lese meir om aktivitetsplikta og unntaka på " + Constants.OMS_AKTIVITET_URL + ".",
            English to "You can read more about the activity obligation and its exceptions online: " +
                    Constants.OMS_AKTIVITET_URL + ".",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvisDuIkkeFyllerAktivitetsplikten() {
    title2 {
        text(
            Bokmal to "Hva skjer hvis du ikke fyller aktivitetsplikten?",
            Nynorsk to "Kva skjer dersom du ikkje innfrir aktivitetsplikta?",
            English to "What happens if you do not fulfil the activity obligation?",
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du ikke fyller aktivitetsplikten, kan utbetalingen av stønaden stoppe inntil " +
                    "vilkårene er oppfylt igjen. Det blir midlertidig stans av utbetalingene dine hvis du",
            Nynorsk to "Dersom du ikkje innfrir aktivitetsplikta, kan utbetalinga av stønaden bli stansa " +
                    "fram til du igjen oppfyller vilkåra. Utbetalingane blir stansa mellombels dersom du",
            English to "If you do not meet the activity obligation, allowance payments may stop until the " +
                    "condition is met again. Your payments will be temporarily suspended if you",
        )
        list {
            item {
                text(
                    Bokmal to "sier nei til jobb",
                    Nynorsk to "takkar nei til jobb",
                    English to "say no to work",
                )
            }
            item {
                text(
                    Bokmal to "sier nei til å delta i, eller slutter i et arbeidsmarkedstiltak",
                    Nynorsk to "sluttar i eller seier nei til å delta i eit arbeidsmarknadstiltak",
                    English to "say no to participating in, or quit, a labour market scheme",
                )
            }
            item {
                text(
                    Bokmal to "sier opp, eller på andre måter slutter i jobben din",
                    Nynorsk to "seier opp eller på andre måtar sluttar i jobben",
                    English to "resign, or otherwise quit your job",
                )
            }
            item {
                text(
                    Bokmal to "blir avskjediget eller sagt opp på grunn av forhold som du selv er skyld i",
                    Nynorsk to "får avskjed eller blir oppsagd på grunn av forhold du sjølv er skuld i",
                    English to "are fired or dismissed due to circumstances for which you are at fault",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Nav må kunne komme i kontakt med deg for å følge deg opp ved behov. Får vi ikke " +
                    "kontakt med deg, kan vi stoppe stønaden din.",
            Nynorsk to "Nav må kunne kontakte deg for å gi oppfølging ved behov. Dersom vi ikkje får kontakt " +
                    "med deg, kan vi stoppe stønaden.",
            English to "Nav must be able to get in touch with you to follow up your case, whenever needed. " +
                    "We may stop your allowance if we cannot get in touch with you. ",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.inntektOgOmstillingsstoenad() {
    title2 {
        text(
            Bokmal to "Inntekt og omstillingsstønad",
            Nynorsk to "Inntekt og omstillingsstønad",
            English to "Income and adjustment allowance",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden skal reduseres med 45 prosent av inntekten din som er over " +
                    "halvparten av grunnbeløpet i folketrygden (G). Stønaden blir redusert ut fra hva du oppgir " +
                    "som forventet inntekt for gjeldende år.  ",
            Nynorsk to "Omstillingsstønaden skal reduserast med 45 prosent av inntekta di som er over " +
                    "halvparten av grunnbeløpet i folketrygda (G). Stønaden blir redusert ut frå kva du oppgir " +
                    "som forventa inntekt for gjeldande år.",
            English to "The adjustment allowance are reduced by 45 percent of your income, if it is more " +
                    "than half of the basic amount in the National Insurance Scheme (G). The allowance will be " +
                    "reduced based on what you declare as anticipated income for the current year.",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.endretInntekt() {
    title2 {
        text(
            Bokmal to "Får du endret inntekt i løpet av året?",
            Nynorsk to "Får du endra inntekt i løpet av året?",
            English to "Will your income change during the year?",
        )
    }
    paragraph {
        text(
            Bokmal to "Om du melder fra om endring av inntekt i løpet av året, vil vi justere " +
                    "omstillingsstønaden fra måneden etter du har gitt beskjed. Inntekten din beregnes ut fra " +
                    "det du har tjent så langt i år, lagt sammen med det du forventer å tjene resten av året. " +
                    "Inntekt som er opptjent før mottak av omstillingsstønaden tas ikke med i beregningen.",
            Nynorsk to "Dersom du melder frå om endring av inntekt i løpet av året, vil vi justere " +
                    "omstillingsstønaden frå månaden etter du har gitt beskjed. Inntekta di blir rekna ut med " +
                    "utgangspunkt i det du har tent så langt i år, lagt saman med det du forventar å tene resten " +
                    "av året. Inntekt du hadde tent opp før du fekk omstillingsstønaden, blir ikkje teken med i " +
                    "utrekninga. ",
            English to "If you report a change in income during the year, we will adjust the adjustment " +
                    "allowance starting in the month after you reported the change. Your income is calculated based " +
                    "on what you earned so far this year, added together with what you expect to earn for the rest " +
                    "of the year. Income earned before receiving any adjustment allowance is not taken into " +
                    "account.",
        )
    }
    paragraph {
        text(
            Bokmal to "Utbetalingen for resten av året vil justeres utfra det du har fått utbetalt så langt " +
                    "det gjeldende året. Meld derfor fra snarest mulig for å få mest mulig riktig utbetalt " +
                    "omstillingsstønad, slik at etteroppgjøret blir så riktig som mulig. Du kan finne mer " +
                    "informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}.",
            Nynorsk to "Utbetalinga for resten av året vil bli justert ut frå det du har fått utbetalt så " +
                    "langt det gjeldande året. Meld difor frå snarast råd, slik at du får utbetalt " +
                    "omstillingsstønaden du har krav på, og etteroppgjeret blir mest mogleg rett. Du finn meir " +
                    "informasjon om etteroppgjer på ${Constants.OMS_ETTEROPPGJOER_URL}.",
            English to "The payment for the rest of the year will be adjusted based on what you have been " +
                    "paid so far in the current year. It is therefore important to notify us as soon as possible " +
                    "to receive the correct amount of adjustment allowance, so that any final settlement will " +
                    "be as correct as possible. You can find more information about final settlements " +
                    "online: ${Constants.OMS_ETTEROPPGJOER_URL}.",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvilkenInntektReduseresEtter() {
    title2 {
        text(
            Bokmal to "Hvilken inntekt skal omstillingsstønaden reduseres etter?",
            Nynorsk to "Etter kva inntekt skal omstillingsstønaden reduserast?",
            English to "What income forms the basis for reducing the adjustment allowance?",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden skal reduseres etter arbeidsinntekt og annen inntekt som er " +
                    "likestilt med arbeidsinntekt. Dette er blant annet:",
            Nynorsk to "Omstillingsstønaden skal reduserast etter arbeidsinntekt og anna inntekt som er " +
                    "likestilt med arbeidsinntekt. Dette er mellom anna",
            English to "The adjustment allowance shall be reduced according to income from employment and " +
                    "other income that is similar to employment income. These include:",
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekt fra alle arbeidsgivere, inkludert feriepenger",
                    Nynorsk to "arbeidsinntekt (inkludert feriepengar) frå alle arbeidsgivarar",
                    English to "earned income from all employers, including holiday pay",
                )
            }
            item {
                text(
                    Bokmal to "næringsinntekt, og inntekt fra salg av næringsvirksomhet",
                    Nynorsk to "næringsinntekt og inntekt frå sal av næringsverksemd",
                    English to "Business income and income from the sale of a business enterprise",
                )
            }
            item {
                text(
                    Bokmal to "styregodtgjørelse og andre godtgjørelser",
                    Nynorsk to "styregodtgjersle og andre godtgjersler",
                    English to "remuneration of board members and other remuneration",
                )
            }
            item {
                text(
                    Bokmal to "royalties",
                    Nynorsk to "royalties",
                    English to "royalties",
                )
            }
            item {
                text(
                    Bokmal to "dagpenger, sykepenger og arbeidsavklaringspenger",
                    Nynorsk to "dagpengar, sjukepengar og arbeidsavklaringspengar",
                    English to "unemployment benefits, sickness benefits and Work Assessment Allowance",
                )
            }
            item {
                text(
                    Bokmal to "svangerskapspenger og foreldrepenger",
                    Nynorsk to "svangerskapspengar og foreldrepengar",
                    English to "pregnancy benefits and parental benefits",
                )
            }
            item {
                text(
                    Bokmal to "omsorgsstønad",
                    Nynorsk to "omsorgsstønad",
                    English to "care benefits",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.hvordanMeldeEndringer(bosattUtland: Expression<Boolean>) {
    title2 {
        text(
            Bokmal to "Hvordan melder du fra om endringer?",
            Nynorsk to "Slik melder du frå om endringar",
            English to "How do I report changes",
        )
    }

    paragraph {
        text(
            Bokmal to "Du kan gi beskjed om endringer på følgende måter:",
            Nynorsk to "Du kan melde frå om endringar på følgjande måtar:",
            English to "You can report changes in the following ways:"
        )

        list {
            item {
                text(
                    Bokmal to "benytte endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}",
                    Nynorsk to "bruk endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}",
                    English to "use the change form on ${Constants.OMS_MELD_INN_ENDRING_URL}"
                )
            }
            item {
                text(
                    Bokmal to "ettersende dokumentasjon angående omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                    Nynorsk to "ettersend dokumentasjon angåande omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                    English to "send more documentation regarding adjustment allowance by going to: ${Constants.ETTERSENDE_OMS_URL}"
                )
            }
        }

        showIf(bosattUtland) {
            text(
                Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til ${Constants.Utland.POSTADRESSE}.",
                Nynorsk to "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til ${Constants.Utland.POSTADRESSE}.",
                English to "Please send documentation as normal post if you do not use BankID or another login option. Send to ${Constants.Utland.POSTADRESSE}."
            )
        }.orShow {
            text(
                Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til ${Constants.POSTADRESSE}.",
                Nynorsk to "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til ${Constants.POSTADRESSE}.",
                English to "Please send documentation as normal post if you do not use BankID or another login option. Send to ${Constants.POSTADRESSE}."
            )
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.utbetalingTilKontonummer() {
    title2 {
        text(
            Bokmal to "Utbetaling til kontonummer",
            Nynorsk to "Utbetaling til kontonummer",
            English to "Payments go to your bank account",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan bare ha ett kontonummer registrert hos Nav. Du kan endre kontonummeret i " +
                    "«Personopplysninger» ved å logge på nav.no. Du kan også sende endring per post. " +
                    "Du finner skjema og riktig adresse på " + Constants.ENDRING_KONTONUMMER_URL + ".",
            Nynorsk to "Du kan berre ha eitt kontonummer registrert hos Nav. Du kan endre kontonummer under " +
                    "«Personopplysningar» ved å logge på nav.no. Du kan også sende endring per post. Du finn skjema " +
                    "og rett adresse på " + Constants.ENDRING_KONTONUMMER_URL + ".",
            English to "You can only have one account number registered with Nav. You can change your account " +
                    "number online (in Personal Data) by logging in to nav.no. You can also report changes by " +
                    "conventional mail. You will find the form and the correct address online: " +
                    Constants.ENDRING_KONTONUMMER_URL + ".",
        )
    }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, InformasjonOmOmstillingsstoenadData>.skatt() {
    title2 {
        text(
            Bokmal to "Skatt",
            Nynorsk to "Skatt",
            English to "Tax",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden er skattepliktig. Du trenger ikke levere skattekortet til Nav " +
                    "fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten.",
            Nynorsk to "Omstillingsstønaden er skattepliktig. Du treng ikkje levere skattekortet til Nav, " +
                    "då skatteopplysningane dine blir sende elektronisk frå Skatteetaten.",
            English to "Adjustment allowance are taxable. You do not need to submit your tax deduction " +
                    "card to Nav because your tax information is sent to Nav electronically from the " +
                    "Norwegian Tax Administration.",
        )
    }
    paragraph {
        text(
            Bokmal to "Endring av skattekort gjøres enklest på Skatteetatens nettsider www.skatteetaten.no. " +
                    "Har du spørsmål kan du ringe Skatteetaten på telefon " + Constants.KONTAKTTELEFON_SKATT + ". " +
                    "Fra utlandet ringer du " + Constants.Utland.KONTAKTTELEFON_SKATT + ".",
            Nynorsk to "Skattekortet endrar du enklast frå nettsidene til Skatteetaten, www.skatteetaten.no. " +
                    "Viss du har spørsmål, kan du ringje Skatteetaten på telefon " + Constants.KONTAKTTELEFON_SKATT + ". " +
                    "Frå utlandet ringjer du " + Constants.Utland.KONTAKTTELEFON_SKATT + ".",
            English to "The easiest way to change your tax deduction card is done on the Tax Administration's " +
                    "website: www.skatteetaten.no. If you have any questions, please call the Tax Administration " +
                    "by phone: " + Constants.KONTAKTTELEFON_SKATT + ". For calls from abroad: " +
                    Constants.Utland.KONTAKTTELEFON_SKATT + ".",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden er pensjonsgivende inntekt. Den gir ikke opptjening av feriepenger.",
            Nynorsk to "Omstillingsstønaden er pensjonsgivande inntekt. Han gir ikkje opptening av feriepengar.",
            English to "Adjustment allowance are considered pensionable income. They do not earn you holiday pay.",
        )
    }
}
