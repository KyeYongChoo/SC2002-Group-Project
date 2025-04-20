# 🌟 SC2002 Group Project

A Java-based command-line application for managing HDB projects, applications, and enquiries.

---

## 📚 Table of Contents
1. [📖 Introduction](#introduction)
2. [✨ Features](#features)
3. [⚙️ Prerequisites](#prerequisites)
4. [🚀 Getting Started](#getting-started)
5. [▶️ Running the Application](#running-the-application)
6. [📂 Data Files](#data-files)
7. [🧪 Testing](#testing)
8. [🛠️ Troubleshooting](#troubleshooting)
9. [🤝 Contributing](#contributing)

---

## 📖 Introduction

This project implements a CLI (Command-Line Interface) system to support:
- **Applicants**: browse and apply for projects, view application status, withdraw applications, and manage enquiries.
- **Officers**: apply to manage HDB projects, assign to projects, and respond to enquiries.
- **Managers**: create, edit, and manage project listings, review officer assignment requests, and approve/reject applications.

The code is organized under `program/control`, `program/boundary`, and `program/entity` packages.

---

## ✨ Features

- Role-based menus (Applicant, Officer, Manager)
- Dynamic menus with conditional visibility
- Lazy instantiation of submenus to avoid premature type casting
- CSV-based persistence (projects, users, requests, enquiries)
- Password reset handler and secure login
- Hash, Salt, and Peppered passwords for secure storage

---

## ⚙️ Prerequisites

- Java JDK 16 or later

---

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/KyeYongChoo/SC2002-Group-Project.git
   cd SC2002-Group-Project
   ```

2. **Verify Java version**
   ```bash
   java -version
   ```
   Ensure it reports version 16 or newer.

3. **(Optional) Initialize Gradle/Maven**
   - If you use Gradle: `gradle init` or include a `build.gradle`.
   - If you use Maven: `mvn archetype:generate` or include a `pom.xml`.

---

## ▶️ Running the Application

Navigate to the source root and run:

```bash
javac -d out src/program/control/Main.java
java -cp out program.control.Main
```

Alternatively, if you have a build tool configured:

```bash
./gradlew run   # for Gradle
mvn compile exec:java -Dexec.mainClass="program.control.Main"  # for Maven
```

---

## 📂 Data Files

All data is stored in CSV files under the `data/` folder:
- `ApplicantList.csv`
- `AssignReqList.csv`
- `EnquiryList.csv`
- `HousingReqList.csv`
- `ManagerList.csv`
- `OfficerList.csv`
- `ProjectList.csv`

**Important:** Changes are written only when the application exits normally.

To seed or edit data:
- Open the CSV files directly in any spreadsheet editor and re-save as CSV.
- Or edit the `.xlsx` files in `data/` and export to CSV.

---

## 🧪 Testing

Only manual testing is supported for now. Please contact Choo Kye Yong if junit tests were required, or if other testing techniques were desired.

---

## 🛠️ Troubleshooting

### ⚠️ ClassCastException on Startup

If you encounter `ClassCastException` when modifying code, it is often due to **lazy instantiation** of menus being triggered too early. Check:
- `MenuNavigator.start()` — ensure you only push/pop before executing actions.
- `MenuGroup.refresh()` — verify suppliers aren’t called prematurely.

### ❌ "Sorry, no items" Messages

You may see this message repeated during menu refresh. This is expected if a submenu’s item list is empty before opening. Suppress it by moving the print logic into the execute phase (not description/ dynamic description) or guarding it with a check on the current menu.

---

## 🤝 Contributing

Feel free to open issues or pull requests.
