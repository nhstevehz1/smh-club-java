package com.smh.club.api.rest.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)/**/
@AllArgsConstructor
@SuperBuilder
public class EmailCreateDto extends EmailDto {}
