package com.hims.repository;

import com.hims.domain.NatReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NATReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(NatReport natReport) {
        String sql = "insert into nat_report(p_id,result,date,time,rating) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, natReport.getP_id(), natReport.getResult(),
                natReport.getDate(), natReport.getTime(), natReport.getRating());
    }

    public void insert(int p_id) {
        String sql = "insert into nat_report(p_id) values (?)";
        jdbcTemplate.update(sql, p_id);
    }

    public boolean isNATUnfilled(int p_id) {
        List<NatReport> natReports = findByPId(p_id);
        for (NatReport natReport : natReports) {
            if (natReport.getResult() == null) {
                return false;
            }
        }
        return true;
    }

    public List<NatReport> findByPId(int p_id) {
        String sql = "select * from nat_report where p_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(NatReport.class), p_id);
        } catch (Exception e) {
            return null;
        }
    }

    public void update(int id, String result, String date, String time, String rating) {
        String sql = "update nat_report set result=?,date=?,time=?,rating=? where id=?";
        jdbcTemplate.update(sql, result, date, time, rating, id);
    }
}
