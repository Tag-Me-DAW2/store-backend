package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.CategoryDto;
import com.tagme.tagme_store_back.domain.repository.CategoryRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.CategoryJpaDao;
import com.tagme.tagme_store_back.persistence.mapper.CategoryMapper;

import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {
    private CategoryJpaDao categoryDao;

    public CategoryRepositoryImpl(CategoryJpaDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryDao.findAll().stream().map(CategoryMapper::fromCategoryJpaEntityToCategoryDto).toList();
    }

    @Override
    public Optional<CategoryDto> findById(Long id) {
        return categoryDao.findById(id).map(CategoryMapper::fromCategoryJpaEntityToCategoryDto);
    }

    @Override
    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        if (categoryDto.id() == null) {
            return CategoryMapper.fromCategoryJpaEntityToCategoryDto(
                    categoryDao.insert(CategoryMapper.fromCategoryDtoToCategoryJpaEntity(categoryDto))
            );
        } else {
            return CategoryMapper.fromCategoryJpaEntityToCategoryDto(
                    categoryDao.update(CategoryMapper.fromCategoryDtoToCategoryJpaEntity(categoryDto))
            );
        }
    }
}
