package com.foodmanager.listeners;

public interface StatsListener {
    void onGetStatsUser(int createdAt, int status);

    void onGetStatsReceitas(int stat);

    void onGetStatsDespensa(int stat);
}
