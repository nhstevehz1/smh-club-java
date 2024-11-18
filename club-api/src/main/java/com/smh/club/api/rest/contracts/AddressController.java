package com.smh.club.api.rest.contracts;

import com.smh.club.api.rest.response.CountResponse;
import com.smh.club.api.rest.response.PageResponse;
import com.smh.club.api.data.dto.AddressDto;
import org.springframework.http.ResponseEntity;

public interface AddressController {
    ResponseEntity<PageResponse<AddressDto>> page(int pageNumber, int pageSize, String sortDir, String sort);
    ResponseEntity<AddressDto> get(int id);
    ResponseEntity<CountResponse> count();
    ResponseEntity<AddressDto> create(AddressDto address);
    ResponseEntity<AddressDto> update(int id, AddressDto address);
    ResponseEntity<Void> delete(int id);
}
