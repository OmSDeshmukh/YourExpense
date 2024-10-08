from llm import get_response
from ocr import recognise_text, process_text
from pprint import pprint

def predict(image_path: str = None):
    '''
    A function for the entire pipeline of the Bill reading ML pipeline
    Image --[ OCR Model ]--> Result --[ Processing ]--> Text --[ LLM ]--> JSON
    
    Args:
        image_path: The path to the image to apply the pipeline on
        
    Returns:
        result_json: JSON containing Heading, Date, Total Amount and Category of the bill 
    '''
    result = recognise_text(image_path= image_path)
    processed_text = process_text(result= result)
    result_json = get_response(text= processed_text)
    
    return result_json
    
    
if __name__ == "__main__":
    result_json = predict(image_path='/Users/omdeshmukh/Downloads/MachineLearning/Projects/YourExpense/images/bill3.jpg')
    pprint(result_json)