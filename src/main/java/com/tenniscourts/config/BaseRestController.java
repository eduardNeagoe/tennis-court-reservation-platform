package com.tenniscourts.config;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

@ApiIgnore
public class BaseRestController {

  protected URI locationByEntity(Long entityId){
        return ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(entityId).toUri();
    }
}
