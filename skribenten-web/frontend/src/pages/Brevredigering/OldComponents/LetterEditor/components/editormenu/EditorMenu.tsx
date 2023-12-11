import type { BoundAction } from "../../lib/actions";
import styles from "./EditorMenu.module.css";

export interface EditorMenuProperties {
  switchType: BoundAction<[type: "PARAGRAPH" | "TITLE1" | "TITLE2"]>;
}

const EditorMenu = ({ switchType }: EditorMenuProperties) => {
  return (
    <div className={styles.container}>
      <div className={styles.top} />
      <div className={styles.bottom}>
        <button disabled type="button">
          Angre
        </button>
        <button disabled type="button">
          Gjør om
        </button>
        <div className={styles.space} />
        <button onClick={switchType.bind(null, "TITLE1")} type="button">
          Tittel 1
        </button>
        <button onClick={switchType.bind(null, "TITLE2")} type="button">
          Tittel 2
        </button>
        <button onClick={switchType.bind(null, "PARAGRAPH")} type="button">
          Normal
        </button>
      </div>
    </div>
  );
};

export default EditorMenu;
