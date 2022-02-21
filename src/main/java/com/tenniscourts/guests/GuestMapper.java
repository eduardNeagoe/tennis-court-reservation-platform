package com.tenniscourts.guests;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    Guest map(GuestDTO source);

    @InheritInverseConfiguration
    GuestDTO map(Guest source);

    @Mapping(target = "name", source = "name")
    Guest map(CreateGuestRequestDTO source);
}