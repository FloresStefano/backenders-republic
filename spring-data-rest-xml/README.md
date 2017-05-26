# spring-data-rest-xml

http://localhost:8080/spring-data-rest-xml/api/persons


Con postman: POST - Body - JSON (application/json)
{
   "personName": "Gino",
   "personAge": 11
}

http://localhost:8080/spring-data-rest-xml/api/persons/search/byName?name=Gino

OR

c:\> curl -H "Content-Type: application/json" -X POST -d {\"personName\":\"mkyong\",\"personAge\":99} http://localhost:8080/spring-data-rest-xml/api/persons