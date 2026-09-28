# Development Documentation

## Introduction

### Archictecture & Principles

Servicerized is a payment API for renting services. Still, **it is meant to be scalable**, for this reason, it is expected that everything on it follows the principle of "always being prepared for growth".

For example, even though servicerized is specialized in renting services, its architecture is generic: products contain only the base of data, and a service is an extension of a product as 'VirtualProduct'. For this reason, the API is ready for implementing a 'PhysicalProduct' if necessary.

The same way, the orders are also generic and extensible. An order is the base for a 'VirtualServiceOrder' or any other kind of order that may need to exist.

## Development

### Development Documentation

#### Not implemented featuresp

In documentation, use **NOT_IMPLEMENTED** to define features that are not complete/available.

- Across controllers code (java), prefer using REST responses, with status code **NOT_IMPLEMENTED**.
- Across subservices code (java), prefer using shared package common-responses to implement **NOT_IMPLEMENTED** failure.

#### Documentation missing

In documentation, use **DOCUMENTATION_MISSING** to define documentation that is ausent.