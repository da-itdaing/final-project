/**
 * Minimal ESLint flat config for TypeScript/React (Vite)
 * Adjust rules/plugins as the project evolves.
 */

/** @type {import('eslint').Linter.Config[]} */
module.exports = [
  { ignores: ['dist', 'build', 'node_modules'] },
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      parser: require('@typescript-eslint/parser'),
      ecmaVersion: 'latest',
      sourceType: 'module',
      globals: {
        window: 'readonly',
        document: 'readonly',
        navigator: 'readonly'
      }
    },
    plugins: {
      '@typescript-eslint': require('@typescript-eslint/eslint-plugin')
    },
    rules: {}
  }
];
