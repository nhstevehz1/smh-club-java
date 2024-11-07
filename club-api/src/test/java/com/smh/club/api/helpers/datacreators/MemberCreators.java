package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.MemberCreateDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberCreators {

    private MemberCreators() {}

    public static MemberDto createMemberDto(int flag) {
        return MemberDto.builder()
                .id(flag)
                .memberNumber(flag + 1)
                .firstName("d_first_" + flag)
                .middleName("d_middle_" + flag)
                .lastName("d_last_" + flag)
                .birthDate(LocalDate.now().minusYears(22 - flag))
                .joinedDate(LocalDate.now().minusYears(flag))
                .build();
    }

    public static MemberDetailDto createMemberDetailDto(int flag) {
        return MemberDetailDto.builder()
                .id(flag)
                .memberNumber(flag + 1)
                .firstName("d_first_" + flag)
                .middleName("d_middle_" + flag)
                .lastName("d_last_" + flag)
                .birthDate(LocalDate.now().minusYears(22 - flag))
                .joinedDate(LocalDate.now().minusYears(flag))
                .build();
    }

    public static MemberCreateDto createMemberCreateDto(int flag) {
        return MemberCreateDto.builder()
                .memberNumber(flag + 1)
                .firstName("d_first_" + flag)
                .middleName("d_middle_" + flag)
                .lastName("d_last_" + flag)
                .birthDate(LocalDate.now().minusYears(22 - flag))
                .joinedDate(LocalDate.now().minusYears(flag))
                .build();
    }

    public static MemberEntity createMemberEntity(int flag) {
        return MemberEntity.builder()
                .memberNumber(flag + 1)
                .firstName("e_first_" + flag)
                .middleName("e_middle_" + flag)
                .lastName("e_last_" + flag)
                .suffix("e_suf_" + flag)
                .birthDate(LocalDate.now().minusYears(22 - flag))
                .joinedDate(LocalDate.now().minusYears(flag))
                .build();
    }

    public static List<MemberDto> createMemberDtoList(int size) {
        List<MemberDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createMemberDto(ii));
        }
        return list;
    }

    public static List<MemberEntity> createMemeberEntityList(int size) {
        List<MemberEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++){
            list.add(createMemberEntity(ii));
        }
        return list;
    }

    public static List<MemberEntity> createMemeberEntityList(int size, int startFlag) {
        List<MemberEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++){
            list.add(createMemberEntity(ii + startFlag));
        }
        return list;
    }

}
