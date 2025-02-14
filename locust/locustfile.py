from locust import FastHttpUser, task, between
import azuread
import os
import uuid

# Test av synkront kall
#payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request.json'), "r").read()
# Test av asynkront kall
payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request_async.json'), "r").read()

class BrevbakerLoadTest(FastHttpUser):
    token = azuread.fetch_token()
    wait_time = between(0,0)
    @task
    def load_test(self):
        #Test av synkront kall lokalt:
        #headers = {'Content-Type': 'application/json'}

        #Test av asynkront kall lokalt:
        headers = {'Content-Type': 'application/json'}

        #Test av synkront kall på clusteret:
        #headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + self.access_token(), 'X-Request-ID': str(uuid.uuid4())}

        # Test av async på clusteret:
        #headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + self.access_token(), 'X-Request-ID': str(uuid.uuid4()), 'orderId': str(uuid.uuid4())}
        self.client.post("/letter/autobrev/pdfAsync", payload, headers=headers)

    def access_token(self):
        return self.token.token
        # Kjent feil. Etter token løper ut etter en time prøver alle workers å fetche token samtidig
        # Kan vi få en uavhengig oppgave med refresh token som oppdaterer denne løpende?
        # mistenker at mange feiler fordi mange forsøker å fetche token samtidig. De som ikke får token dør.
        #if self.token.is_valid():
        #    return self.token.token
        #else:
        #    self.token = azuread.fetch_token()
        #    return self.token.token