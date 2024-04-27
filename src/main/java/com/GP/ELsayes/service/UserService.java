package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
   // public abstract UserResponse editProfile(UserRequest userRequest ,Long userId);
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId);

}
