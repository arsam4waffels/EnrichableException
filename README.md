# EnrichableException

**Better exception handling, enriched.**

`EnrichableException` is a small Java exception-handling utility that lets you attach multiple pieces of information to a single exception.

Instead of throwing an exception and getting:

```text
Something went wrong.
```

you can make it say:

```text
[ERROR-1][2026-08-23 23:15:42][ERROR][Database:DB-001] Connection failed
[ERROR-2][2026-08-23 23:15:42][WARNING][Cache:CACHE-004] Cache was unavailable
[ERROR-3][2026-08-23 23:15:42][CRITICAL][Application:APP-999] Application cannot continue
```

Because apparently one error message wasn't enough.

## How it works

An `EnrichableException` contains a list of `ExceptionInformation` objects.

Every time something relevant happens, you can add more information to the same exception:

```java
EnrichableException exception = new EnrichableException(
        "Database",
        "DB-001",
        "Connection failed",
        ErrorLevel.ERROR
);

exception.addInformation(
        "Cache",
        "CACHE-004",
        "Cache was unavailable",
        ErrorLevel.WARNING
);

exception.addInformation(
        "Application",
        "APP-999",
        "Application cannot continue",
        ErrorLevel.CRITICAL
);

throw exception;
```

So instead of losing context while an error travels through the application, the exception can carry that context with it.

## Error Levels

The project currently provides four levels:

```java
public enum ErrorLevel {
    INFO,
    WARNING,
    ERROR,
    CRITICAL
}
```

In other words:

```text
INFO      -> Nothing to panic about.
WARNING   -> Something feels... off.
ERROR     -> Okay, we have a problem.
CRITICAL  -> Everyone remain calm.. or not.
```

## Configuration

The output can be configured using `ExceptionConfiguration`.

```java
ExceptionConfiguration configuration = new ExceptionConfiguration()
        .setShowTimestamp(true)
        .setShowErrorLevel(true)
        .setShowErrorCount(true)
        .setLogToFile(true);

EnrichableException.setConfig(configuration);
```

Available options:

```text
setShowTimestamp(...)
    Show the date and time of each error.

setShowErrorLevel(...)
    Show the severity of each error.

setShowErrorCount(...)
    Number each stored error.

setLogToFile(...)
    Write the generated error information to errors.log.
```

All options are disabled by default.

By default:

```text
We pretend everything is fine.
```

## Logging

If file logging is enabled:

```java
.setLogToFile(true)
```

the generated exception information will also be appended to:

```text
errors.log
```

Because apparently the console isn't enough.

## Why?

Normal exceptions usually tell you **what** went wrong.

`EnrichableException` tries to preserve more of the story:

```text
Where?
What?
When?
How serious?
What happened before?
What happened after?
```

The idea is simple:

```text
Exception
    |
    +-- Information #1
    |
    +-- Information #2
    |
    +-- Information #3
    |
    +-- Information #n
```

One exception.

More context.

Less detective work.

## Current State

This is a small experimental Java project focused on exception handling, exception enrichment, configuration, and basic error logging.

It is intentionally simple for now.. and tbh I'm too lazy to expand it..

And yes, the exception is currently keeping a diary.

