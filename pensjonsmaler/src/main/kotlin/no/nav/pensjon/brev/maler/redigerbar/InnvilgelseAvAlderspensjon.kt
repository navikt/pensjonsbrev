package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.godkjentYrkesskade_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AvdodSelectors.avdodNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AvdodSelectors.avdodNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AvdodSelectors.harAvdod_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingAvdodSelectors.minst20ArBotidKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingAvdodSelectors.minst20ArTrygdetidKap20_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportForbud_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.InngangOgEksportVurderingSelectors.minst20ArTrygdetid_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdod
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdod_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.faktiskBostedsland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.inngangOgEksportVurderingAvdod
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.egenOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevenderett
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.kravVirkDatoFomSenereEnnOensketUttakstidspunkt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// Tekster og logikk mht ektefelletillegg og barnetillegg er fjernet fra brevmalen etter en samtale med Ingrid Strand

@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val regelverkType = pesysData.regelverkType
        val sivilstand = pesysData.sivilstand
        val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
        val sivilstandBestemtLitenBokstav = pesysData.sivilstand.bestemtForm(storBokstav = false)
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val gjenlevendetilleggInnvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val gjenlevendetilleggKap19Innvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.format()
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val gjenlevendetilleggKap19 =
            pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(then = Kroner(0))
        val gjenlevenderettAnvendt = pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
        val avdodNavn = pesysData.avdod.avdodNavn_safe.ifNull(then = "avdødsnavn mangler")
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val afpPrivatResultatFellesKontoret = pesysData.afpPrivatResultatFellesKontoret_safe.ifNull(then = false)
        val erEksportberegnet = pesysData.alderspensjonVedVirk.erEksportberegnet_safe.ifNull(then = false)
        val eksportForbud = pesysData.inngangOgEksportVurdering.eksportForbud_safe.ifNull(then = false)
        val minst20ArTrygdetid = pesysData.inngangOgEksportVurdering.minst20ArTrygdetid_safe.ifNull(then = false)
        val minst20ArTrygdetidKap20 =
            pesysData.inngangOgEksportVurderingAvdod.minst20ArTrygdetidKap20_safe.ifNull(then = false)
        val minst20ArBotidKap19 = pesysData.inngangOgEksportVurderingAvdod.minst20ArBotidKap19_safe.ifNull(then = false)
        val fakstiskBostedsland = pesysData.faktiskBostedsland_safe.ifNull(then = "bostedsland mangler")
        val harAvdod = pesysData.avdod.harAvdod_safe.ifNull(then = false)
        val eksportTrygdeavtaleEOS =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOS_safe.ifNull(then = false)
        val eksportTrygdeavtaleAvtaleland =
            pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland_safe.ifNull(then = false)
        val godkjentYrkesskade = pesysData.alderspensjonVedVirk.godkjentYrkesskade
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget





        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {
            showIf(
                not(uforeKombinertMedAlder) and not(innvilgetFor67)
                        and not(gjenlevendetilleggInnvilget) and not(gjenlevendetilleggKap19Innvilget)
            ) {
                // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + " i alderspensjon fra folketrygden.",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + " i alderspensjon frå folketrygda",
                        English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + " as retirement pension from the National Insurance Scheme."
                    )
                }
            }.orShowIf(
                uforeKombinertMedAlder and innvilgetFor67 and not(gjenlevendetilleggInnvilget) and not(
                    gjenlevendetilleggKap19Innvilget
                )
            ) {
                // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + "." +
                                " Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + "." +
                                " Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + "." +
                                " You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                    )
                }
            }.orShowIf(
                gjenlevendetilleggKap19Innvilget
            ) {
                //  beloepApOgGjtvedVirkMedDato_001, beloepApOgGjvedVirkMedDato_002
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt fra ".expr() +
                                kravVirkDatoFom + ".",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt frå ".expr() +
                                kravVirkDatoFom + ".",
                        English to "You will receive NOK ".expr() + totalPensjon + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax from ".expr() +
                                kravVirkDatoFom + "."
                    )
                    showIf(gjenlevendetilleggInnvilget) {
                        textExpr(
                            Bokmal to " Av dette er gjenlevendetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            Nynorsk to " Av dette er attlevandetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            English to " Of this, the survivor’s supplement is NOK ".expr() + gjenlevendetilleggKap19.format() + "."
                        )
                    }
                    showIf(uforeKombinertMedAlder) {
                        text(
                            Bokmal to " Dette er i tillegg til uføretrygden din.",
                            Nynorsk to " Dette er i tillegg til uføretrygda di.",
                            English to " This is in addition to your disability benefit.",
                        )
                    }
                }
            }
            showIf(saksbehandlerValg.kravVirkDatoFomSenereEnnOensketUttakstidspunkt) {
                // invilgelseAPVirkfom
                paragraph {
                    text(
                        Bokmal to "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. Dette går frem av folketrygdloven § 22-13.",
                        Nynorsk to "Vi kan tidlegast innvilge søknaden din månaden etter at du søkte. Dette går fram av folketrygdlova § 22-13.",
                        English to "We can at the earliest grant your application the month after you applied. This is in accordance with the regulations of § 22-13 of the National Insurance Act.",
                    )
                }
            }

            includePhrase(Felles.FlereBeregningsperioder)

            showIf(
                saksbehandlerValg.harGjenlevenderett
                        and gjenlevenderettAnvendt and avdodNavn.notNull() and not(gjenlevendetilleggKap19Innvilget) and not(
                    gjenlevendetilleggInnvilget
                )
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter ".expr() + avdodNavn + ". Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening.",
                        Nynorsk to "I beregninga vår har vi teke utgangspunkt i pensjonsrettar du har etter ".expr() + avdodNavn + ". Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening.",
                        English to "We have based the calculation on the pension rights you have after ".expr() + avdodNavn + ". This gives you a higher pension than if we had only based the calculation on your own earnings."
                    )
                }
            }
            // TODO is this correct?
            showIf(saksbehandlerValg.harGjenlevendetillegg and gjenlevenderettAnvendt and gjenlevendetilleggKap19.isNull()) {
                // beregningAPGjtOpptj
                paragraph {
                    text(
                        Bokmal to "Fra januar 2024 er gjenlevenderett i alderspensjonen din skilt ut som et eget gjenlevendetillegg." +
                                " Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Frå januar 2024 er attlevanderett i alderspensjonen din skild ut som eit eige attlevandetillegg." +
                                " Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "From January 2024, the survivor’s right in your retirement pension is separated out as a separate survivor’s supplement." +
                                " The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år.",
                        Nynorsk to "Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år.",
                        English to "The survivor's supplement will not be adjusted when the pension increases from 1 May every year.",
                    )
                }
            }

            showIf(saksbehandlerValg.harGjenlevendetilleggKap19 and gjenlevendetilleggKap19Innvilget and avdodNavn.notNull()) {
                // beregningAPGjtKap19
                paragraph {
                    textExpr(
                        Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr() + avdodNavn + ".",
                        Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr() + avdodNavn + ".",
                        English to "You receive a survivor’s supplement in retirement pension because you have pension rights after ".expr() + avdodNavn + "."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself."
                    )
                }
            }

            showIf(saksbehandlerValg.egenOpptjening and not(gjenlevenderettAnvendt and avdodNavn.isNull())) {
                // beregningAPGjRettOpptjEgen_002
                title1 {
                    text(
                        Bokmal to "Gjenlevenderett i alderspensjon",
                        Nynorsk to "Attlevenderett i alderspensjon",
                        English to "Survivor's rights in retirement pension",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter ".expr() + avdodNavn + ".",
                        Nynorsk to "I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter ".expr() + avdodNavn + ".",
                        English to "We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after ".expr() + avdodNavn + ".",
                    )
                }
            }

            showIf(privatAFPErBrukt) {
                // innvilgelseAPogAFPPrivat
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + uttaksgrad.format() + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefestede pensjonen din (AFP) gjør at du har rett til alderspensjon før du fyller 67 år.",
                        Nynorsk to "Du får ".expr() + uttaksgrad.format() + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefesta pensjonen din (AFP) gjer at du har rett til alderspensjon før 67 år.",
                        English to "You have been granted ".expr() + uttaksgrad.format() + " percent retirement pension because your total retirement pension and contractual early retirement pension (AFP) makes you eligible for retirement pension before the age of 67."
                    )
                }
            }

            showIf(afpPrivatResultatFellesKontoret) {
                // soktAFPPrivatInfo
                paragraph {
                    text(
                        Bokmal to "Du har også søkt om avtalefestet pensjon (AFP), og du vil få et eget vedtak om dette.",
                        Nynorsk to "Du har også søkt om avtalefesta pensjon (AFP), og du vil få eit eige vedtak om dette.",
                        English to "You have also applied for contractual early retirement pension (AFP) and will receive a separate decision on this.",
                    )

                }
            }

            showIf(erEksportberegnet and not(eksportForbud) and not(minst20ArTrygdetid)) {
                // innvilgelseAPUnder20aar
                paragraph {
                    textExpr(
                        Bokmal to "Du har ikke vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du bor i ".expr() + fakstiskBostedsland + ".",
                        Nynorsk to "Du har ikkje vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr() + fakstiskBostedsland + ".",
                        English to "You have not been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr() + fakstiskBostedsland + ".",
                    )
                }
            }

            showIf(
                erEksportberegnet and not(eksportForbud) and harAvdod and not(minst20ArTrygdetidKap20) and not(
                    minst20ArBotidKap19
                )
            ) {
                // innvilgelseAPUnder20aarAvdod
                paragraph {
                    textExpr(
                        Bokmal to "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du når du bor i ".expr() + fakstiskBostedsland + ".",
                        Nynorsk to "Verken du eller avdøde har vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du bur i ".expr() + fakstiskBostedsland + ".",
                        English to "Neither you nor the deceased have been a member of the National Insurance Scheme for at least 20 years. You are therefore not eligible for a full retirement pension while living in ".expr() + fakstiskBostedsland + ".",
                    )
                }
            }

            showIf(uttaksgrad.lessThan(100) and not(uforeKombinertMedAlder)) {
                paragraph {
                    text(
                        Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                        Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                        English to "You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                    )
                }
            }

            showIf(eksportTrygdeavtaleEOS or eksportTrygdeavtaleAvtaleland) {
                // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
                paragraph {
                    textExpr(
                        Bokmal to "Vi forutsetter at du bor i ".expr() + fakstiskBostedsland + ". Hvis du skal flytte til et ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utenfor EØS-området",
                            ifFalse = "annet land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                        Nynorsk to "Vi føreset at du bur i ".expr() + fakstiskBostedsland + ". Dersom du skal flytte til eit ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "land utanfor EØS-området",
                            ifFalse = "anna land"
                        ) + ", må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon.",
                        English to "We presume that you live in ".expr() + fakstiskBostedsland + ". If you are moving to ".expr() + ifElse(
                            eksportTrygdeavtaleEOS,
                            ifTrue = "a country outside the EEA region",
                            ifFalse = "another country"
                        ) + ", it is important that you contact Nav. We will then reassess your eligibility for retirement pension."
                    )
                }
            }

            showIf(regelverkType.isNotAnyOf(AP2025)) {
                // AP2011TidligUttakPenTHjemmel, AP2011TidligUttakHjemmel
                paragraph {
                    textExpr(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til ".expr() + ifElse(
                            pensjonstilleggInnvilget,
                            ifTrue = "19-9",
                            ifFalse = "19-8"
                        ) + ", 19-10".expr() + ifElse(
                            innvilgetFor67,
                            ifTrue = ", 19-11",
                            ifFalse = ""
                        ),
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til ".expr() + ifElse(
                            pensjonstilleggInnvilget,
                            ifTrue = "19-9",
                            " 19-8"
                        ) + ", 19-10".expr() + ifElse(
                            innvilgetFor67,
                            ", 19-11",
                            ""
                        ),
                        English to "This decision was made pursuant to the provisions of §§ 19-2 to ".expr() + ifElse(
                            pensjonstilleggInnvilget,
                            ifTrue = "19-9",
                            ifFalse = "19-8"
                        ) + ", 19-10".expr() + ifElse(
                            innvilgetFor67,
                            ", 19-11",
                            ifFalse = ""
                        )
                    )
                    showIf(regelverkType.isOneOf(AP2016)) {
                        text(
                            Bokmal to ", 19-15",
                            Nynorsk to ", 19-15",
                            English to ", 19-15"

                        )
                        textExpr(
                            Bokmal to ifElse(
                                godkjentYrkesskade,
                                ifTrue = ", 19-20",
                                ifFalse = ""
                            ),
                            Nynorsk to ifElse(
                                godkjentYrkesskade,
                                ifTrue = ", 19-20",
                                ifFalse = ""
                            ),
                            English to ifElse(
                                godkjentYrkesskade,
                                ifTrue = ", 19-20",
                                ifFalse = ""
                            )
                        )
                        text(
                            Bokmal to ", 20-3",
                            Nynorsk to ", 20-3",
                            English to ", 20-3"
                        )
                        showIf(
                            garantipensjonInnvilget and not(innvilgetFor67) {

                            }
                        )
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016) and garantipensjonInnvilget and innvilgetFor67,
                            ifTrue = ", 20-9 til 20-15",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016) and garantipensjonInnvilget and not(innvilgetFor67),
                            ifTrue = ", 20-9 til 20-14",
                            ifFalse = ""
                        ) + " og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til ".expr() + ifElse(
                            pensjonstilleggInnvilget,
                            ifTrue = "19-9",
                            ifFalse = "19-8"
                        ) + ", 19-10".expr() + ifElse(
                            innvilgetFor67,
                            ifTrue = ", 19-11",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016),
                            ifTrue = ", 19-15",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            godkjentYrkesskade,
                            ifTrue = ", 19-20",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016),
                            ifTrue = ", 20-3",
                            ifFalse = ", 20-3"
                        ) + " og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 19-2 to ".expr() + ifElse(
                            pensjonstilleggInnvilget,
                            ifTrue = "19-9",
                            ifFalse = "19-8"
                        ) + ", 19-10".expr() + ifElse(
                            innvilgetFor67,
                            ifTrue = ", 19-11",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016),
                            ifTrue = ", 19-15",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            godkjentYrkesskade,
                            ifTrue = ", 19-20",
                            ifFalse = ""
                        ) + "".expr() + ifElse(
                            regelverkType.isOneOf(AP2016),
                            ifTrue = ", 20-3",
                            ifFalse = ""
                        ) + " og 22-12 of the National Insurance Act.",
                        )
                    }
                }.orShowIf(regelverkType.isOneOf(AP2016)) {

                }
            }
        }
    }
}





