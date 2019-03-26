package com.cvicse.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cvicse.config.YamlConfig;
import com.cvicse.dao.MapDao;
import com.cvicse.dao.exception.ReflectionException;
import com.cvicse.exception.DataAccessException;
import com.cvicse.service.ExcelService;
import com.cvicse.util.DataTypeChecker;

@Service
public class ExcelServiceImpl implements ExcelService {

	/**
	 * yaml配置参数
	 */
	@Autowired
	public YamlConfig yamlConfig;

	@Autowired
	DataSourceProperties dataSourceProperties;

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public List<Map<String, Object>> loadExcel2Map(File file,StringBuilder errorInfo) {

		List<Map<String, Object>> loadedDataListMap = new ArrayList<Map<String, Object>>();
		Map<Integer,String> colNumNameMap = new HashMap<Integer,String>();
		Map<String, Object> commonImport = (Map<String, Object>) yamlConfig.getYamlMap().get("commonImport");
		int headerline = (int) commonImport.get("headerline");
		Map<Integer, Object> excelcolumns = (Map<Integer, Object>) commonImport.get("excelcolumns");
		Set columnNumSet = excelcolumns.keySet();

		FileInputStream fis = null;
		Workbook wb = null;
		try {
			fis = new FileInputStream(file);
			wb = WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheetAt(0);
			Row headerRow = sheet.getRow(headerline);
			if(headerRow != null) {
				Iterator columnNumIterator = columnNumSet.iterator();
				while (columnNumIterator.hasNext()) {
					String keyStr = (String) columnNumIterator.next();
					int key = Integer.parseInt(keyStr);
					Map<String, Object> excelColumnsParam = (Map<String, Object>) excelcolumns.get(keyStr);
					String fieldname = (String) excelColumnsParam.get("fieldname");
					Cell bCell = headerRow.getCell(key);
					colNumNameMap.put(key, bCell.toString());
				}
				
			}
			int BodyFirstRowNum = headerline + 1;
			int BodyLastRowNum = sheet.getLastRowNum()+1;
			for (int i = BodyFirstRowNum; i < BodyLastRowNum; i++) {
				Row bodyRow = sheet.getRow(i);
				if (bodyRow != null) {
					Map<String, Object> rowMap = new HashMap<String, Object>();
					Iterator columnNumIterator = columnNumSet.iterator();
					while (columnNumIterator.hasNext()) {
						String keyStr = (String) columnNumIterator.next();
						int key = Integer.parseInt(keyStr);
						Map<String, Object> excelColumnsParam = (Map<String, Object>) excelcolumns.get(keyStr);
						String tablename = (String) excelColumnsParam.get("tablename");
						String fieldname = (String) excelColumnsParam.get("fieldname");
						Map<String,String> fieldtype = (Map<String,String>) excelColumnsParam.get("fieldtype");
						String basetype = (String)fieldtype.get("basetype");
						String scope = (String)fieldtype.get("scope");
						Map<String,String> dict = (Map<String,String>)excelColumnsParam.get("dict");
						String dataverifyrules = (String)excelColumnsParam.get("dataverifyrules"); 
						String errnotes = (String)excelColumnsParam.get("errnotes");
						Cell bCell = bodyRow.getCell(key);
						if (bCell != null) {
							Object cellObj = getCellStringOrDateVal(bCell);
							if(null != dict && !dict.isEmpty()) {
								cellObj = this.genDictVal(cellObj, dict);
							}
							String[] sqlValAErr = this.valiAGenSqlValByYamlColType(cellObj, basetype,scope,dataverifyrules,errnotes,i,key,colNumNameMap);
							rowMap.put(fieldname.toLowerCase(), sqlValAErr[0]);
//							if ((cellObj instanceof Date)) {
//								rowMap.put(fieldname.toLowerCase(), cellObj);
//							} else {
//								String colVal = (String) cellObj;
//								rowMap.put(fieldname.toLowerCase(), cellObj);
//							}
						 if(null != sqlValAErr[1]&& !sqlValAErr[1].isEmpty()) {
					        	errorInfo = errorInfo.append(sqlValAErr[1]);
					        }
						}
					}
					if (null != rowMap && !rowMap.isEmpty()) {
						loadedDataListMap.add(rowMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return loadedDataListMap;
	}

	@Override
	public void save(List<Map<String, Object>> excelDtaMap,StringBuilder errorInfo) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, Object> commonImport = (Map<String, Object>) yamlConfig.getYamlMap().get("commonImport");
		List<Map<String, Object>> basicTables = (List<Map<String, Object>>) commonImport.get("basicTable");
		List<Map<String, Object>> detailTables = (List<Map<String, Object>>) commonImport.get("detailTable");
		List<Map<String, Object>> allTables = new ArrayList<Map<String, Object>>();
		List<String> batchSql = new ArrayList<String>();
		
		if (null != basicTables && !basicTables.isEmpty()) {
			allTables.addAll(basicTables);
		}
		if (null != detailTables && !detailTables.isEmpty()) {
			allTables.addAll(detailTables);
		}
		if(null != allTables && !allTables.isEmpty()) {
			for (int i = 0; i < excelDtaMap.size(); i++) {
				Map<String, Object> dataMap = excelDtaMap.get(i);
				for (int j = 0; j < allTables.size(); j++) {
					String sqlRow = "insert into @@tal (@@id1@@col) values (@@idval1@@val)";
					Map<String, Object> tablePK = allTables.get(j);
					String tableName = (String) tablePK.get("tableName");
					sqlRow = sqlRow.replace("@@tal", tableName);
					List<Object> pks = (List<Object>) tablePK.get("PK");
					for (int k = 0; k < pks.size(); k++) {
						Map<String,Object> pkMap = (Map<String,Object>)pks.get(k);
						Iterator iter = pkMap.keySet().iterator();
						String idColPart = "";
						String idValPart = "";
						while(iter.hasNext()) {
							String ID = (String)iter.next();
							Map<String,Object> idInfor = (Map<String,Object>)pkMap.get(ID);
							Map<String,String> filedtype = (Map<String,String>)idInfor.get("filedtype");
							String basetype = (String)filedtype.get("basetype");
							String scope = (String)filedtype.get("scope");
							idColPart = idColPart.concat(ID+",");
							Map<String,String> creator = (Map<String,String>)idInfor.get("creator");
							String classStr = (String)creator.get("class");
							String methodStr = (String)creator.get("method");
							Class classClass = Class.forName(classStr);  
					        //获取add()方法
					        Method m = classClass.getMethod(methodStr);
					        //调用add()方法
					        Object obj = classClass.getConstructor().newInstance();  
					        String idVal = (String)m.invoke(obj);
					        Map<Integer,String> colNumNameMap = new HashMap<Integer,String>();
					        colNumNameMap.put(0, "主键");
					        String[] idValAErr = this.valiAGenSqlValByYamlColType(idVal, basetype, scope,"ok","$ROWNUM 行 ID 错误",i,0,colNumNameMap);
					        idValPart = idValPart.concat(idValAErr[0]+",");
					        if(null != idValAErr[1]&& !idValAErr[1].isEmpty()) {
					        	errorInfo = errorInfo.append(idValAErr[1]);
					        }
					        
						}
						sqlRow = sqlRow.replace("@@id1", idColPart);
						sqlRow = sqlRow.replace("@@idval1", idValPart);
					}
					List<String> columns = (List<String>) tablePK.get("columns");
					String colNamePart = "";
					String colValPart = "";
					for (int k = 0; k < columns.size(); k++) {
						String colName = columns.get(k);
						colNamePart = colNamePart.concat(colName+",");
						colValPart = colValPart.concat(dataMap.get(colName.toLowerCase())+",");
					}
					sqlRow = sqlRow.replace("@@col", colNamePart);
					sqlRow = sqlRow.replace("@@val", colValPart);
					sqlRow = sqlRow.replace(",)", ")");
					batchSql.add(sqlRow);
					
				}
			}
			
			DataSource dataSource = applicationContext.getBean(DataSource.class);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			String[] batchSqlArr = new String[batchSql.size()];
			batchSql.toArray(batchSqlArr);
			int[] temp = jdbcTemplate.batchUpdate(batchSqlArr);
			if (temp.length>0) {
				System.out.println("插入成功！");
			}else{
				System.out.println("插入失败");
			}
		}

	}

	protected Object getCellStringOrDateVal(Cell cell) {
		String cellValStr = null;
		switch (cell.getCellType()) {
		case STRING:
			cellValStr = cell.getRichStringCellValue().getString();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				Date ateCellValue = cell.getDateCellValue();
				return (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(ateCellValue);
			}
			cellValStr = Double.toString(cell.getNumericCellValue());

			String tempN = cellValStr.substring(cellValStr.indexOf(".") + 1).replaceAll("0", "");
			if (tempN.isEmpty()) {
				cellValStr = cellValStr.substring(0, cellValStr.indexOf("."));
			}

			break;
		case BOOLEAN:
			cellValStr = Boolean.toString(cell.getBooleanCellValue());
			break;
		case FORMULA:
			FormulaEvaluator evaluator = cell.getRow().getSheet().getWorkbook().getCreationHelper()
					.createFormulaEvaluator();
			CellValue cv = evaluator.evaluate(cell);
			cellValStr = Double.toString(cv.getNumberValue());

			String tempF = cellValStr.substring(cellValStr.indexOf(".") + 1).replaceAll("0", "");
			if (tempF.isEmpty()) {
				cellValStr = cellValStr.substring(0, cellValStr.indexOf("."));
			}
			break;
		case _NONE:
		default:
			cellValStr = cell.getRichStringCellValue().getString();
		}

		return cellValStr;
	}
	
	protected String[] valiAGenSqlValByYamlColType(Object excelColVal,String yamlColType,String scope,String dataverifyrules,String errnotes,int rownum,int colnum,Map<Integer,String> colNumNameMap) {
		if("ok".equalsIgnoreCase(dataverifyrules)) {
			return this.genSqlValByYamlColTypeOK(excelColVal, yamlColType, scope, errnotes,rownum,colnum,colNumNameMap);
		}else {
			//TODO 
			return null;
		}
	}
	
	protected String[] genSqlValByYamlColTypeOK(Object excelColVal,String yamlColType,String scope,String errnotes,int rownum,int colnum,Map<Integer,String> colNumNameMap) {
		String[] sqlValAErr = new String[2];
		String sqlVal = "";
		String errorInfo = "";
		if(yamlColType.equalsIgnoreCase("date")) {
			//String dateJava = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format((Date)excelColVal);
			boolean ok = DataTypeChecker.checkDate((String)excelColVal);
			if(!ok){
				errnotes = errnotes.replace("$ROWNUM", String.valueOf(rownum));
				errnotes = errnotes.replace("$FIELDNAME", colNumNameMap.get(colnum));
				errorInfo = errorInfo.concat(errnotes);
			}
			String dateJava = "";
			if(null != scope && !scope.isEmpty()) {
				dateJava = (new java.text.SimpleDateFormat(scope)).format((String)excelColVal);
				sqlVal = "to_date('"+dateJava+"','"+scope+"')";
			}
			sqlVal = "STR_TO_DATE('"+(String)excelColVal+"','%Y-%m-%d %H:%i:%s %p')";
			
			
		}else if(yamlColType.equalsIgnoreCase("int")) {
			boolean ok = DataTypeChecker.checkNumber((String)excelColVal, "0+");
			if(!ok){
				errnotes = errnotes.replace("$ROWNUM", String.valueOf(rownum));
				errnotes = errnotes.replace("$FIELDNAME", colNumNameMap.get(colnum));
				errorInfo = errorInfo.concat(errnotes);
			}
			if(null != scope && !scope.isEmpty()) {
				int length = Integer.parseInt(scope);
				if(excelColVal.toString().length()>length) {
					errnotes = errnotes.replace("$ROWNUM", String.valueOf(rownum));
					errnotes = errnotes.replace("$FIELDNAME", colNumNameMap.get(colnum));
					errorInfo = errorInfo.concat(errnotes);
				}
			}
			sqlVal = (String)excelColVal;
		}else if(yamlColType.equalsIgnoreCase("double")) {
			String excelColValCutPoint = ((String)excelColVal).replaceAll(",", "");
			boolean ok = DataTypeChecker.checkFloat(excelColValCutPoint, "+");
			if(!ok){
				errnotes = errnotes.replace("$ROWNUM", String.valueOf(rownum));
				errnotes = errnotes.replace("$FIELDNAME", colNumNameMap.get(colnum));
				errorInfo = errorInfo.concat(errnotes);
			}
			if(null != scope && !scope.isEmpty()) {
				String[] scopeArr = scope.split(",");
				String[] excelColValArr = (excelColValCutPoint.split("\\."));
				if(excelColValArr[0].length()>Integer.parseInt(scopeArr[0])||excelColValArr[1].length()>Integer.parseInt(scopeArr[1])) {
					errnotes = errnotes.replace("$ROWNUM", String.valueOf(rownum));
					errnotes = errnotes.replace("$FIELDNAME", colNumNameMap.get(colnum));
					errorInfo = errorInfo.concat(errnotes);
				}
			}
			sqlVal = excelColValCutPoint;
		}else {
			sqlVal = "'"+(String)excelColVal+"'";
		}
		sqlValAErr[0] = sqlVal;
		sqlValAErr[1] = errorInfo;
		return sqlValAErr;
	}
	
	protected String genDictVal(Object excelColVal,Map<String,String> dict) {
		return dict.get(excelColVal.toString());
	}
	
	@Override
	public void saveJDao(List<Map<String, Object>> excelDtaMap,StringBuilder errorInfo) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, DataAccessException, ReflectionException {
		Map<String, Object> commonImport = (Map<String, Object>) yamlConfig.getYamlMap().get("commonImport");
		List<Map<String, Object>> basicTables = (List<Map<String, Object>>) commonImport.get("basicTable");
		List<Map<String, Object>> detailTables = (List<Map<String, Object>>) commonImport.get("detailTable");
		List<Map<String, Object>> allTables = new ArrayList<Map<String, Object>>();
		List<String> batchSql = new ArrayList<String>();
		
		if (null != basicTables && !basicTables.isEmpty()) {
			allTables.addAll(basicTables);
		}
		if (null != detailTables && !detailTables.isEmpty()) {
			allTables.addAll(detailTables);
		}
		MapDao mapDao = new MapDao();
		if(null != allTables && !allTables.isEmpty()) {
			for (int i = 0; i < excelDtaMap.size(); i++) {
				Map<String, Object> dataMap = excelDtaMap.get(i);
				for (int j = 0; j < allTables.size(); j++) {
					Map<String, Object> tablePK = allTables.get(j);
					String tableName = (String) tablePK.get("tableName");
					List<Object> pks = (List<Object>) tablePK.get("PK");
					for (int k = 0; k < pks.size(); k++) {
						Map<String,Object> pkMap = (Map<String,Object>)pks.get(k);
						Iterator iter = pkMap.keySet().iterator();
						String idColPart = "";
						String idValPart = "";
						while(iter.hasNext()) {
							String ID = (String)iter.next();
							Map<String,Object> idInfor = (Map<String,Object>)pkMap.get(ID);
							Map<String,String> filedtype = (Map<String,String>)idInfor.get("filedtype");
							String basetype = (String)filedtype.get("basetype");
							String scope = (String)filedtype.get("scope");
							idColPart = idColPart.concat(ID+",");
							Map<String,String> creator = (Map<String,String>)idInfor.get("creator");
							String classStr = (String)creator.get("class");
							String methodStr = (String)creator.get("method");
//							String classStr = IDGen.substring(0, IDGen.lastIndexOf("."));
//							String methodStr = IDGen.substring(IDGen.lastIndexOf(".")+1);
							Class classClass = Class.forName(classStr);  
					        //获取add()方法
					        Method m = classClass.getMethod(methodStr);
					        //调用add()方法
					        Object obj = classClass.getConstructor().newInstance();  
					        String idVal = (String)m.invoke(obj);
					        Map<Integer,String> colNumNameMap = new HashMap<Integer,String>();
					        colNumNameMap.put(0, "主键");
					        String[] idValAErr = this.valiAGenSqlValByYamlColType(idVal, basetype, scope,"ok","$ROWNUM 行 ID 错误",i,0,colNumNameMap);
					        idValPart = idValPart.concat(idValAErr[0]+",");
					        if(null != idValAErr[1]&& !idValAErr[1].isEmpty()) {
					        	errorInfo = errorInfo.append(idValAErr[1]);
					        }
					        
						}
						dataMap.put(idColPart, idValPart);
					}
					mapDao.insert(tableName, dataMap);
				}
			}
		}

	}

}
