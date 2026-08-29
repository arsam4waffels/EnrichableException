# Enrichable-Exception

Java exceptions were starting to drive me a little crazy.

A message, a stack trace, maybe a cause... and when an error gets a little more complicated, things can get messy pretty quickly.

So I decided to make them a bit more organized.

**Enrichable-Exception** is a small Java library for creating exceptions that can carry more useful information, metadata, error levels, and configuration without turning the code into a mess.

It's still a work in progress, but it's slowly becoming something useful.

---

## Features

* Multiple error information entries
* Error levels
* Custom metadata
* Configurable output
* Optional timestamps
* Optional error count
* Exception cause preservation
* Thread-safe file logging
* Error-level log filtering
* Input validation
* JUnit 5 tests
* Annotation for custom exception classes

---

## Requirements

* Java 21+
* Maven 3+

---

## Installation

Clone the repository:

```bash
git clone https://github.com/arsam4waffels/enrichable.git

cd enrichable
```

Run the tests:

```bash
mvn test
```

---

## Quick Start

Create an `EnrichableException` like this:

```java
EnrichableException exception =
        new EnrichableException(
                "DATABASE",
                "DB-001",
                "Database connection failed",
                ErrorLevel.CRITICAL,
                new IllegalStateException("Connection refused.")
        );

System.out.println(exception);
```

The constructor takes:

```text
context
error code
message
error level
cause
```

---

## Adding Information

Sometimes one error isn't enough.

You can attach additional information to the same exception:

```java
exception.addInformation(
        "AUTH_SERVICE",
        "AUTH-001",
        "Authentication failed",
        ErrorLevel.WARNING
);
```

This can be useful when several related things go wrong during the same operation.

---

## Metadata

You can also attach extra key-value information:

```java
exception
        .addMetadata("userId", "1042")
        .addMetadata("query", "SELECT * FROM users")
        .addMetadata("retryCount", "3");
```

Whether metadata appears in the output is controlled by the configuration:

```java
exception.setConfig(
        new ExceptionConfiguration()
                .setShowMetadata(true)
);
```

---

## Configuration

`ExceptionConfiguration` controls what information should be included in the formatted exception output.

```java
ExceptionConfiguration configuration =
        new ExceptionConfiguration()
                .setShowTimestamp(true)
                .setShowErrorLevel(true)
                .setShowErrorCount(true)
                .setShowMetadata(true);

exception.setConfig(configuration);
```

Currently available options:

| Option           | What it does               |
| ---------------- | -------------------------- |
| `showTimestamp`  | Shows the timestamp        |
| `showErrorLevel` | Shows the error level      |
| `showErrorCount` | Shows the number of errors |
| `showMetadata`   | Shows metadata             |

Everything is disabled by default.

---

## Error Levels

There are currently four levels:

```java
ErrorLevel.INFO
ErrorLevel.WARNING
ErrorLevel.ERROR
ErrorLevel.CRITICAL
```

For example:

```java
new EnrichableException(
        "PAYMENT",
        "PAY-001",
        "Payment processing failed",
        ErrorLevel.ERROR,
        null
);
```

---

## Exception Cause

You can pass the original exception as the cause:

```java
IllegalStateException cause =
        new IllegalStateException("Connection refused.");

EnrichableException exception =
        new EnrichableException(
                "DATABASE",
                "DB-001",
                "Database operation failed",
                ErrorLevel.CRITICAL,
                cause
        );
```

The original cause is preserved and can still be retrieved normally:

```java
exception.getCause();
```

---

## Logging

Sometimes printing an exception to the console isn't enough.

`EnrichableException` can write a formatted exception report to `enrichable.log` using:

```java
exception.writeLog();
```

The generated report keeps each error and its metadata together, so things don't turn into a wall of random error messages.

Example:

```text
════════════════════════════════════════════════════
  ENRICHABLE EXCEPTION REPORT
  Total Errors : 2
  Thrown At    : 2026-08-27 11:58:37
════════════════════════════════════════════════════

  [ERROR-1] [CRITICAL] [DATABASE:DB-001]
  Failed to execute query: table 'users' not found
    └─ Time : 2026-08-27 11:58:37
    └─ retryCount : 3
    └─ query : SELECT * FROM users
    └─ userId : 1042

  [ERROR-2] [WARNING] [DATABASE-SIZE:DB-002]
  Failed to execute query: table 'users-info' not found
    └─ Time : 2026-08-27 11:58:37
```

Each call to `writeLog()` appends a new report to `enrichable.log`.

File logging is thread-safe, so concurrent exceptions can safely write to the shared `enrichable.log` file without interleaving their reports.

You can use `onlyLog()` to log only errors with a specific level. The filter only affects file logging; all exception information is still preserved.

```text
exception
        .onlyLog(ErrorLevel.CRITICAL)
        .writeLog();
```
Metadata is attached to the specific exception information it belongs to, rather than being shared between all errors.


---
## Annotations

Instead of repeating context, codes, and levels every time, you can define them once on the class itself.

`@EnrichableHandler` goes on service classes:

```java
@EnrichableHandler(context = "Database", defaultLevel = ErrorLevel.CRITICAL)
public class DatabaseService {
    public void connect() {
        throw AnnotationProcessor.processHandler(
                DatabaseService.class,
                "DB-001",
                "Connection failed"
        );
    }
}
```

`@EnrichableCode` goes on custom exception classes:

```java
@EnrichableCode(code = "DB-001", level = ErrorLevel.CRITICAL)
public class DatabaseConnectionException extends EnrichableException { ... }

throw AnnotationProcessor.processCode(
        DatabaseConnectionException.class,
        "Database",
        "Connection failed"
);
```

The annotated values are picked up automatically.

---

## Validation

The library performs some basic validation so invalid information doesn't quietly make its way into an exception.

Required text values cannot be `null` or blank.

Metadata also has a few rules:

* `null` keys & values are rejected.
* Blank keys & values become `BLANK`.

For example:

```java
exception.addMetadata("", "user-6969");
```

becomes:

```text
[BLANK=user-6969]
```

And:

```java
exception.addMetadata("userId", "");
```

becomes:

```text
[userId=BLANK]
```

---

## Testing

The project uses JUnit 5.

Run all tests with:

```bash
mvn test
```

The tests currently cover things like:

* Adding information
* Input validation
* Metadata
* Configuration
* Exception causes
* Multiple error information
* Output behavior
* File logging
* Log-level filtering
* Thread-safe file logging

---

## Example

Here's a more complete example:

```java
EnrichableException databaseError =
        new EnrichableException(
                "DATABASE",
                "DB-001",
                "Failed to execute query: table 'users' not found",
                ErrorLevel.CRITICAL,
                new IllegalStateException(
                        "Table 'users' does not exist."
                )
        );

databaseError
        .addMetadata("userId", "1042")
        .addMetadata("query", "SELECT * FROM users")
        .addMetadata("retryCount", "3");

databaseError.setConfig(
        new ExceptionConfiguration()
                .setShowTimestamp(true)
                .setShowErrorLevel(true)
                .setShowErrorCount(true)
                .setShowMetadata(true)
);

System.out.println(databaseError);

databaseError.writeLog();
```

---
## Why EnrichableException?

Java already provides `Throwable.addSuppressed()` for attaching additional exceptions to a throwable. That's useful, but it solves a different problem.

| Feature                                 | `Throwable.addSuppressed()`      | `EnrichableException`            |
| --------------------------------------- | -------------------------------- | -------------------------------- |
| Attach another `Throwable`              | Yes                              | Yes, through the exception cause |
| Add structured error information        | No                               | Yes                              |
| Error context                           | No                               | Yes                              |
| Error code                              | No                               | Yes                              |
| Error level                             | No                               | Yes                              |
| Timestamp per error                     | No                               | Yes                              |
| Custom metadata                         | No                               | Yes                              |
| Multiple related error entries          | Limited to suppressed exceptions | Yes                              |
| Configurable output                     | No                               | Yes                              |
| Formatted error report                  | No                               | Yes                              |
| Designed for structured error reporting | No                               | Yes                              |
| Annotation-based exception creation     | No                               | Yes                              |

`addSuppressed()` is mainly useful when one operation encounters additional exceptions that should not replace the original exception.

`EnrichableException` is designed for a different job: **making errors carry structured, human-readable context that can be logged and inspected later.**

So this isn't really:

> "Java's way vs. our way."

It's more like:

> `addSuppressed()` tells you what other exceptions happened.
> `EnrichableException` tells you what happened, where, why, how severe it was, and gives you extra context to investigate it.

---
## Project Status

This project is actively evolving as I continue exploring better ways to design and manage exceptions in Java.

It is still a work in progress, and the API and design may change as the project grows.
