package com.colvir.mapper;

import com.colvir.dto.ChannelDto;
import com.colvir.model.Channel;
import org.mapstruct.Mapper;

@Mapper
public interface ChannelMapper {

    ChannelDto toDto(Channel channel);

    Channel toEntity(ChannelDto channelDto);
}
