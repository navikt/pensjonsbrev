# Brevoppskriften - Autodokumentasjon for Brevbaker brev

For å bruke npm trenger du ha `node` installert. 
Vi anbefaler at du bruker [asdf](https://asdf-vm.com/) slik at du automatisk kjører nødvendige pakker på støttet versjon.
`asdf` vil sette riktige versjoner for diverse pakker som trengs i dette repoet. Se `.tool-versions` i rot-folderen.

Etter at Node er installert kjører du følgende kommandoer for å starte:

```bash
npm i
npm run dev
```

Da vil Vite sin dev-server kjøre på `localhost:5173`. 
For at API-kall skal fungere lokalt, anbefaler vi at du kjører via BFF.

Kjør docker compose med skribenten profilen for å kjøre opp BFF:
```bash
docker compose --profile skribenten up -d --build
```
Åpne http://localhost:8088/vite-on 