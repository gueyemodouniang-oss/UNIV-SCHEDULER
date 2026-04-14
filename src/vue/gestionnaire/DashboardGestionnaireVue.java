package vue.gestionnaire;

import dao.impl.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import modele.*;
import modele.Utilisateurs.Enseignant;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Jour;
import modele.enums.StatutReservation;
import service.ConflitService;
import service.ReservationService;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardGestionnaireVue extends Application {

    private Utilisateur gestionnaireConnecte;
    private BorderPane root;
    private Button btnActifMenu = null;

    private static final String FOND        = "#FAF8F5";
    private static final String FOND_CARD   = "#FFF5EE";
    private static final String ORANGE      = "#E8762A";
    private static final String ORANGE_FONC = "#C85E18";
    private static final String HEADER_COL  = "#BF5515";
    private static final String ACCENT      = "#F4874B";
    private static final String ACCENT_HOV  = "#D4621A";
    private static final String TEXTE       = "#2C2C2C";
    private static final String TEXTE_GRIS  = "#888888";

    private CoursDAO          coursDAO       = new CoursDAO();
    private SeanceDAO         seanceDAO      = new SeanceDAO();
    private SalleDAO          salleDAO       = new SalleDAO();
    private ReservationDAO    reservationDAO = new ReservationDAO();
    private CreneauHoraireDAO creneauDAO     = new CreneauHoraireDAO();
    private UtilisateurDAO    utilisateurDAO = new UtilisateurDAO();
    private GroupeDAO         groupeDAO      = new GroupeDAO();
    private MatiereDAO        matiereDAO     = new MatiereDAO();

    private ReservationService reservationService = new ReservationService();
    private ConflitService     conflitService     = new ConflitService();

    public DashboardGestionnaireVue(Utilisateur g) { this.gestionnaireConnecte = g; }
    public DashboardGestionnaireVue() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UNIV-SCHEDULER - Gestionnaire");
        root = new BorderPane();
        root.setTop(creerEnTete());
        root.setLeft(creerMenuLateral());
        root.setCenter(creerAccueil());
        root.setStyle("-fx-background-color: " + FOND + ";");
        Scene scene = new Scene(root, 1250, 780);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(660);
        primaryStage.show();
    }

    // ============================================================
    // EN-TÊTE
    // ============================================================
    private HBox creerEnTete() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(0, 25, 0, 25));
        header.setPrefHeight(68);
        header.setStyle("-fx-background-color: " + HEADER_COL + ";");
        header.setAlignment(Pos.CENTER_LEFT);

        Label icone = new Label("🎓");
        icone.setFont(Font.font("Arial", 26));
        Label titre = new Label("UNIV-SCHEDULER");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        titre.setTextFill(Color.WHITE);
        Label badge = new Label("GESTIONNAIRE");
        badge.setStyle("-fx-background-color: white; -fx-text-fill: " + HEADER_COL + ";" +
                "-fx-font-size: 10px; -fx-font-weight: bold;" +
                "-fx-padding: 3 10 3 10; -fx-background-radius: 12;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String prenom = gestionnaireConnecte != null ? gestionnaireConnecte.getPrenom() : "";
        String nom    = gestionnaireConnecte != null ? gestionnaireConnecte.getNom()    : "Gestionnaire";

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        Label lblNom = new Label(prenom + " " + nom);
        lblNom.setTextFill(Color.WHITE);
        lblNom.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label lblRole = new Label("Gestionnaire académique");
        lblRole.setTextFill(Color.web("#FFD9B3"));
        lblRole.setFont(Font.font("Arial", 11));
        userInfo.getChildren().addAll(lblNom, lblRole);

        String ini = (prenom.isEmpty() ? "G" : String.valueOf(prenom.charAt(0))) +
                (nom.isEmpty()    ? "A" : String.valueOf(nom.charAt(0)));
        Label lblIni = new Label(ini.toUpperCase());
        lblIni.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblIni.setTextFill(Color.WHITE);
        StackPane avatar = new StackPane(lblIni);
        avatar.setPrefSize(44, 44);
        avatar.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-background-radius: 22;");

        Button btnDeco = creerBoutonDeco();
        header.getChildren().addAll(icone, titre, badge, spacer, userInfo, avatar, btnDeco);
        return header;
    }

    private Button creerBoutonDeco() {
        Button btn = new Button("⬅ Déconnexion");
        btn.setPrefHeight(36); btn.setPadding(new Insets(0, 16, 0, 16));
        String s1 = "-fx-background-color: white; -fx-text-fill: " + HEADER_COL + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-border-color: white;" +
                "-fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;";
        String s2 = "-fx-background-color: #E53935; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-border-color: #E53935;" +
                "-fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;";
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
        menu.setStyle("-fx-background-color: " + ORANGE + ";");

        Label lblPrincipal = creerLabelSection("PRINCIPAL");
        Button btnAccueil  = creerBoutonMenu("🏠", "Accueil");
        Label lblGestion   = creerLabelSection("GESTION");
        Button btnCours    = creerBoutonMenu("📚", "Cours");
        Button btnSeances  = creerBoutonMenu("📅", "Séances");
        Button btnSalles   = creerBoutonMenu("🏫", "Salles Disponibles");
        Button btnReserv   = creerBoutonMenu("🔖", "Réservations");
        Label lblOutils    = creerLabelSection("OUTILS");
        Button btnConflits = creerBoutonMenu("⚠", "Détection Conflits");
        Button btnPlanning = creerBoutonMenu("🗓", "Planning");

        btnAccueil .setOnAction(e -> naviguer(menu, btnAccueil,  creerAccueil()));
        btnCours   .setOnAction(e -> naviguer(menu, btnCours,    creerVueCours()));
        btnSeances .setOnAction(e -> naviguer(menu, btnSeances,  creerVueSeances()));
        btnSalles  .setOnAction(e -> naviguer(menu, btnSalles,   creerVueSalles()));
        btnReserv  .setOnAction(e -> naviguer(menu, btnReserv,   creerVueReservations()));
        btnConflits.setOnAction(e -> naviguer(menu, btnConflits, creerVueConflits()));
        btnPlanning.setOnAction(e -> naviguer(menu, btnPlanning, creerVuePlanning()));

        activerBtn(menu, btnAccueil);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        Label version = new Label("UNIV-SCHEDULER v1.0");
        version.setTextFill(Color.web("#FFD9B3"));
        version.setFont(Font.font("Arial", 10));
        version.setPadding(new Insets(0, 0, 0, 12));

        menu.getChildren().addAll(
                lblPrincipal, btnAccueil,
                lblGestion, btnCours, btnSeances, btnSalles, btnReserv,
                lblOutils, btnConflits, btnPlanning,
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
        return "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 13px;" +
                "-fx-opacity: 0.85; -fx-alignment: CENTER_LEFT; -fx-padding: 10 12 10 12;" +
                "-fx-background-radius: 8; -fx-cursor: hand;";
    }

    private String styleBtnActif() {
        return "-fx-background-color: white; -fx-text-fill: " + HEADER_COL + "; -fx-font-size: 13px;" +
                "-fx-font-weight: bold; -fx-opacity: 1.0; -fx-alignment: CENTER_LEFT;" +
                "-fx-padding: 10 12 10 12; -fx-background-radius: 8; -fx-cursor: hand;";
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
        page.setStyle("-fx-background-color: " + FOND + ";");

        Label titre = new Label("Bonjour, " +
                (gestionnaireConnecte != null ? gestionnaireConnecte.getPrenom() : "Gestionnaire") + " 👋");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titre.setTextFill(Color.web(TEXTE));
        Label sousTitre = new Label("Gérez les cours, séances et réservations de l'université");
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.web(TEXTE_GRIS));

        HBox cartes = new HBox(18);
        long enAttente = reservationDAO.trouverParStatut(StatutReservation.En_attente).size();
        cartes.getChildren().addAll(
                creerCarteStats("📚", "Cours",       coursDAO.findAll().size(),      "#5D4037", "#EFEBE9"),
                creerCarteStats("📅", "Séances",      seanceDAO.findAll().size(),     "#1B5E20", "#E8F5E9"),
                creerCarteStats("🏫", "Salles",       salleDAO.findAll().size(),      "#0D47A1", "#E3F2FD"),
                creerCarteStats("🔖", "Réservations", reservationDAO.findAll().size(),"#4A148C", "#F3E5F5")
        );

        HBox alerteBox = new HBox(12);
        alerteBox.setAlignment(Pos.CENTER_LEFT);
        alerteBox.setPadding(new Insets(15));
        alerteBox.setStyle("-fx-background-color: #FFF8E1; -fx-background-radius: 10;" +
                "-fx-border-color: #F9A825; -fx-border-width: 0 0 0 4; -fx-border-radius: 10;");
        Label lblAlerte = new Label("⏳  " + enAttente + " réservation(s) en attente de confirmation");
        lblAlerte.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblAlerte.setTextFill(Color.web("#F57F17"));
        Button btnVoirReserv = creerBoutonAction("Voir →", ORANGE, ORANGE_FONC);
        btnVoirReserv.setOnAction(e -> naviguerDepuis("Réservations", creerVueReservations()));
        alerteBox.getChildren().addAll(lblAlerte, btnVoirReserv);

        Label lblAcces = new Label("Accès rapides");
        lblAcces.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblAcces.setTextFill(Color.web(TEXTE));

        HBox acces = new HBox(12);
        acces.getChildren().addAll(
                creerBtnAccesRapide("📚 Cours",       () -> naviguerDepuis("Cours",             creerVueCours())),
                creerBtnAccesRapide("📅 Séances",      () -> naviguerDepuis("Séances",           creerVueSeances())),
                creerBtnAccesRapide("🔖 Réservations", () -> naviguerDepuis("Réservations",      creerVueReservations())),
                creerBtnAccesRapide("⚠ Conflits",      () -> naviguerDepuis("Détection Conflits",creerVueConflits())),
                creerBtnAccesRapide("🗓 Planning",      () -> naviguerDepuis("Planning",          creerVuePlanning()))
        );

        page.getChildren().addAll(new VBox(4, titre, sousTitre), cartes, alerteBox, lblAcces, acces);
        return toScroll(page);
    }

    private void naviguerDepuis(String nomBouton, javafx.scene.Node vue) {
        VBox menu = (VBox) root.getLeft();
        menu.getChildren().stream()
                .filter(n -> n instanceof Button && ((Button)n).getText().contains(nomBouton))
                .findFirst().ifPresent(n -> naviguer(menu, (Button)n, vue));
    }

    // ============================================================
    // VUE COURS — Matière + Enseignant seulement
    // ============================================================
    private ScrollPane creerVueCours() {
        VBox page = creerPage("📚 Gestion des Cours", "Ajoutez et gérez les cours");

        HBox barreRecherche = new HBox(10);
        barreRecherche.setAlignment(Pos.CENTER_LEFT);
        barreRecherche.setPadding(new Insets(12));
        barreRecherche.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        TextField champRecherche = creerChamp("🔍  Rechercher par matière ou enseignant...");
        champRecherche.setPrefWidth(380);
        Label lblNb = new Label();
        lblNb.setFont(Font.font("Arial", 12));
        lblNb.setTextFill(Color.web(TEXTE_GRIS));
        barreRecherche.getChildren().addAll(champRecherche, lblNb);

        TableView<Cours> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(300);

        TableColumn<Cours, Integer> colId  = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCours")); colId.setPrefWidth(55);
        TableColumn<Cours, String>  colMat = new TableColumn<>("Matière");
        colMat.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getMatiere().getNomMatiere() +
                        " (" + d.getValue().getMatiere().getVolumeHoraire() + "h)"));
        TableColumn<Cours, String>  colEns = new TableColumn<>("Enseignant");
        colEns.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getEnseignant().getNom() + " " +
                        d.getValue().getEnseignant().getPrenom()));

        tableau.getColumns().addAll(colId, colMat, colEns);
        ObservableList<Cours> tousCours = FXCollections.observableArrayList(coursDAO.findAll());
        tableau.setItems(tousCours);
        lblNb.setText(tousCours.size() + " cours");

        champRecherche.textProperty().addListener((o, v, n) -> {
            if (n.isEmpty()) { tableau.setItems(tousCours); lblNb.setText(tousCours.size() + " cours"); return; }
            String f = n.toLowerCase();
            ObservableList<Cours> filtre = tousCours.filtered(c ->
                    c.getMatiere().getNomMatiere().toLowerCase().contains(f) ||
                            c.getEnseignant().getNom().toLowerCase().contains(f) ||
                            c.getEnseignant().getPrenom().toLowerCase().contains(f)
            );
            tableau.setItems(filtre);
            lblNb.setText(filtre.size() + " cours");
        });

        VBox formCard = creerCarteForm("➕ Ajouter un cours");

        // Matière
        Label lblMat = new Label("📖 Matière");
        lblMat.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblMat.setTextFill(Color.web(TEXTE_GRIS));
        ComboBox<Matiere> comboMat = new ComboBox<>();
        comboMat.getItems().addAll(matiereDAO.findAll());
        comboMat.setPromptText("Choisir une matière...");
        comboMat.setPrefHeight(40); comboMat.setMaxWidth(Double.MAX_VALUE);
        comboMat.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        VBox colMatiereBox = new VBox(5, lblMat, comboMat);
        HBox.setHgrow(colMatiereBox, Priority.ALWAYS);

        // Enseignant
        Label lblEns = new Label("👤 Enseignant");
        lblEns.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblEns.setTextFill(Color.web(TEXTE_GRIS));
        List<Utilisateur> enseignants = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == modele.enums.Role.Enseignant)
                .collect(Collectors.toList());
        ComboBox<Utilisateur> comboEns = new ComboBox<>();
        comboEns.getItems().addAll(enseignants);
        comboEns.setPromptText("Choisir un enseignant...");
        comboEns.setPrefHeight(40); comboEns.setMaxWidth(Double.MAX_VALUE);
        comboEns.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        comboEns.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Utilisateur u) {
                if (u == null) return "";
                String spec = (u instanceof Enseignant && ((Enseignant)u).getSpecialite() != null)
                        ? " — " + ((Enseignant)u).getSpecialite() : "";
                return u.getNom() + " " + u.getPrenom() + spec;
            }
            @Override public Utilisateur fromString(String s) { return null; }
        });
        VBox colEnsBox = new VBox(5, lblEns, comboEns);
        HBox.setHgrow(colEnsBox, Priority.ALWAYS);

        HBox ligneForm = new HBox(15, colMatiereBox, colEnsBox);

        Label msg = new Label(); msg.setFont(Font.font("Arial", 12));

        Button btnAjouter    = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnSupprimer  = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", ORANGE,    ORANGE_FONC);

        tableau.setOnMouseClicked(e -> {
            Cours sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                comboMat.getItems().stream()
                        .filter(m -> m.getIdMatiere() == sel.getMatiere().getIdMatiere())
                        .findFirst().ifPresent(comboMat::setValue);
                comboEns.getItems().stream()
                        .filter(u -> u.getId() == sel.getEnseignant().getId())
                        .findFirst().ifPresent(comboEns::setValue);
                msg.setText("ℹ Cours sélectionné.");
                msg.setTextFill(Color.web("#1565C0"));
            }
        });

        btnAjouter.setOnAction(e -> {
            if (comboMat.getValue() == null || comboEns.getValue() == null) {
                msg.setText("⚠ Sélectionnez une matière et un enseignant.");
                msg.setTextFill(Color.ORANGE); return;
            }
            try {
                coursDAO.ajouter(new Cours(0, comboMat.getValue(), (Enseignant) comboEns.getValue()));
                tousCours.clear(); tousCours.addAll(coursDAO.findAll());
                lblNb.setText(tousCours.size() + " cours");
                msg.setText("✅ Cours ajouté !"); msg.setTextFill(Color.web("#2E7D32"));
                comboMat.setValue(null); comboEns.setValue(null);
            } catch (ClassCastException ex) {
                msg.setText("⚠ Sélectionnez un enseignant valide.");
                msg.setTextFill(Color.ORANGE);
            }
        });

        btnSupprimer.setOnAction(e -> {
            Cours sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez un cours."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Supprimer ce cours ?");
            confirm.setContentText(
                    "Matière : " + sel.getMatiere().getNomMatiere() +
                            "\nEnseignant : " + sel.getEnseignant().getNom() +
                            "\n\n⚠ Les séances liées seront supprimées.");
            confirm.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) {
                    coursDAO.supprimer(sel.getIdCours());
                    tousCours.remove(sel);
                    lblNb.setText(tousCours.size() + " cours");
                    msg.setText("✅ Cours supprimé."); msg.setTextFill(Color.web("#2E7D32"));
                }
            });
        });

        btnActualiser.setOnAction(e -> {
            tousCours.clear(); tousCours.addAll(coursDAO.findAll());
            lblNb.setText(tousCours.size() + " cours");
        });

        formCard.getChildren().addAll(ligneForm, creerToolbar(btnAjouter, btnSupprimer, btnActualiser), msg);
        page.getChildren().addAll(barreRecherche, tableau, formCard);
        return toScroll(page);
    }

    // ============================================================
    // VUE SÉANCES — Cours + Groupe + Salle + Créneau (sans date)
    // ============================================================
    private ScrollPane creerVueSeances() {
        VBox page = creerPage("📅 Gestion des Séances",
                "Planifiez les séances avec vérification de conflits");

        // Filtres
        HBox filtres = new HBox(12);
        filtres.setAlignment(Pos.CENTER_LEFT);
        ComboBox<Jour> comboJourFiltre = new ComboBox<>();
        comboJourFiltre.getItems().addAll(Jour.values());
        comboJourFiltre.setPromptText("Filtrer par jour");
        comboJourFiltre.setPrefHeight(38);
        comboJourFiltre.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        Button btnReset = creerBoutonAction("✕ Tout", "#757575", "#616161");
        filtres.getChildren().addAll(new Label("Jour :"), comboJourFiltre, btnReset);

        TableView<Seance> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(300);

        TableColumn<Seance, String> colMat  = new TableColumn<>("Matière");
        colMat.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCours().getMatiere().getNomMatiere()));
        TableColumn<Seance, String> colEns  = new TableColumn<>("Enseignant");
        colEns.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCours().getEnseignant().getNom()));
        TableColumn<Seance, String> colGrp  = new TableColumn<>("Groupe");
        colGrp.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getGroupe().getNomGroupe()));
        TableColumn<Seance, String> colSal  = new TableColumn<>("Salle");
        colSal.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getSalle().getNumeroSalle()));
        TableColumn<Seance, String> colCren = new TableColumn<>("Créneau");
        colCren.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCreneau().getJour() + "  " +
                        d.getValue().getCreneau().getHeureDebut() + " - " +
                        d.getValue().getCreneau().getHeureFin()));

        tableau.getColumns().addAll(colMat, colEns, colGrp, colSal, colCren);
        ObservableList<Seance> toutesSeances = FXCollections.observableArrayList(seanceDAO.findAll());
        tableau.setItems(toutesSeances);

        comboJourFiltre.setOnAction(e -> {
            Jour j = comboJourFiltre.getValue();
            if (j != null) tableau.setItems(toutesSeances.filtered(
                    s -> s.getCreneau().getJour() == j));
        });
        btnReset.setOnAction(e -> { comboJourFiltre.setValue(null); tableau.setItems(toutesSeances); });

        // Formulaire ajout
        VBox formCard = creerCarteForm("➕ Ajouter une séance");

        // Cours
        Label lblCours = new Label("📚 Cours");
        lblCours.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblCours.setTextFill(Color.web(TEXTE_GRIS));
        ComboBox<Cours> comboCours = new ComboBox<>();
        comboCours.getItems().addAll(coursDAO.findAll());
        comboCours.setPromptText("Choisir un cours...");
        comboCours.setPrefHeight(38); comboCours.setMaxWidth(Double.MAX_VALUE);
        comboCours.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        comboCours.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Cours c) {
                if (c == null) return "";
                return c.getMatiere().getNomMatiere() + " — " + c.getEnseignant().getNom();
            }
            @Override public Cours fromString(String s) { return null; }
        });
        VBox colCoursBox = new VBox(5, lblCours, comboCours);
        HBox.setHgrow(colCoursBox, Priority.ALWAYS);

        // Groupe
        Label lblGrp = new Label("👥 Groupe");
        lblGrp.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblGrp.setTextFill(Color.web(TEXTE_GRIS));
        ComboBox<Groupe> comboGroupe = new ComboBox<>();
        comboGroupe.getItems().addAll(groupeDAO.findAll());
        comboGroupe.setPromptText("Choisir un groupe...");
        comboGroupe.setPrefHeight(38); comboGroupe.setMaxWidth(Double.MAX_VALUE);
        comboGroupe.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        VBox colGroupeBox = new VBox(5, lblGrp, comboGroupe);
        HBox.setHgrow(colGroupeBox, Priority.ALWAYS);

        // Salle
        Label lblSalle = new Label("🏫 Salle");
        lblSalle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblSalle.setTextFill(Color.web(TEXTE_GRIS));
        ComboBox<Salle> comboSalle = new ComboBox<>();
        comboSalle.getItems().addAll(salleDAO.findAll());
        comboSalle.setPromptText("Choisir une salle...");
        comboSalle.setPrefHeight(38); comboSalle.setMaxWidth(Double.MAX_VALUE);
        comboSalle.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        VBox colSalleBox = new VBox(5, lblSalle, comboSalle);
        HBox.setHgrow(colSalleBox, Priority.ALWAYS);

        // Créneau
        Label lblCren = new Label("⏰ Créneau");
        lblCren.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblCren.setTextFill(Color.web(TEXTE_GRIS));
        ComboBox<CreneauHoraire> comboCren = new ComboBox<>();
        comboCren.getItems().addAll(creneauDAO.findAll());
        comboCren.setPromptText("Choisir un créneau...");
        comboCren.setPrefHeight(38); comboCren.setMaxWidth(Double.MAX_VALUE);
        comboCren.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        VBox colCrenBox = new VBox(5, lblCren, comboCren);
        HBox.setHgrow(colCrenBox, Priority.ALWAYS);

        // Ligne 1 : Cours + Groupe
        HBox ligne1 = new HBox(15, colCoursBox, colGroupeBox);
        // Ligne 2 : Salle + Créneau
        HBox ligne2 = new HBox(15, colSalleBox, colCrenBox);

        // Indicateur conflit temps réel
        Label lblConflitIndicateur = new Label();
        lblConflitIndicateur.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Runnable verifierConflitTempsReel = () -> {
            if (comboSalle.getValue() != null && comboCren.getValue() != null) {
                boolean conflit = conflitService.salleOccupee(
                        comboSalle.getValue().getIdSalle(),
                        comboCren.getValue().getIdCreneauHoraire()
                );
                if (conflit) {
                    lblConflitIndicateur.setText("⚠ Conflit détecté : salle déjà occupée !");
                    lblConflitIndicateur.setTextFill(Color.web("#C62828"));
                } else {
                    lblConflitIndicateur.setText("✅ Aucun conflit — salle disponible");
                    lblConflitIndicateur.setTextFill(Color.web("#2E7D32"));
                }
            } else {
                lblConflitIndicateur.setText("");
            }
        };

        comboSalle.setOnAction(e -> verifierConflitTempsReel.run());
        comboCren.setOnAction(e  -> verifierConflitTempsReel.run());

        Label msg = new Label(); msg.setFont(Font.font("Arial", 12));

        Button btnAjouter    = creerBoutonAction("➕ Ajouter",    "#2E7D32", "#1B5E20");
        Button btnSupprimer  = creerBoutonAction("🗑 Supprimer",  "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", ORANGE,    ORANGE_FONC);

        btnAjouter.setOnAction(e -> {
            if (comboCours.getValue() == null || comboGroupe.getValue() == null ||
                    comboSalle.getValue() == null || comboCren.getValue() == null) {
                msg.setText("⚠ Remplissez tous les champs."); msg.setTextFill(Color.ORANGE); return;
            }
            boolean conflit = conflitService.salleOccupee(
                    comboSalle.getValue().getIdSalle(),
                    comboCren.getValue().getIdCreneauHoraire()
            );
            if (conflit) {
                msg.setText("❌ Impossible : conflit détecté !"); msg.setTextFill(Color.web("#C62828")); return;
            }
            seanceDAO.ajouter(new Seance(0, comboCours.getValue(), comboSalle.getValue(),
                    comboCren.getValue(), comboGroupe.getValue()));
            toutesSeances.clear(); toutesSeances.addAll(seanceDAO.findAll());
            msg.setText("✅ Séance ajoutée !"); msg.setTextFill(Color.web("#2E7D32"));
            comboCours.setValue(null); comboGroupe.setValue(null);
            comboSalle.setValue(null); comboCren.setValue(null);
            lblConflitIndicateur.setText("");
        });

        btnSupprimer.setOnAction(e -> {
            Seance sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Supprimer cette séance ?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.YES) {
                        seanceDAO.supprimer(sel.getIdSeance());
                        toutesSeances.remove(sel);
                    }
                });
            } else afficherAlerte("⚠ Sélectionnez une séance.");
        });

        btnActualiser.setOnAction(e -> {
            toutesSeances.clear(); toutesSeances.addAll(seanceDAO.findAll());
        });

        formCard.getChildren().addAll(ligne1, ligne2, lblConflitIndicateur,
                creerToolbar(btnAjouter, btnSupprimer, btnActualiser), msg);
        page.getChildren().addAll(filtres, tableau, formCard);
        return toScroll(page);
    }

    // ============================================================
    // VUE SALLES DISPONIBLES — sans date
    // ============================================================
    private ScrollPane creerVueSalles() {
        VBox page = creerPage("🏫 Salles Disponibles", "Recherchez les salles libres par créneau");

        HBox filtres = new HBox(12);
        filtres.setAlignment(Pos.CENTER_LEFT);
        filtres.setPadding(new Insets(15));
        filtres.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");

        ComboBox<CreneauHoraire> comboCren = creerCombo("Choisir un créneau", creneauDAO.findAll());
        comboCren.setPrefWidth(220);

        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Tous", "TD", "TP", "AMPHI");
        comboType.setValue("Tous"); comboType.setPrefHeight(38);
        comboType.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");

        Button btnRechercher = creerBoutonAction("🔍 Rechercher", ORANGE, ORANGE_FONC);
        Label lblResultat = new Label();
        lblResultat.setFont(Font.font("Arial", 12));
        lblResultat.setTextFill(Color.web(TEXTE_GRIS));

        TableView<Salle> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(380);

        TableColumn<Salle, String>  colNum = new TableColumn<>("Numéro");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numeroSalle"));
        TableColumn<Salle, Integer> colCap = new TableColumn<>("Capacité");
        colCap.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        TableColumn<Salle, String>  colTyp = new TableColumn<>("Type");
        colTyp.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Salle, String>  colBat = new TableColumn<>("Bâtiment");
        colBat.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getBatiment().getNomBatiment()));
        tableau.getColumns().addAll(colNum, colCap, colTyp, colBat);

        btnRechercher.setOnAction(e -> {
            if (comboCren.getValue() == null) {
                afficherAlerte("⚠ Sélectionnez un créneau."); return;
            }
            List<Salle> salles = salleDAO.trouverSallesDisponibles(
                    comboCren.getValue().getIdCreneauHoraire()
            );
            String type = comboType.getValue();
            if (!type.equals("Tous")) salles = salles.stream()
                    .filter(s -> s.getType().name().equals(type)).collect(Collectors.toList());
            tableau.getItems().clear();
            tableau.getItems().addAll(salles);
            lblResultat.setText(salles.size() + " salle(s) disponible(s)");
        });

        filtres.getChildren().addAll(
                new Label("Créneau :"), comboCren,
                new Label("Type :"), comboType,
                btnRechercher
        );
        page.getChildren().addAll(filtres, lblResultat, tableau);
        return toScroll(page);
    }

    // ============================================================
    // VUE RÉSERVATIONS
    // ============================================================
    private ScrollPane creerVueReservations() {
        VBox page = creerPage("🔖 Gestion des Réservations", "Confirmez, mettez en attente ou annulez");

        HBox filtres = new HBox(12);
        filtres.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> comboStatut = new ComboBox<>();
        comboStatut.getItems().addAll("Tous", "En_attente", "Confirme", "Annule");
        comboStatut.setValue("Tous"); comboStatut.setPrefHeight(38);
        comboStatut.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        filtres.getChildren().addAll(new Label("Filtrer par statut :"), comboStatut);

        TableView<Reservation> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(400);

        TableColumn<Reservation, Integer> colId   = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservation")); colId.setPrefWidth(55);
        TableColumn<Reservation, String>  colSal  = new TableColumn<>("Salle");
        colSal.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getSalle().getNumeroSalle()));
        TableColumn<Reservation, String>  colUser = new TableColumn<>("Demandeur");
        colUser.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getUtilisateur().getNom() + " " +
                        d.getValue().getUtilisateur().getPrenom()));
        TableColumn<Reservation, String>  colCren = new TableColumn<>("Créneau");
        colCren.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCreneau().getJour() + "  " +
                        d.getValue().getCreneau().getHeureDebut() + " - " +
                        d.getValue().getCreneau().getHeureFin()));
        TableColumn<Reservation, String>  colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDateReservation().toString()));
        TableColumn<Reservation, String>  colStat = new TableColumn<>("Statut");
        colStat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatut().name()));
        colStat.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                switch (s) {
                    case "Confirme"   -> setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                    case "Annule"     -> setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold;");
                    case "En_attente" -> setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
                    default           -> setStyle("");
                }
            }
        });

        tableau.getColumns().addAll(colId, colSal, colUser, colCren, colDate, colStat);
        ObservableList<Reservation> toutesReserv =
                FXCollections.observableArrayList(reservationDAO.findAll());
        tableau.setItems(toutesReserv);

        comboStatut.setOnAction(e -> {
            String s = comboStatut.getValue();
            if (s.equals("Tous")) { tableau.setItems(toutesReserv); return; }
            tableau.setItems(toutesReserv.filtered(r -> r.getStatut().name().equals(s)));
        });

        Button btnConfirmer  = creerBoutonAction("✅ Confirmer",  "#2E7D32", "#1B5E20");
        Button btnAttente    = creerBoutonAction("⏳ En attente", "#E65100", "#BF360C");
        Button btnAnnuler    = creerBoutonAction("❌ Annuler",    "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser", ORANGE,    ORANGE_FONC);

        btnConfirmer.setOnAction(e -> {
            Reservation sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservationService.confirmerReservation(sel.getIdReservation());
                toutesReserv.clear(); toutesReserv.addAll(reservationDAO.findAll());
                comboStatut.setValue("Tous"); tableau.setItems(toutesReserv);
            } else afficherAlerte("⚠ Sélectionnez une réservation.");
        });
        btnAttente.setOnAction(e -> {
            Reservation sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                reservationService.mettreEnAttente(sel.getIdReservation());
                toutesReserv.clear(); toutesReserv.addAll(reservationDAO.findAll());
                comboStatut.setValue("Tous"); tableau.setItems(toutesReserv);
            } else afficherAlerte("⚠ Sélectionnez une réservation.");
        });
        btnAnnuler.setOnAction(e -> {
            Reservation sel = tableau.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Annuler cette réservation ?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.YES) {
                        reservationService.annulerReservation(sel.getIdReservation());
                        toutesReserv.clear(); toutesReserv.addAll(reservationDAO.findAll());
                        comboStatut.setValue("Tous"); tableau.setItems(toutesReserv);
                    }
                });
            } else afficherAlerte("⚠ Sélectionnez une réservation.");
        });
        btnActualiser.setOnAction(e -> {
            toutesReserv.clear(); toutesReserv.addAll(reservationDAO.findAll());
        });

        HBox toolbar = new HBox(10);
        toolbar.getChildren().addAll(btnConfirmer, btnAttente, btnAnnuler, btnActualiser);
        page.getChildren().addAll(filtres, toolbar, tableau);
        return toScroll(page);
    }

    // ============================================================
    // VUE CONFLITS — sans date
    // ============================================================
    private ScrollPane creerVueConflits() {
        VBox page = creerPage("⚠ Détection des Conflits",
                "Vérifiez la disponibilité des salles et enseignants");

        // Carte salle
        VBox cardSalle = creerCarteForm("🏫 Vérifier disponibilité d'une salle");
        ComboBox<Salle>          comboSalle = creerCombo("Salle",   salleDAO.findAll());
        ComboBox<CreneauHoraire> comboCren  = creerCombo("Créneau", creneauDAO.findAll());
        HBox.setHgrow(comboSalle, Priority.ALWAYS);
        HBox.setHgrow(comboCren,  Priority.ALWAYS);
        HBox ligne1 = new HBox(12, comboSalle, comboCren);

        VBox resultatSalle = new VBox(8);
        resultatSalle.setPadding(new Insets(12));
        resultatSalle.setVisible(false);

        Button btnVerifSalle = creerBoutonAction("🔍 Vérifier la salle", ORANGE, ORANGE_FONC);
        btnVerifSalle.setOnAction(e -> {
            if (comboSalle.getValue() == null || comboCren.getValue() == null) {
                afficherAlerte("⚠ Remplissez tous les champs."); return;
            }
            boolean occupe = conflitService.salleOccupee(
                    comboSalle.getValue().getIdSalle(),
                    comboCren.getValue().getIdCreneauHoraire()
            );
            resultatSalle.getChildren().clear();
            resultatSalle.setVisible(true);
            if (occupe) {
                resultatSalle.setStyle("-fx-background-color: #FFEBEE; -fx-background-radius: 8;" +
                        "-fx-border-color: #C62828; -fx-border-width: 0 0 0 4; -fx-border-radius: 8;");
                Label lbl = new Label("❌  Conflit détecté !");
                lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                lbl.setTextFill(Color.web("#C62828"));
                Label detail = new Label("La salle " + comboSalle.getValue().getNumeroSalle() +
                        " est déjà occupée au créneau " +
                        comboCren.getValue().getJour() + " " +
                        comboCren.getValue().getHeureDebut() + " - " +
                        comboCren.getValue().getHeureFin());
                detail.setFont(Font.font("Arial", 12));
                detail.setTextFill(Color.web("#757575"));
                resultatSalle.getChildren().addAll(lbl, detail);
            } else {
                resultatSalle.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8;" +
                        "-fx-border-color: #2E7D32; -fx-border-width: 0 0 0 4; -fx-border-radius: 8;");
                Label lbl = new Label("✅  Salle disponible !");
                lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                lbl.setTextFill(Color.web("#2E7D32"));
                Label detail = new Label("La salle " + comboSalle.getValue().getNumeroSalle() +
                        " est libre le " + comboCren.getValue().getJour() + " de " +
                        comboCren.getValue().getHeureDebut() + " à " +
                        comboCren.getValue().getHeureFin());
                detail.setFont(Font.font("Arial", 12));
                detail.setTextFill(Color.web("#757575"));
                resultatSalle.getChildren().addAll(lbl, detail);
            }
        });
        cardSalle.getChildren().addAll(ligne1, creerToolbar(btnVerifSalle), resultatSalle);

        // Carte enseignant
        VBox cardEns = creerCarteForm("👤 Vérifier disponibilité d'un enseignant");
        List<Utilisateur> enseignants = utilisateurDAO.findAll().stream()
                .filter(u -> u.getRole() == modele.enums.Role.Enseignant)
                .collect(Collectors.toList());
        ComboBox<Utilisateur> comboEns = new ComboBox<>();
        comboEns.getItems().addAll(enseignants);
        comboEns.setPromptText("Choisir un enseignant...");
        comboEns.setPrefHeight(38); comboEns.setMaxWidth(Double.MAX_VALUE);
        comboEns.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        comboEns.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Utilisateur u) {
                if (u == null) return "";
                String spec = (u instanceof Enseignant &&
                        ((Enseignant)u).getSpecialite() != null)
                        ? " — " + ((Enseignant)u).getSpecialite() : "";
                return u.getNom() + " " + u.getPrenom() + spec;
            }
            @Override public Utilisateur fromString(String s) { return null; }
        });
        ComboBox<CreneauHoraire> comboCren2 = creerCombo("Créneau", creneauDAO.findAll());
        HBox.setHgrow(comboEns,   Priority.ALWAYS);
        HBox.setHgrow(comboCren2, Priority.ALWAYS);
        HBox ligne2 = new HBox(12, comboEns, comboCren2);

        VBox resultatEns = new VBox(8);
        resultatEns.setPadding(new Insets(12));
        resultatEns.setVisible(false);

        Button btnVerifEns = creerBoutonAction("🔍 Vérifier l'enseignant", ORANGE, ORANGE_FONC);
        btnVerifEns.setOnAction(e -> {
            if (comboEns.getValue() == null || comboCren2.getValue() == null) {
                afficherAlerte("⚠ Remplissez tous les champs."); return;
            }
            boolean occupe = conflitService.enseignantOccupe(
                    comboEns.getValue().getId(),
                    comboCren2.getValue().getIdCreneauHoraire()
            );
            resultatEns.getChildren().clear();
            resultatEns.setVisible(true);
            if (occupe) {
                resultatEns.setStyle("-fx-background-color: #FFEBEE; -fx-background-radius: 8;" +
                        "-fx-border-color: #C62828; -fx-border-width: 0 0 0 4; -fx-border-radius: 8;");
                Label lbl = new Label("❌  Enseignant non disponible !");
                lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                lbl.setTextFill(Color.web("#C62828"));
                Label detail = new Label(comboEns.getValue().getNom() + " " +
                        comboEns.getValue().getPrenom() +
                        " a déjà une séance sur ce créneau : " +
                        comboCren2.getValue().getJour() + " " +
                        comboCren2.getValue().getHeureDebut() + " - " +
                        comboCren2.getValue().getHeureFin());
                detail.setFont(Font.font("Arial", 12));
                detail.setTextFill(Color.web("#757575"));
                resultatEns.getChildren().addAll(lbl, detail);
            } else {
                resultatEns.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 8;" +
                        "-fx-border-color: #2E7D32; -fx-border-width: 0 0 0 4; -fx-border-radius: 8;");
                Label lbl = new Label("✅  Enseignant disponible !");
                lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                lbl.setTextFill(Color.web("#2E7D32"));
                Label detail = new Label(comboEns.getValue().getNom() + " " +
                        comboEns.getValue().getPrenom() +
                        " est libre le " + comboCren2.getValue().getJour() + " de " +
                        comboCren2.getValue().getHeureDebut() + " à " +
                        comboCren2.getValue().getHeureFin());
                detail.setFont(Font.font("Arial", 12));
                detail.setTextFill(Color.web("#757575"));
                resultatEns.getChildren().addAll(lbl, detail);
            }
        });
        cardEns.getChildren().addAll(ligne2, creerToolbar(btnVerifEns), resultatEns);
        page.getChildren().addAll(cardSalle, cardEns);
        return toScroll(page);
    }

    // ============================================================
    // VUE PLANNING — tableau hebdomadaire fixe
    // ============================================================
    private ScrollPane creerVuePlanning() {
        VBox page = creerPage("🗓 Planning des Séances", "Vue hebdomadaire par jour");

        List<Seance> toutesSeances = seanceDAO.findAll();

        // Sélecteur jour
        javafx.scene.layout.FlowPane selectorJour = new javafx.scene.layout.FlowPane(8, 8);
        selectorJour.setAlignment(Pos.CENTER_LEFT);

        VBox contenuPlanning = new VBox(10);

        String styleN = "-fx-background-color: white; -fx-text-fill: " + TEXTE + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20;" +
                "-fx-border-color: #D7CCC8; -fx-border-radius: 20; -fx-cursor: hand;" +
                "-fx-padding: 6 16 6 16;";
        String styleA = "-fx-background-color: " + ORANGE + "; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20;" +
                "-fx-border-color: " + ORANGE + "; -fx-border-radius: 20; -fx-cursor: hand;" +
                "-fx-padding: 6 16 6 16;";

        for (Jour jour : Jour.values()) {
            long nb = toutesSeances.stream()
                    .filter(s -> s.getCreneau().getJour() == jour).count();
            Button btnJour = new Button(jour.name() + (nb > 0 ? "  (" + nb + ")" : ""));
            btnJour.setStyle(styleN);
            if (nb == 0) btnJour.setDisable(true);

            btnJour.setOnAction(e -> {
                selectorJour.getChildren().forEach(n -> {
                    if (n instanceof Button) ((Button)n).setStyle(styleN);
                });
                btnJour.setStyle(styleA);

                contenuPlanning.getChildren().clear();
                List<Seance> seancesJour = toutesSeances.stream()
                        .filter(s -> s.getCreneau().getJour() == jour)
                        .sorted((a, b) -> a.getCreneau().getHeureDebut()
                                .compareTo(b.getCreneau().getHeureDebut()))
                        .collect(Collectors.toList());

                if (seancesJour.isEmpty()) {
                    VBox vide = new VBox();
                    vide.setAlignment(Pos.CENTER); vide.setPadding(new Insets(40));
                    vide.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                            "-fx-border-color: #E0D8CC; -fx-border-radius: 10;");
                    Label lblVide = new Label("📭  Aucune séance ce jour");
                    lblVide.setFont(Font.font("Arial", 14));
                    lblVide.setTextFill(Color.web(TEXTE_GRIS));
                    vide.getChildren().add(lblVide);
                    contenuPlanning.getChildren().add(vide);
                    return;
                }

                Label lblEntete = new Label(jour.name() + " — " + seancesJour.size() + " séance(s)");
                lblEntete.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                lblEntete.setTextFill(Color.web(ORANGE));
                contenuPlanning.getChildren().add(lblEntete);

                for (Seance s : seancesJour) {
                    HBox carte = new HBox(15);
                    carte.setPadding(new Insets(15, 20, 15, 20));
                    carte.setAlignment(Pos.CENTER_LEFT);
                    carte.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                            "-fx-border-color: " + ORANGE + "; -fx-border-width: 0 0 0 4;" +
                            "-fx-border-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 5, 0, 0, 2);");

                    VBox heureBox = new VBox(2);
                    heureBox.setAlignment(Pos.CENTER); heureBox.setPrefWidth(80);
                    heureBox.setStyle("-fx-background-color: " + FOND_CARD +
                            "; -fx-background-radius: 8; -fx-padding: 8;");
                    Label lblDebut = new Label(s.getCreneau().getHeureDebut().toString());
                    lblDebut.setFont(Font.font("Arial", FontWeight.BOLD, 15));
                    lblDebut.setTextFill(Color.web(ORANGE));
                    Label lblFin = new Label(s.getCreneau().getHeureFin().toString());
                    lblFin.setFont(Font.font("Arial", 12));
                    lblFin.setTextFill(Color.web(TEXTE_GRIS));
                    heureBox.getChildren().addAll(lblDebut, lblFin);

                    VBox infoBox = new VBox(4);
                    HBox.setHgrow(infoBox, Priority.ALWAYS);
                    Label lblMatiere = new Label(s.getCours().getMatiere().getNomMatiere());
                    lblMatiere.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    lblMatiere.setTextFill(Color.web(TEXTE));

                    HBox details = new HBox(20);
                    details.getChildren().addAll(
                            creerInfoTag("👤", s.getCours().getEnseignant().getNom()),
                            creerInfoTag("👥", s.getGroupe().getNomGroupe()),
                            creerInfoTag("🏫", s.getSalle().getNumeroSalle())
                    );
                    infoBox.getChildren().addAll(lblMatiere, details);
                    carte.getChildren().addAll(heureBox, infoBox);
                    contenuPlanning.getChildren().add(carte);
                }
            });
            selectorJour.getChildren().add(btnJour);
        }

        // Cliquer premier jour actif
        selectorJour.getChildren().stream()
                .filter(n -> n instanceof Button && !((Button)n).isDisabled())
                .findFirst().ifPresent(n -> ((Button)n).fire());

        page.getChildren().addAll(selectorJour, contenuPlanning);
        return toScroll(page);
    }

    private HBox creerInfoTag(String icone, String texte) {
        HBox tag = new HBox(5); tag.setAlignment(Pos.CENTER_LEFT);
        tag.setPadding(new Insets(3, 8, 3, 8));
        tag.setStyle("-fx-background-color: " + FOND_CARD + "; -fx-background-radius: 12;");
        Label lblIco = new Label(icone); lblIco.setFont(Font.font("Arial", 12));
        Label lblTxt = new Label(texte);
        lblTxt.setFont(Font.font("Arial", 12)); lblTxt.setTextFill(Color.web(TEXTE));
        tag.getChildren().addAll(lblIco, lblTxt);
        return tag;
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private VBox creerPage(String titre, String sousTitre) {
        VBox page = new VBox(20);
        page.setPadding(new Insets(35));
        page.setStyle("-fx-background-color: " + FOND + ";");
        Label lblT = new Label(titre);
        lblT.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblT.setTextFill(Color.web(TEXTE));
        Label lblS = new Label(sousTitre);
        lblS.setFont(Font.font("Arial", 13));
        lblS.setTextFill(Color.web(TEXTE_GRIS));
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #E0D8CC;");
        page.getChildren().addAll(new VBox(4, lblT, lblS), sep);
        return page;
    }

    private VBox creerCarteForm(String titre) {
        VBox card = new VBox(14); card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;" +
                "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);");
        Label lbl = new Label(titre);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setTextFill(Color.web(TEXTE));
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
                ".table-row-cell:selected{-fx-background-color:#FFE0CC;}" +
                ".column-header{-fx-background-color:" + ORANGE + ";}" +
                ".column-header .label{-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;}");
    }

    private VBox creerCarteStats(String ico, String titre, int val,
                                 String couleur, String bg) {
        VBox carte = new VBox(8); carte.setPrefSize(220, 125); carte.setPadding(new Insets(20));
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

    private <T> ComboBox<T> creerCombo(String prompt, List<T> items) {
        ComboBox<T> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setPromptText(prompt); combo.setPrefHeight(38);
        combo.setStyle("-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;");
        return combo;
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
                "-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-background-radius:7;-fx-cursor:hand;";
        String s2 = "-fx-background-color:" + hover + ";-fx-text-fill:white;" +
                "-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-background-radius:7;-fx-cursor:hand;";
        btn.setStyle(s1);
        btn.setOnMouseEntered(e -> btn.setStyle(s2));
        btn.setOnMouseExited(e  -> btn.setStyle(s1));
        return btn;
    }

    private Button creerBtnAccesRapide(String texte, Runnable action) {
        Button btn = creerBoutonAction(texte, ORANGE, ORANGE_FONC);
        btn.setOnAction(e -> action.run()); return btn;
    }

    private HBox creerToolbar(javafx.scene.Node... nodes) {
        HBox tb = new HBox(10); tb.setAlignment(Pos.CENTER_LEFT);
        tb.setPadding(new Insets(4, 0, 4, 0)); tb.getChildren().addAll(nodes); return tb;
    }

    private ScrollPane toScroll(VBox content) {
        ScrollPane sp = new ScrollPane(content); sp.setFitToWidth(true);
        sp.setStyle("-fx-background: " + FOND + "; -fx-background-color: " + FOND + ";");
        return sp;
    }

    private void afficherAlerte(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}