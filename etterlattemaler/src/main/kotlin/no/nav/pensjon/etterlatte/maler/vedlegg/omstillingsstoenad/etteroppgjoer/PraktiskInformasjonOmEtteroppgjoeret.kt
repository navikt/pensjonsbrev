package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.BrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.KronerText
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.faktiskInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.grunnlag
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggDataSelectors.utbetalingData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.afp
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.loennsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.naeringsinntekt
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTOSelectors.utlandsinntekt
import java.time.YearMonth

@TemplateModelHelpers
val praktiskInformasjonOmEtteroppgjoeret: AttachmentTemplate<LangBokmalNynorskEnglish, *> =
    createAttachment(
        title =
        newText(
            Bokmal to "Praktisk informasjon om etteroppgjøret",
            Nynorsk to "",
            English to "",
        ),
        includeSakspart = false,
    ) {

    }

