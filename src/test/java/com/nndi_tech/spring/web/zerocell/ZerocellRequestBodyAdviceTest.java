package com.nndi_tech.spring.web.zerocell;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterScanner;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZerocellRequestBodyAdviceTest {
    static final Reflections reflections = new Reflections(DummyController.class, new MethodParameterScanner());

    @Test
    public void testSupportsZerocellRequestBodyAnnotation() {


        Method method = reflections.getMethodsWithAnyParamAnnotated(ZerocellRequestBody.class)
            .stream().findFirst().get();

        MethodParameter methodParameter = new MethodParameter(method, 0);

        ZerocellRequestBodyAdvice advice = new ZerocellRequestBodyAdvice();

        assertTrue(advice.supports(methodParameter, null, null));
    }

    private static class DummyController {
        public void foo(@ZerocellRequestBody List<Object> param) {}
    }
}
