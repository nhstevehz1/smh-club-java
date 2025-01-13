package com.smh.club.api.rest.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.rest.domain.entities.*;
import com.smh.club.api.rest.domain.repos.MembersRepo;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
@Configuration
public class DevSetup implements CommandLineRunner {
  private final MembersRepo repo;
  private final ObjectMapper mapper;

  @Transactional
  @Override
  public void run(String... args) throws Exception {

    var names = readNames();
    var addresses = readAddresses();
    var emails = readEmails();
    var phones = readPhones();

    var list = new ArrayList<MemberEntity>();
    var random = new Random();

    for (int ii = 0; ii < 200; ii++) {
      var firstName = names.get(random.nextInt(names.size())).first;
      var middleName = random.nextBoolean() ? (char)(random.nextInt(26) + 'A') + "." : null;
      var lastName = names.get(random.nextInt(names.size())).last;

      var member = MemberEntity.builder()
          .memberNumber(ii + 1)
          .firstName(firstName)
          .middleName(middleName)
          .lastName(lastName)
          .birthDate(LocalDate.now().minusYears(22).minusDays(ii+10))
          .joinedDate(LocalDate.now().minusYears(1).minusDays(ii+10))
          .build();

      for (var address : getRandomAddresses(random, addresses)) {
        member.addAddress(address);
      }

      for (var email: getRandomEmails(random, emails)) {
        member.addEmail(email);
      }

      for (var phone: getRandomPhones(random, phones)) {
        member.addPhone(phone);
      }

      list.add(member);
    }
    repo.saveAll(list);
  }

  private List<AddressEntity> getRandomAddresses(Random random, List<Address> addresses) {
    var types = AddressType.values();
    var size = random.nextInt(types.length);
    var bound = addresses.size();
    var results = new ArrayList<AddressEntity>();
    String[] other = {"Apt. ", "Unit "};

    for (int ii = 0; ii < size; ii++) {
      var street = addresses.get(random.nextInt(bound)).getAddress();
      var street2 = random.nextBoolean() ? other[random.nextInt(other.length)] + random.nextInt(100) : null;
      var city = addresses.get(random.nextInt(bound)).getCity();
      var state = addresses.get(random.nextInt(bound)).getState();
      var zip = addresses.get(random.nextInt(bound)).getZip();

      var address = AddressEntity.builder()
          .address1(street)
          .address2(street2)
          .city(city)
          .state(state)
          .zip(zip)
          .addressType(types[ii])
          .build();
      results.add(address);
    }
    return results;
  }

  private List<EmailEntity> getRandomEmails(Random random, List<Email> emails) {
    var size = 3;
    var results = new ArrayList<EmailEntity>();

    for (int ii = 0; ii < size; ii++) {
      var email = emails.get(random.nextInt(emails.size())).getEmail();
      var type = emails.get(random.nextInt(emails.size())).getEmailType();
      var entity = EmailEntity.builder()
          .email(email)
          .emailType(type)
          .build();

      results.add(entity);
    }

    return results;
  }

  private List<PhoneEntity> getRandomPhones(Random random, List<Phone> phones) {
    var size = 3;
    var results = new ArrayList<PhoneEntity>();

    for (int ii = 0; ii < size; ii++) {
      var code = phones.get(random.nextInt(phones.size())).getCountryCode();
      var phone = phones.get(random.nextInt(phones.size())).getPhoneNumber();
      var type = phones.get(random.nextInt(phones.size())).getPhoneType();

      var entity = PhoneEntity.builder()
          .countryCode(code)
          .phoneNumber(phone)
          .phoneType(type)
          .build();
      results.add(entity);
    }

    return results;
  }

  private List<Address> readAddresses() throws IOException {
      var addresses = mapper.readValue(ResourceUtils.getFile("classpath:data/addresses.json"), Address[].class);
      return Arrays.asList(addresses);
  }

  private List<Name> readNames() throws IOException {
    var names = mapper.readValue(ResourceUtils.getFile("classpath:data/names.json"), Name[].class);
    return Arrays.asList(names);
  }

  private List<Email> readEmails() throws IOException {
    var emails = mapper.readValue(ResourceUtils.getFile("classpath:data/emails.json"), Email[].class);
    return Arrays.asList(emails);
  }

  private List<Phone> readPhones() throws IOException {
    var phones = mapper.readValue(ResourceUtils.getFile("classpath:data/phones.json"), Phone[].class);
    return Arrays.asList(phones);
  }

  @Getter
  @AllArgsConstructor
  @Builder
  private static class Address{
    private String address;
    private String city;
    private String state;
    private String zip;
  }

  @Getter
  @AllArgsConstructor
  private static class Name{
    private String first;
    private String last;
  }

  @Getter
  @AllArgsConstructor
  private static class Phone {
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("phone_type")
    private PhoneType phoneType;
  }

  @Getter
  @AllArgsConstructor
  private static class Email {
    @JsonProperty("email")
    private String email;
    @JsonProperty("email_type")
    private EmailType emailType;
  }
}
