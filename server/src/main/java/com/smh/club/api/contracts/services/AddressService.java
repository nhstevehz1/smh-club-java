package com.smh.club.api.contracts.services;

import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.address.AddressMemberDto;
import com.smh.club.api.response.PagedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for addresses.
 */
public interface AddressService {

    /**
     * Retrieves a page of addresses from the database.
     *
     * @param pageable A {@link Pageable} that describes the sort.
     * @return A {@link PagedDto} of type {@link AddressMemberDto}.
     */
    PagedDto<AddressMemberDto> getPage(Pageable pageable);

    /**
     * Finds all addresses belonging to member.
     *
     * @param memberId The {@link MemberEntity} id.
     * @return A list of {@link AddressDto}'s.
     */
    List<AddressDto> findAllByMemberId(int memberId);

    /**
     * Retrieves an address from the database.
     * @param id The id of the address
     * @return An {@link AddressDto} type {@link Optional}
     */
    Optional<AddressDto> getAddress(int id);

    /**
     * Creates an address and stores it in the database.
     *
     * @param address The {@link AddressDto} used to create the address.
     * @return The newly created address.
     */
    AddressDto createAddress(AddressDto address);

    /**
     * Updates an address in the database.
     *
     * @param id The id of the address to update.
     * @param address The {@link AddressDto} containing the updates.
     * @return The updated {@link AddressDto}.
     */
    Optional<AddressDto> updateAddress(int id, AddressDto address);

    /**
     * Deletes an address from the database.
     * @param id The id of the address to delete.
     */
    void deleteAddress(int id);

    /**
     * Gets a count of the address objects in the database.
     * @return The count of address objects.
     */
    long getAddressCount();
}
