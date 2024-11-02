package com.smh.club.api.Services;

import com.smh.club.api.common.mappers.EmailMapper;
import com.smh.club.api.common.services.EmailService;
import com.smh.club.api.data.repos.EmailRepo;
import com.smh.club.api.data.repos.MembersRepo;
import com.smh.club.api.models.Email;
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
public class EmailServiceImpl implements EmailService {
    
    private final EmailRepo emailRepo;
    private final MembersRepo memberRepo;
    
    private final EmailMapper emailMapper;
    
    private final Map<String, String> sortColumnMap = initSortColumnMap();

    @Override
    public PageResponse<Email> getItemListPage(@NonNull PageParams pageParams) {
        log.debug("Getting emil item list page: {}", pageParams);

        var pageRequest = PageRequest.of(
                pageParams.getPageNumber(),
                pageParams.getPageSize(),
                pageParams.getSortDirection(),
                sortColumnMap.getOrDefault(pageParams.getSortColumn(),
                        sortColumnMap.get("default")));
        log.debug("Created pageable: {}", pageRequest);

        var page = emailRepo.findAll(pageRequest);

        return PageResponse.<Email>builder()
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .items(emailMapper.toDataObjectList(page.getContent()))
                .build();
    }

    @Override
    public Optional<Email> getItem(int id) {
        log.debug("Getting email by id: {}", id);

        return emailRepo.findById(id).map(emailMapper::toDataObject);
    }

    @Override
    public Email createItem(Email email) {
        log.debug("creating email: {}", email);

        var memberRef = memberRepo.getReferenceById(email.getMemberId());
        var addressEntity = emailMapper.toEntity(email);
        addressEntity.setMember(memberRef);
        return emailMapper.toDataObject(emailRepo.save(addressEntity));
    }

    @Override
    public Optional<Email> updateItem(int id, Email email) {
        log.debug("Updating email, id: {}, with data: {}", id, email);

        if(id != email.getId()) {
            throw new IllegalArgumentException();
        }

        return emailRepo.findByIdAndMemberId(id, email.getMemberId())
                .map(e -> emailMapper.updateEntity(email, e))
                .map(emailMapper::toDataObject);
    }

    @Override
    public void deleteItem(int id) {
        log.debug("Deleting address, id: {}", id);
        emailRepo.deleteById(id);
    }

    @Override
    public CountResponse getItemCount() {
        log.debug("Getting member count");
        return CountResponse.of(emailRepo.count());
    }

    private Map<String,String> initSortColumnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("default", "id");
        map.put("email", "email");
        map.put("email-type", "emailType");
        
        return map;
    }
}
