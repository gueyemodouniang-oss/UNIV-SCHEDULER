package vue.etudiant;

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
import modele.Utilisateurs.Etudiant;
import modele.enums.Jour;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardEtudiantVue extends Application {

    private Etudiant etudiantConnecte;
    private BorderPane root;
    private Button btnActifMenu = null;

    // Couleurs — même thème Blanc cassé + Orange
    private static final String FOND        = "#FAF8F5";
    private static final String FOND_CARD   = "#FFF5EE";
    private static final String ORANGE      = "#E8762A";
    private static final String ORANGE_FONC = "#C85E18";
    private static final String HEADER_COL  = "#BF5515";
    private static final String ACCENT      = "#F4874B";
    private static final String TEXTE       = "#2C2C2C";
    private static final String TEXTE_GRIS  = "#888888";

    // DAOs
    private SeanceDAO         seanceDAO  = new SeanceDAO();
    private SalleDAO          salleDAO   = new SalleDAO();
    private CreneauHoraireDAO creneauDAO = new CreneauHoraireDAO();
    private GroupeDAO         groupeDAO  = new GroupeDAO();

    public DashboardEtudiantVue(Etudiant etudiant) { this.etudiantConnecte = etudiant; }
    public DashboardEtudiantVue() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UNIV-SCHEDULER - Etudiant");
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

        Label badge = new Label("ETUDIANT");
        badge.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + HEADER_COL + ";" +
                        "-fx-font-size: 10px; -fx-font-weight: bold;" +
                        "-fx-padding: 3 10 3 10; -fx-background-radius: 12;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String prenom  = etudiantConnecte != null ? etudiantConnecte.getPrenom() : "";
        String nom     = etudiantConnecte != null ? etudiantConnecte.getNom()    : "Etudiant";
        String niveau  = etudiantConnecte != null && etudiantConnecte.getNiveau() != null
                ? etudiantConnecte.getNiveau() : "";
        String filiere = etudiantConnecte != null && etudiantConnecte.getFiliere() != null
                ? etudiantConnecte.getFiliere() : "";

        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        Label lblNom = new Label(prenom + " " + nom);
        lblNom.setTextFill(Color.WHITE);
        lblNom.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label lblInfo = new Label(filiere + (niveau.isEmpty() ? "" : " — " + niveau));
        lblInfo.setTextFill(Color.web("#FFD9B3"));
        lblInfo.setFont(Font.font("Arial", 11));
        userInfo.getChildren().addAll(lblNom, lblInfo);

        // Avatar initiales
        String ini = (prenom.isEmpty() ? "E" : String.valueOf(prenom.charAt(0))) +
                (nom.isEmpty()    ? "T" : String.valueOf(nom.charAt(0)));
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

        Label lblEDT       = creerLabelSection("EMPLOI DU TEMPS");
        Button btnEDT      = creerBoutonMenu("📅", "Mon EDT");
        Button btnEDTGroupe= creerBoutonMenu("👥", "EDT par Groupe");

        Label lblSalles    = creerLabelSection("SALLES");
        Button btnSallesDispo = creerBoutonMenu("🏫", "Salles Disponibles");
        Button btnRecherche   = creerBoutonMenu("🔍", "Rechercher une Salle");

        btnAccueil    .setOnAction(e -> naviguer(menu, btnAccueil,     creerAccueil()));
        btnEDT        .setOnAction(e -> naviguer(menu, btnEDT,         creerVueEmploiDuTemps()));
        btnEDTGroupe  .setOnAction(e -> naviguer(menu, btnEDTGroupe,   creerVueEmploiParGroupe()));
        btnSallesDispo.setOnAction(e -> naviguer(menu, btnSallesDispo, creerVueSallesDisponibles()));
        btnRecherche  .setOnAction(e -> naviguer(menu, btnRecherche,   creerVueRechercheSalle()));

        activerBtn(menu, btnAccueil);

        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        Label version = new Label("UNIV-SCHEDULER v1.0");
        version.setTextFill(Color.web("#FFD9B3"));
        version.setFont(Font.font("Arial", 10));
        version.setPadding(new Insets(0, 0, 0, 12));

        menu.getChildren().addAll(
                lblPrincipal, btnAccueil,
                lblEDT, btnEDT, btnEDTGroupe,
                lblSalles, btnSallesDispo, btnRecherche,
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

        // Titre
        String prenom = etudiantConnecte != null ? etudiantConnecte.getPrenom() : "Etudiant";
        Label titre = new Label("Bonjour, " + prenom + " 👋");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titre.setTextFill(Color.web(TEXTE));
        Label sousTitre = new Label("Consultez votre emploi du temps et les salles disponibles");
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.web(TEXTE_GRIS));

        // Carte profil étudiant
        if (etudiantConnecte != null) {
            HBox profilCard = new HBox(20);
            profilCard.setPadding(new Insets(20));
            profilCard.setAlignment(Pos.CENTER_LEFT);
            profilCard.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 12;" +
                            "-fx-border-color: " + ORANGE + "30; -fx-border-radius: 12;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
            );

            // Avatar grand
            String ini = String.valueOf(etudiantConnecte.getPrenom().charAt(0)) +
                    String.valueOf(etudiantConnecte.getNom().charAt(0));
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
                    etudiantConnecte.getPrenom() + " " + etudiantConnecte.getNom());
            lblNomComplet.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            lblNomComplet.setTextFill(Color.web(TEXTE));

            HBox tags = new HBox(10);
            tags.getChildren().addAll(
                    creerTag("🆔 " + (etudiantConnecte.getMatricule() != null ?
                            etudiantConnecte.getMatricule() : "—"), ORANGE),
                    creerTag("📚 " + (etudiantConnecte.getFiliere() != null ?
                            etudiantConnecte.getFiliere() : "—"), "#1B5E20"),
                    creerTag("📊 " + (etudiantConnecte.getNiveau() != null ?
                            etudiantConnecte.getNiveau() : "—"), "#0D47A1"),
                    creerTag("📧 " + etudiantConnecte.getEmail(), "#4A148C")
            );
            infos.getChildren().addAll(lblNomComplet, tags);
            profilCard.getChildren().addAll(avatarGrand, infos);
            page.getChildren().addAll(new VBox(4, titre, sousTitre), profilCard);
        } else {
            page.getChildren().addAll(new VBox(4, titre, sousTitre));
        }

        // Cartes stats
        HBox cartes = new HBox(18);
        cartes.getChildren().addAll(
                creerCarteStats("📅", "Séances",  seanceDAO.findAll().size(),  "#1B5E20", "#E8F5E9"),
                creerCarteStats("🏫", "Salles",   salleDAO.findAll().size(),   "#0D47A1", "#E3F2FD"),
                creerCarteStats("👥", "Groupes",  groupeDAO.findAll().size(),  "#4A148C", "#F3E5F5")
        );

        // Accès rapides
        Label lblAcces = new Label("Accès rapides");
        lblAcces.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblAcces.setTextFill(Color.web(TEXTE));

        HBox acces = new HBox(12);
        acces.getChildren().addAll(
                creerBtnAccesRapide("📅 Mon EDT",          () -> naviguerDepuis("Mon EDT",             creerVueEmploiDuTemps())),
                creerBtnAccesRapide("👥 EDT par Groupe",   () -> naviguerDepuis("EDT par Groupe",       creerVueEmploiParGroupe())),
                creerBtnAccesRapide("🏫 Salles Disponibles",() -> naviguerDepuis("Salles Disponibles", creerVueSallesDisponibles())),
                creerBtnAccesRapide("🔍 Rechercher Salle", () -> naviguerDepuis("Rechercher une Salle",creerVueRechercheSalle()))
        );

        page.getChildren().addAll(cartes, lblAcces, acces);
        return toScroll(page);
    }

    private HBox creerTag(String texte, String couleur) {
        HBox tag = new HBox(5);
        tag.setAlignment(Pos.CENTER_LEFT);
        tag.setPadding(new Insets(4, 10, 4, 10));
        tag.setStyle(
                "-fx-background-color: " + couleur + "15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + couleur + "40;" +
                        "-fx-border-radius: 12;"
        );
        Label lbl = new Label(texte);
        lbl.setFont(Font.font("Arial", 12));
        lbl.setTextFill(Color.web(couleur));
        tag.getChildren().add(lbl);
        return tag;
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
        VBox page = creerPage("📅 Mon Emploi du Temps", "Planning hebdomadaire");

        // Filtrer par filière et niveau
        String filiere = etudiantConnecte != null ? etudiantConnecte.getFiliere() : "";
        String niveau  = etudiantConnecte != null ? etudiantConnecte.getNiveau()  : "";
        String codeDept =
                filiere.equals("Math-Info")       ? "MI" :
                        filiere.equals("Génie Info")      ? "GI" :
                                filiere.equals("Physique-Chimie") ? "PC" : "INFO";

        List<Seance> toutesSeances = seanceDAO.findAll();
        List<Seance> mesSeances = toutesSeances.stream()
                .filter(s -> s.getGroupe().getNomGroupe()
                        .startsWith(niveau + "-" + codeDept))
                .collect(Collectors.toList());

        // ── Toolbar ──
        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10, 15, 10, 15));
        toolbar.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );
        Label lblTotal = new Label("📊 " + mesSeances.size() + " séance(s) — " +
                niveau + " " + filiere);
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblTotal.setTextFill(Color.web(TEXTE));
        Region spacerT = new Region(); HBox.setHgrow(spacerT, Priority.ALWAYS);
        Button btnExportPDF = creerBoutonAction("📕 Exporter PDF", "#C62828", "#B71C1C");
        toolbar.getChildren().addAll(lblTotal, spacerT, btnExportPDF);

        // ── Jours et créneaux ──
        Jour[] jours = Jour.values();

        List<String> creneaux = mesSeances.stream()
                .map(s -> s.getCreneau().getHeureDebut() + " - " + s.getCreneau().getHeureFin())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (mesSeances.isEmpty()) {
            VBox vide = new VBox();
            vide.setAlignment(Pos.CENTER); vide.setPadding(new Insets(60));
            vide.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            Label lblVide = new Label("📭  Aucune séance planifiée pour " + niveau + " " + filiere);
            lblVide.setFont(Font.font("Arial", 16));
            lblVide.setTextFill(Color.web(TEXTE_GRIS));
            vide.getChildren().add(lblVide);
            page.getChildren().addAll(toolbar, vide);
            return toScroll(page);
        }

        // ── Construction du tableau ──
        ScrollPane tableauScroll = new ScrollPane();
        tableauScroll.setFitToWidth(true);
        tableauScroll.setFitToHeight(false);
        tableauScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tableauScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tableauScroll.setStyle(
                "-fx-background: " + FOND + "; -fx-background-color: " + FOND + ";"
        );

        GridPane grille = new GridPane();
        grille.setHgap(2); grille.setVgap(2);
        grille.setPadding(new Insets(5));
        grille.setStyle("-fx-background-color: #E0D8CC;");

        double largeurCreneau = 90;
        double largeurJour    = 130;
        double hauteurLigne   = 75;

        // Coin haut-gauche
        StackPane coinVide = new StackPane();
        coinVide.setPrefSize(largeurCreneau, 50);
        coinVide.setStyle("-fx-background-color: " + HEADER_COL + ";");
        Label lblEDT = new Label("EDT");
        lblEDT.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblEDT.setTextFill(Color.WHITE);
        coinVide.getChildren().add(lblEDT);
        grille.add(coinVide, 0, 0);

        // En-têtes jours
        for (int j = 0; j < jours.length; j++) {
            final int jIndex = j;
            StackPane headerJour = new StackPane();
            headerJour.setPrefSize(largeurJour, 50);
            headerJour.setStyle("-fx-background-color: " + HEADER_COL + ";");
            VBox contenu = new VBox(3);
            contenu.setAlignment(Pos.CENTER);
            Label lblJour = new Label(jours[j].name());
            lblJour.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            lblJour.setTextFill(Color.WHITE);

            long nb = mesSeances.stream()
                    .filter(s -> s.getCreneau().getJour() == jours[jIndex]).count();
            if (nb > 0) {
                Label lblNb = new Label(nb + " séance(s)");
                lblNb.setFont(Font.font("Arial", 9));
                lblNb.setTextFill(Color.web("#FFD9B3"));
                contenu.getChildren().addAll(lblJour, lblNb);
            } else {
                contenu.getChildren().add(lblJour);
            }
            headerJour.getChildren().add(contenu);
            grille.add(headerJour, j + 1, 0);
        }

        // Lignes créneaux
        for (int c = 0; c < creneaux.size(); c++) {
            String creneau = creneaux.get(c);
            String[] parts = creneau.split(" - ");
            String debut   = parts[0];
            String fin     = parts[1];
            String bgLigne = (c % 2 == 0) ? "#FAF8F5" : "#FFF5EE";

            StackPane cellCreneau = new StackPane();
            cellCreneau.setPrefSize(largeurCreneau, hauteurLigne);
            cellCreneau.setStyle("-fx-background-color: " + FOND_CARD + ";");
            VBox heureBox = new VBox(4);
            heureBox.setAlignment(Pos.CENTER);
            Label lblDebut = new Label(debut);
            lblDebut.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            lblDebut.setTextFill(Color.web(ORANGE));
            Label lblSep = new Label("│");
            lblSep.setTextFill(Color.web("#D7CCC8"));
            Label lblFin = new Label(fin);
            lblFin.setFont(Font.font("Arial", 12));
            lblFin.setTextFill(Color.web(TEXTE_GRIS));
            heureBox.getChildren().addAll(lblDebut, lblSep, lblFin);
            cellCreneau.getChildren().add(heureBox);
            grille.add(cellCreneau, 0, c + 1);

            for (int j = 0; j < jours.length; j++) {
                final Jour jour = jours[j];

                List<Seance> seancesTrouvees = mesSeances.stream()
                        .filter(s ->
                                s.getCreneau().getJour() == jour &&
                                        s.getCreneau().getHeureDebut().toString().equals(debut) &&
                                        s.getCreneau().getHeureFin().toString().equals(fin))
                        .collect(Collectors.toList());

                StackPane cellule = new StackPane();
                cellule.setPrefSize(largeurJour, hauteurLigne);

                if (seancesTrouvees.isEmpty()) {
                    cellule.setStyle("-fx-background-color: " + bgLigne + ";");
                    Label lblVide2 = new Label("—");
                    lblVide2.setTextFill(Color.web("#D7CCC8"));
                    lblVide2.setFont(Font.font("Arial", 16));
                    cellule.getChildren().add(lblVide2);
                } else {
                    Seance s = seancesTrouvees.get(0);
                    cellule.setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-border-color: " + ORANGE + ";" +
                                    "-fx-border-width: 0 0 0 3;"
                    );
                    VBox contenuCellule = new VBox(4);
                    contenuCellule.setPadding(new Insets(8, 10, 8, 12));
                    contenuCellule.setAlignment(Pos.TOP_LEFT);
                    contenuCellule.setMaxWidth(largeurJour - 5);

                    Label lblMat = new Label(s.getCours().getMatiere().getNomMatiere());
                    lblMat.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    lblMat.setTextFill(Color.web(TEXTE));
                    lblMat.setWrapText(true);

                    Label lblEns = new Label("👤 " + s.getCours().getEnseignant().getNom());
                    lblEns.setFont(Font.font("Arial", 10));
                    lblEns.setTextFill(Color.web(TEXTE_GRIS));

                    Label lblSalle = new Label("🏫 " + s.getSalle().getNumeroSalle());
                    lblSalle.setFont(Font.font("Arial", 10));
                    lblSalle.setTextFill(Color.web(TEXTE_GRIS));

                    Label lblGrp = new Label("👥 " + s.getGroupe().getNomGroupe());
                    lblGrp.setFont(Font.font("Arial", 10));
                    lblGrp.setTextFill(Color.web(ORANGE));

                    contenuCellule.getChildren().addAll(lblMat, lblEns, lblSalle, lblGrp);
                    cellule.getChildren().add(contenuCellule);
                    StackPane.setAlignment(contenuCellule, Pos.TOP_LEFT);

                    cellule.setOnMouseEntered(ev -> cellule.setStyle(
                            "-fx-background-color: " + FOND_CARD + ";" +
                                    "-fx-border-color: " + ORANGE + ";" +
                                    "-fx-border-width: 0 0 0 3;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);"
                    ));
                    cellule.setOnMouseExited(ev -> cellule.setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-border-color: " + ORANGE + ";" +
                                    "-fx-border-width: 0 0 0 3;"
                    ));
                }
                grille.add(cellule, j + 1, c + 1);
            }
        }

        tableauScroll.setContent(grille);
        tableauScroll.setPrefHeight(Math.min(hauteurLigne * creneaux.size() + 60, 500));

        // ── Export PDF ──
        btnExportPDF.setOnAction(e -> {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setInitialFileName("EDT_" +
                    (etudiantConnecte != null ? etudiantConnecte.getMatricule() : "etudiant") +
                    "_" + LocalDate.now() + ".pdf");
            fc.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("PDF", "*.pdf"));
            java.io.File fichier = fc.showSaveDialog(btnExportPDF.getScene().getWindow());
            if (fichier == null) return;

            try {
                com.itextpdf.text.Document doc =
                        new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
                com.itextpdf.text.pdf.PdfWriter.getInstance(doc,
                        new java.io.FileOutputStream(fichier));
                doc.open();

                com.itextpdf.text.Font fontTitre = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
                        com.itextpdf.text.Font.BOLD,
                        new com.itextpdf.text.BaseColor(191, 85, 21));
                com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
                com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA, 10,
                        com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontWhite = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
                        com.itextpdf.text.Font.BOLD,
                        com.itextpdf.text.BaseColor.WHITE);
                com.itextpdf.text.Font fontOrange = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA, 9,
                        com.itextpdf.text.Font.BOLD,
                        new com.itextpdf.text.BaseColor(232, 118, 42));

                com.itextpdf.text.Paragraph titre =
                        new com.itextpdf.text.Paragraph(
                                "EMPLOI DU TEMPS — " + niveau + " " + filiere, fontTitre);
                titre.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                doc.add(titre);

                String nomEtu = etudiantConnecte != null ?
                        etudiantConnecte.getPrenom() + " " + etudiantConnecte.getNom() : "Etudiant";
                com.itextpdf.text.Paragraph infos = new com.itextpdf.text.Paragraph(
                        "Etudiant : " + nomEtu +
                                "   |   Matricule : " + (etudiantConnecte != null &&
                                etudiantConnecte.getMatricule() != null ?
                                etudiantConnecte.getMatricule() : "-") +
                                "   |   Filiere : " + filiere +
                                "   |   Niveau : " + niveau +
                                "   |   Date : " + LocalDate.now(), fontNormal);
                infos.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                infos.setSpacingAfter(15);
                doc.add(infos);

                com.itextpdf.text.BaseColor couleurHeader =
                        new com.itextpdf.text.BaseColor(191, 85, 21);
                com.itextpdf.text.BaseColor couleurLigne1 =
                        new com.itextpdf.text.BaseColor(250, 248, 245);
                com.itextpdf.text.BaseColor couleurLigne2 =
                        new com.itextpdf.text.BaseColor(255, 245, 238);

                Jour[] joursPDF = Jour.values();
                com.itextpdf.text.pdf.PdfPTable table =
                        new com.itextpdf.text.pdf.PdfPTable(joursPDF.length + 1);
                table.setWidthPercentage(100);
                float[] largeurs = new float[joursPDF.length + 1];
                largeurs[0] = 8f;
                for (int i = 1; i <= joursPDF.length; i++) largeurs[i] = 13f;
                table.setWidths(largeurs);

                com.itextpdf.text.pdf.PdfPCell cellEDT =
                        new com.itextpdf.text.pdf.PdfPCell(
                                new com.itextpdf.text.Phrase("Horaire", fontWhite));
                cellEDT.setBackgroundColor(couleurHeader);
                cellEDT.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                cellEDT.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                cellEDT.setPadding(8);
                table.addCell(cellEDT);

                for (Jour jour : joursPDF) {
                    long nb = mesSeances.stream()
                            .filter(s -> s.getCreneau().getJour() == jour).count();
                    com.itextpdf.text.pdf.PdfPCell cellJour =
                            new com.itextpdf.text.pdf.PdfPCell();
                    cellJour.setBackgroundColor(couleurHeader);
                    cellJour.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cellJour.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                    cellJour.setPadding(6);
                    com.itextpdf.text.Paragraph pJour =
                            new com.itextpdf.text.Paragraph(jour.name(), fontWhite);
                    pJour.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    if (nb > 0) pJour.add("\n" + nb + " seance(s)");
                    cellJour.addElement(pJour);
                    table.addCell(cellJour);
                }

                List<String> creneauxPDF = mesSeances.stream()
                        .map(s -> s.getCreneau().getHeureDebut() +
                                " - " + s.getCreneau().getHeureFin())
                        .distinct().sorted().collect(Collectors.toList());

                for (int c = 0; c < creneauxPDF.size(); c++) {
                    String creneau = creneauxPDF.get(c);
                    String[] parts = creneau.split(" - ");
                    String debut = parts[0]; String fin = parts[1];
                    com.itextpdf.text.BaseColor bgLigne =
                            (c % 2 == 0) ? couleurLigne1 : couleurLigne2;

                    com.itextpdf.text.pdf.PdfPCell cellCren =
                            new com.itextpdf.text.pdf.PdfPCell();
                    cellCren.setBackgroundColor(
                            new com.itextpdf.text.BaseColor(255, 245, 238));
                    cellCren.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cellCren.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                    cellCren.setPadding(5);
                    com.itextpdf.text.Paragraph pCren = new com.itextpdf.text.Paragraph();
                    pCren.add(new com.itextpdf.text.Chunk(debut + "\n", fontOrange));
                    pCren.add(new com.itextpdf.text.Chunk(fin, fontNormal));
                    pCren.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cellCren.addElement(pCren);
                    table.addCell(cellCren);

                    for (Jour jour : joursPDF) {
                        List<Seance> trouvees = mesSeances.stream()
                                .filter(s ->
                                        s.getCreneau().getJour() == jour &&
                                                s.getCreneau().getHeureDebut().toString().equals(debut) &&
                                                s.getCreneau().getHeureFin().toString().equals(fin))
                                .collect(Collectors.toList());

                        com.itextpdf.text.pdf.PdfPCell cellSeance =
                                new com.itextpdf.text.pdf.PdfPCell();
                        cellSeance.setPadding(5);
                        cellSeance.setMinimumHeight(55);

                        if (trouvees.isEmpty()) {
                            cellSeance.setBackgroundColor(bgLigne);
                            cellSeance.addElement(
                                    new com.itextpdf.text.Paragraph("—", fontNormal));
                        } else {
                            Seance s = trouvees.get(0);
                            cellSeance.setBackgroundColor(
                                    new com.itextpdf.text.BaseColor(255, 255, 255));
                            cellSeance.setBorderColorLeft(
                                    new com.itextpdf.text.BaseColor(232, 118, 42));
                            cellSeance.setBorderWidthLeft(3f);
                            com.itextpdf.text.Paragraph pSeance =
                                    new com.itextpdf.text.Paragraph();
                            pSeance.add(new com.itextpdf.text.Chunk(
                                    s.getCours().getMatiere().getNomMatiere() + "\n", fontBold));
                            pSeance.add(new com.itextpdf.text.Chunk(
                                    s.getCours().getEnseignant().getNom() + " " +
                                            s.getCours().getEnseignant().getPrenom() + "\n", fontNormal));
                            pSeance.add(new com.itextpdf.text.Chunk(
                                    "Salle " + s.getSalle().getNumeroSalle() + "\n", fontNormal));
                            pSeance.add(new com.itextpdf.text.Chunk(
                                    s.getGroupe().getNomGroupe(), fontOrange));
                            cellSeance.addElement(pSeance);
                        }
                        table.addCell(cellSeance);
                    }
                }

                doc.add(table);

                com.itextpdf.text.Paragraph footer =
                        new com.itextpdf.text.Paragraph(
                                "\nGenere par UNIV-SCHEDULER — " + LocalDate.now(),
                                new com.itextpdf.text.Font(
                                        com.itextpdf.text.Font.FontFamily.HELVETICA, 8,
                                        com.itextpdf.text.Font.ITALIC,
                                        com.itextpdf.text.BaseColor.GRAY));
                footer.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                doc.add(footer);
                doc.close();
                afficherSucces("✅ Export PDF réussi !\n📁 " + fichier.getAbsolutePath());

            } catch (Exception ex) {
                ex.printStackTrace();
                afficherAlerte("❌ Erreur export PDF : " + ex.getMessage());
            }
        });

        page.getChildren().addAll(toolbar, tableauScroll);
        return toScroll(page);
    }


    // Helper alerte succès
    private void afficherSucces(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    // ============================================================
    // VUE EDT PAR GROUPE — planning visuel par jour
    // ============================================================
    private ScrollPane creerVueEmploiParGroupe() {
        VBox page = creerPage("👥 Emploi du Temps par Groupe",
                "Sélectionnez un groupe pour voir son planning");

        // Sélecteur groupe
        HBox selGroupe = new HBox(12);
        selGroupe.setAlignment(Pos.CENTER_LEFT);
        selGroupe.setPadding(new Insets(15));
        selGroupe.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        ComboBox<Groupe> comboGroupe = new ComboBox<>();
        comboGroupe.getItems().addAll(groupeDAO.findAll());
        comboGroupe.setPromptText("Choisir un groupe");
        comboGroupe.setPrefHeight(38); comboGroupe.setPrefWidth(220);
        comboGroupe.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        Button btnAfficher = creerBoutonAction("📅 Afficher le planning", ORANGE, ORANGE_FONC);
        Label lblResultat = new Label();
        lblResultat.setFont(Font.font("Arial", 12));
        lblResultat.setTextFill(Color.web(TEXTE_GRIS));

        selGroupe.getChildren().addAll(
                new Label("Groupe :"), comboGroupe, btnAfficher, lblResultat);
        if (etudiantConnecte != null && etudiantConnecte.getGroupeId() > 0) {
            comboGroupe.getItems().stream()
                    .filter(g -> g.getIdGroupe() == etudiantConnecte.getGroupeId())
                    .findFirst().ifPresent(g -> {
                        comboGroupe.setValue(g);
                        // Afficher automatiquement après que la vue soit chargée
                        javafx.application.Platform.runLater(() -> btnAfficher.fire());
                    });
        }

        // Sélecteur jour (caché au départ)
        HBox selectorJour = new HBox(8);
        selectorJour.setAlignment(Pos.CENTER_LEFT);
        selectorJour.setVisible(false);

        VBox contenuPlanning = new VBox(10);

        String styleJourNormal = "-fx-background-color: white; -fx-text-fill: " + TEXTE + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20;" +
                "-fx-border-color: #D7CCC8; -fx-border-radius: 20; -fx-cursor: hand;" +
                "-fx-padding: 6 16 6 16;";
        String styleJourActif = "-fx-background-color: " + ORANGE + "; -fx-text-fill: white;" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20;" +
                "-fx-border-color: " + ORANGE + "; -fx-border-radius: 20; -fx-cursor: hand;" +
                "-fx-padding: 6 16 6 16;";

        btnAfficher.setOnAction(e -> {
            if (comboGroupe.getValue() == null) {
                afficherAlerte("⚠ Sélectionnez un groupe."); return;
            }

            Groupe groupe = comboGroupe.getValue();
            List<Seance> seancesGroupe = seanceDAO.findAll().stream()
                    .filter(s -> s.getGroupe().getIdGroupe() == groupe.getIdGroupe())
                    .collect(Collectors.toList());

            lblResultat.setText(seancesGroupe.size() + " séance(s) trouvée(s)");

            // Construire sélecteur jours
            selectorJour.getChildren().clear();
            selectorJour.setVisible(true);
            contenuPlanning.getChildren().clear();

            for (Jour jour : Jour.values()) {
                long nbJour = seancesGroupe.stream()
                        .filter(s -> s.getCreneau().getJour() == jour).count();
                if (nbJour == 0) continue; // Cacher jours sans séances

                Button btnJour = new Button(jour.name() + " (" + nbJour + ")");
                btnJour.setStyle(styleJourNormal);

                btnJour.setOnAction(ev -> {
                    selectorJour.getChildren().forEach(n -> {
                        if (n instanceof Button) ((Button)n).setStyle(styleJourNormal);
                    });
                    btnJour.setStyle(styleJourActif);

                    List<Seance> seancesJour = seancesGroupe.stream()
                            .filter(s -> s.getCreneau().getJour() == jour)
                            .sorted((a, b) -> a.getCreneau().getHeureDebut()
                                    .compareTo(b.getCreneau().getHeureDebut()))
                            .collect(Collectors.toList());

                    contenuPlanning.getChildren().clear();

                    HBox entete = new HBox(10);
                    Label lblJT = new Label(jour.name() + " — " + groupe.getNomGroupe());
                    lblJT.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                    lblJT.setTextFill(Color.web(ORANGE));
                    entete.getChildren().add(lblJT);
                    contenuPlanning.getChildren().add(entete);

                    for (Seance s : seancesJour) {
                        contenuPlanning.getChildren().add(creerCarteSeance(s));
                    }
                });
                selectorJour.getChildren().add(btnJour);
            }

            // Cliquer premier jour automatiquement
            if (!selectorJour.getChildren().isEmpty()) {
                ((Button) selectorJour.getChildren().get(0)).fire();
            } else {
                contenuPlanning.getChildren().clear();
                VBox vide = new VBox();
                vide.setAlignment(Pos.CENTER); vide.setPadding(new Insets(50));
                vide.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                Label lblVide = new Label("📭  Aucune séance pour ce groupe");
                lblVide.setFont(Font.font("Arial", 14)); lblVide.setTextFill(Color.web(TEXTE_GRIS));
                vide.getChildren().add(lblVide);
                contenuPlanning.getChildren().add(vide);
            }
        });

        page.getChildren().addAll(selGroupe, selectorJour, contenuPlanning);
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
        heureBox.setStyle("-fx-background-color: " + FOND_CARD + "; -fx-background-radius: 8;");
        Label lblDebut = new Label(s.getCreneau().getHeureDebut().toString());
        lblDebut.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblDebut.setTextFill(Color.web(ORANGE));
        Label lblFin = new Label(s.getCreneau().getHeureFin().toString());
        lblFin.setFont(Font.font("Arial", 12)); lblFin.setTextFill(Color.web(TEXTE_GRIS));
        heureBox.getChildren().addAll(lblDebut, new Label("│"), lblFin);

        VBox infoBox = new VBox(6);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        Label lblMatiere = new Label(s.getCours().getMatiere().getNomMatiere());
        lblMatiere.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        lblMatiere.setTextFill(Color.web(TEXTE));

        HBox tags = new HBox(10);
        tags.getChildren().addAll(
                creerInfoTag("👤", s.getCours().getEnseignant().getNom()),
                creerInfoTag("👥", s.getGroupe().getNomGroupe()),
                creerInfoTag("🏫", s.getSalle().getNumeroSalle()),
                creerInfoTag("📅", s.getCreneau().getJour().toString())
        );
        infoBox.getChildren().addAll(lblMatiere, tags);
        carte.getChildren().addAll(heureBox, infoBox);
        return carte;
    }

    // ============================================================
    // VUE SALLES DISPONIBLES
    // ============================================================
    private ScrollPane creerVueSallesDisponibles() {
        VBox page = creerPage("🏫 Salles Disponibles",
                "Trouvez les salles libres pour un créneau donné");

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
                new Label("Type :"), comboType,
                btnRechercher
        );

        page.getChildren().addAll(filtres, lblResultat, tableau);
        return toScroll(page);
    }

    // ============================================================
    // VUE RECHERCHE SALLE
    // ============================================================
    private ScrollPane creerVueRechercheSalle() {
        VBox page = creerPage("🔍 Rechercher une Salle",
                "Filtrez les salles par capacité, type ou bâtiment");

        // Carte filtres
        VBox filtresCard = new VBox(15);
        filtresCard.setPadding(new Insets(20));
        filtresCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10;" +
                        "-fx-border-color: #E0D8CC; -fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 6, 0, 0, 2);"
        );

        Label lblFiltres = new Label("🔎 Filtres de recherche");
        lblFiltres.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblFiltres.setTextFill(Color.web(TEXTE));

        // Ligne filtres
        TextField champNom = creerChamp("🔍  Rechercher par numéro...");
        champNom.setPrefWidth(200);

        TextField champCapacite = creerChamp("Capacité minimum");
        champCapacite.setPrefWidth(150);

        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Tous", "TD", "TP", "AMPHI");
        comboType.setValue("Tous"); comboType.setPrefHeight(38);
        comboType.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #D7CCC8;"
        );

        Button btnRechercher = creerBoutonAction("🔍 Rechercher", ORANGE, ORANGE_FONC);
        Button btnReset      = creerBoutonAction("✕ Réinitialiser", "#757575", "#616161");

        HBox ligneFiltres = new HBox(12,
                new Label("Numéro :"), champNom,
                new Label("Capacité min :"), champCapacite,
                new Label("Type :"), comboType,
                btnRechercher, btnReset
        );
        ligneFiltres.setAlignment(Pos.CENTER_LEFT);

        Label lblResultat = new Label();
        lblResultat.setFont(Font.font("Arial", 12));
        lblResultat.setTextFill(Color.web(TEXTE_GRIS));

        filtresCard.getChildren().addAll(lblFiltres, ligneFiltres, lblResultat);

        // Tableau
        TableView<Salle> tableau = new TableView<>();
        styliserTableau(tableau); tableau.setPrefHeight(400);

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
        ObservableList<Salle> toutesSalles =
                FXCollections.observableArrayList(salleDAO.findAll());
        tableau.setItems(toutesSalles);

        // Recherche dynamique par nom
        champNom.textProperty().addListener((o, v, n) -> {
            if (n.isEmpty()) { tableau.setItems(toutesSalles); return; }
            tableau.setItems(toutesSalles.filtered(s ->
                    s.getNumeroSalle().toLowerCase().contains(n.toLowerCase())));
        });

        btnRechercher.setOnAction(e -> {
            List<Salle> salles = salleDAO.findAll();

            if (!champCapacite.getText().isEmpty()) {
                try {
                    int cap = Integer.parseInt(champCapacite.getText());
                    salles = salles.stream()
                            .filter(s -> s.getCapacite() >= cap).collect(Collectors.toList());
                } catch (NumberFormatException ex) {
                    afficherAlerte("⚠ Capacité invalide."); return;
                }
            }

            if (!comboType.getValue().equals("Tous")) {
                String t = comboType.getValue();
                salles = salles.stream()
                        .filter(s -> s.getType().name().equals(t)).collect(Collectors.toList());
            }

            if (!champNom.getText().isEmpty()) {
                String n = champNom.getText().toLowerCase();
                salles = salles.stream()
                        .filter(s -> s.getNumeroSalle().toLowerCase().contains(n))
                        .collect(Collectors.toList());
            }

            tableau.getItems().clear();
            tableau.getItems().addAll(salles);
            lblResultat.setText(salles.size() + " salle(s) trouvée(s)");
        });

        btnReset.setOnAction(e -> {
            champNom.clear(); champCapacite.clear();
            comboType.setValue("Tous"); tableau.setItems(toutesSalles);
            lblResultat.setText("");
        });

        page.getChildren().addAll(filtresCard, tableau);
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
        sep.setPadding(new Insets(3, 0, 3, 0));
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
        lblTit.setFont(Font.font("Arial", 13));
        lblTit.setTextFill(Color.web(TEXTE_GRIS));
        carte.getChildren().addAll(lblIco, lblVal, lblTit);
        return carte;
    }

    private HBox creerInfoTag(String icone, String texte) {
        HBox tag = new HBox(5); tag.setAlignment(Pos.CENTER_LEFT);
        tag.setPadding(new Insets(3, 8, 3, 8));
        tag.setStyle(
                "-fx-background-color: " + FOND_CARD + "; -fx-background-radius: 12;"
        );
        Label lblIco = new Label(icone); lblIco.setFont(Font.font("Arial", 12));
        Label lblTxt = new Label(texte);
        lblTxt.setFont(Font.font("Arial", 12)); lblTxt.setTextFill(Color.web(TEXTE));
        tag.getChildren().addAll(lblIco, lblTxt);
        return tag;
    }

    private TextField creerChamp(String prompt) {
        TextField tf = new TextField(); tf.setPromptText(prompt); tf.setPrefHeight(38);
        tf.setStyle(
                "-fx-background-radius: 7; -fx-border-radius: 7;" +
                        "-fx-border-color: #D7CCC8; -fx-border-width: 1;" +
                        "-fx-padding: 5 12; -fx-font-size: 13px;"
        );
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
        sp.setStyle(
                "-fx-background: " + FOND + "; -fx-background-color: " + FOND + ";"
        );
        return sp;
    }

    private void afficherAlerte(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}