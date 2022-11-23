import Title1 from "./components/title1/Title1";
import Paragraph from "./components/paragraph/Paragraph";
import Header from "./components/header/Header";
import styles from "./LetterEditor.module.css"
import {FC, useState} from "react";
import {SplitContent} from "./components/content/Content";
import {AnyBlock, Block, TextContent, VariableDecl} from "./model";
import {Mapper, updateByIndex, Updater, updateWithPartial} from "../../lib/state";

interface Variables {
    [name: string]: VariableDecl
}

const VARIABLES: Variables = {
    "dato": {
        spec: {name: "dato", type: "datetime"},
        value: "2022-11-23",
    },
    "land": {
        spec: {name: "land", type: "string"},
        value: "Norge",
    }
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
            {type: 'literal', text: " mottatt søknaden din "},
            {type: 'literal', text: "om AFP fra trygdemyndighetene i Spania."},
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
            text: "Vi vil behandle søknaden din når vi har mottatt informasjon fra Fellesordningen om dine AFP-rettigheter. Det tar normalt fire til seks uker før du får svar fra dem på søknaden din. Dette forutsetter at du har sendt inn søknad om AFP i privat sektor til Fellesordningen."
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
    }
]

interface AnyBlockProps {
    blockId: BlockId
    block: AnyBlock
    updateBlocks: UpdateBlocks
}

const AnyBlock: FC<AnyBlockProps> = ({blockId, block, updateBlocks}) => {
    // const updBlock = updateBlock.bind(null, updateBlocks, blockId)
    const updBlock = (mapper: Updater<AnyBlock>) => updateBlocks(blocks => updateByIndex(blocks, blockId, mapper))
    const doUnlock = () => updBlock(b => ({...b, locked: false}))
    const updateContent = (contentId: number, mapper: Updater<TextContent>) => updBlock(b => ({...b, content: updateByIndex(b.content, contentId, mapper)}))

    const splitContent: SplitContent = (contentId) => (currentText, nextText) => {
        const content = block.content[contentId]
        const currentBlock: AnyBlock = {
            ...block,
            content: [...block.content.slice(0, contentId), {...content, text: currentText}]
        }
        const nextBlock: AnyBlock = {
            ...block,
            content: [{...content, text: nextText}, ...block.content.slice(contentId + 1)]
        }
        updateBlocks((blocks) => [...blocks.slice(0, blockId), currentBlock, nextBlock, ...blocks.slice(blockId + 1)])
    }

    switch (block.type) {
        case 'header':
            return <Header block={block}/>
        case 'title1':
            return <Title1 block={block} doUnlock={doUnlock} updateContent={updateContent} splitContent={splitContent}/>
        case 'paragraph':
            return <Paragraph block={block} doUnlock={doUnlock} updateContent={updateContent} splitContent={splitContent}/>
    }
}

const Variable: FC<VariableDecl & { onChange: (value: any) => void }> = ({spec, value, onChange}) => {
    return (
        <label>
            {spec.name}:
            <input type="date" value={value} onChange={(e) => onChange({spec, value: e.target.value})}/>
        </label>
    )
}


type BlockId = number
type UpdateContent = (updContent: (content: TextContent) => TextContent) => void
type UpdateBlock = (updBlock: (block: AnyBlock) => AnyBlock) => void
type UpdateBlocks = (updBlocks: (blocks: AnyBlock[]) => AnyBlock[]) => void


function updateBlock(updateBlocks: UpdateBlocks, id: BlockId, mapBlock: Updater<AnyBlock>) {
    updateBlocks(blocks => updateByIndex(blocks, id, mapBlock))
}

function updateContent(updateBlock: UpdateBlock, id: number, mapContent: Updater<TextContent>) {
    updateBlock(block => ({...block, content: updateByIndex(block.content, id, mapContent)}))
}

const LetterEditor = () => {
    const [blocks, setBlocks] = useState(BLOCKS)
    const [vars, setVars] = useState(VARIABLES)
    const updateVar = (v: VariableDecl) => {
        setVars(
            {
                ...vars, [v.spec.name]: v
            }
        )
        setBlocks(
            blocks.map(b => (
                {
                    ...b,
                    content: b.content.map(c => {
                        if (c.type === 'variable' && c.name === v.spec.name) {
                            return {...c, text: v.value}
                        } else {
                            return c
                        }
                    })
                }
            ))
        )
    }
    return (
        <div className={styles.container}>
            <div>
                {Object.entries(vars).map(([key, v]) =>
                    <Variable key={key} spec={v.spec} value={v.value} onChange={updateVar}/>
                )}
            </div>
            <div>
                {blocks.map((block, blockId) =>
                    <AnyBlock key={blockId}
                              blockId={blockId}
                              block={block}
                              updateBlocks={(updBlocks) => setBlocks(updBlocks(blocks))}
                    />
                )}
            </div>
            <button onClick={() => console.log(blocks)}>Save</button>
        </div>
    )
}

export default LetterEditor