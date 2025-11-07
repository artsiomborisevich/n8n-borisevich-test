FROM n8nio/n8n:1.117.3
USER root

# Install Docker CLI and kubectl
RUN apk update && apk add --no-cache \
    docker-cli \
    curl \
    && curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
    && chmod +x kubectl \
    && mv kubectl /usr/local/bin/kubectl \
    && kubectl version --client

USER node