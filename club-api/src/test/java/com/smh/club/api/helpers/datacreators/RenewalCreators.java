package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RenewalCreators {
    private RenewalCreators() {}

    public static RenewalEntity genRenewalEntity(int flag) {
        return createEntityBuilder(flag).build();
    }

    public static RenewalEntity genRenewalEntity(int flag, MemberEntity memberEntity) {
        return createEntityBuilder(flag)
                .member(memberEntity)
                .build();
    }

    public static RenewalDto genRenewalDto(int flag) {
        return RenewalDto.builder()
                .id(flag)
                .memberId(flag)
                .renewalDate(LocalDate.now().minusYears(flag))
                .renewalYear(String.valueOf(LocalDate.now().minusYears(flag).getYear()))
                .build();
    }

    public static CreateRenewalDto genCreateRenewalDto(int flag) {
        return CreateRenewalDto.builder()
                .memberId(flag)
                .renewalDate(LocalDate.now().minusYears(flag))
                .renewalYear(String.valueOf(LocalDate.now().minusYears(flag).getYear()))
                .build();
    }

    public static UpdateRenewalDto genUpdateRenewalDto(int flag) {
        return UpdateRenewalDto.builder()
                .memberId(flag)
                .renewalDate(LocalDate.now().minusYears(flag))
                .renewalYear(String.valueOf(LocalDate.now().minusYears(flag).getYear()))
                .build();
    }

    public static List<RenewalEntity> genRenewalEntityList(int size) {
        List<RenewalEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genRenewalEntity(ii+1)) ;
        }
        return list;
    }

    public static List<RenewalEntity> genRenewalEntityList(int size, MemberEntity member) {
        var list = genRenewalEntityList(size);
        list.forEach(e -> e.setMember(member));
        return list;
    }

    public static List<RenewalEntity> genRenewalEntityList(int size, int startFlag) {
        List<RenewalEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genRenewalEntity(ii + startFlag)) ;
        }
        return list;
    }

    public static List<RenewalDto> genRenewalDtoList(int size) {
        List<RenewalDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genRenewalDto(ii)) ;
        }
        return list;
    }

    private static RenewalEntity.RenewalEntityBuilder createEntityBuilder(int flag) {
        return RenewalEntity.builder()
                .renewalDate(LocalDate.now().minusYears(flag))
                .renewalYear(String.valueOf(LocalDate.now().minusYears(flag).getYear()));
    }
}
