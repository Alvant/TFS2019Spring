# Web Automation Testing

## tinkoffvacanciesform

В классе `TestRunner` реализованы два теста для формы «Заполните анкету» на сайте https://www.tinkoff.ru/career/vacancies/

    * `test1`: Открыть сайт, в форме прокликать все поля анкеты, не заполняя, чтобы получить все имеющиеся сообщения об ошибке. Добавить шаги на проверку текста ошибок.
    * `test2`: Открыть сайт, в форме заполнить поля анкеты (фамилия и имя, дата рождения, электронная почта, мобильный телефон) невалидными значениями, чтобы получить все сообщения об ошибке. Добавить шаги на проверку текста ошибок.

Тесты записывались с помощью Katalon Recorder.
В папке `src\test\resources\katalon-output` лежат файлы с экспортированным из него java кодом (WebDriver+JUnit).

По примеру [проекта](https://github.com/fizerbrain/web-qa-training) добавлены `BaseRunner` и `BrowserFactory`.

Тесты можно запускать из командной строки с помощью Maven

```bash
mvn test -Dtest=TestRunner#test1,TestRunner#test2 -Dbrowser=chrome verify
```

Поддерживаются браузеры: `chrome`, `firefox`, `opera`.

Если возникнут проблемы с определением пути к веб-драйверам, можно указать путь к директории в команде
```bash
mvn test -Dtest=TestRunner#test1,TestRunner#test2 -Dbrowser=opera "-Dwebdriverspath=<path-to-drivers-folder>" verify
```
при этом программа будет считать, что в указанной папке лежат исполняемые файлы `chromedriver.exe`, `geckodriver.exe`, `operadriver.exe`.