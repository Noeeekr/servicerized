package com.github.noeeekr.servicerized.identity.repository.dto;

/**
 * Internal Dto is meant to provide a version of a model Entity that doesn't not contains relations
 * that are fetched. This is meant for json serialization.
 * 
 * Internal Dto's must implement this interface, also this interface must be a required type for all
 * methods arguments that write entities as json.
 */
public interface SerializableDto {

}
