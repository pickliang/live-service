package io.live_mall.modules.server.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.ImportExcelHelper;
import io.live_mall.modules.server.entity.HistoryDuiFuEntity;
import io.live_mall.modules.server.entity.HistoryDuifuPayEntity;
import io.live_mall.modules.server.model.DuiFuExcelImporter;
import io.live_mall.modules.server.service.HistoryDuiFuService;
import io.live_mall.modules.server.service.HistoryDuifuPayService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.YjViewService;
import io.live_mall.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("excel/")
@Slf4j
public class ExcelController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	YjViewService yjViewService;
	
	
	@Autowired
	SysUserService userservice;
	
	@Value("${temp.path}")
	private String tempPath;
	@Autowired
	private HistoryDuiFuService historyDuiFuService;
	@Autowired
	private HistoryDuifuPayService historyDuifuPayService;
	
	//业绩名单
	 @RequestMapping("/user_import")
	 public void user_map(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
		  	String codedFileName = java.net.URLEncoder.encode("管理人员信息", "UTF-8");
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/user_import.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        hashMap.put("limit", 9999);
	        List<?> list = userservice.queryPage(hashMap).getList();
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	listMap.add(JSONObject.parseObject(JSONObject.toJSONString(object)));
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }
	//业绩名单
	 @RequestMapping("/yj_import")
	 public void yj_map(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
		   String codedFileName = java.net.URLEncoder.encode("业绩信息", "UTF-8");
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/yj.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        hashMap.put("limit", 9999);
	        List<?> list = orderService.selectYjPage(hashMap).getList();
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
	        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
	        	parseObject.put("zbyj",parseObject.get("zbyj") +"万");
	        	parseObject.put("zhyj",parseObject.get("zhyj") +"万");
	        	parseObject.put("nhyj",parseObject.get("nhyj") +"万");
	        	parseObject.put("nhyjcw",parseObject.get("nhyjcw") +"万");
	        	listMap.add(parseObject);
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }
	 
	 
	 
		//、佣金名单、
	 @RequestMapping("/yongjin_import")
	 public void yongjin_import(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
		 String codedFileName = java.net.URLEncoder.encode("佣金信息", "UTF-8");
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/yongjin.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        hashMap.put("limit", 9999);
	        List<?> list = orderService.selectYongjin(hashMap).getList();
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
	        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
	        	parseObject.put("zbyj",parseObject.get("zbyj") +"万");
	        	parseObject.put("zhyj",parseObject.get("zhyj") +"万");
	        	parseObject.put("nhyj",parseObject.get("nhyj") +"万");
	        	if(parseObject.get("yongjin_rate") !=null) {
	        		parseObject.put("yongjin_rate",parseObject.get("yongjin_rate") +"%");
	        	}
	        	parseObject.put("nhyjcw",parseObject.get("nhyjcw") +"万");
	        	listMap.add(parseObject);
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }
	 
	 
	//兑付名单
	 @RequestMapping("/duifu_import")
	 public void duifu_import(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
		 String codedFileName = java.net.URLEncoder.encode("兑付信息", "UTF-8");
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/duifu.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        hashMap.put("limit",9999);
	        List<?> list = orderService.selectDuifuPage(hashMap).getList();
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
	        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
	        	if(parseObject.get("rate") !=null) {
	        		parseObject.put("rate",parseObject.get("rate") +"%");
	        	}
	        	List<JSONObject> payList = parseObject.getJSONArray("orderPayList").toJavaList(JSONObject.class);
	        	for (int i = 0; i < payList.size(); i++) {
	        		parseObject.put("payDate"+(i+1), payList.get(i).get("payDate"));
	        		parseObject.put("payMoney"+(i+1), payList.get(i).get("payMoney"));
				}
	        	listMap.add(parseObject);
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }
	 
	 
	//岗位津贴
	 @RequestMapping("/gwjt_import")
	 public void gwjt_import(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
		 String codedFileName = java.net.URLEncoder.encode("岗位津贴信息", "UTF-8");
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/gwjt.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        hashMap.put("limit", 9999);
	        List<?> list = yjViewService.queryPage(hashMap).getList();
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
	        	parseObject.put("task", parseObject.getString("task")+"万");
	        	parseObject.put("zbyj", parseObject.getString("zbyj")+"万");
	        	parseObject.put("nhyj", parseObject.getString("nhyj")+"万");
	        	if(parseObject.get("jqxs") !=null) {
	        		parseObject.put("jqxs",parseObject.get("jqxs") +"%");
	        	}
	          	if(parseObject.get("wcl") !=null) {
	        		parseObject.put("wcl",parseObject.get("wcl") +"%");
	        	}
	          	
	          	if(parseObject.getInteger("level") == 4) {
	        		parseObject.put("level","城市副总"+parseObject.getInteger("grade")+"级");
	        	}else if(parseObject.getInteger("level") == 3) {
	        		parseObject.put("level","城市总"+parseObject.getInteger("grade")+"级");
	        	}else if(parseObject.getInteger("level") == 2) {
	        		parseObject.put("level","区域总"+parseObject.getInteger("grade")+"级");
	        	}
	        	listMap.add(parseObject);
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }
	 
	 
	//成立名单详情、
	 @RequestMapping("/raise_suc_import")
	 public void raise_suc_import(@RequestParam  Map<String, Object> hashMap,HttpServletResponse response) throws Exception {
	        TemplateExportParams params = new TemplateExportParams(tempPath+"/raise_suc.xlsx");
	        Map<String, Object> map = new HashMap<String, Object>();
	        List<?> list = 	orderService.getOrderSuccess(hashMap);;
	        List<JSONObject> listMap = new ArrayList<JSONObject>();
	        for (Object object : list) {
	        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
	        	map.put("raiseName", parseObject.get("raiseName"));
	        	if(parseObject.getDate("appointTime") !=null) {
	        		parseObject.put("appointTime", DateUtil.formatDateTime(parseObject.getDate("appointTime")));
	        	}
	        	if(parseObject.getDate("aduitTime") !=null) {
	        		parseObject.put("aduitTime", DateUtil.formatDateTime(parseObject.getDate("aduitTime")));
	        	}
	        	listMap.add(parseObject);
			}
	        map.put("list",listMap );
	        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
	        File savefile = new File(tempPath);
	        if (!savefile.exists()) {
	            savefile.mkdirs();
	        }
	        String codedFileName = java.net.URLEncoder.encode(map.get("raiseName")+"成立信息", "UTF-8");
	        //八进制输出流
	        response.setContentType("application/octet-stream");
	        //这后面可以设置导出Excel的名称，此例中名为student.xls
	        response.setHeader("Content-disposition", "attachment;filename="+codedFileName+".xlsx");
	        //刷新缓冲
	        response.flushBuffer();
	        //workbook将Excel写入到response的输出流中，供页面下载
	        workbook.write(response.getOutputStream());
	    }

	/**
	 * 导入历史兑付数据
	 * @param file excel
	 */
	@PostMapping(value = "/import-history-duifu")
	public void historyDuifuImport(@RequestPart("file") MultipartFile file) {
		ImportExcelHelper<DuiFuExcelImporter> helper = new ImportExcelHelper<>();
		List<DuiFuExcelImporter> list = helper.getList(file, DuiFuExcelImporter.class, 0, 1);
		log.info("读取的数据-->{}", JSON.toJSONString(list));
		List<HistoryDuiFuEntity> entities = new ArrayList<>();
		List<HistoryDuifuPayEntity> payEntities = new ArrayList<>();
		list.forEach(record -> {
			HistoryDuiFuEntity entity = new HistoryDuiFuEntity();
			BeanUtils.copyProperties(record, entity);
			entity.setId(String.valueOf(IdWorker.getId()));
			entity.setValueDate(assembleDate(record.getValueDate()));
			entity.setDueDate(assembleDate(record.getDueDate()));
			entity.setEndDate(assembleDate(record.getEndDate()));
			entity.setCreateTime(new Date());
			entities.add(entity);
			payEntities.addAll(assembleHistoryDuifuPay(record, entity.getId()));
		});
		if (!entities.isEmpty()) {
			historyDuiFuService.saveBatch(entities, 200);
		}
		if (!payEntities.isEmpty()) {
			historyDuifuPayService.saveBatch(payEntities, 200);
		}
	}

	private List<HistoryDuifuPayEntity> assembleHistoryDuifuPay(DuiFuExcelImporter record, String historyDuifuId) {
		List<HistoryDuifuPayEntity> payEntities = new ArrayList<>();
		if (StringUtils.isNotBlank(record.getFirstPayMoneyDate())) {
			HistoryDuifuPayEntity payEntity = new HistoryDuifuPayEntity();
			payEntity.setHistoryDuifuId(historyDuifuId);
			payEntity.setPayDate(assembleDate(record.getFirstPayMoneyDate()));
			payEntity.setPayMoney(record.getFirstPayMoney());
			payEntity.setNum(1);
			payEntity.setName("第一次");
			payEntity.setRate(record.getYield());
			payEntities.add(payEntity);
		}
		if (StringUtils.isNotBlank(record.getSecondPayMoneyDate())) {
			HistoryDuifuPayEntity payEntity = new HistoryDuifuPayEntity();
			payEntity.setHistoryDuifuId(historyDuifuId);
			payEntity.setPayDate(assembleDate(record.getSecondPayMoneyDate()));
			payEntity.setPayMoney(record.getSecondPayMoney());
			payEntity.setNum(2);
			payEntity.setName("第二次");
			payEntity.setRate(record.getYield());
			payEntities.add(payEntity);
		}
		if (StringUtils.isNotBlank(record.getThirdPayMoneyDate())) {
			HistoryDuifuPayEntity payEntity = new HistoryDuifuPayEntity();
			payEntity.setHistoryDuifuId(historyDuifuId);
			payEntity.setPayDate(assembleDate(record.getThirdPayMoneyDate()));
			payEntity.setPayMoney(record.getThirdPayMoney());
			payEntity.setNum(3);
			payEntity.setName("第三次");
			payEntity.setRate(record.getYield());
			payEntities.add(payEntity);
		}
		if (StringUtils.isNotBlank(record.getFourthPayMoneyDate())) {
			HistoryDuifuPayEntity payEntity = new HistoryDuifuPayEntity();
			payEntity.setHistoryDuifuId(historyDuifuId);
			payEntity.setPayDate(assembleDate(record.getFourthPayMoneyDate()));
			payEntity.setPayMoney(record.getFourthPayMoney());
			payEntity.setNum(3);
			payEntity.setName("第四次");
			payEntity.setRate(record.getYield());
			payEntities.add(payEntity);
		}
		return payEntities;
	}

	private String assembleDate(String date) {
		if (StringUtils.isBlank(date)) {
			return date;
		}
		String[] dates = date.split(",");
		Map<String, String> map = Maps.newHashMap();
		map.put("一月", "01");
		map.put("二月", "02");
		map.put("三月", "03");
		map.put("四月", "04");
		map.put("五月", "05");
		map.put("六月", "06");
		map.put("七月", "07");
		map.put("八月", "08");
		map.put("九月", "09");
		map.put("十月", "10");
		map.put("十一月", "11");
		map.put("十二月", "12");
		if (dates.length == 3) {
			String year = dates[2].trim();
			String monthAndDay = dates[1];
			String[] splits = monthAndDay.trim().split(" ");
			String month = map.get(splits[0]).trim();
			String day = splits[1].trim();
			return year + "-" + month + "-" + day;
		}
		return dates[0];
	}
}
