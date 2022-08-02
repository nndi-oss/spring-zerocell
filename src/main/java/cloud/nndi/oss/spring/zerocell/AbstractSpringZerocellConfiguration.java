package cloud.nndi.oss.spring.zerocell;

/**
 * COnfiguration for Zerocell integration
 */
public abstract class AbstractSpringZerocellConfiguration {

    /**
     * @return upload directory where files are stored post-upload
     */
    public abstract String getUploadPath();

    /**
     *
     * @return whether to deleted Uploaded Files after extracting data
     */
    public abstract boolean deleteUploadedFiles();
}