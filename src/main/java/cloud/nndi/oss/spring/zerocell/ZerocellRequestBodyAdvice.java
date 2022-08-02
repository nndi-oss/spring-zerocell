package cloud.nndi.oss.spring.zerocell;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

@ControllerAdvice
public class ZerocellRequestBodyAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Objects.requireNonNull(methodParameter, "methodParameter");
        return methodParameter.hasParameterAnnotation(ZerocellRequestBody.class);
    }
}
