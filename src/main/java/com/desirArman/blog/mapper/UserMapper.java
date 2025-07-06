package com.desirArman.blog.mapper;

import com.desirArman.blog.domain.dtos.CreateUserResponseDto;
import com.desirArman.blog.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    CreateUserResponseDto toDto(User user);
}
