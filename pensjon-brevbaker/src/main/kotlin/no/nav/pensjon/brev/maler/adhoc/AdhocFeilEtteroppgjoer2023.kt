package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocFeilEtteroppgjoer2023 : AutobrevTemplate<EmptyBrevdata> {
    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.PE_ADHOC_2024_FEIL_ETTEROPPGJOER_2023
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Feil i etteroppgjørsbrev fra NAV",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Feil i beregningen av etteroppgjør uføretrygd for 2023",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi viser til brev datert 22. oktober om etteroppgjør av uføretrygd for året 2023.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Resultatet av etteroppgjøret ditt har dessverre blitt feil. Feilen gjelder barnetillegget. Inntekten til den andre forelderen som er oppgitt i brevet, har ikke blitt tatt med i beregningen av barnetillegget.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi beklager dette.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan se bort fra brevet. Vi vil sende deg brev med nytt resultat så snart feilen er rettet.",
                )
            }
        }
    }
}