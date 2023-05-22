import {FC} from "react"
import styles from "./EditorMenu.module.css"
import {BoundAction} from "../../../../lib/actions"

export interface EditorMenuProps {
    switchType: BoundAction<[type: "PARAGRAPH" | "TITLE1"| "TITLE2"]>
}

const EditorMenu: FC<EditorMenuProps> = ({switchType}) => {
    return (
        <div className={styles.container}>
            <div className={styles.top}/>
            <div className={styles.bottom}>
                <button type="button" disabled={true}>Angre</button>
                <button type="button" disabled={true}>Gjør om</button>
                <div className={styles.space}/>
                <button type="button" onClick={switchType.bind(null, "TITLE1")}>Tittel 1</button>
                <button type="button" onClick={switchType.bind(null, "TITLE2")}>Tittel 2</button>
                <button type="button" onClick={switchType.bind(null, "PARAGRAPH")}>Normal</button>
            </div>
        </div>
    )
}

export default EditorMenu