import "@navikt/ds-css";

import React from "react";
import ReactDOM from "react-dom/client";

import { App } from "./App";

ReactDOM.createRoot(document.querySelector("#root") as HTMLElement).render(
  <React.StrictMode>
    <div>
      <App />
    </div>
  </React.StrictMode>,
);
