package com.smh.club.api.rest.config;

import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Profile("dev")
@Configuration
public class DevSetup implements CommandLineRunner {
  private final MembersRepo repo;


  @Transactional
  @Override
  public void run(String... args) throws Exception {
    var list = new ArrayList<MemberEntity>();

    for (int ii = 0; ii < 200; ii++) {
      list.add(
          MemberEntity.builder()
          .memberNumber(ii + 1)
          .firstName("First" + Math.round(Math.random() * 1000))
          .middleName("Middle" + Math.round(Math.random() * 1000))
          .lastName("Last" + Math.round(Math.random() * 1000))
          .suffix("Suffix" + Math.round(Math.random() * 1000))
          .birthDate(LocalDate.now().minusYears(22).minusDays(ii+10))
          .joinedDate(LocalDate.now().minusYears(1).minusDays(ii+10))
          .build()
      );
    }
    repo.saveAll(list);
  }
}
