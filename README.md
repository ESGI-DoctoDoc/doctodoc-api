<div align="left" style="position: relative;">
<img src="logo.webp" alt="Logo" width="200" align="right">
<h1>DOCTODOC-API</h1>
<p align="left">
	<img src="https://img.shields.io/github/license/ESGI-DoctoDoc/doctodoc-api?style=default&logo=opensourceinitiative&logoColor=white&color=045b34" alt="license">
	<img src="https://img.shields.io/github/last-commit/ESGI-DoctoDoc/doctodoc-api?style=default&logo=git&logoColor=white&color=045b34" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/ESGI-DoctoDoc/doctodoc-api?style=default&color=045b34" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/ESGI-DoctoDoc/doctodoc-api?style=default&color=045b34" alt="repo-language-count">

<br clear="right">
</p></div>

## Table of Contents

- [ Overview](#-overview)
- [ Features](#-features)
- [ Project Structure](#-project-structure)
  - [ Project Index](#-project-index)
- [ Getting Started](#-getting-started)
  - [ Prerequisites](#-prerequisites)
  - [ Installation](#-installation)
  - [ Usage](#-usage)
  - [ Testing](#-testing)
- [ Project Roadmap](#-project-roadmap)
- [ Contributing](#-contributing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)

---

## Overview

The current healthcare journey places the burden on patients to manage their own appointments and manually share medical
documents (e.g., test results, prescriptions) between healthcare professionals. This fragmented approach leads to
delays, lost information, and administrative overhead that affects both patients and providers.

DoctoDoc simplifies the patient journey and improves coordination across the healthcare ecosystem. The platform
eliminates the need for patients to handle appointment logistics or document transmission, while ensuring that
professionals have secure, real-time access to up-to-date medical records.

At the heart of this ecosystem lies DoctoDoc API — the core component that encapsulates all business logic across our
medical domains. It orchestrates interactions between patients, practitioners, and medical data to enable a seamless,
reliable, and secure digital healthcare experience.

---

## Features

### Patients

- Consult doctor profiles using filters (name, specialties)
- Book appointments easily
- Manage their own account
- Manage close family members
- Manage their own medical records
- Receive notifications for certain events
- Set and manage a referent doctor
- Report a doctor

### Doctors

- Manage their own calendar (availability, absences, appointments)
- Handle appointments with their patients
- Configure their list of medical concerns
- Manage their subscription
- Receive notifications for certain events

### Admin

- Monitor the entire system (appointments, doctors, subscriptions)
- Validate doctor accounts
- Manage specialties
- Receive notifications for certain events

---

## Project Structure

```sh
└── doctodoc-api/
    ├── .github
    │   └── workflows
    ├── Dockerfile
    ├── README.md
    ├── pom.xml
    └── src
        ├── main
            ├── fr.esgi.doctodocapi
                ├── configuration
                ├── infrastructure
                ├── model
                ├── presentation
                ├── use_cases
```

## Getting Started

### Prerequisites

Before getting started with doctodoc-api, ensure your runtime environment meets the following requirements:

- **Framework:** Spring Boot 3
- **Programming Language:** Java 21
- **Container Runtime:** Docker

### Installation

Install doctodoc-api using one of the following methods:

**Build from source:**

1. Clone the doctodoc-api repository:

```sh
❯ git clone https://github.com/ESGI-DoctoDoc/doctodoc-api
```

2. Navigate to the project directory:

```sh
❯ cd doctodoc-api
```

**Using `Java and Maven`**

```sh
❯ mvn clean install
❯ jjava -jar target/MainApplication.jar
```

**Using `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker build -t ESGI-DoctoDoc/doctodoc-api .
```

### Usage

Run doctodoc-api using the following command:
**Using `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker run -it {image_name}
```

### Testing

Install Maven then run the test suite using the following command:
``mvn test``

---

## Contributors

- Mélissa LAURENT
- Abdallah SLIMANE

---