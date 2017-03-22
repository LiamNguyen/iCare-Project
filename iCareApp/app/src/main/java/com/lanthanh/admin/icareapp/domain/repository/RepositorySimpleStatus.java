package com.lanthanh.admin.icareapp.domain.repository;

/**
 * Created by long.vu on 3/22/2017.
 */

public enum RepositorySimpleStatus {
    SUCCESS,
    PATTERN_FAIL,
    INVALID_USERNAME,
    INVALID_PASSWORD,
    INVALID_TOKEN,
    USERNAME_PASSWORD_NOT_MATCH,
    USERNAME_EXISTED,
    UNKNOWN_ERROR,
    API_ERROR,
    REQUIRED_FIELD_MISSING,
    TIME_HAS_BEEN_BOOKED,
    TIME_BOOKED_SUCCESSFULLY,
    PAGE_NOT_FOUND
}
