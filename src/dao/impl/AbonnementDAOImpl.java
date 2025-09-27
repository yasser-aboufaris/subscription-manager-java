package dao.impl;

import dao.AbonnementDAO;
import models.Abonnement;
import models.AbonnementAvecEngagement;
import models.AbonnementSansEngagement;
import utils.ConnectionDatabase;
import java.time.LocalDate;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAOImpl implements AbonnementDAO {

    @Override
    public void create(Abonnement abonnement) {
        String sql = "INSERT INTO abonnement (id, nom_service, montant_mensuel, date_debut, date_fin, statut, type_abonnement, duree_engagement_mois) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, abonnement.getId());
            ps.setString(2, abonnement.getNomService());
            ps.setBigDecimal(3, abonnement.getMontantMensuel());
            ps.setDate(4, abonnement.getDateDebut() != null ? Date.valueOf(abonnement.getDateDebut()) : null);
            ps.setDate(5, abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
            ps.setString(6, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {
                ps.setString(7, "AVEC_ENGAGEMENT");
                ps.setInt(8, ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois());
            } else {
                ps.setString(7, "SANS_ENGAGEMENT");
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error inserting abonnement: " + e.getMessage());
        }
    }

    @Override
    public Abonnement findById(String id) {
        String sql = "SELECT * FROM abonnement WHERE id = ?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding abonnement: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Abonnement> findAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";
        try (Connection conn = ConnectionDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                abonnements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching abonnements: " + e.getMessage());
        }
        return abonnements;
    }

    @Override
    public void update(Abonnement abonnement) {
        String sql = "UPDATE abonnement SET nom_service=?, montant_mensuel=?, date_debut=?, date_fin=?, statut=?, type_abonnement=?, duree_engagement_mois=? WHERE id=?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, abonnement.getNomService());
            ps.setBigDecimal(2, abonnement.getMontantMensuel());
            ps.setDate(3, abonnement.getDateDebut() != null ? Date.valueOf(abonnement.getDateDebut()) : null);
            ps.setDate(4, abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
            ps.setString(5, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {
                ps.setString(6, "AVEC_ENGAGEMENT");
                ps.setInt(7, ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois());
            } else {
                ps.setString(6, "SANS_ENGAGEMENT");
                ps.setNull(7, Types.INTEGER);
            }

            ps.setString(8, abonnement.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating abonnement: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM abonnement WHERE id=?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting abonnement: " + e.getMessage());
        }
    }

    @Override
    public List<Abonnement> findActiveSubscriptions() {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement WHERE statut='ACTIVE'";
        try (Connection conn = ConnectionDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                abonnements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching active abonnements: " + e.getMessage());
        }
        return abonnements;
    }

    @Override
    public List<Abonnement> findByType(String type) {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM abonnement WHERE type_abonnement=?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                abonnements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching abonnements by type: " + e.getMessage());
        }
        return abonnements;
    }

    private Abonnement mapResultSet(ResultSet rs) throws SQLException {
        try {
            String type = rs.getString("type_abonnement");
            String id = rs.getString("id");
            String nomService = rs.getString("nom_service");
            BigDecimal montantMensuel = rs.getBigDecimal("montant_mensuel");
            LocalDate dateDebut = rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null;
            LocalDate dateFin = rs.getDate("date_fin") != null ? rs.getDate("date_fin").toLocalDate() : null;
            Abonnement.Statut statut = Abonnement.Statut.valueOf(rs.getString("statut"));

            if ("AVEC_ENGAGEMENT".equals(type)) {
                int dureeEngagementMois = rs.getInt("duree_engagement_mois");
                return new AbonnementAvecEngagement(id, nomService, montantMensuel, dateDebut, dateFin, statut, dureeEngagementMois);
            } else {
                return new AbonnementSansEngagement(id, nomService, montantMensuel, dateDebut, dateFin, statut);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error mapping ResultSet to Abonnement: " + e.getMessage());
            throw e;
        }
    }
}