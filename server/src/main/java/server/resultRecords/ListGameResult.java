package server.resultRecords;

import server.resultRecords.GameResult;

import java.util.Collection;

public record ListGameResult(Collection<GameResult> games) {}
