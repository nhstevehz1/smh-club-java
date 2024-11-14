package com.smh.club.api.integrationtests.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.PhoneEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.PhoneRepo;
import com.smh.club.api.dto.CreatePhoneDto;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.integrationtests.controllers.IntegrationTests;
import com.smh.club.api.request.PagingConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
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

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
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
public class PhoneIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    private PhoneRepo repo;

    @Autowired
    public PhoneIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/api/v1/phones");
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
    public void clearPhoneTable() {
        repo.deleteAll();
        memberRepo.flush();
    }

    @Test
    public void getListPage_no_params() throws Exception {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(PhoneDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(PhoneDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortDir_desc() throws Exception {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(PhoneEntity::getId).reversed()).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(PhoneDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(PhoneDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) throws Exception {
        addEntitiesToDb(15);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

        var actual = executeGetListPage(PhoneDto.class, path, valueMap, sorted.size(), pageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(PhoneDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(pageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 8, 10})
    public void getListPage_page(int page) throws Exception {
        for (int ii = 0; ii < 10; ii++) {
            addEntitiesToDb(15);
        }

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(PhoneDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(PhoneDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortColumn() throws Exception {
        addEntitiesToDb(15);

        // sort by id
        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(PhoneEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(PhoneDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(PhoneDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by phone number
        sorted = repo.findAll().stream()
                .sorted(Comparator.comparing(PhoneEntity::getPhoneNum)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "phone-number");

        actual = executeGetListPage(PhoneDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(PhoneDto::getPhoneNum)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        //  sort by phone-typ. The sorted list is not reliable since there are only 3 phone types.

    }

    @Test
    public void create_returns_dto_status_created() throws Exception {
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(CreatePhoneDto.class)
                .generate(field(CreatePhoneDto::getMemberId), g -> g.oneOf(memberIdList))
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
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), PhoneDto.class);
        var entity =  repo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void delete_status_noContent() throws Exception {
        var entities = addEntitiesToDb(1);
        var id = entities.get(0).getId();

        // perform DELETE
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        // verify
        var address = repo.findById(id);
        assertFalse(address.isPresent());
    }

    @Test
    public void update_returns_dto_status_ok() throws Exception {
        var phone = addEntitiesToDb(5).get(2);
        var memberId = phone.getMember().getId();
        var update = Instancio.of(CreatePhoneDto.class)
                .set(field(CreatePhoneDto::getMemberId), memberId)
                .create();

        // perform PUT
        mockMvc.perform(put(path + "/{id}", phone.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        // verify
        var entity = repo.findById(phone.getId());

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }
    
    private List<PhoneEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(PhoneEntity.class)
                .size(size) // must be before withSettings
                .withSettings(getSettings())
                .generate(field(PhoneEntity::getMember), g -> g.oneOf(members))
                .create();
        return repo.saveAllAndFlush(entities);
    }

    private void verify(CreatePhoneDto expected, PhoneEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getPhoneNum(), actual.getPhoneNum());
        assertEquals(expected.getPhoneType(), actual.getPhoneType());
    }

    private void verify(PhoneEntity expected, PhoneDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getPhoneNum(), actual.getPhoneNum());
        assertEquals(expected.getPhoneType(), actual.getPhoneType());
    }

    private void verify(List<PhoneEntity> expected, List<PhoneDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }
}
