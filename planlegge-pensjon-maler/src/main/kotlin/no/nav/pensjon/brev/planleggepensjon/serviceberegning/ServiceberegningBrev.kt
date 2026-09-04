package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.IBrevkategori
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.planleggepensjon.Brevkategori
import no.nav.pensjon.brev.planleggepensjon.FeatureToggles
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonBrevkoder
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningBrevDto.pesysData
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDtoData.afp
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDtoData.forventetFremtidigInntekt
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDtoData.uttaksalder
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDtoData.uttaksdato
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.maaneder
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetOpptjeningTabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetTabellRedigerbar
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.etternavn
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.fornavn
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.bruker.mellomnavn

@TemplateModelHelpers
object ServiceberegningBrev : RedigerbarTemplate<ServiceberegningBrevDto> {
    override val kategori: IBrevkategori = Brevkategori.SERVICEBEREGNING_SIMULERINGSBREV
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<ISakstype> = emptySet()
    override val kode: Brevkode.Redigerbart = PlanleggePensjonBrevkoder.Redigerbar.SERVICEBEREGNING_SIMULERINGSBREV
    override val featureToggle = FeatureToggles.apSimulering.toggle

    override val template: LetterTemplate<*, ServiceberegningBrevDto> = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Serviceberegning AFP",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        val ingenYtelser = saksbehandlervalg("ingenYtelser", "Ingen ytelser").bool()
        val vedtakOmAlderspensjon = saksbehandlervalg("vedtakOmAlderspensjon", "Vedtak om alderspensjon").bool()
        val vedtakOmUfoeretrygd = saksbehandlervalg("vedtakOmUfoeretrygd", "Vedtak om uføretrygd").bool()
        val aapUtbetales = saksbehandlervalg("aapUtbetales", "AAP utbetales").bool()
        val mottarSykepenger = saksbehandlervalg("mottarSoekerOmSykepenger", "Mottar / søker om sykepenger").bool()

        title {
            text(bokmal { +"Serviceberegning AFP for " + redigerbarData(felles.bruker.fornavn) })

            ifNotNull(felles.bruker.mellomnavn) { mellomnavn ->
                text(bokmal { +" " + redigerbarData(mellomnavn) })
            }

            text(bokmal { +" " + redigerbarData(felles.bruker.etternavn) })
        }

        outline {
            showIf(ingenYtelser) {
                paragraph { text(bokmal { +"Bruker har ingen ytelser som ikke kan kombineres med AFP." }) }
            }
            showIf(vedtakOmAlderspensjon) {
                paragraph { text(bokmal { +"Bruker har hatt utbetalt alderspensjon frem til " + fritekst("DD.MM.ÅÅÅÅ") + "." }) }
            }
            showIf(vedtakOmUfoeretrygd) {
                paragraph { text(bokmal { +"Bruker har " + fritekst("XX") + " % uføretrygd fra folketrygden." }) }
            }
            showIf(aapUtbetales) {
                paragraph { text(bokmal { +"Bruker har arbeidsavklaringspenger (AAP) til utbetaling per i dag." }) }
            }
            showIf(mottarSykepenger) {
                paragraph {
                    text(
                        bokmal {
                            +"Bruker mottar eller søker om sykepenger. Nav arbeid og ytelser er informert om at bruker søker AFP."
                        })
                }
            }


            title1 {
                text(bokmal { +"Månedlig pensjon før skatt ved " + redigerbarData(pesysData.uttaksalder.aar.format()) + " år" })
                showIf(pesysData.uttaksalder.maaneder greaterThan 1) {
                    text(bokmal { +" og " + redigerbarData(pesysData.uttaksalder.maaneder.format()) + " måneder" })
                }.orShowIf(pesysData.uttaksalder.maaneder greaterThan 0) {
                    text(bokmal { +" og 1 måned" })
                }
                text(bokmal { +" (" + redigerbarData(pesysData.uttaksdato) + ")" })
            }
            includePhrase(AfpOffentligTidsbegrensetTabellRedigerbar(pesysData.afp))

            title1 {
                text(bokmal { +"Opptjeningsgrunnlag i folketrygden" })
            }

            paragraph {
                text(bokmal { +"Forventet fremtidig inntekt: " + redigerbarData(pesysData.forventetFremtidigInntekt.format()) + "." })
            }

            includePhrase(AfpOffentligTidsbegrensetOpptjeningTabell(pesysData.afp))
        }
    }
}
