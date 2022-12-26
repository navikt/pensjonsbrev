import '../../styles/globals.css'
import type {AppContext, AppProps} from 'next/app'
import App from 'next/app'
import {MsalProvider} from "@azure/msal-react"
import {AzureConfig, createMsal} from "../lib/services/msal"
import {SkribentenAPIConfig} from "../lib/services/skribenten"

function SkribentenApp({Component, pageProps}: AppProps<SkribentenConfig>) {
    return (
        <MsalProvider instance={createMsal(pageProps.azure)}>
            <Component {...pageProps} />
        </MsalProvider>
    )
}

export interface SkribentenConfig {
    azure: AzureConfig
    api: SkribentenAPIConfig
}

SkribentenApp.getInitialProps = async (appContext: AppContext) => {
    const appProps = await App.getInitialProps(appContext)
    return {
        ...appProps,
        pageProps: {
            azure: {
                clientId: process.env.AZURE_APP_CLIENT_ID,
                tenantId: process.env.AZURE_APP_TENANT_ID,
            },
            api: {
                url: process.env.SKRIBENTEN_API_URL,
                scope: process.env.SKRIBENTEN_API_SCOPE,
            },
        },
    }
}

export default SkribentenApp
