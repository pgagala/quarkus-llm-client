#!/bin/bash

# Script to initialize the local LLM server with Ollama

# Check if Ollama is installed
if ! command -v ollama &> /dev/null; then
    echo "Ollama is not installed. Installing Ollama..."
    curl -fsSL https://ollama.com/install.sh | sh
    echo "Ollama installed successfully."
fi

# Pull the LLM model (Llama3 in this case)
echo "Pulling Llama3 model (this may take some time depending on your internet connection)..."
ollama pull llama3

echo "Local LLM setup complete!"
echo "You can now run your Quarkus application that will communicate with this local LLM."
echo "To start Ollama, run: ollama serve"
