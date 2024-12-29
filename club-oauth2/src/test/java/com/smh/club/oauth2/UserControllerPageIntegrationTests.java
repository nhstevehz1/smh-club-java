package com.smh.club.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.oauth2.domain.entities.UserDetailsEntity;
import com.smh.club.oauth2.domain.repos.UserRepository;
import com.smh.club.oauth2.dto.UserDto;
import com.smh.club.oauth2.helpers.PageTestParams;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"tests"})
@ExtendWith(InstancioExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(
    provider = ZONKY,
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class UserControllerPageIntegrationTests {

  @Value("${spring.data.web.pageable.default-page-size:20}")
  private int defaultPageSize;

  @Value("${spring.data.rest.sort-param-name:sort}")
  private String sortParamName;

  @Value("${spring.data.rest.size-param-name:size}")
  private String sizeParamName;

  @Value("${spring.data.rest.page-param-name:page}")
  private String pageParamName;

  @Autowired
  private UserRepository repo;

  private final String path = "/api/v1/users";

  @WithSettings // Instancio settings
  private final Settings settings =
      Settings.create().set(Keys.SET_BACK_REFERENCES, true)
          .set(Keys.JPA_ENABLED, true)
          .set(Keys.COLLECTION_MAX_SIZE,0)
          .set(Keys.BEAN_VALIDATION_ENABLED, true);

  @Autowired
  public UserControllerPageIntegrationTests(
      MockMvc mockMvc,
      ObjectMapper mapper) {

    // setup RestAssured to use the MockMvc Context
    RestAssuredMockMvc.mockMvc(mockMvc);

    // Configure RestAssured to use the injected Object mapper.
    RestAssuredMockMvc.config =
        RestAssuredMockMvcConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
            (type, s) -> mapper));
  }

  @WithMockUser
  @ParameterizedTest
  @ValueSource(ints = {1, 5, 20, 50})
  public void getPage_returns_no_params(int entitySize) {
    addEntitiesToDb(entitySize);

    var sorted = repo.findAll()
        .stream().sorted(Comparator.comparing(UserDetailsEntity::getUsername)).toList();
    assertEquals(entitySize, sorted.size());

    Map<String, String> map = new HashMap<>();

    var testParams= PageTestParams.of(UserDto.class, map, path, sorted.size(),
        0, defaultPageSize);

    var actual = executeListPage(testParams);

    var expected = sorted.stream().limit(defaultPageSize).toList();

    verify(expected, actual);
  }

  @WithMockUser
  @ParameterizedTest
  @ValueSource(ints = {1, 5, 20, 50})
  public void getPage_sortDir_desc(int entitySize) {
    addEntitiesToDb(entitySize);

    var sorted = repo.findAll()
        .stream().sorted(Comparator.comparing(UserDetailsEntity::getUsername).reversed()).toList();

    Map<String, String> map = new HashMap<>();
    map.put(sortParamName,  "username,desc");

    var testParams = PageTestParams.of(UserDto.class, map, path, sorted.size(),
        0, defaultPageSize);

    var actual = executeListPage(testParams);
    assertEquals(actual.stream()
        .sorted(Comparator.comparing(UserDto::getUsername).reversed())
        .toList(), actual);

    var expected = sorted.stream().limit(defaultPageSize).toList();
    verify(expected, actual);
  }

  @WithMockUser
  @ParameterizedTest
  @ValueSource(ints = {2,5,8,10})
  public void getListPage_pageSize(int pageSize) {
    addEntitiesToDb(15);

    var sorted = repo.findAll()
        .stream().sorted(Comparator.comparing(UserDetailsEntity::getUsername)).toList();

    Map<String,String> map = new HashMap<>();
    map.put(sizeParamName, String.valueOf(pageSize));

    var testParams = PageTestParams.of(UserDto.class, map, path, sorted.size(),
        0, pageSize);

    var actual = executeListPage(testParams);

    assertEquals(actual.stream()
        .sorted(Comparator.comparing(UserDto::getUsername)).toList(), actual);

    var expected = sorted.stream().limit(pageSize).toList();
    verify(expected, actual);
  }

  @WithMockUser
  @ParameterizedTest
  @ValueSource(ints = {1, 5, 8})
  public void getListPage_page(int page) {
    var entitySize = 150;
    addEntitiesToDb(entitySize);

    var sorted = repo.findAll()
        .stream().sorted(Comparator.comparing(UserDetailsEntity::getUsername)).toList();

    Map<String,String> map = new HashMap<>();
    map.put(pageParamName, String.valueOf(page));

    var testParams = PageTestParams.of(UserDto.class, map, path, sorted.size(),
        page, defaultPageSize);

    var actual = executeListPage(testParams);

    assertEquals(actual.stream()
        .sorted(Comparator.comparing(UserDto::getUsername)).toList(), actual);

    var skip = page * defaultPageSize;
    var expected = sorted.stream().skip(skip).limit(defaultPageSize).toList();
    verify(expected, actual);
  }

  @WithMockUser
  @ParameterizedTest
  @ValueSource(strings = {"id", "username", "firstName", "middleName", "lastName", "email" })
  public void getListPage_sortColumn(String sort) {
    var entitySize = 50;
    addEntitiesToDb(entitySize);
    var sortFields = getSorts().get(sort);

    // sort by id
    var sorted = repo.findAll().stream().sorted(sortFields.getEntity()).toList();
    assertEquals(entitySize, sorted.size());

    var map = new HashMap<String, String>();
    map.put(sortParamName, sort);

    var testParams = PageTestParams.of(UserDto.class, map, path, sorted.size(),
        0, defaultPageSize);

    var actual = executeListPage(testParams);

    assertEquals(actual.stream().sorted(sortFields.getDto()).toList(), actual);

    var expected = sorted.stream().limit(defaultPageSize).toList();
    verify(expected, actual);
  }


  private void addEntitiesToDb(int size) {
    var entities = Instancio.ofList(UserDetailsEntity.class)
        .size(size)
        .ignore(field(UserDetailsEntity::getId))
        .create();

    repo.saveAllAndFlush(entities);
  }

  private void verify(List<UserDetailsEntity> expected, List<UserDto> actual) {
    expected.forEach(e -> {
      var found = actual.stream().filter(a -> a.getId() == e.getId()).findFirst();
      assertTrue(found.isPresent());
      verify(e, found.get());
    });
  }

  private void verify(UserDetailsEntity expected, UserDto actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getUsername(), actual.getUsername());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getFirstName(), actual.getFirstName());
    assertEquals(expected.getLastName(), actual.getLastName());
    assertEquals(expected.getMiddleName(), actual.getMiddleName());
  }

  private List<UserDto> executeListPage(PageTestParams<UserDto> testParams) {
    var result =
        given()
            .auth().none()
            .accept(MediaType.APPLICATION_JSON)
            .queryParams(testParams.getQuery())
            .when()
            .get(testParams.getPath());

    result.then()
        .expect(status().isOk())
        .expect(jsonPath("page.size").value(testParams.getPageSize()))
        .expect(jsonPath("page.totalElements").value(testParams.getTotalCount()))
        .expect(jsonPath("page.totalPages").value(testParams.getTotalPages()))
        .expect(jsonPath("page.number").value(testParams.getPageNumber()))
        .expect(jsonPath("page.isFirst").value(testParams.isFirst()))
        .expect(jsonPath("page.hasPrevious").value(testParams.hasPrevious()))
        .expect(jsonPath("page.hasNext").value(testParams.hasNext()))
        .expect(jsonPath("page.isLast").value(testParams.isLast()));

    return result.getBody().jsonPath().getList(testParams.getListPath(), testParams.getClazz());
  }

  private Map<String, SortFields<UserDetailsEntity, UserDto>> getSorts() {
    Map<String, SortFields<UserDetailsEntity, UserDto>> map = new HashMap<>();

    map.put("id", SortFields.of(Comparator.comparingLong(UserDetailsEntity::getId),
        Comparator.comparingLong(UserDto::getId)));

    map.put("username", SortFields.of(Comparator.comparing(UserDetailsEntity::getUsername),
        Comparator.comparing(UserDto::getUsername)));

    map.put("firstName", SortFields.of(Comparator.comparing(UserDetailsEntity::getFirstName),
        Comparator.comparing(UserDto::getFirstName)));

    map.put("middleName", SortFields.of(Comparator.comparing(UserDetailsEntity::getMiddleName),
        Comparator.comparing(UserDto::getMiddleName)));

    map.put("lastName", SortFields.of(Comparator.comparing(UserDetailsEntity::getLastName),
        Comparator.comparing(UserDto::getLastName)));

    map.put("email", SortFields.of(Comparator.comparing(UserDetailsEntity::getEmail),
        Comparator.comparing(UserDto::getEmail)));

    return map;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  @Builder
  private static class SortFields<E, M> {
    private Comparator<E> entity;
    private Comparator<M> dto;

    public static <E, M> SortFields<E, M> of(Comparator<E> entity, Comparator<M> dto) {
      return SortFields.<E, M>builder()
          .entity(entity)
          .dto(dto)
          .build();
    }
  }
}
