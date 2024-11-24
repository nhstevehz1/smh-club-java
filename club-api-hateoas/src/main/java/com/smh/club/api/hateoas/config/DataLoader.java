package com.smh.club.api.hateoas.config;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.data.domain.entities.AddressType;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Profile("dev")
@Service
public class DataLoader implements ApplicationRunner {

    @Autowired
    private MembersRepo repo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var now = LocalDate.now();
        for (int ii = 0; ii < 30; ii++) {
            var member = MemberEntity.builder()
                .memberNumber(ii+1)
                .firstName("First"+ii)
                .middleName("Middle")
                .lastName("Last"+ii)
                .birthDate(now.minusYears(21))
                .joinedDate(now.minusYears(ii))
                .build();

            for(int xx = 0; xx < 2; xx++ ) {
                var address = AddressEntity.builder()
                    .address1("Add1"+ii)
                    .city("City"+ii)
                    .state("State"+ii)
                    .zip(String.valueOf(10000+ii))
                    .addressType(AddressType.Home)
                    .build();

                member.addAddress(address);
            }

            repo.save(member);
        }

    }
}
