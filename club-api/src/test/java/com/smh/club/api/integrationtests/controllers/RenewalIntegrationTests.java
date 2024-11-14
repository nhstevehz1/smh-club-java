package com.smh.club.api.integrationtests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.entities.RenewalEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.domain.repos.RenewalsRepo;
import com.smh.club.api.dto.CreateRenewalDto;
import com.smh.club.api.dto.PhoneDto;
import com.smh.club.api.dto.RenewalDto;
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
public class RenewalIntegrationTests extends IntegrationTests{
    
    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;
    
    @Autowired
    private RenewalsRepo renewRepo;

    @Autowired
    public RenewalIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/renewals");
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
    public void clearRenewalsTable() {
        renewRepo.deleteAll();
        renewRepo.flush();
    }

    @Test
    public void getListPage_no_params() throws Exception {
        addEntitiesToDb(15);

        var sorted = renewRepo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(RenewalDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @Test
    public void getListPage_sortDir_desc() throws Exception {
        addEntitiesToDb(40);


        var sorted = renewRepo.findAll().stream()
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
        addEntitiesToDb(15);

        var sorted = renewRepo.findAll().stream()
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
        addEntitiesToDb(150);

        var sorted = renewRepo.findAll().stream()
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
        addEntitiesToDb(15);

        // sort by id
        var sorted = renewRepo.findAll().stream()
                .sorted(Comparator.comparingInt(RenewalEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(RenewalDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by renewal-date
        sorted = renewRepo.findAll().stream()
                .sorted(Comparator.comparing(RenewalEntity::getRenewalDate)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "renewal-date");

        actual = executeGetListPage(RenewalDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(RenewalDto::getRenewalDate)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by renewal-year
        sorted = renewRepo.findAll().stream()
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
        var memberIdList = memberRepo.findAll().stream().map(MemberEntity::getId).toList();
        var create = Instancio.of(CreateRenewalDto.class)
                .generate(field(CreateRenewalDto::getMemberId), g -> g.oneOf(memberIdList))
                .generate(field(CreateRenewalDto::getRenewalYear),
                        g-> g.text().pattern("#d#d#d#d"))
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
        var entity =  renewRepo.findById(dto.getId());

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
        var address = renewRepo.findById(id);
        assertFalse(address.isPresent());
    }

    @Test
    public void update_returns_dto_status_ok() throws Exception {
        var renewal = addEntitiesToDb(5).get(2);
        var memberId = renewal.getMember().getId();
        var update = Instancio.of(CreateRenewalDto.class)
                .set(field(CreateRenewalDto::getMemberId), memberId)
                .generate(field(CreateRenewalDto::getRenewalYear),
                        g-> g.text().pattern("#d#d#d#d"))
                .create();

        // perform PUT
        mockMvc.perform(put(path + "/{id}", renewal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        // verify
        var entity = renewRepo.findById(renewal.getId());

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    private List<RenewalEntity> addEntitiesToDb(int size) {
        var members = memberRepo.findAll();

        var entities = Instancio.ofList(RenewalEntity.class)
                .size(size) // must be before withSettings
                .withSettings(getSettings())
                .generate(field(RenewalEntity::getMember), g -> g.oneOf(members))
                .create();

        return renewRepo.saveAllAndFlush(entities);
    }

    private void verify(CreateRenewalDto expected, RenewalEntity actual) {
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
