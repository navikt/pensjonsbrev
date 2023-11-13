const FULLMAKTSREGISTER_API_BASE_PATH = "/skribenten-backend";

export function App() {
  return <button onClick={testBackend}>Test backend API</button>;
}

const testBackend = async () => {
  // await fetch(`${FULLMAKTSREGISTER_API_BASE_PATH}/pen/sak/22972355`);
  // await fetch(`${FULLMAKTSREGISTER_API_BASE_PATH}/pdl/navn/21119343981`);
  await fetch(`${FULLMAKTSREGISTER_API_BASE_PATH}/kodeverk/kommune`);
  // await fetch(`${FULLMAKTSREGISTER_API_BASE_PATH}/lettertemplates/UFOREP`);
};
