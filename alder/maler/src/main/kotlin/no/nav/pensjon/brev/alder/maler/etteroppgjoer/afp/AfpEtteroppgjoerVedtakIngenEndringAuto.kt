package no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp

import no.nav.pensjon.brev.alder.maler.felles.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.afpEtteroppgjoer
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.afpGrunnlag
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringAutoDtoSelectors.afpEtteroppgjoerVedtak
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerVedtakIngenEndringAutoDtoSelectors.innsenderEnhetNavn
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object AfpEtteroppgjoerVedtakIngenEndringAuto : AutobrevTemplate<AfpEtteroppgjoerVedtakIngenEndringAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_VEDTAK_INGEN_ENDRING_AUTO

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
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + afpEtteroppgjoerVedtak.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + afpEtteroppgjoerVedtak.oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(
                AfpEtteroppgjoerVedtakIngenEndringFelles(
                    oppgjoersAar = afpEtteroppgjoerVedtak.oppgjoersAar,
                    innsenderEnhet = innsenderEnhetNavn,
                    afpEtteroppgjoer = afpEtteroppgjoerVedtak.afpEtteroppgjoer,
                    afpGrunnlag = afpEtteroppgjoerVedtak.afpGrunnlag
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
    }
}