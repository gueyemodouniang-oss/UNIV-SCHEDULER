package vue.auth;

import dao.impl.UtilisateurDAO;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.Utilisateurs.*;
import modele.enums.Role;
import vue.admin.DashboardAdminVue;
import vue.enseignant.DashboardEnseignantVue;
import vue.etudiant.DashboardEtudiantVue;
import vue.gestionnaire.DashboardGestionnaireVue;

public class LoginVue extends Application {

    private static final String ORANGE      = "#E8762A";
    private static final String ORANGE_FONC = "#C85E18";
    private static final String HEADER_COL  = "#BF5515";
    private static final String FOND        = "#FAF8F5";
    private static final String TEXTE       = "#2C2C2C";
    private static final String TEXTE_GRIS  = "#888888";

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("UNIV-SCHEDULER - Connexion");

        // ============================================================
        // PANNEAU GAUCHE — branding
        // ============================================================
        VBox panneauGauche = new VBox(30);
        panneauGauche.setPrefWidth(340);
        panneauGauche.setAlignment(Pos.CENTER);
        panneauGauche.setPadding(new Insets(50, 40, 50, 40));
        panneauGauche.setStyle(
                "-fx-background-color: linear-gradient(" +
                        "from 0% 0% to 100% 100%, " +
                        HEADER_COL + ", " + ORANGE + ");"
        );

        // Logo / icône
        Label icone = new Label("🎓");
        icone.setFont(Font.font("Arial", 64));

        Label titrePrincipal = new Label("UNIV\nSCHEDULER");
        titrePrincipal.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titrePrincipal.setTextFill(Color.WHITE);
        titrePrincipal.setTextAlignment(TextAlignment.CENTER);
        titrePrincipal.setAlignment(Pos.CENTER);

        Label sousTitre = new Label("Gestion des Salles\net Emplois du Temps");
        sousTitre.setFont(Font.font("Arial", 14));
        sousTitre.setTextFill(Color.web("#FFD9B3"));
        sousTitre.setTextAlignment(TextAlignment.CENTER);
        sousTitre.setAlignment(Pos.CENTER);

        // Séparateur décoratif
        HBox separateur = new HBox();
        separateur.setPrefHeight(2);
        separateur.setPrefWidth(80);
        separateur.setMaxWidth(80);
        separateur.setStyle("-fx-background-color: rgba(255,255,255,0.4); -fx-background-radius: 2;");

        // Badges rôles
        VBox badges = new VBox(10);
        badges.setAlignment(Pos.CENTER);
        badges.getChildren().addAll(
                creerBadgeRole("👑", "Administrateur"),
                creerBadgeRole("📋", "Gestionnaire"),
                creerBadgeRole("📚", "Enseignant"),
                creerBadgeRole("🎓", "Etudiant")
        );

        Label versionLabel = new Label("v1.0 — L2 Informatique");
        versionLabel.setFont(Font.font("Arial", 11));
        versionLabel.setTextFill(Color.web("#FFD9B3"));

        panneauGauche.getChildren().addAll(
                icone, titrePrincipal, sousTitre,
                separateur, badges, versionLabel
        );

        // ============================================================
        // PANNEAU DROIT — formulaire
        // ============================================================
        VBox panneauDroit = new VBox(0);
        panneauDroit.setPrefWidth(420);
        panneauDroit.setAlignment(Pos.CENTER);
        panneauDroit.setStyle("-fx-background-color: " + FOND + ";");

        VBox formContainer = new VBox(22);
        formContainer.setPadding(new Insets(55, 50, 55, 50));
        formContainer.setAlignment(Pos.CENTER_LEFT);

        // En-tête formulaire
        Label lblBienvenue = new Label("Bienvenue 👋");
        lblBienvenue.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblBienvenue.setTextFill(Color.web(TEXTE));

        Label lblInstruction = new Label("Connectez-vous à votre espace");
        lblInstruction.setFont(Font.font("Arial", 13));
        lblInstruction.setTextFill(Color.web(TEXTE_GRIS));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #E0D8CC;");
        sep.setPadding(new Insets(3, 0, 3, 0));

        // Champ Email
        VBox emailGroup = creerGroupeChamp("📧  Email");
        TextField champEmail = creerChampTexte("ex: nom@universite.sn");
        emailGroup.getChildren().add(champEmail);

        // Champ Mot de passe
        VBox mdpGroup = creerGroupeChamp("🔒  Mot de passe");

        // Conteneur mot de passe avec œil
        PasswordField champMdp = new PasswordField();
        champMdp.setPromptText("••••••••");
        champMdp.setPrefHeight(44);
        champMdp.setMaxWidth(Double.MAX_VALUE);
        champMdp.setStyle(styleChampsBase());

        TextField champMdpVisible = new TextField();
        champMdpVisible.setPromptText("••••••••");
        champMdpVisible.setPrefHeight(44);
        champMdpVisible.setMaxWidth(Double.MAX_VALUE);
        champMdpVisible.setStyle(styleChampsBase());
        champMdpVisible.setVisible(false);
        champMdpVisible.setManaged(false);

        Button btnOeil = new Button("👁");
        btnOeil.setStyle(
                "-fx-background-color: transparent; -fx-cursor: hand;" +
                        "-fx-font-size: 16px; -fx-padding: 0 8 0 8;"
        );
        btnOeil.setOnAction(e -> {
            boolean visible = champMdpVisible.isVisible();
            if (visible) {
                champMdp.setText(champMdpVisible.getText());
                champMdp.setVisible(true); champMdp.setManaged(true);
                champMdpVisible.setVisible(false); champMdpVisible.setManaged(false);
                btnOeil.setText("👁");
            } else {
                champMdpVisible.setText(champMdp.getText());
                champMdpVisible.setVisible(true); champMdpVisible.setManaged(true);
                champMdp.setVisible(false); champMdp.setManaged(false);
                btnOeil.setText("🙈");
            }
        });

        StackPane mdpStack = new StackPane();
        StackPane.setAlignment(btnOeil, Pos.CENTER_RIGHT);
        mdpStack.getChildren().addAll(champMdp, champMdpVisible, btnOeil);
        mdpGroup.getChildren().add(mdpStack);

        // Message erreur / succès
        HBox msgBox = new HBox(8);
        msgBox.setAlignment(Pos.CENTER_LEFT);
        msgBox.setPadding(new Insets(10, 15, 10, 15));
        msgBox.setStyle(
                "-fx-background-color: #FFF3E0; -fx-background-radius: 8;" +
                        "-fx-border-color: " + ORANGE + "40; -fx-border-radius: 8;"
        );
        msgBox.setVisible(false); msgBox.setManaged(false);

        Label lblMsg = new Label();
        lblMsg.setFont(Font.font("Arial", 12));
        lblMsg.setWrapText(true);
        msgBox.getChildren().add(lblMsg);

        // Bouton connexion
        Button btnConnexion = new Button("Se connecter →");
        btnConnexion.setPrefWidth(Double.MAX_VALUE);
        btnConnexion.setPrefHeight(46);
        String styleBtnOn  = "-fx-background-color:" + ORANGE + ";-fx-text-fill:white;" +
                "-fx-font-size:14px;-fx-font-weight:bold;" +
                "-fx-background-radius:10;-fx-cursor:hand;";
        String styleBtnHov = "-fx-background-color:" + ORANGE_FONC + ";-fx-text-fill:white;" +
                "-fx-font-size:14px;-fx-font-weight:bold;" +
                "-fx-background-radius:10;-fx-cursor:hand;";
        btnConnexion.setStyle(styleBtnOn);
        btnConnexion.setOnMouseEntered(e -> btnConnexion.setStyle(styleBtnHov));
        btnConnexion.setOnMouseExited(e  -> btnConnexion.setStyle(styleBtnOn));

        // ===== ACTION CONNEXION =====
        Runnable actionConnexion = () -> {
            String email = champEmail.getText().trim();
            String mdp   = champMdp.isVisible() ?
                    champMdp.getText().trim() :
                    champMdpVisible.getText().trim();

            // Validation
            if (email.isEmpty() || mdp.isEmpty()) {
                afficherMsg(msgBox, lblMsg,
                        "⚠  Veuillez remplir tous les champs.", "#E65100");
                agiterChamp(email.isEmpty() ? champEmail : champMdp);
                return;
            }

            // Vérification format email basique
            if (!email.contains("@")) {
                afficherMsg(msgBox, lblMsg,
                        "⚠  Format d'email invalide.", "#E65100");
                agiterChamp(champEmail);
                return;
            }

            // Tentative connexion
            btnConnexion.setText("Connexion...");
            btnConnexion.setDisable(true);

            Utilisateur utilisateur = utilisateurDAO.login(email, mdp);

            if (utilisateur == null) {
                afficherMsg(msgBox, lblMsg,
                        "❌  Email ou mot de passe incorrect.", "#B71C1C");
                champMdp.clear(); champMdpVisible.clear();
                btnConnexion.setText("Se connecter →");
                btnConnexion.setDisable(false);
                return;
            }

            // Succès
            afficherMsg(msgBox, lblMsg,
                    "✅  Connexion réussie ! Redirection...", "#1B5E20");

            // Courte pause puis redirection
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(Duration.millis(700));
            pause.setOnFinished(ev -> {
                primaryStage.close();
                redirigerSelonRole(utilisateur);
            });
            pause.play();
        };

        btnConnexion.setOnAction(e -> actionConnexion.run());
        champMdp.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) actionConnexion.run();
        });
        champMdpVisible.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) actionConnexion.run();
        });
        champEmail.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) champMdp.requestFocus();
        });

        // Footer
        Label lblFooter = new Label("© 2024 UNIV-SCHEDULER — Projet L2 Informatique");
        lblFooter.setFont(Font.font("Arial", 10));
        lblFooter.setTextFill(Color.web("#BBBBBB"));

        formContainer.getChildren().addAll(
                new VBox(4, lblBienvenue, lblInstruction),
                sep,
                emailGroup,
                mdpGroup,
                msgBox,
                btnConnexion,
                lblFooter
        );

        panneauDroit.getChildren().add(formContainer);

        // ============================================================
        // LAYOUT FINAL — côte à côte
        // ============================================================
        HBox root = new HBox();
        root.getChildren().addAll(panneauGauche, panneauDroit);

        // Animation d'entrée
        FadeTransition fade = new FadeTransition(Duration.millis(600), root);
        fade.setFromValue(0); fade.setToValue(1); fade.play();

        Scene scene = new Scene(root, 760, 520);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        champEmail.requestFocus();
    }

    // ============================================================
    // HELPERS
    // ============================================================
    private HBox creerBadgeRole(String ico, String texte) {
        HBox badge = new HBox(10);
        badge.setAlignment(Pos.CENTER_LEFT);
        badge.setPadding(new Insets(7, 14, 7, 14));
        badge.setStyle(
                "-fx-background-color: rgba(255,255,255,0.15);" +
                        "-fx-background-radius: 20; -fx-cursor: default;"
        );
        Label lblIco  = new Label(ico);  lblIco.setFont(Font.font("Arial", 14));
        Label lblTxt  = new Label(texte);
        lblTxt.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lblTxt.setTextFill(Color.WHITE);
        badge.getChildren().addAll(lblIco, lblTxt);
        return badge;
    }

    private VBox creerGroupeChamp(String label) {
        VBox groupe = new VBox(7);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web("#555555"));
        groupe.getChildren().add(lbl);
        return groupe;
    }

    private TextField creerChampTexte(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(44);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle(styleChampsBase());
        // Focus style
        tf.focusedProperty().addListener((o, ov, nv) ->
                tf.setStyle(nv ? styleChampsActif() : styleChampsBase())
        );
        return tf;
    }

    private String styleChampsBase() {
        return "-fx-background-color: white;" +
                "-fx-border-color: #D7CCC8; -fx-border-width: 1.5;" +
                "-fx-border-radius: 9; -fx-background-radius: 9;" +
                "-fx-padding: 5 14; -fx-font-size: 13px;";
    }

    private String styleChampsActif() {
        return "-fx-background-color: white;" +
                "-fx-border-color: " + ORANGE + "; -fx-border-width: 1.5;" +
                "-fx-border-radius: 9; -fx-background-radius: 9;" +
                "-fx-padding: 5 14; -fx-font-size: 13px;";
    }

    private void afficherMsg(HBox box, Label lbl, String msg, String couleur) {
        lbl.setText(msg);
        lbl.setTextFill(Color.web(couleur));
        box.setVisible(true); box.setManaged(true);

        String bg = couleur.equals("#1B5E20") ? "#E8F5E9" :
                couleur.equals("#B71C1C") ? "#FFEBEE" : "#FFF3E0";
        String border = couleur.equals("#1B5E20") ? "#A5D6A7" :
                couleur.equals("#B71C1C") ? "#EF9A9A" : ORANGE + "40";
        box.setStyle(
                "-fx-background-color: " + bg + "; -fx-background-radius: 8;" +
                        "-fx-border-color: " + border + "; -fx-border-radius: 8;"
        );

        FadeTransition ft = new FadeTransition(Duration.millis(300), box);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private void agiterChamp(javafx.scene.Node champ) {
        javafx.animation.TranslateTransition tt =
                new javafx.animation.TranslateTransition(Duration.millis(60), champ);
        tt.setByX(8); tt.setCycleCount(6); tt.setAutoReverse(true); tt.play();
    }

    private void redirigerSelonRole(Utilisateur utilisateur) {
        Role role = utilisateur.getRole();
        try {
            if      (role == Role.Admin)        new DashboardAdminVue(utilisateur).start(new Stage());
            else if (role == Role.Gestionnaire) new DashboardGestionnaireVue(utilisateur).start(new Stage());
            else if (role == Role.Enseignant)   new DashboardEnseignantVue((Enseignant) utilisateur).start(new Stage());
            else if (role == Role.Etudiant)     new DashboardEtudiantVue((Etudiant) utilisateur).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}