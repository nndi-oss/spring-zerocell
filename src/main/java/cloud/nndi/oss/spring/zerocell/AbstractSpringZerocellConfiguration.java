package cloud.nndi.oss.spring.zerocell;

public abstract class AbstractSpringZerocellConfiguration {

    public abstract String getUploadPath();

    public abstract boolean deleteUploadedFiles();
}