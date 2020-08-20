package com.delaroystudios.alarmreminder.Adapter;

import com.delaroystudios.alarmreminder.Model.Route;

public interface IClickListenerRouteAdapter {
    void gotoUpdateRoute(Route route);
    void itemLongClick(int position);
    void gotoTimeRemaining(int iD, long timeCurrent);
}
