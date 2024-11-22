package com.smh.club.api.hateoas.config;

import com.smh.club.api.data.domain.entities.*;
import com.smh.club.api.hateoas.models.MemberModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper createModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        memberSettings(modelMapper);
        //addressSettings(modelMapper);
        //emailSettings(modelMapper);
        //phoneSettings(modelMapper);
        //renewalSetting(modelMapper);

        return modelMapper;
    }

    private void memberSettings(ModelMapper modelMapper) {
        TypeMap<MemberModel, MemberEntity> entityTypeMap
                = modelMapper.createTypeMap(MemberModel.class, MemberEntity.class);
        entityTypeMap.addMappings(m -> m.skip(MemberEntity::setId));
    }

    /*private void addressSettings(ModelMapper modelMapper) {
        // Address settings
        TypeMap<AddressEntity, AddressModel> dtoTypeMap
                = modelMapper.createTypeMap(AddressEntity.class, AddressModel.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), AddressModel::setMemberId));


        TypeMap<AddressModel, AddressEntity> entTypeMap
                = modelMapper.createTypeMap(AddressModel.class, AddressEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(AddressEntity::setMember);
            m.skip(AddressEntity::setId);
        });
    }

    private void emailSettings(ModelMapper modelMapper) {
        // Email settings
        TypeMap<EmailEntity, EmailMapper> dtoTypeMap
                = modelMapper.createTypeMap(EmailEntity.class, EmailMapper.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), EmailMapper::setMemberId));

        TypeMap<EmailMapper, EmailEntity> entTypeMap
                = modelMapper.createTypeMap(EmailMapper.class, EmailEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(EmailEntity::setMember);
            m.skip(EmailEntity::setId);
        });
    }

    private void phoneSettings(ModelMapper modelMapper) {
        // Phone settings
        TypeMap<PhoneEntity, PhoneMapper> dtoTypeMap
                = modelMapper.createTypeMap(PhoneEntity.class, PhoneMapper.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), PhoneMapper::setMemberId));

        TypeMap<PhoneMapper, PhoneEntity> entTypeMap
                = modelMapper.createTypeMap(PhoneMapper.class, PhoneEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(PhoneEntity::setMember);
            m.skip(PhoneEntity::setId);
        });
    }

    private void renewalSetting(ModelMapper modelMapper) {
        // Renewals settings
        TypeMap<RenewalEntity, RenewalDto> dtoTypeMap
                = modelMapper.createTypeMap(RenewalEntity.class, RenewalDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), RenewalDto::setMemberId));

        TypeMap<RenewalDto, RenewalEntity> entTypeMap
                = modelMapper.createTypeMap(RenewalDto.class, RenewalEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(RenewalEntity::setMember);
            m.skip(RenewalEntity::setId);
        });
    }*/
}
