package com.nndi_tech.spring.web.zerocell;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodParameterScanner;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZerocellRequestBodyArgumentResolverTest {
    ZerocellRequestBodyArgumentResolver resolver = new ZerocellRequestBodyArgumentResolver();
    static final Reflections reflections = new Reflections(DummyController.class, new MethodParameterScanner());

    @Test
    public void testSupportsZerocellRequestBodyAnnotation() {
        MethodParameter methodParameter = dummyControllerMethodParameter();
        assertTrue(resolver.supportsParameter(methodParameter));
    }

    @Test
    public void testResolveMultipartRequest() {
        NativeWebRequest request = (NativeWebRequest) requestWithPart("file", "", "");

        MethodParameter methodParameter = dummyControllerMethodParameter();
        ModelAndViewContainer mav = new ModelAndViewContainer();
        DefaultDataBinderFactory bf = null; // not required for resolver

        Object result = resolver.resolveArgument(methodParameter, mav, request, bf);

        assertNotNull(result);
        assertTrue(result instanceof List);

        List resultList = (List) result;

        assertFalse(resultList.isEmpty());
    }

    private MethodParameter dummyControllerMethodParameter() {
        Method method = reflections.getMethodsWithAnyParamAnnotated(ZerocellRequestBody.class)
            .stream().findFirst().get();

        return new MethodParameter(method, 0);
    }

    private StandardMultipartHttpServletRequest requestWithPart(String name, String disposition, String content) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockPart part = new MockPart(name, null, content.getBytes(StandardCharsets.UTF_8));
        part.getHeaders().set("Content-Disposition", disposition);
        request.addPart(part);
        return new StandardMultipartHttpServletRequest(request);
    }

    private static class DummyController {
        public void foo(@ZerocellRequestBody List<Object> param) {}
    }

}
