FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/reframe-todo-list.jar /reframe-todo-list/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/reframe-todo-list/app.jar"]
