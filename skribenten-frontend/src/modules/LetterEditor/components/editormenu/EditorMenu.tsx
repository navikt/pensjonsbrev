import {FC} from "react"
import styles from "./EditorMenu.module.css"
import {BoundAction} from "../../../../lib/actions"

export interface EditorMenuProps {
    switchType: BoundAction<[type: "PARAGRAPH" | "TITLE1"]>
}

const EditorMenu: FC<EditorMenuProps> = ({switchType}) => {
    return (
        <div className={styles.container}>
            <div className={styles.top}/>
            <div className={styles.bottom}>
                <button type="button">Angre</button>
                <button type="button">Gjør om</button>
                <div className={styles.space}/>
                <button type="button" onClick={switchType.bind(null, "TITLE1")}>Tittel</button>
                <button type="button" onClick={switchType.bind(null, "PARAGRAPH")}>Normal</button>
            </div>
        </div>
    )
}

export default EditorMenu