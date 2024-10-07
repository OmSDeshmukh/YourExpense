# File dealing with OCR using EasyOCR [ https://github.com/JaidedAI/EasyOCR ]

import easyocr
import cv2
import matplotlib.pyplot as plt

# Load the image using OpenCV
image_path = '/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/bill3.jpg'
image = cv2.imread(image_path)

# Initialize the EasyOCR reader for the English language (add more languages if needed)
reader = easyocr.Reader(['en'])

# Perform OCR on the image
results = reader.readtext(image_path)

# result is a list of tuples
# ([Top-left corner, Top-right corner, Bottom-right corner, Bottom-left corner], text, probability)

# Loop over the results
for (bbox, text, prob) in results:
    # Extract the bounding box (4 points), text, and probability
    top_left = tuple([int(val) for val in bbox[0]])
    bottom_right = tuple([int(val) for val in bbox[2]])

    # Draw the bounding box on the image
    cv2.rectangle(image, top_left, bottom_right, (0, 255, 0), 1)  # Green box

    # Put the recognized text on the image (optional)
    cv2.putText(image, text, (top_left[0], top_left[1] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.3, (255, 0, 0), 1)  # Blue text

# Convert BGR image (from OpenCV) to RGB for displaying with matplotlib
image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

# Display the image using Matplotlib
plt.figure(figsize=(10, 10))
plt.imshow(image_rgb)
plt.axis('off')
# plt.show()
cv2.imwrite("../images/output.png", image_rgb)
texto = ""

# Output text and bounding boxes
for (bbox, text, prob) in results:
    print(f"Text: {text}, Bounding Box: {bbox}, Confidence: {prob:.2f}")
    texto+=text