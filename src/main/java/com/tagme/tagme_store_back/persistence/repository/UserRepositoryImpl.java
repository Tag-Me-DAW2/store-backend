package com.tagme.tagme_store_back.persistence.repository;

import com.tagme.tagme_store_back.domain.dto.UserDto;
import com.tagme.tagme_store_back.domain.repository.UserRepository;
import com.tagme.tagme_store_back.persistence.dao.jpa.UserJpaDao;
import com.tagme.tagme_store_back.persistence.dao.jpa.entity.UserJpaEntity;
import com.tagme.tagme_store_back.persistence.mapper.UserMapper;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private UserJpaDao userJpaDao;

    public UserRepositoryImpl(UserJpaDao userJpaDao) {
        this.userJpaDao = userJpaDao;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userJpaDao.findById(id)
                .map(UserMapper::fromUserJpaEntityToUserDto);
    }

    @Override
    public UserDto save(UserDto userDto) {
        UserJpaEntity userJpaEntity = UserMapper.fromUserDtoToUserJpaEntity(userDto);
        if (userJpaEntity.getId() == null) {
            return UserMapper.fromUserJpaEntityToUserDto(userJpaDao.insert(userJpaEntity));
        }

        return UserMapper.fromUserJpaEntityToUserDto(userJpaDao.update(userJpaEntity));
    }

    @Override
    public void deleteById(Long id) {
        userJpaDao.deleteById(id);
    }

    @Override
    public Optional<UserDto> findByToken(String token) {
        return userJpaDao.findByToken(token).map(UserMapper::fromUserJpaEntityToUserDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userJpaDao.findByEmail(email).map(UserMapper::fromUserJpaEntityToUserDto);
    }
}
