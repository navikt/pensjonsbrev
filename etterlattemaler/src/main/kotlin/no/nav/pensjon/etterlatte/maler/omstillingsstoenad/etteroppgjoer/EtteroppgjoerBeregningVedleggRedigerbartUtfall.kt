package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
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
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggInnholdDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTOSelectors.data

data class EtteroppgjoerBeregningVedleggInnholdDTO(
    val etteroppgjoersAar: Int,
)
data class EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO(
    val data: EtteroppgjoerBeregningVedleggInnholdDTO
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object EtteroppgjoerBeregningVedleggRedigerbartUtfall : EtterlatteTemplate<EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL_BEREGNINGVEDLEGG_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerBeregningVedleggRedigerbartUtfallBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK, // TODO: ?
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            paragraph {

                textExpr(
                    Bokmal to "I tabllen over har vi registrert beløp som vi mener ikke skal være med i årsinntekten din for ".expr() + data.etteroppgjoersAar.format() +". Du må gi oss beskjed hvis dette er feil og sende dokumentasjon hvis du har andre inntekter som ikke skal være med i inntekt for de månedene omstillingsstønaden har vært innvilget i "+data.etteroppgjoersAar.format()+". ",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }
    }


}