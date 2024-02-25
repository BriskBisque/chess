package server;

import spark.Response;

import java.util.Objects;

public record UserResponse (boolean success, String username, String AuthToken){}