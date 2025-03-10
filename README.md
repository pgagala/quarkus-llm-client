# Quarkus LangChain4j LLM Client

This is a Quarkus application that communicates with a locally running LLM server using LangChain4j.

## Prerequisites

- JDK 21 or later
- Maven 3.8.1+
- Docker and Docker Compose
- Ollama (if not using Docker)

## Local LLM Setup

### Option 1: Using Docker Compose

The simplest way to set up the local LLM environment is using Docker Compose:

```bash
# Start the local LLM server and the Quarkus application
docker-compose up -d
```

This will start an Ollama server with the Llama3 model and the Quarkus application.

### Option 2: Manual Setup

If you prefer to run Ollama directly on your machine:

1. Install Ollama:
```bash
# On macOS or Linux
curl -fsSL https://ollama.com/install.sh | sh
```

2. Pull the model:
```bash
ollama pull llama3
```

3. Start the Ollama server:
```bash
ollama serve
```

Alternatively, you can use the provided initialization script:

```bash
chmod +x init-local-llm.sh
./init-local-llm.sh
```

## Running the application

You can run the application in development mode:

```bash
./mvnw compile quarkus:dev
```

This will start the application on http://localhost:8080.

## Using the LLM Client

1. Open a web browser and navigate to http://localhost:8080
2. Enter your prompt in the input field
3. Press "Send" or hit Enter
4. The LLM's response will appear in the chat interface

## API Endpoints

The application exposes the following API endpoint:

- `POST /llm/chat` - Send a chat message to the LLM and get a response
    - Request body: `{"message": "Your message here"}`
    - Response: `{"response": "LLM response here"}`

## LangChain4j Features

This application demonstrates several features of LangChain4j:

### Basic LLM Chat

The basic chat endpoint allows simple interactions with the LLM:

```
POST /llm/chat
{"message": "What is Quarkus?"}
```

### Chat with Memory

This endpoint maintains conversation history:

```
POST /llm/advanced/chat-with-memory
{"message": "What is Quarkus?"}
```

Subsequent messages will be processed in the context of the conversation history.

## Customizing LLM Parameters

You can customize the LLM parameters by editing the `application.properties` file:

```
llm.base.url=http://localhost:11434
llm.model.name=llama3
llm.temperature=0.7
llm.timeout.seconds=30
llm.max.tokens=1000
```

## Using Different LLM Models

Ollama supports a variety of models. To use a different model:

1. Pull the model using Ollama:
   ```bash
   ollama pull mistral
   ```

2. Update the model name in `application.properties`:
   ```
   llm.model.name=mistral
   ```

Available models can be found on the [Ollama model library](https://ollama.com/library).

## Packaging and Running in Production

Package the application:

```bash
./mvnw package
```

Run the packaged application:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Creating a Native Executable

You can create a native executable using:

```bash
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can use Docker:

```bash
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Run the native executable:

```bash
./target/quarkus-llm-client-1.0.0-SNAPSHOT-runner
```