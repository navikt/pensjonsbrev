package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocFeilEtteroppgjoer2023 : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_ADHOC_2024_FEIL_ETTEROPPGJOER_2023
    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Feil i etteroppgjørsbrev fra Nav",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Feil i beregningen av etteroppgjør uføretrygd for 2023" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi viser til brev datert 22. oktober om etteroppgjør av uføretrygd for året 2023." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Resultatet av etteroppgjøret ditt har dessverre blitt feil. Feilen gjelder barnetillegget. Inntekten til den andre forelderen som er oppgitt i brevet, har ikke blitt tatt med i beregningen av barnetillegget." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi beklager dette." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan se bort fra brevet. Vi vil sende deg brev med nytt resultat så snart feilen er rettet." },
                )
            }
        }
    }
}