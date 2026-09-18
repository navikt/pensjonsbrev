import { type WorkerRequest, type WorkerResponse } from "~/search/searchProtocol";
import { createSearchWorkerCore } from "~/search/searchWorkerCore";

/** The worker global, typed at the message boundary only. Pulling in the
 *  `webworker` lib instead would add it to the whole program, which clashes
 *  with the `DOM` lib this project compiles against (`tsconfig.json`). */
const ctx = globalThis as unknown as {
  addEventListener: (type: "message", listener: (event: MessageEvent<WorkerRequest>) => void) => void;
  postMessage: (message: WorkerResponse) => void;
};

const core = createSearchWorkerCore();

ctx.addEventListener("message", (event) => {
  ctx.postMessage(core.handle(event.data));
});
