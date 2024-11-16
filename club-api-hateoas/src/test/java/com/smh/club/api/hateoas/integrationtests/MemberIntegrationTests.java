package com.smh.club.api.hateoas.integrationtests;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;

@ActiveProfiles("tests")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class MemberIntegrationTests {//extends IntegrationTests {

    /*@Value("${request.paging.size}")
    private int defaultPageSize;

    @Autowired
    private MembersRepo memberRepo;

    @Autowired
    public MemberIntegrationTests(MockMvc mockMvc, ObjectMapper mapper) {
        super(mockMvc,mapper, "/api/v2/members");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_no_params(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        var actual = executeGetListPageV2(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize, 0);

        assertNotNull(actual);
        //assertEquals(actual.stream()
        //        .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        //var expected = sorted.stream().limit(defaultPageSize).toList();
        //verify(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 20, 50})
    public void getListPage_sortDir_desc(int entitySize) throws Exception {
        addEntitiesToDb(entitySize);

        var sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber).reversed()).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.DIRECTION_NAME, Sort.Direction.DESC.toString());

        var actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
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
        Assertions.assertEquals(entitySize, sorted.size());
        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SIZE_NAME, pageSize);

        var actual = executeGetListPage(MemberDto.class, path,
                valueMap, sorted.size(), Integer.parseInt(pageSize));

        Assertions.assertEquals(actual.stream()
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
        Assertions.assertEquals(entitySize, sorted.size());

        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.PAGE_NAME, String.valueOf(page));

        var actual = executeGetListPage(MemberDto.class, path,
                valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
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
        Assertions.assertEquals(entitySize, sorted.size());
        MultiValueMap<String,String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "id");

        var actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getId)).toList(), actual);

        var expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by member-number
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparingInt(MemberEntity::getMemberNumber)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "member-number");

        actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparingInt(MemberDto::getMemberNumber)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        // sort by first-name
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getFirstName)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "first-name");

        actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getFirstName)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

        //sort by last-name
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getLastName)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "last-name");

        actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getLastName)).toList(), actual);

        expected = sorted.stream().limit(10).toList();
        verify(expected, actual);

        //sort by birthdate
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getBirthDate)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "birth-date");

        actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getBirthDate)).toList(), actual);

        expected = sorted.stream().limit(10).toList();
        verify(expected, actual);

        //sort by joined-date
        sorted = memberRepo.findAll()
                .stream().sorted(Comparator.comparing(MemberEntity::getJoinedDate)).toList();
        Assertions.assertEquals(entitySize, sorted.size());

        valueMap = new LinkedMultiValueMap<>();
        valueMap.add(PagingConfig.SORT_NAME, "joined-date");

        actual = executeGetListPage(MemberDto.class, path, valueMap, sorted.size(), defaultPageSize);

        Assertions.assertEquals(actual.stream()
                .sorted(Comparator.comparing(MemberDto::getJoinedDate)).toList(), actual);

        expected = sorted.stream().limit(defaultPageSize).toList();
        verify(expected, actual);

    }

    @Test
    public void get_returns_memberDto_status_ok() throws Exception {
        // Setup
        var member = addEntitiesToDb(10).get(5);

        // perform get
        var ret = mockMvc.perform(MockMvcRequestBuilders.get(path + "/{id}", member.getId())
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.length()").value(3))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                .andExpect(jsonPath("$._links.update").exists())
                .andExpect(jsonPath("$._links.update.href").isNotEmpty())
                .andExpect(jsonPath("$._links.create").exists())
                .andExpect(jsonPath("$._links.create.href").isNotEmpty())
                .andDo(print())
                .andReturn();

        var dto = mapper.readValue(ret.getResponse().getContentAsString(), MemberDto.class);

        verify(member, dto);
    }

    @Test
    public void get_returns_status_notFound() throws Exception {
        // Setup
        var id = 100;

        // perform get
        mockMvc.perform(get(path + "/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }


    @Test
    public void createMember_returns_memberDto_status_created() throws Exception {
        // Setup
        addEntitiesToDb(10);
        var create = Instancio.of(MemberDto.class)
                .create();

        // perform POST
        var ret = mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.length()").value(3))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                .andExpect(jsonPath("$._links.update").exists())
                .andExpect(jsonPath("$._links.update.href").isNotEmpty())
                .andExpect(jsonPath("$._links.create").exists())
                .andExpect(jsonPath("$._links.create.href").isNotEmpty())
                .andDo(print())
                .andReturn();

        // verify
        var dto = mapper.readValue(ret.getResponse().getContentAsString(), MemberDto.class);
        var entity =  memberRepo.findById(dto.getId());

        Assertions.assertTrue(entity.isPresent());
        verify(create, entity.get());
    }

    @Test
    public void update_returns_memberDto_status_ok() throws Exception{
        // create several members
        var member = addEntitiesToDb(10).get(5);
        var update = Instancio.of(MemberDto.class)
                .set(field(MemberDto::getId), member.getId())
                .set(field(MemberDto::getMemberNumber), member.getMemberNumber())
                .create();

        mockMvc.perform(MockMvcRequestBuilders.put( path + "/{id}", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.length()").value(3))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.self.href").isNotEmpty())
                .andExpect(jsonPath("$._links.update").exists())
                .andExpect(jsonPath("$._links.update.href").isNotEmpty())
                .andExpect(jsonPath("$._links.create").exists())
                .andExpect(jsonPath("$._links.create.href").isNotEmpty())
                .andDo(print());

        var entity = memberRepo.findById(member.getId());

        Assertions.assertTrue(entity.isPresent());
        verify(update, entity.get());
    }

    @Test
    public void update_returns_status_badRequest() throws Exception {
        // Setup
        var id = 100;
        var update = Instancio.create(MemberDto.class);

        // perform get
        mockMvc.perform(put(path + "/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(mapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());
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

        var member = memberRepo.findById(id);
        Assertions.assertFalse(member.isPresent());
    }

    @Test
    public void getCount_returns_count_status_ok() throws Exception {
        // setup
        var count = addEntitiesToDb(25).size();

        // execute
        var ret = mockMvc.perform(get(path + "/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        var response = mapper.readValue(ret.getResponse().getContentAsString(), CountResponse.class);
        Assertions.assertEquals(count, response.getCount());
    }

    private List<MemberEntity> addEntitiesToDb(int size) {
        var entities = Instancio.ofList(MemberEntity.class)
                .size(size) // must be before withSettings
                .withSettings(getSettings())
                .withUnique(field(MemberEntity::getMemberNumber))
                .ignore(field(MemberEntity::getId))
                .create();

        return memberRepo.saveAllAndFlush(entities);
    }

    private void verify(MemberDto expected, MemberEntity actual) {
        Assertions.assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getMiddleName(), actual.getMiddleName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getSuffix(), actual.getSuffix());
        Assertions.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assertions.assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }

    private void verify(MemberEntity expected, MemberDto actual) {
        Assertions.assertEquals(expected.getMemberNumber(), actual.getMemberNumber());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getMiddleName(), actual.getMiddleName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getSuffix(), actual.getSuffix());
        Assertions.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assertions.assertEquals(expected.getJoinedDate(), actual.getJoinedDate());
    }


    private void verify(List<MemberEntity> expected, List<MemberDto> actual) {
        expected.forEach(e -> {
            var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
            assertTrue(found.isPresent());
            verify(e, found.get());
        });
    }*/
}
