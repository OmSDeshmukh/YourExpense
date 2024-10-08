# File dealing with OCR using EasyOCR [ https://github.com/JaidedAI/EasyOCR ]
import easyocr
import cv2
import matplotlib.pyplot as plt

def process_text(result):
    '''
    A function to process the results obtained from the ocr model, structured on the basis of the lines
    
    Args:
        result: result from the OCR model
        
    Returns:
        text: Processed text
    ''' 
    
    # Create a list to hold lines of grouped text
    grouped_lines = []
    threshold = 10  # Define a threshold for how close y-coordinates need to be to group together

    # Sort results based on the y-coordinate of the bottom-left corner of the bounding box
    sorted_results = sorted(result, key=lambda x: x[0][3][1])  # Sort by the bottom-left y-coordinate

    # Group texts based on proximity in y-coordinates
    current_line = []
    current_y = None

    for entry in sorted_results:
        if len(entry) >= 2:
            text = entry[1]
            # Get the y-coordinate of the bottom-left corner
            y_coord = entry[0][3][1]
            
            # If current_line is empty or the current text is close to the last y-coordinate, append it
            if current_y is None or abs(y_coord - current_y) <= threshold:
                current_line.append(text)
            else:
                # If not, store the current line and start a new line
                grouped_lines.append(" ".join(current_line))
                current_line = [text]
            
            # Update the current_y
            current_y = y_coord

    # Append the last line if it exists
    if current_line:
        grouped_lines.append(" ".join(current_line))

    # Join the lines with newline characters for final output
    final_output = "\n".join(grouped_lines)

    # Output the structured text
    # print(final_output)
    
    return final_output


def recognise_text(image_path: str = None):
    '''
    A function to recognise the text present in the bill. It essentially uses the easyocr model
    
    Args:
        image_path: Path to the image of the bill
        
    Returs:
        result: Result from the OCR model
            A list of tuples of form ([Top-left corner, Top-right corner, Bottom-right corner, Bottom-left corner], text, probability)
    '''
    # Load the image using OpenCV
    # image_path = '/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/bill3.jpg'
    image = cv2.imread(image_path)

    # Initialize the EasyOCR reader for the English language (add more languages if needed)
    reader = easyocr.Reader(['en'])

    # Perform OCR on the image
    results = reader.readtext(image_path)

    # result is a list of tuples
    # ([Top-left corner, Top-right corner, Bottom-right corner, Bottom-left corner], text, probability)

    # Loop over the results
    # Used for testing purposes
    # for (bbox, text, prob) in results:
    #     # Extract the bounding box (4 points), text, and probability
    #     top_left = tuple([int(val) for val in bbox[0]])
    #     bottom_right = tuple([int(val) for val in bbox[2]])

    #     # Draw the bounding box on the image
    #     cv2.rectangle(image, top_left, bottom_right, (0, 255, 0), 1)  # Green box

    #     # Put the recognized text on the image (optional)
    #     cv2.putText(image, text, (top_left[0], top_left[1] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.3, (255, 0, 0), 1)  # Blue text

    # # Convert BGR image (from OpenCV) to RGB for displaying with matplotlib
    # image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    # # Display the image using Matplotlib
    # plt.figure(figsize=(10, 10))
    # plt.imshow(image_rgb)
    # plt.axis('off')
    # # plt.show()
    # cv2.imwrite("../images/output.png", image_rgb)
    
    return results
        
        
if __name__ == "__main__":
    result = recognise_text(image_path='/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/bill3.jpg')
    processed_text = process_text(result=result)
    print(processed_text)