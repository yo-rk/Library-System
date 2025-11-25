# 📚 **City Library Digital Management System**

# 📘 *Java Programming Project*

**Name:** Yashieta Sethi
**Roll No.:** 2401010187
**Course Name:** Java Programming
**Programme:** B.Tech CSE CORE
**Session:** 2025–26

---

## 📝 **Description**

A console-based Java application designed to manage a **digital library**.
It demonstrates **Object-Oriented Programming**, **File Handling**, **Collections**, **Comparable interface**, **Regex validation**, and **Data Persistence** through text files (`books.txt` and `members.txt`).

---

## ✨ **Features**

* 📕 Add New Books (Title, Author, Category)
* 👤 Add Members with **Email Validation**
* 🔄 Issue Books with proper availability checks
* 🔁 Return Books to restore availability
* 🔍 Search Books (by title, author, or category)
* 🗂️ Sort Books by:

  * 🔠 Title (using Comparable interface)
  * ✍️ Author
  * 🗃️ Category
* 💾 Auto-save data to files
* 🔐 Regex pattern validation for email
* 🛠️ Full error handling for invalid inputs

---

## 🧠 **Concepts Used**

* **Object-Oriented Programming**
  Classes: `Book`, `Member`, `LibrarySystem`
* **Comparable Interface** for sorting
* **Java Collections**

  * `HashMap`, `ArrayList`, `Collections.sort`
* **File Handling**

  * `BufferedWriter`, `BufferedReader`, `FileWriter`, `FileReader`
* **Regex Validation** (`Pattern`)
* **Exception Handling**

  * `try–catch` blocks
* **Persistent Storage**

  * Saves and loads data from `.txt` files

---

## ▶️ **How to Run**

### 1️⃣ Save as:

```
LibrarySystem.java
```

### 2️⃣ Compile:

```
javac LibrarySystem.java
```

### 3️⃣ Run:

```
java LibrarySystem
```

---

## 📂 **Files Generated Automatically**

* **books.txt**
  Format → `id|title|author|category|issued(1/0)`

* **members.txt**
  Format → `id|name|email|issuedBookIds`

These files are automatically created and updated — no manual setup needed.

---

## ✅ **Conclusion**

This project is a full demonstration of **Java OOP + File Handling + Collections + Validation + Persistence**.
It’s perfectly suitable for **assignments, practical exams, and academic submissions**.


