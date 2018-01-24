package com.manev.quislisting.web.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class ResponseUtil {

    private ResponseUtil() {
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        if (maybeResponse.isPresent()){
            return ResponseEntity.ok().headers(header).body(maybeResponse.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}