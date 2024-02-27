package model;

import java.util.Collection;

public record ObserversData(GameData gameData, Collection<String> observers) {}
