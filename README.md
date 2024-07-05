# Реализация аутентификации и авторизации с использованием Spring Security и JWT

## Описание:

Создать базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.

## Требования:

Настройте базовую конфигурацию Spring Security для вашего приложения.
Используйте JWT для аутентификации пользователей.
Создайте класс для генерации и проверки JWT токенов.

#### Контроллеры:
Создайте контроллеры для аутентификации и регистрации пользователей.
Реализуйте методы для создания нового пользователя и генерации JWT токена при успешной аутентификации.
Реализуйте сохранение пользователей в базу данных PostgreSQL.
Добавьте поддержку ролей пользователей и настройте авторизацию на основе ролей.

# Реализация:

В приложении создано два контроллера. 
1. Можно зарегистрировать нового пользователя или пройти аутентификацию для уже существующего пользователя и получить два JWT токена, access и refresh.
Время жизни токенов настраиваются через параметры приложения. Для упрощения приложения хранилищем refresh токена выбрана sql база
2. Используя полученный access токен можно получить доступ к защищённым данным в зависимости от ролей пользователя.

Доступен Swagger-UI во время работы   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Стек
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white "Java 17")
![Maven](https://img.shields.io/badge/Maven-green.svg?style=for-the-badge&logo=mockito&logoColor=white "Maven")
![Spring](https://img.shields.io/badge/Spring-blueviolet.svg?style=for-the-badge&logo=spring&logoColor=white "Spring")
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![GitHub](https://img.shields.io/badge/git-%23121011.svg?style=for-the-badge&logo=github&logoColor=white "Git")

* Язык: *Java 17*
* Автоматизация сборки: *Maven*
* Фреймворк: *Spring*
* База данных: *PostgreSQL*
* Контроль версий: *Git*