package com.tagme.tagme_store_back.domain.model;

import java.util.List;
import java.util.Objects;

public class Category {
    Long id;
    String name;
    List<Category> subcategories;

    public Category(Long id, String name, List<Category> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name) && Objects.equals(subcategories, category.subcategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subcategories);
    }
}
