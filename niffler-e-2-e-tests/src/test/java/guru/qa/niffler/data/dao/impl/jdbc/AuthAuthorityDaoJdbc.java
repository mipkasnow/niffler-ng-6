package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setReadAndWriteAuthorityToUser(UUID userId) {
        try (PreparedStatement psRead = connection.prepareStatement("INSERT INTO authority (user_id, authority) VALUES (?, ?)");
             PreparedStatement psWrite = connection.prepareStatement("INSERT INTO authority (user_id, authority) VALUES (?, ?)")) {
            psRead.setObject(1, userId);
            psRead.setString(2, Authority.read.name());
            psRead.executeUpdate();

            psWrite.setObject(1, userId);
            psWrite.setString(2, Authority.write.name());
            psWrite.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(AuthorityEntity... authority) {

    }

    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM authority")) {
            ps.execute();
            List<AuthorityEntity> authorityEntities = new ArrayList<>();

            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setUserId(rs.getObject("user_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(ae);
                }
            }
            return authorityEntities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
