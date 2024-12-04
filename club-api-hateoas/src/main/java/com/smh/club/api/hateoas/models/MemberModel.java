package com.smh.club.api.hateoas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.club.api.data.entities.MemberEntity;
import com.smh.club.api.hateoas.validation.constraints.ValidMember;
import com.smh.club.api.shared.annotations.SortExclude;
import com.smh.club.api.shared.annotations.SortTarget;
import com.smh.club.api.shared.validators.constraints.BirthDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@ValidMember
@SortTarget(MemberEntity.class)
public class MemberModel extends RepresentationModel<MemberModel> {

    private int id;

    @Min(1)
    @JsonProperty("member-number")
    private int memberNumber;

    @NotBlank
    @JsonProperty("first-name")
    private String firstName;

    @SortExclude
    @JsonProperty("middle-name")
    private String middleName;

    @NotBlank
    @JsonProperty("last-name")
    private String lastName;

    @SortExclude
    @JsonProperty("suffix")
    private String suffix;

    @NotNull
    @BirthDate
    @JsonProperty("birth-date")
    private LocalDate birthDate;

    @NotNull
    @PastOrPresent
    @JsonProperty("joined-date")
    private LocalDate joinedDate;
}
