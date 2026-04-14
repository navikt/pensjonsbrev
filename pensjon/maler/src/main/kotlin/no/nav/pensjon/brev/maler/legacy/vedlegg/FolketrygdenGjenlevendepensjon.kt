package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text

val vedleggFolketrygdenGjenlevendepensjon =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Folketrygden - vedlegg til vedtak som du kan klage over/anke" },
                nynorsk { +"Folketrygda - vedlegg til vedtak som du kan klage over/anke" },
                english { +"Social Insurance Scheme - attachments to decisions which you can appeal" }
            )
        },
        includeSakspart = false
    ) {
        title1 {
            text(
                bokmal { +"Navs plikt til å hjelpe deg - forvaltningsloven § 11" },
                nynorsk { +"Nav har plikt til å hjelpe deg - forvaltningslova § 11" },
                english { +"Duty of Nav services to assist you - Section 11 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal { +"Nav har plikt til å veilede deg om hvilke rettigheter og plikter du har. Nav hjelper deg med:" },
                nynorsk { +"Nav har plikt til å rettleie deg om kva rettar og plikter du har. Nav kan hjelpe deg med:" },
                english { +"Nav services have a duty to advise you of your rights and obligations. Nav services will help you with:" }
            )
            list {
                item {
                    text(
                        bokmal { +"opplysninger, råd og veiledning" },
                        nynorsk { +"opplysningar, råd og rettleiing" },
                        english { +"information, advice and guidance" }
                    )
                }
                item {
                    text(
                        bokmal { +"opplysninger om andre stønadsordninger" },
                        nynorsk { +"opplysningar om andre stønadsordningar" },
                        english { +"details about other benefit schemes" }
                    )
                }
                item {
                    text(
                        bokmal { +"å søke om en stønad eller pensjon, også fra et annet land" },
                        nynorsk { +"å søke om ein stønad eller ein pensjon, også frå eit anna land" },
                        english { +"applying for a benefit or pension, as well as from another country" }
                    )
                }
                item {
                    text(
                        bokmal { +"å klage hvis søknaden blir avslått" },
                        nynorsk { +"å klage dersom søknaden blir avvist" },
                        english { +"appeals if your application is refused" }
                    )
                }
                item {
                    text(
                        bokmal { +"å formidle kontakt med andre offentlige kontor" },
                        nynorsk { +"å formidle kontakt med andre offentlege kontor" },
                        english { +"making contact with other public offices" }
                    )
                }
            }
        }
        title1 {
            text(
                bokmal { +"Hjelp fra andre - forvaltningsloven § 12" },
                nynorsk { +"Hjelp frå andre - forvaltningslova § 12" },
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
                nynorsk {
                    +"Du har rett til å få hjelp av andre under heile saksbehandlinga. " +
                            "Du kan få hjelp av ein advokat, rettshjelpar eller annan fullmektig. " +
                            "Ein fullmektig kan vere ein person som er myndig eller ein organisasjon du er medlem av."
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
                nynorsk { +"Økonomisk dekning" },
                english { +"Financial cover" }
            )
        }
        title2 {
            text(
                bokmal { +"Dekning av saksomkostninger - forvaltningsloven § 36" },
                nynorsk { +"Dekning av sakskostnader - forvaltningslova § 36" },
                english { +"Cover for case costs - Section 36 of the Public Administration Act" }
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Dersom et vedtak blir endret til gunst for deg, kan du få dekket de nødvendige utgiftene du har hatt for å få endret vedtaket. " +
                            "Krav om dekning av saksomkostninger må settes frem senest tre uker etter at melding om det nye vedtaket ble mottatt, jf. § 36 tredje ledd. " +
                            "Krav om å få dekket sakskostnader skal sendes til Nav."
                },
                nynorsk {
                    +"Dersom eit vedtak blir endra til gunst for deg, kan du få dekt dei nødvendige utgiftene du har hatt for å få endra vedtaket. " +
                            "Du må setje fram krav om dekning av sakskostnader seinast tre veker etter at du mottok melding om det nye vedtaket, jfr. § 36 tredje ledd. " +
                            "Krav om å få dekt sakskostnader skal sendast til Nav."
                },
                english {
                    +"If a decision is amended in your favour, you may have the necessary expenses you have incurred to amend the decision covered. " +
                            "Claims for covering case costs must be presented no later than three weeks after you receive notice of a new decision, cf. Section 36 third paragraph. " +
                            "Claims for case costs to be covered must be sent to Nav services."
                }
            )
        }
        title2 {
            text(
                bokmal { +"Fri rettshjelp" },
                nynorsk { +"Fri rettshjelp" },
                english { +"Free legal aid" }
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Dersom du har behov for juridisk bistand i trygdesaken din, kan du på visse vilkår få utgiftene dekket etter reglene om fri rettshjelp. " +
                            "Her gjelder det visse inntekts- og formuesgrenser. " +
                            "Du kan få utgiftene dekket selv om vedtaket ikke blir endret til gunst for deg. " +
                            "For nærmere informasjon om reglene om fri rettshjelp henvises det til fylkesmannen."
                },
                nynorsk {
                    +"Dersom du har behov for juridisk hjelp i trygdesaka di, kan du på visse vilkår få utgiftene dekt etter reglane om fri rettshjelp. " +
                            "Her gjeld det visse inntekts- og formuesgrenser. " +
                            "Du kan få dekt utgiftene sjølv om vedtaket ikkje blir endra til gunst for deg. " +
                            "For nærmare informasjon om reglane om fri rettshjelp kan du ta kontakt med fylkesmannen."
                },
                english {
                    +"If you have need of legal aid in your national insurance case, you may, under certain conditions, have your expenses covered under the rules of free legal aid. " +
                            "Certain income and wealth limits apply here. " +
                            "You may have the expenses covered even if the decision is not amended in your favour. " +
                            "For further information about the free legal aid rules, please get in touch with the County Governor's office."
                }
            )
        }
        title1 {
            text(
                bokmal { +"Feilutbetaling av stønad" },
                nynorsk { +"Feilutbetaling av stønad" },
                english { +"Benefits paid in error" },
            )
        }
        title2 {
            text(
                bokmal { +"Tilbakekreving - folketrygdloven § 22-15 flg." },
                nynorsk { +"Tilbakekrevjing - folketrygdlova § 22-15 flg." },
                english { +"Recovery - Sections 22-15 ff of the National Insurance Act" },
            )
        }
        paragraph {
            text(
                bokmal { +"Dersom du mottar en stønad eller pensjon du ikke har krav på, kan det feilutbetalte beløpet kreves tilbake og trekkes i fremtidige ytelser." },
                nynorsk { +"Dersom du får ein stønad eller ein pensjon du ikkje har krav på, kan beløpet som er utbetalt på feil grunnlag, bli kravd tilbake og trekt i framtidige ytingar." },
                english { +"If you receive a benefit or pension to which you are not entitled, the wrongly paid amount may be claimed back and deducted from future benefits." },
            )
        }
        title2 {
            text(
                bokmal { +"Straffeansvar - folketygdloven § 25-12" },
                nynorsk { +"Straffeansvar - folketrygdlova § 25-12" },
                english { +"Criminal liability - Section 25-12 of the National Insurance Act" },
            )
        }
        paragraph {
            text(
                bokmal { +"Dersom du gir uriktige opplysninger mot bedre vitende eller ikke gir nødvendige opplysninger, kan det medføre straffeansvar." },
                nynorsk { +"Dersom du gir opplysningar som du veit var usanne eller du ikkje gir dei opplysningane som er nødvendige, kan det føre til straffeansvar." },
                english { +"If you knowingly give incorrect information or do not give the proper information, this can result in criminal liability." },
            )
        }
        title1 {
            text(
                bokmal { +"Søksmål for domstolene" },
                nynorsk { +"Søksmål for domstolane" },
                english { +"Action in the courts" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Det er på visse vilkår mulig å bringe en trygdesak inn for de vanlige domstolene. "
                    +"Før du kan gjøre dette, må du ha benyttet den muligheten du har til å klage og anke over vedtaket."
                },
                nynorsk {
                    +"Ein kan på visse vilkår bringe ei trygdesak inn for dei vanlege domstolane. "
                    +"Før du kan gjere dette, må du ha nytta retten din til å klage og anke over vedtaket."
                },
                english {
                    +"Under certain conditions, it is possible to bring a national insurance matter before the common courts. "
                    +"Before you do this, you must have taken the opportunity you have to appeal the decision."
                },
            )
        }
    }
