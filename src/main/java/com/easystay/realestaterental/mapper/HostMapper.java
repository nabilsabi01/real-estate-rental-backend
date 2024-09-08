package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.HostDTO;
import com.easystay.realestaterental.entity.Host;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;
import com.easystay.realestaterental.entity.Property;

@Mapper(componentModel = "spring")
public interface HostMapper {
    @Mapping(target = "propertyIds", expression = "java(mapPropertyIds(host))")
    HostDTO toHostDTO(Host host);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Host toHost(HostDTO hostDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateHostFromDTO(HostDTO hostDTO, @MappingTarget Host host);

    default List<Long> mapPropertyIds(Host host) {
        return host.getProperties() != null ? host.getProperties().stream().map(Property::getId).collect(Collectors.toList()) : null;
    }
}
