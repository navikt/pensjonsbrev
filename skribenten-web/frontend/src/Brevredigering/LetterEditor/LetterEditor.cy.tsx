import "./editor.css";

import React from "react";

import { getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";

import { LetterEditor } from "./LetterEditor";

describe("<LetterEditor />", () => {
  it("Vertical navigation", () => {
    cy.viewport(800, 1400);
    cy.mount(<LetterEditor initialState={JSON.parse(state)} />);
    cy.contains("punkt 3").click();
    move("{upArrow}", 3);
    move("{rightArrow}", 20);
    move("{upArrow}", 1);
    assertCaret("CP1", 0);
    move("{leftArrow}", 1);
    move("{downArrow}", 1);
    move("{rightArrow}", 4);
    move("{upArrow}", 1);
    assertCaret("fra trygdemyndighetene i", 26);
  });
});

function move(key: string, times: number) {
  cy.focused().type(key.repeat(times));
}

function assertCaret(content: string, caretOffset: number) {
  cy.get(".editor").then(() => {
    cy.contains(content).focused();
    expect(getRange()?.startOffset).to.eq(caretOffset);
  });
}

const state =
  "{\n" +
  '    "title": "Informasjon om saksbehandlingstiden vår",\n' +
  '    "sakspart": {\n' +
  '        "gjelderNavn": "Test Testeson",\n' +
  '        "gjelderFoedselsnummer": "12345678910",\n' +
  '        "saksnummer": "1234",\n' +
  '        "dokumentDato": "14.03.2024"\n' +
  "    },\n" +
  '    "blocks": [\n' +
  "        {\n" +
  '            "id": -29856172,\n' +
  '            "editable": true,\n' +
  '            "content": [\n' +
  "                {\n" +
  '                    "id": 2054713812,\n' +
  '                    "text": "Vi har ",\n' +
  '                    "type": "LITERAL"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": -169301316,\n' +
  '                    "text": "4. januar 2024",\n' +
  '                    "type": "VARIABLE"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": -1820992704,\n' +
  '                    "text": " mottatt søknaden din om ",\n' +
  '                    "type": "LITERAL"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 921220,\n' +
  '                    "text": "YTELSE",\n' +
  '                    "type": "VARIABLE"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 1962386266,\n' +
  '                    "text": " fra trygdemyndighetene i ",\n' +
  '                    "type": "LITERAL"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 1112569114,\n' +
  '                    "text": "LAND",\n' +
  '                    "type": "VARIABLE"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 46,\n' +
  '                    "text": "CP1.",\n' +
  '                    "type": "LITERAL"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 1993244052,\n' +
  '                    "items": [\n' +
  "                        {\n" +
  '                            "content": [\n' +
  "                                {\n" +
  '                                    "id": -1890568417,\n' +
  '                                    "text": "punkt 1 - TODO fjern punktliste - lang tekst som brekker over linje",\n' +
  '                                    "type": "LITERAL"\n' +
  "                                }\n" +
  "                            ]\n" +
  "                        },\n" +
  "                        {\n" +
  '                            "content": [\n' +
  "                                {\n" +
  '                                    "id": 935219458,\n' +
  '                                    "text": "punkt 2 - TODO fjern punktliste",\n' +
  '                                    "type": "LITERAL"\n' +
  "                                }\n" +
  "                            ]\n" +
  "                        },\n" +
  "                        {\n" +
  '                            "content": [\n' +
  "                                {\n" +
  '                                    "id": -1163733415,\n' +
  '                                    "text": "punkt 3 - TODO fjern punktliste",\n' +
  '                                    "type": "LITERAL"\n' +
  "                                }\n" +
  "                            ]\n" +
  "                        }\n" +
  "                    ],\n" +
  '                    "type": "ITEM_LIST"\n' +
  "                }\n" +
  "            ],\n" +
  '            "type": "PARAGRAPH"\n' +
  "        },\n" +
  "        {\n" +
  '            "id": -1125430838,\n' +
  '            "editable": true,\n' +
  '            "content": [\n' +
  "                {\n" +
  '                    "id": -1505022304,\n' +
  '                    "text": "Saksbehandlingstiden vår er vanligvis ",\n' +
  '                    "type": "LITERAL"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 1762521580,\n' +
  '                    "text": "4",\n' +
  '                    "type": "VARIABLE"\n' +
  "                },\n" +
  "                {\n" +
  '                    "id": 1027473067,\n' +
  '                    "text": " uker.",\n' +
  '                    "type": "LITERAL"\n' +
  "                }\n" +
  "            ],\n" +
  '            "type": "PARAGRAPH"\n' +
  "        },\n" +
  "        {\n" +
  '            "id": 1620092169,\n' +
  '            "editable": true,\n' +
  '            "content": [\n' +
  "                {\n" +
  '                    "id": 1620092138,\n' +
  '                    "text": "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss.",\n' +
  '                    "type": "LITERAL"\n' +
  "                }\n" +
  "            ],\n" +
  '            "type": "PARAGRAPH"\n' +
  "        },\n" +
  "        {\n" +
  '            "id": -349491824,\n' +
  '            "editable": true,\n' +
  '            "content": [\n' +
  "                {\n" +
  '                    "id": -349491855,\n' +
  '                    "text": "Meld fra om endringer",\n' +
  '                    "type": "LITERAL"\n' +
  "                }\n" +
  "            ],\n" +
  '            "type": "TITLE1"\n' +
  "        },\n" +
  "        {\n" +
  '            "id": 161611260,\n' +
  '            "editable": true,\n' +
  '            "content": [\n' +
  "                {\n" +
  '                    "id": 161611229,\n' +
  '                    "text": "Du må melde fra til oss med en gang hvis det skjer endringer som kan ha betydning for saken din, for eksempel ved endring av sivilstand eller ved flytting.",\n' +
  '                    "type": "LITERAL"\n' +
  "                }\n" +
  "            ],\n" +
  '            "type": "PARAGRAPH"\n' +
  "        }\n" +
  "    ],\n" +
  '    "signatur": {\n' +
  '        "hilsenTekst": "Med vennlig hilsen",\n' +
  '        "saksbehandlerRolleTekst": "Saksbehandler",\n' +
  '        "saksbehandlerNavn": "Ole Saksbehandler",\n' +
  '        "attesterendeSaksbehandlerNavn": "",\n' +
  '        "navAvsenderEnhet": "NAV Familie- og pensjonsytelser Porsgrunn"\n' +
  "    }\n" +
  "}";
