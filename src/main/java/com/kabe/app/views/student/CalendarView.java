package com.kabe.app.views.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import javafx.util.Duration;
import com.kabe.app.controllers.UserController;
import com.kabe.app.models.Kelas;
import com.kabe.app.models.Tugas;
import com.kabe.app.controllers.TugasController;
import com.kabe.app.controllers.KelasController;

public class CalendarView {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private GridPane calendarGrid;
    private Label monthYearLabel;
    private YearMonth currentMonth;
    private Map<LocalDate, List<TaskInfo>> tasksByDate;
    private Tooltip currentTooltip;
    private NavigationHandler navigationHandler;
    private UserController userController;
    private TugasController tugasController;
    private Tugas selectedTugas;
    private KelasController kelasController;

    public interface NavigationHandler {
        void handleNavigation(String viewName);
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
    
    // Task info class untuk menyimpan informasi tugas
    public static class TaskInfo {
        private String title;
        private String className;
        private String teacher;
        private String description;
        private String type;
        private Color statusColor;
        private int tugasId;
        private LocalDate deadline;
        
        public TaskInfo(String title, String className, String teacher, String description, String type, Color statusColor, int tugasId, LocalDate deadline) {
            this.title = title;
            this.className = className;
            this.teacher = teacher;
            this.description = description;
            this.type = type;
            this.statusColor = statusColor;
            this.tugasId = tugasId;
            this.deadline = deadline;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getClassName() { return className; }
        public String getTeacher() { return teacher; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public Color getStatusColor() { return statusColor; }
        public int getTugasId() { return tugasId; }
        public LocalDate getDeadline() { return deadline; }
    }
    
    public CalendarView(Stage stage, UserController userController, KelasController kelasController, TugasController tugasController) {
        this.userController = userController;
        this.kelasController = kelasController;
        this.tugasController = tugasController;
        this.stage = stage;
        this.currentMonth = YearMonth.now();
        this.tasksByDate = new HashMap<>();
        
        // Load tasks from database
        loadTasksFromDatabase();
        initializeView();
    }
    
    private void loadTasksFromDatabase() {
        tasksByDate.clear();
        
        try {
            // Get all tugas for the current student
            List<Tugas> tugasList = tugasController.getTugasBySiswa(userController.getUser().getId());
            
            for (Tugas tugas : tugasList) {
                if (tugas.getDeadline() != null) {
                    LocalDate deadlineDate = tugas.getDeadline().toLocalDateTime().toLocalDate();
                    
                    // Get kelas name
                    String kelasName = "Kelas";
                    try {
                        List<Kelas> kelasList = kelasController.getClassesByUser(userController.getUser().getId());
                        for (Kelas kelas : kelasList) {
                            if (kelas.getId() == tugas.getKelasId()) {
                                kelasName = kelas.getNama();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting kelas name: " + e.getMessage());
                    }
                    
                    // Get teacher name - assuming it's available through some relationship
                    String teacherName = "Pengajar";
                    // You might need to implement a method to get teacher name from tugas
                    // For now, we'll use a default value
                    
                    // Determine color based on deadline proximity and status
                    Color statusColor = determineTaskColor(deadlineDate, tugas.getId());
                    
                    TaskInfo taskInfo = new TaskInfo(
                        tugas.getTitle(),
                        kelasName,
                        teacherName,
                        tugas.getDeskripsi() != null ? tugas.getDeskripsi() : "Tidak ada deskripsi",
                        tugas.getTipe() != null ? tugas.getTipe() : "Individual",
                        statusColor,
                        tugas.getId(),
                        deadlineDate
                    );
                    
                    // Add to map
                    tasksByDate.computeIfAbsent(deadlineDate, k -> new ArrayList<>()).add(taskInfo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks from database: " + e.getMessage());
            e.printStackTrace();
            
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Memuat Data Tugas");
            alert.setContentText("Terjadi kesalahan saat memuat data tugas dari database.");
            alert.showAndWait();
        }
    }
    
    private Color determineTaskColor(LocalDate deadline, int tugasId) {
        try {
            // Check task status first
            String status = tugasController.getStatusTugas(tugasId, userController.getUser().getId());
            
            if ("Sudah Dikumpulkan".equals(status)) {
                return Color.web("#4CAF50"); // Green for completed
            } else if ("Terlambat".equals(status)) {
                return Color.web("#F44336"); // Red for overdue submission
            }
            // For "Belum Dikumpulkan", we'll proceed to deadline-based coloring
            
            // Color by deadline proximity for unsubmitted tasks
            LocalDate now = LocalDate.now();
            long daysUntilDeadline = now.until(deadline).getDays();
            
            if (daysUntilDeadline < 0) {
                return Color.web("#F44336"); // Red for overdue
            } else if (daysUntilDeadline <= 1) {
                return Color.web("#FF5722"); // Deep orange for due soon
            } else if (daysUntilDeadline <= 3) {
                return Color.web("#FF9800"); // Orange for due within 3 days
            } else {
                return Color.web("#FFC107"); // Amber for normal tasks
            }
        } catch (Exception e) {
            System.err.println("Error determining task color: " + e.getMessage());
            return Color.web("#9E9E9E"); // Gray for unknown status
        }
    }
    
    private void initializeView() {
        root = new BorderPane();
        
        // Background with Sumeru-inspired gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#F1F8E9")), // Light green
            new Stop(0.5, Color.web("#E8F5E8")), // Very light green
            new Stop(1, Color.web("#C8E6C9"))  // Soft green
        );
        
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));
        
        // Create sidebar
        createSidebar();
        
        // Create main content
        createMainContent();
        
        root.setLeft(sidebar);
        root.setCenter(mainContent);
        
        scene = new Scene(root, 1200, 800);
        stage.setTitle("Akademiya - Kalender");
        stage.setScene(scene);
    }
    
    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        
        // Sidebar background
        LinearGradient sidebarGradient = new LinearGradient(
            0, 0, 1, 0, true, null,
            new Stop(0, Color.web("#2D5016")), // Dark forest green
            new Stop(1, Color.web("#3D6B1F"))  // Medium forest green
        );
        
        BackgroundFill sidebarFill = new BackgroundFill(sidebarGradient, 
                                                        new CornerRadii(0, 15, 15, 0, false), 
                                                        null);
        sidebar.setBackground(new Background(sidebarFill));
        sidebar.setEffect(new DropShadow(15, Color.web("#1A3009")));
        
        // Logo and title
        VBox logoContainer = new VBox(5);
        logoContainer.setAlignment(Pos.CENTER);
        
        Label logoLabel = new Label("AKADEMIYA");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setTextFill(Color.web("#E8F5E8"));
        
        Label logoSubtitle = new Label("Platform Pembelajaran");
        logoSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        logoSubtitle.setTextFill(Color.web("#C8E6C9"));
        
        logoContainer.getChildren().addAll(logoLabel, logoSubtitle);
        
        // Navigation menu
        VBox navigationMenu = new VBox(10);
        navigationMenu.setPadding(new Insets(20, 0, 0, 0));
        
        Button dashboardBtn = createMenuButton("🏠 Dashboard", false);
        Button tasksBtn = createMenuButton("📋 Tugas", false);
        Button classesBtn = createMenuButton("🏫 Kelas", false);
        Button calendarBtn = createMenuButton("📅 Kalender", true);
        Button profileBtn = createMenuButton("👤 Profile", false);
        
        navigationMenu.getChildren().addAll(dashboardBtn, tasksBtn, classesBtn, calendarBtn);

        dashboardBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Dashboard");
            }
        });

        tasksBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Tugas");
            }
        });

        classesBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kelas");
            }
        });

        calendarBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Kalender");
            }
        });

        profileBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Profile");
            }
        });
        
        
        // User info at bottom
        VBox userInfo = new VBox(10);
        userInfo.setAlignment(Pos.CENTER);
        userInfo.setPadding(new Insets(20, 0, 0, 0));
        
        Label userAvatar = new Label("👤");
        userAvatar.setFont(Font.font(32));
        userAvatar.setTextFill(Color.web("#E8F5E8"));
        
        Label userName = new Label(userController.getUser().getFullName());
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        userName.setTextFill(Color.web("#E8F5E8"));
        
        Label userRole = new Label(userController.getUser().getRole());
        userRole.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        userRole.setTextFill(Color.web("#C8E6C9"));
        
        Button logoutBtn = createMenuButton("🚪 Logout", false);
        logoutBtn.setStyle("-fx-background-color: rgba(244, 67, 54, 0.8); " +
                          "-fx-background-radius: 8; " +
                          "-fx-text-fill: white; " +
                          "-fx-cursor: hand;");

        logoutBtn.setOnAction(e -> {
            if (navigationHandler != null) {
                navigationHandler.handleNavigation("Logout");
            }
        });
        
        userInfo.getChildren().addAll(userAvatar, userName, userRole, logoutBtn);
        
        sidebar.getChildren().addAll(logoContainer, navigationMenu, userInfo);
        VBox.setVgrow(navigationMenu, Priority.ALWAYS);
    }
    
    private Button createMenuButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        button.setPrefWidth(240);
        button.setPrefHeight(40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(10, 15, 10, 15));
        
        if (isActive) {
            button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #E8F5E8; " +
                           "-fx-cursor: hand;");
        } else {
            button.setStyle("-fx-background-color: transparent; " +
                           "-fx-background-radius: 8; " +
                           "-fx-text-fill: #C8E6C9; " +
                           "-fx-cursor: hand;");
        }
        
        // Hover effect
        button.setOnMouseEntered(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #E8F5E8; " +
                               "-fx-cursor: hand;");
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!isActive) {
                button.setStyle("-fx-background-color: transparent; " +
                               "-fx-background-radius: 8; " +
                               "-fx-text-fill: #C8E6C9; " +
                               "-fx-cursor: hand;");
            }
        });
        
        return button;
    }
    
    private void createMainContent() {
        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        
        // Header
        createHeader();
        
        // Refresh button
        createRefreshButton();
        
        // Calendar navigation
        createCalendarNavigation();
        
        // Calendar grid
        createCalendarGrid();
        
    }
    
    private void createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        Label headerTitle = new Label("Kalender");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        headerTitle.setTextFill(Color.web("#2D5016"));
        
        Label headerSubtitle = new Label("Lihat jadwal dan deadline tugas Anda");
        headerSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        headerSubtitle.setTextFill(Color.web("#4A7C26"));
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(headerTitle, headerSubtitle);
        
        header.getChildren().add(headerText);
        mainContent.getChildren().add(header);
    }
    
    private void createRefreshButton() {
        HBox refreshContainer = new HBox();
        refreshContainer.setAlignment(Pos.CENTER_RIGHT);
        refreshContainer.setPadding(new Insets(0, 0, 10, 0));
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        refreshBtn.setPrefHeight(35);
        refreshBtn.setPadding(new Insets(8, 15, 8, 15));
        refreshBtn.setStyle("-fx-background-color: #4CAF50; " +
                           "-fx-background-radius: 18; " +
                           "-fx-text-fill: white; " +
                           "-fx-cursor: hand;");
        
        refreshBtn.setOnAction(e -> {
            loadTasksFromDatabase();
            updateCalendar();
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Refresh");
            alert.setHeaderText("Data Berhasil Diperbarui");
            alert.setContentText("Kalender telah diperbarui dengan data terbaru.");
            alert.showAndWait();
        });
        
        refreshContainer.getChildren().add(refreshBtn);
        mainContent.getChildren().add(refreshContainer);
    }
    
    private void createCalendarNavigation() {
        HBox navigationContainer = new HBox(20);
        navigationContainer.setAlignment(Pos.CENTER);
        navigationContainer.setPadding(new Insets(0, 0, 20, 0));
        
        // Previous month button
        Button prevButton = new Button("❮");
        prevButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        prevButton.setPrefSize(40, 40);
        prevButton.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 20; " +
                           "-fx-text-fill: #2D5016; " +
                           "-fx-cursor: hand; " +
                           "-fx-border-color: #2D5016; " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 20;");
        prevButton.setOnAction(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        
        // Month year label
        monthYearLabel = new Label();
        monthYearLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        monthYearLabel.setTextFill(Color.web("#2D5016"));
        monthYearLabel.setAlignment(Pos.CENTER);
        monthYearLabel.setPrefWidth(200);
        
        // Next month button
        Button nextButton = new Button("❯");
        nextButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nextButton.setPrefSize(40, 40);
        nextButton.setStyle("-fx-background-color: white; " +
                           "-fx-background-radius: 20; " +
                           "-fx-text-fill: #2D5016; " +
                           "-fx-cursor: hand; " +
                           "-fx-border-color: #2D5016; " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 20;");
        nextButton.setOnAction(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });
        
        // Today button
        Button todayButton = new Button("Hari Ini");
        todayButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        todayButton.setPrefHeight(40);
        todayButton.setPadding(new Insets(10, 20, 10, 20));
        todayButton.setStyle("-fx-background-color: #2D5016; " +
                            "-fx-background-radius: 20; " +
                            "-fx-text-fill: white; " +
                            "-fx-cursor: hand;");
        todayButton.setOnAction(e -> {
            currentMonth = YearMonth.now();
            updateCalendar();
        });
        
        navigationContainer.getChildren().addAll(prevButton, monthYearLabel, nextButton, todayButton);
        mainContent.getChildren().add(navigationContainer);
    }
    
    private void createCalendarGrid() {
        VBox calendarContainer = new VBox(10);
        calendarContainer.setAlignment(Pos.CENTER);
        
        // Calendar wrapper with white background
        VBox calendarWrapper = new VBox();
        calendarWrapper.setStyle("-fx-background-color: white; " +
                               "-fx-background-radius: 15; " +
                               "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                               "-fx-border-width: 1; " +
                               "-fx-border-radius: 15;");
        calendarWrapper.setEffect(new DropShadow(10, Color.web("#E0E0E0")));
        calendarWrapper.setPadding(new Insets(20));
        
        // Days of week header
        HBox daysHeader = new HBox();
        daysHeader.setAlignment(Pos.CENTER);
        String[] dayNames = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        
        for (String day : dayNames) {
            Label dayLabel = new Label(day);
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            dayLabel.setTextFill(Color.web("#2D5016"));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefWidth(120);
            dayLabel.setPrefHeight(40);
            daysHeader.getChildren().add(dayLabel);
        }
        
        // Calendar grid
        calendarGrid = new GridPane();
        calendarGrid.setAlignment(Pos.CENTER);
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        
        calendarWrapper.getChildren().addAll(daysHeader, calendarGrid);
        calendarContainer.getChildren().add(calendarWrapper);
        
        mainContent.getChildren().add(calendarContainer);
        
        // Initial calendar update
        updateCalendar();
    }
    
    private void createLegend() {
        VBox legendContainer = new VBox(15);
        legendContainer.setAlignment(Pos.CENTER);
        legendContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Label legendTitle = new Label("Keterangan:");
        legendTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        legendTitle.setTextFill(Color.web("#2D5016"));
        
        HBox legendItems = new HBox(30);
        legendItems.setAlignment(Pos.CENTER);
        
        // Today legend
        HBox todayLegend = createLegendItem("Hari Ini", Color.web("#2D5016"));
        
        // Task status legends
        HBox completedLegend = createLegendItem("Sudah Dinilai", Color.web("#4CAF50"));
        HBox submittedLegend = createLegendItem("Sudah Dikumpulkan", Color.web("#2196F3"));
        HBox dueSoonLegend = createLegendItem("Deadline Dekat", Color.web("#FF9800"));
        HBox overdueLegend = createLegendItem("Terlambat", Color.web("#F44336"));
        
        legendItems.getChildren().addAll(todayLegend, completedLegend, submittedLegend, dueSoonLegend, overdueLegend);
        legendContainer.getChildren().addAll(legendTitle, legendItems);
        
        mainContent.getChildren().add(legendContainer);
    }
    
    private HBox createLegendItem(String text, Color color) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER);
        
        Label colorBox = new Label();
        colorBox.setPrefSize(16, 16);
        colorBox.setStyle("-fx-background-color: " + toHexString(color) + "; " +
                         "-fx-background-radius: 3;");
        
        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        textLabel.setTextFill(Color.web("#666666"));
        
        item.getChildren().addAll(colorBox, textLabel);
        return item;
    }
    
    private void updateCalendar() {
        // Update month year label
        monthYearLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + 
                              " " + currentMonth.getYear());
        
        // Clear previous calendar
        calendarGrid.getChildren().clear();
        
        // Get first day of month and number of days
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int daysInMonth = currentMonth.lengthOfMonth();
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0, Monday = 1, etc.
        
        // Create calendar cells
        int row = 0;
        int col = startDayOfWeek;
        
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            Button dayButton = createDayButton(date);
            
            calendarGrid.add(dayButton, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
    
    private Button createDayButton(LocalDate date) {
        Button dayButton = new Button(String.valueOf(date.getDayOfMonth()));
        dayButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        dayButton.setPrefSize(120, 80);
        dayButton.setAlignment(Pos.TOP_LEFT);
        dayButton.setPadding(new Insets(8));
        
        // Determine button style based on date
        boolean isToday = date.equals(LocalDate.now());
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        boolean hasTasks = tasksByDate.containsKey(date);
        
        String baseStyle = "-fx-background-radius: 8; -fx-cursor: hand; ";
        String textColor = "#333333";
        String backgroundColor = "white";
        String borderColor = "rgba(0, 0, 0, 0.1)";
        
        if (isToday) {
            backgroundColor = "#2D5016";
            textColor = "white";
            borderColor = "#2D5016";
        } else if (hasTasks) {
            backgroundColor = "#FFF3E0";
            borderColor = "#FF9800";
        } else if (isWeekend) {
            backgroundColor = "#F5F5F5";
            textColor = "#888888";
        }
        
        dayButton.setStyle(baseStyle + 
                          "-fx-background-color: " + backgroundColor + "; " +
                          "-fx-text-fill: " + textColor + "; " +
                          "-fx-border-color: " + borderColor + "; " +
                          "-fx-border-width: 1; " +
                          "-fx-border-radius: 8;");
        
        // Add task indicators
        if (hasTasks) {
            List<TaskInfo> tasks = tasksByDate.get(date);
            VBox taskIndicators = new VBox(2);
            taskIndicators.setAlignment(Pos.BOTTOM_LEFT);
            
            for (int i = 0; i < Math.min(tasks.size(), 3); i++) {
                TaskInfo task = tasks.get(i);
                Label indicator = new Label();
                indicator.setPrefSize(8, 8);
                indicator.setStyle("-fx-background-color: " + toHexString(task.getStatusColor()) + "; " +
                                 "-fx-background-radius: 4;");
                taskIndicators.getChildren().add(indicator);
            }
            
            if (tasks.size() > 3) {
                Label moreLabel = new Label("+" + (tasks.size() - 3));
                moreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                moreLabel.setTextFill(Color.web("#666666"));
                taskIndicators.getChildren().add(moreLabel);
            }
        }
        
        // Add hover effects
        final String finalBackgroundColor = backgroundColor;
        final String finalTextColor = textColor;
        final String finalBorderColor = borderColor;
        
        dayButton.setOnMouseEntered(e -> {
            if (!isToday) {
                dayButton.setStyle(baseStyle + 
                                  "-fx-background-color: " + (hasTasks ? "#FFE0B2" : "#F0F0F0") + "; " +
                                  "-fx-text-fill: " + finalTextColor + "; " +
                                  "-fx-border-color: " + finalBorderColor + "; " +
                                  "-fx-border-width: 2; " +
                                  "-fx-border-radius: 8;");
            }
            
            // Show tooltip with tasks
            if (hasTasks) {
                showTaskTooltip(dayButton, date);
            }
        });
        
        dayButton.setOnMouseExited(e -> {
            if (!isToday) {
                dayButton.setStyle(baseStyle + 
                                  "-fx-background-color: " + finalBackgroundColor + "; " +
                                  "-fx-text-fill: " + finalTextColor + "; " +
                                  "-fx-border-color: " + finalBorderColor + "; " +
                                  "-fx-border-width: 1; " +
                                  "-fx-border-radius: 8;");
            }
            
            // Hide tooltip
            if (currentTooltip != null) {
                currentTooltip.hide();
            }
        });
        
        // Add click handler
        dayButton.setOnAction(e -> {
            if (hasTasks) {
                showTaskDetailsPopup(date);
            }
        });
            return dayButton;
}

private void showTaskTooltip(Button dayButton, LocalDate date) {
    List<TaskInfo> tasks = tasksByDate.get(date);
    if (tasks == null || tasks.isEmpty()) {
        dayButton.setTooltip(null);
        return;
    }
    
    StringBuilder tooltipText = new StringBuilder();
    tooltipText.append("Tugas pada ").append(date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append(":\n\n");
    
    for (TaskInfo task : tasks) {
        tooltipText.append("• ").append(task.getTitle()).append("\n");
        tooltipText.append("  ").append(task.getClassName()).append(" - ").append(task.getTeacher()).append("\n\n");
    }
    
    Tooltip tooltip = new Tooltip(tooltipText.toString());
    tooltip.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); " +
                    "-fx-text-fill: white; " +
                    "-fx-padding: 10; " +
                    "-fx-background-radius: 5; " +
                    "-fx-font-size: 12;");
    
    // Mengatur delay dan durasi tooltip
    tooltip.setShowDelay(Duration.millis(100));
    tooltip.setHideDelay(Duration.millis(200));
    
    // Tooltip akan otomatis muncul di dekat mouse
    dayButton.setTooltip(tooltip);
    currentTooltip = tooltip;
}

private void showTaskDetailsPopup(LocalDate date) {
    List<TaskInfo> tasks = tasksByDate.get(date);
    if (tasks == null || tasks.isEmpty()) return;
    
    // Create popup stage
    Stage popup = new Stage();
    popup.initModality(Modality.APPLICATION_MODAL);
    popup.initStyle(StageStyle.TRANSPARENT);
    popup.setTitle("Detail Tugas");
    
    // Create blur effect for background
    root.setEffect(new GaussianBlur(10));
    
    // Create popup content
    VBox popupContent = new VBox(20);
    popupContent.setPadding(new Insets(30));
    popupContent.setAlignment(Pos.CENTER);
    popupContent.setStyle("-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: rgba(0, 0, 0, 0.1); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 20;");
    
    // Set ukuran berdasarkan persentase dari stage
    popupContent.setPrefWidth(600);
    popupContent.setMinWidth(400);
    popupContent.setMaxWidth(700);
    
    // Tinggi disesuaikan dengan jumlah task
    int contentHeight = Math.min(500, 200 + (tasks.size() * 80));
    popupContent.setPrefHeight(contentHeight);
    popupContent.setMaxHeight(contentHeight);
    
    // Improved drop shadow
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(25);
    dropShadow.setOffsetX(0);
    dropShadow.setOffsetY(5);
    dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
    dropShadow.setSpread(0.1);
    popupContent.setEffect(dropShadow);
    
    // Header
    HBox header = new HBox(15);
    header.setAlignment(Pos.CENTER_LEFT);
    
    Label dateLabel = new Label(date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    dateLabel.setTextFill(Color.web("#2D5016"));
    
    Button closeButton = new Button("✕");
    closeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    closeButton.setPrefSize(30, 30);
    closeButton.setStyle("-fx-background-color: #F44336; " +
                        "-fx-background-radius: 15; " +
                        "-fx-text-fill: white; " +
                        "-fx-cursor: hand;");
    closeButton.setOnAction(e -> {
        root.setEffect(null);
        popup.close();
    });
    
    header.getChildren().addAll(dateLabel, closeButton);
    HBox.setHgrow(dateLabel, Priority.ALWAYS);
    
    // Tasks list
    VBox tasksList = new VBox(15);
    
    for (TaskInfo task : tasks) {
        VBox taskCard = createTaskCard(task);
        tasksList.getChildren().add(taskCard);
    }
    
    ScrollPane scrollPane = new ScrollPane(tasksList);
    scrollPane.setFitToWidth(true);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setStyle("-fx-background-color: transparent; " +
                    "-fx-background: transparent;");
    
    popupContent.getChildren().addAll(header, scrollPane);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);
    
    // Create scene with transparent background
    StackPane popupRoot = new StackPane();
    popupRoot.setStyle("-fx-background-color: transparent;");
    
    // Calculate dynamic size based on content
    double sceneWidth = popupContent.getPrefWidth() + 100; // Extra space for padding
    double sceneHeight = popupContent.getPrefHeight() + 100;
    
    // Create overlay dengan ukuran dinamis
    Rectangle overlay = new Rectangle(0, 0);
    overlay.setFill(Color.rgb(0, 0, 0, 0.4));
    overlay.setArcWidth(0);
    overlay.setArcHeight(0);
    
    popupRoot.getChildren().addAll(overlay, popupContent);
    
    // Center the popup content
    StackPane.setAlignment(popupContent, Pos.CENTER);
    
    Scene popupScene = new Scene(popupRoot, sceneWidth, sceneHeight);
    popupScene.setFill(Color.TRANSPARENT);
    popup.setScene(popupScene);
        
    // Close popup when clicking outside
    popupRoot.setOnMouseClicked(e -> {
        if (e.getTarget() == popupRoot) {
            root.setEffect(null);
            popup.close();
        }
    });
    
    popup.setOnCloseRequest(e -> root.setEffect(null));
    popup.show();
}

private VBox createTaskCard(TaskInfo task) {
    VBox card = new VBox(10);
    card.setPadding(new Insets(15));
    card.setStyle("-fx-background-color: #F9F9F9; " +
                 "-fx-background-radius: 10; " +
                 "-fx-border-color: " + toHexString(task.getStatusColor()) + "; " +
                 "-fx-border-width: 2; " +
                 "-fx-border-radius: 10;");
    
    // Task header
    HBox taskHeader = new HBox(10);
    taskHeader.setAlignment(Pos.CENTER_LEFT);
    
    // Status indicator
    Label statusIndicator = new Label();
    statusIndicator.setPrefSize(12, 12);
    statusIndicator.setStyle("-fx-background-color: " + toHexString(task.getStatusColor()) + "; " +
                           "-fx-background-radius: 6;");
    
    // Task title
    Label titleLabel = new Label(task.getTitle());
    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    titleLabel.setTextFill(Color.web("#2D5016"));
    
    // Task type badge
    Label typeLabel = new Label(task.getType());
    typeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
    typeLabel.setTextFill(Color.web("#666666"));
    typeLabel.setPadding(new Insets(2, 8, 2, 8));
    typeLabel.setStyle("-fx-background-color: " + toHexString(task.getStatusColor().deriveColor(0, 1, 1, 0.2)) + "; " +
                      "-fx-background-radius: 10;");
    
    taskHeader.getChildren().addAll(statusIndicator, titleLabel, typeLabel);
    HBox.setHgrow(titleLabel, Priority.ALWAYS);
    
    // Task details
    VBox taskDetails = new VBox(5);
    
    Label classLabel = new Label("Kelas: " + task.getClassName());
    classLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    classLabel.setTextFill(Color.web("#4A7C26"));
    
    Label teacherLabel = new Label("Pengajar: " + task.getTeacher());
    teacherLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    teacherLabel.setTextFill(Color.web("#4A7C26"));
    
    Label deadlineLabel = new Label("Deadline: " + task.getDeadline().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    deadlineLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    deadlineLabel.setTextFill(Color.web("#4A7C26"));
    
    Label descriptionLabel = new Label("Deskripsi: " + task.getDescription());
    descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    descriptionLabel.setTextFill(Color.web("#666666"));
    descriptionLabel.setWrapText(true);
    
    taskDetails.getChildren().addAll(classLabel, teacherLabel, deadlineLabel, descriptionLabel);
    
    // Action buttons
    HBox actionButtons = new HBox(10);
    actionButtons.setAlignment(Pos.CENTER_RIGHT);
    
    Button viewButton = new Button("Lihat Detail");
    viewButton.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
    viewButton.setPadding(new Insets(5, 15, 5, 15));
    viewButton.setStyle("-fx-background-color: #2D5016; " +
                       "-fx-background-radius: 15; " +
                       "-fx-text-fill: white; " +
                       "-fx-cursor: hand;");
    
    Button submitButton = new Button("Kumpulkan");
    submitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
    submitButton.setPadding(new Insets(5, 15, 5, 15));
    submitButton.setStyle("-fx-background-color: " + toHexString(task.getStatusColor()) + "; " +
                         "-fx-background-radius: 15; " +
                         "-fx-text-fill: white; " +
                         "-fx-cursor: hand;");
    
    // Add action handler

    actionButtons.getChildren().addAll(viewButton);
    
    card.getChildren().addAll(taskHeader, taskDetails, actionButtons);
    
    // Add hover effect
    card.setOnMouseEntered(e -> {
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 10; " +
                     "-fx-border-color: " + toHexString(task.getStatusColor()) + "; " +
                     "-fx-border-width: 2; " +
                     "-fx-border-radius: 10; " +
                     "-fx-cursor: hand;");
    });
    
    card.setOnMouseExited(e -> {
        card.setStyle("-fx-background-color: #F9F9F9; " +
                     "-fx-background-radius: 10; " +
                     "-fx-border-color: " + toHexString(task.getStatusColor()) + "; " +
                     "-fx-border-width: 2; " +
                     "-fx-border-radius: 10;");
    });
    
    return card;
}

    public Tugas getSelectedTugas() {
        return this.selectedTugas;
    }

    public void setSelectedTugas(Tugas selectedTugas) {
        this.selectedTugas = selectedTugas;
    }

private String toHexString(Color color) {
    return String.format("#%02X%02X%02X",
                       (int)(color.getRed() * 255),
                       (int)(color.getGreen() * 255),
                       (int)(color.getBlue() * 255));
}

public void show() {
    stage.show();

}
}