package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDtoSelectors.kap19
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDtoSelectors.uforeTrygdTil_ATT
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val forbeholdTilBeregningeneAfpTIlUforeTrygd =
    createAttachment<LangBokmalNynorsk, AFpPrivatSokerUforeTrygdVedleggDto>(
        title = {
            text(
                bokmal { +"Forbehold" },
                nynorsk { +"Atterhald" },
            )
        },
        includeSakspart = false,
    ) {

        title2 {
            text(
                bokmal { +"Forbehold" },
                nynorsk { +"Atterhald" }
            )
        }
        paragraph {
            text(
                bokmal { +"Pensjonen din er beregnet på grunnlag av de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt selv." },
                nynorsk { +"Pensjonen din er rekna ut på grunnlag av dei opplysningane vi har om deg, i tillegg til dei opplysningane du sjølv har oppgitt." }
            )
        }

        paragraph {
            text(
                bokmal { +"Dette er en beregning som kun er ment som grunnlag for å kunne ta et valg mellom uføretrygd og AFP. Beregningen er ikke juridisk bindende." },
                nynorsk { +"Dette er ei utrekning som berre er meint som grunnlag for å kunne ta et val mellom uføretrygd og AFP. Utrekninga er ikkje juridisk bindande." }
            )
        }

        paragraph {
            text(
                bokmal { +"Vi tar ikke hensyn til opptjening fra eventuell inntekt som du vil ha samtidig med alderspensjon eller uføretrygd." },
                nynorsk { +"Vi tar ikkje omsyn til opptening frå eventuell inntekt som du vil ha samstundes med alderspensjon eller uføretrygd." }
            )
        }
        showIf(kap19) {
            paragraph {
                text(
                    bokmal { +"Innvilget uføretrygd gir opptjening til alderspensjon etter kapittel 19 i Folketrygdloven, og er bare medregnet i tilfelle den får stor betydning for beregningen. I denne saken har vi sett bort fra uføreopptjeningen fordi den får liten eller ingen betydning for alderspensjonen." },
                    nynorsk { +"Innvilga uføretrygd gir opptening til alderspensjon etter kapittel 19 i Folketrygdlova, og er berre medrekna i tilfelle den får stor verknad for berekninga. I denne saka har vi sett bort frå uføreoppteninga fordi den får liten eller ingen verknad for alderspensjonen." }
                )
            }
        }
        showIf(uforeTrygdTil_ATT) {
            paragraph {
                text(
                    bokmal { +"Vi har ikke vurdert om du har rett til å få innvilget uføretrygd da søknaden om uføretrygd ikke er ferdig behandlet ennå." },
                    nynorsk { +"Vi har ikkje vurdert om du har rett til å få innvilga uføretrygd då søknaden om uføretrygd ikkje er ferdig behandla enno." }
                )

            }
        }

        paragraph {
            text(
                bokmal { +"I prognosen av alderspensjon ved overgang fra uføretrygd ved fylte 67 år har vi ikke beregnet skjermingstillegg. Tillegget omfatter uføre født til og med 1962. Tillegget gis fordi uføretrygdede ikke har de samme mulighetene som arbeidsføre til å være i arbeid etter fylte 67 år. Du finner mer informasjon om levealdersjustering og skjermingstillegg på nav.no" },
                nynorsk { +"I prognosen av alderspensjon ved overgang frå uføretrygd ved fylte 67 år har vi ikkje berekna skjermingstillegg. Tillegget omfattar uføre fødd til og med 1962. Tillegget gis fordi uføretrygda ikkje har dei same moglegheitene som arbeidsføre til å vere i arbeid etter fylte 67 år. Du finn meir informasjon om levealdersjustering og skjermingstillegg på nav.no" }
            )
        }

        paragraph {
            text(
                bokmal { +"Beløpene er beregnet i dagens kroneverdi. Vi har benyttet satser for minste pensjonsnivå og garantipensjonsnivå når vi har beregnet pensjonen din." },
                nynorsk { +"Beløpa er berekna i dagens kroneverdi. Vi har nytta satsar for minste pensjonsnivå og garantipensjonsnivå når vi har berekna pensjonen din." }
            )
        }
        paragraph {
            text(
                bokmal { +"Alderspensjon reguleres (økes) med gjennomsnitt av lønns- og prisvekst. AFP i privat sektor reguleres med lønnsvekst fratrukket 0,75%. Endringene fram i tid, som skyldes forskjellig regulering, er ikke tatt hensyn til i beregningen." },
                nynorsk { +"Alderspensjon blir regulert opp med gjennomsnitt av lønns- og prisvekst. AFP i privat sektor blir regulert opp med lønnsvekst minus 0,75%. Endringane fram i tid, som skuldast forskjellig regulering, er det ikkje tatt omsyn til i utrekninga." }
            )
        }
        paragraph {
            text(
                bokmal { +"I beregningen legger Nav til grunn at du bor i Norge. Du kan ha rett til pensjon fra andre land du har arbeidet eller bodd i, slik pensjon er ikke med i beregningen. Vi har heller ikke tatt med tjenestepensjon ut over AFP i denne beregningen." },
                nynorsk { +"I berekninga legg Nav til grunn at du bur i Noreg. Du kan ha rett til pensjon frå andre land du har arbeidd eller budd i, slik pensjon er ikkje med i berekninga. Vi har heller ikkje tatt med tenestepensjon utover AFP i denne berekninga." }
            )
        }
    }