package com.cvicse.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;


public class MapDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate nameJdbc;
	
	void insert(String tableName,List<?> dataListMap) {
		for (int i = 0; i < dataListMap.size(); i++) {
			String batchPoint = "insert into "+tableName+" (@@col) values (@@val)";
			Map<String,Object> dataMap = (Map<String,Object>)dataListMap.get(i);
			Iterator iter = dataMap.keySet().iterator();
			String keyPart = "";
			String valPart = "";
			for (int j = 0; iter.hasNext(); j++) {
				String key = (String)iter.next();
				keyPart = keyPart.concat(keyPart+",");
				valPart = valPart.concat(":"+key+",");
			}
			batchPoint = batchPoint.replace("@@col", keyPart);
			batchPoint = batchPoint.replace("@@val", valPart);
		}
		
		nameJdbc.batchUpdate(batchPoint, dataListMap);
	}
	private Map<?,?> selectMetaData(String tableName){
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from user limit 0");
		SqlRowSetMetaData metaData = rowSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map<String,String> fieldMap = new HashMap<String,String>();
		for (int i = 1; i <= columnCount; i++) {  
			fieldMap.put("ColumnName", metaData.getColumnName(i));
			fieldMap.put("ColumnType", String.valueOf(metaData.getColumnType(i)));
			fieldMap.put("ColumnTypeName", metaData.getColumnTypeName(i));
			fieldMap.put("TableName", metaData.getTableName(i));
			fieldMap.put("SchemaName", metaData.getSchemaName(i));
			System.out.println(fieldMap);
		}
		return fieldMap;
	}
	
}
