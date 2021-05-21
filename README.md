# TranstematicsTest
Тестовое задание для Сервисный Центр "Транстелематика"
ТЗ:
1. Анимированная заставка (SplashScreen) перед экраном авторизации, в это время должен проверяться коннект с интернетом и API.
2. Перед отображением информации должен быть экран авторизации с сохранением в Shared Prefs.
3. Выводить список (RecyclerView) друзей с фотографиями и минимальным описанием.
4. Выполнять просмотр более детальной информации.
5. Выполнять просмотр больших фотографий (фотографии должны кешироваться на устройстве).
6. Приложение должно иметь меню настроек где можно поменять, например, аватарку.
7. Приложение должно быть реализовано на 1 Активити (можно фрагменты, можно лэйауты).
8. Список друзей с фотографиями и кратким описанием должен сохраняться в локальной БД SQLite и обновляться.
9. В случае, если связи нет, но авторизация была произведена ранее - вывести список друзей из локальной БД и вывести на экран сообщение «нет связи».
Выполнено:
1. СплэшСкрин присутсвует, во время загрузки проверяется соединение
2. Авторизация происходит через VK Android SDK и сохраняет токен в Shared Prefs
3. Возвращается список с Аватаром, ФИО и статусом "онлайн"
4. При переходе на карточку пользователя отображается больше информации + аватар лучшего кач-ва
5. При нажатии на аватар в карточке пользователя открывается диалог с бОльшим изображением
6. Меню настроек присутсвует, но смена аватара выдает ошибку "FileNotFound" при выборе файла из галлереи, которая не исправлена
7. SingleActivity
8. Происходит авто-сохранение при загрузке через API
9. Реализован offline режим