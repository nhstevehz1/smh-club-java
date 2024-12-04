package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO for members.  When serialize to JSON, links are generated that
 * represent controller endpoints.
 * Extends {@link RepresentationModel}.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SortTarget(MemberEntity.class)
public class MemberModel extends RepresentationModel<MemberModel> {

    private int id;

    @JsonProperty("member-number")
    private int memberNumber;

    @NotEmpty
    @JsonProperty("first-name")
    private String firstName;

    @SortExclude
    @JsonProperty("middle-name")
    private String middleName;

    @NotEmpty
    @JsonProperty("last-name")
    private String lastName;

    @SortExclude
    @JsonProperty("suffix")
    private String suffix;

    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @JsonProperty("joined-date")
    private LocalDate joinedDate;
}
