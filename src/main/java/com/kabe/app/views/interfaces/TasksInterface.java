package com.kabe.app.views.interfaces;
import com.kabe.app.controllers.KelasController;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.Tugas;

public interface TasksInterface {
    interface NavigationHandler {
        void handleNavigation(String viewName);
    }
    
    void setNavigationHandler(NavigationHandler handler);
    void show();

    Tugas getSelectedTugas();
}