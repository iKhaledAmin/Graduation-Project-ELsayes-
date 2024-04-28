package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.UserRepo;
import com.GP.ELsayes.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Transactional
    public User update(User updatedUser,Long userId){
        Optional<User> existedUserOptional = userRepo.findById(userId);
        if (existedUserOptional.isPresent()) {
            User existedUser = existedUserOptional.get();

            // Copy properties from updatedUser to existedUser
            existedUser.setFirstName(updatedUser.getFirstName());
            existedUser.setLastName(updatedUser.getLastName());
            existedUser.setUserName(updatedUser.getUserName());
            existedUser.setPassword(updatedUser.getPassword());
            existedUser.setEmail(updatedUser.getEmail());
            existedUser.setImage(updatedUser.getImage());
            existedUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existedUser.setBirthday(updatedUser.getBirthday());
            existedUser.setGender(updatedUser.getGender());
            existedUser.setUserRole(existedUser.getUserRole());
            // If there are any relationships like @OneToOne, handle them here

            return userRepo.save(existedUser);

        }
        return null;
    }

    @Override
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId) {
        Optional<User> existedUser = userRepo.findById(userId);
        if (existedUser.isPresent()){

            User updatedUser = userMapper.toEntity(profileRequest);

            updatedUser.setUserName(existedUser.get().getUserName());
            updatedUser = update(updatedUser,userId);

            return userMapper.toResponse(updatedUser);
        }

        return null;
    }


    @Override
    public Optional<User> getEntityByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    @Override
    public Optional<User> getEntityById(Long userId) {
        return userRepo.findById(userId);
    }

    @Override
    public User getById(Long userId) {
        return getEntityById(userId).orElseThrow(
                () -> new NoSuchElementException("There is no user with id = " + userId)
        );
    }




}
