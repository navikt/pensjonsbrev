import React from 'react';
import {NextPage} from "next";
import {SkribentenConfig} from "./_app";
import { Header } from "@navikt/ds-react-internal";

import "@navikt/ds-css"
import "@navikt/ds-css-internal"
import LetterFilter, {LetterFilterProps} from "../modules/LetterFilter/LetterFilter";

// todo remove

let somethingTest: LetterFilterProps = {
    favourites: [
        {id: "0", name: "Test0"},
        {id: "1", name: "Test1"},
        {id: "2", name: "Test2"},
        {id: "3", name: "Test3"},
    ],
    categories: [
        {name: "Brev med skjema", letters: [{id: "4", name: "Test4"}, {id: "5", name: "Test5"}, {id: "6", name: "Test6"}, {id: "7", name: "Test7"},]},
        {name: "Innhente opplysninger", letters: [{id: "8", name: "Test8"}, {id: "9", name: "Test9"}, {id: "10", name: "Test10"}, {id: "11", name: "Test11"},]},
        {name: "Informasjonsbrev", letters: [{id: "12", name: "Test12"}, {id: "13", name: "Test13"}, {id: "14", name: "Test14"}, {id: "15", name: "Test15"},]},
        {name: "Notat", letters: [{id: "16", name: "Test16"}, {id: "17", name: "Test17"}, {id: "18", name: "Test18"}, {id: "19", name: "Test19"},]},
        {name: "Øvrig", letters: [{id: "20", name: "Test20"}, {id: "21", name: "Test21"}, {id: "22", name: "Test22"}, {id: "23", name: "Test23"},]},
        {name: "Vedtak", letters: [{id: "24", name: "Test24"}, {id: "25", name: "Test25"}, {id: "26", name: "Test26"}, {id: "27", name: "Test27"},]},
    ]
}

const Brevvelger:NextPage<SkribentenConfig> = () => {
    return (
        <div>
            <Header>
                <Header.Title as="h1">Skribenten</Header.Title>
                <Header.Button>Brevvelger</Header.Button>
                <Header.Button>Brevbehandler</Header.Button>
                <Header.User name="Test Testerson" className="ml-auto"/>
            </Header>
            <LetterFilter categories={somethingTest.categories} favourites={somethingTest.favourites}/>
        </div>
    );
};

export default Brevvelger;