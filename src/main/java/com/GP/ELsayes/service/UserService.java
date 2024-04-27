package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
   // public abstract UserResponse editProfile(UserRequest userRequest ,Long userId);
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId);

    public Optional<User> getEntityByUserName(String userName);

    Optional<User> getEntityById(Long userId);
}
