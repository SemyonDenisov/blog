Запуск приложения:
1) git clone https://github.com/SemyonDenisov/blog.git
2) cd blog
3) в application-test.properties и application.properties 
поменять путь до папки blog/storage на свой  
4) ./mvnw clean package
5) Адрес приложения: http://localhost:8080/Blog 
   Приложение работает только на порту, указанному в теге base каждого файла в templates (проблема связана с ThymeLeaf)
6) Для повторного запуска и корректного отображения картинок, в случае 
редактирования постов: скопировать содержимое из папки storage_start_state в storage 