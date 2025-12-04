package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallInnholdDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallInnholdDTOSelectors.forhaandsvarselSendtDato
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerVedtakRedigerbartUtfallInnholdDTOSelectors.mottattSvarDato
import java.time.LocalDate

class EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO(
    val etteroppgjoersAar: Int,
    val forhaandsvarselSendtDato: LocalDate,
    val mottattSvarDato: LocalDate?
)

class EtteroppgjoerVedtakRedigerbartUtfallBrevDTO(
    val data: EtteroppgjoerVedtakRedigerbartUtfallInnholdDTO
): RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object EtteroppgjoerVedtakRedigerbartUtfall:
    EtterlatteTemplate<EtteroppgjoerVedtakRedigerbartUtfallBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_VEDTAK_UTFALL

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Etteroppgjør Innhold",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {

            ifNotNull(data.mottattSvarDato){ mottattSvarDato ->
                paragraph {
                    text(
                        bokmal { +"Vi viser til vårt forhåndsvarsel av " + data.forhaandsvarselSendtDato.format() + " om etteroppgjør av din omstillingsstønad og din tilbakemelding vi mottok " + mottattSvarDato.format() + ". Omstillingsstønaden din er endret for " + data.etteroppgjoersAar.format() + "." },
                        nynorsk { +"Vi viser til førehandsvarselet av " + data.forhaandsvarselSendtDato.format() + " om etteroppgjer av din omstillingsstønad og din tilbakemelding som vi mottok " + mottattSvarDato.format() + ". Omstillingsstønaden din er endra for " + data.etteroppgjoersAar.format() + "." },
                        english { +"We refer to our advance notice dated " + data.forhaandsvarselSendtDato.format() + " regarding the final settlement of your adjustment allowance and your response, which we received on " + mottattSvarDato.format() + ". Your adjustment allowance will change for " + data.etteroppgjoersAar.format() + "."}
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Vi viser til vårt forhåndsvarsel av " + data.forhaandsvarselSendtDato.format() + " om etteroppgjør av din omstillingsstønad. Omstillingsstønaden din er endret for " + data.etteroppgjoersAar.format() + "."},
                        nynorsk { +"Vi viser til førehandsvarselet av " + data.forhaandsvarselSendtDato.format() + " om etteroppgjer av din omstillingsstønad. Omstillingsstønaden din er endra for " + data.etteroppgjoersAar.format() + "."},
                        english { +"We refer to our advance notice dated " + data.forhaandsvarselSendtDato.format() + " regarding the final settlement of your adjustment allowance. Your adjustment allowance will change for " + data.etteroppgjoersAar.format() + "."}
                    )
                }
            }


        }
    }
}