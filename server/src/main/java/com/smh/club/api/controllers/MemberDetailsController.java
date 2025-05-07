package com.smh.club.api.controllers;

import com.smh.club.api.contracts.services.AddressService;
import com.smh.club.api.contracts.services.EmailService;
import com.smh.club.api.contracts.services.PhoneService;
import com.smh.club.api.contracts.services.RenewalService;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines endpoints for retrieving member info detail.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberDetailsController {

    private final AddressService addressSvc;
    private final EmailService emailSvc;
    private final PhoneService phoneSvc;
    private final RenewalService renewalSvc;

    /**
     * Retrieves a list of a member's addresses.
     *
     * @param id The member object's id.
     * @return A list of {@link AddressDto}'s.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}/addresses")
    public ResponseEntity<List<AddressDto>> addresses(@PathVariable int id) {
        return ResponseEntity.ok(addressSvc.findAllByMemberId(id));
    }

    /**
     * Retrieves a list of a member's email addresses.
     *
     * @param id The member object's id.
     * @return A list of {@link EmailDto}'s.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}/emails")
    public ResponseEntity<List<EmailDto>> emails(@PathVariable int id) {
        return ResponseEntity.ok(emailSvc.findAllByMemberId(id));
    }

    /**
     * Retrieves a list of a member's phone numbers.
     *
     * @param id The member object's id.
     * @return A list of {@link PhoneDto}'s.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}/phones")
    public ResponseEntity<List<PhoneDto>> phones(@PathVariable int id) {
        return ResponseEntity.ok(phoneSvc.findAllByMemberId(id));
    }

    /**
     * Retrieves a list a of member's renewal information.
     *
     * @param id The member object's id.
     * @return A list of {@link RenewalDto}'s.
     */
    @PreAuthorize("hasAuthority('permission:read')")
    @GetMapping("{id}/renewals")
    public ResponseEntity<List<RenewalDto>> renewals(@PathVariable int id) {
        return ResponseEntity.ok(renewalSvc.findAllByMemberId(id));
    }
}
