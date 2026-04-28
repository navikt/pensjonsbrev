package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text

val vedleggFolketrygdenBokmalEnglish =
    createAttachment<LangBokmalEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Folketrygden - vedlegg til vedtak som du kan klage over/anke" },
                english { +"Social Insurance Scheme - attachments to decisions which you can appeal" }
            )
        },
        includeSakspart = false
    ) {
        title1 {
            text(
                bokmal { +"Navs plikt til å hjelpe deg - forvaltningsloven § 11" },
                english { +"Duty of Nav services to assist you - Section 11 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { +"Nav har plikt til å veilede deg om hvilke rettigheter og plikter du har. Nav hjelper deg med:" },
                english { +"Nav services have a duty to advise you of your rights and obligations. Nav services will help you with:" }
            )
            list {
                item {
                    text(
                        bokmal { +"opplysninger, råd og veiledning" },
                        english { +"information, advice and guidance" }
                    )
                }
                item {
                    text(
                        bokmal { +"opplysninger om andre stønadsordninger" },
                        english { +"details about other benefit schemes" }
                    )
                }
                item {
                    text(
                        bokmal { +"å søke om en stønad eller pensjon, også fra et annet land" },
                        english { +"applying for a benefit or pension, as well as from another country" }
                    )
                }
                item {
                    text(
                        bokmal { +"å klage hvis søknaden blir avslått" },
                        english { +"appeals if your application is refused" }
                    )
                }
                item {
                    text(
                        bokmal { +"å formidle kontakt med andre offentlige kontor" },
                        english { +"making contact with other public offices" }
                    )
                }
            }
        }
        title1 {
            text(
                bokmal { +"Hjelp fra andre - forvaltningsloven § 12" },
                english { +"Assistance from others - Section 12 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du har rett til å få hjelp av andre under hele saksbehandlingen. " +
                            "Du kan la deg bistå av en advokat, rettshjelper eller annen fullmektig. " +
                            "En fullmektig kan være enhver myndig person eller en organisasjon du er medlem av."
                },
                english {
                    +"You are entitled to receive assistance from other people throughout the whole case proceedings. " +
                            "You can be assisted by a lawyer, legal assistant or other authorised representative. " +
                            "An authorised representative can be any person of legal age or organisation of which you are a member."
                }
            )
        }
        title1 {
            text(
                bokmal { +"Økonomisk dekning" },
                english { +"Financial cover" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { +"Dekning av saksomkostninger - forvaltningsloven § 36" },
                        english { +"Cover for case costs - Section 36 of the Public Administration Act" }
                    )
                    newline()
                    text(
                        bokmal {
                            +"Dersom et vedtak blir endret til gunst for deg, kan du få dekket de nødvendige utgiftene du har hatt for å få endret vedtaket. " +
                                    "Krav om dekning av saksomkostninger må settes frem senest tre uker etter at melding om det nye vedtaket ble mottatt, jf. § 36 tredje ledd. " +
                                    "Krav om å få dekket sakskostnader skal sendes til Nav."
                        },
                        english {
                            +"If a decision is amended in your favour, you may have the necessary expenses you have incurred to amend the decision covered. " +
                                    "Claims for covering case costs must be presented no later than three weeks after you receive notice of a new decision, cf. Section 36 third paragraph. " +
                                    "Claims for case costs to be covered must be sent to Nav services."
                        }
                    )
                }
                item {
                    text(
                        bokmal { +"Fri rettshjelp" },
                        english { +"Free legal aid" }
                    )
                    newline()
                    text(
                        bokmal {
                            +"Dersom du har behov for juridisk bistand i trygdesaken din, kan du på visse vilkår få utgiftene dekket etter reglene om fri rettshjelp. " +
                                    "Her gjelder det visse inntekts- og formuesgrenser. " +
                                    "Du kan få utgiftene dekket selv om vedtaket ikke blir endret til gunst for deg. " +
                                    "For nærmere informasjon om reglene om fri rettshjelp henvises det til fylkesmannen."
                        },
                        english {
                            +"If you have need of legal aid in your national insurance case, you may, under certain conditions, have your expenses covered under the rules of free legal aid. " +
                                    "Certain income and wealth limits apply here. " +
                                    "You may have the expenses covered even if the decision is not amended in your favour. " +
                                    "For further information about the free legal aid rules, please get in touch with the County Governor's office."
                        }
                    )
                }
            }
        }
        title1 {
            text(
                bokmal { +"Feilutbetaling av stønad" },
                english { +"Benefits paid in error" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { +"Tilbakekreving - folketrygdloven § 22-15 flg." },
                        english { +"Recovery - Sections 22-15 ff of the National Insurance Act" }
                    )
                    newline()
                    text(
                        bokmal { +"Dersom du mottar en stønad eller pensjon du ikke har krav på, kan det feilutbetalte beløpet kreves tilbake og trekkes i fremtidige ytelser." },
                        english { +"If you receive a benefit or pension to which you are not entitled, the wrongly paid amount may be claimed back and deducted from future benefits." }
                    )
                }
                item {
                    text(
                        bokmal { +"Straffeansvar - folketrygdloven § 25-12" },
                        english { +"Criminal liability - Section 25-12 of the National Insurance Act" }
                    )
                    newline()
                    text(
                        bokmal { +"Dersom du gir uriktige opplysninger mot bedre vitende eller ikke gir nødvendige opplysninger, kan det medføre straffeansvar." },
                        english { +"If you knowingly give incorrect information or do not give the proper information, this can result in criminal liability." }
                    )
                }
            }
        }
        title1 {
            text(
                bokmal { +"Søksmål for domstolene" },
                english { +"Action in the courts" }
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Det er på visse vilkår mulig å bringe en trygdesak inn for de vanlige domstolene. "
                    +"Før du kan gjøre dette, må du ha benyttet den muligheten du har til å klage og anke over vedtaket."
                },
                english {
                    +"Under certain conditions, it is possible to bring a national insurance matter before the common courts. "
                    +"Before you do this, you must have taken the opportunity you have to appeal the decision."
                }
            )
        }
    }