from locust import FastHttpUser, task, between
import azuread
import os
import uuid

thread_safe_token = azuread.ThreadSafeToken()

# Test av synkront kall
payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request.json'), "r").read()
# Test av asynkront kall
# payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request_async.json'), "r").read()

class BrevbakerLoadTest(FastHttpUser):
    wait_time = between(0,0)
    @task
    def load_test(self):
        #Test av synkront kall lokalt:
        #headers = {'Content-Type': 'application/json'}

        # Test av async på clusteret:
#         headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + self.access_token(), 'X-Request-ID': str(uuid.uuid4())}
#         self.client.post("/letter/autobrev/pdfAsync", payload, headers=headers)

        #Test av synkront kall på clusteret:
        headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + self.access_token(), 'X-Request-ID': str(uuid.uuid4())}
        self.client.post("/letter/autobrev/pdf", payload, headers=headers)


    def access_token(self):
        return thread_safe_token.get()