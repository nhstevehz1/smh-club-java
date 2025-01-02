package com.smh.club.api.hateoas.config;

import com.smh.club.api.hateoas.domain.entities.*;
import com.smh.club.api.hateoas.models.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for {@link ModelMapper}
 */
@Configuration
public class MapperConfig {

    /**
     * Creates and configs a {@link ModelMapper}.
     * @return A {@link ModelMapper}.
     */
    @Bean
    public ModelMapper createModelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        memberSettings(modelMapper);
        addressSettings(modelMapper);
        emailSettings(modelMapper);
        phoneSettings(modelMapper);
        renewalSetting(modelMapper);

        return modelMapper;
    }

    private void memberSettings(ModelMapper modelMapper) {
        TypeMap<MemberModel, MemberEntity> entityTypeMap
            = modelMapper.createTypeMap(MemberModel.class, MemberEntity.class);
        entityTypeMap.addMappings(m -> m.skip(MemberEntity::setId));
    }

    private void addressSettings(ModelMapper modelMapper) {
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
        TypeMap<EmailEntity, EmailModel> dtoTypeMap
                = modelMapper.createTypeMap(EmailEntity.class, EmailModel.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), EmailModel::setMemberId));

        TypeMap<EmailModel, EmailEntity> entTypeMap
                = modelMapper.createTypeMap(EmailModel.class, EmailEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(EmailEntity::setMember);
            m.skip(EmailEntity::setId);
        });
    }

    private void phoneSettings(ModelMapper modelMapper) {
        // Phone settings
        TypeMap<PhoneEntity, PhoneModel> dtoTypeMap
                = modelMapper.createTypeMap(PhoneEntity.class, PhoneModel.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), PhoneModel::setMemberId));

        TypeMap<PhoneModel, PhoneEntity> entTypeMap
                = modelMapper.createTypeMap(PhoneModel.class, PhoneEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(PhoneEntity::setMember);
            m.skip(PhoneEntity::setId);
        });
    }

    private void renewalSetting(ModelMapper modelMapper) {
        // Renewals settings
        TypeMap<RenewalEntity, RenewalModel> dtoTypeMap
                = modelMapper.createTypeMap(RenewalEntity.class, RenewalModel.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), RenewalModel::setMemberId));

        TypeMap<RenewalModel, RenewalEntity> entTypeMap
                = modelMapper.createTypeMap(RenewalModel.class, RenewalEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(RenewalEntity::setMember);
            m.skip(RenewalEntity::setId);
        });
    }
}
