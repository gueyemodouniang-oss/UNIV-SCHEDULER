package vue.admin;

import dao.impl.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.*;
import modele.Utilisateurs.*;
import modele.enums.Role;
import modele.enums.TypeSalle;

import java.util.List;

public class DashboardAdminVue extends Application {

    private Utilisateur adminConnecte;
    private BorderPane root;
    private Button btnActifMenu = null;
    private ImageView avatarView;
    private Label labelInitiales;

    private static final String BEIGE_FOND   = "#FAF8F5";
    private static final String BEIGE_CARD   = "#FFF5EE";
    private static final String GRIS_MENU    = "#E8762A";
    private static final String GRIS_FONCE   = "#C85E18";
    private static final String GRIS_HEADER  = "#BF5515";
    private static final String ACCENT       = "#F4874B";
    private static final String ACCENT_HOVER = "#D4621A";
    private static final String TEXTE_SOMBRE = "#2C2C2C";
    private static final String TEXTE_GRIS   = "#888888";

    // DAOs
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private SalleDAO       salleDAO       = new SalleDAO();
    private BatimentDAO    batimentDAO    = new BatimentDAO();
    private CoursDAO       coursDAO       = new CoursDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private MatiereDAO     matiereDAO     = new MatiereDAO();
    private GroupeDAO      groupeDAO      = new GroupeDAO();
    private SeanceDAO      seanceDAO      = new SeanceDAO();
    private EquipementDAO  equipementDAO  = new EquipementDAO();

    public DashboardAdminVue(Utilisateur admin) { this.adminConnecte = admin; }
    public DashboardAdminVue() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UNIV-SCHEDULER - Administration");
        root = new BorderPane();
        root.setTop(creerEnTete(primaryStage));
        root.setLeft(creerMenuLateral());
        root.setCenter(creerAccueil());
        root.setStyle("-fx-background-color: " + BEIGE_FOND + ";");
        Scene scene = new Scene(root, 1250, 780);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(660);
        primaryStage.show();
    }

    // ============================================================
    // EN-TÊTE
    // ============================================================
    private HBox creerEnTete(Stage stage) {
        HBox header = new HBox(15);
        header.setPadding(new Insets(0, 25, 0, 25));
        header.setPrefHeight(68);
        header.setStyle("-fx-background-color: " + GRIS_HEADER + ";");
        header.setAlignment(Pos.CENTER_LEFT);

        Label icone = new Label("🎓");
        icone.setFont(Font.font("Arial", 26));
        Label titre = new Label("UNIV-SCHEDULER");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        titre.setTextFill(Color.web("#F5F0E8"));
        Label badge = new Label("ADMIN");
        badge.setStyle("-fx-background-color: white; -fx-text-fill: " + GRIS_HEADER + ";" +
                "-fx-font-size: 10px; -fx-font-weight: bold;" +
                "-fx-padding: 3 10 3 10; -fx-background-radius: 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String prenom = adminConnecte != null ? adminConnecte.getPrenom() : "";
        String nom    = adminConnecte != null ? adminConnecte.getNom()    : "Admin";

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        Label labelNom = new Label(prenom + " " + nom);
        labelNom.setTextFill(Color.web("#F5F0E8"));
        labelNom.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label labelRole = new Label("Administrateur système");
        labelRole.setTextFill(Color.web("#BDBDBD"));
        labelRole.setFont(Font.font("Arial", 11));
        userInfo.getChildren().addAll(labelNom, labelRole);

        avatarView = new ImageView();
        avatarView.setFitWidth(42); avatarView.setFitHeight(42);
        avatarView.setPreserveRatio(false);
        avatarView.setVisible(false);

        String initiales = (prenom.isEmpty() ? "A" : String.valueOf(prenom.charAt(0))) +
                (nom.isEmpty()    ? "D" : String.valueOf(nom.charAt(0)));
        labelInitiales = new Label(initiales.toUpperCase());
        labelInitiales.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelInitiales.setTextFill(Color.WHITE);

        StackPane avatarPane = new StackPane(labelInitiales, avatarView);
        avatarPane.setPrefSize(44, 44);
        avatarPane.setStyle("-fx-background-color: " + ACCENT + ";" +
                "-fx-background-radius: 22; -fx-cursor: hand;");
        Tooltip.install(avatarPane, new Tooltip("Changer la photo de profil"));
        avatarPane.setOnMouseClicked(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une photo de profil");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png","*.jpg","*.jpeg","*.gif"));
            java.io.File f = fc.showOpenDialog(stage);
            if (f != null) {
                Image img = new Image(f.toURI().toString(), 44, 44, false, true);
                avatarView.setImage(img);
                avatarView.setClip(new Circle(21, 21, 21));
                avatarView.setVisible(true);
                labelInitiales.setVisible(false);
            }
        });
        avatarPane.setOnMouseEntered(e -> avatarPane.setStyle(
                "-fx-background-color: " + ACCENT_HOVER + "; -fx-background-radius: 22; -fx-cursor: hand;"));
        avatarPane.setOnMouseExited(e -> avatarPane.setStyle(
                "-fx-background-color: " + ACCENT + "; -fx-background-radius: 22; -fx-cursor: hand;"));

        Button btnDeco = creerBoutonDeconnexion();
        header.getChildren().addAll(icone, titre, badge, spacer, userInfo, avatarPane, btnDeco);
        return header;
    }

    private Button creerBoutonDeconnexion() {
        Button btn = new Button("⬅ Déconnexion");
        btn.setPrefHeight(36); btn.setPadding(new Insets(0, 16, 0, 16));
        String s1 = "-fx-background-color: white; -fx-text-fill: " + GRIS_HEADER + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                "-fx-border-color: white; -fx-border-radius: 7;" +
                "-fx-background-radius: 7; -fx-cursor: hand;";
        String s2 = "-fx-background-color: #E53935; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold;" +
                "-fx-border-color: #E53935; -fx-border-radius: 7;" +
                "-fx-background-radius: 7; -fx-cursor: hand;";
        btn.setStyle(s1);
        btn.setOnMouseEntered(e -> btn.setStyle(s2));
        btn.setOnMouseExited(e  -> btn.setStyle(s1));
        btn.setOnAction(e -> {
            ((Stage) btn.getScene().getWindow()).close();
            try { new vue.auth.LoginVue().start(new Stage()); }
            catch (Exception ex) { ex.printStackTrace(); }
        });
        return btn;
    }

    // ============================================================
    // MENU LATÉRAL
    // ============================================================
    private VBox creerMenuLateral() {
        VBox menu = new VBox(3);
        menu.setPrefWidth(235);
        menu.setPadding(new Insets(20, 10, 20, 10));
        menu.setStyle("-fx-background-color: " + GRIS_MENU + ";");

        Label lblPrincipal  = creerLabelSection("PRINCIPAL");
        Button btnAccueil   = creerBoutonMenu("🏠", "Accueil");
        Label lblGestion    = creerLabelSection("GESTION");
        Button btnUsers     = creerBoutonMenu("👥", "Utilisateurs");
        Button btnSalles    = creerBoutonMenu("🏫", "Salles");
        Button btnBatiments = creerBoutonMenu("🏢", "Bâtiments");
        Button btnEquipements = creerBoutonMenu("🔧", "Équipements");
        Label lblAnalyse    = creerLabelSection("ANALYSE");
        Button btnStats     = creerBoutonMenu("📊", "Statistiques");

        btnAccueil    .setOnAction(e -> naviguer(menu, btnAccueil,     creerAccueil()));
        btnUsers      .setOnAction(e -> naviguer(menu, btnUsers,       creerVueUtilisateurs()));
        btnSalles     .setOnAction(e -> naviguer(menu, btnSalles,      creerVueSalles()));
        btnBatiments  .setOnAction(e -> naviguer(menu, btnBatiments,   creerVueBatiments()));
        btnEquipements.setOnAction(e -> naviguer(menu, btnEquipements, creerVueEquipements()));
        btnStats      .setOnAction(e -> naviguer(menu, btnStats,       creerVueStatistiques()));

        activerBtn(menu, btnAccueil);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        Label version = new Label("UNIV-SCHEDULER v1.0");
        version.setTextFill(Color.web("#FFD9B3"));
        version.setFont(Font.font("Arial", 10));
        version.setPadding(new Insets(0, 0, 0, 12));

        menu.getChildren().addAll(
                lblPrincipal, btnAccueil,
                lblGestion, btnUsers, btnSalles, btnBatiments, btnEquipements,
                lblAnalyse, btnStats,
                spacer, version
        );
        return menu;
    }

    private Label creerLabelSection(String texte) {
        Label lbl = new Label(texte);
        lbl.setTextFill(Color.web("#FFD9B3"));
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setPadding(new Insets(14, 5, 6, 12));
        return lbl;
    }

    private void naviguer(VBox menu, Button btn, javafx.scene.Node vue) {
        activerBtn(menu, btn); root.setCenter(vue);
    }

    private void activerBtn(VBox menu, Button cible) {
        btnActifMenu = cible;
        menu.getChildren().forEach(n -> {
            if (n instanceof Button) ((Button) n).setStyle(styleBtnNormal());
        });
        cible.setStyle(styleBtnActif());
    }

    private String styleBtnNormal() {
        return "-fx-background-color: transparent; -fx-text-fill: white;" +
                "-fx-font-size: 13px; -fx-opacity: 0.85; -fx-alignment: CENTER_LEFT;" +
                "-fx-padding: 10 12 10 12; -fx-background-radius: 8; -fx-cursor: hand;";
    }

    private String styleBtnActif() {
        return "-fx-background-color: white; -fx-text-fill: " + GRIS_HEADER + ";" +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-opacity: 1.0;" +
                "-fx-alignment: CENTER_LEFT; -fx-padding: 10 12 10 12;" +
                "-fx-background-radius: 8; -fx-cursor: hand;";
    }

    private Button creerBoutonMenu(String ico, String texte) {
        Button btn = new Button(ico + "   " + texte);
        btn.setPrefWidth(215); btn.setPrefHeight(42);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(styleBtnNormal());
        btn.setOnMouseEntered(e -> { if (btn != btnActifMenu) btn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white;" +
                        "-fx-font-size: 13px; -fx-alignment: CENTER_LEFT;" +
                        "-fx-padding: 10 12 10 12; -fx-background-radius: 8; -fx-cursor: hand;");
        });
        btn.setOnMouseExited(e -> { if (btn != btnActifMenu) btn.setStyle(styleBtnNormal()); });
        return btn;
    }

    // ============================================================
    // ACCUEIL
    // ============================================================
    private ScrollPane creerAccueil() {
        VBox page = new VBox(28);
        page.setPadding(new Insets(35));
        page.setStyle("-fx-background-color: " + BEIGE_FOND + ";");

        Label titre = new Label("Bonjour, " +
                (adminConnecte != null ? adminConnecte.getPrenom() : "Admin") + " 👋");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titre.setTextFill(Color.web(TEXTE_SOMBRE));
        Label sousTitre = new Label("Voici l'état actuel de votre système universitaire");
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.web(TEXTE_GRIS));

        HBox cartes = new HBox(18);
        cartes.getChildren().addAll(
                creerCarteStats("👥", "Utilisateurs", utilisateurDAO.findAll().size(), "#5D4037", "#EFEBE9"),
                creerCarteStats("🏫", "Salles",        salleDAO.findAll().size(),        "#1B5E20", "#E8F5E9"),
                creerCarteStats("🏢", "Bâtiments",     batimentDAO.findAll().size(),     "#0D47A1", "#E3F2FD"),
                creerCarteStats("🔧", "Équipements",   equipementDAO.findAll().size(),   "#4A148C", "#F3E5F5")
        );

        Label lblAcces = new Label("Accès rapides");
        lblAcces.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblAcces.setTextFill(Color.web(TEXTE_SOMBRE));

        HBox acces = new HBox(12);
        acces.getChildren().addAll(
                creerBtnAccesRapide("👥 Utilisateurs",  () -> naviguerDepuisAccueil("Utilisateurs",  creerVueUtilisateurs())),
                creerBtnAccesRapide("🏫 Salles",         () -> naviguerDepuisAccueil("Salles",         creerVueSalles())),
                creerBtnAccesRapide("🏢 Bâtiments",      () -> naviguerDepuisAccueil("Bâtiments",      creerVueBatiments())),
                creerBtnAccesRapide("🔧 Équipements",    () -> naviguerDepuisAccueil("Équipements",    creerVueEquipements())),
                creerBtnAccesRapide("📊 Statistiques",   () -> naviguerDepuisAccueil("Statistiques",   creerVueStatistiques()))
        );

        page.getChildren().addAll(new VBox(4, titre, sousTitre), cartes, lblAcces, acces);
        return toScroll(page);
    }

    private void naviguerDepuisAccueil(String nomBouton, javafx.scene.Node vue) {
        VBox menu = (VBox) root.getLeft();
        menu.getChildren().stream()
                .filter(n -> n instanceof Button && ((Button)n).getText().contains(nomBouton))
                .findFirst().ifPresent(n -> naviguer(menu, (Button)n, vue));
    }

    // ============================================================
    // VUE UTILISATEURS
    // ============================================================
    private ScrollPane creerVueUtilisateurs() {
        VBox page = creerPage("👥 Gestion des Utilisateurs",
                "Ajoutez, modifiez et gérez les comptes");

        HBox barreRecherche = new HBox(10);
        barreRecherche.setAlignment(Pos.CENTER_LEFT);
        barreRecherche.setPadding(new Insets(12));
        barreRecherche.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");

        TextField champRecherche = creerChamp("🔍  Rechercher par nom, prénom ou email...");
        champRecherche.setPrefWidth(320);

        ComboBox<String> comboFiltreRole = new ComboBox<>();
        comboFiltreRole.getItems().addAll("Tous les rôles", "Admin",
                "Gestionnaire", "Enseignant", "Etudiant");
        comboFiltreRole.setValue("Tous les rôles");
        comboFiltreRole.setPrefHeight(38);
        comboFiltreRole.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");

        Label lblNbResultats = new Label();
        lblNbResultats.setFont(Font.font("Arial", 12));
        lblNbResultats.setTextFill(Color.web(TEXTE_GRIS));

        Button btnReset = creerBoutonAction("✕ Effacer", "#757575", "#616161");
        barreRecherche.getChildren().addAll(
                champRecherche, new Label("Rôle :"), comboFiltreRole, btnReset, lblNbResultats);

        TableView<Utilisateur> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(320);

        TableColumn<Utilisateur, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); colId.setPrefWidth(55);
        TableColumn<Utilisateur, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TableColumn<Utilisateur, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        TableColumn<Utilisateur, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Utilisateur, String> colRole = new TableColumn<>("Rôle");
        colRole.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getRole().toString()));
        colRole.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) { setText(null); setStyle(""); return; }
                setText(r);
                switch (r) {
                    case "Admin"        -> setStyle("-fx-text-fill:#B71C1C;-fx-font-weight:bold;");
                    case "Gestionnaire" -> setStyle("-fx-text-fill:#1B5E20;-fx-font-weight:bold;");
                    case "Enseignant"   -> setStyle("-fx-text-fill:#0D47A1;-fx-font-weight:bold;");
                    case "Etudiant"     -> setStyle("-fx-text-fill:#4A148C;-fx-font-weight:bold;");
                    default             -> setStyle("");
                }
            }
        });

        tableau.getColumns().addAll(colId, colNom, colPrenom, colEmail, colRole);
        ObservableList<Utilisateur> tousUsers =
                FXCollections.observableArrayList(utilisateurDAO.findAll());
        tableau.setItems(tousUsers);
        lblNbResultats.setText(tousUsers.size() + " utilisateur(s)");

        Runnable appliquerFiltres = () -> {
            String txt  = champRecherche.getText().toLowerCase().trim();
            String role = comboFiltreRole.getValue();
            ObservableList<Utilisateur> filtre = tousUsers.filtered(u -> {
                boolean matchTexte = txt.isEmpty() ||
                        u.getNom().toLowerCase().contains(txt) ||
                        u.getPrenom().toLowerCase().contains(txt) ||
                        u.getEmail().toLowerCase().contains(txt);
                boolean matchRole = role.equals("Tous les rôles") ||
                        u.getRole().toString().equals(role);
                return matchTexte && matchRole;
            });
            tableau.setItems(filtre);
            lblNbResultats.setText(filtre.size() + " utilisateur(s)");
        };

        champRecherche.textProperty().addListener((o, v, n) -> appliquerFiltres.run());
        comboFiltreRole.setOnAction(e -> appliquerFiltres.run());
        btnReset.setOnAction(e -> {
            champRecherche.clear(); comboFiltreRole.setValue("Tous les rôles");
            tableau.setItems(tousUsers);
            lblNbResultats.setText(tousUsers.size() + " utilisateur(s)");
        });

        VBox formCard = creerCarteFormulaire("➕ Ajouter / ✏ Modifier un utilisateur");

        TextField fNom    = creerChamp("Nom *");
        TextField fPrenom = creerChamp("Prénom");
        TextField fEmail  = creerChamp("Email *");
        TextField fMdp    = creerChamp("Mot de passe *");

        ComboBox<Role> comboRole = new ComboBox<>();
        comboRole.getItems().addAll(Role.values());
        comboRole.setPromptText("Rôle *");
        comboRole.setPrefHeight(38);
        comboRole.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");

        TextField fSpecialite  = creerChamp("Spécialité");
        TextField fDepartement = creerChamp("Département");
        TextField fMatricule   = creerChamp("Matricule");
        TextField fFiliere     = creerChamp("Filière");
        TextField fNiveau      = creerChamp("Niveau");
        TextField fService     = creerChamp("Service");

        HBox champsSpecifiques = new HBox(12);
        champsSpecifiques.setVisible(false); champsSpecifiques.setManaged(false);

        comboRole.setOnAction(e -> {
            champsSpecifiques.getChildren().clear();
            Role r = comboRole.getValue();
            if (r == null) {
                champsSpecifiques.setVisible(false);
                champsSpecifiques.setManaged(false); return;
            }
            champsSpecifiques.setVisible(true); champsSpecifiques.setManaged(true);
            switch (r) {
                case Enseignant   -> champsSpecifiques.getChildren().addAll(fSpecialite, fDepartement);
                case Etudiant     -> champsSpecifiques.getChildren().addAll(fMatricule, fFiliere, fNiveau);
                case Gestionnaire -> champsSpecifiques.getChildren().add(fService);
                default -> { champsSpecifiques.setVisible(false); champsSpecifiques.setManaged(false); }
            }
        });

        HBox ligneBase = new HBox(12, fNom, fPrenom, fEmail, fMdp, comboRole);
        HBox.setHgrow(fNom, Priority.ALWAYS); HBox.setHgrow(fPrenom, Priority.ALWAYS);
        HBox.setHgrow(fEmail, Priority.ALWAYS); HBox.setHgrow(fMdp, Priority.ALWAYS);
        HBox.setHgrow(fSpecialite, Priority.ALWAYS); HBox.setHgrow(fDepartement, Priority.ALWAYS);
        HBox.setHgrow(fMatricule, Priority.ALWAYS); HBox.setHgrow(fFiliere, Priority.ALWAYS);
        HBox.setHgrow(fNiveau, Priority.ALWAYS); HBox.setHgrow(fService, Priority.ALWAYS);

        tableau.setOnMouseClicked(e -> {
            Utilisateur sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            fNom.setText(sel.getNom()); fPrenom.setText(sel.getPrenom());
            fEmail.setText(sel.getEmail()); fMdp.setText(sel.getMotDePasse());
            comboRole.setValue(sel.getRole());
            champsSpecifiques.getChildren().clear();
            champsSpecifiques.setVisible(true); champsSpecifiques.setManaged(true);
            switch (sel.getRole()) {
                case Enseignant -> {
                    champsSpecifiques.getChildren().addAll(fSpecialite, fDepartement);
                    if (sel instanceof Enseignant ens) {
                        fSpecialite.setText(ens.getSpecialite()   != null ? ens.getSpecialite()   : "");
                        fDepartement.setText(ens.getDepartement() != null ? ens.getDepartement()  : "");
                    }
                }
                case Etudiant -> {
                    champsSpecifiques.getChildren().addAll(fMatricule, fFiliere, fNiveau);
                    if (sel instanceof Etudiant etu) {
                        fMatricule.setText(etu.getMatricule() != null ? etu.getMatricule() : "");
                        fFiliere.setText(etu.getFiliere()     != null ? etu.getFiliere()   : "");
                        fNiveau.setText(etu.getNiveau()       != null ? etu.getNiveau()    : "");
                    }
                }
                case Gestionnaire -> {
                    champsSpecifiques.getChildren().add(fService);
                    if (sel instanceof Gestionnaire gest)
                        fService.setText(gest.getService() != null ? gest.getService() : "");
                }
                default -> { champsSpecifiques.setVisible(false); champsSpecifiques.setManaged(false); }
            }
        });

        Label msgAjout = new Label(); msgAjout.setFont(Font.font("Arial", 12));

        Button btnAjouter    = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnModifier   = creerBoutonAction("✏ Modifier",   "#1565C0", "#0D47A1");
        Button btnProfil     = creerBoutonAction("👤 Profil",     ACCENT,    ACCENT_HOVER);
        Button btnSupprimer  = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", GRIS_MENU, GRIS_FONCE);

        btnAjouter.setOnAction(e -> {
            if (fNom.getText().isEmpty() || fEmail.getText().isEmpty() ||
                    fMdp.getText().isEmpty() || comboRole.getValue() == null) {
                msgAjout.setText("⚠ Remplissez les champs obligatoires (*).");
                msgAjout.setTextFill(Color.ORANGE); return;
            }
            Utilisateur u = null;
            switch (comboRole.getValue()) {
                case Admin -> u = new Admin(0, fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Admin);
                case Enseignant -> u = new Enseignant(0, fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Enseignant,
                        fSpecialite.getText(), fDepartement.getText());
                case Etudiant -> u = new Etudiant(0, fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Etudiant,
                        fMatricule.getText(), fFiliere.getText(), fNiveau.getText());
                case Gestionnaire -> u = new Gestionnaire(0, fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Gestionnaire, fService.getText());
            }
            if (u != null) {
                utilisateurDAO.ajouter(u);
                tousUsers.clear(); tousUsers.addAll(utilisateurDAO.findAll());
                appliquerFiltres.run();
                msgAjout.setText("✅ Utilisateur ajouté !"); msgAjout.setTextFill(Color.web("#2E7D32"));
                viderFormulaire(fNom, fPrenom, fEmail, fMdp, fSpecialite,
                        fDepartement, fMatricule, fFiliere, fNiveau, fService);
                comboRole.setValue(null);
                champsSpecifiques.setVisible(false); champsSpecifiques.setManaged(false);
                tableau.getSelectionModel().clearSelection();
            }
        });

        btnModifier.setOnAction(e -> {
            Utilisateur sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msgAjout.setText("⚠ Sélectionnez un utilisateur.");
                msgAjout.setTextFill(Color.ORANGE); return;
            }
            if (fNom.getText().isEmpty() || fEmail.getText().isEmpty() ||
                    fMdp.getText().isEmpty() || comboRole.getValue() == null) {
                msgAjout.setText("⚠ Remplissez les champs obligatoires (*).");
                msgAjout.setTextFill(Color.ORANGE); return;
            }
            Utilisateur u = null;
            switch (comboRole.getValue()) {
                case Admin -> u = new Admin(sel.getId(), fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Admin);
                case Enseignant -> u = new Enseignant(sel.getId(), fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Enseignant,
                        fSpecialite.getText(), fDepartement.getText());
                case Etudiant -> u = new Etudiant(sel.getId(), fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Etudiant,
                        fMatricule.getText(), fFiliere.getText(), fNiveau.getText());
                case Gestionnaire -> u = new Gestionnaire(sel.getId(), fPrenom.getText(), fNom.getText(),
                        fEmail.getText(), fMdp.getText(), Role.Gestionnaire, fService.getText());
            }
            if (u != null) {
                utilisateurDAO.modifier(u);
                tousUsers.clear(); tousUsers.addAll(utilisateurDAO.findAll());
                appliquerFiltres.run();
                msgAjout.setText("✅ Utilisateur modifié !"); msgAjout.setTextFill(Color.web("#2E7D32"));
                viderFormulaire(fNom, fPrenom, fEmail, fMdp, fSpecialite,
                        fDepartement, fMatricule, fFiliere, fNiveau, fService);
                comboRole.setValue(null);
                champsSpecifiques.setVisible(false); champsSpecifiques.setManaged(false);
                tableau.getSelectionModel().clearSelection();
            }
        });

        btnProfil.setOnAction(e -> {
            Utilisateur sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) afficherProfilUtilisateur(sel);
            else afficherAlerte("⚠ Sélectionnez un utilisateur.");
        });

        btnSupprimer.setOnAction(e -> {
            Utilisateur sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez un utilisateur."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer " + sel.getNom() + " " + sel.getPrenom() + " ?");
            confirm.setContentText("Cette action est irréversible.");
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    utilisateurDAO.supprimer(sel.getId());
                    tousUsers.remove(sel); appliquerFiltres.run();
                    msgAjout.setText("✅ Utilisateur supprimé.");
                    msgAjout.setTextFill(Color.web("#2E7D32"));
                }
            });
        });

        btnActualiser.setOnAction(e -> {
            tousUsers.clear(); tousUsers.addAll(utilisateurDAO.findAll());
            appliquerFiltres.run();
        });

        formCard.getChildren().addAll(ligneBase, champsSpecifiques,
                creerToolbar(btnAjouter, btnModifier, btnProfil, btnSupprimer, btnActualiser),
                msgAjout);

        page.getChildren().addAll(barreRecherche, tableau, formCard);
        return toScroll(page);
    }

    private void viderFormulaire(TextField... champs) {
        for (TextField f : champs) f.clear();
    }

    private void afficherProfilUtilisateur(Utilisateur u) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Profil — " + u.getNom() + " " + u.getPrenom());
        dialog.setResizable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: " + BEIGE_FOND + ";");

        String ini = String.valueOf(u.getPrenom().charAt(0)) +
                String.valueOf(u.getNom().charAt(0));
        Label lblIni = new Label(ini.toUpperCase());
        lblIni.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblIni.setTextFill(Color.WHITE);
        StackPane avatar = new StackPane(lblIni);
        avatar.setPrefSize(72, 72);
        avatar.setStyle("-fx-background-color: " + ACCENT + "; -fx-background-radius: 36;");

        Label lblNom = new Label(u.getPrenom() + " " + u.getNom());
        lblNom.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblNom.setTextFill(Color.web(TEXTE_SOMBRE));
        Label lblRole = new Label(u.getRole().toString());
        lblRole.setFont(Font.font("Arial", 13));
        lblRole.setTextFill(Color.web(ACCENT));

        VBox details = new VBox(8);
        details.getChildren().addAll(
                creerLigneDetail("📧 Email", u.getEmail()),
                creerLigneDetail("🔑 Rôle",  u.getRole().toString()),
                creerLigneDetail("🆔 ID",    String.valueOf(u.getId()))
        );
        if (u instanceof Enseignant ens) {
            details.getChildren().addAll(
                    creerLigneDetail("🎓 Spécialité",  ens.getSpecialite()  != null ? ens.getSpecialite()  : "-"),
                    creerLigneDetail("🏢 Département", ens.getDepartement() != null ? ens.getDepartement() : "-")
            );
        } else if (u instanceof Etudiant etu) {
            details.getChildren().addAll(
                    creerLigneDetail("🆔 Matricule", etu.getMatricule() != null ? etu.getMatricule() : "-"),
                    creerLigneDetail("📚 Filière",   etu.getFiliere()   != null ? etu.getFiliere()   : "-"),
                    creerLigneDetail("📊 Niveau",    etu.getNiveau()    != null ? etu.getNiveau()    : "-")
            );
        } else if (u instanceof Gestionnaire gest) {
            details.getChildren().add(
                    creerLigneDetail("🏢 Service", gest.getService() != null ? gest.getService() : "-"));
        }

        Button btnFermer = creerBoutonAction("Fermer", GRIS_MENU, GRIS_FONCE);
        btnFermer.setOnAction(e -> dialog.close());

        content.getChildren().addAll(avatar, lblNom, lblRole, new Separator(), details, btnFermer);
        dialog.setScene(new Scene(content, 380, 420));
        dialog.show();
    }

    private HBox creerLigneDetail(String cle, String valeur) {
        HBox ligne = new HBox(10); ligne.setAlignment(Pos.CENTER_LEFT);
        Label lblCle = new Label(cle + " :");
        lblCle.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblCle.setTextFill(Color.web(TEXTE_GRIS)); lblCle.setPrefWidth(140);
        Label lblVal = new Label(valeur);
        lblVal.setFont(Font.font("Arial", 13)); lblVal.setTextFill(Color.web(TEXTE_SOMBRE));
        ligne.getChildren().addAll(lblCle, lblVal);
        return ligne;
    }

    // ============================================================
    // VUE SALLES
    // ============================================================
    private ScrollPane creerVueSalles() {
        VBox page = creerPage("🏫 Gestion des Salles",
                "Ajoutez, modifiez et gérez les salles");

        HBox filtres = new HBox(12);
        filtres.setAlignment(Pos.CENTER_LEFT);
        filtres.setPadding(new Insets(12));
        filtres.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");

        TextField champRecherche = creerChamp("🔍  Rechercher par numéro ou bâtiment...");
        champRecherche.setPrefWidth(280);
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Tous", "TD", "TP", "AMPHI");
        comboType.setValue("Tous"); comboType.setPrefHeight(38);
        comboType.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        Label lblNbSalles = new Label();
        lblNbSalles.setFont(Font.font("Arial", 12));
        lblNbSalles.setTextFill(Color.web(TEXTE_GRIS));
        Button btnReset = creerBoutonAction("✕", "#757575", "#616161");
        filtres.getChildren().addAll(
                champRecherche, new Label("Type :"), comboType, btnReset, lblNbSalles);

        TableView<Salle> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(280);

        TableColumn<Salle, Integer> colId  = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idSalle")); colId.setPrefWidth(55);
        TableColumn<Salle, String>  colNum = new TableColumn<>("Numéro");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numeroSalle"));
        TableColumn<Salle, Integer> colCap = new TableColumn<>("Capacité");
        colCap.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        TableColumn<Salle, String>  colTyp = new TableColumn<>("Type");
        colTyp.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Salle, String>  colBat = new TableColumn<>("Bâtiment");
        colBat.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getBatiment().getNomBatiment()));
        TableColumn<Salle, String> colEq = new TableColumn<>("Équipements");
        colEq.setCellValueFactory(d -> {
            List<Equipement> eqs = equipementDAO.trouverParSalle(
                    d.getValue().getIdSalle());
            String noms = eqs.stream().map(Equipement::getNomEquipement)
                    .reduce((a, b) -> a + ", " + b).orElse("—");
            return new SimpleStringProperty(noms);
        });

        tableau.getColumns().addAll(colId, colNum, colCap, colTyp, colBat, colEq);
        ObservableList<Salle> touteSalles =
                FXCollections.observableArrayList(salleDAO.findAll());
        tableau.setItems(touteSalles);
        lblNbSalles.setText(touteSalles.size() + " salle(s)");

        Runnable appliquerFiltres = () -> {
            String txt  = champRecherche.getText().toLowerCase();
            String type = comboType.getValue();
            ObservableList<Salle> f = touteSalles.filtered(s ->
                    (txt.isEmpty() ||
                            s.getNumeroSalle().toLowerCase().contains(txt) ||
                            s.getBatiment().getNomBatiment().toLowerCase().contains(txt)) &&
                            (type.equals("Tous") || s.getType().name().equals(type)));
            tableau.setItems(f);
            lblNbSalles.setText(f.size() + " salle(s)");
        };
        champRecherche.textProperty().addListener((o, v, n) -> appliquerFiltres.run());
        comboType.setOnAction(e -> appliquerFiltres.run());
        btnReset.setOnAction(e -> {
            champRecherche.clear(); comboType.setValue("Tous");
            tableau.setItems(touteSalles);
            lblNbSalles.setText(touteSalles.size() + " salle(s)");
        });

        VBox formAjout = creerCarteFormulaire("➕ Ajouter / ✏ Modifier une salle");
        TextField fNumero   = creerChamp("Numéro *");
        TextField fCapacite = creerChamp("Capacité *");
        ComboBox<TypeSalle> comboTypeSalle = new ComboBox<>();
        comboTypeSalle.getItems().addAll(TypeSalle.values());
        comboTypeSalle.setPromptText("Type *"); comboTypeSalle.setPrefHeight(38);
        comboTypeSalle.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        ComboBox<Batiment> comboBat = new ComboBox<>();
        comboBat.getItems().addAll(batimentDAO.findAll());
        comboBat.setPromptText("Bâtiment *"); comboBat.setPrefHeight(38);
        comboBat.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        HBox.setHgrow(fNumero, Priority.ALWAYS); HBox.setHgrow(fCapacite, Priority.ALWAYS);
        HBox ligneAjout = new HBox(12, fNumero, fCapacite, comboTypeSalle, comboBat);

        Label msgSalle = new Label(); msgSalle.setFont(Font.font("Arial", 12));

        Button btnAjouter    = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnModifier   = creerBoutonAction("✏ Modifier",   "#1565C0", "#0D47A1");
        Button btnSupprimer  = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", GRIS_MENU, GRIS_FONCE);

        tableau.setOnMouseClicked(e -> {
            Salle sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                fNumero.setText(sel.getNumeroSalle());
                fCapacite.setText(String.valueOf(sel.getCapacite()));
                comboTypeSalle.setValue(sel.getType());
                comboBat.getItems().stream()
                        .filter(b -> b.getIdBatiment() == sel.getBatiment().getIdBatiment())
                        .findFirst().ifPresent(comboBat::setValue);
                msgSalle.setText("ℹ Salle sélectionnée.");
                msgSalle.setTextFill(Color.web("#1565C0"));
            }
        });

        btnAjouter.setOnAction(e -> {
            if (fNumero.getText().isEmpty() || fCapacite.getText().isEmpty() ||
                    comboTypeSalle.getValue() == null || comboBat.getValue() == null) {
                msgSalle.setText("⚠ Remplissez tous les champs.");
                msgSalle.setTextFill(Color.ORANGE); return;
            }
            try {
                int cap = Integer.parseInt(fCapacite.getText());
                salleDAO.ajouter(new Salle(0, fNumero.getText(), cap,
                        comboTypeSalle.getValue(), comboBat.getValue()));
                touteSalles.clear(); touteSalles.addAll(salleDAO.findAll());
                appliquerFiltres.run();
                msgSalle.setText("✅ Salle ajoutée !"); msgSalle.setTextFill(Color.web("#2E7D32"));
                fNumero.clear(); fCapacite.clear();
                comboTypeSalle.setValue(null); comboBat.setValue(null);
            } catch (NumberFormatException ex) {
                msgSalle.setText("⚠ Capacité invalide."); msgSalle.setTextFill(Color.ORANGE);
            }
        });

        btnModifier.setOnAction(e -> {
            Salle sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msgSalle.setText("⚠ Sélectionnez une salle.");
                msgSalle.setTextFill(Color.ORANGE); return;
            }
            try {
                int cap = Integer.parseInt(fCapacite.getText());
                salleDAO.modifier(new Salle(sel.getIdSalle(), fNumero.getText(),
                        cap, comboTypeSalle.getValue(), comboBat.getValue()));
                touteSalles.clear(); touteSalles.addAll(salleDAO.findAll());
                appliquerFiltres.run();
                msgSalle.setText("✅ Salle modifiée !"); msgSalle.setTextFill(Color.web("#2E7D32"));
                fNumero.clear(); fCapacite.clear();
                comboTypeSalle.setValue(null); comboBat.setValue(null);
                tableau.getSelectionModel().clearSelection();
            } catch (NumberFormatException ex) {
                msgSalle.setText("⚠ Capacité invalide."); msgSalle.setTextFill(Color.ORANGE);
            }
        });

        btnSupprimer.setOnAction(e -> {
            Salle sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez une salle."); return; }
            long nbResas   = reservationDAO.findAll().stream()
                    .filter(r -> r.getSalle().getIdSalle() == sel.getIdSalle()).count();
            long nbSeances = seanceDAO.findAll().stream()
                    .filter(s -> s.getSalle().getIdSalle() == sel.getIdSalle()).count();
            String avert = (nbResas > 0 || nbSeances > 0) ?
                    "\n⚠ " + nbResas + " réservation(s) et " + nbSeances +
                            " séance(s) seront supprimées." : "";
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Supprimer la salle " + sel.getNumeroSalle() + " ?");
            confirm.setContentText("Cette action est irréversible." + avert);
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    salleDAO.supprimer(sel.getIdSalle());
                    touteSalles.clear(); touteSalles.addAll(salleDAO.findAll());
                    appliquerFiltres.run();
                    msgSalle.setText("✅ Salle supprimée.");
                    msgSalle.setTextFill(Color.web("#2E7D32"));
                }
            });
        });

        btnActualiser.setOnAction(e -> {
            touteSalles.clear(); touteSalles.addAll(salleDAO.findAll());
            appliquerFiltres.run();
        });

        formAjout.getChildren().addAll(ligneAjout,
                creerToolbar(btnAjouter, btnModifier, btnSupprimer, btnActualiser), msgSalle);
        page.getChildren().addAll(filtres, tableau, formAjout);
        return toScroll(page);
    }

    // ============================================================
    // VUE BÂTIMENTS
    // ============================================================
    private ScrollPane creerVueBatiments() {
        VBox page = creerPage("🏢 Gestion des Bâtiments",
                "Ajoutez, modifiez et gérez les bâtiments");

        HBox barreRecherche = new HBox(10);
        barreRecherche.setAlignment(Pos.CENTER_LEFT);
        barreRecherche.setPadding(new Insets(12));
        barreRecherche.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        TextField champRecherche = creerChamp("🔍  Rechercher un bâtiment...");
        champRecherche.setPrefWidth(300);
        Label lblNbBat = new Label();
        lblNbBat.setFont(Font.font("Arial", 12));
        lblNbBat.setTextFill(Color.web(TEXTE_GRIS));
        Button btnReset = creerBoutonAction("✕ Effacer", "#757575", "#616161");
        barreRecherche.getChildren().addAll(champRecherche, btnReset, lblNbBat);

        TableView<Batiment> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(300);

        TableColumn<Batiment, Integer> colId  = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idBatiment")); colId.setPrefWidth(55);
        TableColumn<Batiment, String>  colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomBatiment"));
        TableColumn<Batiment, String>  colLoc = new TableColumn<>("Localisation");
        colLoc.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        tableau.getColumns().addAll(colId, colNom, colLoc);
        ObservableList<Batiment> tousB =
                FXCollections.observableArrayList(batimentDAO.findAll());
        tableau.setItems(tousB);
        lblNbBat.setText(tousB.size() + " bâtiment(s)");

        champRecherche.textProperty().addListener((o, v, n) -> {
            if (n.isEmpty()) { tableau.setItems(tousB); return; }
            String f = n.toLowerCase();
            tableau.setItems(tousB.filtered(b ->
                    b.getNomBatiment().toLowerCase().contains(f) ||
                            b.getLocalisation().toLowerCase().contains(f)));
        });
        btnReset.setOnAction(e -> { champRecherche.clear(); tableau.setItems(tousB); });

        VBox formCard = creerCarteFormulaire("➕ Ajouter / ✏ Modifier un bâtiment");
        TextField fNom = creerChamp("Nom *"); TextField fLoc = creerChamp("Localisation *");
        HBox.setHgrow(fNom, Priority.ALWAYS); HBox.setHgrow(fLoc, Priority.ALWAYS);
        HBox ligne = new HBox(12, fNom, fLoc);
        Label msg = new Label(); msg.setFont(Font.font("Arial", 12));

        Button btnAjouter    = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnModifier   = creerBoutonAction("✏ Modifier",   "#1565C0", "#0D47A1");
        Button btnSupprimer  = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", GRIS_MENU, GRIS_FONCE);

        tableau.setOnMouseClicked(e -> {
            Batiment sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                fNom.setText(sel.getNomBatiment()); fLoc.setText(sel.getLocalisation());
                msg.setText("ℹ Bâtiment sélectionné."); msg.setTextFill(Color.web("#1565C0"));
            }
        });

        btnAjouter.setOnAction(e -> {
            if (fNom.getText().isEmpty() || fLoc.getText().isEmpty()) {
                msg.setText("⚠ Remplissez tous les champs."); msg.setTextFill(Color.ORANGE); return;
            }
            batimentDAO.ajouter(new Batiment(0, fNom.getText(), fLoc.getText()));
            tousB.clear(); tousB.addAll(batimentDAO.findAll());
            msg.setText("✅ Bâtiment ajouté !"); msg.setTextFill(Color.web("#2E7D32"));
            fNom.clear(); fLoc.clear();
        });

        btnModifier.setOnAction(e -> {
            Batiment sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msg.setText("⚠ Sélectionnez un bâtiment."); msg.setTextFill(Color.ORANGE); return;
            }
            batimentDAO.modifier(new Batiment(sel.getIdBatiment(), fNom.getText(), fLoc.getText()));
            tousB.clear(); tousB.addAll(batimentDAO.findAll());
            msg.setText("✅ Bâtiment modifié !"); msg.setTextFill(Color.web("#2E7D32"));
            fNom.clear(); fLoc.clear(); tableau.getSelectionModel().clearSelection();
        });

        btnSupprimer.setOnAction(e -> {
            Batiment sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez un bâtiment."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Supprimer " + sel.getNomBatiment() + " ?");
            confirm.setContentText("⚠ Toutes les salles seront supprimées.");
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    batimentDAO.supprimer(sel.getIdBatiment());
                    tousB.clear(); tousB.addAll(batimentDAO.findAll());
                    msg.setText("✅ Bâtiment supprimé."); msg.setTextFill(Color.web("#2E7D32"));
                    fNom.clear(); fLoc.clear();
                }
            });
        });

        btnActualiser.setOnAction(e -> {
            tousB.clear(); tousB.addAll(batimentDAO.findAll());
            lblNbBat.setText(tousB.size() + " bâtiment(s)");
        });

        formCard.getChildren().addAll(ligne,
                creerToolbar(btnAjouter, btnModifier, btnSupprimer, btnActualiser), msg);
        page.getChildren().addAll(barreRecherche, tableau, formCard);
        return toScroll(page);
    }

    // ============================================================
    // VUE ÉQUIPEMENTS
    // ============================================================
    private ScrollPane creerVueEquipements() {
        VBox page = creerPage("🔧 Gestion des Équipements",
                "Définissez les équipements et assignez-les aux salles");

        // ── SECTION 1 : Liste des équipements ──
        VBox cardEquip = creerCarteFormulaire("📋 Types d'équipements");

        TableView<Equipement> tableauEq = new TableView<>();
        styliserTableau(tableauEq); tableauEq.setPrefHeight(200);

        TableColumn<Equipement, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipement"));
        colId.setPrefWidth(55);
        TableColumn<Equipement, String> colNom =
                new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomEquipement"));
        TableColumn<Equipement, String> colDesc =
                new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableauEq.getColumns().addAll(colId, colNom, colDesc);
        ObservableList<Equipement> tousEq =
                FXCollections.observableArrayList(equipementDAO.findAll());
        tableauEq.setItems(tousEq);

        TextField fNomEq  = creerChamp("Nom de l'équipement *");
        TextField fDescEq = creerChamp("Description");
        HBox.setHgrow(fNomEq,  Priority.ALWAYS);
        HBox.setHgrow(fDescEq, Priority.ALWAYS);
        HBox ligneEq = new HBox(12, fNomEq, fDescEq);

        Label msgEq = new Label(); msgEq.setFont(Font.font("Arial", 12));

        Button btnAjouterEq   = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnModifierEq  = creerBoutonAction("✏ Modifier",   "#1565C0", "#0D47A1");
        Button btnSupprimerEq = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActuEq      = creerBoutonAction("🔄 Actualiser", GRIS_MENU, GRIS_FONCE);

        tableauEq.setOnMouseClicked(e -> {
            Equipement sel = tableauEq.getSelectionModel().getSelectedItem();
            if (sel != null) {
                fNomEq.setText(sel.getNomEquipement());
                fDescEq.setText(sel.getDescription() != null ? sel.getDescription() : "");
                msgEq.setText("ℹ Équipement sélectionné.");
                msgEq.setTextFill(Color.web("#1565C0"));
            }
        });

        btnAjouterEq.setOnAction(e -> {
            if (fNomEq.getText().isEmpty()) {
                msgEq.setText("⚠ Saisissez un nom."); msgEq.setTextFill(Color.ORANGE); return;
            }
            equipementDAO.ajouter(new Equipement(0, fNomEq.getText(), fDescEq.getText()));
            tousEq.clear(); tousEq.addAll(equipementDAO.findAll());
            msgEq.setText("✅ Équipement ajouté !"); msgEq.setTextFill(Color.web("#2E7D32"));
            fNomEq.clear(); fDescEq.clear();
        });

        btnModifierEq.setOnAction(e -> {
            Equipement sel = tableauEq.getSelectionModel().getSelectedItem();
            if (sel == null) {
                msgEq.setText("⚠ Sélectionnez un équipement.");
                msgEq.setTextFill(Color.ORANGE); return;
            }
            equipementDAO.modifier(new Equipement(
                    sel.getIdEquipement(), fNomEq.getText(), fDescEq.getText()));
            tousEq.clear(); tousEq.addAll(equipementDAO.findAll());
            msgEq.setText("✅ Équipement modifié !"); msgEq.setTextFill(Color.web("#2E7D32"));
            fNomEq.clear(); fDescEq.clear();
            tableauEq.getSelectionModel().clearSelection();
        });

        btnSupprimerEq.setOnAction(e -> {
            Equipement sel = tableauEq.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez un équipement."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Supprimer " + sel.getNomEquipement() + " ?");
            confirm.setContentText("Il sera retiré de toutes les salles.");
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    equipementDAO.supprimer(sel.getIdEquipement());
                    tousEq.clear(); tousEq.addAll(equipementDAO.findAll());
                    msgEq.setText("✅ Équipement supprimé."); msgEq.setTextFill(Color.web("#2E7D32"));
                    fNomEq.clear(); fDescEq.clear();
                }
            });
        });

        btnActuEq.setOnAction(e -> { tousEq.clear(); tousEq.addAll(equipementDAO.findAll()); });

        cardEquip.getChildren().addAll(tableauEq, ligneEq,
                creerToolbar(btnAjouterEq, btnModifierEq, btnSupprimerEq, btnActuEq), msgEq);

        // ── SECTION 2 : Équipements par salle ──
        VBox cardSalle = creerCarteFormulaire("🏫 Assigner / Retirer des équipements");

        ComboBox<Salle> comboSalle = new ComboBox<>();
        comboSalle.getItems().addAll(salleDAO.findAll());
        comboSalle.setPromptText("Choisir une salle...");
        comboSalle.setPrefHeight(38); comboSalle.setPrefWidth(220);
        comboSalle.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");

        ComboBox<Equipement> comboEqAssigner = new ComboBox<>();
        comboEqAssigner.getItems().addAll(equipementDAO.findAll());
        comboEqAssigner.setPromptText("Équipement à assigner...");
        comboEqAssigner.setPrefHeight(38); comboEqAssigner.setPrefWidth(220);
        comboEqAssigner.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");

        TableView<Equipement> tableauEqSalle = new TableView<>();
        styliserTableau(tableauEqSalle); tableauEqSalle.setPrefHeight(160);
        TableColumn<Equipement, String> colNomS = new TableColumn<>("Équipement");
        colNomS.setCellValueFactory(new PropertyValueFactory<>("nomEquipement"));
        TableColumn<Equipement, String> colDescS = new TableColumn<>("Description");
        colDescS.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableauEqSalle.getColumns().addAll(colNomS, colDescS);

        Label lblEqSalle = new Label("Sélectionnez une salle pour voir ses équipements");
        lblEqSalle.setFont(Font.font("Arial", 12));
        lblEqSalle.setTextFill(Color.web(TEXTE_GRIS));

        Label msgAssign = new Label(); msgAssign.setFont(Font.font("Arial", 12));

        Button btnAssigner = creerBoutonAction("➕ Assigner", "#2E7D32", "#1B5E20");
        Button btnRetirer  = creerBoutonAction("➖ Retirer",  "#C62828", "#B71C1C");

        comboSalle.setOnAction(e -> {
            if (comboSalle.getValue() == null) return;
            List<Equipement> eqSalle = equipementDAO.trouverParSalle(
                    comboSalle.getValue().getIdSalle());
            tableauEqSalle.getItems().clear();
            tableauEqSalle.getItems().addAll(eqSalle);
            lblEqSalle.setText(eqSalle.size() + " équipement(s) dans cette salle");
        });

        btnAssigner.setOnAction(e -> {
            if (comboSalle.getValue() == null || comboEqAssigner.getValue() == null) {
                msgAssign.setText("⚠ Sélectionnez une salle et un équipement.");
                msgAssign.setTextFill(Color.ORANGE); return;
            }
            equipementDAO.assignerEquipement(
                    comboSalle.getValue().getIdSalle(),
                    comboEqAssigner.getValue().getIdEquipement());
            List<Equipement> eqSalle = equipementDAO.trouverParSalle(
                    comboSalle.getValue().getIdSalle());
            tableauEqSalle.getItems().clear();
            tableauEqSalle.getItems().addAll(eqSalle);
            lblEqSalle.setText(eqSalle.size() + " équipement(s)");
            msgAssign.setText("✅ Équipement assigné !"); msgAssign.setTextFill(Color.web("#2E7D32"));
            comboEqAssigner.setValue(null);
        });

        btnRetirer.setOnAction(e -> {
            if (comboSalle.getValue() == null) {
                msgAssign.setText("⚠ Sélectionnez une salle.");
                msgAssign.setTextFill(Color.ORANGE); return;
            }
            Equipement selEq = tableauEqSalle.getSelectionModel().getSelectedItem();
            if (selEq == null) {
                msgAssign.setText("⚠ Sélectionnez un équipement dans le tableau.");
                msgAssign.setTextFill(Color.ORANGE); return;
            }
            equipementDAO.retirerEquipement(
                    comboSalle.getValue().getIdSalle(),
                    selEq.getIdEquipement());
            List<Equipement> eqSalle = equipementDAO.trouverParSalle(
                    comboSalle.getValue().getIdSalle());
            tableauEqSalle.getItems().clear();
            tableauEqSalle.getItems().addAll(eqSalle);
            lblEqSalle.setText(eqSalle.size() + " équipement(s)");
            msgAssign.setText("✅ Équipement retiré !"); msgAssign.setTextFill(Color.web("#2E7D32"));
        });

        HBox ligneAssign = new HBox(12,
                new Label("Salle :"), comboSalle,
                new Label("Équipement :"), comboEqAssigner,
                btnAssigner, btnRetirer);
        ligneAssign.setAlignment(Pos.CENTER_LEFT);

        cardSalle.getChildren().addAll(ligneAssign, lblEqSalle, tableauEqSalle, msgAssign);

        page.getChildren().addAll(cardEquip, cardSalle);
        return toScroll(page);
    }

    // ============================================================
    // VUE STATISTIQUES
    // ============================================================
    private ScrollPane creerVueStatistiques() {
        VBox page = creerPage("📊 Statistiques", "Analyse globale du système");

        HBox cartes = new HBox(18);
        cartes.getChildren().addAll(
                creerCarteStats("👥", "Utilisateurs", utilisateurDAO.findAll().size(), "#5D4037", "#EFEBE9"),
                creerCarteStats("🏫", "Salles",        salleDAO.findAll().size(),        "#1B5E20", "#E8F5E9"),
                creerCarteStats("🏢", "Bâtiments",     batimentDAO.findAll().size(),     "#0D47A1", "#E3F2FD"),
                creerCarteStats("🔧", "Équipements",   equipementDAO.findAll().size(),   "#4A148C", "#F3E5F5")
        );

        long nbAdmin = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == Role.Admin).count();
        long nbGest  = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == Role.Gestionnaire).count();
        long nbEns   = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == Role.Enseignant).count();
        long nbEtu   = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == Role.Etudiant).count();

        PieChart pie = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Admin ("        + nbAdmin + ")", nbAdmin),
                new PieChart.Data("Gestionnaire (" + nbGest  + ")", nbGest),
                new PieChart.Data("Enseignant ("   + nbEns   + ")", nbEns),
                new PieChart.Data("Etudiant ("     + nbEtu   + ")", nbEtu)
        ));
        pie.setTitle("Répartition des utilisateurs");
        pie.setPrefSize(420, 320);
        pie.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        long nbTD    = salleDAO.findAll().stream()
                .filter(s -> s.getType() == TypeSalle.TD).count();
        long nbTP    = salleDAO.findAll().stream()
                .filter(s -> s.getType() == TypeSalle.TP).count();
        long nbAmphi = salleDAO.findAll().stream()
                .filter(s -> s.getType() == TypeSalle.AMPHI).count();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis   yAxis = new NumberAxis();
        xAxis.setLabel("Type"); yAxis.setLabel("Nombre");

        BarChart<String, Number> bar = new BarChart<>(xAxis, yAxis);
        bar.setTitle("Salles par type");
        bar.setPrefSize(420, 320);
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        bar.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
                new XYChart.Data<>("TD",    nbTD),
                new XYChart.Data<>("TP",    nbTP),
                new XYChart.Data<>("AMPHI", nbAmphi)
        );
        bar.getData().add(series);

        HBox graphiques = new HBox(25, pie, bar);
        page.getChildren().addAll(cartes, graphiques);
        return toScroll(page);
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private VBox creerPage(String titre, String sousTitre) {
        VBox page = new VBox(20);
        page.setPadding(new Insets(35));
        page.setStyle("-fx-background-color: " + BEIGE_FOND + ";");
        Label lblT = new Label(titre);
        lblT.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblT.setTextFill(Color.web(TEXTE_SOMBRE));
        Label lblS = new Label(sousTitre);
        lblS.setFont(Font.font("Arial", 13)); lblS.setTextFill(Color.web(TEXTE_GRIS));
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #D7CCC8;");
        page.getChildren().addAll(new VBox(4, lblT, lblS), sep);
        return page;
    }

    private VBox creerCarteFormulaire(String titre) {
        VBox card = new VBox(14); card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        Label lbl = new Label(titre);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web(TEXTE_SOMBRE));
        card.getChildren().add(lbl);
        return card;
    }

    private void styliserTableau(TableView<?> tableau) {
        tableau.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        tableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableau.getStylesheets().add("data:text/css," +
                ".table-row-cell:odd{-fx-background-color:#FAF7F2;}" +
                ".table-row-cell:even{-fx-background-color:white;}" +
                ".table-row-cell:selected{-fx-background-color:#D7CCC8;}" +
                ".column-header{-fx-background-color:#3D3D3D;}" +
                ".column-header .label{-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;}");
    }

    private VBox creerCarteStats(String ico, String titre, int val,
                                 String couleur, String bg) {
        VBox carte = new VBox(8);
        carte.setPrefSize(220, 125); carte.setPadding(new Insets(20));
        carte.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 12;" +
                "-fx-border-color: " + couleur + "20; -fx-border-width: 1; -fx-border-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        Label lblIco = new Label(ico); lblIco.setFont(Font.font("Arial", 26));
        Label lblVal = new Label(String.valueOf(val));
        lblVal.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        lblVal.setTextFill(Color.web(couleur));
        Label lblTit = new Label(titre);
        lblTit.setFont(Font.font("Arial", 13)); lblTit.setTextFill(Color.web(TEXTE_GRIS));
        carte.getChildren().addAll(lblIco, lblVal, lblTit);
        return carte;
    }

    private TextField creerChamp(String prompt) {
        TextField tf = new TextField(); tf.setPromptText(prompt); tf.setPrefHeight(38);
        tf.setStyle("-fx-background-radius: 7; -fx-border-radius: 7;" +
                "-fx-border-color: #D7CCC8; -fx-border-width: 1;" +
                "-fx-padding: 5 12; -fx-font-size: 13px;");
        return tf;
    }

    private Button creerBoutonAction(String texte, String couleur, String hover) {
        Button btn = new Button(texte); btn.setPrefHeight(38);
        btn.setPadding(new Insets(0, 18, 0, 18));
        String s1 = "-fx-background-color:" + couleur + ";-fx-text-fill:white;" +
                "-fx-font-size:12px;-fx-font-weight:bold;-fx-background-radius:7;-fx-cursor:hand;";
        String s2 = "-fx-background-color:" + hover + ";-fx-text-fill:white;" +
                "-fx-font-size:12px;-fx-font-weight:bold;-fx-background-radius:7;-fx-cursor:hand;";
        btn.setStyle(s1);
        btn.setOnMouseEntered(e -> btn.setStyle(s2));
        btn.setOnMouseExited(e  -> btn.setStyle(s1));
        return btn;
    }

    private Button creerBtnAccesRapide(String texte, Runnable action) {
        Button btn = creerBoutonAction(texte, GRIS_MENU, ACCENT);
        btn.setOnAction(e -> action.run()); return btn;
    }

    private HBox creerToolbar(javafx.scene.Node... nodes) {
        HBox tb = new HBox(10); tb.setAlignment(Pos.CENTER_LEFT);
        tb.setPadding(new Insets(4, 0, 4, 0)); tb.getChildren().addAll(nodes); return tb;
    }

    private ScrollPane toScroll(VBox content) {
        ScrollPane sp = new ScrollPane(content); sp.setFitToWidth(true);
        sp.setStyle("-fx-background: " + BEIGE_FOND + "; -fx-background-color: " + BEIGE_FOND + ";");
        return sp;
    }

    private void afficherAlerte(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}