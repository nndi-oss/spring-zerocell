Zerocell for Spring
===

[zerocell](https://github.com/creditdatamw/zerocell) integration for Spring Boot. It enables you to upload and extract data quickly, efficiently and _automagically_ from Excel files in your Spring application controllers. It's so magical that you won't see or write any code to read and map data from an Excel Sheet to a POJO.

## Quick Start example

The code below is a complete Spring Boot application that allows users to 
  * upload an Excel file, 
  * process the Excel file by reading each row and mapping it to a POJO
  * return the extracted Excel data as JSON data
  
_All without writing any code to actually deal with Excel!_

```java
public class PersonRow { 
    @Column(index=0, name= "ID")
    private String ID;
    @Column(index=1, name= "Name")
    private String name;
    @Column(index=2, name="Age")
    private int age;

    // .. getters and setters ... 
}

@RestController
@Import(SpringZerocellSupport.class)
public class ExampleController {
    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonRow> extractUsers(@ZerocellRequestBody List<PersonRow> peopleFromExcel) {
        return peopleFromExcel; // Return the loaded data to the user
    }
}

@SpringBootApplication
public class ExampleApplication {
    public static void main(String... args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```

A simple form to interact with the endpoint would looks something like this;

```html
<!doctype html>
<html>
<body>
    <h1>Spring Zerocell Example</h1>

    <form method="post" action="/upload" enctype="multipart/form-data">
        Upload an excel with the following columns 'ID', 'Name', 'Age'
        <input type="file" name="file" >
        <button>Upload</button>
    </form>
</body>
</html>
```

A cURL request to the endpoint would look something like this:

```sh
$ curl -XPOST -F "file=@customers.xlsx" http://localhost:8080/upload
```

## Installation / Usage

Not yet on any repositories, so clone it and install it locally:

```sh
$ git clone https://github.com/nndi-oss/spring-zerocell.git
$ cd spring-zerocell
$ mvn clean install
``` 

Add to your pom and enjoy :) :

```xml
<dependency>
    <groupId>com.nndi-tech.labs</groupId>
    <artifactId>spring-zerocell</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

> NOTE: This should not be considered production-ready...ymmv

---

Copyright (c) 2020, NNDI