package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.contracts.mappers.MemberMapper;
import com.smh.club.api.hateoas.mappers.MemberMapperImpl;
import com.smh.club.api.hateoas.models.MemberModel;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.web.util.UriComponentsBuilder;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InstancioExtension.class)
public class MemberModelAssemblerTests extends AssemblerTests{

    private MemberAssemblerImpl assembler;

    private final MemberMapper mapper
            = new MemberMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.SET_BACK_REFERENCES, true)
            .set(Keys.JPA_ENABLED, true)
            .set(Keys.COLLECTION_MAX_SIZE, 0);

    @BeforeEach
    public void init() {
        var components = UriComponentsBuilder.fromHttpUrl("http://localhost/api/v2/members").build();
        this.assembler = new MemberAssemblerImpl(mapper,
            new PagedResourcesAssembler<>(null, components));
    }

    @Test
    public void model_contains_links() {
        // setup
        var entity = Instancio.create(MemberEntity.class);

        // execute
        var model = assembler.toModel(entity);

        // verify
        assertTrue(model.getLinks().hasSize(3));

        var link = model.getLink(IanaLinkRelations.SELF);
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/members/" + entity.getId()));

        link = model.getLink("update");
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/members/" + entity.getId()));

        link = model.getLink("delete");
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/members/" + entity.getId()));

    }

    @Test
    public void model_matches_entity() {
       // setup
       var entity = Instancio.create(MemberEntity.class);

       // execute
       var model = assembler.toModel(entity);

       // verify
       assertEquals(entity.getId(), model.getId());
       assertEquals(entity.getMemberNumber(), model.getMemberNumber());
       assertEquals(entity.getFirstName(), model.getFirstName());
       assertEquals(entity.getMiddleName(), model.getMiddleName());
       assertEquals(entity.getLastName(), model.getLastName());
       assertEquals(entity.getSuffix(), model.getSuffix());
       assertEquals(entity.getBirthDate(), model.getBirthDate());
       assertEquals(entity.getJoinedDate(), model.getJoinedDate());
    }

    @Test
    public void from_page_to_pageModel () {
        // setup
        var entities = Instancio.ofList(MemberEntity.class)
                .size(10)
                .withUnique(field(MemberEntity::getId))
                .withUnique(field(MemberEntity::getMemberNumber))
                .create();
        var page = createPage(entities);

        // execute
        var ret = assembler.toPagedModel(page);

        // verify
        assertInstanceOf(MemberModel.class, ret.getContent().stream().toList().get(0));
    }

}
