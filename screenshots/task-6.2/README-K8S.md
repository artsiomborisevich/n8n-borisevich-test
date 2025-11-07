# n8n Duty Agent - Kubernetes Monitoring System

**Author:** Artem Borisevich  
**Date:** December 2024  
**Course:** n8n Automation & AI Agents

---

## 📋 Project Overview

This project implements an intelligent Kubernetes monitoring system using n8n workflow automation, AI-powered diagnostics, and k3d cluster management. The system monitors a Spring Boot application deployed in Kubernetes and provides automated alerts with root cause analysis when issues are detected.

### 🎯 Key Features

- ✅ **Real-time K8s Monitoring** - Checks website availability every 5 minutes
- 🤖 **AI-Powered Diagnosis** - Uses Google Gemini to analyze pod failures
- ☸️ **Kubernetes Integration** - Uses community nodes to interact with k8s cluster
- 📱 **Telegram Alerts** - Instant notifications with detailed diagnostics
- 🔧 **Pod Status Analysis** - Monitors CrashLoopBackOff, OOMKilled, and other states
- 👤 **Human-in-the-Loop** - Requests approval before applying fixes

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    k3d Kubernetes Cluster                    │
│                                                              │
│  ┌──────────────────┐         ┌─────────────────────────┐  │
│  │  Spring Boot App │         │   Kubernetes Service    │  │
│  │  (Pod)           │◄────────┤   (NodePort 30080)      │  │
│  │                  │         │                         │  │
│  │  Port 8080       │         └─────────────────────────┘  │
│  │  "SYSTEM ONLINE" │                                      │
│  └──────────────────┘                                      │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ↓ Monitor
          ┌────────────────────────────┐
          │  n8n Workflow Engine       │
          │  (Docker)                  │
          │                            │
          │  ┌──────────────────────┐  │
          │  │ Kubernetes Nodes:    │  │
          │  │ - Get Pods           │  │
          │  │ - Get Logs           │  │
          │  │ - Update Resources   │  │
          │  └──────────────────────┘  │
          │                            │
          │  ┌──────────────────────┐  │
          │  │ Code Node:           │  │
          │  │ - Parse pod status   │  │
          │  │ - Normalize data     │  │
          │  └──────────────────────┘  │
          │                            │
          │  ┌──────────────────────┐  │
          │  │ AI Agent (Gemini)    │  │
          │  └──────────────────────┘  │
          └──────────────┬─────────────┘
                         ↓
                  ┌─────────────┐
                  │  Telegram   │
                  │  Bot Alerts │
                  └─────────────┘
```

---

## 📦 Quick Start

### Prerequisites

- Docker and Docker Compose
- k3d (`choco install k3d` on Windows)
- kubectl (`choco install kubernetes-cli`)
- Telegram account (for alerts)
- Google Gemini API key (for AI diagnostics)

### 1. Start k3d Cluster

```cmd
# Create k3d cluster
k3d cluster create n8n-test-cluster

# Verify cluster is running
kubectl cluster-info
kubectl get nodes
```

### 2. Build and Deploy Application

```cmd
# Build Docker image
docker build -t n8n-borisevich-test_app:latest .

# Import image into k3d
k3d image import n8n-borisevich-test_app:latest -c n8n-test-cluster

# Deploy to Kubernetes
kubectl apply -f n8n-k8s/spring-app-deployment.yaml

# Verify deployment
kubectl get pods
kubectl get services
```

### 3. Start n8n

```cmd
docker-compose -f docker-compose-with-k8s.yml up -d
```

**Services started:**
- **Spring Boot App**: http://localhost:30080 (NodePort)
- **n8n Dashboard**: http://localhost:5678 (admin/admin123)

### 4. Import n8n Workflow

1. Open n8n at http://localhost:5678
2. Login with `admin` / `admin123`
3. Import workflow: [n8n-workflow-task1-ai-k8s-monitoring.json](../../n8n-workflows/task-6.2/n8n-workflow-task1-ai-k8s-monitoring.json)
4. Configure credentials:
   - **Kubernetes**: Upload your kubeconfig file (from `~/.kube/config`)
   - **Telegram Bot**: Bot token and chat ID
   - **Google Gemini**: API key
5. Activate the workflow

### 5. Test the System

```cmd
# Method 1: Delete pod (restarts instantly)
kubectl delete pod <pod-name>

# Method 2: Trigger CrashLoopBackOff (better for testing)
# Update deployment to always fail:
kubectl apply -f n8n-k8s/spring-app-deployment-crash.yaml

# Wait for alert (or trigger manually in n8n)

# Restore normal operation:
kubectl apply -f n8n-k8s/spring-app-deployment.yaml
```

---

## 📚 Implementation Tasks

### Task 6.2: Basic Kubernetes Monitoring ✅

**Objective:** Monitor Spring Boot application in k3d cluster and detect failures

**Implementation:**
- HTTP Request node checks `http://localhost:30080/` every 5 minutes
- IF node validates: Status = 200 AND body contains "SYSTEM ONLINE"
- Kubernetes node retrieves pod information when site is down
- Code node normalizes pod status (Running, CrashLoopBackOff, OOMKilled, etc.)
- Telegram node sends alert with pod details

**Workflow File:** [n8n-workflow-task1-ai-k8s-monitoring.json](../../n8n-workflows/task-6.2/n8n-workflow-task1-ai-k8s-monitoring.json)

**How it works:**
1. Schedule trigger runs every 5 minutes
2. HTTP GET request to NodePort service
3. Check response status and content
4. If down: Query Kubernetes for pod status
5. Parse pod state (waiting, terminated, running)
6. Count container restarts
7. Send Telegram alert with pod diagnostics

**Alert Example:**
```
🚨 Kubernetes Pod Status Alert

📦 Pod Name: n8n-borisevich-test-app-7d9f8c6b5d-x4k2m
📍 Namespace: default
✅ Status: CrashLoopBackOff
🔄 Restarts: 5
```

**Key Kubernetes Features:**
- ✅ Uses community node `n8n-nodes-kubernetes`
- ✅ Secure credential management (kubeconfig)
- ✅ Code node for custom pod status parsing
- ✅ Detects CrashLoopBackOff, OOMKilled, ImagePullBackOff
- ✅ Tracks restart counts

**Screenshots:**
- [AI K8s Workflow](task1-workflow-monitoring.png)
- [AI Diagnosis](task1-telegram-alert-monitoring.png)

---

### Task 6.2: AI-Powered K8s Diagnostics ✅

**Objective:** Add intelligent diagnosis for Kubernetes pod failures

**Implementation:**
- AI Agent (Google Gemini) analyzes pod status and logs
- Code node extracts relevant pod information
- Kubernetes node retrieves pod logs
- AI determines root cause (exit codes, OOM, config issues)
- Provides actionable fix recommendations
**Workflow File:** [n8n-workflow-task2-ai-k8s-recovery.json](../../n8n-workflows/task-6.2/n8n-workflow-task2-ai-k8s-monitoring.json)

**How it works:**
1. HTTP check detects failure
2. Query Kubernetes for pod status
3. Code node normalizes pod data
4. Retrieve pod logs (last 50 lines)
5. AI Agent analyzes:
   - Pod status (CrashLoopBackOff, Error, etc.)
   - Container exit codes
   - Recent logs
   - Restart counts
6. AI provides diagnosis with fix commands
7. Request human approval via Telegram
8. Execute approved fixes

**Alert Example:**
```
🚨 Kubernetes Pod Status Alert

📦 Pod Name: n8n-borisevich-test-app-abc123
📍 Namespace: default
❌ Status: CrashLoopBackOff
🔄 Restarts: 12

🔍 AI DIAGNOSIS:
The pod is stuck in CrashLoopBackOff because the container
is configured to exit immediately with code 1. The deployment
has command: ["/bin/sh", "-c", "exit 1"] which causes the
container to fail on startup.

🎯 ROOT CAUSE:
Invalid container command configured in deployment

🛠️ RECOMMENDED ACTION:
1. Edit deployment: kubectl edit deployment n8n-borisevich-test-app
2. Remove or fix the 'command' and 'args' fields
3. Apply fix: kubectl rollout restart deployment/n8n-borisevich-test-app

📋 Pod Logs:
(Container exits immediately, no logs available)
```

**Key Improvements:**
- ✅ AI analyzes Kubernetes-specific issues
- ✅ Understands pod lifecycle states
- ✅ Interprets container exit codes

**Screenshots:**
- [AI K8s Workflow](task2-workflow-ai-monitoring.png)
- [AI Diagnosis](task2-workflow-ai-alerting.png)

---

## 🚀 Kubernetes Configuration

### Deployment Manifest

**Normal Operation:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: n8n-borisevich-test-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: n8n-borisevich-test-app
  template:
    metadata:
      labels:
        app: n8n-borisevich-test-app
    spec:
      containers:
        - name: n8n-borisevich-test-app
          image: n8n-borisevich-test_app:latest
          imagePullPolicy: Never
          ports:
            - containerPort: 8080
      restartPolicy: Always
      terminationGracePeriodSeconds: 30
```

**CrashLoopBackOff Simulation:**
```yaml
spec:
  template:
    spec:
      containers:
        - name: n8n-borisevich-test-app
          image: n8n-borisevich-test_app:latest
          imagePullPolicy: Never
          command: ["/bin/sh"]
          args: ["-c", "exit 1"]  # Forces immediate failure
      restartPolicy: Always
```

### Service Configuration

```yaml
apiVersion: v1
kind: Service
metadata:
  name: spring-app-service
spec:
  selector:
    app: n8n-borisevich-test-app
  type: NodePort
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      nodePort: 30080  # External access
```

---

## 🧪 Testing Scenarios

### Test 1: Normal Operation

```cmd
# Ensure deployment is healthy
kubectl apply -f n8n-k8s/spring-app-deployment.yaml

# Check pod status
kubectl get pods

# Access website
curl http://localhost:30080/

# Expected: No alert sent
```

### Test 2: CrashLoopBackOff

```cmd
# Apply failing deployment
kubectl apply -f n8n-k8s/spring-app-deployment-crash.yaml

# Watch pod status
kubectl get pods -w

# Trigger n8n workflow (or wait for schedule)
# Expected: Alert with CrashLoopBackOff diagnosis
```

### Test 3: Pod Deletion

```cmd
# Delete pod (forces restart)
kubectl delete pod <pod-name>

# Expected: Brief alert, pod recovers quickly
```


## 🔧 Configuration

### Kubernetes Credentials

n8n needs access to your k3d cluster via kubeconfig:

1. Locate kubeconfig:
   ```cmd
   echo %USERPROFILE%\.kube\config
   ```

2. In n8n:
   - Go to Credentials → New
   - Select "Kubernetes Credentials"
   - Upload kubeconfig file
   - Test connection

## 📚 Documentation

- [n8n Kubernetes Node](https://www.npmjs.com/package/n8n-nodes-kubernetes)
- [k3d Documentation](https://k3d.io/)
- [kubectl Reference](https://kubernetes.io/docs/reference/kubectl/)
- [Kubernetes Pod Lifecycle](https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/)
- [Google Gemini API](https://ai.google.dev/docs)

---