# 🍊OrangeHRM Automation Framework

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=Selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-FF7F00?style=for-the-badge&logo=TestNG&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=Apache-Maven&logoColor=white)

## 👤 Author
**Dwi Agus Rianto** - *QA Engineer*

## 📝 Project Description
This project is a robust automated testing framework for the **OrangeHRM** web application. It is designed using the **Page Object Model (POM)** design pattern to ensure clean, maintainable, and scalable test code. The framework specifically handles the **PIM (Personal Information Management)** module, automating employee registration through **Data-Driven Testing (DDT)**.

## 🚀 Key Features
* **Data-Driven Testing (DDT):** Seamlessly integrated with **Apache POI** to execute multiple test scenarios using data from external Excel files (`.xlsx`).
* **Robust Synchronization:** Implemented advanced **Explicit Waits** and **JavaScript Click fallbacks** to handle the asynchronous behavior of Single Page Applications (SPA) and ensure stability in Headless mode.
* **Professional Logging:** Full execution traceability using **Log4j2** with custom logging levels (INFO, WARN, ERROR).
* **Dynamic Data Generation:** Automated random Employee ID generation to ensure unique data for every test run.
* **CI/CD Ready:** Optimized ChromeOptions for headless execution, making it compatible with GitHub Actions and other CI/CD pipelines.

## 📂 Project Structure
```text
orangehrm-automation
 ├── .github/workflows   # CI/CD Pipeline configuration
 ├── src
 │    ├── main/java/com/orangehrm
 │    │    ├── pages/    # Page Object classes (Login, PIM, Dashboard)
 │    │    └── utils/    # Utility classes (ExcelUtil for DDT)
 │    └── test/java/com/orangehrm
 │         ├── base/     # BaseTest for setup and teardown
 │         └── tests/    # Test execution scripts (DDT scenarios)
 └── src/test/resources
      ├── data/          # Test Data (EmployeeData.xlsx)
      ├── images/        # Assets for profile picture upload
      └── log4j2.xml     # Logging configuration
```
## 🛠 Tech Stack

| Tool | Purpose |
| :--- | :--- |
| **Java 21** | Primary Programming Language |
| **Selenium WebDriver** | Web Browser Automation |
| **TestNG** | Test Execution and Data Management |
| **Apache POI** | Excel File Manipulation for DDT |
| **Log4j2** | Execution Logs and Debugging |
| **Maven** | Project Management and Dependency Control |

## 📊 Automated Reporting & CI/CD
This project is integrated with **GitHub Actions** to provide a fully automated testing pipeline.

* **Live Test Report:** [![Allure Report](https://img.shields.io/badge/Allure-Report-green?style=for-the-badge&logo=allure)](https://dwiagusr.github.io/cypress-ecommerce-automation/)
  > 🔗 **[Click here to view the latest Live Allure Dashboard](https://dwiagusr.github.io/orangehrm-automation-selenium/)**

## ⚙️ How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/dwiagusr/orangehrm-automation.git
   ```
2. **Setup Test Data:**

   ```src/test/resources/data/EmployeeData.xlsx``` with your desired test cases.
3. **Execute Tests:**

   Run the following command in your terminal:
   ```bash
   mvn clean test
   ```