package server;

import spark.Response;

import java.util.Objects;

record UserResponse (String username, String AuthToken){}