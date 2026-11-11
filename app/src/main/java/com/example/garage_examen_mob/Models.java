package com.example.garage_examen_mob;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Models {

    // =========================================================
    // USER / LOGIN
    // =========================================================
    public static class User {
        @SerializedName("id")       public int id;
        @SerializedName("username") public String username;
        @SerializedName("role")     public String role;
    }

    public static class LoginRes {
        @SerializedName("user")  public User user;
        @SerializedName("token") public String token;
    }

    // =========================================================
    // MECANICIEN
    // =========================================================
    public static class Mecanicien {
        @SerializedName("id")            public int id;
        @SerializedName("nom")           public String nom;
        @SerializedName("prenom")        public String prenom;
        @SerializedName("telephone")     public String telephone;
        @SerializedName("specialite")    public String specialite;
        @SerializedName("date_embauche") public String dateEmbauche;

        public Mecanicien() {}
        public Mecanicien(String n, String p, String t, String s, String d) {
            nom = n; prenom = p; telephone = t; specialite = s; dateEmbauche = d;
        }
    }

    // =========================================================
    // CLIENT
    // =========================================================
    public static class Client {
        @SerializedName("id")        public int id;
        @SerializedName("nom")       public String nom;
        @SerializedName("prenom")    public String prenom;
        @SerializedName("telephone") public String telephone;
        @SerializedName("email")     public String email;

        @Override public String toString() { return prenom + " " + nom; }
    }

    // =========================================================
    // VEHICULE
    // =========================================================
    public static class Vehicule {
        @SerializedName("id")              public int id;
        @SerializedName("immatriculation") public String immatriculation;
        @SerializedName("marque")          public String marque;
        @SerializedName("modele")          public String modele;
        @SerializedName("annee")           public int annee;
        @SerializedName("kilometrage")     public int kilometrage;
        @SerializedName("client_id")       public int clientId;
        @SerializedName("client")          public String client;

        public Vehicule() {}
        public Vehicule(String i, String ma, String mo, int a, int k, int cid) {
            immatriculation = i; marque = ma; modele = mo;
            annee = a; kilometrage = k; clientId = cid;
        }

        @Override public String toString() {
            return immatriculation + " — " + marque + " " + modele;
        }
    }

    // =========================================================
    // INTERVENTION
    // =========================================================
    public static class Intervention {
        @SerializedName("id")                public int id;
        @SerializedName("vehicule_id")       public int vehiculeId;
        @SerializedName("immatriculation")   public String immatriculation;
        @SerializedName("marque")            public String marque;
        @SerializedName("modele")            public String modele;
        @SerializedName("mecanicien_id")     public int mecanicienId;
        @SerializedName("mecanicien")        public String mecanicien;
        @SerializedName("date_intervention") public String dateIntervention;
        @SerializedName("description")       public String description;
        @SerializedName("cout")              public double cout;
        @SerializedName("statut")            public String statut;

        public Intervention() {}
        public Intervention(int v, int m, String d, String desc, double c, String s) {
            vehiculeId = v; mecanicienId = m; dateIntervention = d;
            description = desc; cout = c; statut = s;
        }
    }

    // =========================================================
    // STATISTIQUES
    // =========================================================
    public static class Statistiques {
        @SerializedName("totaux")                       public Totaux totaux;
        @SerializedName("interventions_par_statut")     public List<StatutCount> parStatut;
        @SerializedName("interventions_par_mecanicien") public List<MecaCount> parMecano;
        @SerializedName("chiffre_affaires_par_mois")    public List<MoisCA> caParMois;
        @SerializedName("marques_les_plus_frequentes")  public List<MarqueCount> marques;

        public static class Totaux {
            @SerializedName("mecaniciens")      public int mecanicien;
            @SerializedName("clients")          public int clients;
            @SerializedName("vehicules")        public int vehicules;
            @SerializedName("interventions")    public int interventions;
            @SerializedName("chiffre_affaires") public double chiffreAffaires;
        }
        public static class StatutCount { public String statut; public int nombre; }
        public static class MecaCount   { public int id; public String mecanicien; public int nombre; }
        public static class MoisCA      { public String mois; public double total; }
        public static class MarqueCount { public String marque; public int nombre; }
    }
}