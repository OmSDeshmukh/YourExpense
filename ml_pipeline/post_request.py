import base64
import requests
import json
import pprint

# Step 1: Encode the image to base64
def encode_image_to_base64(image_path):
    with open(image_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
    return encoded_string

# Step 2: Send the POST request
def send_image_to_server(encoded_image):
    url = 'http://127.0.0.1:5000/predict'  # Your Flask server URL
    headers = {'Content-Type': 'application/json'}

    # JSON payload with the base64 image string
    payload = {
        'image_base64': encoded_image
    }

    # Send the POST request
    response = requests.post(url, headers=headers, json=payload)
    
    # Print response from the server
    print(f"Status Code: {response.status_code}")
    # Check if the request was successful
    if response.status_code == 200:
        # Print the response JSON (prediction result)
        print(f"Response JSON: {response.json()}")
    else:
        # Print the error message if something went wrong
        print(f"Error: {response.text}")

if __name__ == "__main__":
    # Path to the image you want to encode and send
    image_path = "/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/bill3.jpg"
    
    # Step 1: Encode the image to Base64
    encoded_image = encode_image_to_base64(image_path)
    
    # Step 2: Send the Base64-encoded image to the Flask server
    pprint.pprint(send_image_to_server(encoded_image))