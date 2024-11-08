package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.RenewalDto;
import com.smh.club.api.dto.create.CreateRenewalDto;
import com.smh.club.api.dto.update.UpdateRenewalDto;
import com.smh.club.api.helpers.datacreators.RenewalCreators;
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

import static com.smh.club.api.helpers.datacreators.MemberCreators.createMemeberEntityList;
import static com.smh.club.api.helpers.datacreators.RenewalCreators.genCreateRenewalDto;
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
public class RenewalIntegrationTests extends IntegrationTests{
    
    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;
    
    @Autowired
    private RenewalsRepo repo;

    private List<MemberEntity> members;
    
    @Autowired
    public RenewalIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/renewals");
    }

    @BeforeAll
    public void initMembers() {
        var entities = createMemeberEntityList(5);
        members = memberRepo.saveAllAndFlush(entities);
    }

    @AfterEach
    public void clearRenewalsTable() {
        repo.deleteAll();
        memberRepo.flush();
    }

    @Test
    public void getListPage_no_params() throws Exception {
        addEntitiesToDb(members.get(4), 40);
        addEntitiesToDb(members.get(0), 11);
        addEntitiesToDb(members.get(3), 50);
        addEntitiesToDb(members.get(2), 20);
        addEntitiesToDb(members.get(1), 11);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(RenewalDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }
    
    
    
    private List<RenewalEntity> addEntitiesToDb(MemberEntity member, int startFlag) {
        var entities = RenewalCreators.genRenewalEntityList(3, startFlag);
        entities.forEach(e -> e.setMember(member));
        return repo.saveAllAndFlush(entities);
    }

    @Test
    public void getListPage_sortDir_desc() throws Exception {
        addEntitiesToDb(members.get(4), 40);
        addEntitiesToDb(members.get(0), 11);
        addEntitiesToDb(members.get(3), 50);
        addEntitiesToDb(members.get(2), 20);
        addEntitiesToDb(members.get(1), 11);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId).reversed()).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5,8,10})
    public void getListPage_pageSize(int pageSize) throws Exception {
        addEntitiesToDb(members.get(4), 40);
        addEntitiesToDb(members.get(0), 11);
        addEntitiesToDb(members.get(3), 50);
        addEntitiesToDb(members.get(2), 60);
        addEntitiesToDb(members.get(1), 10);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

        var actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), pageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

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

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var skip = defaultPageSize * page;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortColumn() throws Exception {
        addEntitiesToDb(members.get(4), 40);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 30);
        addEntitiesToDb(members.get(2), 20);
        addEntitiesToDb(members.get(1), 10);

        // sort by id
        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by renewal-date
        sorted = repo.findAll().stream()
                .sorted(Comparator.comparing(RenewalEntity::getRenewalDate)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "renewal-date");

        actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(RenewalDto::getRenewalDate)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by renewal-year
        sorted = repo.findAll().stream()
                .sorted(Comparator.comparing(RenewalEntity::getRenewalYear)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "renewal-year");

        actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(RenewalDto::getRenewalYear)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @Test
    public void create_returns_dto_status_created() throws Exception {
        var create = genCreateRenewalDto(4);
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
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), PhoneDto.class);
        var entity =  repo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void delete_status_noContent() throws Exception {
        var entities = addEntitiesToDb(members.get(1), 0);
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
        var entities = addEntitiesToDb(members.get(1), 0);
        var update = genCreateRenewalDto(members.get(1).getId());
        var id = entities.get(1).getId();

        // perform PUT
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        // verify
        var entity = repo.findById(id);

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }
    
    private void verify(CreateRenewalDto expected, RenewalEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(UpdateRenewalDto expected, RenewalEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(RenewalEntity expected, RenewalDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getRenewalDate(), actual.getRenewalDate());
        assertEquals(expected.getRenewalYear(), actual.getRenewalYear());
    }

    private void verify(List<RenewalEntity> expected, List<RenewalDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }
}
