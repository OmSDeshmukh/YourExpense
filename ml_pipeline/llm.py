import cohere
import os
from dotenv import load_dotenv

# Load the .env file
load_dotenv()

# Get the API key from environment variables
api_key = os.getenv('API_KEY')

co = cohere.ClientV2(api_key)

response = co.chat(
    model="command-r-plus-08-2024",
    messages=[
        {
            "role": "user",
            "content": f"The following is the output of an OCR model on a bill, {texto}, Provide a json with final amount and category of the bill"
        }
    ]
)

print(response["content"]["text"])
print(response.message.content[0].text)