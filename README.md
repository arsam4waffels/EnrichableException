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
* File logging
* Input validation
* JUnit 5 tests

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
        .addMetaData("userId", "1042")
        .addMetaData("query", "SELECT * FROM users")
        .addMetaData("retryCount", "3");
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

If you want to write the exception to the log file:

```java
exception.log();
```

That's it.

---

## Validation

The library performs some basic validation so invalid information doesn't quietly make its way into an exception.

Required text values cannot be `null` or blank.

Metadata also has a few rules:

* `null` keys are rejected.
* `null` values are rejected.
* Blank keys become `BLANK`.
* Blank values become `BLANK`.

For example:

```java
exception.addMetaData("", "user-6969");
```

becomes:

```text
[BLANK=user-6969]
```

And:

```java
exception.addMetaData("userId", "");
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
        .addMetaData("userId", "1042")
        .addMetaData("query", "SELECT * FROM users")
        .addMetaData("retryCount", "3");

databaseError.setConfig(
        new ExceptionConfiguration()
                .setShowTimestamp(true)
                .setShowErrorLevel(true)
                .setShowErrorCount(true)
                .setShowMetadata(true)
);

System.out.println(databaseError);

databaseError.log();
```

---

## Project Status

This is still an active little side project.

Whenever I feel like adding something to it, I'll probably add it.

The API and design may change as the project grows.
