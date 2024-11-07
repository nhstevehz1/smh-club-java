package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.EmailCreateDto;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.EmailType;

import java.util.ArrayList;
import java.util.List;

public class EmailCreators {

    private EmailCreators() {}

    public static EmailEntity createEmailEntity(int flag) {
        return createEntityBuilder(flag).build();
    }

    public static EmailEntity createEmailEntity(int flag, MemberEntity memberEntity) {
        return createEntityBuilder(flag)
                .member(memberEntity)
                .build();
    }

    public static EmailDto createEmailDto(int flag) {
        return EmailDto.builder()
                .memberId(flag)
                .email("d_email_" + flag + "email.com")
                .emailType(EmailType.Other)
                .build();
    }

    public static EmailCreateDto createEmailCreateDto(int flag) {
        return EmailCreateDto.builder()
                .memberId(flag)
                .email("c_email_" + flag + "email.com")
                .emailType(EmailType.Other)
                .build();
    }

    public static List<EmailEntity> createEmailEntityList(int size) {
        List<EmailEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createEmailEntity(ii+1)) ;
        }
        return list;
    }

    public static List<EmailEntity> createEmailEntityList(int size, MemberEntity member) {
        var list = createEmailEntityList(size);
        list.forEach(e -> e.setMember(member));
        return list;
    }

    public static List<EmailEntity> createEmailEntityList(int size, int startFlag) {
        List<EmailEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createEmailEntity(ii + startFlag)) ;
        }
        return list;
    }

    public static List<EmailDto> createEmailDtoList(int size) {
        List<EmailDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createEmailDto(ii)) ;
        }
        return list;
    }

    private static EmailEntity.EmailEntityBuilder createEntityBuilder(int flag) {
        return EmailEntity.builder()
                .email("email@email_" + flag)
                .emailType(EmailType.Other);
    }
}
