package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
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
        name = kode.name,
        letterDataType = EtteroppgjoerVedtakRedigerbartUtfallBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Etteroppgjør Innhold",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {

            ifNotNull(data.mottattSvarDato){ mottattSvarDato ->
                paragraph {
                    textExpr(
                        Language.Bokmal to "Vi viser til forhåndsvarselet vårt om etteroppgjør på omstillingsstønaden din av ".expr() + data.forhaandsvarselSendtDato.format() + "og din tilbakemelding som vi mottok ".expr() + mottattSvarDato.format() + ". Omstillingsstønaden din er endret for " + data.etteroppgjoersAar.format(),
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr(),
                    )
                }
            }.orShow {
                paragraph {
                    textExpr(
                        Language.Bokmal to "Vi viser til forhåndsvarselet vårt om etteroppgjør på omstillingsstønaden din av ".expr() + data.forhaandsvarselSendtDato.format() + ". Omstillingsstønaden din er endret for " + data.etteroppgjoersAar.format(),
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr(),
                    )
                }
            }


        }
    }
}