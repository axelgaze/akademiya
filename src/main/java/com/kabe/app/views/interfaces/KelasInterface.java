package com.kabe.app.views.interfaces;
import com.kabe.app.controllers.KelasController;
import com.kabe.app.models.Kelas;

public interface KelasInterface {
    interface NavigationHandler {
        void handleNavigation(String viewName);
    }
    
    void setNavigationHandler(NavigationHandler handler);
    void show();

    Kelas getSelectedKelas();
    KelasController getKelasController();
}