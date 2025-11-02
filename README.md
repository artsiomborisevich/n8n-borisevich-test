# n8n Duty Agent - Intelligent Website Monitoring System

**Author:** Artem Borisevich  
**Date:** November 2025  
**Course:** n8n Automation & AI Agents

---

## 📋 Project Overview

This project implements an intelligent website monitoring system using n8n workflow automation, AI-powered diagnostics, and Docker container management. The system monitors a Spring Boot application and provides automated alerts with root cause analysis when issues are detected.

### 🎯 Key Features

- ✅ **Real-time Monitoring** - Checks website availability every 5 minutes
- 🤖 **AI-Powered Diagnosis** - Uses Google Gemini to analyze failures and provide root cause
- 🐳 **Docker Integration** - AI agent can execute Docker commands to investigate issues
- 📱 **Telegram Alerts** - Instant notifications with detailed diagnostics
- 🔧 **Self-Healing Recommendations** - Provides exact commands to fix issues

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker Environment                        │
│                                                              │
│  ┌──────────────────┐         ┌─────────────────────────┐  │
│  │  Spring Boot App │◄────────┤   n8n Workflow Engine   │  │
│  │  (Port 8080)     │ Monitor │   (Port 5678)           │  │
│  │                  │         │                         │  │
│  │  "SYSTEM ONLINE" │         │  ┌──────────────────┐   │  │
│  │  Website         │         │  │  AI Agent with   │   │  │
│  └──────────────────┘         │  │  Docker Tools    │   │  │
│                               │  └──────────────────┘   │  │
│                               │          ↕               │  │
│                               │  ┌──────────────────┐   │  │
│                               │  │ Google Gemini AI │   │  │
│                               │  └──────────────────┘   │  │
│                               └──────────┬──────────────┘  │
└────────────────────────────────────────┼──────────────────┘
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
- Telegram account (for alerts)
- Google Gemini API key (for AI diagnostics)

### 1. Start the System

```cmd
docker-compose up -d
```

**Services started:**
- **Spring Boot App**: http://localhost:8080
- **n8n Dashboard**: http://localhost:5678 (admin/admin123)

### 2. Import n8n Workflow

1. Open n8n at http://localhost:5678
2. Login with `admin` / `admin123`
3. Import workflow from `n8n-workflows/` folder
4. Configure credentials (Telegram + Google Gemini)
5. Activate the workflow

### 3. Test the System

```cmd
# Stop the container to trigger alert
docker stop n8n-borisevich-test-app

# Wait for alert (or trigger manually in n8n)

# Start container again
docker start n8n-borisevich-test-app
```

---

## 📚 Three Implementation Tasks

### Task 1: Basic Monitoring ✅

**Objective:** Detect when website goes down and send simple alerts

**Implementation:**
- HTTP Request node checks `http://app:8080/` every 5 minutes
- IF node validates: Status = 200 AND body contains "SYSTEM ONLINE"
- Telegram node sends alert if check fails

**Workflow File:** [`n8n-workflows/n8n-workflow-template-basic.json`](./n8n-workflows/n8n-workflow-template-basic.json)

**How it works:**
1. Schedule trigger runs every 5 minutes
2. HTTP GET request to website
3. Check response status and content
4. Send Telegram alert if down

**Alert Example:**
```
🚨 ALERT: Website is DOWN!

Time: 2025-11-01T15:23:37.149+03:00
Status: 200
URL: http://app:8080/

Expected keyword "SYSTEM ONLINE" was not found or site is unreachable.
```

**Screenshots:**
- [Basic Workflow](screenshots/task1-basic-workflow.png)
- [Telegram Alert](screenshots/task1-basic.png)
- [Website Running](screenshots/site-is-up.png)

---

### Task 2: AI-Powered Diagnostics ✅

**Objective:** Add intelligent diagnosis - explain WHY the site is down

**Implementation:**
- AI Agent (Google Gemini) with Docker command execution tools
- Collects container status, logs, and resource usage
- Analyzes all data to determine root cause
- Provides actionable fix recommendations

**Workflow File:** [`n8n-workflows/n8n-workflow-task2-ai-debugging.json`](./n8n-workflows/n8n-workflow-task2-ai-debugging.json)

**How it works:**
1. HTTP check detects failure (timeout, error, wrong content)
2. AI Agent receives error details
3. AI uses Docker tool to investigate:
   - `docker ps -a` - Check container status
   - `docker logs --tail=50` - Review recent logs
   - `docker inspect` - Get detailed info
4. AI analyzes all data and provides diagnosis
5. Enhanced Telegram alert with:
   - Root cause explanation
   - Exact commands to fix
   - Docker commands AI executed

**Alert Example:**
```
🚨 ALERT: Website is DOWN!

🌐 URL: http://app:8080/
📊 HTTP Status: DOWN

🔍 AI DIAGNOSIS:
HTTP request timed out with ECONNABORTED. Container check shows 
that the "n8n-borisevich-test-app" container is stopped (Exited 
(143) 20 hours ago). The recent logs indicate the Spring Boot 
application started successfully and Tomcat initialized on port 
8080 before the container exited.

🎯 ROOT CAUSE:
The Docker container "n8n-borisevich-test-app" is not running.

🛠️ RECOMMENDED ACTION:
Start the container: docker start n8n-borisevich-test-app

📋 Recent Logs:
...Spring Boot application logs...

💡 This is an automated AI analysis with adaptive investigation.
```

**Key Improvements over Task 1:**
- ✅ Explains WHY site is down (not just that it's down)
- ✅ Analyzes Docker container state
- ✅ Reviews application logs
- ✅ Provides exact fix commands
- ✅ Adapts investigation based on symptoms

**Screenshots:**
- [AI-Enhanced Workflow](screenshots/task2-aidebug-workflow.png)
- [AI Diagnosis Alert](screenshots/task2-ai-debug.png)

#### Scenario B: Website Down (Container Stopped)
```cmd
docker stop n8n-borisevich-test-app
```

**Expected Behavior:**
- ❌ HTTP request times out (ECONNABORTED)
- 🤖 AI investigates with Docker commands
- 📊 AI finds container stopped with exit code 143
- 📝 AI analyzes logs
- 📱 Telegram alert sent with full diagnosis

**Proof:**
- [Container Stopped](./screenshots/container-stopped.png)
- [n8n Execution with Failure](./screenshots/n8n-execution-failure.png)
- [Telegram Alert Received](./screenshots/task2-telegram-ai-diagnosis.png)

---

### Task 3: AI-Powered Recovery 

**Objective:** Add intelligent recovery - recover site AFTER human approval

**Implementation:**
- AI Agent (Google Gemini) with Docker command execution tools
- Collects container status, logs, and resource usage
- Analyzes all data to determine root cause
- Provides actionable fix recommendations 
- Human-in-the-loop approval before executing fixes
- Executes recovery commands upon approval
- Check if site is back up 


**Screenshots:**
- [AI-Enhanced Recovery Workflow](screenshots/task3-recovery.png)
- [Human approval](screenshots/task3-approval.png)
- [App is up](screenshots/task3-app-is-up.png)
- [Site works](screenshots/site-is-up.png)


## 🚀 Application Endpoints

| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `/` | GET | Homepage | HTML with "SYSTEM ONLINE" |
| `/api/health` | GET | Health check | JSON health status |
| `/api/info` | GET | App information | Java version, build info |

**Example Responses:**

```bash
# Homepage
curl http://localhost:8080/
# Returns: HTML page with "SYSTEM ONLINE" in green

# Health Check
curl http://localhost:8080/api/health
# Returns: {"status": "UP"}

# App Info
curl http://localhost:8080/api/info
# Returns: {"java": "21", "spring": "3.3.5", ...}
```

---

## 🧪 Testing & Validation

### Manual Testing

**Test 1: Normal Operation**
```cmd
# Ensure container is running
docker start n8n-borisevich-test-app

# Check website
curl http://localhost:8080/

# Wait for next n8n check (or trigger manually)
# Expected: No alert sent
```

**Test 2: Container Stopped**
```cmd
# Stop the container
docker stop n8n-borisevich-test-app

# Trigger n8n workflow manually
# Expected: Telegram alert with diagnosis
```

**Test 3: AI Investigation**
```cmd
# Stop container
docker stop n8n-borisevich-test-app

# In n8n, watch workflow execution
# Expected: 
#   - HTTP request times out
#   - AI agent executes docker ps
#   - AI agent executes docker logs
#   - AI provides root cause analysis
```

## 🔧 Configuration

### Environment Variables

```yaml
# docker-compose.yml
services:
  n8n:
    environment:
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=admin
      - N8N_BASIC_AUTH_PASSWORD=admin123
      - GENERIC_TIMEZONE=Europe/Moscow
```

### n8n Workflow Configuration

**Required Credentials:**

1. **Telegram Bot**
   - Create bot via @BotFather
   - Get Bot Token
   - Get Chat ID via @userinfobot

2. **Google Gemini API**
   - Get API key from https://aistudio.google.com/app/apikey
   - Configure in n8n Google Gemini Chat Model node

---

## 📞 Support & Documentation

- [n8n Documentation](https://docs.n8n.io/)
- [Google Gemini API Docs](https://ai.google.dev/docs)
- [Telegram Bot API](https://core.telegram.org/bots/api)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Docker Documentation](https://docs.docker.com/)

---

