package dao.impl;

import dao.PaiementDAO;
import models.Paiement;
import utils.ConnectionDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void create(Paiement paiement) {
        String sql = "INSERT INTO paiement (id_paiement, id_abonnement, date_echeance, date_paiement, type_paiement, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paiement.getIdPaiement());
            ps.setString(2, paiement.getIdAbonnement());
            ps.setDate(3, paiement.getDateEcheance() != null ? Date.valueOf(paiement.getDateEcheance()) : null);
            ps.setDate(4, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            ps.setString(5, paiement.getTypePaiement());
            ps.setString(6, paiement.getStatut().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error inserting paiement: " + e.getMessage());
        }
    }

    @Override
    public Paiement findById(String idPaiement) {
        String sql = "SELECT * FROM paiement WHERE id_paiement = ?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPaiement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding paiement: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Paiement> findByAbonnement(String idAbonnement) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_abonnement = ?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idAbonnement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding paiements by abonnement: " + e.getMessage());
        }
        return paiements;
    }

    @Override
    public List<Paiement> findAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";
        try (Connection conn = ConnectionDatabase.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching paiements: " + e.getMessage());
        }
        return paiements;
    }

    @Override
    public void update(Paiement paiement) {
        String sql = "UPDATE paiement SET id_abonnement=?, date_echeance=?, date_paiement=?, type_paiement=?, statut=? WHERE id_paiement=?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paiement.getIdAbonnement());
            ps.setDate(2, paiement.getDateEcheance() != null ? Date.valueOf(paiement.getDateEcheance()) : null);
            ps.setDate(3, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            ps.setString(4, paiement.getTypePaiement());
            ps.setString(5, paiement.getStatut().name());
            ps.setString(6, paiement.getIdPaiement());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating paiement: " + e.getMessage());
        }
    }

    @Override
    public void delete(String idPaiement) {
        String sql = "DELETE FROM paiement WHERE id_paiement=?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPaiement);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting paiement: " + e.getMessage());
        }
    }

    @Override
    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_abonnement=? AND statut!='PAYE'";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idAbonnement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching unpaid paiements: " + e.getMessage());
        }
        return paiements;
    }

    @Override
    public List<Paiement> findLastPayments(String idAbonnement, int limit) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_abonnement=? ORDER BY date_paiement DESC LIMIT ?";
        try (Connection conn = ConnectionDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idAbonnement);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                paiements.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching last paiements: " + e.getMessage());
        }
        return paiements;
    }

    private Paiement mapResultSet(ResultSet rs) throws SQLException {
        String idPaiement = rs.getString("id_paiement");
        String idAbonnement = rs.getString("id_abonnement");
        LocalDate dateEcheance = rs.getDate("date_echeance") != null ? rs.getDate("date_echeance").toLocalDate() : null;
        LocalDate datePaiement = rs.getDate("date_paiement") != null ? rs.getDate("date_paiement").toLocalDate() : null;
        String typePaiement = rs.getString("type_paiement");
        Paiement.Statut statut = Paiement.Statut.valueOf(rs.getString("statut"));

        return new Paiement(idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut);
    }
}
