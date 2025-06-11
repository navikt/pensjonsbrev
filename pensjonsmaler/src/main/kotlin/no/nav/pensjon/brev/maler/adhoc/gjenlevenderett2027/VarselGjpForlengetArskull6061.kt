package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.gjennomsnittInntektG
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022G
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023G
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.DineInntekterTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig2GTabell
import no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027.Tabeller.Gjennomsnittlig3GTabell
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselGjpForlengetArskull6061 : AutobrevTemplate<Gjenlevenderett2027Dto> {

    override val kode = Pesysbrevkoder.AutoBrev.GJP_VARSEL_FORLENGELSE_60_61

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = "GJP_VARSEL_FORLENGELSE_60_61",
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel - Gjenlevendepensjonen din kan bli forlenget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Forhåndsvarsel – Gjenlevendepensjonen din kan bli forlenget",
                Nynorsk to "Førehandsvarsel – Gjenlevandepensjonen din kan bli forlenga "
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024.",
                    Nynorsk to "Vi viser til tidlegare informasjon om at Stortinget har vedteke endringar i reglane i folketrygdlova som gjeld ytingar til etterlatne. Endringa gjeld frå 1. januar 2024. "
                )

            }

            title1 {
                text(
                    Bokmal to "Hva betyr de nye reglene for deg?",
                    Nynorsk to "Kva betyr dei nye reglane for deg? ",
                )

            }

            paragraph {
                text(
                    Bokmal to "Du beholder retten til gjenlevendepensjon. Du får den i tre år fra 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026.",
                    Nynorsk to "Du beheld retten til gjenlevandepensjon. Du får denne i tre år frå 1. januar 2024. Det kan bety at siste utbetaling av pensjonen din blir i desember 2026. ",
                )

            }

            paragraph {
                text(
                    Bokmal to "Hvis du har hatt lav eller ingen pensjonsgivende inntekt de siste fem årene før 2024, kan du beholde gjenlevendepensjonen din til du blir 67 år. ",
                    Nynorsk to "Dersom du har vore utan eller hatt låg pensjonsgivande inntekt dei siste fem åra før 2024, kan du behalde gjenlevandepensjonen din til du blir 67 år. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva er inntektsgrensene?",
                    Nynorsk to "Kva er inntektsgrensene?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023:",
                    Nynorsk to "Pensjonsgivande inntekt må ha vore under tre gongar gjennomsnittleg grunnbeløp i folketrygda (G) i både 2022 og 2023: ",
                )
            }

            includePhrase(Gjennomsnittlig3GTabell)

            paragraph {
                text(
                    Bokmal to "I tillegg må inntekten din i 2019–2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil siat inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere.",
                    Nynorsk to "I tillegg må inntekta di i 2019–2023 ha vore under to gongar grunnbeløpet i folketrygda (G) i gjennomsnitt i desse fem åra. Det vil seie at inntekta kan overstige to gongar grunnbeløpet i eitt enkelt år, så lenge gjennomsnittet av dei fem åra er lågare. ",
                )
            }

            includePhrase(Gjennomsnittlig2GTabell)

            title1 {
                text(
                    Bokmal to "Hvilke opplysninger har vi om deg?",
                    Nynorsk to "Kva opplysningar har vi om deg?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Det er din pensjonsgivende inntekt i årene 2019–2023 som avgjør om du kan beholde gjenlevendepensjonen til du fyller 67 år.",
                    Nynorsk to "Det er den pensjonsgivande inntekta di i åra 2019–2023 som avgjer om du kan behalde gjenlevandepensjonen til du fyller 67 år. ",

                    )
            }

            paragraph {
                text(
                    Bokmal to "Ifølge registeropplysninger vi har om deg fra Skatteetaten, har du i årene 2019–2023 hatt følgende inntekter:",
                    Nynorsk to "Ifølgje registeropplysningar vi har om deg frå Skatteetaten, har du i åra 2019–2023 hatt følgjande inntekter:",

                    )
            }

            includePhrase(DineInntekterTabell(inntekt2019, inntekt2020, inntekt2021, inntekt2022, inntekt2023, gjennomsnittInntektG, inntekt2019G, inntekt2020G, inntekt2021G, inntekt2022G, inntekt2023G))

            paragraph {
                text(
                    Bokmal to "Din inntekt har ifølge våre opplysninger vært lavere enn inntektsgrensene. Dersom det er korrekt, beholder du gjenlevendepensjonen din til og med måneden du fyller 67 år. ",
                    Nynorsk to "Inntekta di har ifølgje opplysningane våre vore lågare enn inntektsgrensene. Dersom det er korrekt, beheld du gjenlevandepensjonen din til og med månaden du fyller 67 år. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Hva må du gjøre?",
                    Nynorsk to "Kva må du gjere?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene i dette brevet er korrekt, trenger du ikke å gjøre noe.",
                    Nynorsk to "Dersom du meiner at opplysningane i dette brevet er korrekte, treng du ikkje å gjere noko.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom du mener at opplysningene om inntekten din i dette brevet er feil, må du gi oss en tilbakemelding innen fire uker fra du mottok dette forhåndsvarselet. Du må da sende oss bekreftelse på at skattemeldingen din er endret. ",
                    Nynorsk to "Dersom du meiner at opplysningane om inntekta di i dette brevet er feil, må du gi oss tilbakemelding innan fire veker frå du fekk dette førehandsvarselet. Du må då sende oss stadfesting på at skattemeldinga di er endra. ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Vi vil da vurdere de nye opplysningene før vi gjør vedtak i saken din. ",
                    Nynorsk to "Vi vil då vurdere dei nye opplysningane før vi gjer vedtak i saka di. ",
                )
            }


            title1 {
                text(
                    Bokmal to "Meld fra om endringer ",
                    Nynorsk to "Meld frå om endringar ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav.",
                    Nynorsk to "Dersom du planlegg å flytte til eit anna land eller du får endra inntekt, familiesituasjon eller jobbsituasjon, kan det påverke gjenlevandepensjonen din. I slike tilfelle må du difor straks melde frå til Nav. ",
                )
            }


            title1 {
                text(
                    Bokmal to "Har du spørsmål? ",
                    Nynorsk to "Har du spørsmål? ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/gjenlevendepensjon. ",
                    Nynorsk to "Du finn meir informasjon på nav.no/gjenlevendepensjon. ",
                )
            }


            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss.",
                    Nynorsk to "Du kan skrive til eller chatte med oss på nav.no/kontakt. ",
                )
            }


            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00. ",
                    Nynorsk to "Dersom du ikkje finn svar på nav.no, kan du ringje oss på telefon 55 55 33 34, kvardagar 09.00–15.00. ",
                )
            }
        }
    }
}