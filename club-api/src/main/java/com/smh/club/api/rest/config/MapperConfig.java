package com.smh.club.api.rest.config;

import com.smh.club.api.rest.domain.entities.*;
import com.smh.club.api.rest.dto.address.AddressCreateDto;
import com.smh.club.api.rest.dto.address.AddressFullNameDto;
import com.smh.club.api.rest.dto.address.AddressUpdateDto;
import com.smh.club.api.rest.dto.email.EmailCreateDto;
import com.smh.club.api.rest.dto.email.EmailFullNameDto;
import com.smh.club.api.rest.dto.email.EmailUpdateDto;
import com.smh.club.api.rest.dto.member.MemberCreateDto;
import com.smh.club.api.rest.dto.phone.PhoneCreateDto;
import com.smh.club.api.rest.dto.phone.PhoneDto;
import com.smh.club.api.rest.dto.phone.PhoneFullNameDto;
import com.smh.club.api.rest.dto.renewal.RenewalCreateDto;
import com.smh.club.api.rest.dto.renewal.RenewalDto;
import com.smh.club.api.rest.dto.renewal.RenewalFullNameDto;
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
        TypeMap<MemberCreateDto, MemberEntity> entityTypeMap
                = modelMapper.createTypeMap(MemberCreateDto.class, MemberEntity.class);
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
       TypeMap<AddressCreateDto, AddressEntity> createMap
           = modelMapper.createTypeMap(AddressCreateDto.class, AddressEntity.class);
       createMap.addMappings(m -> {
          m.skip(AddressEntity::setId);
          m.skip(AddressEntity::setMember);
       });

       TypeMap<AddressUpdateDto, AddressEntity> updateMap
           = modelMapper.createTypeMap(AddressUpdateDto.class, AddressEntity.class);
       updateMap.addMappings(m -> {
          m.skip(AddressEntity::setId);
          m.skip(AddressEntity::setMember);
       });

        TypeMap<AddressEntity, AddressFullNameDto> dtoTypeMap
            = modelMapper.createTypeMap(AddressEntity.class, AddressFullNameDto.class);

        addressMemberSettings(modelMapper);

       modelMapper.validate();
    }

    private void addressMemberSettings(ModelMapper modelMapper) {
        // AddressMember settings
        TypeMap<AddressEntity, AddressFullNameDto> dtoTypeMap
            = modelMapper.createTypeMap(AddressEntity.class, AddressFullNameDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), AddressFullNameDto::setMemberNumber);
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
        TypeMap<EmailCreateDto, EmailEntity> createMap
                = modelMapper.createTypeMap(EmailCreateDto.class, EmailEntity.class);
        createMap.addMappings(m -> {
            m.skip(EmailEntity::setId);
            m.skip(EmailEntity::setMember);
        });

        TypeMap<EmailUpdateDto, EmailEntity> updateMap
                = modelMapper.createTypeMap(EmailUpdateDto.class, EmailEntity.class);
        updateMap.addMappings(m -> {
            m.skip(EmailEntity::setMember);
            m.skip(EmailEntity::setId);
        });

        emailMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void emailMemberSettings(ModelMapper modelMapper) {
        // EmailMember settings
        TypeMap<EmailEntity, EmailFullNameDto> dtoTypeMap
            = modelMapper.createTypeMap(EmailEntity.class, EmailFullNameDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), EmailFullNameDto::setMemberNumber);
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
        TypeMap<PhoneCreateDto, PhoneEntity> createMap
                = modelMapper.createTypeMap(PhoneCreateDto.class, PhoneEntity.class);
        createMap.addMappings(m -> {
            m.skip(PhoneEntity::setMember);
            m.skip(PhoneEntity::setId);
        });

        TypeMap<PhoneDto, PhoneEntity> updateMap
                = modelMapper.createTypeMap(PhoneDto.class, PhoneEntity.class);
        updateMap.addMappings(m -> {
            m.skip(PhoneEntity::setMember);
            m.skip(PhoneEntity::setId);
        });

        phoneMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void phoneMemberSettings(ModelMapper modelMapper) {
        // PhoneMember settings
        TypeMap<PhoneEntity, PhoneFullNameDto> dtoTypeMap
            = modelMapper.createTypeMap(PhoneEntity.class, PhoneFullNameDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), PhoneFullNameDto::setMemberNumber);
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
        TypeMap<RenewalCreateDto, RenewalEntity> creatMap
                = modelMapper.createTypeMap(RenewalCreateDto.class, RenewalEntity.class);
        creatMap.addMappings(m -> {
            m.skip(RenewalEntity::setMember);
            m.skip(RenewalEntity::setId);
        });

        TypeMap<RenewalDto, RenewalEntity> entTypeMap
                = modelMapper.createTypeMap(RenewalDto.class, RenewalEntity.class);
        entTypeMap.addMappings(m -> {
            m.skip(RenewalEntity::setMember);
            m.skip(RenewalEntity::setId);
        });

        renewalMemberSettings(modelMapper);

        modelMapper.validate();
    }

    private void renewalMemberSettings(ModelMapper modelMapper) {
        // RenewalMember settings
        TypeMap<RenewalEntity, RenewalFullNameDto> dtoTypeMap
            = modelMapper.createTypeMap(RenewalEntity.class, RenewalFullNameDto.class);

        dtoTypeMap.addMappings(m -> {
            m.map(src -> src.getMember().getMemberNumber(), RenewalFullNameDto::setMemberNumber);
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
