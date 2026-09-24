package com.samjsoares.soar.mapper;

import static org.junit.Assert.assertEquals;

import com.samjsoares.soar.model.TermInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TermInfoMapperTest {

  @Test
  public void testMapsColumns() throws SQLException {
    Map<String, Object> columns = new HashMap<>();
    columns.put("doc_id", 9L);
    columns.put("term", "search");
    columns.put("frequency", 4);

    TermInfo info = (TermInfo) new TermInfoMapper().mapRow(ResultSets.row(columns), 0);

    assertEquals(9L, info.getDocId());
    assertEquals("search", info.getTerm());
    assertEquals(4, info.getCount());
  }
}
