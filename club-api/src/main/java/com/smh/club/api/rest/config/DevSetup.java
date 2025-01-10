package com.smh.club.api.rest.config;

import com.smh.club.api.rest.domain.entities.AddressEntity;
import com.smh.club.api.rest.domain.entities.AddressType;
import com.smh.club.api.rest.domain.entities.MemberEntity;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    var multi = 100;
    var random = new Random();
    for (int ii = 0; ii < 200; ii++) {
      var member = MemberEntity.builder()
          .memberNumber(ii + 1)
          .firstName("First_" + Math.round(Math.random() * multi))
          .middleName(random.nextBoolean() ? "Middle_" + Math.round(Math.random() * multi): "")
          .lastName("Last_" + Math.round(Math.random() * multi))
          .suffix(random.nextBoolean() ? "Suffix_" + Math.round(Math.random() * multi): "")
          .birthDate(LocalDate.now().minusYears(22).minusDays(ii+10))
          .joinedDate(LocalDate.now().minusYears(1).minusDays(ii+10))
          .build();

      var zipCityState = getZipCityState(random);

      var address = AddressEntity.builder()
          .address1((Math.round(Math.random() * multi) + " Street"))
          .address2(random.nextBoolean() ? "Apt " + (Math.round(Math.random() * multi)) : "")
          .city(zipCityState.getCity())
          .state(zipCityState.getState())
          .zip(zipCityState.getZip())
          .addressType(AddressType.Home)
          .build();
      member.addAddress(address);

      list.add(member);
    }
    repo.saveAll(list);
  }

  private ZipCityState getZipCityState(Random random) {
    var randomInt = random.nextInt(8);
    return switch (randomInt) {
      case 0 -> new ZipCityState("03060", "Nashua", "NH");
      case 1 -> new ZipCityState("O3061", "Nashua", "NH");
      case 2 -> new ZipCityState("32803", "Orlando", "FL");
      case 3 -> new ZipCityState("03031", "Amherst", "NH");
      case 4 -> new ZipCityState("03054", "Merrimack", "NH");
      case 5 -> new ZipCityState("01879", "Lowell", "MA");
      case 6 -> new ZipCityState("33601", "Tampa", "FL");
      default -> new ZipCityState("01879", "Tyngsboro", "MA");
    };
  }

  @Getter
  @AllArgsConstructor
  private static class ZipCityState {
    private String zip;
    private String city;
    private String state;
  }
}
