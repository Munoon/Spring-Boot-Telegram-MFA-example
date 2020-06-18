# Spring Boot Telegram MFA example
Приложение, написанное на Spring Boot, показывает пример реализации функции "Подтверждение авторизации с помощью Telegram"

[DEMO](https://spring-telegram-mfa.herokuapp.com)
•
[YouTube DEMO](https://www.youtube.com/watch?v=7WucYd0-gPE)
•
[Статья на Habr](https://habr.com/ru/post/501728/)

## Запуск
1. Для запуска вам понадобиться Java 11 и Maven.
2. Запустите проект, используя комманду `mvn spring-boot:run`.

## Запуск версии 2.0
1. Версии 2.0 находиться на ветке `telegram_oauth`. Что бы её открыть, введите команду `git switch telegram_oauth`.
2. Телеграм требует, что бы приложение работало на порту 80. 
    Вы можете сменить порт в файле `src/main/resources/application.properties` и использовать приложения для переадресации запросов. К примеру nginx.
3. С помощью `@BotFather` бота добавьте домен. Для локальных приложений вы можете использовать IP адрес в локальной сети.
4. Запустите приложение точно также, как и первую версию, командой `mvn spring-boot:run`.