package cloud.nndi.oss.spring.zerocell;

import com.creditdatamw.zerocell.Reader;
import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.handler.EntityHandler;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.singletonList;

public class ZerocellRequestBodyArgumentResolver extends AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TEMP_FILE_PREFIX = "_chinthu";

    public ZerocellRequestBodyArgumentResolver() {
        super(singletonList(new ZerocellRequestBodyConverter()),
            singletonList(new ZerocellRequestBodyAdvice()));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ZerocellRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
              NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        parameter = parameter.nestedIfOptional();

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");

        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

        final String genericTypeName = parameter.getNestedGenericParameterType().getTypeName();

        if (! genericTypeName.contains(java.util.List.class.getTypeName())) {
            throw new HttpMessageNotReadableException("Invalid container type for ZerocellRequestBody parameter, must be java.util.List", inputMessage);
        }
        String sheetName = EntityHandler.DEFAULT_SHEET;
        String uploadFileName = "file";

        ZerocellRequestBody annotation = parameter.getParameterAnnotation(ZerocellRequestBody.class);
        if (annotation != null) {
            uploadFileName = annotation.formFile();
            if (! annotation.sheetName().isEmpty()) {
                sheetName = annotation.sheetName();
            }
        }

        Object arg = resolveArgumentInternal(uploadFileName, sheetName,
            parameter.getNestedGenericParameterType(), inputMessage);

        if (arg == null) {
            throw new HttpMessageNotReadableException("Required request body is missing: " +
                parameter.getExecutable().toGenericString(), inputMessage);
        }

        return arg;
    }

    @SuppressWarnings("unchecked")
    protected List<Object> resolveArgumentInternal(String uploadFileName, String sheetName,
               Type type, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException {
        assert(httpInputMessage instanceof ServletServerHttpRequest);

        if (((ParameterizedType) type).getActualTypeArguments().length < 1) {
            throw new HttpMessageNotReadableException("Method parameter does not have required Type argument.", httpInputMessage);
        }

        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        Class<?> typeArgumentClass = (Class<?>) typeArgument;

        if (Reader.columnsOf(typeArgumentClass).length < 1) {
            throw new HttpMessageNotReadableException(String.format(
                "Invalid class: %s does not contain zerocell @Column annotations", typeArgumentClass.getName()),
                httpInputMessage);
        }

        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) httpInputMessage;
        StandardMultipartHttpServletRequest multipartRequest = null;
        if (servletServerHttpRequest.getServletRequest() instanceof StandardMultipartHttpServletRequest) {
            multipartRequest = (StandardMultipartHttpServletRequest) servletServerHttpRequest.getServletRequest();
        }

        if (multipartRequest == null) {
            throw new HttpMessageNotReadableException("Invalid request. Needs to be a 'multipart/form-data' request", httpInputMessage);
        }
        try {
            MultipartFile filePart = multipartRequest.getFile(uploadFileName);
            if (filePart == null) {
                throw new IOException();
            }
            return (List<Object>) extractRowsFromFile(typeArgumentClass, filePart, sheetName);
        } catch (IOException e) {
            throw new HttpMessageNotReadableException("Failed to read file part: " + uploadFileName, httpInputMessage);
        } catch (ZeroCellException e) {
            throw new HttpMessageNotReadableException(e.getMessage() + "  while processing: " + uploadFileName, httpInputMessage);
        }
    }

    @SuppressWarnings("rawtypes")
    private Object extractRowsFromFile(Class<?> rowClass, MultipartFile filePart, String sheetName) throws IOException {
        assert filePart != null;

        Path filePath = Files.createTempFile(TEMP_FILE_PREFIX, ".xlsx");
        filePart.transferTo(filePath);

        EntityHandler entityHandler = new EntityHandler<>(rowClass, sheetName, false, 0, 0);

        ReaderUtil.process(filePart.getInputStream(), sheetName, entityHandler.getEntitySheetHandler());

        List data = entityHandler.readAsList();
        entityHandler = null;

        if (! filePath.toFile().delete()) {
            LoggerFactory.getLogger(getClass()).warn("Failed to delete temporary file: " + filePath);
        }

        return data;
    }
}
