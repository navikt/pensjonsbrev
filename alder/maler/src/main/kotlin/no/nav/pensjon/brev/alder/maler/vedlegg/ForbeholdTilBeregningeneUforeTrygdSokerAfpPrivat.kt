package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val forbeholdTilBeregningeneUforeTrygdSokerAfpPrivat = createAttachment<LangBokmalNynorsk, EmptyVedleggData>(
    title = {
        text(
            bokmal { +"Forbehold til beregningene" },
            nynorsk { +"Atterhald til utrekningane" },
        )
    },
    includeSakspart = false,
) {

    title2 {
        text(
        bokmal { +"Forbehold til beregningene" },
        nynorsk { +"Atterhald til utrekningane" }
        )
    }
    paragraph { text(
        bokmal { +"Pensjonen din er beregnet på grunnlag av de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt selv." },
        nynorsk { +"Pensjonen din er rekna ut på grunnlag av dei opplysningane vi har om deg, i tillegg til dei opplysningane du sjølv har oppgitt." }
    ) }

    paragraph { text(
        bokmal { +"Dette er en beregning som kun er ment som grunnlag for å kunne ta et valg mellom uføretrygd og AFP. Beregningen er ikke juridisk bindende." },
        nynorsk { +"Dette er ei utrekning som berre er meint som grunnlag for å kunne ta et val mellom uføretrygd og AFP. Utrekninga er ikkje juridisk bindande." }
    ) }

    paragraph { text(
        bokmal { +"Vi har ikke vurdert om du fyller inngangsvilkårene for å få AFP i privat sektor. Denne vurderingen blir gjort av Fellesordningen for AFP." },
        nynorsk { +"Vi har ikkje vurdert om du fyller inngangsvilkåra for å få AFP i privat sektor. Denne vurderinga blir gjort av Fellesordningen for AFP." }
    ) }

    paragraph { text(
        bokmal { +"Vi tar ikke hensyn til opptjening fra eventuell inntekt og uføretrygd som du vil ha samtidig med alderspensjon." },
        nynorsk { +"Vi tar ikkje omsyn til opptening frå eventuell inntekt og uføretrygd som du vil ha samstundes med alderspensjon." }
    ) }

    paragraph { text(
        bokmal { +"Beløpene er beregnet i dagens kroneverdi. Vi har benyttet satser for minste pensjonsnivå og garantipensjonsnivå når vi har beregnet pensjonen din." },
        nynorsk { +"Beløpa er utrekna i dagens kroneverdi. Vi har nytta satsar for minste pensjonsnivå og garantipensjonsnivå når vi har rekna ut pensjonen din." }
    ) }

    paragraph { text(
        bokmal { +"Alderspensjon reguleres (økes) med gjennomsnitt av lønns- og prisvekst. AFP i privat sektor reguleres med lønnsvekst fratrukket 0,75%. Endringene fram i tid, som skyldes forskjellig regulering, er ikke tatt hensyn til i beregningen." },
        nynorsk { +"Alderspensjon blir regulert opp med gjennomsnitt av lønns- og prisvekst. AFP i privat sektor blir regulert opp med lønnsvekst minus 0,75%. Endringane fram i tid, som skuldast forskjellig regulering, er det ikkje tatt omsyn til i utrekninga." }
    ) }
    paragraph { text(
        bokmal { +"I beregningen legger Nav til grunn at du bor i Norge. Du kan ha rett til pensjon fra andre land du har arbeidet eller bodd i, slik pensjon er ikke med i beregningen. Vi har heller ikke tatt med tjenestepensjon ut over AFP i denne beregninge" },
        nynorsk { +"I utrekninga legg Nav til grunn at du bur i Noreg. Du kan ha rett til pensjon frå andre land du har arbeida eller budd i, slik pensjon er ikkje med i utrekninga. Vi har heller ikkje tatt med tenestepensjon ut over AFP i denne utrekninga." }
    ) }
    paragraph { text(
        bokmal { +"Eventuelle rettigheter til omsorgsopptjening før 1992 som ikke er innvilget per i dag er ikke tatt med i beregningen." },
        nynorsk { +"Eventuelle rettigheiter til omsorgsopptening før 1992 som ikkje er innvilga per i dag, er ikkje tatt med i utrekninga" }
    ) }



}