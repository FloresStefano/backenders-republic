package com.example.mondriandemo.data;

import mondrian.spi.CatalogLocator;

import java.net.URL;

public class ClasspathCatalogLocator implements CatalogLocator {
    public String locate(String catalogPath) {

        URL resource = this.getClass().getClassLoader().getResource(catalogPath);

        if (resource == null) {
            throw new IllegalArgumentException("Cannot find in classpath: " + catalogPath);
        }

        return resource.toString();
    }
}
