package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.CountResponse;
import com.smh.club.api.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final MembersRepo memberRepo;

    private final AddressMapper addressMapper;

    private final Map<String, String> sortColumnMap = initSortColumnMap();

    @Override
    public PageResponse<AddressDto> getAddressListPage(@NonNull PageParams pageParams) {
        log.debug("Getting address item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = addressRepo.findAll(pageRequest);

        return PageResponse.<AddressDto>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(addressMapper.toAddressDtoList(page.getContent()))
                .build();
    }

    @Override
    public Optional<AddressDto> getAddress(int id) {
        log.debug("Getting address by id: {}", id);

        return addressRepo.findById(id).map(addressMapper::toAddressDto);
    }

    @Override
    public AddressDto createAddress(AddressCreateDto address) {
        log.debug("creating address: {}", address);

        var memberRef = memberRepo.getReferenceById(address.getMemberId());

        var addressEntity = addressMapper.toAddressEntity(address);
        addressEntity.setMember(memberRef);
        return addressMapper.toAddressDto(addressRepo.save(addressEntity));
    }

    @Override
    public Optional<AddressDto> updateAddress(int id, AddressCreateDto addressDto) {
        log.debug("Updating address id: {}, with data: {}", id, addressDto);

        return addressRepo.findByIdAndMemberId(id, addressDto.getMemberId())
                .map(e -> addressMapper.updateAddressEntity(addressDto, e))
                .map(addressMapper::toAddressDto);

    }

    @Override
    public void deleteAddress(int id) {
        log.debug("Deleting address, id: {}", id);
        addressRepo.deleteById(id);
    }

    @Override
    public CountResponse getAddressCount() {
        log.debug("Getting member count");
        return CountResponse.of(addressRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "id");
        map.put("address1", "address1");
        map.put("address2", "address2");
        map.put("city", "city");
        map.put("state", "state");
        map.put("zip", "zip");
        return map;
    }
}
