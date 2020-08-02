package com.nndi_tech.spring.web.zerocell;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZerocellRequestBody {
    String sheetName() default "";

    String formFile() default "file";
}
