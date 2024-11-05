package com.smh.club.api.controllers.integration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.domain.entities.AddressEntity;
import com.smh.club.api.domain.entities.MemberEntity;
import com.smh.club.api.domain.repos.AddressRepo;
import com.smh.club.api.domain.repos.MembersRepo;
import com.smh.club.api.dto.AddressCreateDto;
import com.smh.club.api.dto.AddressDto;
import com.smh.club.api.dto.AddressType;
import com.smh.club.api.helpers.datacreators.AddressCreators;
import com.smh.club.api.helpers.datacreators.MemberCreators;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.PageResponse;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class AddressIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    private AddressRepo addressRepo;

    private static List<MemberEntity> members = new ArrayList<>();

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
    public void getAddressListPage_no_default_sort() throws Exception {
        // populate address table
        addAddressesToDb(members.get(4), 4);
        addAddressesToDb(members.get(0), 0);
        addAddressesToDb(members.get(3), 5);
        addAddressesToDb(members.get(2), 2);
        addAddressesToDb(members.get(1), 1);

        var params = PageParams.builder()
                .pageNumber(0)
                .pageSize(10).build();

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId)).toList();

        var pageSize = params.getPageSize();
        var count = addressRepo.count();
        var pages = count / pageSize +  (count % pageSize == 0 ? 0 : 1);
        var length = count < pageSize ? count : pageSize;

        // perform get
        var ret = mockMvc.perform(get("/addresses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        var actual = readPageResponse(ret.getResponse().getContentAsString(), AddressDto.class).getItems();
        assertEquals(actual.stream().sorted(Comparator.comparingInt(AddressDto::getId)).toList(), actual);

        var skip = params.getPageSize() * params.getPageNumber();
        var expected = sorted.stream().skip(skip).limit(length).toList();

        verify(expected, actual);
    }

    @Test
    public void getAddressListPage_sort_default_desc() throws Exception {
        // populate address table
        addAddressesToDb(members.get(4), 4);
        addAddressesToDb(members.get(0), 0);
        addAddressesToDb(members.get(3), 5);
        addAddressesToDb(members.get(2), 2);
        addAddressesToDb(members.get(1), 1);

        var params = PageParams.builder()
                .pageSize(10)
                .sortDirection(Sort.Direction.DESC).build();

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparingInt(AddressEntity::getId).reversed()).toList();

        var pageSize = params.getPageSize();
        var count = sorted.size();
        var pages = count / pageSize +  (count % pageSize == 0 ? 0 : 1);
        var length = Math.min(count, pageSize);

        // perform get
        var ret = mockMvc.perform(get("/addresses")
                        .param(PageParams.DIRECTION_PARAM, String.valueOf(params.getSortDirection()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        var actual = readPageResponse(ret.getResponse().getContentAsString(), AddressDto.class).getItems();
        assertEquals(actual.stream().sorted(Comparator.comparingInt(AddressDto::getId).reversed()).toList(), actual);

        var skip = params.getPageSize() * params.getPageNumber();
        var expected = sorted.stream().skip(skip).limit(length).toList();

        verify(expected, actual);
    }

    @Test
    public void getAddressListPage_sort_state_asc() throws Exception {
        // populate address table
        addAddressesToDb(members.get(4), 4);
        addAddressesToDb(members.get(0), 0);
        addAddressesToDb(members.get(3), 5);
        addAddressesToDb(members.get(2), 2);
        addAddressesToDb(members.get(1), 1);

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getState)).toList();

        var pageSize = 10;
        var count = addressRepo.count();
        var pages = count / pageSize +  (count % pageSize == 0 ? 0 : 1);
        var length = count < pageSize ? count : pageSize;
        PageParams params = PageParams.builder()
                .sortColumn("state")
                .sortDirection(Sort.Direction.ASC).build();


        // perform get
        var ret = mockMvc.perform(get("/addresses")
                        .param(PageParams.DIRECTION_PARAM, String.valueOf(params.getSortDirection()))
                        .param(PageParams.COLUMN_PARAM, "state")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        var actual = readPageResponse(ret.getResponse().getContentAsString(), AddressDto.class).getItems();

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getState)).toList(), actual);

        var expected = sorted.stream().limit(length).toList();
        verify(expected, actual);
    }

    @Test
    public void getAddressListPage_sort_id_asc_page_2() throws Exception {
        // populate address table
        addAddressesToDb(members.get(4), 4);
        addAddressesToDb(members.get(0), 0);
        addAddressesToDb(members.get(3), 5);
        addAddressesToDb(members.get(2), 2);
        addAddressesToDb(members.get(1), 1);

        PageParams params = PageParams.builder()
                .pageSize(5)
                .pageNumber(2)
                .sortColumn("id")
                .sortDirection(Sort.Direction.ASC).build();

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getId)).toList();

        var pageSize = params.getPageSize();
        var count = sorted.size();
        var pages = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        var length = count < pageSize ? count : pageSize;

        // perform get
        var ret = mockMvc.perform(get("/addresses")
                        .param(PageParams.PAGE_PARAM, String.valueOf(params.getPageNumber()))
                        .param(PageParams.SIZE_PARAM, String.valueOf(params.getPageSize()))
                        .param(PageParams.DIRECTION_PARAM, String.valueOf(params.getSortDirection()))
                        .param(PageParams.COLUMN_PARAM, params.getSortColumn())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        var actual = readPageResponse(ret.getResponse().getContentAsString(), AddressDto.class).getItems();

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getId)).toList(), actual);

        var skip = params.getPageSize() * params.getPageNumber();
        var expected = sorted.stream().skip(skip).limit(length).toList();

        verify(expected, actual);
    }

    @Test
    public void getAddressListPage_sort_state_desc_page_1() throws Exception {
        // populate address table
        addAddressesToDb(members.get(4), 4);
        addAddressesToDb(members.get(0), 0);
        addAddressesToDb(members.get(3), 5);
        addAddressesToDb(members.get(2), 2);
        addAddressesToDb(members.get(1), 1);

        PageParams params = PageParams.builder()
                .pageSize(5)
                .pageNumber(1)
                .sortColumn("state")
                .sortDirection(Sort.Direction.DESC).build();

        var sorted = addressRepo.findAll().stream()
                .sorted(Comparator.comparing(AddressEntity::getState).reversed()).toList();

        var pageSize = params.getPageSize();
        var count = sorted.size();
        var pages = count / pageSize + (count % pageSize == 0 ? 0 : 1);
        var length = count < pageSize ? count : pageSize;

        // perform get
        var ret = mockMvc.perform(get("/addresses")
                        .param(PageParams.PAGE_PARAM, String.valueOf(params.getPageNumber()))
                        .param(PageParams.SIZE_PARAM, String.valueOf(params.getPageSize()))
                        .param(PageParams.DIRECTION_PARAM, String.valueOf(params.getSortDirection()))
                        .param(PageParams.COLUMN_PARAM, params.getSortColumn())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(pages))
                .andExpect(jsonPath("$.total-count").value(count))
                .andExpect(jsonPath("$.items.length()").value(length))
                .andDo(print()).andReturn();

        var actual = readPageResponse(ret.getResponse().getContentAsString(), AddressDto.class).getItems();

        assertEquals(actual.stream()
                .sorted(Comparator.comparing(AddressDto::getState).reversed()).toList(), actual);

        var skip = params.getPageSize() * params.getPageNumber();
        var expected = sorted.stream().skip(skip).limit(length).toList();

        verify(expected, actual);
    }

    @Test
    public void createAddresses_returns_addressDto_status_created() throws Exception {
        // create addresses
        var create = AddressCreators.createAddressCreateDto(0);
        create.setMemberId(members.get(0).getId());

        // perform POST
        var ret = mockMvc.perform(post("/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.member-id").value(create.getMemberId()))
                .andExpect(jsonPath("$.address1").value(create.getAddress1()))
                .andExpect(jsonPath("$.address2").value(create.getAddress2()))
                .andExpect(jsonPath("$.city").value(create.getCity()))
                .andExpect(jsonPath("$.state").value(create.getState()))
                .andExpect(jsonPath("$.zip").value(create.getZip()))
                .andExpect(jsonPath("$.address-type").value(create.getAddressType().getAddressName()))
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
        var entities = addAddressesToDb(members.get(1), 0);
        var id = entities.get(0).getId();

        // perform DELETE
        mockMvc.perform(delete("/addresses/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        // verify
        var address = addressRepo.findById(id);
        assertFalse(address.isPresent());
    }

    @Test
    public void update_returns_addressDto_status_ok() throws Exception {
        // create several addresses
        var entities = addAddressesToDb(members.get(1), 0);
        var update = AddressCreators.createAddressCreateDto(members.get(1).getId());
        var id = entities.get(1).getId();

        // perform PUT
        mockMvc.perform(put("/addresses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.member-id").value(update.getMemberId()))
                .andExpect(jsonPath("$.member-id").value(update.getMemberId()))
                .andExpect(jsonPath("$.address1").value(update.getAddress1()))
                .andExpect(jsonPath("$.address2").value(update.getAddress2()))
                .andExpect(jsonPath("$.city").value(update.getCity()))
                .andExpect(jsonPath("$.state").value(update.getState()))
                .andExpect(jsonPath("$.zip").value(update.getZip()))
                .andExpect(jsonPath("$.address-type").value(update.getAddressType().getAddressName()))
                .andDo(print());

        // verify
        var entity = addressRepo.findById(id);

        assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    private List<AddressEntity> addAddressesToDb(MemberEntity member, int startFlag) {
        var entities = AddressCreators.createEntityList(3, startFlag);
        entities.forEach(e -> e.setMember(member));
        entities.get(0).setAddressType(AddressType.Home);
        entities.get(1).setAddressType(AddressType.Work);
        entities.get(2).setAddressType(AddressType.Other);
        return addressRepo.saveAllAndFlush(entities);
    }

    private void verify(AddressCreateDto expected, AddressEntity actual) {
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

    private <T> PageResponse<T> readPageResponse(String json, Class<T> contentClass) throws IOException {
        JavaType type = mapper.getTypeFactory().constructParametricType(PageResponse.class, contentClass);
        return mapper.readValue(json, type);
    }

}
