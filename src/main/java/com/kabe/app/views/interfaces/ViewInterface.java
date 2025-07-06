package com.kabe.app.views.interfaces;

public interface ViewInterface {
    interface NavigationHandler {
        void handleNavigation(String viewName);
    }
    
    void setNavigationHandler(NavigationHandler handler);
    void show();
}