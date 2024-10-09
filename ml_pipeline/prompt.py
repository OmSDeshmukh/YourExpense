prompt_old = """I have extracted the following text and corresponding bounding boxes from an image of a bill using an OCR model. Based on the text provided, I need to extract four key pieces of information:
    1. Heading: The name of the business or vendor issuing the bill.
    2. Date: The date the bill was issued, in formats such as DD/MM/YYYY, MM/DD/YYYY, or variations thereof.
    3. Total Amount: The total amount charged, usually found near the end of the bill with patterns like Total, Amount, Total Due, followed by a number.
    4. Category: The category the bill belongs to, such as groceries, dining, utilities, transportation, etc. You can infer the category based on the business name or the items listed in the text.
    Here’s the OCR output with the bounding boxes and extracted text: """
    
prompt = """I have extracted the following text and corresponding bounding boxes from an image of a bill using an OCR model. Based on the text provided, please extract and return only the values for the following four key pieces of information without any additional explanation or context in JSON format:

1. Heading
2. Date
3. Total Amount
4. Category
If these values are not found or you are not sure of the values, return None for those keys. Give the reply in JSON format
Here’s the OCR output with the bounding boxes and extracted text: """