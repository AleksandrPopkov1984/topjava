package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Repository
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
        validateUser(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            String sql = "INSERT INTO user_roles VALUES (?, ?)";
            batchUpdate(user, sql, 2);
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            String sql = """
                    INSERT INTO user_roles VALUES (?, ?) 
                    ON CONFLICT ON CONSTRAINT user_roles_idx DO UPDATE SET role = ? WHERE user_roles.user_id = ?
                    """;
            batchUpdate(user, sql, 4);
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

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> validate = validator.validate(user);
        if (validate.size() > 0) {
            throw new RuntimeException("UserBean constraints have been violated");
        }
    }

    private void batchUpdate(User user, String sql, int parametersQuantity) {
        int[] count = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, String.valueOf(user.getRoles().stream().toList().get(i)));
                if (parametersQuantity == 4) {
                    ps.setString(3, String.valueOf(user.getRoles().stream().toList().get(i)));
                    ps.setInt(4, user.getId());
                }
            }

            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }
}
