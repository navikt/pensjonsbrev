# Pensjonsbrev
This is a mono-repo for the microservices that together form the new letter ordering system.


## Lokal kjøring av brevbaker og pdf-bygger

For å kjøre løsningen lokalt må man ha docker og docker compose installert.
Kjør `docker compose up -d` i rot-katalogen til prosjektet.

### Debugge pdf-bygger lokalt
Pdf-byggeren er avhengig av ulike pakker for å kompilere LaTeX til pdf.
Derfor må man kjøre pdf-bygger java applikasjonen inne i containeren for at den skal fungere.
For å debugge pdf-byggeren må man derfor sette opp remote debug mot applikasjonen som kjører inne i containeren.

1. Lag en ny run configuration i Intellij av typen Dockerfile.
   * Sett bind ports til `8081:8080 5005:5005`
   * Sett en environment variabel `JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005`
     Om denne verdien er forskjellig i neste steg, erstatt den.
   
2. Lag en ny run configuration av typen remote jvm debug. Under before launch legg til "Run another configuration" og velg
    docker compose konfigurasjonen vi lagde i forrige steg. 
3. Kjør remote jvm debug konfigurasjonen.


### Ytelsestesting med locust

1. Lag en locustfile.py i rot-katalogen(sammen med docker-compose.yml) og legg inn requesten du ønsker å bruke i ytelses-testen.


<details>
<summary>Locustfile.py eksempel</summary>

```
from locust import HttpUser, task, between


class HelloWorldUser(HttpUser):
    wait_time = between(1, 60) #vent mellom 1 og 60 sekunder på respons
    @task
    def hello_world(self):
        payload = 'JSON string' #erstatt med json string som settes som body i request

        headers = {'content-type': 'application/json'}
        r = self.client.post("/letter", payload, headers={'content-type': 'application/json'})
```
</details>

2. Start docker compose med locust profil `docker compose --profile locust up`
3. Gå inn på locust grensesnittet via http://localhost:8089/ og skriv inn url til endepunktet du ønsker å ytelses-teste.
[Se dokumentasjon fra locust for mer info om bruk.](http://docs.locust.io/en/stable/quickstart.html#locust-s-web-interface)
