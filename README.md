# debit-cards-service

## Descripcion
Microservicio de gestion de tarjetas de debito y vinculacion de cuentas.

Diagrama de microservicos completo

<img width="670" height="450" alt="image" src="https://github.com/user-attachments/assets/e4505c9e-768d-4937-ab64-778c4b129cb2" />

## Endpoints
- `GET /api/v1/debit-cards`
- `POST /api/v1/debit-cards`
- `GET /api/v1/debit-cards/{id}`
- `PUT /api/v1/debit-cards/{id}`
- `DELETE /api/v1/debit-cards/{id}`
- `POST /api/v1/debit-cards/{id}/link-account`
- `POST /api/v1/debit-cards/{id}/unlink-account`
- `GET /api/v1/debit-cards/{id}/accounts`

## Nota
El `docker-compose.yml` del entorno esta en (`yanki-service`).

## Proyectos relacionados
- https://github.com/vjoyaroj/bank-config-repo
- https://github.com/vjoyaroj/microservices-config
- https://github.com/vjoyaroj/eureka-server
- https://github.com/vjoyaroj/yanki-service
- https://github.com/vjoyaroj/api-gateway
- https://github.com/vjoyaroj/transactions-service
- https://github.com/vjoyaroj/debit-cards-service
- https://github.com/vjoyaroj/customer-service
- https://github.com/vjoyaroj/credits-service
- https://github.com/vjoyaroj/accounts-service
