

# BlackBox

**BlackBox** is a privacy-focused messaging app for Android that prioritizes security and simplicity. Built using **Jetpack Compose**, this app offers end-to-end encryption (E2EE), multiple PIN-protected inboxes, and a custom-designed SQLite database structure for efficient and scalable data management.

---

## Features

- **End-to-End Encryption**: Ensures secure communication with cryptographic techniques.  
- **Multiple Inboxes**: Each protected by its own PIN, allowing separation of conversations.  
- **Custom SQLite Database Structure**: A unique approach to define tables and manage queries programmatically.  
- **Jetpack Compose**: Built with Compose, modernizing UI development on Android.  

---

## Tech Stack

- **Kotlin**: The programming language for the app.  
- **Jetpack Compose**: For building the user interface.  
- **SQLite**: For local database storage, with a custom implementation for flexibility and scalability.  

---

## SQLite Database Design

The app includes a novel way of interacting with the database using interfaces and helper functions for consistency and maintainability. Here’s a glimpse of the implementation:

```kotlin
interface Column {
    val columnName: String
    val definition: String
}

fun ContentValues.put(key: Column, text: String) =
    this.put(key.columnName, text)

fun ContentValues.put(key: Column, bool: Boolean) =
    this.put(key.columnName, bool)

fun ContentValues.put(key: Column, long: Long) =
    this.put(key.columnName, long)
```


### Table Definitions
Tables are defined programmatically, with `enum` classes representing the schema:

```kotlin
enum class InboxColumns(override val definition: String) : Column {
    INBOX_ID("INTEGER PRIMARY KEY AUTOINCREMENT"),
    INBOX_PUBLIC_KEY("TEXT NOT NULL UNIQUE"),
    INBOX_PRIVATE_KEY("TEXT NOT NULL"),
    LABEL("TEXT"),
    HAS_NEW_MESSAGE("BOOLEAN"),
    IV("TEXT"),
    SALT("TEXT"),
    LAST_MESSAGE_ID("INTEGER DEFAULT 0");

    override val columnName: String = name.lowercase()
}
```

This design allows for queries to be generated dynamically:

```kotlin
fun <T : Column> generateWhereQuery(where: HashMap<T, String>?): String {
    return where?.let { w -> "WHERE (${w.keys.joinToString(" AND ") { column -> "${column.columnName} = ?" }})" } ?: ""
}
```

---

## Object-Oriented and Beyond

The app’s logic relies heavily on Kotlin’s advanced features, including encapsulation and helper methods. Some examples include `Pin` class, designed for validation and secure operations:

### Pin Class

```kotlin
class Pin private constructor(private val text: String) {
    companion object {
        const val LENGTH = 6

        fun parse(text: String): Pin? {
            return if (isValid(text)) Pin(
                Sha256.hash(text)
            )
            else null
        }

        private fun isValid(text: String): Boolean = text.isDigitsOnly() && text.length == LENGTH
    }

    override fun toString() = text
}
```


---

## Challenges and Learning

This is my **first app using Jetpack Compose**, and I’m actively learning through the development process. 

---

## Future Plans

- Add more advanced cryptographic features.  
- Introduce synchronization across devices.  
- Enhance the UI/UX for PIN-protected inbox management. 
- Use right structure for viewmodel
- Implement flow and live data

---

## Contributions

Contributions and suggestions are welcome! Feel free to fork this repository or open an issue.  

---
