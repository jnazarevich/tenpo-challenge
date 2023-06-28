# TENPO challenge

## Requerimientos

1 - Debes desarrollar una API REST en Spring Boot utilizando java 11 o superior, con las siguientes funcionalidades:

a) Debe contener un servicio llamado por api-rest que reciba 2 números, los sume, y le aplique una suba de un porcentaje que debe ser adquirido de un servicio externo (por ejemplo, si el servicio recibe 5 y 5 como valores, y el porcentaje devuelto por el servicio externo es 10, entonces (5 + 5) + 10% = 11). Se deben tener en cuenta las siguientes consideraciones:
- El servicio externo puede ser un mock, tiene que devolver el % sumado.
- Dado que ese % varía poco, podemos considerar que el valor que devuelve ese servicio no va cambiar por 30 minutos.
- Si el servicio externo falla, se debe devolver el último valor retornado. Si no hay valor, debe retornar un error la api.
- Si el servicio falla, se puede reintentar hasta 3 veces.

b) Historial de todos los llamados a todos los endpoint junto con la respuesta en caso de haber sido exitoso. Responder en Json, con data paginada. El guardado del historial de llamadas no debe sumar tiempo al servicio invocado, y en caso de falla, no debe impactar el llamado al servicio principal.

c) La api soporta recibir como máximo 3 rpm (request / minuto), en caso de superar ese umbral, debe retornar un error con el código http y mensaje adecuado.

d) El historial se debe almacenar en una database PostgreSQL.

e) Incluir errores http. Mensajes y descripciones para la serie 4XX.


2 - Se deben incluir tests unitarios.

3 - Esta API debe ser desplegada en un docker container. Este docker puede estar en un dockerhub público. La base de datos también debe correr en un contenedor docker. Recomendación usar docker compose

4 - Debes agregar un Postman Collection o Swagger para que probemos tu API

5 - Tu código debe estar disponible en un repositorio público, junto con las instrucciones de cómo desplegar el servicio y cómo utilizarlo.

6 - Tener en cuenta que la aplicación funcionará de la forma de un sistema distribuido donde puede existir más de una réplica del servicio funcionando en paralelo.

## Instalación

El servicio dockerizado se encuentra alojado en Docker-Hub. Bajarse la imagen mediante esta instrucción.

```bash
docker pull juannazarevich/app-latest:latest
```

Correr el servicio mediante docker-compose


```bash
docker-compose up app
```

## Configuración

Por default el servicio externo mockeado está configurado para que envíe siempre un resultado OK.
Esto se puede modificar mediante la siguiente property en el application.properties

```bash
postman-client.query.retrieve-percentage-data=ok (caso OK. Http status =200)
postman-client.query.retrieve-percentage-data=error (devuelve un http status = 500)
```


## API

### Ejecutar cálculo

Ejecuta el cálculo pedido. 

```json
método : POST
url : {{url}}/processes
{
    "firstNumber" : 15,
    "secondNumber" : 10,
    "type":"SYNC", (opcional)
    "callbackUrl":"https://webhook.com" (opcional)
}
```
Tiene dos modalidades. Si el atributo type (opcional) es SYNC se ejecuta de forma sincrónica. En este caso no haría falta enviar el callbackUrl

```json
http status : 200
{
    "referenceId":"{{uuid}}",
    "status":"COMPLETED",
    "result":"27.5"
}
```

Si el atributo type (opcional) es ASYNC se ejecuta de forma asincrónica. En este caso hace falta enviar el callbackUrl, para poder enviar el resultado a la url especificada.

```json
http status : 202
{
    "referenceId":"{{uuid}}",
    "status":"STARTED"
}
```

### Historial de llamadas

Se obtienen los llamados entrantes y salientes realizados en la app. Incluye paginación.

```json
método : GET
url : {{url}}/service-calls?size=1&page=1
```
Response:

```json
http status : 200
{
    "content": [
        {
            "httpMethod": "GET",
            "url": "http://localhost:8080/service-calls",
            "headers": "user-agent=PostmanRuntime/7.28.4;accept=*/*;postman-token=fcb5c80a-5e5f-4b34-9299-7274df4d6f77;host=localhost:8080;accept-encoding=gzip, deflate, br;connection=keep-alive;",
            "request": "",
            "response": "{\"content\":[{\"httpMethod\":\"GET\",\"url\":\"http://localhost:8080/service-calls\",\"headers\":\"user-agent=PostmanRuntime/7.28.4;accept=*/*;postman-token=7217eb8f-1154-4fab-b06e-4e7b985470bf;host=localhost:8080;accept-encoding=gzip, deflate, br;connection=keep-alive;\",\"request\":\"\",\"response\":\"{\\\"content\\\":[{\\\"httpMethod\\\":\\\"GET\\\",\\\"url\\\":\\\"http://localhost:8080/service-calls\\\",\\\"headers\\\":\\\"user-agent=PostmanRuntime/7.28.4;accept=*/*;postman-token=7217eb8f-1154-4fab-b06e-4e7b985470bf;host=localhost:8080;accept-encoding=gzip, deflate, br;connection=keep-alive;\\\",\\\"request\\\":\\\"\\\",\\\"response\\\":null,\\\"httpStatus\\\":null,\\\"type\\\":\\\"INCOMING\\\",\\\"createdAt\\\":\\\"2023-06-28T20:40:47.124647\\\"}],\\\"pageable\\\":{\\\"sort\\\":{\\\"empty\\\":true,\\\"sorted\\\":false,\\\"unsorted\\\":true},\\\"offset\\\":0,\\\"pageNumber\\\":0,\\\"pageSize\\\":20,\\\"paged\\\":true,\\\"unpaged\\\":false},\\\"totalPages\\\":1,\\\"totalElements\\\":1,\\\"last\\\":true,\\\"size\\\":20,\\\"number\\\":0,\\\"sort\\\":{\\\"empty\\\":true,\\\"sorted\\\":false,\\\"unsorted\\\":true},\\\"numberOfElements\\\":1,\\\"first\\\":true,\\\"empty\\\":false}\",\"httpStatus\":\"200\",\"type\":\"INCOMING\",\"createdAt\":\"2023-06-28T20:40:47.124647\"}],\"pageable\":{\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0,\"pageNumber\":0,\"pageSize\":20,\"paged\":true,\"unpaged\":false},\"totalPages\":1,\"totalElements\":1,\"last\":true,\"size\":20,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":1,\"first\":true,\"empty\":false}",
            "httpStatus": "200",
            "type": "INCOMING",
            "createdAt": "2023-06-28T20:41:37.206244"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 1,
        "pageNumber": 1,
        "pageSize": 1,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 9,
    "totalElements": 9,
    "last": false,
    "size": 1,
    "number": 1,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "numberOfElements": 1,
    "first": false,
    "empty": false
}
```

## Documentación

Se agrega en el repo collections de postman para este servicio y el servicio externo mockeado.
