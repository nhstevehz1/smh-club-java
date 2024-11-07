package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.dto.update.UpdateEmailDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EmailMapperImpl extends DataObjectMapperBase implements EmailMapper {

    public EmailMapperImpl(ModelMapper mapper) {
        super(mapper);
        configureMapper(mapper);
    }

    @Override
    public EmailEntity toEntity(CreateEmailDto createEmailDto) {
        return modelMapper.map(createEmailDto, EmailEntity.class);
    }

    @Override
    public EmailDto toDto(EmailEntity entity) {
        return modelMapper.map(entity, EmailDto.class);
    }

    @Override
    public EmailEntity updateEntity(UpdateEmailDto updateEmailDto, EmailEntity entity) {
        modelMapper.map(updateEmailDto, entity);
        return entity;
    }

    @Override
    public List<EmailDto> toDtoList(List<EmailEntity> entityList) {
        return mapList(entityList, EmailDto.class);
    }

    @Override
    protected void configureMapper(ModelMapper mapper) {
        TypeMap<EmailEntity, EmailDto> dtoTypeMap
                = this.modelMapper.createTypeMap(EmailEntity.class, EmailDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), EmailDto::setMemberId));

        TypeMap<EmailDto, EmailEntity> entTypeMap
                = this.modelMapper.createTypeMap(EmailDto.class, EmailEntity.class);
        entTypeMap.addMappings(m -> m.skip(EmailEntity::setMember));
    }
}
