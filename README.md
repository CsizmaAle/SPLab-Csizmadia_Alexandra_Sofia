# SPLab-Csizmadia_Alexandra_Sofia

- **Main:** [`demo/src/main/java/com/example/demo/DemoApplication.java`](demo/src/main/java/com/example/demo/DemoApplication.java)
- **Classes (book model, JPA entities):** [`demo/src/main/java/com/example/demo/models`](demo/src/main/java/com/example/demo/models)
- **Dependency injection examples:** [`demo/src/main/java/com/example/demo/difexamples`](demo/src/main/java/com/example/demo/difexamples)
- **Controllers:** [`demo/src/main/java/com/example/demo/controllers`](demo/src/main/java/com/example/demo/controllers)
- **Request logging filter:** [`demo/src/main/java/com/example/demo/filters`](demo/src/main/java/com/example/demo/filters)
- **Commands and executors:** [`demo/src/main/java/com/example/demo/commands`](demo/src/main/java/com/example/demo/commands)
- **Persistence (JPA repositories, CrudRepository, Adapters):** [`demo/src/main/java/com/example/demo/persistence`](demo/src/main/java/com/example/demo/persistence)
- **Lab PDFs:** [`Laboratoare_pdf`](Laboratoare_pdf)


### Run code

```bash
cd demo
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. Stop it with `Ctrl + C`.

Each request is logged (before and after processing) to `demo/logs/requests.log`.


### Books endpoints

| Method | URL | Description |
|---|---|---|
| GET | `/books` | all books |
| GET | `/books/{id}` | one book |
| POST | `/books` | creates a book asynchronously, returns `202` and a `requestId` |
| GET | `/books/requests/{requestId}` | status of an async request (`PENDING` / `DONE` / `FAILED`) |
| PUT | `/books/{id}` | changes the title and authors |
| DELETE | `/books/{id}` | deletes a book |

Example:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"title":"Carte 1","authors":["Ana Popescu"]}' \
  http://localhost:8080/books
```


### H2 database

The data is saved in `~/h2db/booksdb`. A demo book is created on the first start.

H2 console: `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:file:~/h2db/booksdb`
- **User Name:** `sa`
- **Password:** empty
