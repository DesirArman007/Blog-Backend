package com.desirArman.blog.mapper;

import com.desirArman.blog.domain.dtos.PhotoDto;
import com.desirArman.blog.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(Photo photo);
}
