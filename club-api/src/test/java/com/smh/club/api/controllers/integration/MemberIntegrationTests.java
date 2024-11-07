package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.controllers.config.PagingConfig;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.MemberCreateDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests extends IntegrationTestsBase {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc,mapper);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortDirName, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }


    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, String pageSize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());
        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sizeName, pageSize);

        var actual = executeGetListPage(MemberDto.class, "/members",
                valueMap, sorted.size(), Integer.parseInt(pageSize));

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(Integer.parseInt(pageSize)).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,4,5,8})
    public void getListPage_page(int page) throws Exception {
        var entitySize = 100;
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());
        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.pageName, String.valueOf(page));

        var actual = executeGetListPage(MemberDto.class, "/members",
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortColumn() throws Exception {
        var entitySize = 50;
        addEntitiesToDb(entitySize);

        // sort by id
        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getId)).toList();
        assertEquals(entitySize, sorted.size());
        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "id");

        var actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by member-number
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "member-number");

        actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by first-name
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getFirstName)).toList();
        assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "first-name");

        actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getFirstName)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        //sort by last-name
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getLastName)).toList();
        assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "last-name");

        actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getLastName)).toList(), actual);

        expected = sorted.stream().limit(10).toList();
        verify(expected, actual);

        //sort by birthdate
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getBirthDate)).toList();
        assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "birth-date");

        actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getBirthDate)).toList(), actual);

        expected = sorted.stream().limit(10).toList();
        verify(expected, actual);

        //sort by joined-date
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getJoinedDate)).toList();
        assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.sortName, "joined-date");

        actual = executeGetListPage(MemberDto.class, "/members", valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getJoinedDate)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

    }

    @Test
    public void createMember_returns_memberDto_status_created() throws Exception {
        // create member
        var create = MemberCreators.createMemberCreateDto(100);

        // perform POST
        var ret = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // verify
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), AddressDto.class);
        var entity =  memberRepo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void deleteMember_status_noContent() throws Exception {
        // create several members
        var entities = addEntitiesToDb(10);
        var id = entities.get(5).getId();

        // perform DELETE
        mockMvc.perform(delete("/members/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        var member = memberRepo.findById(id);
        assertFalse(member.isPresent());
    }

    @Test
    public void update_returns_memberDto_status_ok() throws Exception{
        // create several members
        var entities = addEntitiesToDb(10);
        var update = MemberCreators.createMemberCreateDto(100);
        var id = entities.get(5).getId();

        mockMvc.perform(put("/members/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        var member = memberRepo.findById(id);
        assertTrue(member.isPresent());
        verify(update, member.get());
    }

    private List<MemberEntity> addEntitiesToDb(int size) {
        return memberRepo.saveAllAndFlush(MemberCreators.createMemeberEntityList(size));
    }

    private void verify(MemberCreateDto expected, MemberEntity actual) {
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(MemberEntity expected, MemberDto actual) {
        assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getMiddleName(), actual.getMiddleName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getSuffix(), actual.getSuffix());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }


    private void verify(List<MemberEntity> expected, List<MemberDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }
}
