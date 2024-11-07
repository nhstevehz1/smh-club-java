package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.AddressType;

import java.util.ArrayList;
import java.util.List;

public class AddressCreators {

    private AddressCreators() {}

    public static AddressEntity createAddressEntity(int flag) {
        return createAddressEntityBuilder(flag).build();
    }

    public static AddressEntity createAddressEntity(int flag, MemberEntity memberEntity) {
        return createAddressEntityBuilder(flag)
                .member(memberEntity)
                .build();
    }

    public static AddressDto createAddressDto(int flag) {
        return AddressDto.builder()
                .id(flag)
                .memberId(flag)
                .address1("d_address1_" + flag)
                .address2("d_address2_" + flag)
                .city("d_city_" + flag)
                .state("d_state_" + flag)
                .zip("d_zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }

    public static AddressCreateDto createAddressCreateDto(int flag) {
        return AddressCreateDto.builder()
                .memberId(flag)
                .address1("c_address1_" + flag)
                .address2("c_address2_" + flag)
                .city("c_city_" + flag)
                .state("c_state_" + flag)
                .zip("c_zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }

    public static List<AddressEntity> createAddressEntityList(int size) {
        List<AddressEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createAddressEntity(ii+1)) ;
        }
        return list;
    }

    public static List<AddressEntity> createAddressEntityList(int size, MemberEntity member) {
        var list = createAddressEntityList(size);
        list.forEach(e -> e.setMember(member));
        return list;
    }

    public static List<AddressEntity> createAddressEntityList(int size, int startFlag) {
        List<AddressEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(createAddressEntity(ii + startFlag)) ;
        }
        return list;
    }

    public static List<AddressDto> createAddressDtoList(int size) {
        List<AddressDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
         list.add(createAddressDto(ii)) ;
        }
        return list;
    }

    private static AddressEntity.AddressEntityBuilder createAddressEntityBuilder(int flag) {
        return AddressEntity.builder()
                .address1("e_address1_" + flag)
                .address2("e_address2_" + flag)
                .city("e_city_" + flag)
                .state("e_state_" + flag)
                .zip("e_zip_" + flag)
                .addressType(AddressType.Home);
    }

}
