import { sveltekit } from '@sveltejs/kit/vite';
import { enhancedImages } from '@sveltejs/enhanced-img';
import path from 'path';
import { defineConfig } from 'vite';
import pkg from './package.json' assert { type: 'json' };

export default defineConfig({
	plugins: [enhancedImages(), sveltekit()],
	define: {
		__NAME__: `"${pkg.name}"`,
		__VERSION__: `"${pkg.version}"`
	},
	resolve: {
		alias: {
			$lib: path.resolve('./src/lib')
		}
	}
});
