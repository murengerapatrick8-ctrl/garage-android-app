package com.example.garage_examen_mob;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Utils {

    // ====== Adapter Mécaniciens ======
    public static class MecanicienAdapter extends RecyclerView.Adapter<MecanicienAdapter.VH> {
        public interface OnClick { void on(Models.Mecanicien m); }
        private final List<Models.Mecanicien> data;
        private final OnClick listener;

        public MecanicienAdapter(List<Models.Mecanicien> d, OnClick l) { data = d; listener = l; }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_mecanicien, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Models.Mecanicien m = data.get(pos);
            h.nom.setText(m.prenom + " " + m.nom);
            h.spec.setText(m.specialite == null ? "—" : m.specialite);
            h.tel.setText(m.telephone == null ? "—" : m.telephone);
            h.itemView.setOnClickListener(v -> { if (listener != null) listener.on(m); });
        }
        @Override public int getItemCount() { return data.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView nom, spec, tel;
            VH(@NonNull View v) {
                super(v);
                nom  = v.findViewById(R.id.tvNom);
                spec = v.findViewById(R.id.tvSpecialite);
                tel  = v.findViewById(R.id.tvTel);
            }
        }
    }

    // ====== Adapter Véhicules ======
    public static class VehiculeAdapter extends RecyclerView.Adapter<VehiculeAdapter.VH> {
        private final List<Models.Vehicule> data;
        public VehiculeAdapter(List<Models.Vehicule> d) { data = d; }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_vehicule, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Models.Vehicule v = data.get(pos);
            h.immat.setText(v.immatriculation);
            h.desc.setText(v.marque + " " + v.modele + " • " + v.annee);
            h.km.setText(v.kilometrage + " km");
            h.client.setText("Client : " + (v.client == null ? "—" : v.client));
        }
        @Override public int getItemCount() { return data.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView immat, desc, km, client;
            VH(@NonNull View v) {
                super(v);
                immat  = v.findViewById(R.id.tvImmat);
                desc   = v.findViewById(R.id.tvDesc);
                km     = v.findViewById(R.id.tvKm);
                client = v.findViewById(R.id.tvClient);
            }
        }
    }

    // ====== Adapter Interventions ======
    public static class InterventionAdapter extends RecyclerView.Adapter<InterventionAdapter.VH> {
        private final List<Models.Intervention> data;
        public InterventionAdapter(List<Models.Intervention> d) { data = d; }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
            return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_intervention, p, false));
        }
        @Override public void onBindViewHolder(@NonNull VH h, int pos) {
            Models.Intervention i = data.get(pos);
            h.vehicule.setText(i.immatriculation + " — " + i.marque + " " + i.modele);
            h.meca.setText("Mécanicien : " + (i.mecanicien == null ? "—" : i.mecanicien));
            h.desc.setText(i.description);
            h.date.setText(i.dateIntervention);
            h.cout.setText(String.format("%,.0f FBu", i.cout));
            h.statut.setText(i.statut == null ? "—" : i.statut.replace("_", " "));
            int color;
            if ("terminee".equals(i.statut))      color = Color.parseColor("#2E7D32");
            else if ("en_cours".equals(i.statut)) color = Color.parseColor("#F9A825");
            else                                  color = Color.parseColor("#C62828");
            h.statut.setTextColor(color);
        }
        @Override public int getItemCount() { return data.size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView vehicule, meca, desc, date, cout, statut;
            VH(@NonNull View v) {
                super(v);
                vehicule = v.findViewById(R.id.tvVehicule);
                meca     = v.findViewById(R.id.tvMecanicien);
                desc     = v.findViewById(R.id.tvDescription);
                date     = v.findViewById(R.id.tvDate);
                cout     = v.findViewById(R.id.tvCout);
                statut   = v.findViewById(R.id.tvStatut);
            }
        }
    }
}