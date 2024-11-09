package com.smh.club.api.integrationtests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.AddressType;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.helpers.datacreators.AddressCreators;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import com.smh.club.api.request.PagingConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Comparator;
import java.util.List;

import static com.smh.club.api.helpers.datacreators.AddressCreators.genAddressEntityList;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    private AddressRepo addressRepo;

    private List<MemberEntity> members;

    @Autowired
    public AddressIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/addresses");
    }

    @BeforeAll
    public void initMembers() {
        var entities = MemberCreators.createMemeberEntityList(5);
        members = memberRepo.saveAllAndFlush(entities);
    }

    @AfterEach
    public void clearAddressTable() {
        addressRepo.deleteAll();
        addressRepo.flush();
    }

    @Test
    public void getListPage_no_params() throws Exception {
        // populate address table
        addEntitiesToDb(members.get(4), 4);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 5);
        addEntitiesToDb(members.get(2), 2);
        addEntitiesToDb(members.get(1), 1);

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(AddressDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(AddressDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortDir_desc() throws Exception {
        // populate address table
        addEntitiesToDb(members.get(4), 4);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 5);
        addEntitiesToDb(members.get(2), 2);
        addEntitiesToDb(members.get(1), 1);

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId).reversed()).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) throws Exception {
        addEntitiesToDb(members.get(4), 4);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 5);
        addEntitiesToDb(members.get(2), 2);
        addEntitiesToDb(members.get(1), 1);

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

        var actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), pageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8, 10})
    public void getListPage_page(int page) throws Exception {
        for (int ii = 0; ii < 10; ii++) {
            addEntitiesToDb(members.get(4), ii + 40);
            addEntitiesToDb(members.get(0), ii + 60);
            addEntitiesToDb(members.get(3), ii + 50);
            addEntitiesToDb(members.get(2), ii + 20);
            addEntitiesToDb(members.get(1), ii + 11);
        }


        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortColumn() throws Exception {
        addEntitiesToDb(members.get(4), 4);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 5);
        addEntitiesToDb(members.get(2), 2);
        addEntitiesToDb(members.get(1), 1);

        // sort by id
        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(AddressDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by address1
        sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getAddress1)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "address1");

        actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getAddress1)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by address2
        sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getAddress1)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "address2");

        actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getAddress2)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by city
        sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getCity)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "city");

        actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getCity)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by state
        sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getState)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "state");

        actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getState)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by zip
        sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getZip)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "zip");

        actual = executeGetListPage(AddressDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getZip)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @Test
    public void createAddresses_returns_addressDto_status_created() throws Exception {
        // create addresses
        var create = AddressCreators.genCreateAddressDto(0);
        create.setMemberId(members.get(0).getId());

        // perform POST
        var ret = mockMvc.perform(post(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // verify
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), AddressDto.class);
        var entity =  addressRepo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void deleteAddress_status_noContent() throws Exception {
        // create several addresses
        var entities = addEntitiesToDb(members.get(1), 0);
        var id = entities.get(0).getId();

        // perform DELETE
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        // verify
        var address = addressRepo.findById(id);
        assertFalse(address.isPresent());
    }

    @Test
    public void update_returns_addressDto_status_ok() throws Exception {
        // create several addresses
        var entities = addEntitiesToDb(members.get(1), 0);
        var update = AddressCreators.genCreateAddressDto(members.get(1).getId());
        var id = entities.get(1).getId();

        // perform PUT
        mockMvc.perform(put("/addresses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        // verify
        var entity = addressRepo.findById(id);

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    private List<AddressEntity> addEntitiesToDb(MemberEntity member, int startFlag) {
        var entities = genAddressEntityList(3, startFlag);
        entities.forEach(e -> e.setMember(member));
        entities.get(0).setAddressType(AddressType.Home);
        entities.get(1).setAddressType(AddressType.Work);
        entities.get(2).setAddressType(AddressType.Other);
        return addressRepo.saveAllAndFlush(entities);
    }

    private void verify(CreateAddressDto expected, AddressEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getZip(), actual.getZip());
        assertEquals(expected.getAddressType(), actual.getAddressType());
    }

    private void verify(AddressEntity expected, AddressDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getZip(), actual.getZip());
        assertEquals(expected.getAddressType(), actual.getAddressType());
    }

    private void verify(List<AddressEntity> expected, List<AddressDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }
}
