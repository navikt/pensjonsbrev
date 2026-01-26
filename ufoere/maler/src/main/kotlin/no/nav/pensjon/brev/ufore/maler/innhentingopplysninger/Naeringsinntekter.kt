package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_NAERINGSINNTEKTER
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object Naeringsinntekter : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_NAERINGSINNTEKTER
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger om næringsinntekter",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Vi trenger opplysninger om dine næringsinntekter " })
        }
        outline {
            paragraph {
                text(bokmal { + fritekst("Veiledning til saksbehandler: Gjør en selvstendig vurdering av hvilken dokumentasjon og informasjon som er nødvendig for å behandle saken. Rediger malen og tilpass til det du trenger.") })
            }
            paragraph {
                text(bokmal { +"Som et ledd i behandlingen av uføresaken din trenger vi bekreftede opplysninger om inntekten din. " })
            }
            paragraph {
                text(bokmal { +"Du eier og driver " + fritekst("navn på firma") + ", og vi har derfor behov for ytterligere opplysninger for å få vurdert din inntektsevne. Dersom ikke mottatt: (Vi ber om at du fyller ut inntektsskjema for selvstendig næringsdrivende.) " })
            }
            paragraph {
                text(bokmal { +"Skjemaet finner du på nav.no/fyllut/nav120607 " })
            }
            paragraph {
                text(bokmal { +"Dersom selskapet er avsluttet, så ber vi om dokumentasjon på dette; gjerne i form av melding om sletting fra Brønnøysundregisteret. " })
            }
            paragraph {
                text(bokmal { +"Dersom selskapet ikke er avsluttet og slettet, så ber vi om: " })
                list {
                    item {
                        text(bokmal { + "Dine personlige selvangivelser for årene " + fritekst("XXXX-XXXX") })
                    }
                    item {
                        text(bokmal { + "Næringsspesifikasjon levert i ALTINN/skatteetaten fra 2021, og:" })
                    }
                    item {
                        text(bokmal { + "Næringsoppgaver for årene " + fritekst("XXXX-XXXX (gjelder kun dersom vi må ha opplysninger før 2021)") })
                    }
                    item {
                        text(bokmal { + "Personinntektsskjema for " + fritekst("XXXX-XXXX (Gjelder kun selvstendig næringsdrivende, og brukes ikke etter 2022)") })
                    }
                    item {
                        text(bokmal { + "Foreløpig resultatregnskap hittil i år og forventet fremtidig pensjonsgivende inntekt" })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Sender du dokumentasjonen med posten, må du legge ved en forside som du finner på ${Constants.NAV_URL}" })
            }
            paragraph {
                text(bokmal { +"Ved tap av delvis inntektsevne, gis det en gradert ytelse som svarer til den del av inntektsevnen/arbeidsevnen som er tapt. " })
            }
            paragraph {
                text(bokmal { +"For å kunne vurdere dette må vi ha bekreftede inntektsopplysninger der det fremgår hvilken inntekt du forventer å få fremover. " })
            }
            paragraph {
                text(bokmal { +"Fristen for å sende inn nødvendige opplysninger settes til " + fritekst("dato") + ". "})
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
