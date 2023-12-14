import type { BoundAction } from "../lib/actions";

export type EditorMenuProperties = {
  switchType: BoundAction<[type: "PARAGRAPH" | "TITLE1" | "TITLE2"]>;
};

export const EditorMenu = ({ switchType }: EditorMenuProperties) => {
  return (
    <div>
      <button disabled type="button">
        Angre
      </button>
      <button disabled type="button">
        Gjør om
      </button>
      <div />
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
  );
};
