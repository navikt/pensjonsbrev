import subprocess as sp
import json
import base64

set_context_command = "kubectl config use-context dev-gcp"


def get_secrets_name(application_name):
    return run_command(
        f"kubectl get azureapp -n pensjonsbrev {application_name} -o go-template='{'{{.spec.secretName}}'}'"
    ).replace("'", "")


def get_secrets(secret_name):
    return run_command(f"kubectl get secret {secret_name} -n pensjonsbrev -o json")


def run_command(command: str):
    return sp.run(command.split(" "), capture_output=True).stdout.decode("utf-8")


def read_existing_env(file_path):
    with open(file_path, "w+") as env:
        env_vars = [line.split("=", 1) for line in env.read().splitlines()]
        return {key: value for key, value in env_vars}


def base64_decode(value, url=False):
    if url:
        return base64.urlsafe_b64decode(str(value)).decode("utf-8")
    else:
        return base64.b64decode(str(value)).decode("utf-8")


def get_application_secrets(application_name):
    secret_name = get_secrets_name(application_name)
    secrets = get_secrets(secret_name)
    return json.loads(secrets)["data"]


def setup_skribenten_web_bff_secrets():

    env_file_path = ".env"

    print(f"Setting up secrets for {env_file_path}")

    secrets = get_application_secrets("skribenten-web")

    env_object = read_existing_env(env_file_path)

    env_object.update(
        {
            "AZURE_APP_CLIENT_ID": base64_decode(secrets["AZURE_APP_CLIENT_ID"]),
            "AZURE_OPENID_CONFIG_ISSUER": base64_decode(secrets["AZURE_OPENID_CONFIG_ISSUER"], True),
            "AZURE_OPENID_CONFIG_TOKEN_ENDPOINT": base64_decode(secrets["AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"], True),
            "AZURE_APP_WELL_KNOWN_URL": base64_decode(secrets["AZURE_APP_WELL_KNOWN_URL"], True),
            "AZURE_APP_JWK": f"\'{base64_decode(secrets['AZURE_APP_JWK'])}\'",
            "AZURE_OPENID_CONFIG_JWKS_URI": base64_decode(secrets["AZURE_OPENID_CONFIG_JWKS_URI"], True),
        }
    )

    with open(env_file_path, "w+") as env_file:
        env_file.writelines([f"{key}={value}\n" for key, value in env_object.items()])
        print(f"Successfully written new secrets to {env_file_path}")


# Setter riktig context for kubectl
run_command(set_context_command)

setup_skribenten_web_bff_secrets()
