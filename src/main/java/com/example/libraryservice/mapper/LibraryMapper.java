package com.example.libraryservice.mapper;


import com.example.libraryservice.dto.LibraryDto;
import com.example.libraryservice.entity.Library;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LibraryMapper {
    LibraryMapper INSTANCE = Mappers.getMapper(LibraryMapper.class);

    Library LibraryDtoToLibrary(LibraryDto libraryDto);
}
