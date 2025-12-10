#!/bin/bash

echo "=================================================="
echo "      ToDoList - Execução de Testes e Cobertura   "
echo "=================================================="

# Garante que o script pare se houver erro
set -e

echo "1. Limpando projeto e compilando..."
mvn clean compile

echo "2. Executando Testes Unitários e de Integração..."
# O perfil 'all-tests' ativa surefire e failsafe. O perfil 'coverage' ativa o JaCoCo.
mvn verify -P all-tests,coverage

echo "=================================================="
echo "      Relatório de Cobertura Gerado com Sucesso!  "
echo "=================================================="
echo "Acesse o relatório em: target/site/jacoco/index.html"
