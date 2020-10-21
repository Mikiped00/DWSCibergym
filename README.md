# CyberGym
Web Application developed as a project in _Secure Web Development_ subject, in [_Cybersecurity Engineering Degree_](https://urjc.es/estudios/grado/3100-ingenieria-de-la-ciberseguridad) in Rey Juan Carlos University.

## What is it? 📖
<p align="justify">The application manages a <b>gym website</b>, with a <b>public site</b> where everyone can see the gym activities and its details. There're also <b>two private sections</b>, one for the administrator, who can make changes on the lessons and add and remove users from the databases, and another one for each registered user, who can book one lesson from the available activities. Users can also change their profile and private data, and login using <b>Oauth2</b> with Google or Github accounts. All functionality can be performed using <b>HTML templates with Model-View-Controller (MVC)</b>, and also using the provided <b>Rest API</b>. Data is stored in a MySQL Database.</p>
<p align="justify">Security mecanisms are implemented using Spring Security, with special emphasis to <b>vulnerabilities</b> such as SQLi, Cross-Site Scripting XSS, Broken Authentication and Broken Access Control, as well as Sensitive Data Exposure.</p>

### Deployment 📦
<p align="justify">Changes must be done in <i>DWSGym/src/main/resources/application.yml</i> in order to get the project working. MySQL Database user and password and Oauth2 tokens must be added to that file.</p>

### Used tools 🛠️
* [Spring Tools 4](https://spring.io/tools) - The Java IDE
* [Spring Security](https://spring.io/projects/spring-security) - Authentication and access control framework
* [MySQL Workbench](https://www.mysql.com/products/workbench/) - Used to manage MySQL Database
* [Thymeleaf](https://www.thymeleaf.org/) - Java template engine
* [Maven](https://maven.apache.org/) - Dependencies Handler

## Authors ✒️
* [Kaputt4](https://github.com/Kaputt4)
* [Mikiped00](https://github.com/Mikiped00)
* [G0nz4lo-4lvarez-H3rv4s](https://github.com/G0nz4lo-4lvarez-H3rv4s)
