package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.data.repos.AddressRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.Address;
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
    public PageResponse<Address> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting address item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = addressRepo.findAll(pageRequest);

        return PageResponse.<Address>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(addressMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Optional<Address> getItem(int id) {
        log.debug("Getting address by id: {}", id);

        return addressRepo.findById(id).map(addressMapper::toDataObject);
    }

    @Override
    public Address createItem(Address address) {
        log.debug("creating address: {}", address);

        var memberRef = memberRepo.getReferenceById(address.getMemberId());

        var addressEntity = addressMapper.toEntity(address);
        addressEntity.setMember(memberRef);
        return addressMapper.toDataObject(addressRepo.save(addressEntity));
    }

    @Override
    public Optional<Address> updateItem(int id, Address address) {
        log.debug("Updating address id: {}, with data: {}", id, address);

        if(id != address.getId()) {
            throw new IllegalArgumentException();
        }
        return addressRepo.findByIdAndMemberId(id, address.getMemberId())
                .map(e -> addressMapper.updateEntity(address, e))
                .map(addressMapper::toDataObject);

    }

    @Override
    public void deleteItem(int id) {
        log.debug("Deleting address, id: {}", id);
        addressRepo.deleteById(id);
    }

    @Override
    public CountResponse getItemCount() {
        log.debug("Getting member count");
        return CountResponse.of(addressRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "id");
        map.put("address1", "address1");
        map.put("address2", "address2");
        map.put("city", "city");
        map.put("state", "date");
        map.put("zip", "zip");

        return map;
    }
}
