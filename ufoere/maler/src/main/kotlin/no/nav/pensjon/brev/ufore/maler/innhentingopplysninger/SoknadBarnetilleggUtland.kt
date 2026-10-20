package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG_UTLAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadBarnetilleggUtland : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_SOKNAD_BARNETILLEGG_UTLAND
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Søkt barnetillegg Barn som ikke bor i Norge",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Din søknad om barnetillegg i uføretrygd" },
                nynorsk { + "Din søknad om barnetillegg i uføretrygd" })
        }
        outline {
            paragraph {
                text(bokmal { +"Du har søkt om barnetillegg for barn som ikke bor i Norge. For å kunne behandle søknaden trenger vi flere opplysninger fra deg. " },
                    nynorsk { +"Du har søkt om barnetillegg for barn som ikkje bur i Noreg. For å kunne behandle søknaden treng vi fleire opplysningar frå deg. " })
            }
            paragraph {
                text(bokmal { +"Vi trenger" },
                    nynorsk { +"Vi treng" })
                list {
                    item { text(bokmal { +"bekreftet kopi av fødselsattest for barnet/barna" },
                        nynorsk { +"bekrefta kopi av fødselsattest for barnet/barna" }) }
                    item { text(bokmal { +"kopi av identifikasjonspapir med bilde av barnet/barna" },
                        nynorsk { +"kopi av identifikasjonspapir med bilete av barnet/barna" }) }
                    item { text(bokmal { +"dokumentasjon som viser at du er med på å forsørge barnet/barna" },
                        nynorsk { +"dokumentasjon som viser at du er med på å forsørgje barnet/barna" }) }
                    item { text(bokmal { +"dokumentasjon som viser hvor barnet bor i dag" },
                        nynorsk { +"dokumentasjon som viser kvar barnet bur i dag" }) }
                }
            }
            paragraph {
                text(bokmal { +"Hvis fødselsattesten ikke er på engelsk eller et av de nordiske språkene, må vi ha en oversatt verifisert versjon i tillegg. " },
                    nynorsk { +"Hvis fødselsattesten ikkje er på engelsk eller eit av dei nordiske språka, må vi ha ein omsett verifisert versjon i tillegg. " })
            }
            paragraph {
                text(bokmal { +"Hvis du overfører jevnlig penger for å forsørge barnet, må vi få kopi av bankutskrift og dokumentasjon på hvem du sender disse pengene til. " },
                    nynorsk { +"Hvis du overfører jamleg pengar for å forsørgje barnet, må vi få kopi av bankutskrift og dokumentasjon på kven du sender desse pengane til. " })
            }
            paragraph {
                text(bokmal { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklest og raskest å ettersende digitalt. Du finner skjemaoversikten og veiledning på våre nettsider ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL} " },
                    nynorsk { +"Du kan ettersende dokumentasjon digitalt eller i posten. Det er enklast og raskast å ettersende digitalt. Du finn skjemaoversikta og rettleiing på nettsidene våre ${Constants.SOKNAD_URL} eller ${Constants.ETTERSENDE_URL} " })
            }
            paragraph {
                text(bokmal { +"Vi ber om at opplysningene sendes oss innen " + fritekst("dato") + ". " },
                    nynorsk { +"Vi ber om at opplysningane vert sendt oss innan " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { +"Hvis vi ikke får nødvendige opplysninger innen fristen, kan søknaden bli avslått på grunn av manglende opplysninger. Gi oss beskjed dersom det tar lenger tid å innhente opplysningene." },
                    nynorsk { +"Dersom vi ikkje får nødvendige opplysningar innan fristen, kan søknaden bli avslått på grunn av manglande opplysningar. Gi oss beskjed dersom det tek lengre tid å innhente opplysningane." })
            }
            paragraph {
                text(bokmal { +"I folketrygdloven § 21-3 finner du informasjon om opplysningsplikten din til Nav. " },
                    nynorsk { +"I folketrygdlova § 21-3 finn du informasjon om opplysningsplikta di til Nav. " })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
