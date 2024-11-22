package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.data.domain.entities.AddressEntity;
import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.contracts.mappers.AddressMapper;
import com.smh.club.api.hateoas.mappers.AddressMapperImpl;
import com.smh.club.api.hateoas.models.AddressModel;
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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InstancioExtension.class)
public class AddressModelAssemblerTests extends AssemblerTests{

    private AddressAssemblerImpl assembler;

    private final AddressMapper mapper =
        new AddressMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);

    @BeforeEach
    public void init() {
        var components = UriComponentsBuilder.fromHttpUrl("http://localhost/api/v2/addresses").build();
        this.assembler = new AddressAssemblerImpl(mapper,
            new PagedResourcesAssembler<>(null, components));
    }

    @Test
    public void model_contains_links() {
        // setup
        var entity = Instancio.create(AddressEntity.class);

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
        var entity = Instancio.create(AddressEntity.class);

        // execute
        var model = assembler.toModel(entity);

        // verify
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getMember().getId(), model.getMemberId());
        assertEquals(entity.getAddress1(), model.getAddress1());
        assertEquals(entity.getAddress2(), model.getAddress2());
        assertEquals(entity.getCity(), model.getCity());
        assertEquals(entity.getState(), model.getState());
        assertEquals(entity.getZip(), model.getZip());
    }

    @Test
    public void from_page_to_pageModel () {
        // setup
        var entities = Instancio.ofList(AddressEntity.class)
            .size(10)
            .withUnique(field(AddressEntity::getId))
            .create();
        var page = createPage(entities);

        // execute
        var ret = assembler.toPagedModel(page);

        // verify
        assertInstanceOf(AddressModel.class, ret.getContent().stream().toList().get(0));
    }
}
