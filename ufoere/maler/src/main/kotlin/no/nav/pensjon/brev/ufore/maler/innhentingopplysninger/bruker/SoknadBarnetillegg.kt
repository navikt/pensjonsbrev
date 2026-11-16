package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger.bruker

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_SOKNAD_BARNETILLEGG
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object SoknadBarnetillegg : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNH_OPPL_SOKNAD_BARNETILLEGG
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Manglende dokumentasjon barnetillegg",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val arsak = saksbehandlervalg("arsak", "Velg årsak:").enum<ArsakBTManglendeDokEnum>()

        title {
            text (bokmal { + "Din søknad om barnetillegg i uføretrygd" },
                nynorsk { + "Din søknad om barnetillegg i uføretrygd" })
        }
        outline {
            showIf(arsak.equalTo(ArsakBTManglendeDokEnum.IKKE_BOENDE)) {

                paragraph {
                    text(
                        bokmal { +"Du har søkt om barnetillegg for barn som ikke bor hos deg. For å kunne behandle din søknad, trenger vi at du sender oss dokumentasjon på forsørgelse av barn. " },
                        nynorsk { +"Du har søkt om barnetillegg for barn som ikkje bur hos deg. For å kunne behandle søknaden din, treng vi at du sender oss dokumentasjon på forsørging av barn. " })
                }
                paragraph {
                    text(
                        bokmal { +"Dokumentasjon på forsørgelse kan være avtale om delt bosted, samværsavtale, avtale om bidrag. Dokumentasjonen må ha dato og være undertegnet av begge foreldrene. " },
                        nynorsk { +"Dokumentasjon på forsørging kan vere avtale om delt bustad, samværsavtale, avtale om bidrag. Dokumentasjonen må ha dato og vere underteikna av begge foreldra. " })
                }
            }.orShowIf(arsak.equalTo(ArsakBTManglendeDokEnum.FOSTERFORELDER)) {
                paragraph {
                    text(bokmal { +"Du har søkt om barnetillegg for barn som du er fosterforelder til. For at vi skal kunne vurdere og beregne barnetillegget, ber vi om at du sender inn bekreftelse på hva du får i godtgjørelse fra det offentlige. " },
                        nynorsk { +"Du har søkt om barnetillegg for barn som du er fosterforelder til. For at vi skal kunne vurdere og berekne barnetillegget, ber vi om at du sender inn stadfesting på kva du får i godtgjersle frå det offentlege. " })
                }
                paragraph {
                    text(bokmal { +"Det kan gis fosterhjemsgodtgjørelse til personer som har fosterbarn. Fosterhjemsgodtgjørelse består av utgiftsdekning og arbeidsgodtgjørelse. " },
                        nynorsk { +"Det kan gjevast fosterheimsgodtgjersle til personar som har fosterbarn. Fosterheimsgodtgjersle består av utgiftsdekning og arbeidsgodtgjersle. " })
                }
                paragraph {
                    text(bokmal { +"Arbeidsgodtgjørelsen skattlegges som lønn og anses som personinntekt. Det er personinntekt etter skatteloven § 12-2 som har betydning for reduksjon av barnetillegg på grunn av inntekt etter folketrygdloven § 12-16. " },
                        nynorsk { +"Arbeidsgodtgjersla vert skattlagd som løn og vert rekna som personinntekt. Det er personinntekt etter skattelova § 12-2 som har tyding for reduksjon av barnetillegg på grunn av inntekt etter folketrygdlova § 12-16. " })
                }
            }.orShowIf(arsak.equalTo(ArsakBTManglendeDokEnum.UTLAND)) {
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
                        nynorsk { +"Dersom fødselsattesten ikkje er på engelsk eller eit av dei nordiske språka, må vi ha ein omsett verifisert versjon i tillegg. " })
                }
                paragraph {
                    text(bokmal { +"Hvis du overfører jevnlig penger for å forsørge barnet, må vi få kopi av bankutskrift og dokumentasjon på hvem du sender disse pengene til. " },
                        nynorsk { +"Dersom du overfører jamleg pengar for å forsørgje barnet, må vi få kopi av bankutskrift og dokumentasjon på kven du sender desse pengane til. " })
                }
            }

            includePhrase(Felles.EttersendeDokumentasjon)
            paragraph {
                text(bokmal { +"Vi ber om at du sender opplysningene til oss innen " + fritekst("dato") + ". " },
                    nynorsk { +"Vi ber om at du sender opplysningane til oss innan " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { +"Dersom vi ikke får nødvendige opplysninger innen frist, kan søknaden din bli avslått på grunn av manglende opplysninger. " },
                    nynorsk { +"Dersom vi ikkje får nødvendige opplysningar innan frist, kan søknaden din bli avslått på grunn av manglande opplysningar. " })
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

enum class ArsakBTManglendeDokEnum(override val displayText: String) : SaksbehandlerValgEnum {
    IKKE_BOENDE("Ikke boende hos søker"),
    FOSTERFORELDER("Fosterforelder"),
    UTLAND("Utland"),
}

