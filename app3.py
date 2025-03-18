import os
from authlib.integrations.flask_client import OAuth
from flask import Flask, render_template, request, redirect, url_for, session
import datetime
from google.oauth2.credentials import Credentials
from google.auth.transport.requests import Request
from googleapiclient.discovery import build
from pymongo import MongoClient
import spacy
from dotenv import load_dotenv
from pytz import timezone
import dateparser
import re
from datetime import datetime, timedelta
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email import encoders
import autogen
import pytz

# Load environment variables from .env file
load_dotenv()

# Flask app setup
app = Flask(__name__)
app.secret_key = os.getenv('FLASK_SECRET_KEY', os.urandom(24))
app.config['SESSION_COOKIE_NAME'] = 'google_auth_session'

# Google OAuth setup
SCOPES = [
    'https://www.googleapis.com/auth/calendar',
    'https://www.googleapis.com/auth/userinfo.email'
]

oauth = OAuth(app)
oauth.register(
    name='google',
    client_id=os.getenv('CLIENT_ID'),
    client_secret=os.getenv('CLIENT_SECRET'),
    authorize_url='https://accounts.google.com/o/oauth2/auth',
    access_token_url='https://accounts.google.com/o/oauth2/token',
    client_kwargs={
        'scope': SCOPES,
        'access_type': 'offline',
        'prompt': 'consent',
    },
    server_metadata_url='https://accounts.google.com/.well-known/openid-configuration'
)

# SpaCy NLP Setup
nlp = spacy.load("en_core_web_sm")

# MongoDB Setup
client = MongoClient('mongodb://localhost:27017/')
db = client['scheduling_bot']
print("Connected to MongoDB")
candidate_responses_collection = db['candidate_responses']
print("Stored the responses")

# Define recruiter email addresses
RECRUITER_EMAILS = ['shreyapunati2004@gmail.com', 'recruiter2@gmail.com', 'recruiter3@gmail.com']

# Email Configuration
SENDER_EMAIL = os.getenv("SENDER_EMAIL")
SENDER_PASSWORD = os.getenv("SENDER_PASSWORD")

# Function to convert local time to UTC format for .ics file
def convert_to_utc(date_str, time_str, timezone_str="Asia/Kolkata"):
    try:
        local = pytz.timezone(timezone_str)
        naive_dt = datetime.strptime(f"{date_str} {time_str}", "%d-%m-%Y %H:%M")
        local_dt = local.localize(naive_dt)
        utc_dt = local_dt.astimezone(pytz.utc)
        return utc_dt.strftime("%Y%m%dT%H%M%SZ")
    except Exception as e:
        print(f"❌ Time conversion failed: {e}")
        return None

# Email Sending Function with Calendar Invite
def send_email_with_invite(to_email, subject, body, start_time, end_time, date, location="Online"):
    try:
        msg = MIMEMultipart()
        msg["From"] = SENDER_EMAIL
        msg["To"] = to_email
        msg["Subject"] = subject

        # Add email body with an accept button
        accept_url = f"http://127.0.0.1:5000/accept_invite?candidate_email={to_email}"
        full_body = f"""
        <p>{body}</p>
        <p><strong>Date:</strong> {date}</p>
        <p><strong>Time:</strong> {start_time}</p>
        <p><strong>Location:</strong> {location}</p>
        <br>
        <a href="{accept_url}" style="padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">Accept Interview</a>
        """

        msg.attach(MIMEText(full_body, "html"))

        # Convert start and end times to UTC for the calendar invite
        start_utc = convert_to_utc(date, start_time)
        end_utc = convert_to_utc(date, end_time)

        # Create the .ics file content
        ics_content = f"""BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//Google Inc//NONSGML Google Calendar 70.9054//EN
BEGIN:VEVENT
SUMMARY:Interview with Candidate
DTSTART:{start_utc}
DTEND:{end_utc}
LOCATION:{location}
DESCRIPTION:Interview scheduled for the recruiter and candidate.
STATUS:CONFIRMED
BEGIN:VALARM
TRIGGER:-PT15M
DESCRIPTION:Reminder for the interview
ACTION:DISPLAY
END:VALARM
END:VEVENT
END:VCALENDAR"""
        
        

        # Attach .ics file
        part = MIMEBase("application", "octet-stream")
        part.set_payload(ics_content)
        encoders.encode_base64(part)
        part.add_header("Content-Disposition", "attachment", filename="interview_invite.ics")
        msg.attach(part)

        # Send the email
        server = smtplib.SMTP("smtp.gmail.com", 587)
        server.starttls()
        server.login(SENDER_EMAIL, SENDER_PASSWORD)
        server.sendmail(SENDER_EMAIL, to_email, msg.as_string())
        server.quit()

        return f"Email with calendar invite successfully sent to {to_email}"

    except Exception as e:
        print(f"Failed to send email: {str(e)}")
        return f"Failed to send email: {str(e)}"



# AutoGen AI Email Agent Setup
class EmailAgent(autogen.Agent):
    def __init__(self, name="EmailAgent"):
        super().__init__(name)

    def send_candidate_email(self, candidate_email, availability):
        subject = "Interview Confirmation"
        body = f"Dear Candidate, your interview is scheduled on {availability.get('date', 'N/A')} at {availability.get('start_time', 'N/A')}."

        # Calculate end time (45-minute duration)
        start_time = availability.get('start_time', 'N/A')
        date = availability.get('date', 'N/A')
        end_time = self.calculate_end_time(date, start_time, duration_minutes=45)

        return send_email_with_invite(candidate_email, subject, body, start_time, end_time, date)

    def send_recruiter_email(self, recruiter_email, candidate_email, availability):
        subject = "New Interview Scheduled"
        body = f"Dear Recruiter, you have an interview scheduled with candidate {candidate_email} on {availability.get('date', 'N/A')} at {availability.get('start_time', 'N/A')}."

        start_time = availability.get('start_time', 'N/A')
        date = availability.get('date', 'N/A')
        end_time = self.calculate_end_time(date, start_time, duration_minutes=45)

        return send_email_with_invite(recruiter_email, subject, body, start_time, end_time, date)

    def send_candidate_acceptance_recruiter_email(self, recruiter_email, candidate_email, availability):
        subject = "Candidate Accepted Interview"
        body = (
            f"The candidate {candidate_email} has accepted the interview invite!\n\n"
            f"Date: {availability.get('date', 'Unknown')}\n"
            f"Time: {availability.get('start_time', 'Unknown')}"
        )

        start_time = availability.get('start_time', 'Unknown')
        date = availability.get('date', 'Unknown')
        end_time = self.calculate_end_time(date, start_time, duration_minutes=45)

        return send_email_with_invite(recruiter_email, subject, body, start_time, end_time, date)

    # Helper function to calculate end time
    def calculate_end_time(self, date, start_time, duration_minutes=45):
        try:
            start_datetime = datetime.strptime(f"{date} {start_time}", "%d-%m-%Y %H:%M")
            end_datetime = start_datetime + timedelta(minutes=duration_minutes)
            return end_datetime.strftime("%H:%M")
        except Exception as e:
            print(f"Time conversion failed: {e}")
            return "N/A"

    # def send_no_availability_email(self, candidate_email):
    #     subject = "No Availability for Your Chosen Slot"
    #     body = "Dear Candidate, unfortunately, no recruiters are available for the given slot. Please select another one."
    #     return send_email(candidate_email, subject, body)


# Instantiate the Email Agent
email_agent = EmailAgent()


# Routes for Flask app
@app.route('/')
def index():
    if 'recruiter_credentials' not in session:
        return redirect(url_for('recruiter_login'))
    return render_template('index.html')


@app.route('/recruiter_login')
def recruiter_login():
    redirect_uri = 'http://127.0.0.1:5000/recruiter_authorize'
    return oauth.google.authorize_redirect(redirect_uri)


@app.route('/recruiter_authorize')
def recruiter_authorize():
    try:
        token = oauth.google.authorize_access_token()
        if not token:
            return "Authentication failed: No token received", 401

        user_info = oauth.google.userinfo()
        if not user_info:
            return "Authentication failed: User info not available", 401

        session['recruiter_credentials'] = {
            'access_token': token['access_token'],
            'refresh_token': token.get('refresh_token'),
            'expires_at': token['expires_at']
        }

        session['recruiter_email'] = user_info.get('email')

        return redirect('/')

    except Exception as e:
        print(f"Authentication error: {str(e)}")
        return f"Authentication failed: {str(e)}", 401


# Extract Date and Time
def extract_availability(text):
    doc = nlp(text)
    parsed_date = None

    for ent in doc.ents:
        if ent.label_ == "DATE":
            parsed = dateparser.parse(ent.text, settings={'PREFER_DATES_FROM': 'future', 'RELATIVE_BASE': datetime.now()})
            if parsed:
                parsed_date = parsed
                break

    time_pattern = r'(\d{1,2}(?::\d{2})?\s*(?:AM|PM|am|pm)\b)'
    times = re.findall(time_pattern, text, re.IGNORECASE)

    if parsed_date and len(times) >= 2:
        start_time = dateparser.parse(times[0]).time()
        return {
            'date': parsed_date.strftime("%d-%m-%Y"),
            'start_time': start_time.strftime("%H:%M")
        }

    return None


# Handle Scheduling Logic
@app.route('/submit', methods=['POST'])
def submit():
    candidate_email = request.form['candidate_email']
    candidate_response = request.form['candidate_response']

    availability = extract_availability(candidate_response)

    if availability:
        # Store the candidate response with their availability in MongoDB
        candidate_data = {
            'email': candidate_email,
            'availability': availability
        }

        try:
            # Insert candidate response into MongoDB collection
            candidate_responses_collection.insert_one(candidate_data)
            print(f"Successfully stored data for {candidate_email}")
        except Exception as e:
            print(f"Failed to store data: {str(e)}")

        available_recruiter = RECRUITER_EMAILS[0] if RECRUITER_EMAILS else None

        if available_recruiter:
            email_agent.send_candidate_email(candidate_email, availability)
            email_agent.send_recruiter_email(available_recruiter, candidate_email, availability)
            return render_template('thank_you.html', message="Interview Scheduled! Please check your gmail for the further details.")
        else:
            return render_template('thank_you.html', message="No recruiters are available for the given slot. Please select another one.")
    else:
        return render_template('thank_you.html', message="Unable to extract availability from response.")


@app.route('/accept_invite', methods=['GET'])
def accept_invite():
    candidate_email = request.args.get('candidate_email')

    if not candidate_email:
        return "Error: No candidate email provided."

    # Retrieve candidate's availability from MongoDB
    candidate_data = candidate_responses_collection.find_one({'email': candidate_email})

    if not candidate_data or 'availability' not in candidate_data:
        return "Error: No interview schedule found for this candidate."

    availability = candidate_data['availability']
    recruiter_email = RECRUITER_EMAILS[0]  # Assuming first recruiter for now

    # Notify recruiter that candidate accepted the invite, using the new method in EmailAgent
    email_agent.send_candidate_acceptance_recruiter_email(
        recruiter_email,
        candidate_email,
        availability
    )

    return render_template('thank_you.html', message="Thank you for accepting the interview invite!")



if __name__ == '__main__':
    app.run(debug=True)
