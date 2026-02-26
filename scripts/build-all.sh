#!/usr/bin/env bash
# Build de todos os microserviços + layout (ordem: remotes primeiro, layout por último).
# Executar na raiz do projeto (pasta que contém layout/ e os *-module/).

set -e

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

echo "=== Build em: $ROOT ==="

# Remotes (microserviços)
MODULES=(
  "estado-module/frontend"
  "tipo-comodo-module/frontend"
  "tipo-orgao-module/frontend"
  "orgao-empresa-module/frontend"
  "interessado-module/frontend"
  "atualizacao-module/frontend"
  "status-module/frontend"
  "situacao-ocupante-module/frontend"
  "situacao-juridica-module/frontend"
)

for dir in "${MODULES[@]}"; do
  if [ -d "$dir" ]; then
    echo ">>> Build: $dir"
    (cd "$dir" && npm ci --no-audit --no-fund && npm run build)
  else
    echo ">>> Pulando (não encontrado): $dir"
  fi
done

# Layout por último (depende das URLs dos remotes no .env)
echo ">>> Build: layout"
(cd layout && npm ci --no-audit --no-fund && npm run build)

echo "=== Build concluído ==="
