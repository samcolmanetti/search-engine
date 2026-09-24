package com.samjsoares.soar.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.samjsoares.soar.model.DocumentInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class DocumentInfoMapperTest {

  @Test
  public void testMapsColumns() throws SQLException {
    Map<String, Object> columns = new HashMap<>();
    columns.put("id", 3L);
    columns.put("url", "http://example.com/");
    columns.put("time_indexed", 1234L);

    DocumentInfo info = (DocumentInfo) new DocumentInfoMapper().mapRow(ResultSets.row(columns), 0);

    assertEquals(3L, info.getId());
    assertEquals("http://example.com/", info.getUrl());
    assertEquals(1234L, info.getTimeIndexed());
    // Title and description are not read by this mapper.
    assertNull(info.getTitle());
    assertNull(info.getDescription());
  }
}
