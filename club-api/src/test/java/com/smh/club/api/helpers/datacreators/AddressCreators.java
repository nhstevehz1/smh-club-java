package com.smh.club.api.helpers.datacreators;

import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.AddressType;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.dto.update.UpdateAddressDto;

import java.util.ArrayList;
import java.util.List;

public class AddressCreators {

    private AddressCreators() {}

    public static AddressEntity genAddressEntity(int flag) {
        return genAddressEntityBuilder(flag).build();
    }

    public static AddressEntity genAddressEntity(int flag, MemberEntity memberEntity) {
        return genAddressEntityBuilder(flag)
                .member(memberEntity)
                .build();
    }

    public static AddressDto genAddressDto(int flag) {
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

    public static CreateAddressDto genCreateAddressDto(int flag) {
        return CreateAddressDto.builder()
                .memberId(flag)
                .address1("c_address1_" + flag)
                .address2("c_address2_" + flag)
                .city("c_city_" + flag)
                .state("c_state_" + flag)
                .zip("c_zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }

    public static UpdateAddressDto genUpdateAddressDto(int flag) {
        return UpdateAddressDto.builder()
                .memberId(flag)
                .address1("u_address1_" + flag)
                .address2("u_address2_" + flag)
                .city("u_city_" + flag)
                .state("u_state_" + flag)
                .zip("u_zip_" + flag)
                .addressType(AddressType.Home)
                .build();
    }

    public static List<AddressEntity> genAddressEntityList(int size) {
        List<AddressEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genAddressEntity(ii+1)) ;
        }
        return list;
    }

    public static List<AddressEntity> genAddressEntityList(int size, MemberEntity member) {
        var list = genAddressEntityList(size);
        list.forEach(e -> e.setMember(member));
        return list;
    }

    public static List<AddressEntity> genAddressEntityList(int size, int startFlag) {
        List<AddressEntity> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
            list.add(genAddressEntity(ii + startFlag)) ;
        }
        return list;
    }

    public static List<AddressDto> genAddressDtoList(int size) {
        List<AddressDto> list = new ArrayList<>(size);
        for (int ii = 0; ii < size; ii++) {
         list.add(genAddressDto(ii)) ;
        }
        return list;
    }

    private static AddressEntity.AddressEntityBuilder genAddressEntityBuilder(int flag) {
        return AddressEntity.builder()
                .address1("e_address1_" + flag)
                .address2("e_address2_" + flag)
                .city("e_city_" + flag)
                .state("e_state_" + flag)
                .zip("e_zip_" + flag)
                .addressType(AddressType.Home);
    }

}
