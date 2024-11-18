package com.smh.club.api.data.services;

import com.smh.club.api.data.contracts.mappers.AddressMapper;
import com.smh.club.api.data.contracts.services.AddressService;
import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.repos.AddressRepo;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.data.dto.AddressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class AddressServiceImpl extends AbstractServiceBase implements AddressService {

    private final AddressRepo addressRepo;
    private final MembersRepo memberRepo;
    private final AddressMapper addressMapper;

    @Override
    public Page<AddressDto> getAddressListPage(int pageNumber, int pageSize,
                                               @NonNull String direction, @NonNull String sort) {

        var pageRequest = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.fromString(direction),
                getSortColumn(sort));

        log.debug("Created pageable: {}", pageRequest);

        return addressRepo.findAll(pageRequest).map(addressMapper::toDto);
    }

    @Override
    public Optional<AddressDto> getAddress(int id) {
        log.debug("Getting address by id: {}", id);

        return addressRepo.findById(id).map(addressMapper::toDto);
    }

    @Override
    public AddressDto createAddress(AddressDto address) {
        log.debug("creating address: {}", address);

        var memberRef = memberRepo.getReferenceById(address.getMemberId());

        var addressEntity = addressMapper.toEntity(address);
        addressEntity.setMember(memberRef);
        return addressMapper.toDto(addressRepo.save(addressEntity));
    }

    @Override
    public Optional<AddressDto> updateAddress(int id, AddressDto addressDto) {
        log.debug("Updating address id: {}, with data: {}", id, addressDto);

        return addressRepo.findByIdAndMemberId(id, addressDto.getMemberId())
                .map(e -> addressMapper.updateEntity(addressDto, e))
                .map(addressMapper::toDto);

    }

    @Override
    public void deleteAddress(int id) {
        log.debug("Deleting address, id: {}", id);
        addressRepo.deleteById(id);
    }

    @Override
    public long getAddressCount() {
        log.debug("Getting member count");
        return addressRepo.count();
    }

    protected String getSortColumn(String key) {
        var source = AddressDto.class;
        var target = AddressEntity.class;

        return getSort(key, source, target)
                .orElse(getDefaultSort(source, target)
                        .orElse("id"));
    }
}
