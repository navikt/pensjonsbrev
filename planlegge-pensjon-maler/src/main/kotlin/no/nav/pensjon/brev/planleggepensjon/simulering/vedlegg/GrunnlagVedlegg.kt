package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.sivilstatus
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringsinformasjonSelectors.utenlandsperioder
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiode
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.arbeidetUtenlands
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.fom
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.landkode
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringUtenlandsperiodeSelectors.tom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.brev.InternKonstruktoer

@OptIn(InternKonstruktoer::class)
private val sivilstatusValueSelector = SimpleSelector<Sivilstatus, String>(
    className = "no.nav.pensjon.brev.planleggepensjon.simulering.Sivilstatus",
    propertyName = "value",
    propertyType = "String",
    selector = Sivilstatus::value
)

private val Expression<Sivilstatus>.value: Expression<String>
    get() = select(sivilstatusValueSelector)

@TemplateModelHelpers
val grunnlagVedlegg = createAttachment<LangBokmal, Simuleringsinformasjon>(
    title = {
        text(bokmal { +"Grunnlag for beregningen" })
    },
    includeSakspart = false,
) {
    title1 {
        text(bokmal { +"Opphold utenfor Norge" })
    }
    paragraph {
        table(header = {
            column {
                text(bokmal { +"Land" })
            }
            column {
                text(bokmal { +"Periode" })
            }
            column {
                text(bokmal { +"Jobbet" })
            }
        }) {
            forEach(utenlandsperioder) { periode ->
                row {
                    cell {
                        text(bokmal { +periode.landkode })
                    }
                    cell {
                        ifNotNull(periode.tom) { tomDato ->
                            text(bokmal { +periode.fom.format(short = true) + "–" + tomDato.format(short = true) })
                        }.orShow {
                            text(bokmal { +periode.fom.format(short = true) + " (Varig opphold)" })
                        }
                    }
                    cell {
                        text(bokmal { +ifElse(periode.arbeidetUtenlands, "Ja", "") })
                    }
                }
            }
        }
    }

    title1 {
        text(bokmal { +"Sivilstatus: " + sivilstatus.value })
    }
    paragraph {
        text(
            bokmal {
                +"Hvis du bor sammen med noen kan inntekten til den du bor med ha betydning for hva du får i alderspensjon. Når du mottar alderspensjon må du derfor melde fra til Nav ved endring i sivilstand."
            },
        )
    }
}
