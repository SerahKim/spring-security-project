package com.security.user.mapper;

import com.security.user.dto.SignupReqDTO;
import com.security.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // SignupReqDTO -> UserEntity 변환
    @Mapping(target = "password", source = "hashedPwd")
    UserEntity toUserEntity(SignupReqDTO signupReqDTO, String hashedPwd);
}
