# Bank System

## Descripción

Este proyecto corresponde a la construcción de un sistema de información para un banco en Java.

El sistema fue desarrollado en dos entregas, iniciando con el modelado del dominio y posteriormente aplicando arquitectura DDD para la lógica de negocio.

## Qué se desarrolló en este proyecto

En el desarrollo del sistema se trabajaron las siguientes partes:

- usuarios del sistema
- clientes
- cuentas bancarias
- préstamos
- transferencias
- productos bancarios
- bitácora de operaciones
- servicios de negocio (DDD)
- puertos para persistencia
- reglas de negocio y validaciones

## Entrega 1 - Modelo del dominio

En esta fase se crearon las clases principales del modelo del banco, relacionadas con:

- usuarios del sistema
- clientes
- cuentas bancarias
- préstamos
- transferencias
- productos bancarios
- bitácora de operaciones

También se definieron varios enums para representar roles, estados, tipos de cuenta, tipos de préstamo y otras categorías necesarias dentro del modelo.

## Organización del proyecto (Entrega 1)

El proyecto quedó dividido en dos paquetes principales:

- bank_model: contiene las clases del modelo del banco
- bank_enums: contiene los enums usados por las clases del modelo

## Clases creadas

- User
- Customer
- NaturalPersonCustomer
- BusinessCustomer
- Account
- Loan
- Transfer
- BankProduct
- AuditLog

## Enums creados

- UserRole
- UserStatus
- AccountType
- AccountStatus
- CurrencyType
- LoanType
- LoanStatus
- TransferStatus
- ProductCategory

## Entrega 2 - Arquitectura DDD (Servicios y Puertos)

En esta fase se implementó la arquitectura DDD separando responsabilidades del sistema en capas.

Se agregaron servicios de negocio, interfaces (puertos) y lógica de validación para operaciones bancarias.

## Servicios implementados

- TransferService: manejo de transferencias bancarias con validaciones de seguridad, saldo, estados y reglas de negocio.
- LoanService: gestión de aprobación de préstamos con validación de roles y estados del sistema.

## Puertos (Interfaces)

- AccountPort: acceso a operaciones de cuentas
- LoanPort: acceso a préstamos
- TransferPort: acceso a transferencias
- AuditPort: registro de bitácora de operaciones

## Lógica implementada

- Validación de roles de usuario
- Control de estados en préstamos y transferencias
- Reglas de negocio para saldo y aprobaciones
- Separación de lógica usando arquitectura DDD


## Autor

Brandon Gallego
