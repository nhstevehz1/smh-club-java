package com.smh.club.api.rest.config;

import com.smh.club.api.rest.domain.entities.*;
import com.smh.club.api.rest.dto.*;
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
        addressSettings(modelMapper);
        addressMemberSettings(modelMapper);
        emailSettings(modelMapper);
        emailMemberSettings(modelMapper);
        phoneSettings(modelMapper);
        renewalSetting(modelMapper);

        return modelMapper;
    }

    private void memberSettings(ModelMapper modelMapper) {
        TypeMap<MemberDto, MemberEntity> entityTypeMap
                = modelMapper.createTypeMap(MemberDto.class, MemberEntity.class);
        entityTypeMap.addMappings(m -> {
            m.skip(MemberEntity::setId);
            m.skip(MemberEntity::setAddresses);
            m.skip(MemberEntity::setEmails);
            m.skip(MemberEntity::setPhones);
            m.skip(MemberEntity::setRenewals);
        });

        modelMapper.validate();
    }

    private void addressSettings(ModelMapper modelMapper) {
        // Address settings
        TypeMap<AddressEntity, AddressDto> dtoTypeMap
                = modelMapper.createTypeMap(AddressEntity.class, AddressDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), AddressDto::setMemberId));


        TypeMap<AddressDto, AddressEntity> entTypeMap
                = modelMapper.createTypeMap(AddressDto.class, AddressEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(AddressEntity::setMember);
            m.skip(AddressEntity::setId);
        });
        modelMapper.validate();
    }

    private void addressMemberSettings(ModelMapper modelMapper) {
        // AddressMember settings
        TypeMap<AddressEntity, AddressMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(AddressEntity.class, AddressMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), AddressMemberDto::setMemberNumber);

            m.map(src -> src.getMember().getFirstName(),
                (dest, v) -> dest.getFullName().setFirstName(String.valueOf(v)));

            m.map(src -> src.getMember().getMiddleName(),
                (dest, v) -> dest.getFullName().setMiddleName(String.valueOf(v)));

            m.map(src -> src.getMember().getLastName(),
                (dest, v) -> dest.getFullName().setLastName(String.valueOf(v)));

            m.map(src -> src.getMember().getSuffix(),
                (dest, v) -> dest.getFullName().setSuffix(String.valueOf(v)));
        });

        modelMapper.validate();
    }
    private void emailSettings(ModelMapper modelMapper) {
        // Email settings
        TypeMap<EmailEntity, EmailDto> dtoTypeMap
                = modelMapper.createTypeMap(EmailEntity.class, EmailDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), EmailDto::setMemberId));

        TypeMap<EmailDto, EmailEntity> entTypeMap
                = modelMapper.createTypeMap(EmailDto.class, EmailEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(EmailEntity::setMember);
            m.skip(EmailEntity::setId);
        });
        modelMapper.validate();
    }

    private void emailMemberSettings(ModelMapper modelMapper) {
        // EmailMember settings
        TypeMap<EmailEntity, EmailMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(EmailEntity.class, EmailMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), EmailMemberDto::setMemberNumber);

            m.map(src -> src.getMember().getFirstName(),
                (dest, v) -> dest.getFullName().setFirstName(String.valueOf(v)));

            m.map(src -> src.getMember().getMiddleName(),
                (dest, v) -> dest.getFullName().setMiddleName(String.valueOf(v)));

            m.map(src -> src.getMember().getLastName(),
                (dest, v) -> dest.getFullName().setLastName(String.valueOf(v)));

            m.map(src -> src.getMember().getSuffix(),
                (dest, v) -> dest.getFullName().setSuffix(String.valueOf(v)));
        });

        modelMapper.validate();
    }

    private void phoneSettings(ModelMapper modelMapper) {
        // Phone settings
        TypeMap<PhoneEntity, PhoneDto> dtoTypeMap
                = modelMapper.createTypeMap(PhoneEntity.class, PhoneDto.class);
        dtoTypeMap.addMappings(m -> m.map(src -> src.getMember().getId(), PhoneDto::setMemberId));

        TypeMap<PhoneDto, PhoneEntity> entTypeMap
                = modelMapper.createTypeMap(PhoneDto.class, PhoneEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(PhoneEntity::setMember);
            m.skip(PhoneEntity::setId);
        });
        modelMapper.validate();
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
        modelMapper.validate();
    }
}
