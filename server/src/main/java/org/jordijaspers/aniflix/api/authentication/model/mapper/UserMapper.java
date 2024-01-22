package org.jordijaspers.aniflix.api.authentication.model.mapper;

import org.jordijaspers.aniflix.api.authentication.model.Role;
import org.jordijaspers.aniflix.api.authentication.model.User;
import org.jordijaspers.aniflix.api.authentication.model.request.RegisterUserRequest;
import org.jordijaspers.aniflix.api.authentication.model.response.RegisterResponse;
import org.jordijaspers.aniflix.api.authentication.model.response.UserResponse;
import org.jordijaspers.aniflix.config.SharedMapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = SharedMapperConfiguration.class)
public abstract class UserMapper {

    public abstract User toUser(RegisterUserRequest request);

    @Mapping(target = "authorities", expression = "java(mapRolesToAuthorities(user.getRoles()))")
    public abstract RegisterResponse toRegisterResponse(User user);

    @Mapping(target = "authorities", expression = "java(mapRolesToAuthorities(user.getRoles()))")
    @Mapping(target = "refreshToken", source = "refreshToken.value")
    @Mapping(target = "accessToken", source = "accessToken.value")
    @Mapping(target = "expiresAt", source = "accessToken.expiresAt")
    public abstract UserResponse toUserResponse(User user);

    public List<String> mapRolesToAuthorities(final List<Role> roles) {
        return roles.stream().map(Role::getAuthority).collect(Collectors.toList());
    }

}
