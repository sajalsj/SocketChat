# 🤖 Real-Time ChatBot Interface

A single-screen Android chat application built using **MVVM + Clean Architecture** that supports **real-time messaging via WebSocket** and **offline message queuing with auto-retry**.

---

## 🚀 Features

- 🔌 **WebSocket-based real-time messaging**
- 📶 **Offline message queue** (Room DB + auto resend)
- 💬 **Multiple bots**: SupportBot, SalesBot, FAQBot
- 📜 **Conversation list** with last message preview and unread count
- 🧠 **Persistent chat history per bot**
- 🧱 MVVM + Repository pattern for clean separation
- ✨ ViewBinding & Lifecycle-aware architecture
- ✅ Built with Kotlin, Retrofit, Room, OkHttp, Coroutine, LiveData


---

## 🧱 Architecture

---

## 💻 Tech Stack

| Layer           | Library                |
|----------------|------------------------|
| Networking      | OkHttp WebSocket       |
| Database        | Room                   |
| Architecture    | MVVM, Repository       |
| Concurrency     | Kotlin Coroutines      |
| UI              | ViewBinding, RecyclerView |
| Offline Support | Room + Retry Logic     |

---
