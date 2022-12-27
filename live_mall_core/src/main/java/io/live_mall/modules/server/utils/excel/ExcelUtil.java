package io.live_mall.modules.server.utils.excel;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *  
 * 只需要两步即可完成以前复杂的Excel读取 用法步骤： 1.定义需要读取的表头字段和表头对应的属性字段 String keyValue
 * ="手机名称:phoneName,颜色:color,售价:price"; 2.读取数据 List<PhoneModel> list =
 * ExcelUtil.readXls("C://test.xlsx",ExcelUtil.getMap(keyValue),"com.lkx.excel.PhoneModel");
 *
 * @author daitao
 * @date:   2020年2月18日 下午4:48:59
 */
public class ExcelUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * getMap:(将传进来的表头和表头对应的属性存进Map集合，表头字段为key,属性为value)
	 *
	 * @author likaixuan
	 * @param 把传进指定格式的字符串解析到Map中
	 *            形如: String keyValue = "手机名称:phoneName,颜色:color,售价:price";
	 * @return
	 * @since JDK 1.7
	 */
	public static Map<String, String> getMap(String keyValue) {
		Map<String, String> map = new HashMap<String, String>();
		if (keyValue != null) {
			String[] str = keyValue.split(",");
			for (String element : str) {
				String[] str2 = element.split(":");
				map.put(str2[0], str2[1]);
			}
		}
		return map;
	}

	/**
	 * @author likaixuan
	 * @param 把传进指定格式的字符串解析到List中
	 * @return List
	 * @Date 2018年5月9日 21:42:24
	 * @since JDK 1.7
	 */
	public static List<String> getList(String keyValue) {
		List<String> list = new ArrayList<String>();
		if (keyValue != null) {
			String[] str = keyValue.split(",");

			for (String element : str) {
				String[] str2 = element.split(":");
				list.add(str2[0]);
			}
		}
		return list;
	}

	/**
	 * readXlsPart:(根据传进来的map集合读取Excel) 传进来4个参数 <String,String>类型，第二个要反射的类的具体路径)
	 *
	 * @author likaixuan
	 * @param filePath
	 *            Excel文件路径
	 * @param map
	 *            表头和属性的Map集合,其中Map中Key为Excel列的名称，Value为反射类的属性
	 * @param classPath
	 *            需要映射的model的路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static <T> List<T> readXlsPart(String filePath, Map map, String classPath, int... rowNumIndex)
			throws Exception {

		Set keySet = map.keySet();// 返回键的集合

		/** 反射用 **/
		Class<?> demo = null;
		Object obj = null;
		/** 反射用 **/

		List<Object> list = new ArrayList<Object>();
		demo = Class.forName(classPath);
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream is = new FileInputStream(filePath);
		Workbook wb = null;

		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(is);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(is);
		} else {
			throw new Exception("您输入的excel格式不正确");
		}
		for (int sheetNum = 0; sheetNum < 1; sheetNum++) {// 获取每个Sheet表

			int rowNum_x = -1;// 记录第x行为表头
			Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
			List<String> headlist = new ArrayList();// 存放所有的表头字段信息

			Sheet hssfSheet = wb.getSheetAt(sheetNum);

			// 设置默认最大行为2w行
			if (hssfSheet != null && hssfSheet.getLastRowNum() > 60000) {
				throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

				if (rowNumIndex != null && rowNumIndex.length > 0 && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
					Row hssfRow = hssfSheet.getRow(rowNumIndex[0]);
					if (hssfRow == null) {
						throw new RuntimeException("指定的行为空，请检查");
					}
					rowNum = rowNumIndex[0] - 1;
				}
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
					if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}

				if (rowNum_x == -1) {
					// 循环列Cell
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

						Cell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}

						String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

						tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
						tempCellValue = tempCellValue.trim();

						headlist.add(tempCellValue);

						Iterator it = keySet.iterator();

						while (it.hasNext()) {
							Object key = it.next();
							if (StringUtils.isNotBlank(tempCellValue)
									&& StringUtils.equals(tempCellValue, key.toString())) {
								rowNum_x = rowNum;
								cellmap.put(map.get(key).toString(), cellNum);
							}
						}
						if (rowNum_x == -1) {
							throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
						}
					}

				} else {
					obj = demo.newInstance();
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						Object key = it.next();
						Integer cellNum_x = cellmap.get(map.get(key).toString());
						if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
							continue;
						}
						String attr = map.get(key).toString();// 得到属性

						Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

						Cell cell = hssfRow.getCell(cellNum_x);
						getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);

					}
					list.add(obj);
				}

			}
		}
		is.close();
		// wb.close();
		return (List<T>) list;
	}

	/**
	 * readXls:(根据传进来的map集合读取Excel) 传进来3个参数 <String,String>类型，第二个要反射的类的具体路径)
	 *
	 * @author likaixuan
	 * @param filePath
	 *            Excel文件路径
	 * @param map
	 *            表头和属性的Map集合,其中Map中Key为Excel列的名称，Value为反射类的属性
	 * @param classPath
	 *            需要映射的model的路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static <T> List<T> readXls(String filePath, Map map, String classPath, int... rowNumIndex) throws Exception {

		Set keySet = map.keySet();// 返回键的集合

		/** 反射用 **/
		Class<?> demo = null;
		Object obj = null;
		/** 反射用 **/

		List<Object> list = new ArrayList<Object>();
		demo = Class.forName(classPath);
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream is = new FileInputStream(filePath);
		Workbook wb = null;

		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(is);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(is);
		} else {
			throw new Exception("您输入的excel格式不正确");
		}
		// 默认循环所有sheet，如果rowNumIndex[]
		for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {// 获取每个Sheet表

			int rowNum_x = -1;// 记录第x行为表头
			Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
			List<String> headlist = new ArrayList();// 存放所有的表头字段信息

			Sheet hssfSheet = wb.getSheetAt(sheetNum);

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				if (rowNumIndex != null && rowNumIndex.length > 0 && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
					Row hssfRow = hssfSheet.getRow(rowNumIndex[0]);
					if (hssfRow == null) {
						throw new RuntimeException("指定的行为空，请检查");
					}
					rowNum = rowNumIndex[0] - 1;
				}
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
					if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}

				if (rowNum_x == -1) {
					// 循环列Cell
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

						Cell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}

						String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

						tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
						tempCellValue = tempCellValue.trim();

						headlist.add(tempCellValue);
						Iterator it = keySet.iterator();
						while (it.hasNext()) {
							Object key = it.next();
							if (StringUtils.isNotBlank(tempCellValue)
									&& StringUtils.equals(tempCellValue, key.toString())) {
								rowNum_x = rowNum;
								cellmap.put(map.get(key).toString(), cellNum);
							}
						}
						if (rowNum_x == -1) {
							throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
						}
					}

					// 读取到列后，检查表头是否完全一致--start
					for (int i = 0; i < headlist.size(); i++) {
						boolean boo = false;
						Iterator itor = keySet.iterator();
						while (itor.hasNext()) {
							String tempname = itor.next().toString();
							if (tempname.equals(headlist.get(i))) {
								boo = true;
							}
						}
						if (boo == false) {
							throw new Exception("表头字段和定义的属性字段不匹配，请检查");
						}
					}

					Iterator itor = keySet.iterator();
					while (itor.hasNext()) {
						boolean boo = false;
						String tempname = itor.next().toString();
						for (int i = 0; i < headlist.size(); i++) {
							if (tempname.equals(headlist.get(i))) {
								boo = true;
							}
						}
						if (boo == false) {
							throw new Exception("表头字段和定义的属性字段不匹配，请检查");
						}
					}
					// 读取到列后，检查表头是否完全一致--end

				} else {
					obj = demo.newInstance();
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						Object key = it.next();
						Integer cellNum_x = cellmap.get(map.get(key).toString());
						if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
							continue;
						}
						String attr = map.get(key).toString();// 得到属性

						Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

						Cell cell = hssfRow.getCell(cellNum_x);
						getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);

					}
					list.add(obj);
				}

			}
		}
		is.close();
		// wb.close();
		return (List<T>) list;
	}

	/**
	 * readXlsPart:(根据传进来的map集合读取Excel) 传进来4个参数 <String,String>类型，第二个要反射的类的具体路径)
	 *
	 * @author likaixuan
	 * @param filePath
	 *            Excel文件路径
	 * @param map
	 *            表头和属性的Map集合,其中Map中Key为Excel列的名称，Value为反射类的属性
	 * @param classPath
	 *            需要映射的model的路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static <T> JSONArray readXlsPart(ExcelParam param) throws Exception {

		Set keySet = param.getMap().keySet();// 返回键的集合

		/** 反射用 **/
		Class<?> demo = null;
		Object obj = null;
		/** 反射用 **/

		JSONArray list = new JSONArray();
		demo = Class.forName(param.getClassPath());
		String fileName = param.getFile().getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		//修改为文件
		InputStream is = new FileInputStream(param.getFile());
		Workbook wb = null;

		if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
			wb = new HSSFWorkbook(is);
		} else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
			wb = new XSSFWorkbook(is);
		} else {
			throw new Exception("您输入的excel格式不正确");
		}
		int startSheetNum = 0;
		int endSheetNum = 1;
		if (null != param.getSheetIndex()) {
			startSheetNum = param.getSheetIndex() - 1;
			endSheetNum = param.getSheetIndex();
		}
		for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

			int rowNum_x = -1;// 记录第x行为表头
			Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
			List<String> headlist = new ArrayList();// 存放所有的表头字段信息

			Sheet hssfSheet = wb.getSheetAt(sheetNum);
			// 设置默认最大行为2w行
			if (hssfSheet != null && hssfSheet.getLastRowNum() > 60000) {
				throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

				if (param.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
					Row hssfRow = hssfSheet.getRow(param.getRowNumIndex());
					if (hssfRow == null) {
						throw new RuntimeException("指定的行为空，请检查");
					}
					rowNum = param.getRowNumIndex() - 1;
				}
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
					if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}

				if (rowNum_x == -1) {
					// 循环列Cell
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

						Cell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}
						String tempCellValue="";
					    boolean isMerge = isMergedRegion(hssfSheet, rowNum,hssfCell.getColumnIndex());
						if(isMerge) {
							tempCellValue = getMergedRegionValue(hssfSheet, hssfRow.getRowNum(), hssfCell.getColumnIndex());
						}
						 
						tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

						tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
						tempCellValue = tempCellValue.trim();

						headlist.add(tempCellValue);

						Iterator it = keySet.iterator();

						while (it.hasNext()) {
							Object key = it.next();
							if (StringUtils.isNotBlank(tempCellValue)
									&& StringUtils.equals(tempCellValue, key.toString())) {
								rowNum_x = rowNum;
								cellmap.put(param.getMap().get(key).toString(), cellNum);
							}
						}
						if (rowNum_x == -1) {
							throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
						}
					}

				} else {
					obj = demo.newInstance();
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						Object key = it.next();
						Integer cellNum_x = cellmap.get(param.getMap().get(key).toString());
						if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
							continue;
						}
						String attr = param.getMap().get(key).toString();// 得到属性

						Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

						Cell cell = hssfRow.getCell(cellNum_x);
						getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);

					}
					list.add(obj);
				}

			}
		}
		is.close();
		// wb.close();
		return list;
	}
	
	
	
	
	/**
	 * readXlsPart:(根据传进来的map集合读取Excel) 传进来4个参数 <String,String>类型，第二个要反射的类的具体路径)
	 *
	 * @author likaixuan
	 * @param filePath
	 *            Excel文件路径
	 * @param map
	 *            表头和属性的Map集合,其中Map中Key为Excel列的名称，Value为反射类的属性
	 * @param classPath
	 *            需要映射的model的路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static <T> List<T> readXlsPartMap(ExcelParam param) throws Exception {

		Set keySet = param.getMap().keySet();// 返回键的集合

		/** 反射用 **/
		Class<?> demo = null;
		Object obj = null;
		/** 反射用 **/

		List<Object> list = new ArrayList<Object>();
		demo = Class.forName(param.getClassPath());
		String fileName = param.getFile().getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		//修改为文件
		InputStream is = new FileInputStream(param.getFile());
		Workbook wb = null;

		if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
			wb = new HSSFWorkbook(is);
		} else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
			wb = new XSSFWorkbook(is);
		} else {
			throw new Exception("您输入的excel格式不正确");
		}
		int startSheetNum = 0;
		int endSheetNum = 1;
		if (null != param.getSheetIndex()) {
			startSheetNum = param.getSheetIndex() - 1;
			endSheetNum = param.getSheetIndex();
		}
		for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

			int rowNum_x = -1;// 记录第x行为表头
			Map<String, Object> cellmap = new HashMap<String, Object>();// 存放每一个field字段对应所在的列的序号
			List<String> headlist = new ArrayList();// 存放所有的表头字段信息

			Sheet hssfSheet = wb.getSheetAt(sheetNum);
			// 设置默认最大行为2w行
			if (hssfSheet != null && hssfSheet.getLastRowNum() > 60000) {
				throw new Exception("Excel 数据超过60000行,请检查是否有空行,或分批导入");
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

				if (param.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
					Row hssfRow = hssfSheet.getRow(param.getRowNumIndex());
					if (hssfRow == null) {
						throw new RuntimeException("指定的行为空，请检查");
					}
					rowNum = param.getRowNumIndex() - 1;
				}
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
					if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}

				if (rowNum_x == -1) {
					// 循环列Cell
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

						Cell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}
						String tempCellValue="";
					    boolean isMerge = isMergedRegion(hssfSheet, rowNum,hssfCell.getColumnIndex());
						if(isMerge) {
							tempCellValue = getMergedRegionValue(hssfSheet, hssfRow.getRowNum(), hssfCell.getColumnIndex());
						}
						 
						tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

						tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
						tempCellValue = tempCellValue.trim();

						headlist.add(tempCellValue);

						Iterator it = keySet.iterator();

						while (it.hasNext()) {
							Object key = it.next();
							if (StringUtils.isNotBlank(tempCellValue) && StringUtils.equals(tempCellValue, key.toString())) {
								rowNum_x = rowNum;
								if(cellmap.get(param.getMap().get(key).toString()) !=null) {
									Object object = cellmap.get(param.getMap().get(key).toString());
									if(object instanceof ArrayList) {
									      List<Integer> cellNumList=	 (ArrayList<Integer>)object;
									      cellNumList.add(cellNum);
									}else  {
										  ArrayList<Integer> arrayList = new ArrayList<Integer>();
										  arrayList.add(cellNum);
										 cellmap.put(param.getMap().get(key).toString(), arrayList);
									}
								}else {
									cellmap.put(param.getMap().get(key).toString(), cellNum);
								}
								
							}
						}
						if (rowNum_x == -1) {
							throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
						}
					}

				} else {
					obj = demo.newInstance();
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						Object key = it.next();
						Object cellNum_x_obj = cellmap.get(param.getMap().get(key).toString());
						if(cellNum_x_obj instanceof ArrayList) {
							 List<Integer> cellNumList=	 (ArrayList<Integer>)cellNum_x_obj;
							 for (Integer cellNum_x : cellNumList) {
								 if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
										continue;
									}
									String attr = param.getMap().get(key).toString();// 得到属性

									Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

									Cell cell = hssfRow.getCell(cellNum_x);
									getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);
							}
						}else {
							Integer cellNum_x=	(Integer)cellNum_x_obj;
							if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
								continue;
							}
							String attr = param.getMap().get(key).toString();// 得到属性

							Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

							Cell cell = hssfRow.getCell(cellNum_x);
							getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);
						}
						
						

					}
					list.add(obj);
				}

			}
		}
		is.close();
		// wb.close();
		return (List<T>) list;
	}

	/**
	 * setter:(反射的set方法给属性赋值)
	 *
	 * @author likaixuan
	 * @param obj
	 *            具体的类
	 * @param att
	 *            类的属性
	 * @param value
	 *            赋予属性的值
	 * @param type
	 *            属性是哪种类型 比如:String double boolean等类型
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void setter(Object obj, String att, Object value, Class<?> type, int row, int col, Object key)
			throws Exception {
		try {
			if(obj instanceof JSONObject) {
				((JSONObject) obj).put((String) att, value);
			}else {
				Method method = obj.getClass().getMethod("set" + StringUtil.toUpperCaseFirstOne(att), type);
				method.invoke(obj, value);
			}
		} catch (Exception e) {
			throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 赋值异常  " + e);
		}

	}

	/**
	 * getAttrVal:(反射的get方法得到属性值)
	 *
	 * @author likaixuan
	 * @param obj
	 *            具体的类
	 * @param att
	 *            类的属性
	 * @param value
	 *            赋予属性的值
	 * @param type
	 *            属性是哪种类型 比如:String double boolean等类型
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static Object getAttrVal(Object obj, String att, Class<?> type) throws Exception {
		try {
			Method method = obj.getClass().getMethod("get" + StringUtil.toUpperCaseFirstOne(att));
			Object value = new Object();
			value = method.invoke(obj);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * getValue:(得到Excel列的值)
	 *
	 * @author likaixuan
	 * @param hssfCell
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void getValue(Sheet hssfSheet,Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
			throws Exception {
		Object val = null;
	    boolean isMerge = isMergedRegion(hssfSheet, row,col);
		if(isMerge) {
			val = getMergedRegionValue(hssfSheet, row, col);
		}else {
				if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							if (attrType == String.class) {
								val = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
							} else {
								val = dateConvertFormat(sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())));
							}
						} catch (ParseException e) {
							throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
						}
					} else {
						if (attrType == String.class) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							val = cell.getStringCellValue();
						} else if (attrType == BigDecimal.class) {
							val = new BigDecimal(cell.getNumericCellValue());
						} else if (attrType == long.class) {
							val = (long) cell.getNumericCellValue();
						} else if (attrType == Double.class) {
							val = cell.getNumericCellValue();
						} else if (attrType == Float.class) {
							val = (long) cell.getNumericCellValue();
						} else if (attrType == int.class || attrType == Integer.class) {
							val = (long) cell.getNumericCellValue();
						} else if (attrType == Short.class) {
							val = (long) cell.getNumericCellValue();
						} else {
							val = (long) cell.getNumericCellValue();
						}
					}
		
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					val = cell.getStringCellValue();
				}
		}

		setter(obj, attr, val, attrType, row, col, key);
	}

	
	public static void getValueA(Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
			throws Exception {
		Object val = null;
		
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			val = cell.getBooleanCellValue();

		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					if (attrType == String.class) {
						val = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
					} else {
						val = dateConvertFormat(sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())));
					}
				} catch (ParseException e) {
					throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
				}
			} else {
				if (attrType == String.class) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					val = cell.getStringCellValue();
				} else if (attrType == BigDecimal.class) {
					val = new BigDecimal(cell.getNumericCellValue());
				} else if (attrType == long.class) {
					val = (long) cell.getNumericCellValue();
				} else if (attrType == Double.class) {
					val = cell.getNumericCellValue();
				} else if (attrType == Float.class) {
					val = (float) cell.getNumericCellValue();
				} else if (attrType == int.class || attrType == Integer.class) {
					val = (int) cell.getNumericCellValue();
				} else if (attrType == Short.class) {
					val = (short) cell.getNumericCellValue();
				} else {
					val = cell.getNumericCellValue();
				}
			}

		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			val = cell.getStringCellValue();
		}

		setter(obj, attr, val, attrType, row, col, key);
	}

	/**
	 * exportExcel:(导出Excel)
	 *
	 * @author likaixuan
	 * @param hssfCell
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void exportExcel(String outFilePath, String keyValue, List<?> list, String classPath)
			throws Exception {

		Map<String, String> map = getMap(keyValue);
		List<String> keyList = getList(keyValue);

		Class<?> demo = null;
		demo = Class.forName(classPath);
		Object obj = demo.newInstance();
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 声明样式
		HSSFCellStyle style = wb.createCellStyle();
		// 居中显示
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 在sheet里创建第一行为表头，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow rowHeader = sheet.createRow(0);
		// 创建单元格并设置单元格内容

		// 存储属性信息
		Map<String, String> attMap = new HashMap();
		int index = 0;

		for (String key : keyList) {
			rowHeader.createCell(index).setCellValue(key);
			attMap.put(Integer.toString(index), map.get(key).toString());
			index++;
		}

		// 在sheet里创建表头下的数据
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < map.size(); j++) {

				Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
						new Class[] { obj.getClass() });

				Object value = getAttrVal(list.get(i), attMap.get(Integer.toString(j)), attrType);
				if (null == value) {
					value = "";
				}
				row.createCell(j).setCellValue(value.toString());
				// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style.setAlignment(HorizontalAlignment.CENTER);
			}
		}

		// 输出Excel文件
		try {
			FileOutputStream out = new FileOutputStream(outFilePath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("导出失败！" + e);
		} catch (IOException e) {
			throw new IOException("导出失败！" + e);
		}

	}

	/**
	 * exportExcel:(导出Excel)
	 *
	 * @author likaixuan
	 * @param hssfCell
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void exportExcelOutputStream(HttpServletResponse response, String keyValue, List<?> list,
			String classPath, String... fileName) throws Exception {

		Map<String, String> map = getMap(keyValue);
		List<String> keyList = getList(keyValue);

		Class<?> demo = null;
		demo = Class.forName(classPath);
		Object obj = demo.newInstance();
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 声明样式
		HSSFCellStyle style = wb.createCellStyle();
		// 居中显示
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 在sheet里创建第一行为表头，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow rowHeader = sheet.createRow(0);
		// 创建单元格并设置单元格内容

		// 存储属性信息
		Map<String, String> attMap = new HashMap();
		int index = 0;

		for (String key : keyList) {
			rowHeader.createCell(index).setCellValue(key);
			attMap.put(Integer.toString(index), map.get(key).toString());
			index++;
		}

		// 在sheet里创建表头下的数据
		for (int i = 0; i < list.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < map.size(); j++) {
				Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
						new Class[] { obj.getClass() });
				Object value = getAttrVal(list.get(i), attMap.get(Integer.toString(j)), attrType);
				if (null == value) {
					value = "";
				}
				row.createCell(j).setCellValue(value.toString());
				// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style.setAlignment(HorizontalAlignment.CENTER);
			}
		}

		// 输出Excel文件
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = fileName[0];
			if (StringUtils.isEmpty(fileName[0])) {
				newFileName = df.format(new Date());
			}
			OutputStream outstream = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(newFileName.getBytes(), "iso-8859-1") + ".xls");
			response.setContentType("application/x-download");
			wb.write(outstream);
			outstream.close();

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("导出失败！" + e);
		} catch (IOException e) {
			throw new IOException("导出失败！" + e);
		}

	}

	/**
	 * 使用流生成Excel
	 * 
	 * @param file
	 * @param map
	 * @param classPath
	 * @param rowNumIndex
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readXls(byte[] buf, Map map, String classPath, int... rowNumIndex) throws Exception {

		Set keySet = map.keySet();// 返回键的集合

		/** 反射用 **/
		Class<?> demo = null;
		Object obj = null;
		/** 反射用 **/

		List<Object> list = new ArrayList<Object>();
		demo = Class.forName(classPath);

		InputStream is = new ByteArrayInputStream(buf);

		Workbook wb = null;
		
		if (POIFSFileSystem.hasPOIFSHeader(is)) {
            wb = new HSSFWorkbook(is);
        }
        // if (POIXMLDocument.hasOOXMLHeader(is)) {
        //     wb = new XSSFWorkbook(OPCPackage.open(is));
        // }



		// 默认循环所有sheet，如果rowNumIndex[]
		for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {// 获取每个Sheet表

			int rowNum_x = -1;// 记录第x行为表头
			Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
			List<String> headlist = new ArrayList();// 存放所有的表头字段信息

			Sheet hssfSheet = wb.getSheetAt(sheetNum);

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				if (rowNumIndex != null && rowNumIndex.length > 0 && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
					Row hssfRow = hssfSheet.getRow(rowNumIndex[0]);
					if (hssfRow == null) {
						throw new RuntimeException("指定的行为空，请检查");
					}
					rowNum = rowNumIndex[0] - 1;
				}
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				boolean flag = false;
				for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
					if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
						flag = true;
					}
				}
				if (!flag) {
					continue;
				}

				if (rowNum_x == -1) {
					// 循环列Cell
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

						Cell hssfCell = hssfRow.getCell(cellNum);
						if (hssfCell == null) {
							continue;
						}
						String tempCellValue="";
					    boolean isMerge = isMergedRegion(hssfSheet, rowNum,hssfCell.getColumnIndex());
						if(isMerge) {
							tempCellValue = getMergedRegionValue(hssfSheet, hssfRow.getRowNum(), hssfCell.getColumnIndex());
						}
						 
						tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

						tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
						tempCellValue = tempCellValue.trim();

						headlist.add(tempCellValue);
						Iterator it = keySet.iterator();
						while (it.hasNext()) {
							Object key = it.next();
							if (StringUtils.isNotBlank(tempCellValue)
									&& StringUtils.equals(tempCellValue, key.toString())) {
								rowNum_x = rowNum;
								cellmap.put(map.get(key).toString(), cellNum);
							}
						}
						if (rowNum_x == -1) {
							throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
						}
					}

					// 读取到列后，检查表头是否完全一致--start
					for (int i = 0; i < headlist.size(); i++) {
						boolean boo = false;
						Iterator itor = keySet.iterator();
						while (itor.hasNext()) {
							String tempname = itor.next().toString();
							if (tempname.equals(headlist.get(i))) {
								boo = true;
							}
						}
						if (boo == false) {
							throw new Exception("表头字段和定义的属性字段不匹配，请检查");
						}
					}

					Iterator itor = keySet.iterator();
					while (itor.hasNext()) {
						boolean boo = false;
						String tempname = itor.next().toString();
						for (int i = 0; i < headlist.size(); i++) {
							if (tempname.equals(headlist.get(i))) {
								boo = true;
							}
						}
						if (boo == false) {
							throw new Exception("表头字段和定义的属性字段不匹配，请检查");
						}
					}
					// 读取到列后，检查表头是否完全一致--end

				} else {
					obj = demo.newInstance();
					Iterator it = keySet.iterator();
					while (it.hasNext()) {
						Object key = it.next();
						Integer cellNum_x = cellmap.get(map.get(key).toString());
						if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
							continue;
						}
						String attr = map.get(key).toString();// 得到属性

						Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

						Cell cell = hssfRow.getCell(cellNum_x);
						getValue(hssfSheet,cell, obj, attr, attrType, rowNum, cellNum_x, key);

					}
					list.add(obj);
				}

			}
		}
		is.close();
		// wb.close();
		return (List<T>) list;
	}

	/**
	 * String类型日期转为Date类型
	 *
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	public static Date dateConvertFormat(String dateStr) throws ParseException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = format.parse(dateStr);
		return date;
	}

	
	
	/**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
             * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }
    
    
    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == Cell.CELL_TYPE_STRING){

            return cell.getStringCellValue();

        }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }
    
    
	
}

