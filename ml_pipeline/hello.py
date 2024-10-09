from flask import Flask, request, jsonify
from flask_cors import CORS

import os
import json
import base64

from main import predict

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 16 MB

@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"

@app.route("/bye")
def bye_world():
    return "<p>Bye, Bye  world!</p>"

@app.route('/predict', methods=['POST'])
def ml_prediction():
    print("Hola")
    # try:
    # Get the data from the POST request (JSON format)
    data = request.get_json()
    keys = list(data.keys())
    print(keys)
    print("HI")
    # Assuming the model expects a list of feature values as input
    # image_path = data['image_path']  # Extract features list from request
    image_path = "/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/decoded_image.jpg"
    
    image_base64 = data['image_base64']  # Expecting the key to be 'image_base64'
    # print(image_base64)
    
    # Decode the base64 string into bytes
    image_data = base64.b64decode(image_base64)

    # Optionally, save the image to the file system (as an example)
    with open(image_path, "wb") as f:
        f.write(image_data)
    
    # Ensure the image file exists at the specified path
    if not os.path.exists(image_path):
        return jsonify({'error': 'Image file not found'}), 400

    # Make a prediction
    prediction = predict(image_path=image_path)  # The model expects a 2D array for multiple inputs
    
    print(prediction)
    return prediction, 200
    
    # except Exception as e:
    #     return jsonify({'error': str(e)}), 500
    
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)  # Run Flask on port 5000