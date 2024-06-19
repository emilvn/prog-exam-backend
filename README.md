# Programmerings eksamen juni 2024 - Backend
By Emil Vennervald Nielsen

Made with [Spring Boot](https://spring.io/projects/spring-boot) and [Maven](https://maven.apache.org/)

Frontend repository: [prog-exam-frontend](https://github.com/emilvn/prog-exam-frontend)

## Setup
Clone the repository and enter the directory
```bash
git clone https://github.com/emilvn/prog-exam-backend.git
cd prog-exam-backend
```

in `src/main/resources` create a file called `env.properties` and add the following properties
```properties
DB_URL=your_db_url
DB_USER=your_db_username
DB_PASS=your_db_password
```

Open the project in IntelliJ IDEA and run application from the following class
```java 
src.main.java.dk.emilvn.exam.ExamApplication
```

You can now access the API at `http://localhost:8080`


## Tests

To run all tests, run the following command
```bash
mvn test
```

The application is set up to use h2 in-memory database for tests. 
If you instead want to use a dedicated test database for running tests, you can create a file called `env.test.properties` in `src/test/resources` and add the following properties
```properties
TEST_DB_URL=your_test_db_url
TEST_DB_USER=your_test_db_username
TEST_DB_PASS=your_test_db_password
```