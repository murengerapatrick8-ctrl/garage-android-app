package com.example.garage_examen_mob;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends AppCompatActivity {

    private String table;
    private final List<String[]> rows = new ArrayList<>();
    private final List<String[]> filtered = new ArrayList<>();
    private TableAdapter adapter;
    private TextInputEditText etSearch;
    private RecyclerView rv;

    private final List<Models.Mecanicien> mecas = new ArrayList<>();
    private final List<Models.Client> clis = new ArrayList<>();
    private final List<Models.Vehicule> vehis = new ArrayList<>();
    private final List<Models.Intervention> intervs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_data);

        table = getIntent().getStringExtra("table");
        String title = getIntent().getStringExtra("title");
        if (table == null) { finish(); return; }

        // 🛡️ Toolbar sécurisée
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title == null ? "Données" : title);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // 🛡️ Recherche sécurisée
        etSearch = findViewById(R.id.etSearch);
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
                public void onTextChanged(CharSequence s, int a, int b, int c) { filtrer(s.toString()); }
                public void afterTextChanged(Editable s) {}
            });
        }

        // 🛡️ Liste sécurisée
        rv = findViewById(R.id.recycler);
        if (rv != null) {
            rv.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TableAdapter(filtered, this::onRowClick);
            rv.setAdapter(adapter);
        }

        // 🛡️ FAB sécurisé
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        if (fab != null) {
            if ("statistiques".equals(table)) {
                fab.setVisibility(View.GONE);
            } else {
                fab.setOnClickListener(v -> formulaireAjout());
            }
        }

        definirEntetes();
        charger();
    }

    // =========================================================
    // EN-TÊTES DYNAMIQUES
    // =========================================================
    private void definirEntetes() {
        TextView h1 = findViewById(R.id.headerCol1);
        TextView h2 = findViewById(R.id.headerCol2);
        TextView h3 = findViewById(R.id.headerCol3);
        TextView h4 = findViewById(R.id.headerCol4);
        TextView h5 = findViewById(R.id.headerCol5);
        TextView h6 = findViewById(R.id.headerCol6);

        if (h1 == null || h2 == null || h3 == null) return;

        h1.setVisibility(View.VISIBLE);
        h2.setVisibility(View.VISIBLE);
        h3.setVisibility(View.VISIBLE);
        if (h4 != null) h4.setVisibility(View.VISIBLE);
        if (h5 != null) h5.setVisibility(View.VISIBLE);
        if (h6 != null) h6.setVisibility(View.VISIBLE);

        switch (table) {
            case "mecaniciens":
                h1.setText("ID"); h2.setText("Nom complet"); h3.setText("Téléphone");
                if (h4 != null) h4.setText("Spécialité");
                if (h5 != null) h5.setText("Date embauche");
                if (h6 != null) h6.setVisibility(View.GONE);
                break;
            case "clients":
                h1.setText("ID"); h2.setText("Nom complet"); h3.setText("Téléphone");
                if (h4 != null) h4.setText("Email");
                if (h5 != null) h5.setVisibility(View.GONE);
                if (h6 != null) h6.setVisibility(View.GONE);
                break;
            case "vehicules":
                h1.setText("ID"); h2.setText("Immatriculation"); h3.setText("Marque / Modèle");
                if (h4 != null) h4.setText("Année");
                if (h5 != null) h5.setText("Client");
                if (h6 != null) h6.setVisibility(View.GONE);
                break;
            case "interventions":
                h1.setText("ID"); h2.setText("Véhicule"); h3.setText("Mécanicien");
                if (h4 != null) h4.setText("Date");
                if (h5 != null) h5.setText("Coût");
                if (h6 != null) h6.setText("Statut");
                break;
            case "statistiques":
                h1.setText("#"); h2.setText("Indicateur"); h3.setText("Valeur");
                if (h4 != null) h4.setVisibility(View.GONE);
                if (h5 != null) h5.setVisibility(View.GONE);
                if (h6 != null) h6.setVisibility(View.GONE);
                break;
        }
    }

    // =========================================================
    // CHARGEMENT DES DONNÉES
    // =========================================================
    private void charger() {
        rows.clear(); filtered.clear();
        mecas.clear(); clis.clear(); vehis.clear(); intervs.clear();

        switch (table) {
            case "mecaniciens":
                Api.get().getMecaniciens().enqueue(new Callback<Api.Res<List<Models.Mecanicien>>>() {
                    @Override public void onResponse(Call<Api.Res<List<Models.Mecanicien>>> c, Response<Api.Res<List<Models.Mecanicien>>> r) {
                        if (r.isSuccessful() && r.body() != null && r.body().success) {
                            mecas.addAll(r.body().data);
                            for (Models.Mecanicien m : mecas) {
                                rows.add(new String[]{
                                        String.valueOf(m.id), m.prenom + " " + m.nom,
                                        m.telephone == null ? "—" : m.telephone,
                                        m.specialite == null ? "—" : m.specialite,
                                        m.dateEmbauche == null ? "—" : m.dateEmbauche
                                });
                            }
                            filtrer(etSearch != null ? etSearch.getText().toString() : "");
                        } else { toast("Erreur serveur"); }
                    }
                    @Override public void onFailure(Call<Api.Res<List<Models.Mecanicien>>> c, Throwable t) { toast("Erreur réseau : " + t.getMessage()); }
                });
                break;

            case "clients":
                Api.get().getClients().enqueue(new Callback<Api.Res<List<Models.Client>>>() {
                    @Override public void onResponse(Call<Api.Res<List<Models.Client>>> c, Response<Api.Res<List<Models.Client>>> r) {
                        if (r.isSuccessful() && r.body() != null && r.body().success) {
                            clis.addAll(r.body().data);
                            for (Models.Client cl : clis) {
                                rows.add(new String[]{
                                        String.valueOf(cl.id), cl.prenom + " " + cl.nom,
                                        cl.telephone == null ? "—" : cl.telephone,
                                        cl.email == null ? "—" : cl.email
                                });
                            }
                            filtrer(etSearch != null ? etSearch.getText().toString() : "");
                        } else { toast("Erreur serveur"); }
                    }
                    @Override public void onFailure(Call<Api.Res<List<Models.Client>>> c, Throwable t) { toast("Erreur réseau : " + t.getMessage()); }
                });
                break;

            case "vehicules":
                Api.get().getVehicules().enqueue(new Callback<Api.Res<List<Models.Vehicule>>>() {
                    @Override public void onResponse(Call<Api.Res<List<Models.Vehicule>>> c, Response<Api.Res<List<Models.Vehicule>>> r) {
                        if (r.isSuccessful() && r.body() != null && r.body().success) {
                            vehis.addAll(r.body().data);
                            for (Models.Vehicule v : vehis) {
                                rows.add(new String[]{
                                        String.valueOf(v.id), v.immatriculation,
                                        v.marque + " " + v.modele, String.valueOf(v.annee),
                                        v.client == null ? "—" : v.client
                                });
                            }
                            filtrer(etSearch != null ? etSearch.getText().toString() : "");
                        } else { toast("Erreur serveur"); }
                    }
                    @Override public void onFailure(Call<Api.Res<List<Models.Vehicule>>> c, Throwable t) { toast("Erreur réseau : " + t.getMessage()); }
                });
                break;

            case "interventions":
                Api.get().getInterventions().enqueue(new Callback<Api.Res<List<Models.Intervention>>>() {
                    @Override public void onResponse(Call<Api.Res<List<Models.Intervention>>> c, Response<Api.Res<List<Models.Intervention>>> r) {
                        if (r.isSuccessful() && r.body() != null && r.body().success) {
                            intervs.addAll(r.body().data);
                            for (Models.Intervention i : intervs) {
                                rows.add(new String[]{
                                        String.valueOf(i.id),
                                        i.immatriculation == null ? "—" : i.immatriculation,
                                        i.mecanicien == null ? "—" : i.mecanicien,
                                        i.dateIntervention == null ? "—" : i.dateIntervention,
                                        String.format("%,.0f FBu", i.cout),
                                        i.statut == null ? "—" : i.statut
                                });
                            }
                            filtrer(etSearch != null ? etSearch.getText().toString() : "");
                        } else { toast("Erreur serveur"); }
                    }
                    @Override public void onFailure(Call<Api.Res<List<Models.Intervention>>> c, Throwable t) { toast("Erreur réseau : " + t.getMessage()); }
                });
                break;

            case "statistiques":
                Api.get().getStatistiques().enqueue(new Callback<Api.Res<Models.Statistiques>>() {
                    @Override public void onResponse(Call<Api.Res<Models.Statistiques>> c, Response<Api.Res<Models.Statistiques>> r) {
                        if (r.isSuccessful() && r.body() != null && r.body().success) {
                            Models.Statistiques.Totaux t = r.body().data.totaux;
                            rows.add(new String[]{"1", "Mécaniciens", String.valueOf(t.mecanicien), "", "", ""});
                            rows.add(new String[]{"2", "Clients", String.valueOf(t.clients), "", "", ""});
                            rows.add(new String[]{"3", "Véhicules", String.valueOf(t.vehicules), "", "", ""});
                            rows.add(new String[]{"4", "Interventions", String.valueOf(t.interventions), "", "", ""});
                            rows.add(new String[]{"5", "Chiffre d'aff.", String.format("%,.0f FBu", t.chiffreAffaires), "", "", ""});
                            filtrer(etSearch != null ? etSearch.getText().toString() : "");
                        } else { toast("Erreur serveur"); }
                    }
                    @Override public void onFailure(Call<Api.Res<Models.Statistiques>> c, Throwable t) { toast("Erreur réseau : " + t.getMessage()); }
                });
                break;
        }
    }

    // =========================================================
    // FILTRAGE
    // =========================================================
    private void filtrer(String query) {
        filtered.clear();
        String q = query.toLowerCase();
        for (String[] row : rows) {
            StringBuilder sb = new StringBuilder();
            for (String cell : row) sb.append(cell).append(" ");
            if (sb.toString().toLowerCase().contains(q)) filtered.add(row);
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    // =========================================================
    // CLIC SUR UNE LIGNE
    // =========================================================
    private void onRowClick(int id) {
        if ("statistiques".equals(table)) return;

        switch (table) {
            case "mecaniciens":
                for (Models.Mecanicien m : mecas) {
                    if (m.id == id) {
                        new AlertDialog.Builder(this).setTitle(m.prenom + " " + m.nom)
                                .setItems(new String[]{"✏️ Modifier", "🗑️ Supprimer"}, (d, w) -> {
                                    if (w == 0) formulaireMecanicien(m);
                                    else supprimerMecanicien(m);
                                }).show();
                        return;
                    }
                }
                break;
            case "clients":
                for (Models.Client cl : clis) {
                    if (cl.id == id) {
                        new AlertDialog.Builder(this).setTitle(cl.prenom + " " + cl.nom)
                                .setItems(new String[]{"✏️ Modifier", "🗑️ Supprimer"}, (d, w) -> {
                                    if (w == 0) formulaireClient(cl);
                                    else supprimerClient(cl);
                                }).show();
                        return;
                    }
                }
                break;
            case "vehicules":
                for (Models.Vehicule v : vehis) {
                    if (v.id == id) {
                        new AlertDialog.Builder(this).setTitle(v.immatriculation)
                                .setItems(new String[]{"✏️ Modifier", "🗑️ Supprimer"}, (d, w) -> {
                                    if (w == 0) chargerClientsPourVehicule(v);
                                    else supprimerVehicule(v);
                                }).show();
                        return;
                    }
                }
                break;
            case "interventions":
                for (Models.Intervention i : intervs) {
                    if (i.id == id) {
                        new AlertDialog.Builder(this).setTitle("Intervention #" + i.id)
                                .setItems(new String[]{"✏️ Modifier", "🗑️ Supprimer"}, (d, w) -> {
                                    if (w == 0) chargerDonneesPourIntervention(i);
                                    else supprimerIntervention(i);
                                }).show();
                        return;
                    }
                }
                break;
        }
    }

    // =========================================================
    // AJOUT
    // =========================================================
    private void formulaireAjout() {
        switch (table) {
            case "mecaniciens":   formulaireMecanicien(null); break;
            case "clients":       formulaireClient(null); break;
            case "vehicules":     chargerClientsPourVehicule(null); break;
            case "interventions": chargerDonneesPourIntervention(null); break;
        }
    }

    // =========================================================
    // FORMULAIRES (Mécanicien, Client, Véhicule, Intervention)
    // =========================================================
    private void formulaireMecanicien(@androidx.annotation.Nullable Models.Mecanicien ex) {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_form, null);
        EditText nom = v.findViewById(R.id.etNom);
        EditText prenom = v.findViewById(R.id.etPrenom);
        EditText tel = v.findViewById(R.id.etTel);
        EditText spec = v.findViewById(R.id.etSpec);
        EditText date = v.findViewById(R.id.etDate);

        int[] caches = {R.id.etImmat, R.id.etMarque, R.id.etModele, R.id.etAnnee, R.id.etKm, R.id.etDescription, R.id.etCout, R.id.spClient, R.id.spVehicule, R.id.spMecanicien, R.id.spStatut};
        for (int id : caches) {
            View view = v.findViewById(id);
            if (view != null) view.setVisibility(View.GONE);
        }

        if (ex != null) {
            nom.setText(ex.nom); prenom.setText(ex.prenom);
            tel.setText(ex.telephone); spec.setText(ex.specialite);
            date.setText(ex.dateEmbauche);
        }

        new AlertDialog.Builder(this)
                .setTitle(ex == null ? "Nouveau mécanicien" : "Modifier mécanicien")
                .setView(v)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    Models.Mecanicien m = new Models.Mecanicien(
                            nom.getText().toString(), prenom.getText().toString(),
                            tel.getText().toString(), spec.getText().toString(), date.getText().toString());
                    if (ex == null) Api.get().createMecanicien(m).enqueue(cb("Créé ✔"));
                    else            Api.get().updateMecanicien(ex.id, m).enqueue(cb("Modifié ✔"));
                }).setNegativeButton("Annuler", null).show();
    }

    private void supprimerMecanicien(Models.Mecanicien m) {
        new AlertDialog.Builder(this).setTitle("Supprimer ?").setMessage("Supprimer " + m.prenom + " " + m.nom + " ?")
                .setPositiveButton("Oui", (d, w) -> Api.get().deleteMecanicien(m.id).enqueue(cb("Supprimé ✔")))
                .setNegativeButton("Annuler", null).show();
    }

    private void formulaireClient(@androidx.annotation.Nullable Models.Client ex) {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_form, null);
        EditText nom = v.findViewById(R.id.etNom);
        EditText prenom = v.findViewById(R.id.etPrenom);
        EditText tel = v.findViewById(R.id.etTel);
        EditText email = v.findViewById(R.id.etSpec);

        int[] caches = {R.id.etImmat, R.id.etMarque, R.id.etModele, R.id.etAnnee, R.id.etKm, R.id.etDate, R.id.etDescription, R.id.etCout, R.id.spClient, R.id.spVehicule, R.id.spMecanicien, R.id.spStatut};
        for (int id : caches) {
            View view = v.findViewById(id);
            if (view != null) view.setVisibility(View.GONE);
        }

        if (ex != null) {
            nom.setText(ex.nom); prenom.setText(ex.prenom);
            tel.setText(ex.telephone); email.setText(ex.email);
        }

        new AlertDialog.Builder(this)
                .setTitle(ex == null ? "Nouveau client" : "Modifier client")
                .setView(v)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    Models.Client c = new Models.Client();
                    c.nom = nom.getText().toString(); c.prenom = prenom.getText().toString();
                    c.telephone = tel.getText().toString(); c.email = email.getText().toString();
                    if (ex == null) Api.get().createClient(c).enqueue(cb("Créé ✔"));
                    else { c.id = ex.id; Api.get().updateClient(ex.id, c).enqueue(cb("Modifié ✔")); }
                }).setNegativeButton("Annuler", null).show();
    }

    private void supprimerClient(Models.Client c) {
        new AlertDialog.Builder(this).setTitle("Supprimer ?").setMessage("Supprimer " + c.prenom + " " + c.nom + " ?")
                .setPositiveButton("Oui", (d, w) -> Api.get().deleteClient(c.id).enqueue(cb("Supprimé ✔")))
                .setNegativeButton("Annuler", null).show();
    }

    private void chargerClientsPourVehicule(@androidx.annotation.Nullable Models.Vehicule ex) {
        Api.get().getClients().enqueue(new Callback<Api.Res<List<Models.Client>>>() {
            @Override public void onResponse(Call<Api.Res<List<Models.Client>>> c, Response<Api.Res<List<Models.Client>>> r) {
                if (r.isSuccessful() && r.body() != null && r.body().success) formulaireVehicule(r.body().data, ex);
            }
            @Override public void onFailure(Call<Api.Res<List<Models.Client>>> c, Throwable t) { toast("Erreur clients : " + t.getMessage()); }
        });
    }

    private void formulaireVehicule(List<Models.Client> clients, @androidx.annotation.Nullable Models.Vehicule ex) {
        if (clients.isEmpty()) { toast("Aucun client. Créez d'abord un client."); return; }
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_form, null);
        EditText immat = v.findViewById(R.id.etImmat);
        EditText marque = v.findViewById(R.id.etMarque);
        EditText modele = v.findViewById(R.id.etModele);
        EditText annee = v.findViewById(R.id.etAnnee);
        EditText km = v.findViewById(R.id.etKm);
        Spinner sp = v.findViewById(R.id.spClient);

        int[] caches = {R.id.etNom, R.id.etPrenom, R.id.etTel, R.id.etSpec, R.id.etDate, R.id.etDescription, R.id.etCout, R.id.spVehicule, R.id.spMecanicien, R.id.spStatut};
        for (int id : caches) {
            View view = v.findViewById(id);
            if (view != null) view.setVisibility(View.GONE);
        }

        sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, clients));

        if (ex != null) {
            immat.setText(ex.immatriculation); marque.setText(ex.marque); modele.setText(ex.modele);
            annee.setText(String.valueOf(ex.annee)); km.setText(String.valueOf(ex.kilometrage));
            for (int i = 0; i < clients.size(); i++) if (clients.get(i).id == ex.clientId) { sp.setSelection(i); break; }
        }

        new AlertDialog.Builder(this)
                .setTitle(ex == null ? "Nouveau véhicule" : "Modifier véhicule")
                .setView(v)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    try {
                        Models.Client c = (Models.Client) sp.getSelectedItem();
                        Models.Vehicule veh = new Models.Vehicule(
                                immat.getText().toString(), marque.getText().toString(), modele.getText().toString(),
                                Integer.parseInt(annee.getText().toString()), Integer.parseInt(km.getText().toString()), c.id);
                        if (ex == null) Api.get().createVehicule(veh).enqueue(cb("Créé ✔"));
                        else            Api.get().updateVehicule(ex.id, veh).enqueue(cb("Modifié ✔"));
                    } catch (Exception e) { toast("Champs invalides"); }
                }).setNegativeButton("Annuler", null).show();
    }

    private void supprimerVehicule(Models.Vehicule v) {
        new AlertDialog.Builder(this).setTitle("Supprimer ?").setMessage("Supprimer " + v.immatriculation + " ?")
                .setPositiveButton("Oui", (d, w) -> Api.get().deleteVehicule(v.id).enqueue(cb("Supprimé ✔")))
                .setNegativeButton("Annuler", null).show();
    }

    private void chargerDonneesPourIntervention(@androidx.annotation.Nullable Models.Intervention ex) {
        Api.get().getVehicules().enqueue(new Callback<Api.Res<List<Models.Vehicule>>>() {
            @Override public void onResponse(Call<Api.Res<List<Models.Vehicule>>> c, Response<Api.Res<List<Models.Vehicule>>> r) {
                if (r.isSuccessful() && r.body() != null && r.body().success) {
                    List<Models.Vehicule> vehs = r.body().data;
                    Api.get().getMecaniciens().enqueue(new Callback<Api.Res<List<Models.Mecanicien>>>() {
                        @Override public void onResponse(Call<Api.Res<List<Models.Mecanicien>>> c2, Response<Api.Res<List<Models.Mecanicien>>> r2) {
                            if (r2.isSuccessful() && r2.body() != null && r2.body().success) formulaireIntervention(vehs, r2.body().data, ex);
                        }
                        @Override public void onFailure(Call<Api.Res<List<Models.Mecanicien>>> c2, Throwable t) { toast("Erreur mécaniciens : " + t.getMessage()); }
                    });
                }
            }
            @Override public void onFailure(Call<Api.Res<List<Models.Vehicule>>> c, Throwable t) { toast("Erreur véhicules : " + t.getMessage()); }
        });
    }

    private void formulaireIntervention(List<Models.Vehicule> vehs, List<Models.Mecanicien> mecs, @androidx.annotation.Nullable Models.Intervention ex) {
        if (vehs.isEmpty() || mecs.isEmpty()) { toast("Données manquantes"); return; }
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_form, null);
        EditText date = v.findViewById(R.id.etDate);
        EditText desc = v.findViewById(R.id.etDescription);
        EditText cout = v.findViewById(R.id.etCout);
        Spinner spVeh = v.findViewById(R.id.spVehicule);
        Spinner spMec = v.findViewById(R.id.spMecanicien);
        Spinner spStat = v.findViewById(R.id.spStatut);

        int[] caches = {R.id.etNom, R.id.etPrenom, R.id.etTel, R.id.etSpec, R.id.etImmat, R.id.etMarque, R.id.etModele, R.id.etAnnee, R.id.etKm, R.id.spClient};
        for (int id : caches) {
            View view = v.findViewById(id);
            if (view != null) view.setVisibility(View.GONE);
        }

        spVeh.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehs));
        spMec.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mecs));
        String[] statuts = {"en_attente", "en_cours", "terminee"};
        spStat.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuts));

        if (ex != null) {
            date.setText(ex.dateIntervention); desc.setText(ex.description); cout.setText(String.valueOf(ex.cout));
            for (int i = 0; i < vehs.size(); i++) if (vehs.get(i).id == ex.vehiculeId) { spVeh.setSelection(i); break; }
            for (int i = 0; i < mecs.size(); i++) if (mecs.get(i).id == ex.mecanicienId) { spMec.setSelection(i); break; }
            for (int i = 0; i < statuts.length; i++) if (statuts[i].equals(ex.statut)) { spStat.setSelection(i); break; }
        }

        new AlertDialog.Builder(this)
                .setTitle(ex == null ? "Nouvelle intervention" : "Modifier intervention")
                .setView(v)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    try {
                        Models.Vehicule veh = (Models.Vehicule) spVeh.getSelectedItem();
                        Models.Mecanicien mec = (Models.Mecanicien) spMec.getSelectedItem();
                        Models.Intervention i = new Models.Intervention(
                                veh.id, mec.id, date.getText().toString(), desc.getText().toString(),
                                Double.parseDouble(cout.getText().toString()), (String) spStat.getSelectedItem());
                        if (ex == null) Api.get().createIntervention(i).enqueue(cb("Créé ✔"));
                        else            Api.get().updateIntervention(ex.id, i).enqueue(cb("Modifié ✔"));
                    } catch (Exception e) { toast("Champs invalides"); }
                }).setNegativeButton("Annuler", null).show();
    }

    private void supprimerIntervention(Models.Intervention i) {
        new AlertDialog.Builder(this).setTitle("Supprimer ?").setMessage("Supprimer l'intervention #" + i.id + " ?")
                .setPositiveButton("Oui", (d, w) -> Api.get().deleteIntervention(i.id).enqueue(cb("Supprimé ✔")))
                .setNegativeButton("Annuler", null).show();
    }

    // =========================================================
    // CALLBACKS ET ADAPTER
    // =========================================================
    private <T> Callback<Api.Res<T>> cb(String ok) {
        return new Callback<Api.Res<T>>() {
            @Override public void onResponse(Call<Api.Res<T>> c, Response<Api.Res<T>> r) {
                if (r.isSuccessful()) { toast(ok); charger(); }
                else if (r.code() == 409) {
                    new AlertDialog.Builder(DataActivity.this)
                            .setTitle("⚠️ Action impossible").setMessage("Cet élément est utilisé ailleurs.\nSupprimez d'abord les éléments liés.")
                            .setPositiveButton("Compris", null).show();
                } else { toast("Erreur " + r.code()); }
            }
            @Override public void onFailure(Call<Api.Res<T>> c, Throwable t) { toast("Échec : " + t.getMessage()); }
        };
    }

    private void toast(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    public interface OnRowClick { void onClick(int id); }

    public static class TableAdapter extends RecyclerView.Adapter<TableAdapter.VH> {
        private final List<String[]> data;
        private final OnRowClick listener;

        public TableAdapter(List<String[]> data, OnRowClick l) { this.data = data; this.listener = l; }

        @androidx.annotation.NonNull @Override
        public VH onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_row, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@androidx.annotation.NonNull VH h, int pos) {
            String[] row = data.get(pos);
            h.col1.setText(row.length > 0 ? row[0] : "");
            h.col2.setText(row.length > 1 ? row[1] : "");
            h.col3.setText(row.length > 2 ? row[2] : "");
            h.col4.setText(row.length > 3 ? row[3] : "");
            h.col5.setText(row.length > 4 ? row[4] : "");
            h.col6.setText(row.length > 5 ? row[5] : "");

            h.col4.setVisibility(row.length > 3 && !row[3].isEmpty() ? View.VISIBLE : View.GONE);
            h.col5.setVisibility(row.length > 4 && !row[4].isEmpty() ? View.VISIBLE : View.GONE);
            h.col6.setVisibility(row.length > 5 && !row[5].isEmpty() ? View.VISIBLE : View.GONE);

            int bg = (pos % 2 == 0) ? android.graphics.Color.WHITE : android.graphics.Color.parseColor("#F5F5F5");
            h.itemView.setBackgroundColor(bg);

            h.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(Integer.parseInt(row[0]));
            });
        }

        @Override public int getItemCount() { return data.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView col1, col2, col3, col4, col5, col6;
            VH(@androidx.annotation.NonNull View v) {
                super(v);
                col1 = v.findViewById(R.id.col1); col2 = v.findViewById(R.id.col2);
                col3 = v.findViewById(R.id.col3); col4 = v.findViewById(R.id.col4);
                col5 = v.findViewById(R.id.col5); col6 = v.findViewById(R.id.col6);
            }
        }
    }
}