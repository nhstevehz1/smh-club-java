package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.EmailEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.EmailRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.EmailDto;
import com.smh.club.api.dto.EmailType;
import com.smh.club.api.dto.create.CreateEmailDto;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import com.smh.club.api.request.PagingConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
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

import static com.smh.club.api.helpers.datacreators.EmailCreators.createEmailEntityList;
import static com.smh.club.api.helpers.datacreators.EmailCreators.genCreateEmailDto;
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
public class EmailIntegrationTests extends IntegrationTests {

    @Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    private EmailRepo repo;

    private List<MemberEntity> members;

    @Autowired
    public EmailIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc, mapper, "/emails");
    }

    @BeforeAll
    public void initMembers() {
        var entities = MemberCreators.createMemeberEntityList(5);
        members = memberRepo.saveAllAndFlush(entities);
    }

    @Test
    public void getListPage_no_params() throws Exception {
        // populate address table
        addEntitiesToDb(members.get(4), 4);
        addEntitiesToDb(members.get(0), 0);
        addEntitiesToDb(members.get(3), 5);
        addEntitiesToDb(members.get(2), 2);
        addEntitiesToDb(members.get(1), 1);

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPage(EmailDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream().sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

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

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId).reversed()).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId).reversed()).toList(), actual);

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

        var sorted = repo.findAll().stream()
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, String.valueOf(pageSize));

        var actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), pageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

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
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

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
                .sorted(Comparator.comparingInt(EmailEntity::getId)).toList();

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(EmailDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
        
        // sort by email
        sorted = repo.findAll().stream()
                .sorted(Comparator.comparing(EmailEntity::getEmail)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "email");

        actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(EmailDto::getEmail)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by email-type
        sorted = repo.findAll().stream()
                .sorted(Comparator.comparing(EmailEntity::getEmailType)).toList();

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "email-type");

        actual = executeGetListPage(EmailDto.class, path, valueMap, sorted.size(), defaultPageSize);

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(EmailDto::getEmailType)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);
    }

    @Test
    public void create_returns_address_status_created() throws Exception {
        // create addresses
        var create = genCreateEmailDto(0);
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
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), EmailDto.class);
        var entity =  repo.findById(dto.getId());

        assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void delete_status_noContent() throws Exception {
        // create several addresses
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
        // create several addresses
        var entities = addEntitiesToDb(members.get(1), 0);
        var update = genCreateEmailDto(members.get(1).getId());
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
    
    private List<EmailEntity> addEntitiesToDb(MemberEntity member, int startFlag) {
        var entities = createEmailEntityList(3, startFlag);
        entities.forEach(e -> e.setMember(member));
        entities.get(0).setEmailType(EmailType.Home);
        entities.get(1).setEmailType(EmailType.Work);
        entities.get(2).setEmailType(EmailType.Other);
        return repo.saveAllAndFlush(entities);
    }

    private void verify(CreateEmailDto expected, EmailEntity actual) {
        assertEquals(expected.getMemberId(), actual.getMember().getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(EmailEntity expected, EmailDto actual) {
        assertEquals(expected.getMember().getId(), actual.getMemberId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmailType(), actual.getEmailType());
    }

    private void verify(List<EmailEntity> expected, List<EmailDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }

}
