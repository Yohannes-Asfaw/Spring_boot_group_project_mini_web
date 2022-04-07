package com.kelem.kelem.dao;

import java.util.List;

import com.kelem.kelem.model.UserModel;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserModel, Long>{
    public UserModel findByUsername(String username);
    public List<UserModel> findAllByRole(String role);
}
