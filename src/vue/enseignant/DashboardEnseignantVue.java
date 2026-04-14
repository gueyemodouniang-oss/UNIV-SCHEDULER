package vue.enseignant;

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
import modele.enums.Jour;
import modele.enums.StatutReservation;
import service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardEnseignantVue extends Application {

    private Enseignant enseignantConnecte;
    private BorderPane root;
    private Button btnActifMenu = null;

    // Couleurs — thème Blanc cassé + Orange
    private static final String FOND        = "#FAF8F5";
    private static final String FOND_CARD   = "#FFF5EE";
    private static final String ORANGE      = "#E8762A";
    private static final String ORANGE_FONC = "#C85E18";
    private static final String HEADER_COL  = "#BF5515";
    private static final String TEXTE       = "#2C2C2C";
    private static final String TEXTE_GRIS  = "#888888";

    // DAOs
    private SeanceDAO         seanceDAO      = new SeanceDAO();
    private SalleDAO          salleDAO       = new SalleDAO();
    private ReservationDAO    reservationDAO = new ReservationDAO();
    private CreneauHoraireDAO creneauDAO     = new CreneauHoraireDAO();

    // Service
    private ReservationService reservationService = new ReservationService();

    public DashboardEnseignantVue(Enseignant enseignant) { this.enseignantConnecte = enseignant; }
    public DashboardEnseignantVue() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UNIV-SCHEDULER - Enseignant");
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

        Label icone = new Label("📚");
        icone.setFont(Font.font("Arial", 26));

        Label titre = new Label("UNIV-SCHEDULER");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        titre.setTextFill(Color.WHITE);

        Label badge = new Label("ENSEIGNANT");
        badge.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + HEADER_COL + ";" +
                        "-fx-font-size: 10px; -fx-font-weight: bold;" +
                        "-fx-padding: 3 10 3 10; -fx-background-radius: 12;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String prenom = enseignantConnecte != null ? enseignantConnecte.getPrenom() : "";
        String nom    = enseignantConnecte != null ? enseignantConnecte.getNom()    : "Enseignant";
        String dept   = enseignantConnecte != null && enseignantConnecte.getDepartement() != null
                ? enseignantConnecte.getDepartement() : "";
        String spec   = enseignantConnecte != null && enseignantConnecte.getSpecialite() != null
                ? enseignantConnecte.getSpecialite() : "";

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        Label lblNom = new Label(prenom + " " + nom);
        lblNom.setTextFill(Color.WHITE);
        lblNom.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label lblInfo = new Label(dept + (spec.isEmpty() ? "" : " — " + spec));
        lblInfo.setTextFill(Color.web("#FFD9B3"));
        lblInfo.setFont(Font.font("Arial", 11));
        userInfo.getChildren().addAll(lblNom, lblInfo);

        // Avatar initiales
        String ini = (prenom.isEmpty() ? "E" : String.valueOf(prenom.charAt(0))) +
                (nom.isEmpty()    ? "N" : String.valueOf(nom.charAt(0)));
        Label lblIni = new Label(ini.toUpperCase());
        lblIni.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblIni.setTextFill(Color.WHITE);
        StackPane avatar = new StackPane(lblIni);
        avatar.setPrefSize(44, 44);
        avatar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.25);" +
                        "-fx-background-radius: 22;"
        );

        Button btnDeco = creerBoutonDeco();
        header.getChildren().addAll(icone, titre, badge, spacer, userInfo, avatar, btnDeco);
        return header;
    }

    private Button creerBoutonDeco() {
        Button btn = new Button("⬅ Déconnexion");
        btn.setPrefHeight(36); btn.setPadding(new Insets(0, 16, 0, 16));
        String s1 = "-fx-background-color: white; -fx-text-fill: " + HEADER_COL + ";" +
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
        menu.setStyle("-fx-background-color: " + ORANGE + ";");

        Label lblPrincipal = creerLabelSection("PRINCIPAL");
        Button btnAccueil  = creerBoutonMenu("🏠", "Accueil");

        Label lblEDT     = creerLabelSection("EMPLOI DU TEMPS");
        Button btnEDT    = creerBoutonMenu("📅", "Mon EDT");

        Label lblSalles       = creerLabelSection("SALLES & RÉSERVATIONS");
        Button btnReserver    = creerBoutonMenu("🔖", "Réserver une Salle");
        Button btnMesResas    = creerBoutonMenu("📋", "Mes Réservations");
        Button btnSallesDispo = creerBoutonMenu("🏫", "Salles Disponibles");

        btnAccueil   .setOnAction(e -> naviguer(menu, btnAccueil,    creerAccueil()));
        btnEDT       .setOnAction(e -> naviguer(menu, btnEDT,        creerVueEmploiDuTemps()));
        btnReserver  .setOnAction(e -> naviguer(menu, btnReserver,   creerVueReservation()));
        btnMesResas  .setOnAction(e -> naviguer(menu, btnMesResas,   creerVueMesReservations()));
        btnSallesDispo.setOnAction(e -> naviguer(menu, btnSallesDispo, creerVueSallesDisponibles()));

        activerBtn(menu, btnAccueil);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        Label version = new Label("UNIV-SCHEDULER v1.0");
        version.setTextFill(Color.web("#FFD9B3"));
        version.setFont(Font.font("Arial", 10));
        version.setPadding(new Insets(0, 0, 0, 12));

        menu.getChildren().addAll(
                lblPrincipal, btnAccueil,
                lblEDT, btnEDT,
                lblSalles, btnReserver, btnMesResas, btnSallesDispo,
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
        return "-fx-background-color: white; -fx-text-fill: " + HEADER_COL + ";" +
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
        VBox page = new VBox(25);
        page.setPadding(new Insets(35));
        page.setStyle("-fx-background-color: " + FOND + ";");

        String prenom = enseignantConnecte != null ? enseignantConnecte.getPrenom() : "Enseignant";
        Label titre = new Label("Bonjour, " + prenom + " 👋");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titre.setTextFill(Color.web(TEXTE));
        Label sousTitre = new Label("Gérez votre emploi du temps et vos réservations de salles");
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.web(TEXTE_GRIS));

        // Carte profil
        if (enseignantConnecte != null) {
            HBox profilCard = new HBox(20);
            profilCard.setPadding(new Insets(20));
            profilCard.setAlignment(Pos.CENTER_LEFT);
            profilCard.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 12;" +
                            "-fx-border-color: " + ORANGE + "30; -fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
            );

            String ini = String.valueOf(enseignantConnecte.getPrenom().charAt(0)) +
                    String.valueOf(enseignantConnecte.getNom().charAt(0));
            Label lblIni = new Label(ini.toUpperCase());
            lblIni.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            lblIni.setTextFill(Color.WHITE);
            StackPane avatarGrand = new StackPane(lblIni);
            avatarGrand.setPrefSize(70, 70);
            avatarGrand.setStyle(
                    "-fx-background-color: " + ORANGE + "; -fx-background-radius: 35;"
            );

            VBox infos = new VBox(8);
            HBox.setHgrow(infos, Priority.ALWAYS);
            Label lblNomComplet = new Label(
                    enseignantConnecte.getPrenom() + " " + enseignantConnecte.getNom());
            lblNomComplet.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblNomComplet.setTextFill(Color.web(TEXTE));

            HBox tags = new HBox(10);
            tags.getChildren().addAll(
                    creerTag("🎓 " + (enseignantConnecte.getSpecialite()  != null ?
                            enseignantConnecte.getSpecialite()  : "—"), ORANGE),
                    creerTag("🏢 " + (enseignantConnecte.getDepartement() != null ?
                            enseignantConnecte.getDepartement() : "—"), "#1B5E20"),
                    creerTag("📧 " + enseignantConnecte.getEmail(), "#0D47A1")
            );
            infos.getChildren().addAll(lblNomComplet, tags);
            profilCard.getChildren().addAll(avatarGrand, infos);
            page.getChildren().addAll(new VBox(4, titre, sousTitre), profilCard);
        } else {
            page.getChildren().addAll(new VBox(4, titre, sousTitre));
        }

        // Stats
        int nbSeances = enseignantConnecte != null ?
                seanceDAO.findAll().stream()
                        .filter(s -> s.getCours().getEnseignant().getId() == enseignantConnecte.getId())
                        .collect(Collectors.toList()).size() : 0;
        int nbResas = enseignantConnecte != null ?
                reservationDAO.trouverParUtilisateur(enseignantConnecte.getId()).size() : 0;

        HBox cartes = new HBox(18);
        cartes.getChildren().addAll(
                creerCarteStats("📅", "Mes Séances",      nbSeances,                "#1B5E20", "#E8F5E9"),
                creerCarteStats("🔖", "Mes Réservations", nbResas,                  "#4A148C", "#F3E5F5"),
                creerCarteStats("🏫", "Salles Dispo",     salleDAO.findAll().size(),"#0D47A1", "#E3F2FD")
        );

        // Accès rapides
        Label lblAcces = new Label("Accès rapides");
        lblAcces.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblAcces.setTextFill(Color.web(TEXTE));

        HBox acces = new HBox(12);
        VBox menu = (VBox) root.getLeft();
        acces.getChildren().addAll(
                creerBtnAccesRapide("📅 Mon EDT",
                        () -> naviguerDepuis("Mon EDT",            creerVueEmploiDuTemps())),
                creerBtnAccesRapide("🔖 Réserver une Salle",
                        () -> naviguerDepuis("Réserver une Salle", creerVueReservation())),
                creerBtnAccesRapide("📋 Mes Réservations",
                        () -> naviguerDepuis("Mes Réservations",   creerVueMesReservations())),
                creerBtnAccesRapide("🏫 Salles Disponibles",
                        () -> naviguerDepuis("Salles Disponibles", creerVueSallesDisponibles()))
        );

        page.getChildren().addAll(cartes, lblAcces, acces);
        return toScroll(page);
    }

    private void naviguerDepuis(String nomBouton, javafx.scene.Node vue) {
        VBox menu = (VBox) root.getLeft();
        menu.getChildren().stream()
                .filter(n -> n instanceof Button && ((Button)n).getText().contains(nomBouton))
                .findFirst().ifPresent(n -> naviguer(menu, (Button)n, vue));
    }

    // ============================================================
    // VUE EMPLOI DU TEMPS — planning visuel par jour
    // ============================================================
    private ScrollPane creerVueEmploiDuTemps() {
        VBox page = creerPage("📅 Mon Emploi du Temps",
                "Vos séances organisées par jour");

        List<Seance> mesSeances = enseignantConnecte != null ?
                seanceDAO.findAll().stream()
                        .filter(s -> s.getCours().getEnseignant().getId() == enseignantConnecte.getId())
                        .collect(Collectors.toList()) :
                seanceDAO.findAll();

        // Sélecteur jour
        FlowPane selectorJour = new FlowPane(8, 8);
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
            long nb = mesSeances.stream()
                    .filter(s -> s.getCreneau().getJour() == jour).count();

            Button btnJour = new Button(jour.name() + (nb > 0 ? "  (" + nb + ")" : ""));
            btnJour.setStyle(styleN);
            btnJour.setDisable(nb == 0);

            btnJour.setOnAction(e -> {
                selectorJour.getChildren().forEach(n -> {
                    if (n instanceof Button) ((Button)n).setStyle(styleN);
                });
                btnJour.setStyle(styleA);

                List<Seance> seancesJour = mesSeances.stream()
                        .filter(s -> s.getCreneau().getJour() == jour)
                        .sorted((a, b) -> a.getCreneau().getHeureDebut()
                                .compareTo(b.getCreneau().getHeureDebut()))
                        .collect(Collectors.toList());

                contenuPlanning.getChildren().clear();

                HBox entete = new HBox(10);
                Label lblJT = new Label(jour.name());
                lblJT.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                lblJT.setTextFill(Color.web(ORANGE));
                Label lblNb = new Label(seancesJour.size() + " séance(s)");
                lblNb.setFont(Font.font("Arial", 13));
                lblNb.setTextFill(Color.web(TEXTE_GRIS));
                lblNb.setPadding(new Insets(4, 0, 0, 0));
                entete.getChildren().addAll(lblJT, lblNb);
                contenuPlanning.getChildren().add(entete);

                for (Seance s : seancesJour) {
                    contenuPlanning.getChildren().add(creerCarteSeance(s));
                }
            });
            selectorJour.getChildren().add(btnJour);
        }

        // Cliquer premier jour actif
        selectorJour.getChildren().stream()
                .filter(n -> n instanceof Button && !((Button)n).isDisabled())
                .findFirst().ifPresent(n -> ((Button)n).fire());

        // Si aucune séance
        if (mesSeances.isEmpty()) {
            VBox vide = new VBox();
            vide.setAlignment(Pos.CENTER); vide.setPadding(new Insets(50));
            vide.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10;" +
                            "-fx-border-color: #E0D8CC; -fx-border-radius: 10;"
            );
            Label lbl = new Label("📭  Aucune séance planifiée");
            lbl.setFont(Font.font("Arial", 14)); lbl.setTextFill(Color.web(TEXTE_GRIS));
            vide.getChildren().add(lbl);
            contenuPlanning.getChildren().add(vide);
        }

        page.getChildren().addAll(selectorJour, contenuPlanning);
        return toScroll(page);
    }

    // Carte séance réutilisable
    private HBox creerCarteSeance(Seance s) {
        HBox carte = new HBox(15);
        carte.setPadding(new Insets(15, 20, 15, 20));
        carte.setAlignment(Pos.CENTER_LEFT);
        carte.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: " + ORANGE + "; -fx-border-width: 0 0 0 4;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 5, 0, 0, 2);"
        );

        VBox heureBox = new VBox(3);
        heureBox.setAlignment(Pos.CENTER); heureBox.setPrefWidth(85);
        heureBox.setPadding(new Insets(8));
        heureBox.setStyle(
                "-fx-background-color: " + FOND_CARD + "; -fx-background-radius: 8;"
        );
        Label lblDebut = new Label(s.getCreneau().getHeureDebut().toString());
        lblDebut.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblDebut.setTextFill(Color.web(ORANGE));
        Label lblTiret = new Label("│");
        lblTiret.setTextFill(Color.web("#D7CCC8"));
        Label lblFin = new Label(s.getCreneau().getHeureFin().toString());
        lblFin.setFont(Font.font("Arial", 12));
        lblFin.setTextFill(Color.web(TEXTE_GRIS));
        heureBox.getChildren().addAll(lblDebut, lblTiret, lblFin);

        VBox infoBox = new VBox(6);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        Label lblMatiere = new Label(s.getCours().getMatiere().getNomMatiere());
        lblMatiere.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lblMatiere.setTextFill(Color.web(TEXTE));

        HBox tags = new HBox(10);
        tags.getChildren().addAll(
                creerInfoTag("👥", s.getGroupe().getNomGroupe()),
                creerInfoTag("🏫", s.getSalle().getNumeroSalle()),
                creerInfoTag("📅", s.getCreneau().getJour().toString())
        );
        infoBox.getChildren().addAll(lblMatiere, tags);
        carte.getChildren().addAll(heureBox, infoBox);
        return carte;
    }

    // ============================================================
    // VUE RÉSERVER UNE SALLE
    // ============================================================
    private ScrollPane creerVueReservation() {
        VBox page = creerPage("🔖 Réserver une Salle",
                "Soumettez une demande de réservation");

        VBox formCard = new VBox(18);
        formCard.setPadding(new Insets(25));
        formCard.setMaxWidth(560);
        formCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 12;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        Label lblForm = new Label("📝 Nouvelle réservation");
        lblForm.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lblForm.setTextFill(Color.web(TEXTE));

        ComboBox<Salle> comboSalle = new ComboBox<>();
        comboSalle.getItems().addAll(salleDAO.findAll());
        comboSalle.setPromptText("Choisir une salle");
        comboSalle.setPrefHeight(38); comboSalle.setMaxWidth(Double.MAX_VALUE);
        comboSalle.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        ComboBox<CreneauHoraire> comboCreneau = new ComboBox<>();
        comboCreneau.getItems().addAll(creneauDAO.findAll());
        comboCreneau.setPromptText("Choisir un créneau");
        comboCreneau.setPrefHeight(38); comboCreneau.setMaxWidth(Double.MAX_VALUE);
        comboCreneau.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefHeight(38); datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        Label msgResultat = new Label();
        msgResultat.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        msgResultat.setWrapText(true);

        Button btnReserver = creerBoutonAction("✅ Envoyer la demande", ORANGE, ORANGE_FONC);
        btnReserver.setMaxWidth(Double.MAX_VALUE);

        btnReserver.setOnAction(e -> {
            if (comboSalle.getValue() == null ||
                    comboCreneau.getValue() == null ||
                    datePicker.getValue() == null) {
                msgResultat.setText("⚠ Remplissez tous les champs.");
                msgResultat.setTextFill(Color.web(ORANGE));
                return;
            }
            Reservation resa = new Reservation(
                    0,
                    comboSalle.getValue(),
                    comboCreneau.getValue(),
                    enseignantConnecte,
                    StatutReservation.En_attente,
                    datePicker.getValue()
            );
            boolean succes = reservationService.ajouterReservationEnAttente(resa);
            if (succes) {
                msgResultat.setText("✅ Demande envoyée ! En attente de confirmation.");
                msgResultat.setTextFill(Color.web("#1B5E20"));
                comboSalle.setValue(null);
                comboCreneau.setValue(null);
                datePicker.setValue(LocalDate.now());
            } else {
                msgResultat.setText("❌ Conflit détecté : cette salle est déjà occupée sur ce créneau.");
                msgResultat.setTextFill(Color.RED);
            }
        });

        // Lignes du formulaire
        formCard.getChildren().addAll(
                lblForm,
                creerLigneFormulaire("🏫 Salle :",    comboSalle),
                creerLigneFormulaire("⏰ Créneau :",  comboCreneau),
                creerLigneFormulaire("📅 Date :",     datePicker),
                btnReserver, msgResultat
        );

        page.getChildren().add(formCard);
        return toScroll(page);
    }

    private VBox creerLigneFormulaire(String label, javafx.scene.Node champ) {
        VBox ligne = new VBox(6);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web(TEXTE_GRIS));
        ligne.getChildren().addAll(lbl, champ);
        return ligne;
    }

    // ============================================================
    // VUE MES RÉSERVATIONS
    // ============================================================
    private ScrollPane creerVueMesReservations() {
        VBox page = creerPage("📋 Mes Réservations",
                "Consultez et gérez vos demandes de réservation");

        // Toolbar
        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> comboStatut = new ComboBox<>();
        comboStatut.getItems().addAll("Tous", "En_attente", "Confirme", "Annule");
        comboStatut.setValue("Tous"); comboStatut.setPrefHeight(36);
        comboStatut.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        Button btnFiltrer  = creerBoutonAction("🔍 Filtrer",          ORANGE,    ORANGE_FONC);
        Button btnAnnuler  = creerBoutonAction("❌ Annuler sélection", "#C62828", "#B71C1C");
        Button btnActualiser = creerBoutonAction("🔄 Actualiser",      "#455A64", "#37474F");
        Label  lblResultat = new Label();
        lblResultat.setFont(Font.font("Arial", 12));
        lblResultat.setTextFill(Color.web(TEXTE_GRIS));

        toolbar.getChildren().addAll(
                new Label("Statut :"), comboStatut, btnFiltrer,
                btnAnnuler, btnActualiser, lblResultat
        );

        // Tableau
        TableView<Reservation> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(420);

        TableColumn<Reservation, String> colSalle = new TableColumn<>("Salle");
        colSalle.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getSalle().getNumeroSalle()));

        TableColumn<Reservation, String> colCreneau = new TableColumn<>("Créneau");
        colCreneau.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCreneau().getJour() + "  " +
                        d.getValue().getCreneau().getHeureDebut() + " - " +
                        d.getValue().getCreneau().getHeureFin()));

        TableColumn<Reservation, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDateReservation().toString()));

        TableColumn<Reservation, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getStatut().name()));
        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                String style = "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 4 10;";
                if      (s.equals("Confirme"))   setStyle(style + "-fx-text-fill: #1B5E20;");
                else if (s.equals("Annule"))     setStyle(style + "-fx-text-fill: #B71C1C;");
                else                             setStyle(style + "-fx-text-fill: #E65100;");
            }
        });

        tableau.getColumns().addAll(colSalle, colCreneau, colDate, colStatut);

        ObservableList<Reservation> toutesResas = FXCollections.observableArrayList(
                enseignantConnecte != null ?
                        reservationDAO.trouverParUtilisateur(enseignantConnecte.getId()) :
                        reservationDAO.findAll()
        );
        tableau.setItems(toutesResas);
        lblResultat.setText(toutesResas.size() + " réservation(s)");

        btnFiltrer.setOnAction(e -> {
            String f = comboStatut.getValue();
            if (f.equals("Tous")) {
                tableau.setItems(toutesResas);
            } else {
                tableau.setItems(toutesResas.filtered(r -> r.getStatut().name().equals(f)));
            }
            lblResultat.setText(tableau.getItems().size() + " réservation(s)");
        });

        btnActualiser.setOnAction(e -> {
            toutesResas.setAll(
                    enseignantConnecte != null ?
                            reservationDAO.trouverParUtilisateur(enseignantConnecte.getId()) :
                            reservationDAO.findAll()
            );
            comboStatut.setValue("Tous");
            tableau.setItems(toutesResas);
            lblResultat.setText(toutesResas.size() + " réservation(s)");
        });

        btnAnnuler.setOnAction(e -> {
            Reservation sel = tableau.getSelectionModel().getSelectedItem();
            if (sel == null) { afficherAlerte("⚠ Sélectionnez une réservation."); return; }
            if (sel.getStatut() != StatutReservation.En_attente) {
                afficherAlerte("⚠ Seules les réservations EN ATTENTE peuvent être annulées.");
                return;
            }
            reservationService.annulerReservation(sel.getIdReservation());
            toutesResas.setAll(
                    reservationDAO.trouverParUtilisateur(enseignantConnecte.getId()));
            lblResultat.setText(toutesResas.size() + " réservation(s)");
        });

        page.getChildren().addAll(toolbar, tableau);
        return toScroll(page);
    }

    // ============================================================
    // VUE SALLES DISPONIBLES
    // ============================================================
    private ScrollPane creerVueSallesDisponibles() {
        VBox page = creerPage("🏫 Salles Disponibles",
                "Consultez les salles libres pour un créneau donné");

        HBox filtres = new HBox(12);
        filtres.setAlignment(Pos.CENTER_LEFT);
        filtres.setPadding(new Insets(15));
        filtres.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        ComboBox<CreneauHoraire> comboCren = new ComboBox<>();
        comboCren.getItems().addAll(creneauDAO.findAll());
        comboCren.setPromptText("Choisir un créneau");
        comboCren.setPrefHeight(38); comboCren.setPrefWidth(220);
        comboCren.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Tous", "TD", "TP", "AMPHI");
        comboType.setValue("Tous"); comboType.setPrefHeight(38);
        comboType.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

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
                    .filter(s -> s.getType().name().equals(type))
                    .collect(Collectors.toList());
            tableau.getItems().clear();
            tableau.getItems().addAll(salles);
            lblResultat.setText(salles.size() + " salle(s) disponible(s)");
        });

        filtres.getChildren().addAll(
                new Label("Créneau :"), comboCren,
                new Label("Type :"),   comboType,
                btnRechercher
        );

        page.getChildren().addAll(filtres, lblResultat, tableau);
        return toScroll(page);
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

    private void styliserTableau(TableView<?> tableau) {
        tableau.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );
        tableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableau.getStylesheets().add(
                "data:text/css," +
                        ".table-row-cell:odd{-fx-background-color:#FAF7F2;}" +
                        ".table-row-cell:even{-fx-background-color:white;}" +
                        ".table-row-cell:selected{-fx-background-color:#FFE0CC;}" +
                        ".column-header{-fx-background-color:" + ORANGE + ";}" +
                        ".column-header .label{-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:12px;}"
        );
    }

    private VBox creerCarteStats(String ico, String titre, int val,
                                 String couleur, String bg) {
        VBox carte = new VBox(8);
        carte.setPrefSize(220, 125); carte.setPadding(new Insets(20));
        carte.setStyle(
                "-fx-background-color: " + bg + "; -fx-background-radius: 12;" +
                        "-fx-border-color: " + couleur + "20; -fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );
        Label lblIco = new Label(ico); lblIco.setFont(Font.font("Arial", 26));
        Label lblVal = new Label(String.valueOf(val));
        lblVal.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        lblVal.setTextFill(Color.web(couleur));
        Label lblTit = new Label(titre);
        lblTit.setFont(Font.font("Arial", 13)); lblTit.setTextFill(Color.web(TEXTE_GRIS));
        carte.getChildren().addAll(lblIco, lblVal, lblTit);
        return carte;
    }

    private HBox creerTag(String texte, String couleur) {
        HBox tag = new HBox(5); tag.setAlignment(Pos.CENTER_LEFT);
        tag.setPadding(new Insets(4, 10, 4, 10));
        tag.setStyle(
                "-fx-background-color: " + couleur + "15;" +
                        "-fx-background-radius: 12; -fx-border-color: " + couleur + "40;" +
                        "-fx-border-radius: 12;"
        );
        Label lbl = new Label(texte);
        lbl.setFont(Font.font("Arial", 12));
        lbl.setTextFill(Color.web(couleur));
        tag.getChildren().add(lbl);
        return tag;
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