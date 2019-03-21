## Структура

    * `src`: папка с проектом IntelliJ IDEA

## Как работает

Главный метод — `Main()` в классе `Program`.

## База данных


Для работы с базой в папке `database` надо создать файл `DatabaseParameters.java`.
У класса должны быть статические текстовые поля

    * `url`: строка подключения к базе (протокол, хост, порт, имя базы данных)
    * `login`: логин пользователя базы данных, у которого есть права чтения, записи, редактирования для нужных таблиц
    * `password`: пароль пользователя

В базе данных должны быть созданы таблицы, как описано в задании

    * `address ( id int auto_increment not null, postcode varchar(256), country varchar(256), region varchar(256), city varchar(256), street varchar(256), house int, flat int, primary key (id) )`
    * `persons ( id int auto_increment not null, surname varchar(256), name varchar(256), middlename varchar(256), birthday date, gender varchar(1), inn varchar(12), address_id int not null, foreign key (address_id) references address(id), primary key (id) )`
