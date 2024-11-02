package com.smh.club.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smh.club.api.common.services.MemberService;
import com.smh.club.api.models.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberControllerImpl.class)
public class MemberControllerTests extends ControllerTestBase<Member> {

    @MockBean
    private MemberService svc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnMember() throws Exception {
        // setup
        var id = 12;
        var member = createDataObject(id);
        when(svc.getItem(id)).thenReturn(Optional.of(member));

        // execute
        mockMvc.perform(get("/members/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.member-number").value(member.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(member.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(member.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(member.getLastName()))
                .andExpect(jsonPath("$.suffix").value(member.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(member.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(member.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).getItem(id);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldCreateMember() throws Exception {
        // setup
        var ret = createDataObject(0);
        when(svc.createItem(ret)).thenReturn(ret);

        // execute and verify
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ret))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).createItem(ret);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldUpdateMember() throws Exception {
        // setup
        var id = 10;
        var ret = createDataObject(10);
        when(svc.updateItem(id, ret)).thenReturn(Optional.of(ret));

        // execute and verify
        mockMvc.perform(put("/members/{id}", ret.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ret))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ret.getId()))
                .andExpect(jsonPath("$.member-number").value(ret.getMemberNumber()))
                .andExpect(jsonPath("$.first-name").value(ret.getFirstName()))
                .andExpect(jsonPath("$.middle-name").value(ret.getMiddleName()))
                .andExpect(jsonPath("$.last-name").value(ret.getLastName()))
                .andExpect(jsonPath("$.suffix").value(ret.getSuffix()))
                .andExpect(jsonPath("$.birth-date").value(ret.getBirthDate().toString()))
                .andExpect(jsonPath("$.joined-date").value(ret.getJoinedDate().toString()))
                .andDo(print());

        verify(svc).updateItem(ret.getId(), ret);
        verifyNoMoreInteractions(svc);
    }

    @Test
    public void shouldDeleteMember() throws Exception {
        // setup
        var id = 1;
        doNothing().when(svc).deleteItem(id);

        // execute and verify
        mockMvc.perform(delete("/members/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Override
    protected Member createDataObject(int flag) {
        var now = LocalDate.now();

        return Member.builder()
                .id(flag)
                .memberNumber(flag + 10)
                .firstName("first_" + flag)
                .middleName("middle_" + flag)
                .lastName("last_" + flag)
                .suffix("suffix_" + flag)
                .birthDate(now.minusYears(30))
                .joinedDate(now.minusYears(2))
                .build();
    }
}
