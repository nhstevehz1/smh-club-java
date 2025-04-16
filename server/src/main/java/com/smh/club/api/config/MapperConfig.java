package com.smh.club.api.config;

import com.smh.club.api.domain.entities.*;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressMemberDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.email.EmailMemberDto;
import com.smh.club.api.dto.member.MemberDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.phone.PhoneMemberDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import com.smh.club.api.dto.renewal.RenewalMemberDto;
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
        emailSettings(modelMapper);
        phoneSettings(modelMapper);
        renewalSetting(modelMapper);

        return modelMapper;
    }

    private void memberSettings(ModelMapper modelMapper) {
        TypeMap<MemberDto, MemberEntity> entityMap
            = modelMapper.createTypeMap(MemberDto.class, MemberEntity.class);
        entityMap.addMappings(m -> {
            m.skip(MemberEntity::setId);
            m.skip(MemberEntity::setAddresses);
            m.skip(MemberEntity::setEmails);
            m.skip(MemberEntity::setPhones);
            m.skip(MemberEntity::setRenewals);
        });

        modelMapper.validate();
    }

    private void addressSettings(ModelMapper modelMapper) {
       TypeMap<AddressDto, AddressEntity> entityMap
           = modelMapper.createTypeMap(AddressDto.class, AddressEntity.class);
       entityMap.addMappings(m -> {
           m.skip(AddressEntity::setId);
           m.skip(AddressEntity::setMember);
       });

       TypeMap<AddressEntity, AddressDto> dtoMap
           = modelMapper.createTypeMap(AddressEntity.class, AddressDto.class);
       dtoMap.addMappings(m ->
          m.map(src -> src.getMember().getId(), AddressDto::setMemberId));

        addressMemberSettings(modelMapper);

       modelMapper.validate();
    }

    private void addressMemberSettings(ModelMapper modelMapper) {
        // AddressMember settings
        TypeMap<AddressEntity, AddressMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(AddressEntity.class, AddressMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getId(), AddressMemberDto::setMemberId);
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
        TypeMap<EmailDto, EmailEntity> entityMap
                = modelMapper.createTypeMap(EmailDto.class, EmailEntity.class);
        entityMap.addMappings(m -> {
            m.skip(EmailEntity::setId);
            m.skip(EmailEntity::setMember);
        });

        TypeMap<EmailEntity, EmailDto> dtoMap
            = modelMapper.createTypeMap(EmailEntity.class, EmailDto.class);
        dtoMap.addMappings(m ->
            m.map(src -> src.getMember().getId(), EmailDto::setMemberId));

        emailMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void emailMemberSettings(ModelMapper modelMapper) {
        // EmailMember settings
        TypeMap<EmailEntity, EmailMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(EmailEntity.class, EmailMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getId(), EmailMemberDto::setMemberId);
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

    }

    private void phoneSettings(ModelMapper modelMapper) {
        // Phone settings
        TypeMap<PhoneDto, PhoneEntity> entityMap
                = modelMapper.createTypeMap(PhoneDto.class, PhoneEntity.class);
        entityMap.addMappings(m -> {
            m.skip(PhoneEntity::setId);
            m.skip(PhoneEntity::setMember);
        });

        TypeMap<PhoneEntity, PhoneDto> dtoMap
            = modelMapper.createTypeMap(PhoneEntity.class, PhoneDto.class);
        dtoMap.addMappings(m ->
            m.map(src -> src.getMember().getId(), PhoneDto::setMemberId));

        phoneMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void phoneMemberSettings(ModelMapper modelMapper) {
        // PhoneMember settings
        TypeMap<PhoneEntity, PhoneMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(PhoneEntity.class, PhoneMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getId(), PhoneMemberDto::setMemberId);
            m.map(src -> src.getMember().getMemberNumber(), PhoneMemberDto::setMemberNumber);
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

    private void renewalSetting(ModelMapper modelMapper) {
        // Renewals settings
        TypeMap<RenewalDto, RenewalEntity> entityMap
                = modelMapper.createTypeMap(RenewalDto.class, RenewalEntity.class);
        entityMap.addMappings(m -> {
            m.skip(RenewalEntity::setId);
            m.skip(RenewalEntity::setMember);
        });

        TypeMap<RenewalEntity, RenewalDto> dtoMap
            = modelMapper.createTypeMap(RenewalEntity.class, RenewalDto.class);
        dtoMap.addMappings(m ->
            m.map(src -> src.getMember().getId(), RenewalDto::setMemberId));

        renewalMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void renewalMemberSettings(ModelMapper modelMapper) {
        // RenewalMember settings
        TypeMap<RenewalEntity, RenewalMemberDto> dtoTypeMap
            = modelMapper.createTypeMap(RenewalEntity.class, RenewalMemberDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getId(), RenewalMemberDto::setMemberId);
            m.map(src -> src.getMember().getMemberNumber(), RenewalMemberDto::setMemberNumber);
            m.map(src -> src.getMember().getFirstName(),
                (dest, v) -> dest.getFullName().setFirstName(String.valueOf(v)));
            m.map(src -> src.getMember().getMiddleName(),
                (dest, v) -> dest.getFullName().setMiddleName(String.valueOf(v)));
            m.map(src -> src.getMember().getLastName(),
                (dest, v) -> dest.getFullName().setLastName(String.valueOf(v)));
            m.map(src -> src.getMember().getSuffix(),
                (dest, v) -> dest.getFullName().setSuffix(String.valueOf(v)));
        });
    }
}
