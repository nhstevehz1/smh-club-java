package com.smh.club.api.mappers;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.data.entities.AddressEntity;
import com.smh.club.api.models.Address;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressMapperImpl implements AddressMapper {
    @Override
    public AddressEntity toEntity(Address dataObject) {
        return AddressEntity.builder()
                .address1(dataObject.getAddress1())
                .address2(dataObject.getAddress2())
                .city(dataObject.getCity())
                .state(dataObject.getState())
                .zip(dataObject.getZip())
                .addressType(dataObject.getAddressType())
                .build();
    }

    @Override
    public Address toDataObject(AddressEntity entity) {
        return Address.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .Address1(entity.getAddress1())
                .Address2(entity.getAddress2())
                .city(entity.getCity())
                .state(entity.getState())
                .zip(entity.getZip())
                .addressType(entity.getAddressType())
                .build();
    }

    @Override
    public void update(Address dataObject, AddressEntity entity) {
        entity.setAddress1(dataObject.getAddress1());
        entity.setAddress2(dataObject.getAddress2());
        entity.setCity(dataObject.getCity());
        entity.setState(dataObject.getState());
        entity.setZip(dataObject.getZip());
        entity.setAddressType(dataObject.getAddressType());
    }

    @Override
    public List<Address> toDataObjectList(List<AddressEntity> entityList) {
        return entityList.stream().map(this::toDataObject).collect(Collectors.toList());
    }
}
