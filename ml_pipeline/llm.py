import cohere
import os
from dotenv import load_dotenv
from prompt import prompt

# Load the .env file
load_dotenv()

# Get the API key from environment variables
api_key = os.getenv('API_KEY')

def get_response(text: str = None):
    '''
    Args:
        text: The incoming text detected by the OCR model
    
    Returns:
        answer: Json containing the required extracted information
    '''
    co = cohere.ClientV2(api_key)
    
    input_prompt = f"{prompt} {text}"

    response = co.chat(
        model="command-r-plus-08-2024",
        messages=[
            {
                "role": "user",
                "content": input_prompt
            }
        ]
    )
    
    response_json = response.message.content[0].text
    # print("Response:",response_json)
    
    return response_json