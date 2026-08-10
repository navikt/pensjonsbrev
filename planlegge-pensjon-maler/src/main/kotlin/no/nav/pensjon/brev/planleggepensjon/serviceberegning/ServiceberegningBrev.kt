package no.nav.pensjon.brev.planleggepensjon.serviceberegning

import no.nav.pensjon.brev.api.model.IBrevkategori
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.planleggepensjon.Brevkategori
import no.nav.pensjon.brev.planleggepensjon.FeatureToggles
import no.nav.pensjon.brev.planleggepensjon.PlanleggePensjonBrevkoder
import no.nav.pensjon.brev.planleggepensjon.redigerbar
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningBrevDto.saksbehandlerValg
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDto.afp
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDto.forventetFremtidigInntekt
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDto.uttaksalder
import no.nav.pensjon.brev.planleggepensjon.serviceberegning.selectors.serviceberegningDto.uttaksdato
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.aar
import no.nav.pensjon.brev.planleggepensjon.simulering.selectors.alder.maaneder
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetTabellRedigerbar
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.AfpOffentligTidsbegrensetOpptjeningTabell
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
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
    override val modelSpecification: TemplateModelSpecification = TemplateModelSpecification(emptyMap(), null)

    override val template: LetterTemplate<*, ServiceberegningBrevDto> = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Serviceberegning",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(bokmal { +"Serviceberegning AFP for " + felles.bruker.fornavn.redigerbar() })

            ifNotNull(felles.bruker.mellomnavn) { mellomnavn ->
                text(bokmal { +" " + mellomnavn.redigerbar() })
            }

            text(bokmal { +" " + felles.bruker.etternavn.redigerbar() })
        }

        outline {
            paragraph {
                text(bokmal { +("Bruker har ingen ytelser som ikke kan kombineres med AFP.") })
            }
            paragraph {
                text(bokmal { +("Bruker har hatt utbetalt alderspensjon frem til " + fritekst("DD.MM.ÅÅÅÅ") + ".") })
            }
            paragraph {
                text(bokmal { +("Bruker har " + fritekst("XX") + " % uføretrygd fra folketrygden.") })
            }
            paragraph {
                text(bokmal { +("Bruker har arbeidsavklaringspenger (AAP) til utbetaling per i dag.") })
            }
            paragraph {
                text(
                    bokmal {
                        +("Bruker mottar eller søker om sykepenger. Nav arbeid og ytelser er informert om at bruker søker AFP.")
                    },
                )
            }

            title1 {
                text(bokmal { +"Månedlig pensjon før skatt ved " + saksbehandlerValg.uttaksalder.aar.format().redigerbar() + " år" })
                showIf(saksbehandlerValg.uttaksalder.maaneder greaterThan 1) {
                    text(bokmal { +" og " + saksbehandlerValg.uttaksalder.maaneder.format().redigerbar() + " måneder" })
                }.orShowIf(saksbehandlerValg.uttaksalder.maaneder greaterThan 0) {
                    text(bokmal { +" og 1 måned" })
                }
                text(bokmal { +" (" + saksbehandlerValg.uttaksdato.redigerbar() + ")" })
            }
            includePhrase(AfpOffentligTidsbegrensetTabellRedigerbar(saksbehandlerValg.afp, sumLabel = "Sum"))

            title1 {
                text(bokmal { +"Opptjeningsgrunnlag i folketrygden" })
            }
            ifNotNull(saksbehandlerValg.forventetFremtidigInntekt) { forventetFremtidigInntekt ->
                paragraph {
                    text(bokmal { +"Forventet fremtidig inntekt: " + forventetFremtidigInntekt.format().redigerbar() + "." })
                }
            }
            includePhrase(AfpOffentligTidsbegrensetOpptjeningTabell(saksbehandlerValg.afp))
        }
    }
}
