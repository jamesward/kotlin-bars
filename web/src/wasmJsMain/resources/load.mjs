import { instantiate } from './kotlin-bars-web-wasm-js.uninstantiated.mjs';

await wasmSetup;

instantiate({ skia: Module['asm'] });