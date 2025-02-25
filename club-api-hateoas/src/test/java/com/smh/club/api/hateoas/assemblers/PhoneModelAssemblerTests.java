package com.smh.club.api.hateoas.assemblers;

import com.smh.club.api.hateoas.config.MapperConfig;
import com.smh.club.api.hateoas.contracts.mappers.PhoneMapper;
import com.smh.club.api.hateoas.domain.entities.PhoneEntity;
import com.smh.club.api.hateoas.mappers.PhoneMapperImpl;
import com.smh.club.api.hateoas.models.PhoneModel;
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
public class PhoneModelAssemblerTests extends AssemblerTests{


    private PhoneAssemblerImpl assembler;

    private final PhoneMapper mapper =
        new PhoneMapperImpl(new MapperConfig().createModelMapper());

    @WithSettings
    private final Settings settings = Settings.create()
        .set(Keys.SET_BACK_REFERENCES, true)
        .set(Keys.JPA_ENABLED, true)
        .set(Keys.COLLECTION_MAX_SIZE, 0);

    @BeforeEach
    public void init() {
        var components = UriComponentsBuilder.fromUriString("http://localhost/api/v2/phones").build();
        this.assembler = new PhoneAssemblerImpl(mapper,
            new PagedResourcesAssembler<>(null, components));
    }

    @Test
    public void model_contains_links() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var model = assembler.toModel(entity);

        // verify
        assertTrue(model.getLinks().hasSize(3));

        var link = model.getLink(IanaLinkRelations.SELF);
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/phones/" + entity.getId()));

        link = model.getLink("update");
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/phones/" + entity.getId()));

        link = model.getLink("delete");
        assertTrue(link.isPresent());
        assertTrue(link.get().getHref().endsWith("/api/v2/phones/" + entity.getId()));

    }

    @Test
    public void model_matches_entity() {
        // setup
        var entity = Instancio.create(PhoneEntity.class);

        // execute
        var model = assembler.toModel(entity);

        // verify
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getMember().getId(), model.getMemberId());
        assertEquals(entity.getPhoneNumber(), model.getPhoneNumber());
        assertEquals(entity.getPhoneType(), model.getPhoneType());
    }

    @Test
    public void from_page_to_pageModel () {
        // setup
        var entities = Instancio.ofList(PhoneEntity.class)
            .size(10)
            .withUnique(field(PhoneEntity::getId))
            .create();
        var page = createPage(entities);

        // execute
        var ret = assembler.toPagedModel(page);

        // verify
        assertInstanceOf(PhoneModel.class, ret.getContent().stream().toList().get(0));
    }
}
