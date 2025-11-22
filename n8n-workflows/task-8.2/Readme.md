# Task 8.2: Building a Custom RAG Pipeline

## Objective
The goal of this task is to build your own Retrieval-Augmented Generation (RAG) pipeline. You can use any data source: technical documentation, configuration files, logs—anything that interests you.

## Requirements
- **Choose a suitable chunking strategy**: Base it on the type of data in your knowledge base.
- **Build a full RAG pipeline**: Loading → Chunking → Embeddings → Vector Storage → Search → Response Generation.
- **Implementation Options**: Your imagination is completely free. Choose the path that suits you best:
  - Repeat the step-by-step pipeline from the webinar (RAG chatbot on documentation in n8n).
  - Build your own RAG in n8n, using the principles discussed in the lecture part.
  - For hardcore enthusiasts: Program RAG using one of the AI frameworks (LangChain, LlamaIndex, or any other of your choice).

## Files in This Folder
- `n8n-workflow-with-rag.json`: The n8n workflow JSON file implementing the RAG pipeline.
- `workflow.png`: A screenshot or diagram of the workflow.
- `Readme.md`: This file, describing the task and contents.

## How to Use
1. Import the `n8n-workflow-with-rag.json` into your n8n instance.
2. Ensure all necessary nodes and credentials are configured.
3. Run the workflow to test the RAG functionality.

For more details on RAG and n8n, refer to the webinar or lecture materials.
