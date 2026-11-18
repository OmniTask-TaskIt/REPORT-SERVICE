# RPAD service

Servicio Java/Spring Boot organizado con arquitectura hexagonal:

- `Application`: puertos de entrada/salida y casos de uso.
- `Domain`: modelo y puertos de persistencia, sin dependencias de Spring.
- `Infrastucture/Inbound`: API REST y DTOs.
- `Infrastucture/Outbound/Persistence`: adaptadores MongoDB y perfil local en memoria.
- `Infrastucture/Outbound/Messaging`: adaptador Kafka.


