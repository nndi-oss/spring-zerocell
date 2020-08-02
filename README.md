Zerocell for Spring
===

[zerocell](https://github.com/creditdatamw/zerocell) integration for Spring MVC/Spring Boot. It enables you to upload and extract data quickly, efficiently and _automagically_ from Excel files in your Spring application controllers.

It's so magical that you won't see or write any code to read and map data from an Excel Sheet to a POJO.

## Example 

The code below is a complete Spring Boot application that allows users to upload an Excel file and converts the rows -> POJOs -> JSON; all without writing any code to actually deal with Excel!

```java

// (1) Define our class to map from an Excel row
public class PersonRow { 
    @Column(index=0, name= "ID")
    private String ID;
    @Column(index=1, name= "Name")
    private String name;
    @Column(index=2, name="Age")
    private int age;

    // .. getters and setters ... 
}

// (2) Configure our Spring Boot application to recognize the @ZerocellRequestBody annotation
@Configuration
@EnableWebMvc
public class Config implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ZerocellRequestBodyArgumentResolver());
    }
}

// (3) Create our controller with application/json as the output media type
@RestController
public class ExampleController {
    @PostMapping(value = "/upload",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    // (3.1) Add @ZerocellRequestBody to indicate that we want to enable users to upload files
    // (3.2) Use our row class, PersonRow, to hold the data after zerocell loads it
    // (3.3) Note that the collection type MUST always be java.util.List
    public List<PersonRow> extractUsers(@ZerocellRequestBody List<PersonRow> peopleFromExcel) {
        return peopleFromExcel; // (4) Return the loaded data to the user
    }
}


@SpringBootApplication
public class ExampleApplication {
    public static void main(String... args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```

**So let's see what's actually happening in here**

1. We define our class to map from an Excel row
2. Configure our Spring Boot application to recognize the `@ZerocellRequestBody` annotation
3. Create our controller with application/json as the output media type    
  3.1. Add `@ZerocellRequestBody` to indicate that we want to enable users to upload Excel files handled by zerocell    
  3.2. Use our row class, PersonRow, to hold the data after zerocell loads it    
  3.3. Note that the collection type MUST always be java.util.List    
4. Return the loaded data to the user as JSON


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

## Usage

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