package com.Smoke.Signals;

public enum MessageType {
    PUBLIC,
    READ_RESPONSE,
    COMMENT,
    POST,
    EDIT_MESSAGE,
    IDENTITY_PACKET, LOG_PACKET, LOG_REQUEST,
    TYPING,
    FILE,
    LEAVE,
    REMOVE,
    ADD,
    EDIT_COMMENT, DELETE_COMMENT, EDIT_COMMENT_WITH_IMAGE,
    DELETE_POST,
    ERROR,
    GET_PUBLIC_PAGE_NAME, RETURN_PUBLIC_PAGE_NAME,
    RECEIVED,
    IS_ONLINE, IS_OFFLINE,
    SEND_INVITE,
    UNKNOWN
}