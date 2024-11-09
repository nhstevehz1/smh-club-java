package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.PhoneType;
import com.smh.club.api.dto.create.CreatePhoneDto;
import com.smh.club.api.dto.update.UpdatePhoneDto;

import java.util.ArrayList;
import java.util.List;

public class PhoneCreators {
    private PhoneCreators() {}

    public static PhoneEntity genPhoneEntity(int flag) {
        return createEntityBuilder(flag).build();
    }

    public static PhoneEntity genPhoneEntity(int flag, MemberEntity memberEntity) {
        return createEntityBuilder(flag)
                .member(memberEntity)
                .build();
    }

    public static PhoneDto genPhoneDto(int flag) {
        return PhoneDto.builder()
                .phoneNum("e_phone_" + flag)
                .phoneType(PhoneType.Mobile)
                .build();
    }

    public static CreatePhoneDto genCreatePhoneDto(int flag) {
        return CreatePhoneDto.builder()
                .phoneNum("c_pn" + flag)
                .phoneType(PhoneType.Mobile)
                .build();
    }

    public static UpdatePhoneDto genUpdatePhoneDto(int flag) {
        return UpdatePhoneDto.builder()
                .memberId(flag)
                .phoneNum("u_pn" + flag)
                .phoneType(PhoneType.Mobile)
                .build();
    }

    public static List<PhoneEntity> genPhoneEntityList(int size) {
        List<PhoneEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genPhoneEntity(ii+1)) ;
        }
        return list;
    }

    public static List<PhoneEntity> genPhoneEntityList(int size, MemberEntity member) {
        var list = genPhoneEntityList(size);
        list.forEach(e -> e.setMember(member));
        return list;
    }

    public static List<PhoneEntity> genPhoneEntityList(int size, int startFlag) {
        List<PhoneEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genPhoneEntity(ii + startFlag)) ;
        }
        return list;
    }

    public static List<PhoneDto> genPhoneDtoList(int size) {
        List<PhoneDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genPhoneDto(ii)) ;
        }
        return list;
    }

    private static PhoneEntity.PhoneEntityBuilder createEntityBuilder(int flag) {
        return PhoneEntity.builder()
                .phoneNum("e_phone_" + flag)
                .phoneType(PhoneType.Mobile);
    }
    
}
