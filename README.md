# spring-lms-rest

Library Management System

## how to run:

1. Just opening LmsApplication.java and running main method will be enough for running app. No additional configuration
   needed
2. Another method is to run the following command in a terminal window: ./mvnw spring-boot:run

app uses h2-db, for connecting to it just leave everything default in h2-console and press 'connect'

Flyway is used for database migrations and some data will be inserted into db:
1. Admin user with username: 'user1' and password: 'password1'
2. User with username: 'user2' and password: "password2"
3. User with username: 'user3' and password: 'password3'
4. Three books
5. User roles