# My blog
Personal blog powered by spring boot + thymeleaf. Also, check the Vue.js version in restapi branch. You can find front-end code on [blog-vue](https://github.com/minatoyukina/blog-vue). URL: http://47.102.218.113/ 

# Dependency
* spring boot 2.0.5 
* thymeleaf 3.0.9
* elastic-search 5.6.11
* spring security
* spring data jpa
* ~~thinker.md~~ editor.md
* toastr

# Build Setup
1. git clone or download zip.
2. open it with eclipse or intellij idea(recomended), build dependencies.
3. create a `blog` database(mysql 8.0+). execute `authority.sql` into `blog` database.
4. download elastic-search 5.6.11, run it at 9200/9300 port. start the project. then you are good to go.  
### NOTE: you can register a account and change athority_id to 1 in `user_authority` table to get admin role. don't forget to change `file.uploadFolder` in `application.properties` path to your own.

# TODO
* [x] replace thinker.md with editor.md(!important)
* [x] add comments reply function
* [ ] update OAuth2.0
* [x] add some interests modules
* [x] refactor front-end codes with Vue.js
