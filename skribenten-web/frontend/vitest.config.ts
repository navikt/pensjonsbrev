import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import {fileURLToPath, URL} from "node:url";

export default defineConfig({
    plugins: [react()],
    resolve: {
        alias: {
            "~": fileURLToPath(new URL("src", import.meta.url)),
        },
    },
    test: {
        environment: 'jsdom',
    },
})