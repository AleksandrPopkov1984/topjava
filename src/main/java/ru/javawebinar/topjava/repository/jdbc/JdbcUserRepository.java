package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.Validator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final UserResultSetExtractor RESULT_SET_EXTRACTOR = new UserResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    private Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        JdbcUtil.validate(user, validator);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            String sql = "INSERT INTO user_roles VALUES (?, ?)";
            batchUpdate(user, sql, false);
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            String sql = """
                    INSERT INTO user_roles VALUES (?, ?) 
                    ON CONFLICT ON CONSTRAINT user_roles_idx DO UPDATE SET role = ? WHERE user_roles.user_id = ?
                    """;
            batchUpdate(user, sql, true);
            deleteNotActualRoles(user);
        } else {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    private static class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();

            while (rs.next()) {
                Integer userKey = rs.getInt("id");
                User user = users.get(userKey);
                if (user == null) {
                    user = new User();
                    users.put(userKey, user);
                    user.setId(userKey);
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRegistered(rs.getDate("registered"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                }
                if (user.getRoles() == null) {
                    user.setRoles(new HashSet<>());
                }
                if (rs.getString("role") != null) {
                    Role role = Role.valueOf(rs.getString("role"));
                    user.getRoles().add(role);
                }
            }
            return users.values().stream().toList();
        }
    }

    @Override
    public User get(int id) {
        String sql = "SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE id=?";
        List<User> users = jdbcTemplate.query(sql, RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        String sql = "SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE email=?";
        List<User> users = jdbcTemplate.query(sql, RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id ORDER BY name, email";
        List<User> users = jdbcTemplate.query(sql, RESULT_SET_EXTRACTOR);
        return users;
    }

    private void batchUpdate(User user, String sql, boolean isUpdate) {
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                List<Role> roles = user.getRoles().stream().toList();
                ps.setString(2, String.valueOf(roles.get(i)));
                if (isUpdate) {
                    ps.setString(3, String.valueOf(roles.get(i)));
                    ps.setInt(4, user.getId());
                }
            }

            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }

    private void deleteNotActualRoles(User user) {
        List<String> roles = user.getRoles().stream()
                .map(r -> String.valueOf(r))
                .toList();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", user.getId());
        String sql = "DELETE FROM user_roles WHERE user_id=:id";
        if (roles.size() == 0) {
            namedParameterJdbcTemplate.update(sql, parameters);
        } else {
            parameters.addValue("roles", roles);
            namedParameterJdbcTemplate.update(sql + " AND role NOT IN (:roles)", parameters);
        }
    }
}
