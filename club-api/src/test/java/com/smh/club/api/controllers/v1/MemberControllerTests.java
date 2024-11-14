package com.smh.club.api.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.controllers.ControllerTests;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.dto.CreateMemberDto;
import com.smh.club.api.dto.MemberDetailDto;
import com.smh.club.api.dto.MemberDto;
import com.smh.club.api.request.PageParams;
import com.smh.club.api.response.PageResponse;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("tests")
@ExtendWith(InstancioExtension.class)
@WebMvcTest(MemberControllerImpl.class)
public class MemberControllerTests extends ControllerTests {

    @MockBean
    private MemberService svc;

    @WithSettings
    private final Settings settings =
            Settings.create().set(Keys.SET_BACK_REFERENCES, true)
                    .set(Keys.JPA_ENABLED, true)
                    .set(Keys.MAX_DEPTH, 4);

    @Autowired
    protected MemberControllerTests(MockMvc mockMvc, ObjectMapper objMapper) {
        super(mockMvc, objMapper, "/api/v1/members");
    }

    @Test
    public void shouldReturnPage() throws Exception {
        // setup
        var ret = Instancio.createList(MemberDto.class);

        var params = PageParams.builder().pageNumber(2).pageSize(10).sortColumn("id")
                .sortDirection(Sort.Direction.DESC).build();

        var response = PageResponse.<MemberDto>builder()
                .totalPages(100).totalCount(20)
                .items(ret)
                .build();

        when(svc.getMemberListPage(any(PageParams.class))).thenReturn(response);

        // execute and verify
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total-pages").value(response.getTotalPages()))
                .andExpect(jsonPath("$.total-count").value(response.getTotalCount()))
                .andExpect(jsonPath("$.items.length()").value(response.getItems().size()))
                .andDo(print());

        verify(svc).getMemberListPage(any(PageParams.class));
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnMember() throws Exception {
        // setup
        var ret = Instancio.create(MemberDto.class);
        when(svc.getMember(ret.getId())).thenReturn(Optional.of(ret));

        // execute
        mockMvc.perform(get(path + "/{id}", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).getMember(ret.getId());
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnNotFound_when_memberId_does_not_exist() throws Exception {
        // setup
        var id = 12;
        when(svc.getMember(id)).thenReturn(Optional.empty());

        // execute
        mockMvc.perform(get(path + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getMember(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreate() throws Exception {
        // setup
        var ret = Instancio.create(MemberDto.class);
        var create = modelMapper.map(ret, CreateMemberDto.class);
        when(svc.createMember(create)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(ret))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).createMember(create);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldUpdate() throws Exception {
        // setup
        var ret = Instancio.create(MemberDto.class);
        var update = modelMapper.map(ret, CreateMemberDto.class);
        when(svc.updateMember(ret.getId(), update)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put(path + "/{id}", ret.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(ret))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).updateMember(ret.getId(), update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void update_member_should_return_badRequest() throws Exception {
        // setup
        var id = 10;
        var update = Instancio.create(CreateMemberDto.class);
        when(svc.updateMember(id, update)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(put(path + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(svc).updateMember(id, update);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDelete() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteMember(id);

        // execute and verify
        mockMvc.perform(delete(path + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(svc).deleteMember(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldReturnMemberDetail() throws Exception {
        // setup
        var ret = Instancio.create(MemberDetailDto.class);

        when(svc.getMemberDetail(ret.getId())).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(get(path + "/{id}/detail", ret.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andExpect(jsonPath("$.addresses.length()").value(ret.getAddresses().size()))
                .andExpect(jsonPath("$.emails.length()").value(ret.getEmails().size()))
                .andExpect(jsonPath("$.phones.length()").value(ret.getPhones().size()))
                .andExpect(jsonPath("$.renewals.length()").value(ret.getRenewals().size()))
                .andDo(print());

        verify(svc).getMemberDetail(ret.getId());
        verifyNoMoreInteractions(svc);

    }

    @Test
    public void shouldReturnEmptyMemberDetails() throws Exception {
        // setup
        var id = 1;
        when(svc.getMemberDetail(id)).thenReturn(Optional.empty());

        // execute and verify
        mockMvc.perform(get(path + "/{id}/detail", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(svc).getMemberDetail(id);
        verifyNoMoreInteractions(svc);

    }
}
