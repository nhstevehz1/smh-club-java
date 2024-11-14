package com.smh.club.api.integrationtests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.create.CreateAddressDto;
import com.smh.club.api.dto.update.UpdateAddressDto;
import com.smh.club.api.request.PagingConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@ExtendWith(InstancioExtension.class)
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

    @Autowired
    public AddressIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/addresses");
    }

    @BeforeAll
    public void initMembers() {
        // there seems to be a bug where @WithSettings is not recognized in before all
        var members = Instancio.ofList(MemberEntity.class)
                .size(5)
                .withSettings(getSettings())
                .ignore(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
        memberRepo.saveAllAndFlush(members);
    }

    @AfterEach
    public void clearAddressTable() {
        addressRepo.deleteAll();
        addressRepo.flush();
    }

    @Test
    public void getListPage_no_params() throws Exception {
        // populate address table
        addEntitiesToDb(15);

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
       addEntitiesToDb(15);

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
        addEntitiesToDb(15);

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
        addEntitiesToDb(150);

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
        addEntitiesToDb(15);

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
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(CreateAddressDto.class)
                .generate(field(CreateAddressDto::getMemberId), g -> g.oneOf(memberIdList))
                .create();

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
        var entities = addEntitiesToDb(5);
        var id = entities.get(2).getId();

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
        var address = addEntitiesToDb(5).get(2);
        var memberId = address.getMember().getId();
        var update = Instancio.of(UpdateAddressDto.class)
                .set(field(UpdateAddressDto::getMemberId), memberId)
                .create();

        // perform PUT
        mockMvc.perform(put(path + "/{id}", address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        // verify
        var entity = addressRepo.findById(address.getId());

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    private List<AddressEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(AddressEntity.class)
                .size(size) // must be before withSettings
                .withSettings(getSettings())
                .generate(field(AddressEntity::getMember), g -> g.oneOf(members))
                .create();

        return addressRepo.saveAllAndFlush(entities);
    }

    private void verify(UpdateAddressDto expected, AddressEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getAddress1(), actual.getAddress1());
        assertEquals(expected.getAddress2(), actual.getAddress2());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getState(), actual.getState());
        assertEquals(expected.getZip(), actual.getZip());
        assertEquals(expected.getAddressType(), actual.getAddressType());
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
