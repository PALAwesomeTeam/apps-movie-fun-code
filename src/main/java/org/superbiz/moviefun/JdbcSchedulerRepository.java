package org.superbiz.moviefun;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class JdbcSchedulerRepository implements SchedulerRepository {
    public final JdbcTemplate jdbcTemplate;

    public JdbcSchedulerRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public AlbumSchedulerItem create(AlbumSchedulerItem startTime) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO  album_scheduler_task (started_at) " +
                "VALUES(?)"
            );
            statement.setTimestamp(1,startTime.getStartTime());
            return statement;
        });
        return find(startTime.getStartTime());
    }

    @Override
    public AlbumSchedulerItem find(Timestamp startTime) {
        return jdbcTemplate.query(
                "SELECT started_at FROM album_scheduler_task where started_at = ?",
                new Object[]{startTime},
                extractor

        );
    }

    @Override
    public AlbumSchedulerItem findLatest() {
        return jdbcTemplate.query(
                "SELECT started_at FROM album_scheduler_task ORDER BY started_at DESC LIMIT 1",

            extractor
        );


    }

    @Override
    public List<AlbumSchedulerItem> list() {
        return null;
    }

    @Override
    public AlbumSchedulerItem update(Timestamp startTime, AlbumSchedulerItem newStartDate) {
        return null;
    }

    @Override
    public void delete(AlbumSchedulerItem startTime) {

    }

    private final RowMapper<AlbumSchedulerItem> mapper = (rs, rowNum) -> new AlbumSchedulerItem(
            rs.getTimestamp("started_at")
    );

    private final ResultSetExtractor<AlbumSchedulerItem> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
