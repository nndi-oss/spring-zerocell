package com.nndi_tech.spring.web.zerocell;

public abstract class AbstractSpringZerocellConfiguration {

    public abstract String getUploadPath();

    public abstract boolean deleteUploadedFiles();
}