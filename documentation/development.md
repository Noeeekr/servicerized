# Development Documentation

### Marking not implemented features

In documentation, use "NOT_IMPLEMENTED" (without ") to define features that are not complete/available.
In documentation, use "DOCUMENTATION_MISSING" (without ") to define documentation that is ausent.

In services (java) prefer using shared package common-responses to implement NOT_IMPLEMENTED failure.
In controllers (java) prefer using REST responses, with status code NOT_IMPLEMENTED.
