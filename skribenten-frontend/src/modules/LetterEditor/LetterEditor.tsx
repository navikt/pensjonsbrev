import Title1 from "./components/title1/Title1"
import Paragraph from "./components/paragraph/Paragraph"
import Header from "./components/header/Header"
import styles from "./LetterEditor.module.css"
import {FC, useState} from "react"
import {AnyBlock, VariableDecl, Variables} from "./model"
import {bindAction, BoundAction, combine} from "../../lib/actions"
import {SplitBlockAtContent} from "./components/content/Content"
import {BlocksAction} from "./actions/blocks"
import {BlockAction} from "./actions/block"
import {VariableAction} from "./actions/variable"
import {VariablesAction} from "./actions/variables"

const VARIABLES: Variables = {
    "dato": {
        spec: {name: "dato", type: "date"},
        value: "2022-11-23",
    },
    "land": {
        spec: {name: "land", type: "text"},
        value: "Spania",
    },
}

const BLOCKS: AnyBlock[] = [
    {
        type: "header",
        locked: true,
        content: [{type: 'literal', text: "Informasjon om saksbehandlingstiden vår"}],
    }, {
        type: "paragraph",
        locked: true,
        content: [
            {type: 'literal', text: "Vi har "},
            {type: 'variable', name: "dato", text: "11. November 2022"},
            {type: 'literal', text: " mottatt søknaden din om AFP fra trygdemyndighetene i "},
            {type: 'variable', name: "land", text: "Spania"},
            {type: 'literal', text: "."},
        ],
    }, {
        type: "paragraph",
        locked: true,
        content: [{type: 'literal', text: "Vi har 11. November 2022 mottatt søknaden din om AFP fra folketrygden."}],
    }, {
        type: "paragraph",
        locked: true,
        content: [{type: 'literal', text: "Du har ikke høy nok opptjening til å ta ut 50 prosent alderspensjon fra 1. Januar 2023. Eventuelle AFP-rettigheter vil kunne gi deg rett til uttak av alderspensjon."}],
    }, {
        type: "paragraph",
        locked: true,
        content: [{
            type: 'literal',
            text: "Vi vil behandle søknaden din når vi har mottatt informasjon fra Fellesordningen om dine AFP-rettigheter. Det tar normalt fire til seks uker før du får svar fra dem på søknaden din. Dette forutsetter at du har sendt inn søknad om AFP i privat sektor til Fellesordningen.",
        }],
    }, {
        type: "paragraph",
        locked: true,
        content: [{type: 'literal', text: "Saksbehandlingstiden vår er vanligvis 10 uker."}],
    }, {
        type: "paragraph",
        locked: true,
        content: [{type: 'literal', text: "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss."}],
    }, {
        type: "title1",
        locked: true,
        content: [{type: 'literal', text: "Informasjon "}, {type: 'literal', text: "til deg"}],
    },
]

interface AnyBlockProps {
    block: AnyBlock,
    splitBlock: SplitBlockAtContent
    updateBlock: BoundAction<[block: AnyBlock]>
}

const AnyBlock: FC<AnyBlockProps> = ({block, splitBlock, updateBlock}) => {

    const doUnlock = bindAction(BlockAction.unlock, updateBlock, block)
    const updateBlockContent = bindAction(BlockAction.updateBlockContent, updateBlock, block)

    switch (block.type) {
        case 'header':
            return <Header block={block}/>
        case 'title1':
            return <Title1 block={block} doUnlock={doUnlock} updateContent={updateBlockContent} splitContent={splitBlock}/>
        case 'paragraph':
            return <Paragraph block={block} doUnlock={doUnlock} updateContent={updateBlockContent} splitContent={splitBlock}/>
    }
}

interface VariableProps {
    variable: VariableDecl
    updateValue: BoundAction<[value: string]>
}
const Variable: FC<VariableProps> = ({variable, updateValue}) => {
    return (
        <label>
            {variable.spec.name}:
            <input type={variable.spec.type} value={variable.value} onChange={(e) => updateValue(e.target.value)}/>
        </label>
    )
}

const LetterEditor = () => {
    const [blocks, updateBlocks] = useState(BLOCKS)
    const [vars, updateVars] = useState(VARIABLES)
    const updateBlock = bindAction(BlocksAction.updateBlock, updateBlocks, blocks)
    const splitBlock = bindAction(BlocksAction.splitBlock, updateBlocks, blocks)
    const updateVariable = bindAction(
        VariableAction.updateValue,
        combine(
            bindAction(BlocksAction.setVariable, updateBlocks, blocks),
            bindAction(VariablesAction.updateVariable, updateVars, vars),
        )
    )

    return (
        <div className={styles.container}>
            <div>
                {Object.entries(vars).map(([key, v]) =>
                    <Variable key={key} variable={v} updateValue={updateVariable.bind(null, v)}/>
                )}
            </div>
            <div>
                {blocks.map((block, blockId) =>
                    <AnyBlock key={blockId}
                              block={block}
                              splitBlock={splitBlock.bind(null, blockId, block)}
                              updateBlock={updateBlock.bind(null, blockId)}
                    />
                )}
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor