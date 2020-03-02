package io.quell.techtest.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@ApiModel
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    @ApiModelProperty(required = true)
    String path;

    @ApiModelProperty(required = true)
    int status;

    @ApiModelProperty(required = true)
    String message;
}
