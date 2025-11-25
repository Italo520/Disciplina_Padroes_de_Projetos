#!/bin/bash

# Script de instalação do Docker para Ubuntu/Debian
# Execute com: bash install-docker.sh

set -e

echo "========================================"
echo "  Instalação do Docker + Docker Compose"
echo "========================================"
echo ""

# Verifica se está rodando como root
if [ "$EUID" -eq 0 ]; then 
    echo "❌ NÃO execute este script como root (sudo)!"
    echo "Execute sem sudo: bash install-docker.sh"
    exit 1
fi

echo "📦 Atualizando repositórios..."
sudo apt update

echo "📦 Instalando dependências..."
sudo apt install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

echo "🔑 Adicionando chave GPG do Docker..."
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo "📝 Adicionando repositório do Docker..."
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

echo "📦 Atualizando repositórios novamente..."
sudo apt update

echo "🐳 Instalando Docker Engine..."
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "👤 Adicionando usuário $USER ao grupo docker..."
sudo usermod -aG docker $USER

echo "✅ Docker instalado com sucesso!"
echo ""
echo "📋 Versões instaladas:"
docker --version
docker compose version

echo ""
echo "⚠️  IMPORTANTE:"
echo "1. Você precisa SAIR e ENTRAR novamente na sessão para as permissões terem efeito"
echo "   Ou execute: newgrp docker"
echo ""
echo "2. Após reiniciar a sessão, teste com:"
echo "   docker run hello-world"
echo ""
echo "3. Depois, volte para este projeto e execute:"
echo "   ./docker-dev.sh up"
echo ""
