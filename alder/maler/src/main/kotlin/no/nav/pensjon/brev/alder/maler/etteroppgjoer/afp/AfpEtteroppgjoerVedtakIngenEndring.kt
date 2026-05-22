package no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.felles.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.afpEtteroppgjoer
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.afpGrunnlag
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AfpEtteroppgjoerVedtakIngenEndring : RedigerbarTemplate<AfpEtteroppgjoerVedtakIngenEndringDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_VEDTAK_INGEN_ENDRING

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring - AFP etteroppgjør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(
                AfpEtteroppgjoerVedtakIngenEndringFelles(
                    oppgjoersAar = pesysData.oppgjoersAar,
                    innsenderEnhet = "Nav".expr(),
                    afpEtteroppgjoer = pesysData.afpEtteroppgjoer,
                    afpGrunnlag = pesysData.afpGrunnlag
                )
            )
        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper = setOf(Sakstype.AFP)
}