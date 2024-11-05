package com.smh.club.api.controllers.testconfigs;

import com.smh.club.api.Services.AddressServiceImpl;
import com.smh.club.api.common.controllers.AddressController;
import com.smh.club.api.common.mappers.AddressMapper;
import com.smh.club.api.common.services.AddressService;
import com.smh.club.api.controllers.AddressControllerImpl;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.mappers.AddressMapperImpl;
import com.smh.club.api.mappers.config.MapperConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AddressTestConfig {

    @Bean
    public AddressController getAddressController(AddressService addressService) {
        return new AddressControllerImpl(addressService);
    }

    @Bean
    public AddressService getAddressService(AddressRepo addressRepo, MembersRepo memberRepo, AddressMapper mapper) {
        return new AddressServiceImpl(addressRepo, memberRepo, mapper);
    }

    @Bean
    public AddressMapper getAddressMapper(MapperConfig mapperConfig) {
        return new AddressMapperImpl(mapperConfig.createModelMapper());
    }


}
