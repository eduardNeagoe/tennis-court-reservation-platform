package com.tenniscourts.schedules;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    Schedule map(ScheduleDTO source);

    ScheduleDTO map(Schedule source);

    List<ScheduleDTO> map(List<Schedule> source);
}
