package cloud.nndi.oss.spring.zerocell;

import com.creditdatamw.zerocell.Reader;
import com.creditdatamw.zerocell.handler.EntityHandler;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class ZerocellRequestBodyConverter implements HttpMessageConverter<Object> {

    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return Reader.columnsOf(aClass).length > 0 && (
            mediaType.includes(MediaType.MULTIPART_FORM_DATA) ||
            mediaType.toString().contains("multipart/form-data"));
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(
            MediaType.MULTIPART_FORM_DATA,
            MediaType.MULTIPART_MIXED
        );
    }

    @Override
    public Object read(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        ZerocellRequestBodyArgumentResolver processor = new ZerocellRequestBodyArgumentResolver();
        return processor.resolveArgumentInternal("file", EntityHandler.DEFAULT_SHEET, aClass, httpInputMessage);
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {

    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return false;
    }
}
