package com.tenniscourts.tenniscourts;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TennisCourtMapper {
    TennisCourtDTO map(TennisCourt source);

    @InheritInverseConfiguration
    TennisCourt map(TennisCourtDTO source);
}
