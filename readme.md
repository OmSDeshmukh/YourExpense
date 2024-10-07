
# YourExpense

YourExpense is a modern and intuitive expense tracking application designed to help you manage and monitor your daily expenses across various categories. Additionally, it features a machine learning (ML) pipeline that allows you to upload images of bills/receipts, extract relevant information like the date, amount, and item details, and automatically add them to your expense records.

## Features

### 1. **Expense Management**
- **Track Daily Expenses**: Log your expenses by adding amount, category, date, and a short description.
- **Categories**: Organize your expenses by categories such as groceries, entertainment, transportation, utilities, dining, and more.
- **Expense Reports**: Get detailed summaries of your expenses by day, week, or month.
- **Custom Categories**: Create custom categories for your expenses.
- **Expense History**: View past expenses, filter by date or category, and manage entries easily.

### 2. **Bill Upload via Image**
- **ML-Based Bill Scanning**: Upload a photo or scanned image of your bill/receipt. Our ML pipeline extracts essential details such as:
  - Date of purchase
  - Vendor name
  - List of items purchased
  - Total amount spent
- **Auto-Logging**: Extracted data is automatically logged in your expense tracker, saving you time and manual effort.

### 3. **Analytics**
- **Spending Trends**: Visualize your spending patterns and receive insights based on your historical data.
- **Category Breakdown**: Get a detailed breakdown of expenses per category over customizable periods.
- **Daily/Monthly Limits**: Set expense limits and get notified when you're close to exceeding them.

### 4. **Mobile-Friendly**
- YourExpense is fully responsive, allowing you to log and review your expenses on the go via mobile or tablet devices.

---

## Table of Contents
1. [Getting Started](#getting-started)
2. [Installation](#installation)
3. [ML Pipeline for Bill Processing](#ml-pipeline-for-bill-processing)
4. [Usage](#usage)
5. [Contributing](#contributing)
6. [License](#license)

---

## Getting Started

These instructions will get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Ensure you have the following installed:
- **Python 3.8+**
- **pip** (Python package manager)
- **dotenv** (For env file and API key management)
- **Easy OCR** ([Git Hub Repo](https://github.com/JaidedAI/EasyOCR))

### ML Pipeline Dependencies
The following libraries are required for the machine learning pipeline:
- **OpenCV**: For image processing.
- **easyocr**: For OCR (Optical Character Recognition) to read text from bills.
- **cohere**: For LLM api

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/yourexpense.git
   cd yourexpense
   ```

2. Create a virtual environment and activate it:
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   ```

3. Install required dependencies:
   ```bash
   pip install -r requirements.txt
   ```

4. Set up the environment variables (create a `.env` file):
   ```bash
   # .env file
   SECRET_KEY=your_secret_key
   DATABASE_URL=your_database_url
   ```

5. Set up the database:
   ```bash
   flask db upgrade
   ```

6. Run the application:
   ```bash
   flask run
   ```

---

## ML Pipeline for Bill Processing

The ML pipeline is responsible for processing the uploaded bill image and extracting essential information. The following steps outline how the pipeline works:

1. **Image Preprocessing**:
   - The uploaded image is first cleaned and preprocessed using OpenCV to enhance text readability for the OCR engine.
   - This includes grayscale conversion, noise reduction, and thresholding.

2. **Text Extraction**:
   - The preprocessed image is passed through Tesseract OCR to extract raw text.
   - The extracted text is then parsed to identify key information such as:
     - **Total Amount**: Recognized by patterns (e.g., numbers followed by currency symbols).
     - **Date**: Recognized by date patterns (e.g., `MM/DD/YYYY`).
     - **Vendor Name**: Extracted from the first few lines of the receipt.

3. **Data Logging**:
   - Once extracted, the details are automatically logged into the app's expense tracker under the correct categories and date.

### Pipeline Usage

The bill upload feature is integrated into the web interface. Users can click the "Upload Bill" button, select an image, and the backend handles the rest.

You can manually trigger the ML pipeline by:
```bash
python ml_pipeline.py --input path_to_image
```

---

## Usage

### Adding an Expense Manually
1. Navigate to the "Add Expense" section.
2. Enter the following details:
   - Amount
   - Category
   - Date
   - Description
3. Click "Submit" to log the expense.

### Uploading a Bill
1. Click the "Upload Bill" button on the dashboard.
2. Select the bill image from your device.
3. The application will automatically process and log the data.

### Viewing Reports
1. Go to the "Reports" section.
2. Select a time period (daily, weekly, monthly).
3. View your expenses as a chart or list.

---

## Contributing

We welcome contributions from the community! Here's how you can contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

Please ensure all new features are covered by tests and linting passes successfully.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Contact

For any questions, feel free to reach out at [your-email@example.com](mailto:210010033@iitdh.ac.in).

Enjoy tracking your expenses with **YourExpense**!
