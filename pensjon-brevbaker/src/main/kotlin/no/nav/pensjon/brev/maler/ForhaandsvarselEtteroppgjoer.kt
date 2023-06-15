package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.*
import no.nav.pensjon.brev.maler.vedlegg.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*

@TemplateModelHelpers
object ForhaandsvarselEtteroppgjoer : AutobrevTemplate<ForhaandsvarselEtteroppgjoerDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_FORHAANDSVARSEL_EO_AUTO

    override val template: LetterTemplate<LanguageSupport.Triple<Bokmal, Nynorsk, English>, ForhaandsvarselEtteroppgjoerDto>
        get() = createTemplate(
            name = ForhaandsvarselEtteroppgjoer.kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                displayTitle = "Varsel - etteroppgjør av uføretrygd",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            )
        ) {

            title {
                showIf() {
                    textExpr(
                        Bokmal to "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for <PeriodeFom>".expr(),
                        Nynorsk to "Nytt førehandsvarsel om etteroppgjer av uføretrygd for <PeriodeFom>".expr(),
                        English to "New advance notice of settlement of disability benefit for <PeriodeFom>".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Forhåndsvarsel om etteroppgjør av uføretrygd for <PeriodeFom>".expr(),
                        Nynorsk to "Førehandsvarsel om etteroppgjer av uføretrygd for <PeriodeFom>".expr(),
                        English to "Advance notice of settlement of disability benefit for <PeriodeFom>".expr()
                    )
                }
            }
        }
}
