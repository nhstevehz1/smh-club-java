package com.smh.club.api.rest.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.data.domain.entities.MemberEntity;
import com.smh.club.api.data.domain.repos.MembersRepo;
import com.smh.club.api.rest.dto.AddressDto;
import com.smh.club.api.rest.dto.MemberDto;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.instancio.Instancio;
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
import smh.club.shared.config.PagingConfig;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class MemberIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo repo;

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc,mapper, "/api/v1/members");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();

        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber).reversed()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }


    @ParameterizedTest
    @CsvSource({"1,5", "5,2", "20,5", "50,5"})
    public void getListPage_pageSize(int entitySize, String pageSize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, pageSize);

        var actual = executeGetListPage(MemberDto.class, path,
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

        var sorted = repo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(MemberDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        var skip = page * defaultPageSize;
        var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "member-number", "first-name", "middle-name", "last-name",
        "suffix", "birth-date", "joined-date" })
    public void getListPage_sortColumn(String sort) throws Exception {
        var entitySize = 50;
        addEntitiesToDb(entitySize);
        var sortFields = getSorts().get(sort);

        // sort by id
        var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
        assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, sort);

        var actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @Test
    public void createMember_returns_memberDto_status_created() throws Exception {
        // create member
        var create = Instancio.of(MemberDto.class)
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
        var entity =  repo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void deleteMember_status_noContent() throws Exception {
        // create several members
        var entities = addEntitiesToDb(10);
        var id = entities.get(5).getId();

        // perform DELETE
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        var member = repo.findById(id);
        assertFalse(member.isPresent());
    }

    @Test
    public void update_returns_memberDto_status_ok() throws Exception{
        // create several members
        var member = addEntitiesToDb(10).get(5);
        var update = Instancio.of(MemberDto.class)
                .set(field(MemberDto::getId), member.getId())
                .set(field(MemberDto::getMemberNumber), member.getMemberNumber())
                .create();

        mockMvc.perform(put( path + "/{id}", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        var entity = repo.findById(member.getId());
        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    private List<MemberEntity> addEntitiesToDb(int size) {
        var entities = Instancio.ofList(MemberEntity.class)
                .size(size) // must be before withSettings
                .withSettings(getSettings())
                .withUnique(field(MemberEntity::getMemberNumber))
                .ignore(field(MemberEntity::getId))
                .create();

        return repo.saveAllAndFlush(entities);
    }

    private void verify(MemberDto expected, MemberEntity actual) {
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

    private Map<String, SortFields<MemberEntity, MemberDto>> getSorts() {

        Map<String, SortFields<MemberEntity, MemberDto>> map = new HashMap<>();
        map.put("id", SortFields.of(Comparator.comparingInt(MemberEntity::getId),
            Comparator.comparingInt(MemberDto::getId)));

        map.put("member-number", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("first-name", SortFields.of(Comparator.comparing(MemberEntity::getFirstName),
            Comparator.comparing(MemberDto::getFirstName)));

        map.put("middle-name", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("last-name", SortFields.of(Comparator.comparing(MemberEntity::getLastName),
            Comparator.comparing(MemberDto::getLastName)));

        map.put("suffix", SortFields.of(Comparator.comparingInt(MemberEntity::getMemberNumber),
            Comparator.comparingInt(MemberDto::getMemberNumber)));

        map.put("birth-date", SortFields.of(Comparator.comparing(MemberEntity::getBirthDate),
            Comparator.comparing(MemberDto::getBirthDate)));

        map.put("joined-date", SortFields.of(Comparator.comparing(MemberEntity::getJoinedDate),
            Comparator.comparing(MemberDto::getJoinedDate)));

        return map;
    }
}
