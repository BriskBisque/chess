package server;

import spark.Response;

import java.util.Objects;

public record UserResponse (String username, String authToken){}