const FULLMAKTSREGISTER_API_BASE_PATH = "/skribenten-backend";

export function App() {
  return <button onClick={testBackend}>Test backend API</button>;
}

const testBackend = async () => {
  await fetch(`${FULLMAKTSREGISTER_API_BASE_PATH}/pen/sak/22972355`);
};
