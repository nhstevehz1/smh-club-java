package com.smh.club.api.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.*;
import com.smh.club.api.domain.repos.*;
import com.smh.club.api.dto.address.AddressDto;
import com.smh.club.api.dto.email.EmailDto;
import com.smh.club.api.dto.phone.PhoneDto;
import com.smh.club.api.dto.renewal.RenewalDto;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Transactional
@WithMockUser(authorities = {"ROLE_club-admin", "ROLE_club-user"})
@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberDetailIntegrationTests {

    // need to mock the decoder otherwise an initialization error is thrown
    @MockitoBean private JwtDecoder jwtDecoder;

    private final ObjectMapper mapper;

    @Autowired private MembersRepo membersRepo;
    @Autowired private AddressRepo addressRepo;
    @Autowired private EmailRepo emailRepo;
    @Autowired private PhoneRepo phoneRepo;
    @Autowired private RenewalsRepo renewalRepo;

    @WithSettings // Instancio settings
    Settings settings =
        Settings.create().set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.BEAN_VALIDATION_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0)
            .set(Keys.BEAN_VALIDATION_ENABLED, true);


    @Autowired
    public MemberDetailIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        configure(mockMvc, mapper);
        this.mapper = mapper;
    }

    private void configure(MockMvc mockMvc, ObjectMapper mapper) {
        // setup RestAssured to use the MockMvc Context
        RestAssuredMockMvc.mockMvc(mockMvc);

        // Configure RestAssured to use the injected Object mapper.
        RestAssuredMockMvc.config =
            RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> {
                    log.debug(String.valueOf(type));
                    log.debug(String.valueOf(s));
                    return mapper;
                }));
    }

    @BeforeEach
    public void init() {
        // there seems to be a bug where @WithSettings is not recognized in @BeforeAll
        var members = Instancio.ofList(MemberEntity.class)
            .size(5)
            .ignore(field(MemberEntity::getId))
            .withUnique(field(MemberEntity::getMemberNumber))
            .create();

        membersRepo.saveAllAndFlush(members);
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    public void getAddresses(int index) throws JsonProcessingException {
        // setup
        var members = membersRepo.findAll();

        var entities = Instancio.ofList(AddressEntity.class)
            .size(100)
            .ignore(field(AddressEntity::getId))
            .generate(field(AddressEntity::getMember), g -> g.oneOf(members))
            .create();
        addressRepo.saveAllAndFlush(entities);

        var full = membersRepo.findAll();
        var member = full.get(index);

        // execute
        var results = execute(member.getId(), "/addresses", AddressDto.class);

        // verify
        assertFalse(results.isEmpty());
        member.getAddresses().forEach(entity -> {
            var result = results.stream().filter(dto -> dto.getId() == entity.getId()).findFirst();
            assertTrue(result.isPresent());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    public void getEmails(int index) throws JsonProcessingException {
        // setup
        var members = membersRepo.findAll();

        var entities = Instancio.ofList(EmailEntity.class)
            .size(100)
            .ignore(field(EmailEntity::getId))
            .generate(field(EmailEntity::getMember), g -> g.oneOf(members))
            .create();
        emailRepo.saveAllAndFlush(entities);

        var full = membersRepo.findAll();
        var member = full.get(index);

        // execute
        var results = execute(member.getId(), "/emails", EmailDto.class);

        // verify
        assertFalse(results.isEmpty());
        member.getEmails().forEach(entity -> {
            var result = results.stream().filter(dto -> dto.getId() == entity.getId()).findFirst();
            assertTrue(result.isPresent());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    public void getPhones(int index) throws JsonProcessingException {
        // setup
        var members = membersRepo.findAll();

        var entities = Instancio.ofList(PhoneEntity.class)
            .size(100)
            .ignore(field(PhoneEntity::getId))
            .generate(field(PhoneEntity::getMember), g -> g.oneOf(members))
            .create();
        phoneRepo.saveAllAndFlush(entities);

        var full = membersRepo.findAll();
        var member = full.get(index);

        // execute
        var results = execute(member.getId(), "/phones", PhoneDto.class);

        // verify
        assertFalse(results.isEmpty());
        member.getPhones().forEach(entity -> {
            var result = results.stream().filter(dto -> dto.getId() == entity.getId()).findFirst();
            assertTrue(result.isPresent());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4})
    public void getRenewals(int index) throws JsonProcessingException {
        // setup
        var members = membersRepo.findAll();

        var entities = Instancio.ofList(RenewalEntity.class)
            .size(100)
            .ignore(field(RenewalEntity::getId))
            .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
            .create();
        renewalRepo.saveAllAndFlush(entities);

        var full = membersRepo.findAll();
        var member = full.get(index);

        // execute
        var results = execute(member.getId(), "/renewals", RenewalDto.class);

        // verify
        assertFalse(results.isEmpty());
        member.getRenewals().forEach(entity -> {
            var result = results.stream().filter(dto -> dto.getId() == entity.getId()).findFirst();
            assertTrue(result.isPresent());
        });
    }

    private <T> List<T> execute(int memberId, String path, Class<T> clazz) throws JsonProcessingException {
        var result =
            given()
                .auth().none()
                .accept(MediaType.APPLICATION_JSON)
            .when()
                .get("/api/v1/members/" + memberId + path)
            .then()
                .assertThat().status(HttpStatus.OK)
                .assertThat().contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract().body().asString();

        return convertToList(result, clazz);
    }

    private <T> List<T> convertToList(String json, Class<T> clazz) throws JsonProcessingException {
        var mapType = new TypeReference<List<T>>() {};
        return mapper.readValue(json, mapType);
    }

}
