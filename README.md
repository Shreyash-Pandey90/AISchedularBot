# AI-Powered Interview Scheduling Bot

## Overview
The **AI-Powered Interview Scheduling Bot** automates interview scheduling by checking recruiter availability and scheduling interviews accordingly. It integrates AI-driven NLP (using spaCy) to interact with candidates via a chatbot. The system supports OAuth2 authentication with JWT and ensures secure login for both recruiters and candidates.

## Features
- **User Authentication:** Supports login via email and Google OAuth2 authentication.
- **Chatbot Interaction:** Candidates can interact with the chatbot to request an interview.
- **Automated Scheduling:** Interviews are scheduled automatically if the recruiter’s slot is available.
- **Email Notifications:** Both recruiter and candidate receive email notifications upon scheduling.
- **Dashboard:** Displays upcoming meetings for the candidate and recruiter.
- **AI-powered NLP:** Utilizes **spaCy** for natural language processing.

## Tech Stack
### Backend:
- **Spring Boot** (Authentication, Authorization, and Business Logic)

### Frontend:
- **React.js** with **Material UI** (User Interface)

### Database:
- **MongoDB** 

### AI/NLP:
- **spaCy** (Used for chatbot conversation handling)

## Installation & Setup
### Prerequisites:
Ensure you have the following installed:
- **Java 17+** (for Spring Boot backend)
- **Node.js 16+** (for React frontend)
- **MongoDB** (for database storage)
- **Python 3+** (for spaCy chatbot)

### Backend Setup:
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ai-scheduling-bot.git
   cd ai-scheduling-bot/backend
