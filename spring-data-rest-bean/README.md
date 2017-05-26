# spring-data-rest-bean

http://localhost:8080/spring-data-rest-bean/api/persons

Con postman: POST - Body - JSON (application/json)
{
   "personName": "Gino",
   "personAge": 11
}

http://localhost:8080/spring-data-rest-bean/api/persons/search/byName?name=Gino

OR

c:\> curl -H "Content-Type: application/json" -X POST -d {\"personName\":\"mkyong\",\"personAge\":99} http://localhost:8080/spring-data-rest-bean/api/persons