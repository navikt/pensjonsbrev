import LetterEditor from "../modules/LetterEditor/LetterEditor"

// const RedigerBrev: NextPage = () => {
function RedigerBrev() {
    // const [editor, setEditor] = useState<EditorJS>()
    // useEffect(() => {
    //     console.log(typeof window)
    //     setEditor(new EditorJS(editorContainerId))
    // })
    // useEffect(() => new EditorJS(editorContainerId))
    // const editor = new EditorJS(editorContainerId)
    //            <EditorJs value={{blocks: blocks}} placeholder={"heisann!!"}/>
    return (
        <div style={{ display: 'flex', flexDirection: 'column'}}>
            <LetterEditor />
        </div>
    )
}

export default RedigerBrev