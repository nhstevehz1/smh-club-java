package com.smh.club.api.dto.email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@NoArgsConstructor
@SuperBuilder
public class EmailCreateDto extends EmailBaseDto {
}
